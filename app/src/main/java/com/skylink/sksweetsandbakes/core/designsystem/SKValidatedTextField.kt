package com.skylink.sksweetsandbakes.core.designsystem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import com.skylink.sksweetsandbakes.ui.theme.AppDimens
import com.skylink.sksweetsandbakes.ui.theme.AppFontSizes
import kotlinx.coroutines.delay
import network.chaintech.cmpcountrycodepicker.model.CountryDetails
import network.chaintech.cmpcountrycodepicker.ui.CountryPickerBasicTextField

sealed class ValidationStatus {
    object Idle : ValidationStatus()
    data class Valid(val message: String = "Looks good!") : ValidationStatus()
    data class Invalid(val error: String) : ValidationStatus()
}

/**
 * Enterprise live keystroke validated text field.
 * Validates immediately upon typing with visual green success / red error styling.
 */
@Composable
fun SKValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    validationStatus: ValidationStatus = ValidationStatus.Idle,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val successColor = MaterialTheme.colorScheme.primary
    val isError = validationStatus is ValidationStatus.Invalid
    val isValid = validationStatus is ValidationStatus.Valid

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    label,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
            },
            placeholder = placeholder?.let { { Text(it) } },
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = when {
                            isError -> MaterialTheme.colorScheme.error
                            isValid -> successColor
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            },
            trailingIcon = {
                if (trailingIcon != null) {
                    trailingIcon()
                } else {
                    when {
                        isValid -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Valid Input",
                                tint = successColor,
                                modifier = Modifier.size(AppDimens.dp20)
                            )
                        }

                        isError -> {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Invalid Input",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(AppDimens.dp20)
                            )
                        }
                    }
                }
            },
            isError = isError,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(AppDimens.dp12),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = when {
                    isValid -> successColor
                    isError -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.primary
                },
                unfocusedBorderColor = when {
                    isValid -> successColor.copy(alpha = 0.7f)
                    isError -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.outline
                },
                focusedLabelColor = when {
                    isValid -> successColor
                    isError -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.primary
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedVisibility(
            visible = isError || (isValid && value.isNotBlank()),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = AppDimens.dp8, top = AppDimens.dp4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (validationStatus) {
                    is ValidationStatus.Invalid -> {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(AppDimens.dp13)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp4))
                        Text(
                            text = validationStatus.error,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = AppFontSizes.sp11,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    is ValidationStatus.Valid -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = successColor,
                            modifier = Modifier.size(AppDimens.dp13)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp4))
                        Text(
                            text = validationStatus.message,
                            color = successColor,
                            fontSize = AppFontSizes.sp11,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    ValidationStatus.Idle -> {}
                }
            }
        }
    }
}


/**
 * Ultra-premium mobile number input field:
 * - Single-line clean label
 * - Country flag & code badge (🇮🇳 +91) with divider
 * - Large, crisp, spaced font for 10 digits
 * - Real-time live character counter (e.g. 7/10)
 * - Green check on valid / red warning on invalid
 * - Instant keystroke validation with strict Indian TRAI/DoT rules
 */
@Composable
fun SKMobileNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Mobile Number *",
    placeholder: String = "98765 43210",
    defaultCountryCode: String = "in",
    countryCode: String = "+91",
    countryFlag: String = "🇮🇳",
    validationStatus: ValidationStatus = ValidationStatus.Idle,
    onCountryClick: (() -> Unit)? = null,
    onCountrySelected: ((CountryDetails) -> Unit)? = null
) {
    val effectiveStatus = if (validationStatus is ValidationStatus.Idle && value.isNotEmpty()) {
        com.skylink.sksweetsandbakes.core.util.ValidationUtils.validateIndianMobile(value)
    } else {
        validationStatus
    }
    val successColor = MaterialTheme.colorScheme.primary
    val isError = effectiveStatus is ValidationStatus.Invalid
    val isValid = effectiveStatus is ValidationStatus.Valid

    Column(modifier = modifier.fillMaxWidth()) {
        CountryPickerBasicTextField(
            mobileNumber = value,
            defaultCountryCode = defaultCountryCode.lowercase(),
            onMobileNumberChange = { input ->
                val digitsOnly = input.filter { it.isDigit() }
                if (digitsOnly.length <= 15) {
                    onValueChange(digitsOnly)
                }
            },
            onCountrySelected = { details ->
                onCountrySelected?.invoke(details)
            },
            modifier = Modifier.fillMaxWidth(),
            defaultPaddingValues = PaddingValues(
                horizontal = AppDimens.dp4,
                vertical = AppDimens.dp6
            ),
            showCountryFlag = true,
            showCountryPhoneCode = true,
            showCountryName = false,
            showCountryCode = false,
            showArrowDropDown = true,
            spaceAfterCountryFlag = AppDimens.dp8,
            spaceAfterCountryPhoneCode = AppDimens.dp6,
            spaceAfterCountryName = AppDimens.dp6,
            spaceAfterCountryCode = AppDimens.dp6,
            countryFlagSize = AppDimens.dp24,
            showVerticalDivider = true,
            spaceAfterVerticalDivider = AppDimens.dp6,
            verticalDividerColor = MaterialTheme.colorScheme.outlineVariant,
            label = { Text(label, maxLines = 1, softWrap = false) },
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    letterSpacing = AppFontSizes.sp1
                )
            },
            trailingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = AppDimens.dp12)
                ) {
                    Text(
                        text = "${value.length}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isValid) successColor else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (isValid) FontWeight.Bold else FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.width(AppDimens.dp6))
                    when {
                        isValid -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Valid Mobile",
                                tint = successColor,
                                modifier = Modifier.size(AppDimens.dp20)
                            )
                        }

                        isError -> {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Invalid Mobile",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(AppDimens.dp20)
                            )
                        }
                    }
                }
            },
            isError = isError,
            singleLine = true,
            shape = RoundedCornerShape(AppDimens.dp12),
            focusedBorderThickness = AppDimens.dp2,
            unfocusedBorderThickness = AppDimens.dp1,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = when {
                    isValid -> successColor
                    isError -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.primary
                },
                unfocusedBorderColor = when {
                    isValid -> successColor.copy(alpha = 0.7f)
                    isError -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.outlineVariant
                },
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        AnimatedVisibility(
            visible = isError || (isValid && value.isNotBlank()),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = AppDimens.dp12, top = AppDimens.dp4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (effectiveStatus) {
                    is ValidationStatus.Invalid -> {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(AppDimens.dp13)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp4))
                        Text(
                            text = effectiveStatus.error,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = AppFontSizes.sp12,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    is ValidationStatus.Valid -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = successColor,
                            modifier = Modifier.size(AppDimens.dp13)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp4))
                        Text(
                            text = effectiveStatus.message,
                            color = successColor,
                            fontSize = AppFontSizes.sp12,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    ValidationStatus.Idle -> {}
                }
            }
        }
    }
}

