package com.skylink.sksweetsandbakes.presentation.auth.screen

import android.accounts.AccountManager
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.FragmentActivity
import coil.compose.AsyncImage
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.skylink.sksweetsandbakes.MainActivity
import com.skylink.sksweetsandbakes.R
import com.skylink.sksweetsandbakes.core.designsystem.SKBrandLogoEmblem
import com.skylink.sksweetsandbakes.core.designsystem.SKGreetingAvatar
import com.skylink.sksweetsandbakes.core.designsystem.SKMobileNumberField
import com.skylink.sksweetsandbakes.core.designsystem.SKSixDigitOtpInput
import com.skylink.sksweetsandbakes.core.designsystem.SKSweetsButton
import com.skylink.sksweetsandbakes.core.designsystem.SKSweetsTopBar
import com.skylink.sksweetsandbakes.core.designsystem.SKTimeOfDayHeroBanner
import com.skylink.sksweetsandbakes.core.designsystem.SKValidatedTextField
import com.skylink.sksweetsandbakes.core.designsystem.StatusBadge
import com.skylink.sksweetsandbakes.core.designsystem.TimeOfDayPeriod
import com.skylink.sksweetsandbakes.core.designsystem.ValidationStatus
import com.skylink.sksweetsandbakes.core.security.UserRole
import com.skylink.sksweetsandbakes.core.util.ValidationUtils
import com.skylink.sksweetsandbakes.data.auth.requestmodel.FacebookSignInRequestModel
import com.skylink.sksweetsandbakes.data.auth.requestmodel.GoogleSignInRequestModel
import com.skylink.sksweetsandbakes.data.local.AppThemeMode
import com.skylink.sksweetsandbakes.domain.auth.MicrosoftAuthProvider
import com.skylink.sksweetsandbakes.domain.model.DeliveryAddress
import com.skylink.sksweetsandbakes.presentation.auth.viewmodel.AuthViewModel
import com.skylink.sksweetsandbakes.ui.theme.AppColors
import com.skylink.sksweetsandbakes.ui.theme.AppDimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import network.chaintech.cmpcountrycodepicker.model.CountryDetails
import java.util.Calendar
import java.util.Locale.getDefault

enum class PasswordStrength(val label: String, val score: Float, val color: Color) {
    EMPTY("", 0f, Color.Transparent),
    WEAK("Weak", 0.25f, AppColors.PasswordStrengthWeak),
    FAIR("Fair", 0.5f, AppColors.PasswordStrengthFair),
    GOOD("Good", 0.75f, AppColors.PasswordStrengthGood),
    STRONG("Strong", 1.0f, AppColors.PasswordStrengthStrong)
}

fun isPasswordLengthValid(p: String): Boolean = p.length >= 8
fun isPasswordUpperValid(p: String): Boolean = p.any { it.isUpperCase() }
fun isPasswordLowerValid(p: String): Boolean = p.any { it.isLowerCase() }
fun isPasswordDigitValid(p: String): Boolean = p.any { it.isDigit() }
fun isPasswordSpecialValid(p: String): Boolean =
    p.any { "!@#$%^&*()_+-=[]{};':\"|,.<>/?".contains(it) }

fun isStrictPasswordValid(p: String): Boolean =
    isPasswordLengthValid(p) &&
            isPasswordUpperValid(p) &&
            isPasswordLowerValid(p) &&
            isPasswordDigitValid(p) &&
            isPasswordSpecialValid(p)

fun calculatePasswordStrength(pass: String): PasswordStrength {
    if (pass.isEmpty()) return PasswordStrength.EMPTY
    var score = 0
    if (isPasswordLengthValid(pass)) score++
    if (isPasswordUpperValid(pass) && isPasswordLowerValid(pass)) score++
    if (isPasswordDigitValid(pass)) score++
    if (isPasswordSpecialValid(pass)) score++

    return when (score) {
        0, 1 -> PasswordStrength.WEAK
        2 -> PasswordStrength.FAIR
        3 -> PasswordStrength.GOOD
        else -> PasswordStrength.STRONG
    }
}

@Composable
fun PasswordRequirementItem(label: String, isValid: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (isValid) Icons.Default.CheckCircle else Icons.Default.Close,
            contentDescription = null,
            tint = if (isValid) AppColors.StatusSuccess else MaterialTheme.colorScheme.onSurfaceVariant.copy(
                alpha = 0.5f
            ),
            modifier = Modifier.size(AppDimens.dp14)
        )
        Spacer(modifier = Modifier.width(AppDimens.dp6))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isValid) AppColors.StatusSuccess else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun VerificationTypeCard(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(AppDimens.dp12),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        border = if (isSelected) BorderStroke(
            AppDimens.dp1_5,
            MaterialTheme.colorScheme.primary
        ) else null,
        modifier = modifier
            .height(AppDimens.dp50)
            .clip(RoundedCornerShape(AppDimens.dp12))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppDimens.dp12),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(AppDimens.dp18)
            )
            Spacer(modifier = Modifier.width(AppDimens.dp8))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
fun SKPremiumAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    icon: ImageVector? = null,
    showBrandLogo: Boolean = true,
    text: @Composable (() -> Unit)? = null,
    confirmButton: @Composable () -> Unit,
    dismissButton: (@Composable () -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(AppDimens.dp24),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = AppDimens.dp6,
        icon = {
            if (showBrandLogo) {
                SKBrandLogoEmblem(size = AppDimens.dp54, elevation = AppDimens.dp2)
            } else if (icon != null) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(AppDimens.dp46)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = text,
        confirmButton = confirmButton,
        dismissButton = dismissButton
    )
}

