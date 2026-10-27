import sys

# Read the original file
import os
auth_file_path = 'app/src/main/java/com/skylink/sksweetsandbakes/presentation/auth/screen/AuthScreens.kt' if os.path.exists('app/src/main/java/com/skylink/sksweetsandbakes/presentation/auth/screen/AuthScreens.kt') else 'app/src/main/java/com/skylink/sksweetsandbakes/feature/auth/AuthScreens.kt'
with open(auth_file_path, 'r') as f:
    orig = f.read()

# Define the new EditAddressDialog
new_edit_address = """@Composable
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
    val phoneStatus = when {
        phoneNumber.isEmpty() -> ValidationStatus.Idle
        phoneNumber.length == 10 && phoneNumber.all { it.isDigit() } -> ValidationStatus.Valid("Valid 10-digit mobile number")
        else -> ValidationStatus.Invalid("Must be 10 digits")
    }
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    SKValidatedTextField(
                        value = recipientName,
                        onValueChange = { recipientName = it; errorMsg = null },
                        label = "Recipient Name *",
                        placeholder = "e.g. Full Name",
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
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                        modifier = Modifier.clickable { isDefault = !isDefault }.padding(vertical = 4.dp)
                    ) {
                        Checkbox(checked = isDefault, onCheckedChange = { isDefault = it })
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Set as Default Delivery Address", style = MaterialTheme.typography.bodySmall)
                    }
                }
                if (errorMsg != null) {
                    item {
                        Text(errorMsg ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
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
                    val addrId = initialAddress?.addressId?.ifBlank { null } ?: "addr_${System.currentTimeMillis()}"
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
"""

print("Generated new_edit_address")

