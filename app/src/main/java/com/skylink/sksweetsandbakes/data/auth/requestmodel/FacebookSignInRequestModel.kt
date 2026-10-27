package com.skylink.sksweetsandbakes.data.auth.requestmodel

import kotlinx.serialization.Serializable

@Serializable
data class FacebookSignInRequestModel(
    val accessToken: String? = null,
    val userId: String? = null,
    val email: String? = null,
    val displayName: String? = null,
    val profilePictureUri: String? = null
)