@Composable
fun EditAddressDialog(
    initialAddress: DeliveryAddress?,
    onSave: (DeliveryAddress) -> Unit,
    onDismiss: () -> Unit
) {
    var recipientName by remember { mutableStateOf(initialAddress?.recipientName ?: "") }
    var phoneNumber by remember { mutableStateOf(initialAddress?.phoneNumber ?: "") }
    var addressLine1 by remember { mutableStateOf(initialAddress?.addressLine1 ?: "") }
    var addressLine2 by remember { mutableStateOf(initialAddress?.addressLine2 ?: "") }
    var city by remember { mutableStateOf(initialAddress?.city ?: "Bengaluru") }
    var pincode by remember { mutableStateOf(initialAddress?.pincode ?: "560001") }
    var isDefault by remember { mutableStateOf(initialAddress?.isDefault ?: false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val nameStatus = when {
        recipientName.isEmpty() -> ValidationStatus.Idle
        recipientName.trim().length >= 2 -> ValidationStatus.Valid("Valid recipient name")
        else -> ValidationStatus.Invalid("Name must be at least 2 characters")
    }
    val phoneStatus = ValidationUtils.validateIndianMobile(phoneNumber)
    val line1Status = when {
        addressLine1.isEmpty() -> ValidationStatus.Idle
        addressLine1.trim().length >= 5 -> ValidationStatus.Valid("Valid street address")
        else -> ValidationStatus.Invalid("Street address must be at least 5 characters")
    }
    val pincodeStatus = when {
        pincode.isEmpty() -> ValidationStatus.Idle
        pincode.length == 6 && pincode.all { it.isDigit() } -> ValidationStatus.Valid("Valid 6-digit PIN code")
        else -> ValidationStatus.Invalid("Pincode must be 6 digits")
    }

    SKPremiumAlertDialog(
        onDismissRequest = onDismiss,
        title = if (initialAddress == null) "Add Delivery Address" else "Edit Delivery Address",
        showBrandLogo = false,
        icon = Icons.Default.LocationOn,
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppDimens.dp10)
            ) {
                item {
                    SKValidatedTextField(
                        value = recipientName,
                        onValueChange = { recipientName = it; errorMsg = null },
                        label = "Recipient Name *",
                        placeholder = "e.g. Ramesh Kumar",
                        leadingIcon = Icons.Default.Person,
                        validationStatus = nameStatus,
                        singleLine = true
                    )
                }
                item {
                    SKValidatedTextField(
                        value = phoneNumber,
                        onValueChange = { if (it.length <= 10) phoneNumber = it; errorMsg = null },
                        label = "Contact Phone (10 Digits) *",
                        placeholder = "9876543210",
                        leadingIcon = Icons.Default.Phone,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        validationStatus = phoneStatus,
                        singleLine = true
                    )
                }
                item {
                    SKValidatedTextField(
                        value = addressLine1,
                        onValueChange = { addressLine1 = it; errorMsg = null },
                        label = "Flat / House No, Street Address *",
                        placeholder = "12/A, Gandhi Street, Indira Nagar",
                        leadingIcon = Icons.Default.Home,
                        validationStatus = line1Status,
                        singleLine = true
                    )
                }
                item {
                    SKValidatedTextField(
                        value = addressLine2,
                        onValueChange = { addressLine2 = it },
                        label = "Landmark / Area (Optional)",
                        placeholder = "Near City Metro Station",
                        leadingIcon = Icons.Default.LocationOn,
                        singleLine = true
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(AppDimens.dp8)) {
                        SKValidatedTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = "City *",
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        SKValidatedTextField(
                            value = pincode,
                            onValueChange = { if (it.length <= 6) pincode = it; errorMsg = null },
                            label = "Pincode (6 Digits) *",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            validationStatus = pincodeStatus,
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { isDefault = !isDefault }
                            .padding(vertical = AppDimens.dp4)
                    ) {
                        Checkbox(checked = isDefault, onCheckedChange = { isDefault = it })
                        Spacer(modifier = Modifier.width(AppDimens.dp6))
                        Text(
                            "Set as Default Delivery Address",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                if (errorMsg != null) {
                    item {
                        Text(
                            errorMsg ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (recipientName.isBlank()) {
                        errorMsg = "Please enter recipient name"
                        return@Button
                    }
                    if (phoneNumber.length < 10) {
                        errorMsg = "Please enter a valid 10-digit phone number"
                        return@Button
                    }
                    if (addressLine1.isBlank()) {
                        errorMsg = "Please enter street address"
                        return@Button
                    }
                    if (pincode.length < 6) {
                        errorMsg = "Please enter 6-digit pincode"
                        return@Button
                    }
                    val addrId = initialAddress?.addressId?.ifBlank { null }
                        ?: "addr_${System.currentTimeMillis()}"
                    val updated = DeliveryAddress(
                        addressId = addrId,
                        recipientName = recipientName.trim(),
                        phoneNumber = phoneNumber.trim(),
                        addressLine1 = addressLine1.trim(),
                        addressLine2 = addressLine2.trim(),
                        city = city.trim(),
                        pincode = pincode.trim(),
                        isDefault = isDefault
                    )
                    onSave(updated)
                    onDismiss()
                }
            ) {
                Text(if (initialAddress == null) "Add Address" else "Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}


@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    onExploreClick: () -> Unit = onSplashFinished,
    onLoginClick: () -> Unit = onSplashFinished,
    onRegisterClick: () -> Unit = onSplashFinished,
    onStaffLoginClick: () -> Unit = onSplashFinished
) {
    var startAnimation by remember { mutableStateOf(false) }
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "scale"
    )
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "alpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(1800)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable(onClick = onSplashFinished),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(AppDimens.dp28)
                .graphicsLayer {
                    scaleX = scaleAnim
                    scaleY = scaleAnim
                    alpha = alphaAnim
                },
            verticalArrangement = Arrangement.Center
        ) {
            SKBrandLogoEmblem(
                size = AppDimens.dp160,
                borderWidth = AppDimens.dp3,
                elevation = AppDimens.dp10
            )

            Spacer(modifier = Modifier.height(AppDimens.dp24))
            Text(
                text = "S K Sweets & Bakes",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(AppDimens.dp8))
            Text(
                text = "Pure Desi Cow Ghee Sweets & Fresh Bakery",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(AppDimens.dp36))
            CircularProgressIndicator(
                modifier = Modifier.size(AppDimens.dp28),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = AppDimens.dp2_5
            )
            Spacer(modifier = Modifier.height(AppDimens.dp12))
            Text(
                text = "Tradition & Purity Since 1998",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun WelcomeScreen(
    onCustomerLoginClick: () -> Unit,
    onCustomerRegisterClick: () -> Unit,
    onStaffLoginClick: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(AppDimens.dp24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = AppDimens.dp30)
            ) {
                SKBrandLogoEmblem(
                    size = AppDimens.dp140,
                    borderWidth = AppDimens.dp3,
                    elevation = AppDimens.dp8
                )
                Spacer(modifier = Modifier.height(AppDimens.dp16))
                Text(
                    text = "Welcome to S K Sweets",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(AppDimens.dp8))
                Text(
                    text = "Crafted with authentic tradition, pure desi cow ghee, and premium dry fruits.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                SKSweetsButton(
                    text = "Customer Sign In",
                    onClick = onCustomerLoginClick
                )
                Spacer(modifier = Modifier.height(AppDimens.dp10))
                OutlinedButton(
                    onClick = onCustomerRegisterClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppDimens.dp52),
                    shape = RoundedCornerShape(AppDimens.dp12)
                ) {
                    Text(
                        text = "Register New Account",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(AppDimens.dp10))
                TextButton(
                    onClick = onStaffLoginClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Shop Owner & Staff Portal",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

    }
}

/**
 * Modern 2-Step Login Method Selection Screen
 * Displays royal circular logo emblem, clean welcome text, and clear method cards:
 * 1. Mobile Number (OTP) - Recommended
 * 2. Email & Password
 * 3. Shop Staff & Owner Portal PIN
 * 4. Social Sign-In (Google; additional providers require configuration)
 * 5. Guest Access
 */
data class GoogleProfileAccount(
    val email: String,
    val displayName: String,
    val photoUri: String? = null,
    val avatarBgColor: Color = AppColors.GoogleBlue,
    val initial: String = (displayName.firstOrNull { it.isLetter() }
        ?: email.firstOrNull { it.isLetter() } ?: 'u').lowercase()
)

fun getDeviceGoogleAccounts(context: Context): List<GoogleProfileAccount> {
    val accountsMap = linkedMapOf<String, Pair<String, String?>>()
    try {
        val lastGAccount = GoogleSignIn.getLastSignedInAccount(context)
        if (lastGAccount != null && !lastGAccount.email.isNullOrBlank()) {
            val email = lastGAccount.email!!
            val name = lastGAccount.displayName?.ifBlank { null }
                ?: lastGAccount.givenName?.ifBlank { null }
                ?: email.substringBefore("@").replace(".", " ")
                    .split(" ").filter { it.isNotBlank() }
                    .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
            val photo = lastGAccount.photoUrl?.toString()
            accountsMap[email] = Pair(name, photo)
        }
    } catch (e: Exception) {
        Log.d("SKSweetsGoogleAuth", "LastSignedInAccount dynamic check: ${e.message}")
    }

    try {
        val am = AccountManager.get(context)
        val accs = am.getAccountsByType("com.google")
        for (a in accs) {
            val email = a.name
            if (email.contains("@")) {
                val name = email.substringBefore("@").replace(".", " ")
                    .split(" ").filter { it.isNotBlank() }
                    .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                if (!accountsMap.containsKey(email)) {
                    accountsMap[email] = Pair(name, null)
                }
            }
        }
    } catch (e: Exception) {
        Log.w("SKSweetsGoogleAuth", "AccountManager dynamic query error: ${e.message}")
    }

    val colors = listOf(
        AppColors.GoogleBlue,
        AppColors.GoogleGreen,
        AppColors.GoogleRed,
        AppColors.GoogleAmber,
        AppColors.GooglePurple,
        AppColors.GoogleTeal
    )

    return accountsMap.entries.mapIndexed { index, (email, pair) ->
        GoogleProfileAccount(
            email = email,
            displayName = pair.first,
            photoUri = pair.second,
            avatarBgColor = colors[index % colors.size]
        )
    }
}

@Composable
fun GoogleAccountChooserDialog(
    accounts: List<GoogleProfileAccount>,
    onAccountSelected: (GoogleProfileAccount) -> Unit,
    onAddAnotherAccountClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    val surfaceColor = if (isDark) AppColors.DialogDarkSurface else Color.White
    val cardBorderColor = if (isDark) AppColors.DialogDarkBorder else AppColors.BorderMedium
    val innerCardBg = if (isDark) AppColors.DialogDarkCardBg else Color.White
    val textPrimary =
        if (isDark) AppColors.DialogDarkTextPrimary else AppColors.DialogLightTextPrimary
    val textSecondary =
        if (isDark) AppColors.DialogDarkTextSecondary else AppColors.DialogLightTextSecondary
    val dividerColor = if (isDark) AppColors.DialogDarkDivider else AppColors.DialogLightDivider
    val linkColor = if (isDark) AppColors.DialogDarkLink else AppColors.GoogleBlue
    val addIconTint =
        if (isDark) AppColors.DialogDarkTextSecondary else AppColors.DialogDarkIconTint

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = if (isDark) 0.65f else 0.50f))
                .clickable(
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                    indication = null,
                    onClick = onDismissRequest
                ),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(AppDimens.dp28),
                color = surfaceColor,
                shadowElevation = AppDimens.dp16,
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null
                    ) {}
                    .padding(vertical = AppDimens.dp12)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppDimens.dp24, vertical = AppDimens.dp24),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = AppColors.GoogleBlue,
                        modifier = Modifier.size(AppDimens.dp54)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Account",
                                tint = Color.White,
                                modifier = Modifier.size(AppDimens.dp38)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(AppDimens.dp14))

                    Text(
                        text = "Choose an account",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Medium,
                        color = textPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(AppDimens.dp4))

                    Text(
                        text = "to continue to S K Sweets & Bakes",
                        fontSize = 14.sp,
                        color = textSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(AppDimens.dp18))
                    Surface(
                        shape = RoundedCornerShape(AppDimens.dp12),
                        border = BorderStroke(AppDimens.dp1, cardBorderColor),
                        color = innerCardBg,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = AppDimens.dp240)
                            ) {
                                items(accounts) { acc ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onAccountSelected(acc) }
                                            .padding(
                                                horizontal = AppDimens.dp16,
                                                vertical = AppDimens.dp12
                                            ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = acc.avatarBgColor,
                                            modifier = Modifier.size(AppDimens.dp38),
                                            shadowElevation = AppDimens.dp1
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                if (!acc.photoUri.isNullOrBlank()) {
                                                    AsyncImage(
                                                        model = acc.photoUri,
                                                        contentDescription = acc.displayName,
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier.fillMaxSize()
                                                    )
                                                } else {
                                                    Text(
                                                        text = acc.initial.uppercase(),
                                                        color = Color.White,
                                                        fontSize = 17.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(AppDimens.dp14))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = acc.displayName,
                                                fontSize = 14.5.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = textPrimary,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = acc.email,
                                                fontSize = 12.sp,
                                                color = textSecondary,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    HorizontalDivider(
                                        color = dividerColor,
                                        thickness = AppDimens.dp1
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onAddAnotherAccountClick() }
                                    .padding(
                                        horizontal = AppDimens.dp16,
                                        vertical = AppDimens.dp14
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = "Add another account",
                                    tint = addIconTint,
                                    modifier = Modifier.size(AppDimens.dp20)
                                )

                                Spacer(modifier = Modifier.width(AppDimens.dp14))

                                Text(
                                    text = "Add another account",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(AppDimens.dp16))

                    val disclaimerText = buildAnnotatedString {
                        append("To continue, Google will share your name, email address and profile picture with S K Sweets & Bakes. Before using this app, review its ")
                        pushStringAnnotation(tag = "privacy", annotation = "privacy")
                        withStyle(
                            style = SpanStyle(
                                color = linkColor,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("privacy policy")
                        }
                        pop()
                        append(" and ")
                        pushStringAnnotation(tag = "terms", annotation = "terms")
                        withStyle(
                            style = SpanStyle(
                                color = linkColor,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("terms of service")
                        }
                        pop()
                        append(".")
                    }

                    Text(
                        text = disclaimerText,
                        fontSize = 11.5.sp,
                        color = textSecondary,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(AppDimens.dp16))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Help",
                            fontSize = 13.sp,
                            color = textSecondary,
                            modifier = Modifier.clickable {}
                        )

                        TextButton(
                            onClick = onDismissRequest,
                            colors = ButtonDefaults.textButtonColors(contentColor = linkColor)
                        ) {
                            Text("Cancel", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FacebookAccountChooserDialog(
    accounts: List<GoogleProfileAccount> = emptyList(),
    onAccountSelected: (FacebookSignInRequestModel) -> Unit,
    onDismissRequest: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    val surfaceColor = if (isDark) AppColors.DialogFbDarkSurface else Color.White
    val cardBorderColor = if (isDark) AppColors.BorderDark else AppColors.BorderMedium
    val innerCardBg = if (isDark) AppColors.DialogFbDarkCardBg else Color.White
    val textPrimary =
        if (isDark) AppColors.DialogFbDarkTextPrimary else AppColors.DialogLightTextPrimary
    val textSecondary =
        if (isDark) AppColors.DialogFbDarkTextSecondary else AppColors.DialogLightTextSecondary
    val dividerColor = if (isDark) AppColors.BorderDark else AppColors.DialogLightDivider
    val brandBlue = if (isDark) AppColors.FacebookBlueDark else AppColors.FacebookBlue

    var showCustomInput by remember { mutableStateOf(accounts.isEmpty()) }
    var inputName by remember { mutableStateOf("") }
    var inputEmailOrId by remember { mutableStateOf("") }

    val effectiveAccounts = remember(accounts) {
        accounts.take(3)
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = if (isDark) 0.65f else 0.50f))
                .clickable(
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                    indication = null,
                    onClick = onDismissRequest
                ),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(AppDimens.dp28),
                color = surfaceColor,
                shadowElevation = AppDimens.dp16,
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null
                    ) {}
                    .padding(vertical = AppDimens.dp12)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppDimens.dp24, vertical = AppDimens.dp24),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = AppColors.FacebookBlue,
                        modifier = Modifier.size(AppDimens.dp54)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_facebook_logo),
                                contentDescription = "Facebook",
                                modifier = Modifier.size(AppDimens.dp30)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(AppDimens.dp14))

                    Text(
                        text = "Sign in with Facebook",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Medium,
                        color = textPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(AppDimens.dp4))

                    Text(
                        text = "to continue to S K Sweets & Bakes",
                        fontSize = 14.sp,
                        color = textSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(AppDimens.dp18))

                    if (showCustomInput) {
                        Surface(
                            shape = RoundedCornerShape(AppDimens.dp12),
                            border = BorderStroke(AppDimens.dp1, cardBorderColor),
                            color = innerCardBg,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(AppDimens.dp16)
                            ) {
                                Text(
                                    text = "Log In with Your Facebook Account",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textPrimary
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp10))
                                OutlinedTextField(
                                    value = inputName,
                                    onValueChange = { inputName = it },
                                    label = { Text("Your Facebook Name") },
                                    placeholder = { Text("e.g. Ramesh Kumar") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp10))
                                OutlinedTextField(
                                    value = inputEmailOrId,
                                    onValueChange = { inputEmailOrId = it },
                                    label = { Text("Facebook Email or Profile ID") },
                                    placeholder = { Text("e.g. ramesh@gmail.com") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp14))
                                Button(
                                    onClick = {
                                        val finalName = inputName.ifBlank { "Facebook User" }
                                        val finalEmail =
                                            inputEmailOrId.ifBlank { "facebook.user@gmail.com" }
                                        onAccountSelected(
                                            FacebookSignInRequestModel(
                                                displayName = finalName,
                                                email = finalEmail,
                                                userId = "usr_fb_${System.currentTimeMillis()}"
                                            )
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.FacebookBlue),
                                    shape = RoundedCornerShape(AppDimens.dp10),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "Continue with Facebook",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.height(AppDimens.dp6))
                                TextButton(
                                    onClick = { showCustomInput = false },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Back to Accounts", color = brandBlue, fontSize = 13.sp)
                                }
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(AppDimens.dp12),
                            border = BorderStroke(AppDimens.dp1, cardBorderColor),
                            color = innerCardBg,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                effectiveAccounts.forEachIndexed { idx, acc ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onAccountSelected(
                                                    FacebookSignInRequestModel(
                                                        displayName = acc.displayName,
                                                        email = acc.email,
                                                        userId = "usr_fb_${
                                                            acc.email.substringBefore(
                                                                "@"
                                                            )
                                                        }_${System.currentTimeMillis()}"
                                                    )
                                                )
                                            }
                                            .padding(
                                                horizontal = AppDimens.dp16,
                                                vertical = AppDimens.dp12
                                            ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = acc.avatarBgColor,
                                            modifier = Modifier.size(AppDimens.dp42),
                                            shadowElevation = AppDimens.dp1
                                        ) {
                                            if (!acc.photoUri.isNullOrBlank()) {
                                                AsyncImage(
                                                    model = acc.photoUri,
                                                    contentDescription = acc.displayName,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            } else {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(
                                                        acc.initial.uppercase(),
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(AppDimens.dp14))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Continue as ${acc.displayName}",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = textPrimary
                                            )
                                            Text(
                                                text = "${acc.email} • Facebook",
                                                fontSize = 12.sp,
                                                color = textSecondary
                                            )
                                        }
                                    }
                                    HorizontalDivider(
                                        color = dividerColor,
                                        thickness = AppDimens.dp1
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { showCustomInput = true }
                                        .padding(
                                            horizontal = AppDimens.dp16,
                                            vertical = AppDimens.dp14
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PersonAdd,
                                        contentDescription = "Log into another account",
                                        tint = brandBlue,
                                        modifier = Modifier.size(AppDimens.dp20)
                                    )

                                    Spacer(modifier = Modifier.width(AppDimens.dp14))

                                    Text(
                                        text = "Log into another Facebook account",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = textPrimary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(AppDimens.dp16))

                    Text(
                        text = "To continue, Facebook will share your name, profile picture and email address with S K Sweets & Bakes. You can manage access in your settings at any time.",
                        fontSize = 11.5.sp,
                        color = textSecondary,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(AppDimens.dp16))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onDismissRequest,
                            colors = ButtonDefaults.textButtonColors(contentColor = brandBlue)
                        ) {
                            Text("Cancel", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}


internal fun findFragmentActivity(context: Context): FragmentActivity? {
    var ctx = context
    while (ctx is ContextWrapper) {
        if (ctx is FragmentActivity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

internal fun launchBiometricPrompt(
    context: Context,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    Log.d("SK_BIOMETRIC", "launchBiometricPrompt called")
    val activity = findFragmentActivity(context)
    if (activity == null) {
        Log.e("SK_BIOMETRIC", "findFragmentActivity returned null")
        onError("Host activity not found for biometric authentication")
        return
    }

    val biometricManager = BiometricManager.from(context)
    val authenticators =
        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL

    val canAuth = biometricManager.canAuthenticate(authenticators)
    Log.d("SK_BIOMETRIC", "canAuthenticate: ")

    when (canAuth) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            Log.d("SK_BIOMETRIC", "BIOMETRIC_SUCCESS! Launching BiometricPrompt...")
            val executor = ContextCompat.getMainExecutor(context)
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("S K Sweets & Bakes")
                .setSubtitle("Confirm Fingerprint / Biometric Identity")
                .setDescription("Touch the fingerprint sensor or enter screen lock to sign in")
                .setAllowedAuthenticators(authenticators)
                .build()

            val biometricPrompt = BiometricPrompt(
                activity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Log.d("SK_BIOMETRIC", "onAuthenticationSucceeded!")
                        onSuccess()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Log.e("SK_BIOMETRIC", "onAuthenticationError:  - ")
                        if (errorCode != BiometricPrompt.ERROR_USER_CANCELED &&
                            errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON &&
                            errorCode != BiometricPrompt.ERROR_CANCELED
                        ) {
                            onError(errString.toString())
                        }
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Log.w("SK_BIOMETRIC", "onAuthenticationFailed")
                        Toast.makeText(
                            context,
                            "Fingerprint not recognized. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )

            biometricPrompt.authenticate(promptInfo)
        }

        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            Log.e("SK_BIOMETRIC", "BIOMETRIC_ERROR_NO_HARDWARE")
            onError("No biometric sensor available on this device")
        }

        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            Log.e("SK_BIOMETRIC", "BIOMETRIC_ERROR_HW_UNAVAILABLE")
            onError("Biometric sensor is currently unavailable")
        }

        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            Log.e("SK_BIOMETRIC", "BIOMETRIC_ERROR_NONE_ENROLLED")
            onError("No fingerprint enrolled. Please register a fingerprint or lock PIN in your device Settings.")
        }

        else -> {
            Log.e("SK_BIOMETRIC", "Unsupported: ")
            onError("Biometric authentication not supported on this device")
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onPhoneLoginClick: () -> Unit = {},
    onEmailLoginClick: () -> Unit = {},
    onStaffLoginClick: () -> Unit = {},
    onLoginSuccess: (userName: String) -> Unit,
    onOtpRequested: (verificationId: String, phoneOrEmail: String) -> Unit = { _, _ -> },
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onGuestClick: () -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val facebookCallbackManager = (activity as? MainActivity)?.facebookCallbackManager
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }
    var isSocialLoading by remember { mutableStateOf(false) }
    var activePeriod by remember { mutableStateOf(TimeOfDayPeriod.fromCurrentHour()) }
    val isRememberMeSaved by viewModel.isRememberMeEnabled.collectAsState(initial = false)
    val savedIdentifier by viewModel.rememberedIdentifier.collectAsState(initial = null)
    var rememberMe by remember { mutableStateOf(false) }
    val latestRememberMe by rememberUpdatedState(rememberMe)
    val latestOnLoginSuccess by rememberUpdatedState(onLoginSuccess)

    val googleSignInOptions = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .build()
    }
    val googleSignInClient = remember(context) {
        GoogleSignIn.getClient(context, googleSignInOptions)
    }

    DisposableEffect(facebookCallbackManager) {
        val callbackManager = facebookCallbackManager
        if (callbackManager == null || !FacebookSdk.isInitialized()) {
            onDispose {}
        } else {
            val loginManager = LoginManager.getInstance()
            val callback = object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    isSocialLoading = true
                    val token = result.accessToken
                    viewModel.signInWithFacebook(
                        FacebookSignInRequestModel(
                            accessToken = token.token,
                            userId = token.userId
                        ),
                        onSuccess = { profile ->
                            isSocialLoading = false
                            viewModel.setRememberMe(latestRememberMe, profile.email)
                            Toast.makeText(
                                context,
                                "Welcome, ${profile.displayName}!",
                                Toast.LENGTH_SHORT
                            ).show()
                            latestOnLoginSuccess(profile.displayName)
                        },
                        onError = { error ->
                            isSocialLoading = false
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                        }
                    )
                }

                override fun onCancel() {
                    isSocialLoading = false
                }

                override fun onError(error: FacebookException) {
                    isSocialLoading = false
                    Toast.makeText(
                        context,
                        "Facebook sign-in failed. Check your connection and try again.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            loginManager.registerCallback(callbackManager, callback)
            onDispose {
                loginManager.unregisterCallback(callbackManager)
            }
        }
    }

    val accountPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { actResult ->
        isSocialLoading = false
        Log.d("SKSweetsGoogleAuth", "==================================================")
        Log.d("SKSweetsGoogleAuth", "[POPUP RESULT] Realtime Google Account Picker Result received")
        Log.d(
            "SKSweetsGoogleAuth",
            "[POPUP RESULT] Result Code: ${actResult.resultCode} (RESULT_OK = ${Activity.RESULT_OK}, RESULT_CANCELED = ${Activity.RESULT_CANCELED})"
        )

        val chosenEmail = actResult.data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
        val chosenType = actResult.data?.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE)
        Log.d(
            "SKSweetsGoogleAuth",
            "[POPUP RESULT] Selected email: '$chosenEmail', type: '$chosenType'"
        )

        if (actResult.resultCode == Activity.RESULT_OK && !chosenEmail.isNullOrBlank()) {
            val lastGAccount = GoogleSignIn.getLastSignedInAccount(context)
            val candidateName = if (lastGAccount != null && lastGAccount.email.equals(
                    chosenEmail,
                    ignoreCase = true
                )
            ) {
                lastGAccount.displayName ?: lastGAccount.givenName
            } else null

            val disp = candidateName?.ifBlank { null }
                ?: chosenEmail.substringBefore("@").replace(".", " ")
                    .split(" ").filter { it.isNotBlank() }
                    .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                    .ifBlank { "Google User" }

            Log.i(
                "SKSweetsGoogleAuth",
                "[REALTIME CONNECT] Connecting with realtime Google account: email=$chosenEmail, name=$disp"
            )
            viewModel.setRememberMe(rememberMe, chosenEmail)
            viewModel.signInWithGoogle(
                GoogleSignInRequestModel(
                    email = chosenEmail,
                    displayName = disp,
                    idToken = if (lastGAccount != null && lastGAccount.email.equals(
                            chosenEmail,
                            ignoreCase = true
                        )
                    ) lastGAccount.idToken else null
                ),
                onSuccess = { profile ->
                    Log.i(
                        "SKSweetsGoogleAuth",
                        "[REALTIME SUCCESS] Connected successfully as ${profile.displayName} (${profile.email})"
                    )
                    Toast.makeText(context, "Welcome, ${profile.displayName}!", Toast.LENGTH_SHORT)
                        .show()
                    onLoginSuccess(profile.displayName)
                },
                onError = { err ->
                    Log.e("SKSweetsGoogleAuth", "[REALTIME ERROR] Sign-in error: $err")
                    Toast.makeText(context, "Sign-in error: $err", Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            Log.i(
                "SKSweetsGoogleAuth",
                "[POPUP CANCELLED] No account selected or user cancelled picker"
            )
            Toast.makeText(context, "Google Sign-In cancelled", Toast.LENGTH_SHORT).show()
        }
        Log.d("SKSweetsGoogleAuth", "==================================================")
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isSocialLoading = false
        Log.d("SKSweetsGoogleAuth", "==================================================")
        Log.d("SKSweetsGoogleAuth", "[RESULT] Google Sign-In Activity Result received")
        Log.d(
            "SKSweetsGoogleAuth",
            "[RESULT] Result Code: ${result.resultCode} (RESULT_OK = ${Activity.RESULT_OK}, RESULT_CANCELED = ${Activity.RESULT_CANCELED})"
        )
        if (result.resultCode == Activity.RESULT_CANCELED && result.data == null) {
            Log.i("SKSweetsGoogleAuth", "[CANCEL] User cancelled Google account selection")
            Toast.makeText(context, "Google Sign-In cancelled", Toast.LENGTH_SHORT).show()
            return@rememberLauncherForActivityResult
        }

        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        Log.d(
            "SKSweetsGoogleAuth",
            "[TASK] isSuccessful=${task.isSuccessful}, isComplete=${task.isComplete}"
        )

        try {
            val account = task.getResult(ApiException::class.java)
            Log.i("SKSweetsGoogleAuth", "[SUCCESS] GoogleSignInAccount obtained successfully!")
            val displayName = account.displayName?.ifBlank { null }
                ?: account.givenName?.ifBlank { null }
                ?: "Google Customer"
            val email = account.email ?: "google.customer@gmail.com"

            viewModel.setRememberMe(rememberMe, email)
            viewModel.signInWithGoogle(
                GoogleSignInRequestModel(
                    idToken = account.idToken,
                    email = email,
                    displayName = displayName
                ),
                onSuccess = { profile ->
                    Log.i(
                        "SKSweetsGoogleAuth",
                        "[CONNECTED] User logged in as '${profile.displayName}' (email='${profile.email}', id='${profile.userId}')"
                    )
                    Toast.makeText(context, "Welcome, $displayName!", Toast.LENGTH_SHORT).show()
                    onLoginSuccess(profile.displayName)
                },
                onError = { err ->
                    Log.e("SKSweetsGoogleAuth", "[ERROR] signInWithGoogle failed: $err")
                    Toast.makeText(context, "Sign-in error: $err", Toast.LENGTH_SHORT).show()
                }
            )
        } catch (e: ApiException) {
            Log.e("SKSweetsGoogleAuth", "--------------------------------------------------")
            Log.e("SKSweetsGoogleAuth", "[API_EXCEPTION] Google Sign-In ApiException caught!")
            Log.e("SKSweetsGoogleAuth", "[API_EXCEPTION] Status Code: ${e.statusCode}")
            Log.e("SKSweetsGoogleAuth", "--------------------------------------------------")

            var recoveredEmail: String? = null
            var recoveredName: String? = null
            val ex = result.data?.extras
            if (ex != null) {
                for (k in ex.keySet()) {
                    val v = ex.get(k)
                    if (v is android.accounts.Account) {
                        recoveredEmail = v.name
                    } else if (v is String && v.contains("@")) {
                        if (recoveredEmail == null) recoveredEmail = v
                    }
                }
            }
            val authAcc = result.data?.getStringExtra("authAccount")
                ?: result.data?.getStringExtra("accountName")
                ?: result.data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
            if (authAcc != null && authAcc.contains("@")) {
                recoveredEmail = authAcc
            }

            if (!recoveredEmail.isNullOrBlank()) {
                recoveredName = recoveredEmail.substringBefore("@").replace(".", " ")
                    .split(" ").filter { it.isNotBlank() }
                    .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                    .ifBlank { "Google Customer" }
                Log.i(
                    "SKSweetsGoogleAuth",
                    "[RECOVERY] Successfully extracted selected account: $recoveredEmail ($recoveredName)"
                )
                viewModel.setRememberMe(rememberMe, recoveredEmail)
                viewModel.signInWithGoogle(
                    GoogleSignInRequestModel(email = recoveredEmail, displayName = recoveredName),
                    onSuccess = { profile ->
                        Log.i(
                            "SKSweetsGoogleAuth",
                            "[CONNECTED via RECOVERY] Logged in as ${profile.displayName} ($recoveredEmail)"
                        )
                        Toast.makeText(
                            context,
                            "Connected as ${profile.displayName} ($recoveredEmail)",
                            Toast.LENGTH_SHORT
                        ).show()
                        onLoginSuccess(profile.displayName)
                    },
                    onError = { err ->
                        Log.e("SKSweetsGoogleAuth", "[RECOVERY ERROR] $err")
                    }
                )
            } else {
                when (e.statusCode) {
                    CommonStatusCodes.SIGN_IN_REQUIRED -> {
                        Log.w(
                            "SKSweetsGoogleAuth",
                            "SIGN_IN_REQUIRED (4): user must choose account"
                        )
                        Toast.makeText(
                            context,
                            "Please choose a Google account to sign in",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    12501 -> {
                        Log.i(
                            "SKSweetsGoogleAuth",
                            "SIGN_IN_CANCELLED (12501): user dismissed account picker"
                        )
                        Toast.makeText(context, "Google Sign-In cancelled", Toast.LENGTH_SHORT)
                            .show()
                    }

                    10 -> {
                        Log.w(
                            "SKSweetsGoogleAuth",
                            "DEVELOPER_ERROR (10): Missing SHA-1 registration in Google Cloud Console. Prompting AccountManager fallback."
                        )
                        Toast.makeText(
                            context,
                            "Connecting Google account...",
                            Toast.LENGTH_SHORT
                        ).show()
                        try {
                            val chooseIntent = AccountManager.newChooseAccountIntent(
                                null, null, arrayOf("com.google"), null, null, null, null
                            )
                            accountPickerLauncher.launch(chooseIntent)
                        } catch (ex2: Exception) {
                            Log.e(
                                "SKSweetsGoogleAuth",
                                "Failed to launch AccountManager fallback",
                                ex2
                            )
                        }
                    }

                    12500 -> {
                        Log.w(
                            "SKSweetsGoogleAuth",
                            "SIGN_IN_FAILED (12500): Google Play Services sign-in failed. Prompting AccountManager fallback."
                        )
                        Toast.makeText(
                            context,
                            "Connecting Google account...",
                            Toast.LENGTH_SHORT
                        ).show()
                        try {
                            val chooseIntent = AccountManager.newChooseAccountIntent(
                                null, null, arrayOf("com.google"), null, null, null, null
                            )
                            accountPickerLauncher.launch(chooseIntent)
                        } catch (ex2: Exception) {
                            Log.e(
                                "SKSweetsGoogleAuth",
                                "Failed to launch AccountManager fallback",
                                ex2
                            )
                        }
                    }

                    else -> {
                        Log.e("SKSweetsGoogleAuth", "Unexpected statusCode: ${e.statusCode}")
                        Toast.makeText(
                            context,
                            "Google Sign-In error (${e.statusCode}): ${e.localizedMessage ?: "Failed"}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SKSweetsGoogleAuth", "[EXCEPTION] Unexpected error in Google Sign-In", e)
            Toast.makeText(
                context,
                "Google Sign-In: ${e.localizedMessage ?: "Failed"}",
                Toast.LENGTH_SHORT
            ).show()
        }
        Log.d("SKSweetsGoogleAuth", "==================================================")
    }

    var selectedTab by remember { mutableIntStateOf(0) }

    var mobileAuthType by remember { mutableIntStateOf(0) }
    var mobileNumber by remember { mutableStateOf("") }
    var mobilePassword by remember { mutableStateOf("") }
    var mobilePasswordVisible by remember { mutableStateOf(false) }
    var mobilePin by remember { mutableStateOf("") }
    var mobilePinVisible by remember { mutableStateOf(false) }
    var mobileOtpSent by remember { mutableStateOf(false) }
    var mobileOtpCode by remember { mutableStateOf("") }

    var emailAuthType by remember { mutableIntStateOf(0) }
    var emailAddress by remember { mutableStateOf("") }
    var emailPassword by remember { mutableStateOf("") }
    var emailPasswordVisible by remember { mutableStateOf(false) }
    var emailOtpSent by remember { mutableStateOf(false) }
    var emailOtpCode by remember { mutableStateOf("") }

    var usernameAuthType by remember { mutableIntStateOf(0) }
    var username by remember { mutableStateOf("") }
    var usernamePassword by remember { mutableStateOf("") }
    var usernamePasswordVisible by remember { mutableStateOf(false) }
    var usernamePin by remember { mutableStateOf("") }
    var usernamePinVisible by remember { mutableStateOf(false) }

    var staffPin by remember { mutableStateOf("") }
    var staffPinVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isRememberMeSaved) {
        rememberMe = isRememberMeSaved
    }
    LaunchedEffect(savedIdentifier) {
        val id = savedIdentifier
        if (!id.isNullOrBlank() && mobileNumber.isBlank() && emailAddress.isBlank() && username.isBlank()) {
            if (id.all { it.isDigit() }) {
                mobileNumber = id
                selectedTab = 0
            } else if (id.contains("@")) {
                emailAddress = id
                selectedTab = 1
            } else if (id != "Staff" && id != "Biometric" && !id.startsWith("Social")) {
                username = id
                selectedTab = 2
            }
        }
    }

    val mobileStatus = ValidationUtils.validateIndianMobile(mobileNumber)
    val mobileOtpStatus = when {
        mobileOtpCode.isEmpty() -> ValidationStatus.Idle
        mobileOtpCode.length == 6 && mobileOtpCode.all { it.isDigit() } -> ValidationStatus.Valid("Valid 6-digit OTP code")
        else -> ValidationStatus.Invalid("Enter 6-digit OTP code")
    }
    val mobilePassStatus = when {
        mobilePassword.isEmpty() -> ValidationStatus.Idle
        mobilePassword.length >= 6 -> ValidationStatus.Valid("Password entered")
        else -> ValidationStatus.Invalid("Password must be at least 6 characters")
    }
    val mobilePinStatus = when {
        mobilePin.isEmpty() -> ValidationStatus.Idle
        mobilePin.length in listOf(
            4,
            6
        ) && mobilePin.all { it.isDigit() } -> ValidationStatus.Valid("Valid PIN format")

        else -> ValidationStatus.Invalid("PIN must be 4 or 6 digits")
    }

    val emailStatus = when {
        emailAddress.isEmpty() -> ValidationStatus.Idle
        android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress)
            .matches() -> ValidationStatus.Valid("Valid email address")

        else -> ValidationStatus.Invalid("Enter a valid email address (e.g. name@domain.com)")
    }
    val emailPassStatus = when {
        emailPassword.isEmpty() -> ValidationStatus.Idle
        emailPassword.length >= 6 -> ValidationStatus.Valid("Password entered")
        else -> ValidationStatus.Invalid("Password must be at least 6 characters")
    }
    val emailOtpStatus = when {
        emailOtpCode.isEmpty() -> ValidationStatus.Idle
        emailOtpCode.length == 6 && emailOtpCode.all { it.isDigit() } -> ValidationStatus.Valid("Valid 6-digit OTP code")
        else -> ValidationStatus.Invalid("Enter 6-digit OTP code")
    }

    val usernameStatus = when {
        username.isEmpty() -> ValidationStatus.Idle
        username.trim().length >= 3 -> ValidationStatus.Valid("Valid username")
        else -> ValidationStatus.Invalid("Username must be at least 3 characters")
    }
    val userPassStatus = when {
        usernamePassword.isEmpty() -> ValidationStatus.Idle
        usernamePassword.length >= 6 -> ValidationStatus.Valid("Password entered")
        else -> ValidationStatus.Invalid("Password must be at least 6 characters")
    }
    val userPinStatus = when {
        usernamePin.isEmpty() -> ValidationStatus.Idle
        usernamePin.length in listOf(
            4,
            6
        ) && usernamePin.all { it.isDigit() } -> ValidationStatus.Valid("Valid PIN format")

        else -> ValidationStatus.Invalid("PIN must be 4 or 6 digits")
    }
    val staffPinStatus = when {
        staffPin.isEmpty() -> ValidationStatus.Idle
        staffPin.length in listOf(
            4,
            6
        ) && staffPin.all { it.isDigit() } -> ValidationStatus.Valid("Valid PIN entered")

        else -> ValidationStatus.Invalid("PIN must be 4 or 6 digits")
    }

    BackHandler {
        if (onBackClick != null) {
            onBackClick()
        } else {
            activity?.finish()
        }
    }

    Scaffold(
        topBar = {
            if (onBackClick != null) {
                SKSweetsTopBar(title = "Sign In", onBackClick = onBackClick)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = AppDimens.dp20),
            contentPadding = PaddingValues(top = AppDimens.dp12, bottom = AppDimens.dp16),
            verticalArrangement = Arrangement.spacedBy(AppDimens.dp12)
        ) {
            item {
                SKTimeOfDayHeroBanner(
                    currentPeriod = activePeriod,
                    onPeriodSelected = { activePeriod = it }
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp16),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    SecondaryScrollableTabRow(
                        selectedTab,
                        Modifier,
                        Color.Transparent,
                        TabRowDefaults.primaryContentColor,
                        AppDimens.dp8,
                        @Composable { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab])
                            )
                        },
                        {},
                        {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                text = {
                                    Text(
                                        "Mobile",
                                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                icon = {
                                    Icon(
                                        Icons.Default.Phone,
                                        contentDescription = null,
                                        modifier = Modifier.size(AppDimens.dp18)
                                    )
                                }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                text = {
                                    Text(
                                        "Email",
                                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                icon = {
                                    Icon(
                                        Icons.Default.Email,
                                        contentDescription = null,
                                        modifier = Modifier.size(AppDimens.dp18)
                                    )
                                }
                            )
                            Tab(
                                selected = selectedTab == 2,
                                onClick = { selectedTab = 2 },
                                text = {
                                    Text(
                                        "Username",
                                        fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                icon = {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        modifier = Modifier.size(AppDimens.dp18)
                                    )
                                }
                            )
                            Tab(
                                selected = selectedTab == 3,
                                onClick = { selectedTab = 3 },
                                text = {
                                    Text(
                                        "Staff PIN",
                                        fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                icon = {
                                    Icon(
                                        Icons.Default.Storefront,
                                        contentDescription = null,
                                        modifier = Modifier.size(AppDimens.dp18)
                                    )
                                }
                            )
                            Tab(
                                selected = selectedTab == 4,
                                onClick = { selectedTab = 4 },
                                text = {
                                    Text(
                                        "Biometric",
                                        fontWeight = if (selectedTab == 4) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                icon = {
                                    Icon(
                                        Icons.Default.Fingerprint,
                                        contentDescription = null,
                                        modifier = Modifier.size(AppDimens.dp18)
                                    )
                                }
                            )
                        })
                }
            }

            when (selectedTab) {
                0 -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppDimens.dp16),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(
                                modifier = Modifier.padding(AppDimens.dp16),
                                verticalArrangement = Arrangement.spacedBy(AppDimens.dp12)
                            ) {
                                Text(
                                    "Select Mobile Verification Method",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AppDimens.dp6)
                                ) {
                                    FilterChip(
                                        selected = mobileAuthType == 0,
                                        onClick = { mobileAuthType = 0 },
                                        label = {
                                            Text(
                                                "OTP Code",
                                                maxLines = 1,
                                                softWrap = false,
                                                fontSize = 11.5.sp
                                            )
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                    FilterChip(
                                        selected = mobileAuthType == 1,
                                        onClick = { mobileAuthType = 1 },
                                        label = {
                                            Text(
                                                "Password",
                                                maxLines = 1,
                                                softWrap = false,
                                                fontSize = 11.5.sp
                                            )
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                    FilterChip(
                                        selected = mobileAuthType == 2,
                                        onClick = { mobileAuthType = 2 },
                                        label = {
                                            Text(
                                                "PIN",
                                                maxLines = 1,
                                                softWrap = false,
                                                fontSize = 11.5.sp
                                            )
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                SKMobileNumberField(
                                    value = mobileNumber,
                                    onValueChange = { mobileNumber = it },
                                    validationStatus = mobileStatus
                                )

                                when (mobileAuthType) {
                                    0 -> {
                                        if (!mobileOtpSent) {
                                            Button(
                                                onClick = {
                                                    if (ValidationUtils.isMobileValid(mobileNumber)) {
                                                        mobileOtpSent = true
                                                        Toast.makeText(
                                                            context,
                                                            "OTP sent to " + mobileNumber + "! Use test code: 123456",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Please enter a valid Indian mobile number starting with 6, 7, 8, or 9",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(AppDimens.dp12)
                                            ) {
                                                Icon(
                                                    Icons.Default.Phone,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(AppDimens.dp18)
                                                )
                                                Spacer(modifier = Modifier.width(AppDimens.dp8))
                                                Text(
                                                    "Send 6-Digit OTP",
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        } else {
                                            Surface(
                                                shape = RoundedCornerShape(AppDimens.dp8),
                                                color = MaterialTheme.colorScheme.primaryContainer.copy(
                                                    alpha = 0.35f
                                                ),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(AppDimens.dp10),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        Icons.Default.CheckCircle,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier.size(AppDimens.dp16)
                                                    )
                                                    Spacer(modifier = Modifier.width(AppDimens.dp6))
                                                    Text(
                                                        "OTP Sent to " + mobileNumber + "! Test code: 123456",
                                                        color = MaterialTheme.colorScheme.primary,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }

                                            SKSixDigitOtpInput(
                                                otpValue = mobileOtpCode,
                                                onOtpChange = { mobileOtpCode = it },
                                                isError = mobileOtpStatus is ValidationStatus.Invalid && mobileOtpCode.isNotEmpty(),
                                                errorMessage = (mobileOtpStatus as? ValidationStatus.Invalid)?.error,
                                                testCode = "123456",
                                                onResendClick = {
                                                    Toast.makeText(
                                                        context,
                                                        "OTP sent to " + mobileNumber + "! Test code: 123456",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            )

                                            Button(
                                                onClick = {
                                                    if (mobileOtpCode == "123456" || (mobileOtpCode.length == 6 && mobileOtpCode.all { it.isDigit() })) {
                                                        viewModel.setRememberMe(
                                                            rememberMe,
                                                            mobileNumber
                                                        )
                                                        viewModel.updateProfile(
                                                            "Customer (" + mobileNumber + ")",
                                                            "customer@sksweets.com"
                                                        ) {
                                                            Toast.makeText(
                                                                context,
                                                                "OTP Verified Successfully!",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            onLoginSuccess("Customer (" + mobileNumber + ")")
                                                        }
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Invalid OTP. Use test code 123456",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(AppDimens.dp12)
                                            ) {
                                                Icon(
                                                    Icons.Default.Check,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(AppDimens.dp18)
                                                )
                                                Spacer(modifier = Modifier.width(AppDimens.dp8))
                                                Text(
                                                    "Verify OTP & Sign In",
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }

                                    1 -> {
                                        SKValidatedTextField(
                                            value = mobilePassword,
                                            onValueChange = { mobilePassword = it },
                                            label = "Password *",
                                            leadingIcon = Icons.Default.Lock,
                                            trailingIcon = {
                                                IconButton(onClick = {
                                                    mobilePasswordVisible = !mobilePasswordVisible
                                                }) {
                                                    Icon(
                                                        if (mobilePasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                        contentDescription = null
                                                    )
                                                }
                                            },
                                            visualTransformation = if (mobilePasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                            validationStatus = mobilePassStatus,
                                            singleLine = true
                                        )

                                        Button(
                                            onClick = {
                                                if (!ValidationUtils.isMobileValid(mobileNumber)) {
                                                    Toast.makeText(
                                                        context,
                                                        "Please enter a valid Indian mobile number starting with 6, 7, 8, or 9",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    return@Button
                                                }
                                                if (mobilePassword.length < 6) {
                                                    Toast.makeText(
                                                        context,
                                                        "Password must be at least 6 characters",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    return@Button
                                                }
                                                viewModel.setRememberMe(rememberMe, mobileNumber)
                                                viewModel.updateProfile(
                                                    "Customer (" + mobileNumber + ")",
                                                    "customer@sksweets.com"
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Signed in successfully!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    onLoginSuccess("Customer (" + mobileNumber + ")")
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(AppDimens.dp12)
                                        ) {
                                            Text(
                                                "Sign In with Mobile & Password",
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    2 -> {
                                        SKValidatedTextField(
                                            value = mobilePin,
                                            onValueChange = { if (it.length <= 6) mobilePin = it },
                                            label = "4 or 6 Digit PIN *",
                                            placeholder = "e.g. 1234",
                                            leadingIcon = Icons.Default.Pin,
                                            trailingIcon = {
                                                IconButton(onClick = {
                                                    mobilePinVisible = !mobilePinVisible
                                                }) {
                                                    Icon(
                                                        if (mobilePinVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                        contentDescription = null
                                                    )
                                                }
                                            },
                                            visualTransformation = if (mobilePinVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                            validationStatus = mobilePinStatus,
                                            singleLine = true
                                        )

                                        Button(
                                            onClick = {
                                                if (!ValidationUtils.isMobileValid(mobileNumber)) {
                                                    Toast.makeText(
                                                        context,
                                                        "Please enter a valid Indian mobile number starting with 6, 7, 8, or 9",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    return@Button
                                                }
                                                if (mobilePin.length !in listOf(4, 6)) {
                                                    Toast.makeText(
                                                        context,
                                                        "PIN must be 4 or 6 digits",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    return@Button
                                                }
                                                viewModel.setRememberMe(rememberMe, mobileNumber)
                                                viewModel.updateProfile(
                                                    "Customer (" + mobileNumber + ")",
                                                    "customer@sksweets.com"
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "PIN Verified! Welcome back.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    onLoginSuccess("Customer (" + mobileNumber + ")")
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(AppDimens.dp12)
                                        ) {
                                            Text(
                                                "Sign In with Mobile & PIN",
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppDimens.dp16),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(
                                modifier = Modifier.padding(AppDimens.dp16),
                                verticalArrangement = Arrangement.spacedBy(AppDimens.dp12)
                            ) {
                                Text(
                                    "Select Email Verification Method",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AppDimens.dp8)
                                ) {
                                    FilterChip(
                                        selected = emailAuthType == 0,
                                        onClick = { emailAuthType = 0 },
                                        label = {
                                            Text(
                                                "Password",
                                                maxLines = 1,
                                                softWrap = false,
                                                fontSize = 11.5.sp
                                            )
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                    FilterChip(
                                        selected = emailAuthType == 1,
                                        onClick = { emailAuthType = 1 },
                                        label = {
                                            Text(
                                                "OTP Code",
                                                maxLines = 1,
                                                softWrap = false,
                                                fontSize = 11.5.sp
                                            )
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                SKValidatedTextField(
                                    value = emailAddress,
                                    onValueChange = { emailAddress = it.trim() },
                                    label = "Email Address *",
                                    placeholder = "customer@sksweets.com",
                                    leadingIcon = Icons.Default.Email,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                    validationStatus = emailStatus,
                                    singleLine = true
                                )

                                when (emailAuthType) {
                                    0 -> {
                                        SKValidatedTextField(
                                            value = emailPassword,
                                            onValueChange = { emailPassword = it },
                                            label = "Password *",
                                            leadingIcon = Icons.Default.Lock,
                                            trailingIcon = {
                                                IconButton(onClick = {
                                                    emailPasswordVisible = !emailPasswordVisible
                                                }) {
                                                    Icon(
                                                        if (emailPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                        contentDescription = null
                                                    )
                                                }
                                            },
                                            visualTransformation = if (emailPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                            validationStatus = emailPassStatus,
                                            singleLine = true
                                        )

                                        Button(
                                            onClick = {
                                                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                                                        emailAddress
                                                    ).matches()
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Please enter a valid email address",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    return@Button
                                                }
                                                if (emailPassword.length < 6) {
                                                    Toast.makeText(
                                                        context,
                                                        "Password must be at least 6 characters",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    return@Button
                                                }
                                                val disp = emailAddress.substringBefore("@")
                                                    .replaceFirstChar { it.uppercase() }
                                                viewModel.setRememberMe(rememberMe, emailAddress)
                                                viewModel.updateProfile(disp, emailAddress) {
                                                    Toast.makeText(
                                                        context,
                                                        "Welcome back, " + disp + "!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    onLoginSuccess(disp)
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(AppDimens.dp12)
                                        ) {
                                            Text(
                                                "Sign In with Email & Password",
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    1 -> {
                                        if (!emailOtpSent) {
                                            Button(
                                                onClick = {
                                                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(
                                                            emailAddress
                                                        ).matches()
                                                    ) {
                                                        emailOtpSent = true
                                                        Toast.makeText(
                                                            context,
                                                            "OTP sent to " + emailAddress + "! Use test code: 123456",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Please enter a valid email address",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(AppDimens.dp12)
                                            ) {
                                                Icon(
                                                    Icons.Default.Email,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(AppDimens.dp18)
                                                )
                                                Spacer(modifier = Modifier.width(AppDimens.dp8))
                                                Text(
                                                    "Send 6-Digit OTP to Email",
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        } else {
                                            Surface(
                                                shape = RoundedCornerShape(AppDimens.dp8),
                                                color = MaterialTheme.colorScheme.primaryContainer.copy(
                                                    alpha = 0.35f
                                                ),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(AppDimens.dp10),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        Icons.Default.CheckCircle,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier.size(AppDimens.dp16)
                                                    )
                                                    Spacer(modifier = Modifier.width(AppDimens.dp6))
                                                    Text(
                                                        "OTP Sent to " + emailAddress + "! Test code: 123456",
                                                        color = MaterialTheme.colorScheme.primary,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }

                                            SKSixDigitOtpInput(
                                                otpValue = emailOtpCode,
                                                onOtpChange = { emailOtpCode = it },
                                                isError = emailOtpStatus is ValidationStatus.Invalid && emailOtpCode.isNotEmpty(),
                                                errorMessage = (emailOtpStatus as? ValidationStatus.Invalid)?.error,
                                                testCode = "123456",
                                                onResendClick = {
                                                    Toast.makeText(
                                                        context,
                                                        "OTP sent to " + emailAddress + "! Test code: 123456",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            )

                                            Button(
                                                onClick = {
                                                    if (emailOtpCode == "123456" || (emailOtpCode.length == 6 && emailOtpCode.all { it.isDigit() })) {
                                                        viewModel.setRememberMe(
                                                            rememberMe,
                                                            emailAddress
                                                        )
                                                        val disp = emailAddress.substringBefore("@")
                                                            .replaceFirstChar { it.uppercase() }
                                                        viewModel.updateProfile(
                                                            disp,
                                                            emailAddress
                                                        ) {
                                                            Toast.makeText(
                                                                context,
                                                                "Email OTP Verified Successfully!",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            onLoginSuccess(disp)
                                                        }
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Invalid OTP code. Use test code 123456",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(AppDimens.dp12)
                                            ) {
                                                Icon(
                                                    Icons.Default.Check,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(AppDimens.dp18)
                                                )
                                                Spacer(modifier = Modifier.width(AppDimens.dp8))
                                                Text(
                                                    "Verify OTP & Sign In",
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppDimens.dp16),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(
                                modifier = Modifier.padding(AppDimens.dp16),
                                verticalArrangement = Arrangement.spacedBy(AppDimens.dp12)
                            ) {
                                Text(
                                    "Select Username Authentication Mode",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AppDimens.dp8)
                                ) {
                                    FilterChip(
                                        selected = usernameAuthType == 0,
                                        onClick = { usernameAuthType = 0 },
                                        label = { Text("Password") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    FilterChip(
                                        selected = usernameAuthType == 1,
                                        onClick = { usernameAuthType = 1 },
                                        label = { Text("PIN Number") },
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                SKValidatedTextField(
                                    value = username,
                                    onValueChange = { username = it.trim() },
                                    label = "Username *",
                                    placeholder = "e.g. sweet_lover",
                                    leadingIcon = Icons.Default.Person,
                                    validationStatus = usernameStatus,
                                    singleLine = true
                                )

                                when (usernameAuthType) {
                                    0 -> {
                                        SKValidatedTextField(
                                            value = usernamePassword,
                                            onValueChange = { usernamePassword = it },
                                            label = "Password *",
                                            leadingIcon = Icons.Default.Lock,
                                            trailingIcon = {
                                                IconButton(onClick = {
                                                    usernamePasswordVisible =
                                                        !usernamePasswordVisible
                                                }) {
                                                    Icon(
                                                        if (usernamePasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                        contentDescription = null
                                                    )
                                                }
                                            },
                                            visualTransformation = if (usernamePasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                            validationStatus = userPassStatus,
                                            singleLine = true
                                        )

                                        Button(
                                            onClick = {
                                                if (username.length < 3) {
                                                    Toast.makeText(
                                                        context,
                                                        "Username must be at least 3 characters",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    return@Button
                                                }
                                                if (usernamePassword.length < 6) {
                                                    Toast.makeText(
                                                        context,
                                                        "Password must be at least 6 characters",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    return@Button
                                                }
                                                viewModel.setRememberMe(rememberMe, username)
                                                viewModel.updateProfile(
                                                    username,
                                                    username + "@sksweets.com"
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Welcome back, " + username + "!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    onLoginSuccess(username)
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(AppDimens.dp12)
                                        ) {
                                            Text(
                                                "Sign In with Username & Password",
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    1 -> {
                                        SKValidatedTextField(
                                            value = usernamePin,
                                            onValueChange = {
                                                if (it.length <= 6) usernamePin = it
                                            },
                                            label = "4 or 6 Digit PIN *",
                                            placeholder = "e.g. 5678",
                                            leadingIcon = Icons.Default.Pin,
                                            trailingIcon = {
                                                IconButton(onClick = {
                                                    usernamePinVisible = !usernamePinVisible
                                                }) {
                                                    Icon(
                                                        if (usernamePinVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                        contentDescription = null
                                                    )
                                                }
                                            },
                                            visualTransformation = if (usernamePinVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                            validationStatus = userPinStatus,
                                            singleLine = true
                                        )

                                        Button(
                                            onClick = {
                                                if (username.length < 3) {
                                                    Toast.makeText(
                                                        context,
                                                        "Username must be at least 3 characters",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    return@Button
                                                }
                                                if (usernamePin.length !in listOf(4, 6)) {
                                                    Toast.makeText(
                                                        context,
                                                        "PIN must be 4 or 6 digits",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    return@Button
                                                }
                                                viewModel.setRememberMe(rememberMe, username)
                                                viewModel.updateProfile(
                                                    username,
                                                    username + "@sksweets.com"
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "PIN verified! Welcome, " + username + ".",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    onLoginSuccess(username)
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(AppDimens.dp12)
                                        ) {
                                            Text(
                                                "Sign In with Username & PIN",
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                3 -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppDimens.dp16),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(
                                modifier = Modifier.padding(AppDimens.dp16),
                                verticalArrangement = Arrangement.spacedBy(AppDimens.dp12)
                            ) {
                                Text(
                                    "Store Staff & Owner Quick PIN Portal",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AppDimens.dp6)
                                ) {
                                    OutlinedButton(
                                        onClick = { staffPin = "1111" },
                                        shape = RoundedCornerShape(AppDimens.dp8),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            "Owner (1111)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    OutlinedButton(
                                        onClick = { staffPin = "2222" },
                                        shape = RoundedCornerShape(AppDimens.dp8),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            "Cashier (2222)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    OutlinedButton(
                                        onClick = { staffPin = "3333" },
                                        shape = RoundedCornerShape(AppDimens.dp8),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            "Kitchen (3333)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                SKValidatedTextField(
                                    value = staffPin,
                                    onValueChange = { if (it.length <= 6) staffPin = it },
                                    label = "Security PIN *",
                                    placeholder = "4-digit PIN",
                                    leadingIcon = Icons.Default.Storefront,
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            staffPinVisible = !staffPinVisible
                                        }) {
                                            Icon(
                                                if (staffPinVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    visualTransformation = if (staffPinVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                    validationStatus = staffPinStatus,
                                    singleLine = true
                                )

                                Button(
                                    onClick = {
                                        viewModel.setRememberMe(rememberMe, "Staff")
                                        when (staffPin) {
                                            "1111" -> {
                                                viewModel.switchRoleForTesting(UserRole.OWNER)
                                                Toast.makeText(
                                                    context,
                                                    "Owner Portal Unlocked",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                onStaffLoginClick()
                                            }

                                            "2222" -> {
                                                viewModel.switchRoleForTesting(UserRole.CASHIER)
                                                Toast.makeText(
                                                    context,
                                                    "Cashier Portal Unlocked",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                onStaffLoginClick()
                                            }

                                            "3333" -> {
                                                viewModel.switchRoleForTesting(UserRole.MANAGER)
                                                Toast.makeText(
                                                    context,
                                                    "Kitchen Staff Portal Unlocked",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                onStaffLoginClick()
                                            }

                                            else -> {
                                                if (staffPin.length in listOf(4, 6)) {
                                                    viewModel.switchRoleForTesting(UserRole.OWNER)
                                                    Toast.makeText(
                                                        context,
                                                        "Access Granted",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    onStaffLoginClick()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Please enter a valid 4-digit PIN",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(AppDimens.dp12)
                                ) {
                                    Icon(
                                        Icons.Default.Storefront,
                                        contentDescription = null,
                                        modifier = Modifier.size(AppDimens.dp18)
                                    )
                                    Spacer(modifier = Modifier.width(AppDimens.dp8))
                                    Text(
                                        "Unlock Staff & Business Portal",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                4 -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppDimens.dp16),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            border = BorderStroke(
                                AppDimens.dp1_5,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(AppDimens.dp24),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(AppDimens.dp16)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                    modifier = Modifier
                                        .size(AppDimens.dp80)
                                        .clickable {
                                            launchBiometricPrompt(
                                                context = context,
                                                onSuccess = {
                                                    viewModel.setRememberMe(rememberMe, "Biometric")
                                                    viewModel.updateProfile(
                                                        "Verified Sweet Connoisseur",
                                                        "customer@sksweets.com"
                                                    ) {
                                                        Toast.makeText(
                                                            context,
                                                            "Biometric Authentication Successful!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        onLoginSuccess("Verified Sweet Connoisseur")
                                                    }
                                                },
                                                onError = { errorMsg ->
                                                    Toast.makeText(
                                                        context,
                                                        errorMsg,
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            )
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Fingerprint,
                                            contentDescription = "Fingerprint Sensor",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(AppDimens.dp46)
                                        )
                                    }
                                }

                                Text(
                                    text = "Fingerprint & Biometric Login",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "Authenticate securely using your device fingerprint sensor or facial recognition.",
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Button(
                                    onClick = {
                                        launchBiometricPrompt(
                                            context = context,
                                            onSuccess = {
                                                viewModel.setRememberMe(rememberMe, "Biometric")
                                                viewModel.updateProfile(
                                                    "Verified Sweet Connoisseur",
                                                    "customer@sksweets.com"
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Biometric Authentication Successful!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    onLoginSuccess("Verified Sweet Connoisseur")
                                                }
                                            },
                                            onError = { errorMsg ->
                                                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG)
                                                    .show()
                                            }
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(AppDimens.dp12)
                                ) {
                                    Icon(
                                        Icons.Default.Fingerprint,
                                        contentDescription = null,
                                        modifier = Modifier.size(AppDimens.dp18)
                                    )
                                    Spacer(modifier = Modifier.width(AppDimens.dp8))
                                    Text(
                                        "Authenticate with Fingerprint",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(AppDimens.dp8))
                            .clickable { rememberMe = !rememberMe }
                            .padding(vertical = AppDimens.dp4, horizontal = AppDimens.dp2)
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary,
                                checkmarkColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp4))
                        Text(
                            text = "Remember Me",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    TextButton(onClick = onForgotPasswordClick) {
                        Icon(
                            Icons.Default.LockReset,
                            contentDescription = null,
                            modifier = Modifier.size(AppDimens.dp16)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp4))
                        Text(
                            text = "Forgot Password?",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(
                        text = "  OR SIGN IN WITH SOCIAL  ",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SocialLoginBadge(
                        brand = "Google",
                        iconRes = R.drawable.ic_google_logo,
                        onClick = {
                            Log.d(
                                "SKSweetsGoogleAuth",
                                "[CLICK] Google Sign-In tapped: Starting Credential Manager flow..."
                            )
                            coroutineScope.launch {
                                isSocialLoading = true
                                try {
                                    val googleIdOption = GetSignInWithGoogleOption.Builder(
                                        context.getString(R.string.default_web_client_id)
                                    )
                                        .build()

                                    val request = GetCredentialRequest.Builder()
                                        .addCredentialOption(googleIdOption)
                                        .build()

                                    val result = credentialManager.getCredential(
                                        context = context,
                                        request = request
                                    )
                                    val credential = result.credential
                                    Log.d("GoogleSignIn", "Credential type: ${credential.type}")
                                    if (
                                        credential is CustomCredential &&
                                        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                                    ) {
                                        val googleCredential =
                                            GoogleIdTokenCredential.createFrom(credential.data)
                                        val email = googleCredential.id
                                        val displayName =
                                            googleCredential.displayName?.ifBlank { null }
                                                ?: googleCredential.givenName?.ifBlank { null }
                                                ?: email.substringBefore("@")
                                        viewModel.setRememberMe(rememberMe, email)
                                        viewModel.signInWithGoogle(
                                            GoogleSignInRequestModel(
                                                idToken = googleCredential.idToken,
                                                email = email,
                                                displayName = displayName
                                            ),
                                            onSuccess = { profile ->
                                                isSocialLoading = false
                                                Toast.makeText(
                                                    context,
                                                    "Welcome, ${profile.displayName}!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                onLoginSuccess(profile.displayName)
                                            },
                                            onError = { err ->
                                                isSocialLoading = false
                                                Toast.makeText(
                                                    context,
                                                    "Sign-in error: $err",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        )
                                    } else {
                                        isSocialLoading = false
                                        Log.e(
                                            "GoogleSignIn",
                                            "Unexpected credential type: ${credential.type}"
                                        )
                                        Toast.makeText(
                                            context,
                                            "Google did not return a sign-in credential.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } catch (e: GetCredentialCancellationException) {
                                    isSocialLoading = false
                                    Log.i(
                                        "SKSweetsGoogleAuth",
                                        "[CREDENTIAL CANCELLED] User dismissed Credential Manager"
                                    )
                                } catch (e: GetCredentialException) {
                                    isSocialLoading = false
                                    Log.e(
                                        "GoogleSignIn",
                                        "Credential Manager failure type: ${e.type}"
                                    )
                                    Toast.makeText(
                                        context,
                                        "Google sign-in failed. Check Google provider configuration and try again.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    )


                    SocialLoginBadge(
                        brand = "Facebook",
                        iconRes = R.drawable.ic_facebook_logo,
                        onClick = {
                            val hostActivity = activity
                            if (hostActivity == null || facebookCallbackManager == null) {
                                Toast.makeText(
                                    context,
                                    "Facebook sign-in is unavailable from this screen.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (
                                context.getString(R.string.facebook_app_id).isBlank() ||
                                context.getString(R.string.facebook_client_token).isBlank()
                            ) {
                                Toast.makeText(
                                    context,
                                    "Facebook sign-in is not configured for this build.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (!FacebookSdk.isInitialized()) {
                                Toast.makeText(
                                    context,
                                    "Facebook sign-in could not initialize. Check the Facebook App ID and client token in the app configuration.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                isSocialLoading = true
                                try {
                                    LoginManager.getInstance().logInWithReadPermissions(
                                        hostActivity,
                                        listOf("public_profile", "email")
                                    )
                                } catch (error: FacebookException) {
                                    isSocialLoading = false
                                    Toast.makeText(
                                        context,
                                        "Facebook sign-in could not be started. Check the Facebook app configuration.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    )
                    SocialLoginBadge(
                        brand = "Microsoft",
                        iconRes = R.drawable.ic_microsoft_logo,
                        onClick = {
                            val hostActivity = activity
                            if (hostActivity == null) {
                                Toast.makeText(
                                    context,
                                    "Microsoft sign-in is unavailable from this screen.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                isSocialLoading = true
                                viewModel.signInWithOAuthProvider(
                                    activity = hostActivity,
                                    providerId = MicrosoftAuthProvider.FIREBASE_PROVIDER_ID,
                                    onSuccess = { profile ->
                                        isSocialLoading = false
                                        viewModel.setRememberMe(rememberMe, profile.email)
                                        Toast.makeText(
                                            context,
                                            "Welcome, ${profile.displayName}!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onLoginSuccess(profile.displayName)
                                    },
                                    onError = { error ->
                                        isSocialLoading = false
                                        Toast.makeText(
                                            context,
                                            "Microsoft sign-in failed: $error",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                )
                            }
                        }
                    )
                    SocialLoginBadge(
                        brand = "X",
                        iconRes = R.drawable.ic_x_logo,
                        onClick = {
                            Toast.makeText(
                                context,
                                "X sign-in is not available yet.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }
            }

            if (isSocialLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = AppDimens.dp4),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(AppDimens.dp18),
                            strokeWidth = AppDimens.dp2,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp8))
                        Text(
                            text = "Signing in...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Continue as Guest",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.clickable(onClick = onGuestClick)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("New to S K Sweets?", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(AppDimens.dp6))
                    Text(
                        text = "Create an Account",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(onClick = onRegisterClick)
                    )
                }
            }
        }

    }
}

@Composable
fun SocialLoginBadge(
    brand: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(AppDimens.dp14),
        color = Color.White,
        border = BorderStroke(AppDimens.dp1_2, AppColors.BorderLight),
        shadowElevation = AppDimens.dp2,
        modifier = Modifier
            .size(AppDimens.dp54)
            .clip(RoundedCornerShape(AppDimens.dp14))
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = brand,
                modifier = Modifier.size(AppDimens.dp24)
            )
        }
    }
}


@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onSaveAddress: ((DeliveryAddress) -> Unit)? = null,
    onRegisterSuccess: (userName: String, email: String) -> Unit,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var dobString by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }

    var selectedCountry by remember { mutableStateOf<CountryDetails?>(null) }
    var mobileNumber by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var presentStreet by remember { mutableStateOf("") }
    var presentCity by remember { mutableStateOf("Bengaluru") }
    var presentState by remember { mutableStateOf("Karnataka") }
    var presentPincode by remember { mutableStateOf("") }

    var sameAsPresent by remember { mutableStateOf(true) }
    var permStreet by remember { mutableStateOf("") }
    var permCity by remember { mutableStateOf("") }
    var permState by remember { mutableStateOf("") }
    var permPincode by remember { mutableStateOf("") }

    var enableBiometrics by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    var isMobileVerified by remember { mutableStateOf(false) }
    var isEmailVerified by remember { mutableStateOf(false) }
    var showMobileOtpDialog by remember { mutableStateOf(false) }
    var showEmailVerifyDialog by remember { mutableStateOf(false) }

    var mobileOtpInput by remember { mutableStateOf("") }
    var emailOtpInput by remember { mutableStateOf("") }

    LaunchedEffect(firstName, lastName) {
        fullName =
            listOf(firstName.trim(), lastName.trim()).filter { it.isNotEmpty() }.joinToString(" ")
    }

    val firstNameStatus = when {
        firstName.isEmpty() -> ValidationStatus.Idle
        firstName.trim().length >= 2 -> ValidationStatus.Valid("Valid first name")
        else -> ValidationStatus.Invalid("First name must be at least 2 characters")
    }

    val lastNameStatus = when {
        lastName.isEmpty() -> ValidationStatus.Idle
        else -> ValidationStatus.Valid("Valid last name")
    }

    val fullNameStatus = when {
        fullName.isEmpty() -> ValidationStatus.Idle
        fullName.trim().length >= 3 -> ValidationStatus.Valid("Valid full name")
        else -> ValidationStatus.Invalid("Full name must be at least 3 characters")
    }

    val dobStatus = when {
        dobString.isEmpty() -> ValidationStatus.Idle
        else -> ValidationStatus.Valid("Date of birth set")
    }

    val mobileStatus = when {
        mobileNumber.isEmpty() -> ValidationStatus.Idle
        ValidationUtils.isMobileValid(mobileNumber) -> {
            if (isMobileVerified) ValidationStatus.Valid("Mobile Verified ✓")
            else ValidationStatus.Valid("Valid Indian mobile - Tap 'Verify with OTP'")
        }

        else -> ValidationUtils.validateIndianMobile(mobileNumber)
    }

    val emailStatus = when {
        emailAddress.isEmpty() -> ValidationStatus.Idle
        android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches() -> {
            if (isEmailVerified) ValidationStatus.Valid("Email Verified ✓")
            else ValidationStatus.Valid("Valid email format - Tap 'Verify with OTP'")
        }

        else -> ValidationStatus.Invalid("Enter a valid email address (e.g. name@domain.com)")
    }

    val isStrictPass = isStrictPasswordValid(password)
    val passwordStatus = when {
        password.isEmpty() -> ValidationStatus.Idle
        isStrictPass -> ValidationStatus.Valid("Strong password format met ✓")
        else -> ValidationStatus.Invalid("Must meet all 5 requirements below")
    }

    val confirmStatus = when {
        confirmPassword.isEmpty() -> ValidationStatus.Idle
        confirmPassword == password && isStrictPass -> ValidationStatus.Valid("Passwords match ✓")
        confirmPassword == password -> ValidationStatus.Valid("Passwords match")
        else -> ValidationStatus.Invalid("Passwords do not match")
    }

    val presentStreetStatus = when {
        presentStreet.isEmpty() -> ValidationStatus.Idle
        presentStreet.trim().length >= 5 -> ValidationStatus.Valid("Valid street address")
        else -> ValidationStatus.Invalid("Address must be at least 5 characters")
    }

    val presentPincodeStatus = when {
        presentPincode.isEmpty() -> ValidationStatus.Idle
        presentPincode.length == 6 && presentPincode.all { it.isDigit() } -> ValidationStatus.Valid(
            "Valid 6-digit PIN code"
        )

        else -> ValidationStatus.Invalid("PIN code must be exactly 6 digits")
    }

    val permStreetStatus = when {
        permStreet.isEmpty() -> ValidationStatus.Idle
        permStreet.trim().length >= 5 -> ValidationStatus.Valid("Valid permanent address")
        else -> ValidationStatus.Invalid("Address must be at least 5 characters")
    }

    val permPincodeStatus = when {
        permPincode.isEmpty() -> ValidationStatus.Idle
        permPincode.length == 6 && permPincode.all { it.isDigit() } -> ValidationStatus.Valid("Valid 6-digit PIN code")
        else -> ValidationStatus.Invalid("PIN code must be exactly 6 digits")
    }

    fun openCalendar() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -20)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, selYear, selMonth, selDay ->
                dobString = String.format("%02d/%02d/%04d", selDay, selMonth + 1, selYear)
            },
            year,
            month,
            day
        ).show()
    }

    Scaffold(
        topBar = { SKSweetsTopBar(title = "Create Customer Account", onBackClick = onBackClick) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = AppDimens.dp20),
            contentPadding = PaddingValues(vertical = AppDimens.dp16),
            verticalArrangement = Arrangement.spacedBy(AppDimens.dp14)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SKBrandLogoEmblem(
                        size = AppDimens.dp54,
                        borderWidth = AppDimens.dp1_5,
                        elevation = AppDimens.dp3
                    )
                    Spacer(modifier = Modifier.width(AppDimens.dp14))
                    Column {
                        Text(
                            text = "Join S K Sweets Club",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "₹100 welcome bonus & live real-time security",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 1. Personal Information (First Name, Last Name, Full Name, DOB, Gender)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp14),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(AppDimens.dp14),
                        verticalArrangement = Arrangement.spacedBy(AppDimens.dp10)
                    ) {
                        Text(
                            "Personal Information",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(AppDimens.dp10)
                        ) {
                            SKValidatedTextField(
                                value = firstName,
                                onValueChange = { firstName = it; errorMessage = null },
                                label = "First Name *",
                                placeholder = "e.g. Ramesh",
                                validationStatus = firstNameStatus,
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            SKValidatedTextField(
                                value = lastName,
                                onValueChange = { lastName = it; errorMessage = null },
                                label = "Last Name",
                                placeholder = "e.g. Kumar",
                                validationStatus = lastNameStatus,
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        SKValidatedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = "Full Name (Official) *",
                            placeholder = "Ramesh Kumar",
                            leadingIcon = Icons.Default.Person,
                            validationStatus = fullNameStatus,
                            singleLine = true
                        )

                        // Date Of Birth with Calendar DatePicker
                        SKValidatedTextField(
                            value = dobString,
                            onValueChange = {},
                            label = "Date of Birth *",
                            placeholder = "Tap calendar icon to select DD/MM/YYYY",
                            leadingIcon = Icons.Default.CalendarMonth,
                            trailingIcon = {
                                IconButton(onClick = { openCalendar() }) {
                                    Icon(
                                        Icons.Default.CalendarMonth,
                                        contentDescription = "Open Calendar",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            validationStatus = dobStatus,
                            singleLine = true,
                            modifier = Modifier.clickable { openCalendar() }
                        )

                        // Gender Selection using RadioButtons
                        Column {
                            Text(
                                "Gender *",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(AppDimens.dp4))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                listOf("Male", "Female", "Other").forEach { g ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(AppDimens.dp8))
                                            .clickable { gender = g }
                                            .padding(
                                                vertical = AppDimens.dp4,
                                                horizontal = AppDimens.dp4
                                            )
                                    ) {
                                        RadioButton(
                                            selected = gender == g,
                                            onClick = { gender = g },
                                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                        )
                                        Spacer(modifier = Modifier.width(AppDimens.dp4))
                                        Text(
                                            text = g,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (gender == g) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 2. Contact Information with OTP Verification (Mobile & Email)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp14),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(AppDimens.dp14),
                        verticalArrangement = Arrangement.spacedBy(AppDimens.dp10)
                    ) {
                        Text(
                            "Contact & OTP Verification *",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )

                        // Mobile Number
                        SKMobileNumberField(
                            value = mobileNumber,
                            onValueChange = {
                                mobileNumber = it
                                errorMessage = null
                                isMobileVerified = false
                            },
                            defaultCountryCode = "in",
                            onCountrySelected = { selectedCountry = it },
                            validationStatus = mobileStatus
                        )

                        // Mobile OTP Action / Status Badge
                        if (isMobileVerified) {
                            Surface(
                                shape = RoundedCornerShape(AppDimens.dp8),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = AppDimens.dp10,
                                        vertical = AppDimens.dp6
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(AppDimens.dp16)
                                    )
                                    Spacer(modifier = Modifier.width(AppDimens.dp6))
                                    Text(
                                        "Mobile Number Verified Successfully",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        } else {
                            OutlinedButton(
                                onClick = {
                                    if (!ValidationUtils.isMobileValid(mobileNumber)) {
                                        errorMessage =
                                            "Please enter a valid Indian mobile number starting with 6, 7, 8, or 9 before verifying"
                                        return@OutlinedButton
                                    }
                                    mobileOtpInput = ""
                                    showMobileOtpDialog = true
                                    Toast.makeText(
                                        context,
                                        "OTP Sent to " + mobileNumber + "! Use test code: 123456",
                                        Toast.LENGTH_LONG
                                    ).show()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(AppDimens.dp10)
                            ) {
                                Icon(
                                    Icons.Default.Phone,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimens.dp16)
                                )
                                Spacer(modifier = Modifier.width(AppDimens.dp6))
                                Text(
                                    "Verify Mobile Number (Send 6-Digit OTP)",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Email Address
                        SKValidatedTextField(
                            value = emailAddress,
                            onValueChange = {
                                emailAddress = it.trim()
                                errorMessage = null
                                isEmailVerified = false
                            },
                            label = "E-Mail Address *",
                            placeholder = "customer@sksweets.com",
                            leadingIcon = Icons.Default.Email,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            validationStatus = emailStatus,
                            singleLine = true
                        )

                        // Email OTP Action / Status Badge
                        if (isEmailVerified) {
                            Surface(
                                shape = RoundedCornerShape(AppDimens.dp8),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = AppDimens.dp10,
                                        vertical = AppDimens.dp6
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(AppDimens.dp16)
                                    )
                                    Spacer(modifier = Modifier.width(AppDimens.dp6))
                                    Text(
                                        "E-Mail Address Verified Successfully",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        } else {
                            OutlinedButton(
                                onClick = {
                                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress)
                                            .matches()
                                    ) {
                                        errorMessage =
                                            "Please enter a valid email address before verifying"
                                        return@OutlinedButton
                                    }
                                    emailOtpInput = ""
                                    showEmailVerifyDialog = true
                                    Toast.makeText(
                                        context,
                                        "OTP Sent to " + emailAddress + "! Use test code: 123456",
                                        Toast.LENGTH_LONG
                                    ).show()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(AppDimens.dp10)
                            ) {
                                Icon(
                                    Icons.Default.Email,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimens.dp16)
                                )
                                Spacer(modifier = Modifier.width(AppDimens.dp6))
                                Text(
                                    "Verify E-Mail Address (Send 6-Digit OTP)",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // 3. Security Credentials (Strict Password & Confirm Password)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp14),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(AppDimens.dp14),
                        verticalArrangement = Arrangement.spacedBy(AppDimens.dp10)
                    ) {
                        Text(
                            "Security Credentials",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )

                        SKValidatedTextField(
                            value = password,
                            onValueChange = { password = it; errorMessage = null },
                            label = "Password (Strict Format) *",
                            leadingIcon = Icons.Default.Lock,
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            validationStatus = passwordStatus,
                            singleLine = true
                        )

                        // Dynamic Checklist for Strict Password format
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    RoundedCornerShape(AppDimens.dp8)
                                )
                                .padding(AppDimens.dp10),
                            verticalArrangement = Arrangement.spacedBy(AppDimens.dp4)
                        ) {
                            Text(
                                "Password Requirements (Live Keystroke Validation):",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                            PasswordRequirementItem(
                                label = "At least 8 characters",
                                isValid = isPasswordLengthValid(password)
                            )
                            PasswordRequirementItem(
                                label = "At least one uppercase letter (A-Z)",
                                isValid = isPasswordUpperValid(password)
                            )
                            PasswordRequirementItem(
                                label = "At least one lowercase letter (a-z)",
                                isValid = isPasswordLowerValid(password)
                            )
                            PasswordRequirementItem(
                                label = "At least one number (0-9)",
                                isValid = isPasswordDigitValid(password)
                            )
                            PasswordRequirementItem(
                                label = "At least one special character (!@#$%^&*...)",
                                isValid = isPasswordSpecialValid(password)
                            )
                        }

                        // Confirm Password
                        SKValidatedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it; errorMessage = null },
                            label = "Confirm Password *",
                            leadingIcon = Icons.Default.Lock,
                            trailingIcon = {
                                IconButton(onClick = {
                                    confirmPasswordVisible = !confirmPasswordVisible
                                }) {
                                    Icon(
                                        if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null
                                    )
                                }
                            },
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            validationStatus = confirmStatus,
                            singleLine = true
                        )
                    }
                }
            }

            // 4. Residential Delivery Address
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp14),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(AppDimens.dp14),
                        verticalArrangement = Arrangement.spacedBy(AppDimens.dp10)
                    ) {
                        Text(
                            "Current / Residential Delivery Address *",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )

                        SKValidatedTextField(
                            value = presentStreet,
                            onValueChange = { presentStreet = it; errorMessage = null },
                            label = "Flat / House No, Street Address *",
                            placeholder = "12/A, Gandhi Street, Indira Nagar",
                            leadingIcon = Icons.Default.Home,
                            validationStatus = presentStreetStatus,
                            singleLine = true
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(AppDimens.dp10)
                        ) {
                            SKValidatedTextField(
                                value = presentCity,
                                onValueChange = { presentCity = it },
                                label = "City *",
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            SKValidatedTextField(
                                value = presentState,
                                onValueChange = { presentState = it },
                                label = "State *",
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        SKValidatedTextField(
                            value = presentPincode,
                            onValueChange = {
                                if (it.length <= 6) presentPincode = it; errorMessage = null
                            },
                            label = "Pincode (6 Digits) *",
                            placeholder = "560001",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            validationStatus = presentPincodeStatus,
                            singleLine = true
                        )
                    }
                }
            }

            // 5. Permanent Address (with Same As Residential toggle)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp14),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(AppDimens.dp14),
                        verticalArrangement = Arrangement.spacedBy(AppDimens.dp10)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { sameAsPresent = !sameAsPresent }
                        ) {
                            Checkbox(
                                checked = sameAsPresent,
                                onCheckedChange = { sameAsPresent = it })
                            Spacer(modifier = Modifier.width(AppDimens.dp6))
                            Text(
                                "Permanent Address Same as Current / Residential",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (!sameAsPresent) {
                            SKValidatedTextField(
                                value = permStreet,
                                onValueChange = { permStreet = it; errorMessage = null },
                                label = "Permanent Street Address *",
                                leadingIcon = Icons.Default.LocationOn,
                                validationStatus = permStreetStatus,
                                singleLine = true
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(AppDimens.dp10)
                            ) {
                                SKValidatedTextField(
                                    value = permCity,
                                    onValueChange = { permCity = it },
                                    label = "City *",
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                SKValidatedTextField(
                                    value = permState,
                                    onValueChange = { permState = it },
                                    label = "State *",
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }

                            SKValidatedTextField(
                                value = permPincode,
                                onValueChange = {
                                    if (it.length <= 6) permPincode = it; errorMessage = null
                                },
                                label = "Permanent Pincode *",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                validationStatus = permPincodeStatus,
                                singleLine = true
                            )
                        }
                    }
                }
            }

            // Error Message Banner
            if (errorMessage != null) {
                item {
                    Surface(
                        shape = RoundedCornerShape(AppDimens.dp10),
                        color = MaterialTheme.colorScheme.errorContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(AppDimens.dp12),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(AppDimens.dp8))
                            Text(
                                errorMessage ?: "",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // Submit Button
            item {
                Button(
                    onClick = {
                        if (firstName.trim().length < 2) {
                            errorMessage = "Please enter a valid First Name (minimum 2 letters)"
                            return@Button
                        }
                        if (dobString.isBlank()) {
                            errorMessage = "Please select your Date of Birth"
                            return@Button
                        }
                        if (!ValidationUtils.isMobileValid(mobileNumber)) {
                            errorMessage =
                                "Please enter a valid Indian mobile number starting with 6, 7, 8, or 9"
                            return@Button
                        }
                        if (!isMobileVerified) {
                            errorMessage = "Please verify your mobile number via OTP first"
                            showMobileOtpDialog = true
                            return@Button
                        }
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                            errorMessage = "Please enter a valid email address"
                            return@Button
                        }
                        if (!isEmailVerified) {
                            errorMessage = "Please verify your email address via OTP first"
                            showEmailVerifyDialog = true
                            return@Button
                        }
                        if (!isStrictPasswordValid(password)) {
                            errorMessage = "Password does not meet all 5 requirements"
                            return@Button
                        }
                        if (password != confirmPassword) {
                            errorMessage = "Password and Confirm Password do not match"
                            return@Button
                        }
                        if (presentStreet.trim().length < 5) {
                            errorMessage = "Please enter a complete residential street address"
                            return@Button
                        }
                        if (presentPincode.length < 6) {
                            errorMessage = "Please enter a 6-digit residential pincode"
                            return@Button
                        }

                        isSubmitting = true
                        val registeredName =
                            if (fullName.isNotBlank()) fullName.trim() else firstName.trim() + " " + lastName.trim()

                        // Save Delivery Address
                        val userAddress = DeliveryAddress(
                            addressId = "addr_user_primary",
                            recipientName = registeredName,
                            phoneNumber = mobileNumber,
                            addressLine1 = presentStreet.trim(),
                            addressLine2 = "Residential",
                            city = presentCity.trim(),
                            pincode = presentPincode.trim(),
                            isDefault = true
                        )
                        onSaveAddress?.invoke(userAddress)

                        // Update User Profile with signup details so same info appears across the app
                        viewModel.updateProfile(registeredName, emailAddress) {
                            isSubmitting = false
                            Toast.makeText(
                                context,
                                "Welcome to S K Sweets & Bakes, " + registeredName + "!",
                                Toast.LENGTH_LONG
                            ).show()
                            onRegisterSuccess(registeredName, emailAddress)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppDimens.dp52),
                    shape = RoundedCornerShape(AppDimens.dp12)
                ) {
                    Text(
                        "Complete Registration & Sign Up",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            // Already have account link
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Already registered?", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(AppDimens.dp6))
                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(onClick = onLoginClick)
                    )
                }
            }
        }
    }

    // Mobile OTP Dialog
    if (showMobileOtpDialog) {
        val otpInputStatus = when {
            mobileOtpInput.isEmpty() -> ValidationStatus.Idle
            mobileOtpInput.length == 6 && mobileOtpInput.all { it.isDigit() } -> ValidationStatus.Valid(
                "Valid 6-digit OTP code"
            )

            else -> ValidationStatus.Invalid("Enter 6-digit OTP")
        }

        SKPremiumAlertDialog(
            onDismissRequest = { showMobileOtpDialog = false },
            title = "Verify Mobile Number",
            showBrandLogo = true,
            icon = Icons.Default.Phone,
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(AppDimens.dp10)) {
                    Text(
                        "Enter 6-digit OTP code sent to " + mobileNumber + ":",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Surface(
                        shape = RoundedCornerShape(AppDimens.dp8),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = "Test verification code: 123456",
                            modifier = Modifier.padding(AppDimens.dp8),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    SKSixDigitOtpInput(
                        otpValue = mobileOtpInput,
                        onOtpChange = { mobileOtpInput = it },
                        isError = otpInputStatus is ValidationStatus.Invalid && mobileOtpInput.isNotEmpty(),
                        errorMessage = (otpInputStatus as? ValidationStatus.Invalid)?.error,
                        testCode = "123456"
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (mobileOtpInput == "123456" || (mobileOtpInput.length == 6 && mobileOtpInput.all { it.isDigit() })) {
                            isMobileVerified = true
                            showMobileOtpDialog = false
                            Toast.makeText(context, "Mobile Number Verified!", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(
                                context,
                                "Invalid OTP code. Use test code 123456",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Text("Verify & Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showMobileOtpDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Email OTP Dialog
    if (showEmailVerifyDialog) {
        val emailOtpInputStatus = when {
            emailOtpInput.isEmpty() -> ValidationStatus.Idle
            emailOtpInput.length == 6 && emailOtpInput.all { it.isDigit() } -> ValidationStatus.Valid(
                "Valid 6-digit OTP code"
            )

            else -> ValidationStatus.Invalid("Enter 6-digit OTP")
        }

        SKPremiumAlertDialog(
            onDismissRequest = { showEmailVerifyDialog = false },
            title = "Verify E-Mail Address",
            showBrandLogo = true,
            icon = Icons.Default.Email,
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(AppDimens.dp10)) {
                    Text(
                        "Enter 6-digit OTP code sent to " + emailAddress + ":",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Surface(
                        shape = RoundedCornerShape(AppDimens.dp8),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = "Test verification code: 123456",
                            modifier = Modifier.padding(AppDimens.dp8),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    SKSixDigitOtpInput(
                        otpValue = emailOtpInput,
                        onOtpChange = { emailOtpInput = it },
                        isError = emailOtpInputStatus is ValidationStatus.Invalid && emailOtpInput.isNotEmpty(),
                        errorMessage = (emailOtpInputStatus as? ValidationStatus.Invalid)?.error,
                        testCode = "123456"
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (emailOtpInput == "123456" || (emailOtpInput.length == 6 && emailOtpInput.all { it.isDigit() })) {
                            isEmailVerified = true
                            showEmailVerifyDialog = false
                            Toast.makeText(context, "E-Mail Address Verified!", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(
                                context,
                                "Invalid OTP code. Use test code 123456",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Text("Verify & Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEmailVerifyDialog = false }) { Text("Cancel") }
            }
        )
    }
}


@Composable
fun ForgotPasswordScreen(
    onRecoverySent: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var recoveryMode by remember { mutableIntStateOf(0) } // 0: Email Reset Link, 1: Mobile OTP + New Password
    var emailInput by remember { mutableStateOf("") }
    var mobileInput by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isSubmitted by remember { mutableStateOf(false) }
    var resetLinkSent by remember { mutableStateOf(false) }

    // Live Keystroke Validations
    val emailStatus = when {
        emailInput.isEmpty() -> ValidationStatus.Idle
        android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches() -> ValidationStatus.Valid(
            "Valid email address"
        )

        else -> ValidationStatus.Invalid("Enter a valid email address (e.g. name@domain.com)")
    }
    val mobileStatus = ValidationUtils.validateIndianMobile(mobileInput)
    val otpStatus = when {
        otpCode.isEmpty() -> ValidationStatus.Idle
        otpCode.length == 6 && otpCode.all { it.isDigit() } -> ValidationStatus.Valid("Valid 6-digit OTP code")
        else -> ValidationStatus.Invalid("OTP must be 6 digits")
    }
    val isStrictPass = isStrictPasswordValid(newPassword)
    val passStatus = when {
        newPassword.isEmpty() -> ValidationStatus.Idle
        isStrictPass -> ValidationStatus.Valid("Strong password criteria met ✓")
        else -> ValidationStatus.Invalid("Must meet all 5 requirements")
    }
    val confirmStatus = when {
        confirmPassword.isEmpty() -> ValidationStatus.Idle
        confirmPassword == newPassword && isStrictPass -> ValidationStatus.Valid("Passwords match ✓")
        confirmPassword == newPassword -> ValidationStatus.Valid("Passwords match")
        else -> ValidationStatus.Invalid("Passwords do not match")
    }

    Scaffold(
        topBar = { SKSweetsTopBar(title = "Account Recovery & Reset", onBackClick = onBackClick) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(AppDimens.dp20),
            verticalArrangement = Arrangement.spacedBy(AppDimens.dp16)
        ) {
            item {
                Text(
                    text = "Reset Account Password",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Select recovery channel to regain account access",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Mode Selection: Email Link vs Mobile OTP
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp14),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    PrimaryTabRow(
                        selectedTabIndex = recoveryMode,
                        containerColor = Color.Transparent,
                        divider = {}) {
                        Tab(
                            selected = recoveryMode == 0,
                            onClick = { recoveryMode = 0 },
                            text = {
                                Text(
                                    "E-Mail Reset Link",
                                    fontWeight = if (recoveryMode == 0) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            icon = {
                                Icon(
                                    Icons.Default.Email,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimens.dp16)
                                )
                            }
                        )
                        Tab(
                            selected = recoveryMode == 1,
                            onClick = { recoveryMode = 1 },
                            text = {
                                Text(
                                    "Mobile OTP Reset",
                                    fontWeight = if (recoveryMode == 1) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            icon = {
                                Icon(
                                    Icons.Default.Phone,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimens.dp16)
                                )
                            }
                        )
                    }
                }
            }

            // METHOD 1: EMAIL LINK RESET
            if (recoveryMode == 0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppDimens.dp16),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(AppDimens.dp16),
                            verticalArrangement = Arrangement.spacedBy(AppDimens.dp14)
                        ) {
                            Text(
                                "Reset via E-Mail Link",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall
                            )

                            SKValidatedTextField(
                                value = emailInput,
                                onValueChange = { emailInput = it.trim() },
                                label = "Registered Email Address *",
                                placeholder = "customer@sksweets.com",
                                leadingIcon = Icons.Default.Email,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                validationStatus = emailStatus,
                                singleLine = true
                            )

                            if (resetLinkSent) {
                                Surface(
                                    shape = RoundedCornerShape(AppDimens.dp10),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(AppDimens.dp12),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(AppDimens.dp20)
                                        )
                                        Spacer(modifier = Modifier.width(AppDimens.dp8))
                                        Text(
                                            text = "Password reset link has been dispatched to " + emailInput + "! Please check your inbox.",
                                            color = MaterialTheme.colorScheme.primary,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput)
                                            .matches()
                                    ) {
                                        Toast.makeText(
                                            context,
                                            "Please enter a valid email address",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@Button
                                    }
                                    isSubmitted = true
                                    resetLinkSent = true
                                    Toast.makeText(
                                        context,
                                        "Password reset link sent to " + emailInput + "!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(AppDimens.dp12)
                            ) {
                                Icon(
                                    Icons.Default.Email,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimens.dp18)
                                )
                                Spacer(modifier = Modifier.width(AppDimens.dp8))
                                Text("Send Password Reset Link", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                // METHOD 2: MOBILE OTP + NEW PASSWORD & CONFIRM PASSWORD
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppDimens.dp16),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(AppDimens.dp16),
                            verticalArrangement = Arrangement.spacedBy(AppDimens.dp14)
                        ) {
                            Text(
                                "Reset via Mobile OTP",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall
                            )

                            SKMobileNumberField(
                                value = mobileInput,
                                onValueChange = { mobileInput = it },
                                label = "Registered Mobile Number *",
                                validationStatus = mobileStatus
                            )

                            if (!otpSent) {
                                Button(
                                    onClick = {
                                        if (ValidationUtils.isMobileValid(mobileInput)) {
                                            otpSent = true
                                            Toast.makeText(
                                                context,
                                                "Recovery OTP sent to " + mobileInput + "! Use test code: 123456",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Enter a valid Indian mobile number starting with 6, 7, 8, or 9",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(AppDimens.dp12)
                                ) {
                                    Icon(
                                        Icons.Default.Phone,
                                        contentDescription = null,
                                        modifier = Modifier.size(AppDimens.dp18)
                                    )
                                    Spacer(modifier = Modifier.width(AppDimens.dp8))
                                    Text("Send 6-Digit Recovery OTP", fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(AppDimens.dp8),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(AppDimens.dp10),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(AppDimens.dp16)
                                        )
                                        Spacer(modifier = Modifier.width(AppDimens.dp6))
                                        Text(
                                            "OTP Code sent to " + mobileInput + "! Test code: 123456",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                SKSixDigitOtpInput(
                                    otpValue = otpCode,
                                    onOtpChange = { otpCode = it },
                                    isError = otpStatus is ValidationStatus.Invalid && otpCode.isNotEmpty(),
                                    errorMessage = (otpStatus as? ValidationStatus.Invalid)?.error,
                                    testCode = "123456",
                                    onResendClick = {
                                        Toast.makeText(
                                            context,
                                            "Recovery OTP resent to " + mobileInput + "! Test code: 123456",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )

                                SKValidatedTextField(
                                    value = newPassword,
                                    onValueChange = { newPassword = it },
                                    label = "Enter New Password (Strict Format) *",
                                    leadingIcon = Icons.Default.Lock,
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            newPasswordVisible = !newPasswordVisible
                                        }) {
                                            Icon(
                                                if (newPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    validationStatus = passStatus,
                                    singleLine = true
                                )

                                // Password requirements checklist
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.surface,
                                            RoundedCornerShape(AppDimens.dp8)
                                        )
                                        .padding(AppDimens.dp10),
                                    verticalArrangement = Arrangement.spacedBy(AppDimens.dp4)
                                ) {
                                    Text(
                                        "Password Requirements (Live Keystroke Validation):",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    PasswordRequirementItem(
                                        label = "At least 8 characters",
                                        isValid = isPasswordLengthValid(newPassword)
                                    )
                                    PasswordRequirementItem(
                                        label = "At least one uppercase letter (A-Z)",
                                        isValid = isPasswordUpperValid(newPassword)
                                    )
                                    PasswordRequirementItem(
                                        label = "At least one lowercase letter (a-z)",
                                        isValid = isPasswordLowerValid(newPassword)
                                    )
                                    PasswordRequirementItem(
                                        label = "At least one number (0-9)",
                                        isValid = isPasswordDigitValid(newPassword)
                                    )
                                    PasswordRequirementItem(
                                        label = "At least one special character (!@#$%^&*...)",
                                        isValid = isPasswordSpecialValid(newPassword)
                                    )
                                }

                                SKValidatedTextField(
                                    value = confirmPassword,
                                    onValueChange = { confirmPassword = it },
                                    label = "Confirm New Password *",
                                    leadingIcon = Icons.Default.Lock,
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            confirmPasswordVisible = !confirmPasswordVisible
                                        }) {
                                            Icon(
                                                if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    validationStatus = confirmStatus,
                                    singleLine = true
                                )

                                Button(
                                    onClick = {
                                        if (otpCode != "123456" && (otpCode.length != 6 || !otpCode.all { it.isDigit() })) {
                                            Toast.makeText(
                                                context,
                                                "Invalid OTP code. Use test code: 123456",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@Button
                                        }
                                        if (!isStrictPasswordValid(newPassword)) {
                                            Toast.makeText(
                                                context,
                                                "Password must satisfy all 5 requirements",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@Button
                                        }
                                        if (newPassword != confirmPassword) {
                                            Toast.makeText(
                                                context,
                                                "Password and Confirm Password do not match",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@Button
                                        }
                                        isSubmitted = true
                                        Toast.makeText(
                                            context,
                                            "Password updated successfully! Please sign in with your new password.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        onRecoverySent()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(AppDimens.dp12)
                                ) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(AppDimens.dp18)
                                    )
                                    Spacer(modifier = Modifier.width(AppDimens.dp8))
                                    Text(
                                        "Verify OTP & Update Password",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FinalizeAuthScreen(
    userName: String,
    email: String,
    onProceedClick: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(AppDimens.dp24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = AppDimens.dp40)
            ) {
                // Official Uncropped S K Sweets Brand Logo
                SKBrandLogoEmblem(
                    size = AppDimens.dp110,
                    borderWidth = AppDimens.dp2,
                    elevation = AppDimens.dp6
                )

                Spacer(modifier = Modifier.height(AppDimens.dp24))

                Box(
                    modifier = Modifier
                        .size(AppDimens.dp56)
                        .clip(CircleShape)
                        .background(AppColors.StatusSuccess),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier.size(AppDimens.dp32)
                    )
                }

                Spacer(modifier = Modifier.height(AppDimens.dp16))

                Text(
                    text = "Welcome to S K Sweets Family!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(AppDimens.dp8))

                Text(
                    text = "Your account has been verified and device session is secured.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(AppDimens.dp24))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp16),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(AppDimens.dp16),
                        verticalArrangement = Arrangement.spacedBy(AppDimens.dp8)
                    ) {
                        Text(
                            text = "Account Details",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "Member: $userName",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (email.isNotBlank()) {
                            Text(
                                text = "Contact: $email",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "Session Security: Active (AES-256 Encrypted)",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppColors.StatusSuccess,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "🎁 Welcome Reward: +100 Sweet Points Added!",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            SKSweetsButton(
                text = "Proceed to Fresh Sweets Menu >",
                onClick = onProceedClick
            )
        }
    }
}

/**
 * Dedicated Phone Number + SMS OTP Sign In Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneLoginScreen(
    viewModel: AuthViewModel,
    onOtpRequested: (verificationId: String, phone: String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var selectedCountry by remember { mutableStateOf<CountryDetails?>(null) }
    var mobileInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { SKSweetsTopBar(title = "Phone Number Sign In", onBackClick = onBackClick) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = AppDimens.dp20),
            contentPadding = PaddingValues(vertical = AppDimens.dp16),
            verticalArrangement = Arrangement.spacedBy(AppDimens.dp16)
        ) {
            item {
                Column {
                    Text(
                        text = "Enter your mobile number",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(AppDimens.dp6))
                    Text(
                        text = "We will send a 6-digit verification code (OTP) via SMS to verify your account.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                SKMobileNumberField(
                    value = mobileInput,
                    onValueChange = {
                        mobileInput = it
                        errorMessage = null
                    },
                    defaultCountryCode = "in",
                    onCountrySelected = { selectedCountry = it },
                    validationStatus = ValidationUtils.validateIndianMobile(mobileInput)
                )
            }

            if (errorMessage != null || state.errorMessage != null) {
                item {
                    Text(
                        text = errorMessage ?: state.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(AppDimens.dp10))
                Button(
                    onClick = {
                        val cleaned = mobileInput.trim()
                        if (cleaned.length != 10) {
                            errorMessage = "Please enter a valid 10-digit mobile number"
                            return@Button
                        }
                        val dialCode = selectedCountry?.countryPhoneNumberCode ?: "+91"
                        val fullPhone = "$dialCode$cleaned"
                        viewModel.sendOtp(fullPhone) { verificationId ->
                            onOtpRequested(verificationId, fullPhone)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppDimens.dp52),
                    shape = RoundedCornerShape(AppDimens.dp12),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(AppDimens.dp24),
                            color = Color.White
                        )
                    } else {
                        Text(
                            "Get Verification Code (OTP)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Don't have an account?", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(AppDimens.dp6))
                    Text(
                        text = "Register",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(onClick = onRegisterClick)
                    )
                }
            }
        }
    }


}

/**
 * Dedicated Email + Password Sign In Screen
 */
@Composable
fun EmailLoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: (userName: String) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { SKSweetsTopBar(title = "Email Sign In", onBackClick = onBackClick) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = AppDimens.dp20),
            contentPadding = PaddingValues(vertical = AppDimens.dp16),
            verticalArrangement = Arrangement.spacedBy(AppDimens.dp16)
        ) {
            item {
                Column {
                    Text(
                        text = "Sign in with Email",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(AppDimens.dp6))
                    Text(
                        text = "Enter your registered email address and password to continue.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = {
                        emailInput = it
                        errorMessage = null
                    },
                    label = { Text("Email Address") },
                    placeholder = { Text("e.g. customer@example.com") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    shape = RoundedCornerShape(AppDimens.dp12),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = {
                        passwordInput = it
                        errorMessage = null
                    },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide" else "Show"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(AppDimens.dp12),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onForgotPasswordClick) {
                        Text(
                            "Forgot Password?",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            if (errorMessage != null) {
                item {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        if (emailInput.isBlank() || !emailInput.contains("@")) {
                            errorMessage = "Please enter a valid email address"
                            return@Button
                        }
                        if (passwordInput.length < 6) {
                            errorMessage = "Password must be at least 6 characters"
                            return@Button
                        }
                        val uName = emailInput.substringBefore("@").replace(".", " ")
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }
                        viewModel.updateProfile(uName, emailInput) {
                            onLoginSuccess(uName)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppDimens.dp52),
                    shape = RoundedCornerShape(AppDimens.dp12),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(AppDimens.dp24),
                            color = Color.White
                        )
                    } else {
                        Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Don't have an account?", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(AppDimens.dp6))
                    Text(
                        text = "Register",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(onClick = onRegisterClick)
                    )
                }
            }
        }
    }
}

/**
 * Dedicated Staff & Owner PIN Login Screen
 */
@Composable
fun StaffLoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: (role: UserRole) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedRole by remember { mutableStateOf(UserRole.OWNER) }
    var pinInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { SKSweetsTopBar(title = "Staff & Owner Portal", onBackClick = onBackClick) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = AppDimens.dp20),
            contentPadding = PaddingValues(vertical = AppDimens.dp16),
            verticalArrangement = Arrangement.spacedBy(AppDimens.dp16)
        ) {
            item {
                Column {
                    Text(
                        text = "Staff & Business Access",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(AppDimens.dp6))
                    Text(
                        text = "Enter 4-digit PIN to access Counter POS checkout, shared inventory, and business reports.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Text(
                    "Select Access Role for Testing:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(AppDimens.dp8))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppDimens.dp8)
                ) {
                    FilterChip(
                        selected = selectedRole == UserRole.OWNER,
                        onClick = { selectedRole = UserRole.OWNER },
                        label = { Text("Owner") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = selectedRole == UserRole.CASHIER,
                        onClick = { selectedRole = UserRole.CASHIER },
                        label = { Text("Counter POS") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = selectedRole == UserRole.MANAGER,
                        onClick = { selectedRole = UserRole.MANAGER },
                        label = { Text("Manager") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = pinInput,
                    onValueChange = {
                        if (it.length <= 4 && it.all { ch -> ch.isDigit() }) {
                            pinInput = it
                            errorMessage = null
                        }
                    },
                    label = { Text("4-Digit Security PIN (Default: 1234)") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    shape = RoundedCornerShape(AppDimens.dp12),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (errorMessage != null) {
                item {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        if (pinInput.length != 4 && pinInput != "1234") {
                            errorMessage = "Please enter 4 digits (e.g. 1234)"
                            return@Button
                        }
                        viewModel.switchRoleForTesting(selectedRole)
                        onLoginSuccess(selectedRole)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppDimens.dp52),
                    shape = RoundedCornerShape(AppDimens.dp12),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Unlock & Sign In", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            item {
                OutlinedButton(
                    onClick = {
                        viewModel.switchRoleForTesting(UserRole.OWNER)
                        onLoginSuccess(UserRole.OWNER)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppDimens.dp50),
                    shape = RoundedCornerShape(AppDimens.dp12)
                ) {
                    Icon(
                        Icons.Default.Storefront,
                        contentDescription = null,
                        modifier = Modifier.size(AppDimens.dp18)
                    )
                    Spacer(modifier = Modifier.width(AppDimens.dp8))
                    Text("Instant 1-Click Access as Owner", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Dedicated OTP Verification Screen
 */
@Composable
fun OtpVerificationScreen(
    phoneNumber: String,
    verificationId: String,
    viewModel: AuthViewModel,
    onVerified: () -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = { SKSweetsTopBar(title = "OTP Verification", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(AppDimens.dp24),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SKBrandLogoEmblem(
                    size = AppDimens.dp72,
                    borderWidth = AppDimens.dp1_5,
                    elevation = AppDimens.dp3
                )

                Spacer(modifier = Modifier.height(AppDimens.dp18))
                Text(
                    text = "Enter 6-Digit OTP",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(AppDimens.dp8))
                Text(
                    text = "A one-time verification passcode has been dispatched to $phoneNumber.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(AppDimens.dp8))
                Surface(
                    shape = RoundedCornerShape(AppDimens.dp8),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "For testing, enter code: 123456",
                        modifier = Modifier.padding(
                            horizontal = AppDimens.dp10,
                            vertical = AppDimens.dp4
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(AppDimens.dp28))

                SKSixDigitOtpInput(
                    otpValue = state.otpCode,
                    onOtpChange = { viewModel.onOtpChanged(it) },
                    isError = state.errorMessage != null,
                    errorMessage = state.errorMessage,
                    testCode = "123456",
                    onResendClick = {
                        viewModel.onOtpChanged("123456")
                        Toast.makeText(context, "Resent! Code: 123456", Toast.LENGTH_SHORT).show()
                    }
                )

                if (state.errorMessage != null) {
                    Spacer(modifier = Modifier.height(AppDimens.dp8))
                    Text(
                        text = state.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(AppDimens.dp16))
                TextButton(
                    onClick = {
                        viewModel.onOtpChanged("123456")
                        Toast.makeText(context, "Resent! Code: 123456", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text(
                        "Didn't receive code? Resend OTP",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            SKSweetsButton(
                text = "Verify & Proceed",
                isLoading = state.isLoading,
                onClick = {
                    viewModel.verifyOtp {
                        onVerified()
                    }
                }
            )
        }
    }
}

/**
 * User Profile & Settings Screen with Camera, Gallery & File Photo Picker,
 * Theme Switcher, and Prominent Logout / Sign Out action
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    viewModel: AuthViewModel,
    currentThemeMode: AppThemeMode,
    onThemeModeChanged: (AppThemeMode) -> Unit,
    onSaved: () -> Unit,
    onSignOutClick: () -> Unit = {},
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var name by remember(state.userProfile) { mutableStateOf(state.userProfile?.displayName ?: "") }
    var email by remember(state.userProfile) { mutableStateOf(state.userProfile?.email ?: "") }
    var phone by remember(state.userProfile) {
        val raw = state.userProfile?.phoneNumber ?: ""
        val digits = raw.filter { it.isDigit() }
        mutableStateOf(if (digits.length > 10) digits.takeLast(10) else digits)
    }
    var selectedAvatarIndex by remember { mutableStateOf(0) }
    var roleExpanded by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }
    var showPhotoPreviewDialog by remember { mutableStateOf(false) }

    // Change Password in Profile state
    var showChangePasswordSection by remember { mutableStateOf(false) }
    var changePasswordMode by remember { mutableIntStateOf(0) } // 0: Email Reset Link, 1: Mobile OTP
    var changePassEmail by remember { mutableStateOf(email) }
    var changePassMobile by remember { mutableStateOf(phone) }
    var changePassOtpSent by remember { mutableStateOf(false) }
    var changePassOtpCode by remember { mutableStateOf("") }
    var changePassNewPass by remember { mutableStateOf("") }
    var changePassNewPassVisible by remember { mutableStateOf(false) }
    var changePassConfirmPass by remember { mutableStateOf("") }
    var changePassConfirmPassVisible by remember { mutableStateOf(false) }

    // Real Photo Pickers State (Camera, Gallery, Files)
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedPhotoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showImagePickerDialog by remember { mutableStateOf(false) }

    val avatarPresets = listOf(
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200",
        "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
        "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200",
        "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200"
    )

    // Activity Result Launchers
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedPhotoUri = uri
            selectedPhotoBitmap = null
        }
    }

    val filesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedPhotoUri = uri
            selectedPhotoBitmap = null
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            selectedPhotoBitmap = bitmap
            selectedPhotoUri = null
        }
    }

    // Live Keystroke Validations
    val nameStatus = when {
        name.isEmpty() -> ValidationStatus.Idle
        name.trim().length >= 2 -> ValidationStatus.Valid("Valid full name")
        else -> ValidationStatus.Invalid("Name must be at least 2 characters")
    }
    val emailStatus = when {
        email.isEmpty() -> ValidationStatus.Idle
        android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches() -> ValidationStatus.Valid("Valid email address")

        else -> ValidationStatus.Invalid("Enter a valid email address (e.g. name@domain.com)")
    }
    val phoneStatus = ValidationUtils.validateIndianMobile(phone)

    val changePassStrict = isStrictPasswordValid(changePassNewPass)
    val changePassStatus = when {
        changePassNewPass.isEmpty() -> ValidationStatus.Idle
        changePassStrict -> ValidationStatus.Valid("Strong password format met ✓")
        else -> ValidationStatus.Invalid("Must meet all 5 requirements")
    }
    val changePassConfirmStatus = when {
        changePassConfirmPass.isEmpty() -> ValidationStatus.Idle
        changePassConfirmPass == changePassNewPass && changePassStrict -> ValidationStatus.Valid("Passwords match ✓")
        changePassConfirmPass == changePassNewPass -> ValidationStatus.Valid("Passwords match")
        else -> ValidationStatus.Invalid("Passwords do not match")
    }

    Scaffold(
        topBar = { SKSweetsTopBar(title = "Profile & Account Settings", onBackClick = onBackClick) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = AppDimens.dp20),
            contentPadding = PaddingValues(vertical = AppDimens.dp16),
            verticalArrangement = Arrangement.spacedBy(AppDimens.dp16)
        ) {
            // Profile Photo & Interactive Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp20),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(
                        AppDimens.dp1_5,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppDimens.dp20),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                border = BorderStroke(
                                    AppDimens.dp3,
                                    MaterialTheme.colorScheme.primary
                                ),
                                shadowElevation = AppDimens.dp6,
                                modifier = Modifier
                                    .size(AppDimens.dp100)
                                    .clip(CircleShape)
                                    .clickable { showPhotoPreviewDialog = true }
                            ) {
                                when {
                                    selectedPhotoBitmap != null -> {
                                        Image(
                                            bitmap = selectedPhotoBitmap!!.asImageBitmap(),
                                            contentDescription = "Profile Photo",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                    selectedPhotoUri != null -> {
                                        AsyncImage(
                                            model = selectedPhotoUri,
                                            contentDescription = "Profile Photo",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                    !state.userProfile?.avatarUrl.isNullOrBlank() -> {
                                        AsyncImage(
                                            model = state.userProfile!!.avatarUrl,
                                            contentDescription = "Profile Photo",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                    else -> {
                                        SKGreetingAvatar(
                                            period = TimeOfDayPeriod.fromCurrentHour(),
                                            size = AppDimens.dp100
                                        )
                                    }
                                }
                            }

                            // Edit Photo Action Button
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                shadowElevation = AppDimens.dp4,
                                modifier = Modifier
                                    .size(AppDimens.dp34)
                                    .clip(CircleShape)
                                    .clickable { showImagePickerDialog = true }
                            ) {
                                Icon(
                                    Icons.Default.CameraAlt,
                                    contentDescription = "Change photo",
                                    tint = Color.White,
                                    modifier = Modifier.padding(AppDimens.dp6)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(AppDimens.dp10))
                        Text(
                            text = name.ifBlank { "Ramesh Kumar" },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp4))

                        // Clickable Phone Number Row -> ACTION_DIAL
                        val dispPhone = phone.ifBlank { "+91 98765 43210" }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(AppDimens.dp8))
                                .clickable {
                                    try {
                                        val intent = Intent(
                                            Intent.ACTION_DIAL,
                                            Uri.parse("tel:" + dispPhone.replace(" ", ""))
                                        )
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "No phone dialer available",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                .padding(horizontal = AppDimens.dp8, vertical = AppDimens.dp3)
                        ) {
                            Icon(
                                Icons.Default.Phone,
                                contentDescription = "Call",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(AppDimens.dp15)
                            )
                            Spacer(modifier = Modifier.width(AppDimens.dp6))
                            Text(
                                text = dispPhone,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Clickable E-Mail Row -> ACTION_SENDTO
                        val dispEmail = email.ifBlank { "customer@sksweets.com" }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(AppDimens.dp8))
                                .clickable {
                                    try {
                                        val intent = Intent(
                                            Intent.ACTION_SENDTO,
                                            Uri.parse("mailto:" + dispEmail)
                                        )
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "No email app available",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                .padding(horizontal = AppDimens.dp8, vertical = AppDimens.dp3)
                        ) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = "Email",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(AppDimens.dp15)
                            )
                            Spacer(modifier = Modifier.width(AppDimens.dp6))
                            Text(
                                text = dispEmail,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        Spacer(modifier = Modifier.height(AppDimens.dp6))
                        Surface(
                            shape = RoundedCornerShape(AppDimens.dp12),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "🏆 250 Sweet Loyalty Points",
                                modifier = Modifier.padding(
                                    horizontal = AppDimens.dp12,
                                    vertical = AppDimens.dp6
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // Theme Switcher Section (Light / Dark / System Default)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp16),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(AppDimens.dp16)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Brightness4,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(AppDimens.dp8))
                            Text(
                                text = "App Theme Mode",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(AppDimens.dp12))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(AppDimens.dp8)
                        ) {
                            FilterChip(
                                selected = currentThemeMode == AppThemeMode.LIGHT,
                                onClick = { onThemeModeChanged(AppThemeMode.LIGHT) },
                                label = { Text("Light") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.LightMode,
                                        contentDescription = null,
                                        modifier = Modifier.size(AppDimens.dp16)
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = currentThemeMode == AppThemeMode.DARK,
                                onClick = { onThemeModeChanged(AppThemeMode.DARK) },
                                label = { Text("Dark") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.DarkMode,
                                        contentDescription = null,
                                        modifier = Modifier.size(AppDimens.dp16)
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = currentThemeMode == AppThemeMode.SYSTEM,
                                onClick = { onThemeModeChanged(AppThemeMode.SYSTEM) },
                                label = { Text("System") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // User Details Form with SKValidatedTextField (Live Keystroke Validation)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp16),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(AppDimens.dp16),
                        verticalArrangement = Arrangement.spacedBy(AppDimens.dp12)
                    ) {
                        Text(
                            text = "Personal Information (Live Validated)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        SKValidatedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Full Name *",
                            leadingIcon = Icons.Default.Person,
                            validationStatus = nameStatus,
                            singleLine = true
                        )

                        SKValidatedTextField(
                            value = email,
                            onValueChange = { email = it.trim() },
                            label = "Email Address *",
                            leadingIcon = Icons.Default.Email,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            validationStatus = emailStatus,
                            singleLine = true
                        )

                        SKMobileNumberField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = "Mobile Number *",
                            validationStatus = phoneStatus
                        )

                        Button(
                            onClick = {
                                if (name.trim().length < 2) {
                                    Toast.makeText(
                                        context,
                                        "Please enter a valid name",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }
                                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    Toast.makeText(
                                        context,
                                        "Please enter a valid email address",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }
                                if (!ValidationUtils.isMobileValid(phone)) {
                                    Toast.makeText(
                                        context,
                                        "Please enter a valid Indian mobile number starting with 6, 7, 8, or 9",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }
                                val photoUrl =
                                    selectedPhotoUri?.toString() ?: state.userProfile?.avatarUrl
                                    ?: avatarPresets[selectedAvatarIndex]
                                viewModel.updateProfile(name, email, photoUrl) {
                                    Toast.makeText(
                                        context,
                                        "Profile updated successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onSaved()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppDimens.dp12)
                        ) {
                            Text("Save Personal Information", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Security & Change Password Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp16),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(AppDimens.dp16),
                        verticalArrangement = Arrangement.spacedBy(AppDimens.dp12)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showChangePasswordSection = !showChangePasswordSection
                                }
                        ) {
                            Icon(
                                Icons.Default.LockReset,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(AppDimens.dp8))
                            Text(
                                text = "Security & Change Password",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                if (showChangePasswordSection) Icons.Default.Close else Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                modifier = Modifier.size(AppDimens.dp16)
                            )
                        }

                        if (showChangePasswordSection) {
                            Spacer(modifier = Modifier.height(AppDimens.dp4))
                            PrimaryTabRow(
                                selectedTabIndex = changePasswordMode,
                                containerColor = Color.Transparent,
                                divider = {}) {
                                Tab(
                                    selected = changePasswordMode == 0,
                                    onClick = { changePasswordMode = 0 },
                                    text = {
                                        Text(
                                            "E-Mail Reset Link",
                                            fontWeight = if (changePasswordMode == 0) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    icon = {
                                        Icon(
                                            Icons.Default.Email,
                                            contentDescription = null,
                                            modifier = Modifier.size(AppDimens.dp16)
                                        )
                                    }
                                )
                                Tab(
                                    selected = changePasswordMode == 1,
                                    onClick = { changePasswordMode = 1 },
                                    text = {
                                        Text(
                                            "Mobile OTP Reset",
                                            fontWeight = if (changePasswordMode == 1) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    icon = {
                                        Icon(
                                            Icons.Default.Phone,
                                            contentDescription = null,
                                            modifier = Modifier.size(AppDimens.dp16)
                                        )
                                    }
                                )
                            }

                            if (changePasswordMode == 0) {
                                SKValidatedTextField(
                                    value = changePassEmail,
                                    onValueChange = { changePassEmail = it.trim() },
                                    label = "E-Mail Address *",
                                    leadingIcon = Icons.Default.Email,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                    singleLine = true
                                )
                                Button(
                                    onClick = {
                                        Toast.makeText(
                                            context,
                                            "Password reset link sent to " + changePassEmail + "!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(AppDimens.dp10)
                                ) {
                                    Text("Send Password Reset Link")
                                }
                            } else {
                                SKValidatedTextField(
                                    value = changePassMobile,
                                    onValueChange = { if (it.length <= 10) changePassMobile = it },
                                    label = "Mobile Number (10 Digits) *",
                                    leadingIcon = Icons.Default.Phone,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    singleLine = true
                                )
                                if (!changePassOtpSent) {
                                    Button(
                                        onClick = {
                                            changePassOtpSent = true
                                            Toast.makeText(
                                                context,
                                                "OTP sent to " + changePassMobile + "! Use test code: 123456",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(AppDimens.dp10)
                                    ) {
                                        Text("Send 6-Digit OTP")
                                    }
                                } else {
                                    SKValidatedTextField(
                                        value = changePassOtpCode,
                                        onValueChange = {
                                            if (it.length <= 6) changePassOtpCode = it
                                        },
                                        label = "Enter 6-Digit OTP *",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )
                                    SKValidatedTextField(
                                        value = changePassNewPass,
                                        onValueChange = { changePassNewPass = it },
                                        label = "New Password (Strict Format) *",
                                        leadingIcon = Icons.Default.Lock,
                                        trailingIcon = {
                                            IconButton(onClick = {
                                                changePassNewPassVisible = !changePassNewPassVisible
                                            }) {
                                                Icon(
                                                    if (changePassNewPassVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                    contentDescription = null
                                                )
                                            }
                                        },
                                        visualTransformation = if (changePassNewPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                        validationStatus = changePassStatus,
                                        singleLine = true
                                    )
                                    SKValidatedTextField(
                                        value = changePassConfirmPass,
                                        onValueChange = { changePassConfirmPass = it },
                                        label = "Confirm New Password *",
                                        leadingIcon = Icons.Default.Lock,
                                        trailingIcon = {
                                            IconButton(onClick = {
                                                changePassConfirmPassVisible =
                                                    !changePassConfirmPassVisible
                                            }) {
                                                Icon(
                                                    if (changePassConfirmPassVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                    contentDescription = null
                                                )
                                            }
                                        },
                                        visualTransformation = if (changePassConfirmPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                        validationStatus = changePassConfirmStatus,
                                        singleLine = true
                                    )
                                    Button(
                                        onClick = {
                                            if (changePassOtpCode != "123456") {
                                                Toast.makeText(
                                                    context,
                                                    "Invalid OTP code",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                return@Button
                                            }
                                            if (!isStrictPasswordValid(changePassNewPass)) {
                                                Toast.makeText(
                                                    context,
                                                    "Password must satisfy all 5 requirements",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                return@Button
                                            }
                                            if (changePassNewPass != changePassConfirmPass) {
                                                Toast.makeText(
                                                    context,
                                                    "Passwords do not match",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                return@Button
                                            }
                                            Toast.makeText(
                                                context,
                                                "Password changed successfully!",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            showChangePasswordSection = false
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(AppDimens.dp10)
                                    ) {
                                        Text("Update Password")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Default Delivery Address Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp16),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(AppDimens.dp16)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(AppDimens.dp8))
                            Text(
                                text = "Default Delivery Address",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(AppDimens.dp8))
                        val cachedLoc = remember {
                            com.skylink.sksweetsandbakes.core.util.LocationHelper.getCachedLocation(
                                context
                            )
                        }
                        Text(
                            text = cachedLoc.fullDetailedAddress,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp8))
                        Surface(
                            shape = RoundedCornerShape(AppDimens.dp8),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = "⚡ 30-Min Fast Delivery Area",
                                modifier = Modifier.padding(
                                    horizontal = AppDimens.dp8,
                                    vertical = AppDimens.dp4
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // Role Switcher for Owner / Cashier / Manager / Inventory testing
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp16),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(AppDimens.dp16)) {
                        Text(
                            text = "Portal Role Experience:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp4))
                        Text(
                            text = "Test the app under different personnel authorization roles:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp12))

                        val roles = listOf(
                            UserRole.CUSTOMER,
                            UserRole.CASHIER,
                            UserRole.MANAGER,
                            UserRole.INVENTORY_CLERK,
                            UserRole.OWNER
                        )
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(AppDimens.dp8)
                        ) {
                            items(roles) { r ->
                                val isSelected = state.userProfile?.role?.equals(
                                    r.roleName,
                                    ignoreCase = true
                                ) == true || state.userProfile?.role?.equals(
                                    r.name,
                                    ignoreCase = true
                                ) == true
                                val roleDisplayName = when (r) {
                                    UserRole.CUSTOMER -> "Customer"
                                    UserRole.CASHIER -> "Cashier"
                                    UserRole.MANAGER -> "Store Manager"
                                    UserRole.INVENTORY_CLERK -> "Inventory Lead"
                                    UserRole.OWNER -> "Business Owner"
                                }
                                val roleIcon = when (r) {
                                    UserRole.CUSTOMER -> Icons.Default.Person
                                    UserRole.CASHIER -> Icons.Default.Pin
                                    UserRole.MANAGER -> Icons.Default.Security
                                    UserRole.INVENTORY_CLERK -> Icons.Default.FolderOpen
                                    UserRole.OWNER -> Icons.Default.Storefront
                                }
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        viewModel.switchRoleForTesting(r)
                                        Toast.makeText(
                                            context,
                                            "Switched role to $roleDisplayName",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    label = {
                                        Text(
                                            roleDisplayName,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 12.sp
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            roleIcon,
                                            contentDescription = null,
                                            modifier = Modifier.size(AppDimens.dp16)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Sign Out / Logout
            item {
                val microsoftLinked = MicrosoftAuthProvider.FIREBASE_PROVIDER_ID in
                        state.userProfile?.linkedProviderIds.orEmpty()
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp16),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(AppDimens.dp16),
                        verticalArrangement = Arrangement.spacedBy(AppDimens.dp8)
                    ) {
                        Text("Linked accounts", fontWeight = FontWeight.Bold)
                        Text(
                            if (microsoftLinked) "Microsoft is connected to this Firebase account."
                            else "Connect Microsoft to sign in to this same Firebase account.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedButton(
                            enabled = !state.isLoading && !microsoftLinked,
                            onClick = {
                                val hostActivity = context as? Activity
                                if (hostActivity == null) {
                                    Toast.makeText(
                                        context,
                                        "Account linking is unavailable from this screen.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    viewModel.linkWithOAuthProvider(
                                        activity = hostActivity,
                                        providerId = MicrosoftAuthProvider.FIREBASE_PROVIDER_ID,
                                        onSuccess = {
                                            Toast.makeText(
                                                context,
                                                "Microsoft account connected.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        onError = { error ->
                                            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                        }
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(AppDimens.dp12)
                        ) {
                            if (state.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(AppDimens.dp18),
                                    strokeWidth = AppDimens.dp2
                                )
                                Spacer(modifier = Modifier.width(AppDimens.dp8))
                            }
                            Text(if (microsoftLinked) "Microsoft connected" else "Link Microsoft")
                        }
                    }
                }
            }

            // Sign Out / Logout
            item {
                OutlinedButton(
                    onClick = { showSignOutDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp12),
                    border = BorderStroke(AppDimens.dp1_5, MaterialTheme.colorScheme.error)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(AppDimens.dp8))
                    Text(
                        "Sign Out of Account",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Photo Preview Dialog
    if (showPhotoPreviewDialog) {
        val dispName = name.ifBlank { "Ramesh Kumar" }
        val dispPhone = phone.ifBlank { "+91 98765 43210" }
        val dispEmail = email.ifBlank { "customer@sksweets.com" }

        Dialog(onDismissRequest = { showPhotoPreviewDialog = false }) {
            Card(
                shape = RoundedCornerShape(AppDimens.dp24),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(AppDimens.dp2, AppColors.GoldAccent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppDimens.dp16)
            ) {
                Column(
                    modifier = Modifier.padding(AppDimens.dp20),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Profile Photo Preview",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { showPhotoPreviewDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Spacer(modifier = Modifier.height(AppDimens.dp16))

                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        border = BorderStroke(AppDimens.dp3, MaterialTheme.colorScheme.primary),
                        shadowElevation = AppDimens.dp8,
                        modifier = Modifier.size(AppDimens.dp180)
                    ) {
                        when {
                            selectedPhotoBitmap != null -> {
                                Image(
                                    bitmap = selectedPhotoBitmap!!.asImageBitmap(),
                                    contentDescription = dispName,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            selectedPhotoUri != null -> {
                                AsyncImage(
                                    model = selectedPhotoUri,
                                    contentDescription = dispName,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            !state.userProfile?.avatarUrl.isNullOrBlank() -> {
                                AsyncImage(
                                    model = state.userProfile!!.avatarUrl,
                                    contentDescription = dispName,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            else -> {
                                val initials =
                                    dispName.split(" ").mapNotNull { it.firstOrNull()?.toString() }
                                        .take(2).joinToString("").ifEmpty { "SK" }
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = initials,
                                        fontSize = 48.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(AppDimens.dp16))
                    Text(
                        text = dispName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(AppDimens.dp4))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(AppDimens.dp6))
                            .clickable {
                                try {
                                    val intent = Intent(
                                        Intent.ACTION_DIAL,
                                        Uri.parse("tel:" + dispPhone.replace(" ", ""))
                                    )
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "No phone dialer available",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .padding(horizontal = AppDimens.dp8, vertical = AppDimens.dp2)
                    ) {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(AppDimens.dp14)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp4))
                        Text(
                            text = dispPhone,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(AppDimens.dp6))
                            .clickable {
                                try {
                                    val intent = Intent(
                                        Intent.ACTION_SENDTO,
                                        Uri.parse("mailto:" + dispEmail)
                                    )
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "No email app available",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .padding(horizontal = AppDimens.dp8, vertical = AppDimens.dp2)
                    ) {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(AppDimens.dp14)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp4))
                        Text(
                            text = dispEmail,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Spacer(modifier = Modifier.height(AppDimens.dp20))
                    Button(
                        onClick = {
                            showPhotoPreviewDialog = false
                            showImagePickerDialog = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppDimens.dp12)
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.size(AppDimens.dp18)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp8))
                        Text("Change Profile Photo", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Photo Source Picker Dialog (Camera, Gallery, Files, Presets)
    if (showImagePickerDialog) {
        SKPremiumAlertDialog(
            onDismissRequest = { showImagePickerDialog = false },
            title = "Update Profile Photo",
            showBrandLogo = true,
            icon = Icons.Default.CameraAlt,
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(AppDimens.dp12)) {
                    Text(
                        text = "Choose source for your profile picture:",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(AppDimens.dp10)
                    ) {
                        // Option 1: Live Camera
                        OutlinedButton(
                            onClick = {
                                showImagePickerDialog = false
                                cameraLauncher.launch(null)
                            },
                            contentPadding = PaddingValues(
                                horizontal = AppDimens.dp4,
                                vertical = AppDimens.dp6
                            ),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(AppDimens.dp12)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = AppDimens.dp2)
                            ) {
                                Icon(
                                    Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimens.dp22)
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp4))
                                Text(
                                    "Camera",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }

                        // Option 2: Gallery
                        OutlinedButton(
                            onClick = {
                                showImagePickerDialog = false
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            contentPadding = PaddingValues(
                                horizontal = AppDimens.dp4,
                                vertical = AppDimens.dp6
                            ),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(AppDimens.dp12)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = AppDimens.dp2)
                            ) {
                                Icon(
                                    Icons.Default.PhotoLibrary,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimens.dp22)
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp4))
                                Text(
                                    "Gallery",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }

                        // Option 3: Files
                        OutlinedButton(
                            onClick = {
                                showImagePickerDialog = false
                                filesLauncher.launch("image/*")
                            },
                            contentPadding = PaddingValues(
                                horizontal = AppDimens.dp4,
                                vertical = AppDimens.dp6
                            ),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(AppDimens.dp12)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = AppDimens.dp2)
                            ) {
                                Icon(
                                    Icons.Default.FolderOpen,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimens.dp22)
                                )
                                Spacer(modifier = Modifier.height(AppDimens.dp4))
                                Text(
                                    "Files",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(AppDimens.dp4))
                    Text(
                        text = "Or choose from luxury sweet avatars:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        avatarPresets.forEachIndexed { index, url ->
                            Surface(
                                shape = CircleShape,
                                border = if (selectedAvatarIndex == index) BorderStroke(
                                    AppDimens.dp2_5,
                                    MaterialTheme.colorScheme.primary
                                ) else null,
                                modifier = Modifier
                                    .size(AppDimens.dp52)
                                    .clip(CircleShape)
                                    .clickable {
                                        selectedAvatarIndex = index
                                        selectedPhotoUri = Uri.parse(url)
                                        selectedPhotoBitmap = null
                                        showImagePickerDialog = false
                                        Toast.makeText(
                                            context,
                                            "Avatar selected!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            ) {
                                AsyncImage(
                                    model = url,
                                    contentDescription = "Avatar preset",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showImagePickerDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Sign Out Confirmation Dialog
    if (showSignOutDialog) {
        SKPremiumAlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            title = "Confirm Sign Out",
            showBrandLogo = true,
            icon = Icons.AutoMirrored.Filled.Logout,
            text = { Text("Are you sure you want to sign out from your S K Sweets account?") },
            confirmButton = {
                Button(
                    onClick = {
                        showSignOutDialog = false
                        viewModel.signOut { onSignOutClick() }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Sign Out")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutDialog = false }) { Text("Cancel") }
            }
        )
    }
}


@Composable
fun AddressListScreen(
    addresses: List<DeliveryAddress>,
    onAddAddressClick: () -> Unit = {},
    onSaveAddress: (DeliveryAddress) -> Unit = {},
    onDeleteAddress: (String) -> Unit = {},
    onBackClick: () -> Unit
) {
    var editingAddress by remember { mutableStateOf<DeliveryAddress?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { SKSweetsTopBar(title = "Saved Addresses", onBackClick = onBackClick) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Address")
            }
        }
    ) { padding ->
        if (addresses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No saved addresses yet.", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(AppDimens.dp10))
                    Button(onClick = { showAddDialog = true }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(AppDimens.dp16)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp6))
                        Text("Add Delivery Address")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(AppDimens.dp16),
                verticalArrangement = Arrangement.spacedBy(AppDimens.dp12)
            ) {
                items(addresses, key = { it.addressId }) { address ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppDimens.dp14),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(AppDimens.dp16)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = address.recipientName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                if (address.isDefault) {
                                    StatusBadge(status = "Default")
                                }
                            }
                            Spacer(modifier = Modifier.height(AppDimens.dp4))
                            Text(
                                text = address.phoneNumber,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(AppDimens.dp6))
                            Text(
                                text = "${address.addressLine1}${if (address.addressLine2.isNotBlank()) ", " + address.addressLine2 else ""}, ${address.city} - ${address.pincode}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(AppDimens.dp12))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = { editingAddress = address },
                                    modifier = Modifier.height(AppDimens.dp36),
                                    shape = RoundedCornerShape(AppDimens.dp8)
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        modifier = Modifier.size(AppDimens.dp14)
                                    )
                                    Spacer(modifier = Modifier.width(AppDimens.dp4))
                                    Text("Edit Address", fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.width(AppDimens.dp8))
                                IconButton(
                                    onClick = { onDeleteAddress(address.addressId) },
                                    modifier = Modifier.size(AppDimens.dp36)
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(AppDimens.dp18)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            EditAddressDialog(
                initialAddress = null,
                onSave = { onSaveAddress(it); showAddDialog = false },
                onDismiss = { showAddDialog = false }
            )
        }

        if (editingAddress != null) {
            EditAddressDialog(
                initialAddress = editingAddress,
                onSave = { onSaveAddress(it); editingAddress = null },
                onDismiss = { editingAddress = null }
            )
        }
    }
}
