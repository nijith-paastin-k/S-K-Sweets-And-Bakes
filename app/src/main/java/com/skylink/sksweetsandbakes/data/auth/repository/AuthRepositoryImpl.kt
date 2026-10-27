package com.skylink.sksweetsandbakes.data.auth.repository

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.core.firebase.FirebaseConfig
import com.skylink.sksweetsandbakes.core.security.UserRole
import com.skylink.sksweetsandbakes.data.auth.requestmodel.FacebookSignInRequestModel
import com.skylink.sksweetsandbakes.data.auth.requestmodel.GoogleSignInRequestModel
import com.skylink.sksweetsandbakes.data.auth.responsemodel.FacebookSignInResponseModel
import com.skylink.sksweetsandbakes.data.auth.responsemodel.GoogleSignInResponseModel
import com.skylink.sksweetsandbakes.data.local.SKSweetsDatabaseHelper
import com.skylink.sksweetsandbakes.data.local.UserPreferences
import com.skylink.sksweetsandbakes.domain.auth.MicrosoftAuthProvider
import com.skylink.sksweetsandbakes.domain.model.UserProfile
import com.skylink.sksweetsandbakes.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val userPreferences: UserPreferences,
    private val databaseHelper: SKSweetsDatabaseHelper? = null
) : AuthRepository {

    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    override val currentUserProfile: Flow<UserProfile?> = _currentUser.asStateFlow()

    private val _currentRole = MutableStateFlow(UserRole.CUSTOMER)
    override val currentRole: Flow<UserRole> = _currentRole.asStateFlow()

    override val isRememberMeEnabled: Flow<Boolean> = userPreferences.rememberMe
    override val rememberedIdentifier: Flow<String?> = userPreferences.rememberedIdentifier

    private val auth: FirebaseAuth
        get() = FirebaseConfig.auth

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            _currentUser.value = firebaseUser?.toUserProfile()
            _currentRole.value = UserRole.CUSTOMER
        }
    }

    override suspend fun setRememberMe(enabled: Boolean, identifier: String) {
        userPreferences.setRememberMe(enabled, identifier)
    }

    override suspend fun sendOtp(phoneNumber: String): Result<String> {
        if (phoneNumber.length < 10) {
            return Result.Error(IllegalArgumentException("Please enter a valid 10-digit mobile number"))
        }
        return Result.Success("mock_verification_id_${System.currentTimeMillis()}")
    }

    override suspend fun verifyOtp(verificationId: String, code: String): Result<UserProfile> {
        if (code.length != 6) {
            return Result.Error(IllegalArgumentException("Please enter a valid 6-digit OTP"))
        }

        val profile = _currentUser.value ?: UserProfile(
            userId = "usr_${System.currentTimeMillis()}",
            phoneNumber = "+91 98765 43210",
            displayName = "Ramesh Kumar",
            role = "customer"
        )
        _currentUser.value = profile
        userPreferences.saveUserSession(
            userId = profile.userId,
            role = UserRole.fromString(profile.role),
            phone = profile.phoneNumber,
            name = profile.displayName
        )
        return Result.Success(profile)
    }

    override suspend fun updateProfile(
        name: String,
        email: String,
        phone: String?
    ): Result<UserProfile> {
        val current = _currentUser.value ?: UserProfile(
            userId = "usr_${System.currentTimeMillis()}",
            phoneNumber = phone ?: "+91 98765 43210",
            displayName = name,
            email = email,
            role = "customer"
        )
        val updated = current.copy(
            displayName = name,
            email = email,
            phoneNumber = phone ?: current.phoneNumber,
            updatedAt = System.currentTimeMillis()
        )
        _currentUser.value = updated
        userPreferences.saveUserSession(
            userId = updated.userId,
            role = UserRole.fromString(updated.role),
            phone = updated.phoneNumber,
            name = updated.displayName,
            userEmail = updated.email
        )
        databaseHelper?.saveUserProfile(updated)
        return Result.Success(updated)
    }

    override suspend fun signInWithGoogle(request: GoogleSignInRequestModel): Result<GoogleSignInResponseModel> {
        val idToken = request.idToken?.takeIf(String::isNotBlank)
            ?: return Result.Error(IllegalArgumentException("Google did not return an ID token. Check Firebase and OAuth client configuration."))
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val user = auth.signInWithCredential(credential).await().user
                ?: return Result.Error(IllegalStateException("Firebase completed Google sign-in without a user."))
            val profile = persistFirebaseUser(user)
            Result.Success(GoogleSignInResponseModel(userProfile = profile, isSuccess = true))
        } catch (error: Exception) {
            Result.Error(error, providerErrorMessage(error, "Google"))
        }
    }

    override suspend fun signInWithFacebook(request: FacebookSignInRequestModel): Result<FacebookSignInResponseModel> {
        val accessToken = request.accessToken?.takeIf(String::isNotBlank)
            ?: return Result.Error(IllegalArgumentException("Facebook did not return an access token."))
        return try {
            val credential = FacebookAuthProvider.getCredential(accessToken)
            val user = auth.signInWithCredential(credential).await().user
                ?: return Result.Error(IllegalStateException("Firebase completed Facebook sign-in without a user."))
            val profile = persistFirebaseUser(user)
            Result.Success(FacebookSignInResponseModel(userProfile = profile, isSuccess = true))
        } catch (error: Exception) {
            Result.Error(error, providerErrorMessage(error, "Facebook"))
        }
    }

    private fun providerErrorMessage(error: Exception, providerName: String): String {
        val cause = generateSequence(error as Throwable?) { it.cause }.lastOrNull()
        return when {
            error is FirebaseAuthUserCollisionException || cause is FirebaseAuthUserCollisionException ->
                "This $providerName account is already associated with another sign-in method. Sign in with that method to access the existing account."

            error is FirebaseNetworkException || cause is FirebaseNetworkException ->
                "A network error interrupted $providerName sign-in. Check your connection and try again."

            else -> "$providerName sign-in failed. Check your provider configuration and try again."
        }
    }

    override suspend fun signInWithOAuthProvider(
        activity: Activity,
        providerId: String
    ): Result<UserProfile> {
        if (providerId != MicrosoftAuthProvider.FIREBASE_PROVIDER_ID) {
            return Result.Error(IllegalArgumentException("Unsupported OAuth provider: $providerId"))
        }
        return try {
            val provider = buildOAuthProvider(providerId)
            val pendingResult = auth.pendingAuthResult
            val authResult = pendingResult
                ?: auth.startActivityForSignInWithProvider(activity, provider)
            val user = authResult.await().user
                ?: return Result.Error(IllegalStateException("Firebase completed OAuth sign-in without a user."))
            Result.Success(persistFirebaseUser(user))
        } catch (error: Exception) {
            logOAuthFailure(error, providerId)
            Result.Error(error, oauthErrorMessage(error, providerId, isLinking = false))
        }
    }

    override suspend fun linkWithOAuthProvider(
        activity: Activity,
        providerId: String
    ): Result<UserProfile> {
        if (providerId != MicrosoftAuthProvider.FIREBASE_PROVIDER_ID) {
            return Result.Error(IllegalArgumentException("Unsupported account-linking provider."))
        }
        val currentUser = auth.currentUser
            ?: return Result.Error(IllegalStateException("Sign in before linking an account."))
        return try {
            val authResult = auth.pendingAuthResult
                ?: currentUser.startActivityForLinkWithProvider(
                    activity,
                    buildOAuthProvider(providerId)
                )
            val user = authResult.await().user
                ?: return Result.Error(IllegalStateException("Firebase completed account linking without a user."))
            Result.Success(persistFirebaseUser(user))
        } catch (error: Exception) {
            logOAuthFailure(error, providerId)
            Result.Error(error, oauthErrorMessage(error, providerId, isLinking = true))
        }
    }

    private fun logOAuthFailure(error: Exception, providerId: String) {
        val rootCause = generateSequence(error as Throwable?) { it.cause }.lastOrNull()
        val authError = (error as? FirebaseAuthException) ?: (rootCause as? FirebaseAuthException)
        Log.e(
            "OAuthAuth",
            "provider=$providerId code=${authError?.errorCode} msg=${rootCause?.message ?: error.message}"
        )
    }

    private fun buildOAuthProvider(providerId: String): OAuthProvider {
        val builder = OAuthProvider.newBuilder(providerId)
        if (providerId == MicrosoftAuthProvider.FIREBASE_PROVIDER_ID) {
            builder.setScopes(MicrosoftAuthProvider.scopes)
        }
        return builder.build()
    }

    private fun oauthErrorMessage(
        error: Exception,
        providerId: String,
        isLinking: Boolean
    ): String {
        val providerDisplayName =
            if (providerId == MicrosoftAuthProvider.FIREBASE_PROVIDER_ID) "Microsoft" else providerId
        val rootCause = generateSequence(error as Throwable?) { it.cause }.lastOrNull()
        return when {
            error is FirebaseAuthUserCollisionException || rootCause is FirebaseAuthUserCollisionException ->
                "This $providerDisplayName account is already connected to another account. Sign in to that account first."

            error is FirebaseNetworkException || rootCause is FirebaseNetworkException ->
                "A network error interrupted $providerDisplayName authentication. Check your connection and try again."

            error.message?.contains("cancel", ignoreCase = true) == true ->
                "$providerDisplayName authentication was cancelled."

            isLinking ->
                "$providerDisplayName could not be linked. Verify the provider configuration in Firebase Console and try again."

            else ->
                "$providerDisplayName sign-in failed. Verify the provider configuration and your network connection, then try again."
        }
    }

    private suspend fun persistFirebaseUser(user: com.google.firebase.auth.FirebaseUser): UserProfile {
        val profile = user.toUserProfile()
        _currentUser.value = profile
        _currentRole.value = UserRole.CUSTOMER
        userPreferences.saveUserSession(
            userId = profile.userId,
            role = UserRole.CUSTOMER,
            phone = profile.phoneNumber,
            name = profile.displayName,
            userEmail = profile.email
        )
        databaseHelper?.saveUserProfile(profile)
        return profile
    }

    private fun com.google.firebase.auth.FirebaseUser.toUserProfile() = UserProfile(
        userId = uid,
        phoneNumber = phoneNumber.orEmpty(),
        displayName = displayName?.takeIf(String::isNotBlank)
            ?: email?.substringBefore("@").orEmpty(),
        email = email.orEmpty(),
        role = UserRole.CUSTOMER.roleName,
        linkedProviderIds = providerData.mapNotNull { it.providerId }.toSet(),
        avatarUrl = photoUrl?.toString().orEmpty()
    )

    override suspend fun switchRoleForTesting(role: UserRole) {
        _currentRole.value = role
        val current = _currentUser.value
        if (current != null) {
            _currentUser.value = current.copy(role = role.roleName)
        }
        userPreferences.setRole(role)
    }

    override suspend fun signOut() {
        auth.signOut()
        _currentUser.value = null
        _currentRole.value = UserRole.CUSTOMER
        userPreferences.clearSession()
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            auth.currentUser?.delete()?.await()
            signOut()
            Result.Success(Unit)
        } catch (error: Exception) {
            Result.Error(error)
        }
    }
}