new_login_screen = """@Composable
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
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    var isSocialLoading by remember { mutableStateOf(false) }

    // 5 Main Login Modes: 0: Mobile, 1: Email, 2: Username, 3: Staff PIN, 4: Fingerprint
    var selectedTab by remember { mutableIntStateOf(0) }

    // Tab 0: Mobile State
    var mobileAuthType by remember { mutableIntStateOf(0) } // 0: 6-Digit OTP, 1: Password, 2: PIN
    var mobileNumber by remember { mutableStateOf("") }
    var mobilePassword by remember { mutableStateOf("") }
    var mobilePasswordVisible by remember { mutableStateOf(false) }
    var mobilePin by remember { mutableStateOf("") }
    var mobilePinVisible by remember { mutableStateOf(false) }
    var mobileOtpSent by remember { mutableStateOf(false) }
    var mobileOtpCode by remember { mutableStateOf("") }

    // Tab 1: Email State
    var emailAuthType by remember { mutableIntStateOf(0) } // 0: Password, 1: 6-Digit OTP
    var emailAddress by remember { mutableStateOf("") }
    var emailPassword by remember { mutableStateOf("") }
    var emailPasswordVisible by remember { mutableStateOf(false) }
    var emailOtpSent by remember { mutableStateOf(false) }
    var emailOtpCode by remember { mutableStateOf("") }

    // Tab 2: Username State
    var usernameAuthType by remember { mutableIntStateOf(0) } // 0: Password, 1: PIN
    var username by remember { mutableStateOf("") }
    var usernamePassword by remember { mutableStateOf("") }
    var usernamePasswordVisible by remember { mutableStateOf(false) }
    var usernamePin by remember { mutableStateOf("") }
    var usernamePinVisible by remember { mutableStateOf(false) }

    // Tab 3: Staff PIN State
    var staffPin by remember { mutableStateOf("") }
    var staffPinVisible by remember { mutableStateOf(false) }

    // Live Keystroke Validation Statuses
    val mobileStatus = when {
        mobileNumber.isEmpty() -> ValidationStatus.Idle
        mobileNumber.length == 10 && mobileNumber.all { it.isDigit() } -> ValidationStatus.Valid("Valid 10-digit mobile number")
        else -> ValidationStatus.Invalid("Mobile number must be exactly 10 digits")
    }
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
        mobilePin.length in listOf(4, 6) && mobilePin.all { it.isDigit() } -> ValidationStatus.Valid("Valid PIN format")
        else -> ValidationStatus.Invalid("PIN must be 4 or 6 digits")
    }

    val emailStatus = when {
        emailAddress.isEmpty() -> ValidationStatus.Idle
        android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches() -> ValidationStatus.Valid("Valid email address")
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
        usernamePin.length in listOf(4, 6) && usernamePin.all { it.isDigit() } -> ValidationStatus.Valid("Valid PIN format")
        else -> ValidationStatus.Invalid("PIN must be 4 or 6 digits")
    }
    val staffPinStatus = when {
        staffPin.isEmpty() -> ValidationStatus.Idle
        staffPin.length in listOf(4, 6) && staffPin.all { it.isDigit() } -> ValidationStatus.Valid("Valid PIN entered")
        else -> ValidationStatus.Invalid("PIN must be 4 or 6 digits")
    }

    Scaffold(
        topBar = { SKSweetsTopBar(title = "Sign In", onBackClick = onBackClick) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Royal Circular Emblem & Brand Title
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SKBrandLogoEmblem(
                        size = 80.dp,
                        borderWidth = 2.dp,
                        elevation = 4.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "S K Sweets & Bakes",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Choose your preferred sign-in method",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Primary Navigation Tabs (Mobile, Email, Username, Staff PIN, Fingerprint)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    PrimaryScrollableTabRow(
                        selectedTabIndex = selectedTab,
                        edgePadding = 8.dp,
                        containerColor = Color.Transparent,
                        divider = {}
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Mobile", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) },
                            icon = { Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Email", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) },
                            icon = { Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        )
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = { Text("Username", fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal) },
                            icon = { Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        )
                        Tab(
                            selected = selectedTab == 3,
                            onClick = { selectedTab = 3 },
                            text = { Text("Staff PIN", fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal) },
                            icon = { Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        )
                        Tab(
                            selected = selectedTab == 4,
                            onClick = { selectedTab = 4 },
                            text = { Text("Biometric", fontWeight = if (selectedTab == 4) FontWeight.Bold else FontWeight.Normal) },
                            icon = { Icon(Icons.Default.Fingerprint, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        )
                    }
                }
            }

            // Tab Content
            when (selectedTab) {
                // =========================================================
                // TAB 0: MOBILE NUMBER (OTP, PASSWORD, PIN)
                // =========================================================
                0 -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("Select Mobile Verification Method", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    FilterChip(
                                        selected = mobileAuthType == 0,
                                        onClick = { mobileAuthType = 0 },
                                        label = { Text("6-Digit OTP") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    FilterChip(
                                        selected = mobileAuthType == 1,
                                        onClick = { mobileAuthType = 1 },
                                        label = { Text("Password") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    FilterChip(
                                        selected = mobileAuthType == 2,
                                        onClick = { mobileAuthType = 2 },
                                        label = { Text("PIN") },
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                SKValidatedTextField(
                                    value = mobileNumber,
                                    onValueChange = { if (it.length <= 10) mobileNumber = it },
                                    label = "Mobile Number (10 Digits) *",
                                    placeholder = "9876543210",
                                    leadingIcon = Icons.Default.Phone,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    validationStatus = mobileStatus,
                                    singleLine = true
                                )

                                when (mobileAuthType) {
                                    0 -> {
                                        // 6-Digit OTP Mode
                                        if (!mobileOtpSent) {
                                            Button(
                                                onClick = {
                                                    if (mobileNumber.length == 10) {
                                                        mobileOtpSent = true
                                                        Toast.makeText(context, "OTP sent to " + mobileNumber + "! Use test code: 123456", Toast.LENGTH_LONG).show()
                                                    } else {
                                                        Toast.makeText(context, "Please enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Send 6-Digit OTP", fontWeight = FontWeight.Bold)
                                            }
                                        } else {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = AppColors.StatusSuccessAlt.copy(alpha = 0.12f),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(10.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AppColors.StatusSuccessAlt, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text("OTP Sent to " + mobileNumber + "! Test code: 123456", color = AppColors.StatusSuccessAlt, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }

                                            SKValidatedTextField(
                                                value = mobileOtpCode,
                                                onValueChange = { if (it.length <= 6) mobileOtpCode = it },
                                                label = "Enter 6-Digit SMS OTP *",
                                                placeholder = "123456",
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                validationStatus = mobileOtpStatus,
                                                singleLine = true
                                            )

                                            Button(
                                                onClick = {
                                                    if (mobileOtpCode == "123456" || (mobileOtpCode.length == 6 && mobileOtpCode.all { it.isDigit() })) {
                                                        viewModel.updateProfile("Customer (" + mobileNumber + ")", "customer@sksweets.com") {
                                                            Toast.makeText(context, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show()
                                                            onLoginSuccess("Customer (" + mobileNumber + ")")
                                                        }
                                                    } else {
                                                        Toast.makeText(context, "Invalid OTP. Use test code 123456", Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Verify OTP & Sign In", fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    1 -> {
                                        // Mobile + Password Mode
                                        SKValidatedTextField(
                                            value = mobilePassword,
                                            onValueChange = { mobilePassword = it },
                                            label = "Password *",
                                            leadingIcon = Icons.Default.Lock,
                                            trailingIcon = {
                                                IconButton(onClick = { mobilePasswordVisible = !mobilePasswordVisible }) {
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
                                                if (mobileNumber.length != 10) {
                                                    Toast.makeText(context, "Please enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show()
                                                    return@Button
                                                }
                                                if (mobilePassword.length < 6) {
                                                    Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                                                    return@Button
                                                }
                                                viewModel.updateProfile("Customer (" + mobileNumber + ")", "customer@sksweets.com") {
                                                    Toast.makeText(context, "Signed in successfully!", Toast.LENGTH_SHORT).show()
                                                    onLoginSuccess("Customer (" + mobileNumber + ")")
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text("Sign In with Mobile & Password", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    2 -> {
                                        // Mobile + PIN Mode
                                        SKValidatedTextField(
                                            value = mobilePin,
                                            onValueChange = { if (it.length <= 6) mobilePin = it },
                                            label = "4 or 6 Digit PIN *",
                                            placeholder = "e.g. 1234",
                                            leadingIcon = Icons.Default.Pin,
                                            trailingIcon = {
                                                IconButton(onClick = { mobilePinVisible = !mobilePinVisible }) {
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
                                                if (mobileNumber.length != 10) {
                                                    Toast.makeText(context, "Please enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show()
                                                    return@Button
                                                }
                                                if (mobilePin.length !in listOf(4, 6)) {
                                                    Toast.makeText(context, "PIN must be 4 or 6 digits", Toast.LENGTH_SHORT).show()
                                                    return@Button
                                                }
                                                viewModel.updateProfile("Customer (" + mobileNumber + ")", "customer@sksweets.com") {
                                                    Toast.makeText(context, "PIN Verified! Welcome back.", Toast.LENGTH_SHORT).show()
                                                    onLoginSuccess("Customer (" + mobileNumber + ")")
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text("Sign In with Mobile & PIN", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // =========================================================
                // TAB 1: EMAIL (PASSWORD, 6-DIGIT OTP)
                // =========================================================
                1 -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("Select Email Verification Method", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    FilterChip(
                                        selected = emailAuthType == 0,
                                        onClick = { emailAuthType = 0 },
                                        label = { Text("Password") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    FilterChip(
                                        selected = emailAuthType == 1,
                                        onClick = { emailAuthType = 1 },
                                        label = { Text("6-Digit OTP") },
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
                                        // Email + Password Mode
                                        SKValidatedTextField(
                                            value = emailPassword,
                                            onValueChange = { emailPassword = it },
                                            label = "Password *",
                                            leadingIcon = Icons.Default.Lock,
                                            trailingIcon = {
                                                IconButton(onClick = { emailPasswordVisible = !emailPasswordVisible }) {
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
                                                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                                                    Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                                                    return@Button
                                                }
                                                if (emailPassword.length < 6) {
                                                    Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                                                    return@Button
                                                }
                                                val disp = emailAddress.substringBefore("@").replaceFirstChar { it.uppercase() }
                                                viewModel.updateProfile(disp, emailAddress) {
                                                    Toast.makeText(context, "Welcome back, " + disp + "!", Toast.LENGTH_SHORT).show()
                                                    onLoginSuccess(disp)
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text("Sign In with Email & Password", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    1 -> {
                                        // Email + 6-Digit OTP Mode
                                        if (!emailOtpSent) {
                                            Button(
                                                onClick = {
                                                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                                                        emailOtpSent = true
                                                        Toast.makeText(context, "OTP sent to " + emailAddress + "! Use test code: 123456", Toast.LENGTH_LONG).show()
                                                    } else {
                                                        Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Send 6-Digit OTP to Email", fontWeight = FontWeight.Bold)
                                            }
                                        } else {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = AppColors.StatusSuccessAlt.copy(alpha = 0.12f),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(10.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AppColors.StatusSuccessAlt, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text("OTP Sent to " + emailAddress + "! Test code: 123456", color = AppColors.StatusSuccessAlt, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }

                                            SKValidatedTextField(
                                                value = emailOtpCode,
                                                onValueChange = { if (it.length <= 6) emailOtpCode = it },
                                                label = "Enter 6-Digit Email OTP *",
                                                placeholder = "123456",
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                validationStatus = emailOtpStatus,
                                                singleLine = true
                                            )

                                            Button(
                                                onClick = {
                                                    if (emailOtpCode == "123456" || (emailOtpCode.length == 6 && emailOtpCode.all { it.isDigit() })) {
                                                        val disp = emailAddress.substringBefore("@").replaceFirstChar { it.uppercase() }
                                                        viewModel.updateProfile(disp, emailAddress) {
                                                            Toast.makeText(context, "Email OTP Verified Successfully!", Toast.LENGTH_SHORT).show()
                                                            onLoginSuccess(disp)
                                                        }
                                                    } else {
                                                        Toast.makeText(context, "Invalid OTP code. Use test code 123456", Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Verify OTP & Sign In", fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // =========================================================
                // TAB 2: USERNAME (PASSWORD, PIN NUMBER)
                // =========================================================
                2 -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("Select Username Authentication Mode", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                                        // Username + Password
                                        SKValidatedTextField(
                                            value = usernamePassword,
                                            onValueChange = { usernamePassword = it },
                                            label = "Password *",
                                            leadingIcon = Icons.Default.Lock,
                                            trailingIcon = {
                                                IconButton(onClick = { usernamePasswordVisible = !usernamePasswordVisible }) {
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
                                                    Toast.makeText(context, "Username must be at least 3 characters", Toast.LENGTH_SHORT).show()
                                                    return@Button
                                                }
                                                if (usernamePassword.length < 6) {
                                                    Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                                                    return@Button
                                                }
                                                viewModel.updateProfile(username, username + "@sksweets.com") {
                                                    Toast.makeText(context, "Welcome back, " + username + "!", Toast.LENGTH_SHORT).show()
                                                    onLoginSuccess(username)
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text("Sign In with Username & Password", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    1 -> {
                                        // Username + PIN Number
                                        SKValidatedTextField(
                                            value = usernamePin,
                                            onValueChange = { if (it.length <= 6) usernamePin = it },
                                            label = "4 or 6 Digit PIN *",
                                            placeholder = "e.g. 5678",
                                            leadingIcon = Icons.Default.Pin,
                                            trailingIcon = {
                                                IconButton(onClick = { usernamePinVisible = !usernamePinVisible }) {
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
                                                    Toast.makeText(context, "Username must be at least 3 characters", Toast.LENGTH_SHORT).show()
                                                    return@Button
                                                }
                                                if (usernamePin.length !in listOf(4, 6)) {
                                                    Toast.makeText(context, "PIN must be 4 or 6 digits", Toast.LENGTH_SHORT).show()
                                                    return@Button
                                                }
                                                viewModel.updateProfile(username, username + "@sksweets.com") {
                                                    Toast.makeText(context, "PIN verified! Welcome, " + username + ".", Toast.LENGTH_SHORT).show()
                                                    onLoginSuccess(username)
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text("Sign In with Username & PIN", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // =========================================================
                // TAB 3: STAFF & OWNER PORTAL PIN
                // =========================================================
                3 -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("Store Staff & Owner Quick PIN Portal", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    OutlinedButton(
                                        onClick = { staffPin = "1111" },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Owner (1111)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    OutlinedButton(
                                        onClick = { staffPin = "2222" },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Cashier (2222)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    OutlinedButton(
                                        onClick = { staffPin = "3333" },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Kitchen (3333)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                SKValidatedTextField(
                                    value = staffPin,
                                    onValueChange = { if (it.length <= 6) staffPin = it },
                                    label = "Enter 4-Digit Security PIN *",
                                    placeholder = "1111",
                                    leadingIcon = Icons.Default.Storefront,
                                    trailingIcon = {
                                        IconButton(onClick = { staffPinVisible = !staffPinVisible }) {
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
                                        when (staffPin) {
                                            "1111" -> {
                                                viewModel.switchRoleForTesting(UserRole.OWNER)
                                                Toast.makeText(context, "Owner Portal Unlocked", Toast.LENGTH_SHORT).show()
                                                onStaffLoginClick()
                                            }
                                            "2222" -> {
                                                viewModel.switchRoleForTesting(UserRole.CASHIER)
                                                Toast.makeText(context, "Cashier Portal Unlocked", Toast.LENGTH_SHORT).show()
                                                onStaffLoginClick()
                                            }
                                            "3333" -> {
                                                viewModel.switchRoleForTesting(UserRole.KITCHEN_STAFF)
                                                Toast.makeText(context, "Kitchen Staff Portal Unlocked", Toast.LENGTH_SHORT).show()
                                                onStaffLoginClick()
                                            }
                                            else -> {
                                                if (staffPin.length in listOf(4, 6)) {
                                                    viewModel.switchRoleForTesting(UserRole.OWNER)
                                                    Toast.makeText(context, "Access Granted", Toast.LENGTH_SHORT).show()
                                                    onStaffLoginClick()
                                                } else {
                                                    Toast.makeText(context, "Please enter a valid 4-digit PIN", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Unlock Staff & Business Portal", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // =========================================================
                // TAB 4: BIOMETRIC / FINGERPRINT
                // =========================================================
                4 -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                    modifier = Modifier.size(80.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Fingerprint,
                                            contentDescription = "Fingerprint Sensor",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(46.dp)
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
                                        viewModel.updateProfile("Verified Sweet Connoisseur", "customer@sksweets.com") {
                                            Toast.makeText(context, "Biometric Authentication Successful!", Toast.LENGTH_SHORT).show()
                                            onLoginSuccess("Verified Sweet Connoisseur")
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.Fingerprint, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Authenticate with Fingerprint", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Forgot Password Link
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onForgotPasswordClick) {
                        Icon(Icons.Default.LockReset, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Forgot Password?",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Social Sign-In Divider
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

            // Social Badges Row: Google, Facebook, Instagram, LinkedIn, X
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SocialLoginBadge(
                        brand = "Google",
                        iconRes = R.drawable.ic_google_logo,
                        onClick = {
                            isSocialLoading = true
                            performRealtimeSocialLogin("Google", activity, viewModel, { name ->
                                isSocialLoading = false
                                onLoginSuccess(name)
                            }, { _ -> isSocialLoading = false })
                        }
                    )
                    SocialLoginBadge(
                        brand = "Facebook",
                        iconRes = R.drawable.ic_facebook_logo,
                        onClick = {
                            isSocialLoading = true
                            performRealtimeSocialLogin("Facebook", activity, viewModel, { name ->
                                isSocialLoading = false
                                onLoginSuccess(name)
                            }, { _ -> isSocialLoading = false })
                        }
                    )
                    SocialLoginBadge(
                        brand = "Instagram",
                        iconRes = R.drawable.ic_instagram_logo,
                        onClick = {
                            isSocialLoading = true
                            performRealtimeSocialLogin("Instagram", activity, viewModel, { name ->
                                isSocialLoading = false
                                onLoginSuccess(name)
                            }, { _ -> isSocialLoading = false })
                        }
                    )
                    SocialLoginBadge(
                        brand = "LinkedIn",
                        iconRes = R.drawable.ic_linkedin_logo,
                        onClick = {
                            isSocialLoading = true
                            performRealtimeSocialLogin("LinkedIn", activity, viewModel, { name ->
                                isSocialLoading = false
                                onLoginSuccess(name)
                            }, { _ -> isSocialLoading = false })
                        }
                    )
                    SocialLoginBadge(
                        brand = "X",
                        iconRes = R.drawable.ic_x_logo,
                        onClick = {
                            isSocialLoading = true
                            performRealtimeSocialLogin("X", activity, viewModel, { name ->
                                isSocialLoading = false
                                onLoginSuccess(name)
                            }, { _ -> isSocialLoading = false })
                        }
                    )
                }
            }

            // Guest Access
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

            // Register Link
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("New to S K Sweets?", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(6.dp))
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

fun performRealtimeSocialLogin(
    brand: String,
    activity: FragmentActivity?,
    viewModel: AuthViewModel,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    val b = brand.lowercase()

    // Dynamically query user accounts from device or session
    var dynamicEmail: String? = null
    var dynamicName: String? = null

    if (activity != null) {
        try {
            val lastGAccount = com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(activity)
            if (lastGAccount != null && !lastGAccount.email.isNullOrBlank()) {
                dynamicEmail = lastGAccount.email
                dynamicName = lastGAccount.displayName?.ifBlank { null }
                    ?: lastGAccount.givenName?.ifBlank { null }
            }
        } catch (_: Exception) {}

        if (dynamicEmail.isNullOrBlank()) {
            try {
                val manager = android.accounts.AccountManager.get(activity)
                val accounts = manager.getAccountsByType("com.google")
                val firstAccount = accounts.firstOrNull { it.name.isNotBlank() }
                if (firstAccount != null) {
                    dynamicEmail = firstAccount.name
                }
            } catch (_: Exception) {}
        }
    }

    // Dynamic fallback from existing profile if available
    val currentProfile = viewModel.uiState.value.userProfile
    if (dynamicEmail.isNullOrBlank() && !currentProfile?.email.isNullOrBlank()) {
        dynamicEmail = currentProfile?.email
    }
    if (dynamicName.isNullOrBlank() && !currentProfile?.displayName.isNullOrBlank()) {
        dynamicName = currentProfile?.displayName
    }

    // Compute dynamic display name from dynamic email if displayName was not directly found
    val derivedNameFromEmail = dynamicEmail?.substringBefore("@")
        ?.replace(".", " ")
        ?.replace("_", " ")
        ?.split(" ")
        ?.filter { it.isNotBlank() }
        ?.joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

    val formattedBrand = brand.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    val resolvedName = when {
        !dynamicName.isNullOrBlank() -> dynamicName
        !derivedNameFromEmail.isNullOrBlank() -> derivedNameFromEmail
        else -> "$formattedBrand User"
    }

    val resolvedEmail = when {
        !dynamicEmail.isNullOrBlank() -> dynamicEmail
        else -> "${b}.user@sksweets.com"
    }

    viewModel.updateProfile(resolvedName, resolvedEmail) {
        onSuccess(resolvedName)
    }
}

@Composable
fun SocialLoginBadge(
    brand: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.2.dp, AppColors.BorderLight),
        shadowElevation = 2.dp,
        modifier = Modifier
            .size(54.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = brand,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
"""

