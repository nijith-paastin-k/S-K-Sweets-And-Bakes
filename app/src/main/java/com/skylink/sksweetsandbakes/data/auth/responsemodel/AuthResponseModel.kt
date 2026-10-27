package com.skylink.sksweetsandbakes.data.auth.responsemodel

import com.skylink.sksweetsandbakes.domain.model.UserProfile
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseModel(
    val userProfile: UserProfile,
    val sessionToken: String? = null,
    val isSuccess: Boolean = true
)

@Serializable
data class SendOtpResponseModel(
    val verificationId: String,
    val isSuccess: Boolean = true
)

@Serializable
data class VerifyOtpResponseModel(
    val userProfile: UserProfile,
    val sessionToken: String? = null
)

@Serializable
data class UpdateProfileResponseModel(
    val userProfile: UserProfile,
    val isUpdated: Boolean = true
)

@Serializable
data class GoogleSignInResponseModel(
    val userProfile: UserProfile,
    val isSuccess: Boolean = true
)
typealias SendOtpResponse = SendOtpResponseModel
typealias VerifyOtpResponse = VerifyOtpResponseModel
typealias UpdateProfileResponse = UpdateProfileResponseModel
typealias GoogleSignInResponse = GoogleSignInResponseModel
