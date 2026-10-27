package com.skylink.sksweetsandbakes.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.skylink.sksweetsandbakes.core.security.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sk_sweets_prefs")

enum class AppThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

class UserPreferences(private val context: Context) {

    companion object {
        val KEY_USER_ID = stringPreferencesKey("user_id")
        val KEY_USER_ROLE = stringPreferencesKey("user_role")
        val KEY_PHONE_NUMBER = stringPreferencesKey("phone_number")
        val KEY_DISPLAY_NAME = stringPreferencesKey("display_name")
        val KEY_EMAIL = stringPreferencesKey("email")
        val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
        val KEY_REMEMBER_ME = booleanPreferencesKey("remember_me")
        val KEY_REMEMBERED_IDENTIFIER = stringPreferencesKey("remembered_identifier")
    }

    val userId: Flow<String?> = context.dataStore.data.map { it[KEY_USER_ID] }
    val userRole: Flow<UserRole> = context.dataStore.data.map {
        UserRole.fromString(it[KEY_USER_ROLE])
    }
    val displayName: Flow<String?> = context.dataStore.data.map { it[KEY_DISPLAY_NAME] }
    val email: Flow<String?> = context.dataStore.data.map { it[KEY_EMAIL] }
    val phoneNumber: Flow<String?> = context.dataStore.data.map { it[KEY_PHONE_NUMBER] }
    val rememberMe: Flow<Boolean> = context.dataStore.data.map { it[KEY_REMEMBER_ME] ?: false }
    val rememberedIdentifier: Flow<String?> =
        context.dataStore.data.map { it[KEY_REMEMBERED_IDENTIFIER] }

    val themeMode: Flow<AppThemeMode> = context.dataStore.data.map { prefs ->
        when (prefs[KEY_THEME_MODE]) {
            "LIGHT" -> AppThemeMode.LIGHT
            "DARK" -> AppThemeMode.DARK
            else -> AppThemeMode.SYSTEM
        }
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        context.dataStore.edit { prefs ->
            prefs[KEY_THEME_MODE] = mode.name
        }
    }

    suspend fun setRememberMe(enabled: Boolean, identifier: String = "") {
        context.dataStore.edit { prefs ->
            prefs[KEY_REMEMBER_ME] = enabled
            if (enabled && identifier.isNotBlank()) {
                prefs[KEY_REMEMBERED_IDENTIFIER] = identifier
            } else if (!enabled) {
                prefs.remove(KEY_REMEMBERED_IDENTIFIER)
            }
        }
    }

    suspend fun saveUserSession(
        userId: String,
        role: UserRole,
        phone: String,
        name: String,
        userEmail: String = ""
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = userId
            prefs[KEY_USER_ROLE] = role.roleName
            prefs[KEY_PHONE_NUMBER] = phone
            prefs[KEY_DISPLAY_NAME] = name
            if (userEmail.isNotBlank()) {
                prefs[KEY_EMAIL] = userEmail
            }
        }
    }

    suspend fun setRole(role: UserRole) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_ROLE] = role.roleName
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_USER_ID)
            prefs.remove(KEY_USER_ROLE)
            prefs.remove(KEY_PHONE_NUMBER)
            prefs.remove(KEY_DISPLAY_NAME)
            prefs.remove(KEY_EMAIL)
            prefs.remove(KEY_REMEMBER_ME)
            prefs.remove(KEY_REMEMBERED_IDENTIFIER)
        }
    }
}