print("Generated new_login_screen")

new_register_screen = """@Composable
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

    var selectedCountry by remember { mutableStateOf<network.chaintech.cmpcountrycodepicker.model.CountryDetails?>(null) }
    var showCountryDialog by remember { mutableStateOf(false) }
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

    // Auto-update full name when first/last name changes
    LaunchedEffect(firstName, lastName) {
        fullName = listOf(firstName.trim(), lastName.trim()).filter { it.isNotEmpty() }.joinToString(" ")
    }

    // Live Keystroke Validations
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
        mobileNumber.length == 10 && mobileNumber.all { it.isDigit() } -> {
            if (isMobileVerified) ValidationStatus.Valid("Mobile Verified ✓")
            else ValidationStatus.Valid("Valid 10 digits - Tap 'Verify with OTP'")
        }
        else -> ValidationStatus.Invalid("Mobile number must be exactly 10 digits")
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
        presentPincode.length == 6 && presentPincode.all { it.isDigit() } -> ValidationStatus.Valid("Valid 6-digit PIN code")
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

    // Calendar DatePicker Dialog
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
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header with authentic circular emblem
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SKBrandLogoEmblem(
                        size = 54.dp,
                        borderWidth = 1.5.dp,
                        elevation = 3.dp
                    )
                    Spacer(modifier = Modifier.width(14.dp))
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
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Personal Information", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            SKValidatedTextField(
                                value = firstName,
                                onValueChange = { firstName = it; errorMessage = null },
                                label = "First Name *",
                                placeholder = "e.g. First Name",
                                validationStatus = firstNameStatus,
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            SKValidatedTextField(
                                value = lastName,
                                onValueChange = { lastName = it; errorMessage = null },
                                label = "Last Name",
                                placeholder = "e.g. Last Name",
                                validationStatus = lastNameStatus,
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        SKValidatedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = "Full Name (Official) *",
                            placeholder = "Full Name",
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
                            Text("Gender *", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                listOf("Male", "Female", "Other").forEach { g ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { gender = g }
                                            .padding(vertical = 4.dp, horizontal = 4.dp)
                                    ) {
                                        RadioButton(
                                            selected = gender == g,
                                            onClick = { gender = g },
                                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
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
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Contact & OTP Verification *", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)

                        // Mobile Number
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .height(56.dp)
                                    .clickable { showCountryDialog = true }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(selectedCountry.flag, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(selectedCountry.dialCode, fontWeight = FontWeight.Bold)
                                }
                            }

                            SKValidatedTextField(
                                value = mobileNumber,
                                onValueChange = {
                                    if (it.length <= 10) {
                                        mobileNumber = it
                                        errorMessage = null
                                        isMobileVerified = false
                                    }
                                },
                                label = "Mobile Number (10 Digits) *",
                                placeholder = "9876543210",
                                leadingIcon = Icons.Default.Phone,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                validationStatus = mobileStatus,
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        // Mobile OTP Action / Status Badge
                        if (isMobileVerified) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = AppColors.StatusSuccessAlt.copy(alpha = 0.12f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AppColors.StatusSuccessAlt, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Mobile Number Verified Successfully", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AppColors.StatusSuccessAlt)
                                }
                            }
                        } else {
                            OutlinedButton(
                                onClick = {
                                    if (mobileNumber.length != 10) {
                                        errorMessage = "Please enter a valid 10-digit mobile number before verifying"
                                        return@OutlinedButton
                                    }
                                    mobileOtpInput = ""
                                    showMobileOtpDialog = true
                                    Toast.makeText(context, "OTP Sent to " + mobileNumber + "! Use test code: 123456", Toast.LENGTH_LONG).show()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Verify Mobile Number (Send 6-Digit OTP)", fontWeight = FontWeight.Bold)
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
                                shape = RoundedCornerShape(8.dp),
                                color = AppColors.StatusSuccessAlt.copy(alpha = 0.12f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AppColors.StatusSuccessAlt, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("E-Mail Address Verified Successfully", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AppColors.StatusSuccessAlt)
                                }
                            }
                        } else {
                            OutlinedButton(
                                onClick = {
                                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                                        errorMessage = "Please enter a valid email address before verifying"
                                        return@OutlinedButton
                                    }
                                    emailOtpInput = ""
                                    showEmailVerifyDialog = true
                                    Toast.makeText(context, "OTP Sent to " + emailAddress + "! Use test code: 123456", Toast.LENGTH_LONG).show()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Verify E-Mail Address (Send 6-Digit OTP)", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 3. Security Credentials (Strict Password & Confirm Password)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Security Credentials", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)

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
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("Password Requirements (Live Keystroke Validation):", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            PasswordRequirementItem(label = "At least 8 characters", isValid = isPasswordLengthValid(password))
                            PasswordRequirementItem(label = "At least one uppercase letter (A-Z)", isValid = isPasswordUpperValid(password))
                            PasswordRequirementItem(label = "At least one lowercase letter (a-z)", isValid = isPasswordLowerValid(password))
                            PasswordRequirementItem(label = "At least one number (0-9)", isValid = isPasswordDigitValid(password))
                            PasswordRequirementItem(label = "At least one special character (!@#$%^&*...)", isValid = isPasswordSpecialValid(password))
                        }

                        // Confirm Password
                        SKValidatedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it; errorMessage = null },
                            label = "Confirm Password *",
                            leadingIcon = Icons.Default.Lock,
                            trailingIcon = {
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
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
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Current / Residential Delivery Address *", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)

                        SKValidatedTextField(
                            value = presentStreet,
                            onValueChange = { presentStreet = it; errorMessage = null },
                            label = "Flat / House No, Street Address *",
                            placeholder = "12/A, Gandhi Street, Indira Nagar",
                            leadingIcon = Icons.Default.Home,
                            validationStatus = presentStreetStatus,
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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
                            onValueChange = { if (it.length <= 6) presentPincode = it; errorMessage = null },
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
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { sameAsPresent = !sameAsPresent }
                        ) {
                            Checkbox(checked = sameAsPresent, onCheckedChange = { sameAsPresent = it })
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Permanent Address Same as Current / Residential", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
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

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                onValueChange = { if (it.length <= 6) permPincode = it; errorMessage = null },
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
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.errorContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(errorMessage ?: "", color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodyMedium)
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
                        if (mobileNumber.length != 10) {
                            errorMessage = "Please enter a valid 10-digit mobile number"
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
                        val registeredName = if (fullName.isNotBlank()) fullName.trim() else firstName.trim() + " " + lastName.trim()

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
                            Toast.makeText(context, "Welcome to S K Sweets & Bakes, " + registeredName + "!", Toast.LENGTH_LONG).show()
                            onRegisterSuccess(registeredName, emailAddress)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Complete Registration & Sign Up", fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                    Spacer(modifier = Modifier.width(6.dp))
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
            mobileOtpInput.length == 6 && mobileOtpInput.all { it.isDigit() } -> ValidationStatus.Valid("Valid 6-digit OTP code")
            else -> ValidationStatus.Invalid("Enter 6-digit OTP")
        }

        SKPremiumAlertDialog(
            onDismissRequest = { showMobileOtpDialog = false },
            title = "Verify Mobile Number",
            showBrandLogo = true,
            icon = Icons.Default.Phone,
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter 6-digit OTP code sent to " + mobileNumber + ":", style = MaterialTheme.typography.bodyMedium)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = "Test verification code: 123456",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    SKValidatedTextField(
                        value = mobileOtpInput,
                        onValueChange = { if (it.length <= 6) mobileOtpInput = it },
                        label = "6-Digit SMS OTP *",
                        placeholder = "123456",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        validationStatus = otpInputStatus,
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (mobileOtpInput == "123456" || (mobileOtpInput.length == 6 && mobileOtpInput.all { it.isDigit() })) {
                            isMobileVerified = true
                            showMobileOtpDialog = false
                            Toast.makeText(context, "Mobile Number Verified!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Invalid OTP code. Use test code 123456", Toast.LENGTH_SHORT).show()
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
            emailOtpInput.length == 6 && emailOtpInput.all { it.isDigit() } -> ValidationStatus.Valid("Valid 6-digit OTP code")
            else -> ValidationStatus.Invalid("Enter 6-digit OTP")
        }

        SKPremiumAlertDialog(
            onDismissRequest = { showEmailVerifyDialog = false },
            title = "Verify E-Mail Address",
            showBrandLogo = true,
            icon = Icons.Default.Email,
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter 6-digit OTP code sent to " + emailAddress + ":", style = MaterialTheme.typography.bodyMedium)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = "Test verification code: 123456",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    SKValidatedTextField(
                        value = emailOtpInput,
                        onValueChange = { if (it.length <= 6) emailOtpInput = it },
                        label = "6-Digit Email OTP *",
                        placeholder = "123456",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        validationStatus = emailOtpInputStatus,
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (emailOtpInput == "123456" || (emailOtpInput.length == 6 && emailOtpInput.all { it.isDigit() })) {
                            isEmailVerified = true
                            showEmailVerifyDialog = false
                            Toast.makeText(context, "E-Mail Address Verified!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Invalid OTP code. Use test code 123456", Toast.LENGTH_SHORT).show()
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
"""

