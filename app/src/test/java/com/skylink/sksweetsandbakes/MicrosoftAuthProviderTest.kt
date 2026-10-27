package com.skylink.sksweetsandbakes

import com.skylink.sksweetsandbakes.domain.auth.MicrosoftAuthProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MicrosoftAuthProviderTest {
    @Test
    fun exposesStandardMicrosoftProviderIdAndScopes() {
        assertEquals("microsoft.com", MicrosoftAuthProvider.FIREBASE_PROVIDER_ID)
        val scopes = MicrosoftAuthProvider.scopes.toSet()
        assertTrue(scopes.contains("User.Read"))
        assertTrue(scopes.contains("openid"))
        assertTrue(scopes.contains("profile"))
        assertTrue(scopes.contains("email"))
    }
}