@Composable
fun SKSixDigitOtpInput(
    otpValue: String,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    testCode: String = "123456",
    onResendClick: (() -> Unit)? = null
) {
    val successColor = MaterialTheme.colorScheme.primary
    val isComplete = otpValue.length == 6
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(250)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppDimens.dp10)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                },
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = otpValue,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }
                    if (digits.length <= 6) {
                        onOtpChange(digits)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .matchParentSize()
                    .padding(horizontal = AppDimens.dp4),
                decorationBox = { /* Hidden field decoration, visual boxes are rendered below */ }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until 6) {
                    val char = otpValue.getOrNull(i)?.toString() ?: ""
                    val isCurrent = otpValue.length == i

                    val borderColor = when {
                        isError -> MaterialTheme.colorScheme.error
                        isComplete -> successColor
                        isCurrent -> MaterialTheme.colorScheme.primary
                        char.isNotEmpty() -> MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    }

                    val borderWidth = when {
                        isError || isComplete || isCurrent -> AppDimens.dp2
                        char.isNotEmpty() -> AppDimens.dp1_5
                        else -> AppDimens.dp1
                    }

                    Surface(
                        shape = RoundedCornerShape(AppDimens.dp12),
                        color = when {
                            isCurrent -> MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                            char.isNotEmpty() -> MaterialTheme.colorScheme.surfaceVariant
                            else -> MaterialTheme.colorScheme.surface
                        },
                        border = BorderStroke(borderWidth, borderColor),
                        shadowElevation = if (char.isNotEmpty() || isCurrent) AppDimens.dp2 else AppDimens.dp0,
                        modifier = Modifier
                            .size(width = AppDimens.dp48, height = AppDimens.dp56)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                focusRequester.requestFocus()
                                keyboardController?.show()
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (char.isNotEmpty()) {
                                Text(
                                    text = char,
                                    fontSize = AppFontSizes.sp24,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            } else if (isCurrent) {
                                Box(
                                    modifier = Modifier
                                        .size(AppDimens.dp8)
                                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                                )
                            } else {
                                Text(
                                    text = "•",
                                    fontSize = AppFontSizes.sp18,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
                                )
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when {
                isError -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(AppDimens.dp14)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp4))
                        Text(
                            errorMessage ?: "Invalid 6-digit OTP",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = AppFontSizes.sp12,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                isComplete -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = successColor,
                            modifier = Modifier.size(AppDimens.dp14)
                        )
                        Spacer(modifier = Modifier.width(AppDimens.dp4))
                        Text(
                            "6-digit OTP passcode entered",
                            color = successColor,
                            fontSize = AppFontSizes.sp12,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                else -> {
                    Text(
                        text = "Tap boxes above to enter 6-digit code",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = AppFontSizes.sp11
                    )
                }
            }

            if (onResendClick != null) {
                TextButton(
                    onClick = onResendClick,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(AppDimens.dp0)
                ) {
                    Text(
                        text = "Resend OTP",
                        fontSize = AppFontSizes.sp12,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(AppDimens.dp10),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
            border = BorderStroke(
                AppDimens.dp1,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppDimens.dp10))
                .clickable { onOtpChange(testCode) }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = AppDimens.dp12, vertical = AppDimens.dp7),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⚡", fontSize = AppFontSizes.sp14)
                    Spacer(modifier = Modifier.width(AppDimens.dp6))
                    Text(
                        text = "Demo Code: $testCode",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Surface(
                    shape = RoundedCornerShape(AppDimens.dp6),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = "Tap to Auto-Fill",
                        modifier = Modifier.padding(
                            horizontal = AppDimens.dp8,
                            vertical = AppDimens.dp3
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = AppFontSizes.sp10
                    )
                }
            }
        }
    }
}