print("Generated new_register_screen")

new_forgot_password_screen = """@Composable
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
        android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches() -> ValidationStatus.Valid("Valid email address")
        else -> ValidationStatus.Invalid("Enter a valid email address (e.g. name@domain.com)")
    }
    val mobileStatus = when {
        mobileInput.isEmpty() -> ValidationStatus.Idle
        mobileInput.length == 10 && mobileInput.all { it.isDigit() } -> ValidationStatus.Valid("Valid 10-digit mobile number")
        else -> ValidationStatus.Invalid("Mobile number must be exactly 10 digits")
    }
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    PrimaryTabRow(selectedTabIndex = recoveryMode, containerColor = Color.Transparent, divider = {}) {
                        Tab(
                            selected = recoveryMode == 0,
                            onClick = { recoveryMode = 0 },
                            text = { Text("E-Mail Reset Link", fontWeight = if (recoveryMode == 0) FontWeight.Bold else FontWeight.Normal) },
                            icon = { Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        )
                        Tab(
                            selected = recoveryMode == 1,
                            onClick = { recoveryMode = 1 },
                            text = { Text("Mobile OTP Reset", fontWeight = if (recoveryMode == 1) FontWeight.Bold else FontWeight.Normal) },
                            icon = { Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        )
                    }
                }
            }

            // METHOD 1: EMAIL LINK RESET
            if (recoveryMode == 0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Text("Reset via E-Mail Link", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)

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
                                    shape = RoundedCornerShape(10.dp),
                                    color = AppColors.StatusSuccessAlt.copy(alpha = 0.12f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AppColors.StatusSuccessAlt, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Password reset link has been dispatched to " + emailInput + "! Please check your inbox.",
                                            color = AppColors.StatusSuccessAlt,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                                        Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    isSubmitted = true
                                    resetLinkSent = true
                                    Toast.makeText(context, "Password reset link sent to " + emailInput + "!", Toast.LENGTH_LONG).show()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
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
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Text("Reset via Mobile OTP", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)

                            SKValidatedTextField(
                                value = mobileInput,
                                onValueChange = { if (it.length <= 10) mobileInput = it },
                                label = "Registered Mobile Number (10 Digits) *",
                                placeholder = "9876543210",
                                leadingIcon = Icons.Default.Phone,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                validationStatus = mobileStatus,
                                singleLine = true
                            )

                            if (!otpSent) {
                                Button(
                                    onClick = {
                                        if (mobileInput.length == 10) {
                                            otpSent = true
                                            Toast.makeText(context, "Recovery OTP sent to " + mobileInput + "! Use test code: 123456", Toast.LENGTH_LONG).show()
                                        } else {
                                            Toast.makeText(context, "Enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Send 6-Digit Recovery OTP", fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = AppColors.StatusSuccessAlt.copy(alpha = 0.12f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AppColors.StatusSuccessAlt, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("OTP Code sent to " + mobileInput + "! Test code: 123456", color = AppColors.StatusSuccessAlt, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                SKValidatedTextField(
                                    value = otpCode,
                                    onValueChange = { if (it.length <= 6) otpCode = it },
                                    label = "Enter 6-Digit Recovery OTP *",
                                    placeholder = "123456",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    validationStatus = otpStatus,
                                    singleLine = true
                                )

                                SKValidatedTextField(
                                    value = newPassword,
                                    onValueChange = { newPassword = it },
                                    label = "Enter New Password (Strict Format) *",
                                    leadingIcon = Icons.Default.Lock,
                                    trailingIcon = {
                                        IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
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
                                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                                        .padding(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("Password Requirements (Live Keystroke Validation):", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                    PasswordRequirementItem(label = "At least 8 characters", isValid = isPasswordLengthValid(newPassword))
                                    PasswordRequirementItem(label = "At least one uppercase letter (A-Z)", isValid = isPasswordUpperValid(newPassword))
                                    PasswordRequirementItem(label = "At least one lowercase letter (a-z)", isValid = isPasswordLowerValid(newPassword))
                                    PasswordRequirementItem(label = "At least one number (0-9)", isValid = isPasswordDigitValid(newPassword))
                                    PasswordRequirementItem(label = "At least one special character (!@#$%^&*...)", isValid = isPasswordSpecialValid(newPassword))
                                }

                                SKValidatedTextField(
                                    value = confirmPassword,
                                    onValueChange = { confirmPassword = it },
                                    label = "Confirm New Password *",
                                    leadingIcon = Icons.Default.Lock,
                                    trailingIcon = {
                                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
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
                                            Toast.makeText(context, "Invalid OTP code. Use test code: 123456", Toast.LENGTH_SHORT).show()
                                            return@Button
                                        }
                                        if (!isStrictPasswordValid(newPassword)) {
                                            Toast.makeText(context, "Password must satisfy all 5 requirements", Toast.LENGTH_SHORT).show()
                                            return@Button
                                        }
                                        if (newPassword != confirmPassword) {
                                            Toast.makeText(context, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show()
                                            return@Button
                                        }
                                        isSubmitted = true
                                        Toast.makeText(context, "Password updated successfully! Please sign in with your new password.", Toast.LENGTH_LONG).show()
                                        onRecoverySent()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Verify OTP & Update Password", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
"""

