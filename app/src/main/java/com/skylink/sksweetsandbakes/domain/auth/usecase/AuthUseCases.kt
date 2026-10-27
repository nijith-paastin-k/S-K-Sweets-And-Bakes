package com.skylink.sksweetsandbakes.domain.auth.usecase

import android.app.Activity
import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.core.security.UserRole
import com.skylink.sksweetsandbakes.data.auth.requestmodel.FacebookSignInRequestModel
import com.skylink.sksweetsandbakes.data.auth.requestmodel.GoogleSignInRequestModel
import com.skylink.sksweetsandbakes.data.auth.responsemodel.FacebookSignInResponseModel
import com.skylink.sksweetsandbakes.data.auth.responsemodel.GoogleSignInResponseModel
import com.skylink.sksweetsandbakes.domain.model.UserProfile
import com.skylink.sksweetsandbakes.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<UserProfile?> = authRepository.currentUserProfile
    fun getRole(): Flow<UserRole> = authRepository.currentRole
    fun isRememberMeEnabled(): Flow<Boolean> = authRepository.isRememberMeEnabled
    fun rememberedIdentifier(): Flow<String?> = authRepository.rememberedIdentifier
}

class SignInWithPhoneUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String): Result<String> =
        authRepository.sendOtp(phoneNumber)
}

class VerifyOtpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(verificationId: String, code: String): Result<UserProfile> =
        authRepository.verifyOtp(verificationId, code)
}

class UpdateUserProfileUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        phone: String? = null
    ): Result<UserProfile> =
        authRepository.updateProfile(name, email, phone)
}

class SignInWithGoogleUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(request: GoogleSignInRequestModel): Result<GoogleSignInResponseModel> =
        authRepository.signInWithGoogle(request)
}

class SignInWithFacebookUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(request: FacebookSignInRequestModel): Result<FacebookSignInResponseModel> =
        authRepository.signInWithFacebook(request)
}

class SignInWithOAuthProviderUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(activity: Activity, providerId: String): Result<UserProfile> =
        authRepository.signInWithOAuthProvider(activity, providerId)
}

class LinkOAuthProviderUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(activity: Activity, providerId: String): Result<UserProfile> =
        authRepository.linkWithOAuthProvider(activity, providerId)
}

class SwitchRoleUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(role: UserRole) = authRepository.switchRoleForTesting(role)
}

class SignOutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.signOut()
}

class SetRememberMeUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(enabled: Boolean, identifier: String = "") =
        authRepository.setRememberMe(enabled, identifier)
}
