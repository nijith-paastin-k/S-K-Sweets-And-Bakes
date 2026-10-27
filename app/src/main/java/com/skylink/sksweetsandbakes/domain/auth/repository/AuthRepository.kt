package com.skylink.sksweetsandbakes.domain.auth.repository

import android.app.Activity
import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.core.security.UserRole
import com.skylink.sksweetsandbakes.data.auth.requestmodel.FacebookSignInRequestModel
import com.skylink.sksweetsandbakes.data.auth.requestmodel.GoogleSignInRequestModel
import com.skylink.sksweetsandbakes.data.auth.responsemodel.FacebookSignInResponseModel
import com.skylink.sksweetsandbakes.data.auth.responsemodel.GoogleSignInResponseModel
import com.skylink.sksweetsandbakes.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUserProfile: Flow<UserProfile?>
    val currentRole: Flow<UserRole>
    val isRememberMeEnabled: Flow<Boolean>
    val rememberedIdentifier: Flow<String?>
    suspend fun setRememberMe(enabled: Boolean, identifier: String = "")
    suspend fun sendOtp(phoneNumber: String): Result<String>
    suspend fun verifyOtp(verificationId: String, code: String): Result<UserProfile>
    suspend fun updateProfile(
        name: String,
        email: String,
        phone: String? = null
    ): Result<UserProfile>

    suspend fun signInWithGoogle(request: GoogleSignInRequestModel): Result<GoogleSignInResponseModel>
    suspend fun signInWithFacebook(request: FacebookSignInRequestModel): Result<FacebookSignInResponseModel>
    suspend fun signInWithOAuthProvider(activity: Activity, providerId: String): Result<UserProfile>
    suspend fun linkWithOAuthProvider(activity: Activity, providerId: String): Result<UserProfile>
    suspend fun switchRoleForTesting(role: UserRole)
    suspend fun signOut()
    suspend fun deleteAccount(): Result<Unit>
}