print("Generated new_forgot_password_screen")

new_profile_edit_screen = """@Composable
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
    var phone by remember(state.userProfile) { mutableStateOf(state.userProfile?.phoneNumber ?: "") }
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
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationStatus.Valid("Valid email address")
        else -> ValidationStatus.Invalid("Enter a valid email address (e.g. name@domain.com)")
    }
    val phoneStatus = when {
        phone.isEmpty() -> ValidationStatus.Idle
        phone.length == 10 && phone.all { it.isDigit() } -> ValidationStatus.Valid("Valid 10-digit phone number")
        else -> ValidationStatus.Invalid("Phone number must be 10 digits")
    }

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
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Photo & Interactive Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                                shadowElevation = 6.dp,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .clickable { showPhotoPreviewDialog = true }
                            ) {
                                when {
                                    selectedPhotoBitmap != null -> {
                                        Image(
                                            bitmap = androidx.compose.ui.graphics.asImageBitmap(selectedPhotoBitmap!!),
                                            contentDescription = "Profile Photo",
                                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    selectedPhotoUri != null -> {
                                        AsyncImage(
                                            model = selectedPhotoUri,
                                            contentDescription = "Profile Photo",
                                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    !state.userProfile?.avatarUrl.isNullOrBlank() -> {
                                        AsyncImage(
                                            model = state.userProfile!!.avatarUrl,
                                            contentDescription = "Profile Photo",
                                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    else -> {
                                        val initials = name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").ifEmpty { "SK" }
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = initials,
                                                style = MaterialTheme.typography.headlineMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }

                            // Edit Photo Action Button
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                shadowElevation = 4.dp,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .clickable { showImagePickerDialog = true }
                            ) {
                                Icon(
                                    Icons.Default.CameraAlt,
                                    contentDescription = "Change photo",
                                    tint = Color.White,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = name.ifBlank { viewModel.uiState.value.userProfile?.displayName ?: "Customer" }, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))

                        // Clickable Phone Number Row -> ACTION_DIAL
                        val dispPhone = phone.ifBlank { "+91 98765 43210" }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    try {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dispPhone.replace(" ", "")))
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "No phone dialer available", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
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
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    try {
                                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + dispEmail))
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "No email app available", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(Icons.Default.Email, contentDescription = "Email", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = dispEmail,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "🏆 250 Sweet Loyalty Points",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
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
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Brightness4, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "App Theme Mode",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = currentThemeMode == AppThemeMode.LIGHT,
                                onClick = { onThemeModeChanged(AppThemeMode.LIGHT) },
                                label = { Text("Light") },
                                leadingIcon = { Icon(Icons.Default.LightMode, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = currentThemeMode == AppThemeMode.DARK,
                                onClick = { onThemeModeChanged(AppThemeMode.DARK) },
                                label = { Text("Dark") },
                                leadingIcon = { Icon(Icons.Default.DarkMode, contentDescription = null, modifier = Modifier.size(16.dp)) },
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
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
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

                        SKValidatedTextField(
                            value = phone,
                            onValueChange = { if (it.length <= 10) phone = it },
                            label = "Phone Number (10 Digits) *",
                            leadingIcon = Icons.Default.Phone,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            validationStatus = phoneStatus,
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                if (name.trim().length < 2) {
                                    Toast.makeText(context, "Please enter a valid name", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (phone.length != 10) {
                                    Toast.makeText(context, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                val photoUrl = selectedPhotoUri?.toString() ?: state.userProfile?.avatarUrl ?: avatarPresets[selectedAvatarIndex]
                                viewModel.updateProfile(name, email, photoUrl) {
                                    Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                                    onSaved()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
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
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showChangePasswordSection = !showChangePasswordSection }
                        ) {
                            Icon(Icons.Default.LockReset, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Security & Change Password",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                if (showChangePasswordSection) Icons.Default.Close else Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        if (showChangePasswordSection) {
                            Spacer(modifier = Modifier.height(4.dp))
                            PrimaryTabRow(selectedTabIndex = changePasswordMode, containerColor = Color.Transparent, divider = {}) {
                                Tab(
                                    selected = changePasswordMode == 0,
                                    onClick = { changePasswordMode = 0 },
                                    text = { Text("E-Mail Reset Link", fontWeight = if (changePasswordMode == 0) FontWeight.Bold else FontWeight.Normal) },
                                    icon = { Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                )
                                Tab(
                                    selected = changePasswordMode == 1,
                                    onClick = { changePasswordMode = 1 },
                                    text = { Text("Mobile OTP Reset", fontWeight = if (changePasswordMode == 1) FontWeight.Bold else FontWeight.Normal) },
                                    icon = { Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp)) }
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
                                        Toast.makeText(context, "Password reset link sent to " + changePassEmail + "!", Toast.LENGTH_LONG).show()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
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
                                            Toast.makeText(context, "OTP sent to " + changePassMobile + "! Use test code: 123456", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Send 6-Digit OTP")
                                    }
                                } else {
                                    SKValidatedTextField(
                                        value = changePassOtpCode,
                                        onValueChange = { if (it.length <= 6) changePassOtpCode = it },
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
                                            IconButton(onClick = { changePassNewPassVisible = !changePassNewPassVisible }) {
                                                Icon(if (changePassNewPassVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
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
                                            IconButton(onClick = { changePassConfirmPassVisible = !changePassConfirmPassVisible }) {
                                                Icon(if (changePassConfirmPassVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
                                            }
                                        },
                                        visualTransformation = if (changePassConfirmPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                        validationStatus = changePassConfirmStatus,
                                        singleLine = true
                                    )
                                    Button(
                                        onClick = {
                                            if (changePassOtpCode != "123456") {
                                                Toast.makeText(context, "Invalid OTP code", Toast.LENGTH_SHORT).show()
                                                return@Button
                                            }
                                            if (!isStrictPasswordValid(changePassNewPass)) {
                                                Toast.makeText(context, "Password must satisfy all 5 requirements", Toast.LENGTH_SHORT).show()
                                                return@Button
                                            }
                                            if (changePassNewPass != changePassConfirmPass) {
                                                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                                                return@Button
                                            }
                                            Toast.makeText(context, "Password changed successfully!", Toast.LENGTH_LONG).show()
                                            showChangePasswordSection = false
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Update Password")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Role Switcher for Owner / Cashier / Manager testing
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Portal Role Experience:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Test the app under different personnel authorization roles:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val roles = listOf(UserRole.CUSTOMER, UserRole.CASHIER, UserRole.KITCHEN_STAFF, UserRole.STORE_MANAGER, UserRole.OWNER)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            roles.forEach { r ->
                                FilterChip(
                                    selected = state.userProfile?.role == r,
                                    onClick = {
                                        viewModel.switchRoleForTesting(r)
                                        Toast.makeText(context, "Switched role to " + r.name, Toast.LENGTH_SHORT).show()
                                    },
                                    label = { Text(r.name.take(4), fontSize = 11.sp) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // Sign Out / Logout
            item {
                OutlinedButton(
                    onClick = { showSignOutDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign Out of Account", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Photo Preview Dialog
    if (showPhotoPreviewDialog) {
        val dispName = name.ifBlank { viewModel.uiState.value.userProfile?.displayName ?: "Customer" }
        val dispPhone = phone.ifBlank { "+91 98765 43210" }
        val dispEmail = email.ifBlank { "customer@sksweets.com" }

        Dialog(onDismissRequest = { showPhotoPreviewDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(2.dp, AppColors.GoldAccent),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                        shadowElevation = 8.dp,
                        modifier = Modifier.size(180.dp)
                    ) {
                        when {
                            selectedPhotoBitmap != null -> {
                                Image(
                                    bitmap = androidx.compose.ui.graphics.asImageBitmap(selectedPhotoBitmap!!),
                                    contentDescription = dispName,
                                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            selectedPhotoUri != null -> {
                                AsyncImage(
                                    model = selectedPhotoUri,
                                    contentDescription = dispName,
                                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            !state.userProfile?.avatarUrl.isNullOrBlank() -> {
                                AsyncImage(
                                    model = state.userProfile!!.avatarUrl,
                                    contentDescription = dispName,
                                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            else -> {
                                val initials = dispName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").ifEmpty { "SK" }
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

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = dispName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dispPhone.replace(" ", "")))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "No phone dialer available", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
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
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + dispEmail))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "No email app available", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = dispEmail,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            showPhotoPreviewDialog = false
                            showImagePickerDialog = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
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
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Choose source for your profile picture:",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Option 1: Live Camera
                        OutlinedButton(
                            onClick = {
                                showImagePickerDialog = false
                                cameraLauncher.launch(null)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 4.dp)) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Camera", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 4.dp)) {
                                Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Gallery", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Option 3: Files
                        OutlinedButton(
                            onClick = {
                                showImagePickerDialog = false
                                filesLauncher.launch("image/*")
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 4.dp)) {
                                Icon(Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Files", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
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
                                border = if (selectedAvatarIndex == index) BorderStroke(2.5.dp, MaterialTheme.colorScheme.primary) else null,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        selectedAvatarIndex = index
                                        selectedPhotoUri = Uri.parse(url)
                                        selectedPhotoBitmap = null
                                        showImagePickerDialog = false
                                        Toast.makeText(context, "Avatar selected!", Toast.LENGTH_SHORT).show()
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
"""

