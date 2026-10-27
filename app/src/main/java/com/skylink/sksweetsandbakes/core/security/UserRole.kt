package com.skylink.sksweetsandbakes.core.security

/**
 * Supported application roles.
 */
enum class UserRole(val roleName: String) {
    CUSTOMER("customer"),
    OWNER("owner"),
    MANAGER("manager"),
    CASHIER("cashier"),
    INVENTORY_CLERK("inventory_clerk");

    companion object {
        fun fromString(role: String?): UserRole {
            return when (role?.lowercase()) {
                "owner" -> OWNER
                "manager" -> MANAGER
                "cashier" -> CASHIER
                "inventory_clerk" -> INVENTORY_CLERK
                else -> CUSTOMER
            }
        }
    }
}

/**
 * Granular permissions mapped by role.
 */
object RolePermissions {

    fun canViewFinancialReports(role: UserRole): Boolean {
        return role == UserRole.OWNER
    }

    fun canViewExpenses(role: UserRole): Boolean {
        return role == UserRole.OWNER
    }

    fun canManageStaff(role: UserRole): Boolean {
        return role == UserRole.OWNER
    }

    fun canEditProductsAndPricing(role: UserRole): Boolean {
        return role == UserRole.OWNER || role == UserRole.MANAGER
    }

    fun canProcessPos(role: UserRole): Boolean {
        return role == UserRole.OWNER || role == UserRole.MANAGER || role == UserRole.CASHIER
    }

    fun canAdjustInventory(role: UserRole): Boolean {
        return role == UserRole.OWNER || role == UserRole.MANAGER || role == UserRole.INVENTORY_CLERK
    }

    fun canReceiveStock(role: UserRole): Boolean {
        return role == UserRole.OWNER || role == UserRole.MANAGER || role == UserRole.INVENTORY_CLERK
    }

    fun canUpdateOrderStatus(role: UserRole): Boolean {
        return role == UserRole.OWNER || role == UserRole.MANAGER || role == UserRole.CASHIER
    }
}
