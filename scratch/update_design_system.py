with open('app/src/main/java/com/skylink/sksweetsandbakes/core/designsystem/SKValidatedTextField.kt', 'r') as f:
    code = f.read()

# Add extra imports if not present
new_imports = """
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
"""

if 'import androidx.compose.foundation.text.BasicTextField' not in code:
    code = code.replace(
        'import androidx.compose.ui.unit.sp\n',
        'import androidx.compose.ui.unit.sp\n' + new_imports + '\n'
    )

new_components = """

/**
 * Ultra-premium mobile number input field:
 * - Single-line clean label
 * - Country flag & code badge (🇮🇳 +91) with divider
 * - Large, crisp, spaced font for 10 digits
 * - Real-time live character counter (e.g. 7/10)
 * - Green check on valid / red warning on invalid
 * - Instant keystroke validation
 */
@Composable
fun SKMobileNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Mobile Number *",
    placeholder: String = "98765 43210",
    countryCode: String = "+91",
    countryFlag: String = "🇮🇳",
    validationStatus: ValidationStatus = ValidationStatus.Idle,
    onCountryClick: (() -> Unit)? = null
) {
    val successColor = AppColors.StatusSuccessAlt
    val isError = validationStatus is ValidationStatus.Invalid
    val isValid = validationStatus is ValidationStatus.Valid

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = { input ->
                val digitsOnly = input.filter { it.isDigit() }
                if (digitsOnly.length <= 10) {
                    onValueChange(digitsOnly)
                }
            },
            label = { Text(label, maxLines = 1, softWrap = false) },
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    letterSpacing = 1.sp
                )
            },
            leadingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 12.dp, end = 8.dp)
                        .then(if (onCountryClick != null) Modifier.clickable(onClick = onCountryClick) else Modifier)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Text(text = countryFlag, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = countryCode,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )
                }
            },
            trailingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Text(
                        text = "${value.length}/10",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (value.length == 10) successColor else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (value.length == 10) FontWeight.Bold else FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    when {
                        isValid -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Valid Mobile",
                                tint = successColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        isError -> {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Invalid Mobile",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            },
            isError = isError,
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(14.dp),
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

        // Live validation feedback text
        AnimatedVisibility(
            visible = isError || (isValid && value.isNotBlank()),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (validationStatus) {
                    is ValidationStatus.Invalid -> {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = validationStatus.error,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    is ValidationStatus.Valid -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = successColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = validationStatus.message,
                            color = successColor,
                            fontSize = 12.sp,
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
 * Modern 6-Digit OTP Box Display with 6 distinct interactive cards.
 * Features:
 * - 6 rounded digit boxes
 * - Active focus highlight
 * - One-tap auto-fill test code button (123456)
 * - Real-time green border on completion
 * - Resend countdown / action
 */
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
    val successColor = AppColors.StatusSuccessAlt
    val isComplete = otpValue.length == 6

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 6 Elevated Digit Boxes in a centered row
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Invisible text field listening to keyboard
            BasicTextField(
                value = otpValue,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }
                    if (digits.length <= 6) {
                        onOtpChange(digits)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .matchParentSize()
                    .padding(horizontal = 4.dp),
                decorationBox = { /* Hidden field decoration, visual boxes are rendered below */ }
            )

            // Visible 6 Digit Boxes
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
                        isError || isComplete || isCurrent -> 2.dp
                        char.isNotEmpty() -> 1.5.dp
                        else -> 1.dp
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = when {
                            isCurrent -> MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                            char.isNotEmpty() -> MaterialTheme.colorScheme.surfaceVariant
                            else -> MaterialTheme.colorScheme.surface
                        },
                        border = BorderStroke(borderWidth, borderColor),
                        shadowElevation = if (char.isNotEmpty() || isCurrent) 2.dp else 0.dp,
                        modifier = Modifier
                            .size(width = 48.dp, height = 56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (char.isNotEmpty()) {
                                Text(
                                    text = char,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            } else if (isCurrent) {
                                // Subtle blinking indicator dot
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                                )
                            } else {
                                Text(
                                    text = "•",
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live status feedback row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when {
                isError -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Error, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(errorMessage ?: "Invalid 6-digit OTP", color = MaterialTheme.colorScheme.error, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
                isComplete -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = successColor, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("6-digit OTP passcode entered", color = successColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                else -> {
                    Text(
                        text = "Enter the 6-digit code sent to your device",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
            }

            if (onResendClick != null) {
                TextButton(
                    onClick = onResendClick,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Resend OTP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // One-Tap Quick Test Auto-Fill Pill
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable { onOtpChange(testCode) }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⚡", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Demo Code: $testCode",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = "Tap to Auto-Fill",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
"""

code += new_components

with open('app/src/main/java/com/skylink/sksweetsandbakes/core/designsystem/SKValidatedTextField.kt', 'w') as f:
    f.write(code)

print("Updated SKValidatedTextField.kt with SKMobileNumberField and SKSixDigitOtpInput")