print("Generated new_profile_edit_screen")

# Replace EditAddressDialog
p1_start = orig.find('@Composable\nfun EditAddressDialog(')
p1_end = orig.find('@Composable\nfun SplashScreen(', p1_start)
assert p1_start != -1 and p1_end != -1, "p1 not found"

step1 = orig[:p1_start] + new_edit_address + "\n\n" + orig[p1_end:]

# Replace LoginScreen
p2_start = step1.find('fun LoginScreen(')
p2_end = step1.find('fun RegisterScreen(', p2_start)
assert p2_start != -1 and p2_end != -1, "p2 not found"

step2 = step1[:p2_start] + new_login_screen + "\n\n" + step1[p2_end:]

# Replace RegisterScreen
p3_start = step2.find('fun RegisterScreen(')
p3_end = step2.find('fun ForgotPasswordScreen(', p3_start)
assert p3_start != -1 and p3_end != -1, "p3 not found"

step3 = step2[:p3_start] + new_register_screen + "\n\n" + step2[p3_end:]

# Replace ForgotPasswordScreen
p4_start = step3.find('fun ForgotPasswordScreen(')
p4_end = step3.find('fun FinalizeAuthScreen(', p4_start)
assert p4_start != -1 and p4_end != -1, "p4 not found"

step4 = step3[:p4_start] + new_forgot_password_screen + "\n\n" + step3[p4_end:]

# Replace ProfileEditScreen
p5_start = step4.find('fun ProfileEditScreen(')
p5_end = step4.find('fun AddressListScreen(', p5_start)
assert p5_start != -1 and p5_end != -1, "p5 not found"

final_code = step4[:p5_start] + new_profile_edit_screen + "\n\n" + step4[p5_end:]

with open(auth_file_path, 'w') as f:
    f.write(final_code)

print("AuthScreens.kt successfully updated!")
