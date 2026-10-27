package com.skylink.sksweetsandbakes.data.auth.responsemodel

import com.skylink.sksweetsandbakes.domain.model.UserProfile
import kotlinx.serialization.Serializable

@Serializable
data class FacebookSignInResponseModel(
    val userProfile: UserProfile,
    val isSuccess: Boolean = true
)
