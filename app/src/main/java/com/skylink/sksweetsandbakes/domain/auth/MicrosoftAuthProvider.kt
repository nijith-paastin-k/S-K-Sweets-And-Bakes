package com.skylink.sksweetsandbakes.domain.auth

object MicrosoftAuthProvider {
    const val FIREBASE_PROVIDER_ID = "microsoft.com"
    val scopes = listOf("User.Read", "openid", "profile", "email")
}
