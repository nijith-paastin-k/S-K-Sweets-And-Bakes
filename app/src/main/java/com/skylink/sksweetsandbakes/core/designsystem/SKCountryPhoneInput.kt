package com.skylink.sksweetsandbakes.core.designsystem

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.skylink.sksweetsandbakes.ui.theme.AppColors
import com.skylink.sksweetsandbakes.ui.theme.AppDimens
import network.chaintech.cmpcountrycodepicker.model.CountryDetails
import network.chaintech.cmpcountrycodepicker.ui.CountryPickerBasicTextField

/**
 * CountryPickerBasicText component using cmp-country-code-picker
 * Provides dynamic country data with search, flags, and dial codes.
 */
@Composable
fun CountryPickerBasicText() {
    val selectedCountryState: MutableState<CountryDetails?> = remember {
        mutableStateOf(null)
    }
    var mobileNumber by remember {
        mutableStateOf("")
    }

    CountryPickerBasicTextField(
        mobileNumber = mobileNumber,
        defaultCountryCode = "ad",
        onMobileNumberChange = {
            mobileNumber = it
        },
        onCountrySelected = {
            selectedCountryState.value = it
        },
        modifier = Modifier.fillMaxWidth(),
        defaultPaddingValues = PaddingValues(AppDimens.dp6),
        showCountryFlag = true,
        showCountryPhoneCode = true,
        showCountryName = false,
        showCountryCode = false,
        showArrowDropDown = true,
        spaceAfterCountryFlag = AppDimens.dp8,
        spaceAfterCountryPhoneCode = AppDimens.dp6,
        spaceAfterCountryName = AppDimens.dp6,
        spaceAfterCountryCode = AppDimens.dp6,
        label = {
            Text(text = "Mobile Number")
        },
        focusedBorderThickness = AppDimens.dp2,
        unfocusedBorderThickness = AppDimens.dp1,
        shape = RoundedCornerShape(AppDimens.dp10),
        verticalDividerColor = AppColors.BorderSubtle,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppColors.BorderSubtle,
            unfocusedBorderColor = AppColors.BorderSubtle
        )
    )
}

/**
 * Premium SK Sweets brand styled country picker text field
 */
@Composable
fun SKCountryPickerPhoneField(
    mobileNumber: String,
    onMobileNumberChange: (String) -> Unit,
    onCountrySelected: (CountryDetails) -> Unit,
    modifier: Modifier = Modifier,
    defaultCountryCode: String = "in",
    label: String = "Mobile Number *",
    isError: Boolean = false
) {
    CountryPickerBasicTextField(
        mobileNumber = mobileNumber,
        defaultCountryCode = defaultCountryCode,
        onMobileNumberChange = onMobileNumberChange,
        onCountrySelected = onCountrySelected,
        modifier = modifier.fillMaxWidth(),
        defaultPaddingValues = PaddingValues(AppDimens.dp6),
        showCountryFlag = true,
        showCountryPhoneCode = true,
        showCountryName = false,
        showCountryCode = false,
        showArrowDropDown = true,
        spaceAfterCountryFlag = AppDimens.dp8,
        spaceAfterCountryPhoneCode = AppDimens.dp6,
        spaceAfterCountryName = AppDimens.dp6,
        spaceAfterCountryCode = AppDimens.dp6,
        label = {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        },
        isError = isError,
        focusedBorderThickness = AppDimens.dp2,
        unfocusedBorderThickness = AppDimens.dp1,
        shape = RoundedCornerShape(AppDimens.dp12),
        verticalDividerColor = MaterialTheme.colorScheme.outlineVariant,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            errorBorderColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}
