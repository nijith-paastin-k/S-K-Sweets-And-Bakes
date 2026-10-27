with open('app/src/main/java/com/skylink/sksweetsandbakes/feature/auth/AuthScreens.kt', 'r') as f:
    text = f.read()

# Add imports if missing
if 'import androidx.compose.ui.graphics.asImageBitmap' not in text:
    text = text.replace(
        'package com.skylink.sksweetsandbakes.feature.auth\n',
        'package com.skylink.sksweetsandbakes.feature.auth\n\nimport androidx.compose.ui.graphics.asImageBitmap\nimport androidx.compose.material3.ScrollableTabRow\n'
    )

# Replace PrimaryScrollableTabRow with ScrollableTabRow
text = text.replace('PrimaryScrollableTabRow(', 'ScrollableTabRow(')

# Fix duplicate @Composable
text = text.replace('@Composable\n@Composable\nfun LoginScreen', '@Composable\nfun LoginScreen')
text = text.replace('@Composable\n@Composable\nfun ProfileEditScreen', '@Composable\nfun ProfileEditScreen')

# Fix missing @Composable before FinalizeAuthScreen and AddressListScreen
text = text.replace('\n\nfun FinalizeAuthScreen(', '\n\n@Composable\nfun FinalizeAuthScreen(')
text = text.replace('\n\nfun AddressListScreen(', '\n\n@Composable\nfun AddressListScreen(')

# Fix UserRole references: KITCHEN_STAFF and STORE_MANAGER
text = text.replace('UserRole.KITCHEN_STAFF', 'UserRole.MANAGER')
text = text.replace('UserRole.STORE_MANAGER', 'UserRole.MANAGER')
text = text.replace(
    'val roles = listOf(UserRole.CUSTOMER, UserRole.CASHIER, UserRole.KITCHEN_STAFF, UserRole.STORE_MANAGER, UserRole.OWNER)',
    'val roles = listOf(UserRole.CUSTOMER, UserRole.CASHIER, UserRole.MANAGER, UserRole.OWNER)'
)
text = text.replace(
    'selected = state.userProfile?.role == r,',
    'selected = state.userProfile?.role?.equals(r.roleName, ignoreCase = true) == true || state.userProfile?.role?.equals(r.name, ignoreCase = true) == true,'
)

with open('app/src/main/java/com/skylink/sksweetsandbakes/feature/auth/AuthScreens.kt', 'w') as f:
    f.write(text)

print("patch_auth.py applied successfully")
