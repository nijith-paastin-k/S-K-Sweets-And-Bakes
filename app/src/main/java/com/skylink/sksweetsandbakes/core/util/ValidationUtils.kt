package com.skylink.sksweetsandbakes.core.util

import com.skylink.sksweetsandbakes.core.designsystem.ValidationStatus

object ValidationUtils {
    /**
     * Strictly validates Indian mobile numbers according to the National Numbering Plan (DoT & TRAI):
     * - Must contain exactly 10 digits
     * - Must start with 6, 7, 8, or 9
     * - Rejects fake repetitive patterns (e.g. 1111111111, 9999999999)
     * - Rejects sequential dummy numbers (e.g. 1234567890, 9876543210, 0123456789)
     */
    fun validateIndianMobile(raw: String): ValidationStatus {
        if (raw.isEmpty()) return ValidationStatus.Idle
        val digits = raw.filter { it.isDigit() }

        if (digits.isNotEmpty()) {
            val firstChar = digits.first()
            if (firstChar !in '6'..'9') {
                return ValidationStatus.Invalid("Indian mobile numbers must start with 6, 7, 8, or 9 (starts with $firstChar)")
            }
        }

        if (digits.length < 10) {
            return ValidationStatus.Invalid("Enter 10-digit mobile number (${digits.length}/10)")
        }

        if (digits.length > 10) {
            return ValidationStatus.Invalid("Mobile number cannot exceed 10 digits")
        }

        if (digits.toSet().size <= 2) {
            return ValidationStatus.Invalid("Invalid number: repetitive digits not allowed")
        }

        val dummySequences = setOf(
            "1234567890", "9876543210", "0123456789", "9876501234",
            "9898989898", "9090909090", "9123456789", "8901234567",
            "7890123456", "6789012345", "9988776655", "8877665544"
        )
        if (digits in dummySequences) {
            return ValidationStatus.Invalid("Invalid mobile number: test sequence not allowed")
        }

        return ValidationStatus.Valid("Valid Indian mobile number ✓")
    }

    fun isMobileValid(raw: String): Boolean {
        return validateIndianMobile(raw) is ValidationStatus.Valid
    }
}
