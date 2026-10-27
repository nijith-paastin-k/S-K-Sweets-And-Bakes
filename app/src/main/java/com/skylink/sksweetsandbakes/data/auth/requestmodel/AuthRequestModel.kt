package com.skylink.sksweetsandbakes.data.auth.requestmodel

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestModel(
    val identifier: String,
    val passwordOrOtp: String? = null
)

@Serializable
data class SendOtpRequestModel(
    val phoneNumber: String
)

@Serializable
data class VerifyOtpRequestModel(
    val verificationId: String,
    val code: String
)

@Serializable
data class UpdateProfileRequestModel(
    val name: String,
    val email: String,
    val phone: String? = null
)

@Serializable
data class GoogleSignInRequestModel(
    val idToken: String? = null,
    val email: String? = null,
    val displayName: String? = null
)
typealias SendOtpRequest = SendOtpRequestModel
typealias VerifyOtpRequest = VerifyOtpRequestModel
typealias UpdateProfileRequest = UpdateProfileRequestModel
typealias GoogleSignInRequest = GoogleSignInRequestModel
