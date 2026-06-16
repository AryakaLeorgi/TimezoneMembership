package com.example.timezonemembership.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timezonemembership.ui.theme.*
import com.example.timezonemembership.viewmodel.MemberViewModel

@Composable
fun AuthScreen(
    viewModel: MemberViewModel,
    onAuthSuccess: (role: String, id: Int) -> Unit
) {
    var showRegister by remember { mutableStateOf(false) }
    var registrationSuccess by remember { mutableStateOf(false) }
    val currentUser by viewModel.currentUser.collectAsState()

    // Redirect if already logged in
    LaunchedEffect(currentUser) {
        currentUser?.let {
            onAuthSuccess(it.role, it.id)
        }
    }

    if (showRegister) {
        RegisterPage(
            viewModel = viewModel,
            onBackToLogin = {
                showRegister = false
            },
            onRegistrationSuccess = {
                registrationSuccess = true
                showRegister = false
            }
        )
    } else {
        LoginPage(
            viewModel = viewModel,
            registrationSuccess = registrationSuccess,
            onDismissSuccess = { registrationSuccess = false },
            onNavigateToRegister = {
                registrationSuccess = false
                viewModel.clearAuthError()
                showRegister = true
            }
        )
    }
}

// ==================== LOGIN PAGE ====================

@Composable
private fun LoginPage(
    viewModel: MemberViewModel,
    registrationSuccess: Boolean,
    onDismissSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    val authError by viewModel.authError.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Brand Header
            Text(
                text = "TIMEZONE",
                style = MaterialTheme.typography.displayMedium,
                color = BluePrimary,
                fontWeight = FontWeight.Black,
                letterSpacing = 3.sp
            )
            Text(
                text = "Membership & Loyalty",
                style = MaterialTheme.typography.titleMedium,
                color = OnLightSurfaceVariant,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Registration Success Message
            if (registrationSuccess) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.12f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Registrasi berhasil! Silakan login dengan akun baru Anda.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SuccessGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Auth Error Message
            authError?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ErrorRed.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = ErrorRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ErrorRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Login Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LightCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Masuk ke Akun",
                        style = MaterialTheme.typography.headlineMedium,
                        color = OnLightSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Masukkan email dan password Anda",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnLightSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Email Field
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.labelLarge,
                        color = OnLightSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = false
                            onDismissSuccess()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Masukkan email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = BluePrimary) },
                        isError = emailError,
                        supportingText = if (emailError) { { Text("Email wajib diisi") } } else null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = OnLightSurfaceVariant.copy(alpha = 0.3f)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.labelLarge,
                        color = OnLightSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = false
                            onDismissSuccess()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Masukkan password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BluePrimary) },
                        isError = passwordError,
                        supportingText = if (passwordError) { { Text("Password wajib diisi") } } else null,
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = OnLightSurfaceVariant.copy(alpha = 0.3f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Remember Me
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = BluePrimary,
                                uncheckedColor = OnLightSurfaceVariant
                            )
                        )
                        Text(
                            text = "Ingat Akun Saya",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnLightSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Login Button
                    Button(
                        onClick = {
                            emailError = email.isBlank()
                            passwordError = password.isBlank()
                            if (!emailError && !passwordError) {
                                viewModel.login(email.trim(), password, rememberMe)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BluePrimary,
                            contentColor = LightSurface
                        )
                    ) {
                        Text(
                            text = "Masuk",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Register Link
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Belum punya akun? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnLightSurfaceVariant
                )
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        text = "Daftar Sekarang",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BluePrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ==================== REGISTER PAGE ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterPage(
    viewModel: MemberViewModel,
    onBackToLogin: () -> Unit,
    onRegistrationSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isAdminRole by remember { mutableStateOf(false) }

    var nameErrorMsg by remember { mutableStateOf<String?>(null) }
    var emailErrorMsg by remember { mutableStateOf<String?>(null) }
    var phoneErrorMsg by remember { mutableStateOf<String?>(null) }
    var passwordErrorMsg by remember { mutableStateOf<String?>(null) }
    var confirmPasswordErrorMsg by remember { mutableStateOf<String?>(null) }

    val authError by viewModel.authError.collectAsState()
    val uiMessage by viewModel.uiMessage.collectAsState()

    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    // Listen for successful registration
    LaunchedEffect(uiMessage) {
        if (uiMessage?.contains("Registrasi berhasil") == true) {
            viewModel.clearMessage()
            onRegistrationSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Buat Akun Baru", fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.clearAuthError()
                        onBackToLogin()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBackground,
                    titleContentColor = OnLightSurface,
                    navigationIconContentColor = OnLightSurface
                )
            )
        },
        containerColor = LightBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // Header
            Text(
                text = "Registrasi Member",
                style = MaterialTheme.typography.headlineMedium,
                color = OnLightSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Isi data lengkap untuk mendaftarkan akun baru",
                style = MaterialTheme.typography.bodyMedium,
                color = OnLightSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Auth Error
            authError?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ErrorRed.copy(alpha = 0.1f))
                ) {
                    Text(
                        text = it,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = ErrorRed,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LightCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Name
                    Text(text = "Nama Lengkap", style = MaterialTheme.typography.labelLarge, color = OnLightSurface, modifier = Modifier.padding(bottom = 4.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; nameErrorMsg = null },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Masukkan nama lengkap") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BluePrimary) },
                        isError = nameErrorMsg != null,
                        supportingText = nameErrorMsg?.let { { Text(it) } },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = OnLightSurfaceVariant.copy(alpha = 0.3f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email
                    Text(text = "Email", style = MaterialTheme.typography.labelLarge, color = OnLightSurface, modifier = Modifier.padding(bottom = 4.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; emailErrorMsg = null },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("contoh@email.com") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = BluePrimary) },
                        isError = emailErrorMsg != null,
                        supportingText = emailErrorMsg?.let { { Text(it) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = OnLightSurfaceVariant.copy(alpha = 0.3f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Phone
                    Text(text = "No. Handphone", style = MaterialTheme.typography.labelLarge, color = OnLightSurface, modifier = Modifier.padding(bottom = 4.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it; phoneErrorMsg = null },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("08xxxxxxxxxx") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = BluePrimary) },
                        isError = phoneErrorMsg != null,
                        supportingText = phoneErrorMsg?.let { { Text(it) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = OnLightSurfaceVariant.copy(alpha = 0.3f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password
                    Text(text = "Password (Min 6 karakter)", style = MaterialTheme.typography.labelLarge, color = OnLightSurface, modifier = Modifier.padding(bottom = 4.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; passwordErrorMsg = null },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Masukkan password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BluePrimary) },
                        isError = passwordErrorMsg != null,
                        supportingText = passwordErrorMsg?.let { { Text(it) } },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = OnLightSurfaceVariant.copy(alpha = 0.3f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Confirm Password
                    Text(text = "Konfirmasi Password", style = MaterialTheme.typography.labelLarge, color = OnLightSurface, modifier = Modifier.padding(bottom = 4.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it; confirmPasswordErrorMsg = null },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Masukkan ulang password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BluePrimary) },
                        isError = confirmPasswordErrorMsg != null,
                        supportingText = confirmPasswordErrorMsg?.let { { Text(it) } },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = OnLightSurfaceVariant.copy(alpha = 0.3f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Admin toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isAdminRole,
                            onCheckedChange = { isAdminRole = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = GoldSecondary,
                                uncheckedColor = OnLightSurfaceVariant
                            )
                        )
                        Text(
                            text = "Daftar sebagai Admin (Demo)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnLightSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register Button
            Button(
                onClick = {
                    var isValid = true

                    if (name.isBlank()) { nameErrorMsg = "Nama wajib diisi"; isValid = false }

                    if (email.isBlank()) {
                        emailErrorMsg = "Email wajib diisi"; isValid = false
                    } else if (!emailRegex.matches(email.trim())) {
                        emailErrorMsg = "Format email tidak valid (contoh: user@domain.com)"; isValid = false
                    }

                    val normalizedPhone = phone.trim()
                    if (phone.isBlank()) {
                        phoneErrorMsg = "No. HP wajib diisi"; isValid = false
                    } else if (!normalizedPhone.startsWith("08") && !normalizedPhone.startsWith("+62")) {
                        phoneErrorMsg = "No. HP harus diawali '08' atau '+62'"; isValid = false
                    } else if (normalizedPhone.replace("+", "").any { !it.isDigit() }) {
                        phoneErrorMsg = "No. HP hanya boleh berisi angka"; isValid = false
                    } else if (normalizedPhone.length < 10 || normalizedPhone.length > 15) {
                        phoneErrorMsg = "Panjang No. HP harus 10-15 digit"; isValid = false
                    }

                    if (password.length < 6) { passwordErrorMsg = "Password minimal 6 karakter"; isValid = false }
                    if (confirmPassword != password) { confirmPasswordErrorMsg = "Password tidak cocok"; isValid = false }

                    if (isValid) {
                        viewModel.register(
                            name = name.trim(),
                            email = email.trim(),
                            phone = phone.trim(),
                            password = password,
                            role = if (isAdminRole) "ADMIN" else "MEMBER"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePrimary,
                    contentColor = LightSurface
                )
            ) {
                Text(
                    text = "Daftar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Back to Login Link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sudah punya akun? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnLightSurfaceVariant
                )
                TextButton(onClick = {
                    viewModel.clearAuthError()
                    onBackToLogin()
                }) {
                    Text(
                        text = "Masuk",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BluePrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
