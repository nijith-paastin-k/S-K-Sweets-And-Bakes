package com.skylink.sksweetsandbakes.presentation.auth.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skylink.sksweetsandbakes.core.common.Result
import com.skylink.sksweetsandbakes.core.security.UserRole
import com.skylink.sksweetsandbakes.data.auth.requestmodel.FacebookSignInRequestModel
import com.skylink.sksweetsandbakes.data.auth.requestmodel.GoogleSignInRequestModel
import com.skylink.sksweetsandbakes.domain.auth.usecase.GetCurrentUserUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.LinkOAuthProviderUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.SetRememberMeUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.SignInWithFacebookUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.SignInWithGoogleUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.SignInWithOAuthProviderUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.SignInWithPhoneUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.SignOutUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.SwitchRoleUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.UpdateUserProfileUseCase
import com.skylink.sksweetsandbakes.domain.auth.usecase.VerifyOtpUseCase
import com.skylink.sksweetsandbakes.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val phoneNumber: String = "",
    val otpCode: String = "",
    val verificationId: String? = null,
    val userProfile: UserProfile? = null,
    val userRole: UserRole = UserRole.CUSTOMER,
    val errorMessage: String? = null,
    val isOtpSent: Boolean = false,
    val isOtpVerified: Boolean = false
)

class AuthViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signInWithPhoneUseCase: SignInWithPhoneUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val switchRoleUseCase: SwitchRoleUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val setRememberMeUseCase: SetRememberMeUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase? = null,
    private val signInWithFacebookUseCase: SignInWithFacebookUseCase? = null,
    private val signInWithOAuthProviderUseCase: SignInWithOAuthProviderUseCase? = null,
    private val linkOAuthProviderUseCase: LinkOAuthProviderUseCase? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val isRememberMeEnabled: Flow<Boolean> = getCurrentUserUseCase.isRememberMeEnabled()
    val rememberedIdentifier: Flow<String?> = getCurrentUserUseCase.rememberedIdentifier()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { profile ->
                _uiState.value = _uiState.value.copy(
                    userProfile = profile,
                    userRole = if (profile != null) UserRole.fromString(profile.role) else UserRole.CUSTOMER
                )
            }
        }
        viewModelScope.launch {
            getCurrentUserUseCase.getRole().collect { role ->
                _uiState.value = _uiState.value.copy(userRole = role)
            }
        }
    }

    fun setRememberMe(enabled: Boolean, identifier: String = "") {
        viewModelScope.launch {
            setRememberMeUseCase(enabled, identifier)
        }
    }

    fun onPhoneChanged(phone: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phone, errorMessage = null)
    }

    fun onOtpChanged(otp: String) {
        _uiState.value = _uiState.value.copy(otpCode = otp, errorMessage = null)
    }

    fun sendOtp(phoneOrEmail: String, onSuccess: (verificationId: String) -> Unit) {
        val identifier = phoneOrEmail.trim()
        if (identifier.length < 5) {
            _uiState.value =
                _uiState.value.copy(errorMessage = "Enter a valid mobile number or email")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            when (val res = signInWithPhoneUseCase(identifier)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        phoneNumber = identifier,
                        verificationId = res.data,
                        isOtpSent = true
                    )
                    onSuccess(res.data)
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = res.message
                    )
                }

                is Result.Loading -> {}
            }
        }
    }

    fun sendOtp(onSuccess: (verificationId: String) -> Unit) {
        sendOtp(_uiState.value.phoneNumber, onSuccess)
    }

    fun verifyOtp(onSuccess: () -> Unit) {
        val code = _uiState.value.otpCode.trim()
        val verificationId = _uiState.value.verificationId ?: "mock_verification_id"
        if (code.length != 6) {
            _uiState.value = _uiState.value.copy(errorMessage = "Enter a 6-digit verification code")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            when (val res = verifyOtpUseCase(verificationId, code)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isOtpVerified = true,
                        userProfile = res.data
                    )
                    onSuccess()
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = res.message
                    )
                }

                is Result.Loading -> {}
            }
        }
    }

    fun updateProfile(name: String, email: String, phone: String? = null, onSuccess: () -> Unit) {
        if (name.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Name cannot be empty")
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            when (val res = updateUserProfileUseCase(name, email, phone)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, userProfile = res.data)
                    onSuccess()
                }

                is Result.Error -> {
                    _uiState.value =
                        _uiState.value.copy(isLoading = false, errorMessage = res.message)
                }

                is Result.Loading -> {}
            }
        }
    }

    fun signInWithFacebook(
        request: FacebookSignInRequestModel,
        onSuccess: (UserProfile) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        Log.d(
            "SKSweetsFacebookAuth",
            "[VM.signInWithFacebook] Initiating with email='${request.email}', name='${request.displayName}'"
        )
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val useCase = signInWithFacebookUseCase
            if (useCase == null) {
                val message = "Facebook sign-in is not configured."
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message)
                onError(message)
                return@launch
            }
            when (val res = useCase(request)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        userProfile = res.data.userProfile
                    )
                    onSuccess(res.data.userProfile)
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = res.message
                    )
                    onError(res.message)
                }

                is Result.Loading -> {}
            }
        }
    }

    fun signInWithGoogle(
        request: GoogleSignInRequestModel,
        onSuccess: (UserProfile) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        Log.d(
            "SKSweetsGoogleAuth",
            "[VM.signInWithGoogle] Initiating with email='${request.email}', name='${request.displayName}'"
        )
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val useCase = signInWithGoogleUseCase
            if (useCase == null) {
                val message = "Google sign-in is not configured."
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message)
                onError(message)
                return@launch
            }
            when (val res = useCase(request)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        userProfile = res.data.userProfile
                    )
                    onSuccess(res.data.userProfile)
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = res.message
                    )
                    onError(res.message)
                }

                is Result.Loading -> {}
            }
        }
    }

    fun signInWithOAuthProvider(
        activity: Activity,
        providerId: String,
        onSuccess: (UserProfile) -> Unit,
        onError: (String) -> Unit
    ) {
        val useCase = signInWithOAuthProviderUseCase
        if (useCase == null) {
            val message = "OAuth sign-in is not configured."
            _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message)
            onError(message)
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            when (val result = useCase(activity, providerId)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        userProfile = result.data
                    )
                    onSuccess(result.data)
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                    onError(result.message)
                }

                is Result.Loading -> Unit
            }
        }
    }

    fun linkWithOAuthProvider(
        activity: Activity,
        providerId: String,
        onSuccess: (UserProfile) -> Unit,
        onError: (String) -> Unit
    ) {
        val useCase = linkOAuthProviderUseCase
        if (useCase == null) {
            val message = "Account linking is not configured."
            _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = message)
            onError(message)
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            when (val result = useCase(activity, providerId)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        userProfile = result.data
                    )
                    onSuccess(result.data)
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                    onError(result.message)
                }

                is Result.Loading -> Unit
            }
        }
    }

    fun switchRoleForTesting(role: UserRole) {
        viewModelScope.launch {
            switchRoleUseCase(role)
        }
    }

    fun signOut(onComplete: () -> Unit) {
        viewModelScope.launch {
            signOutUseCase()
            _uiState.value = AuthUiState()
            onComplete()
        }
    }
}
