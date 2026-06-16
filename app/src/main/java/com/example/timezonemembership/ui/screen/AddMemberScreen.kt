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
import androidx.compose.ui.unit.dp
import com.example.timezonemembership.ui.theme.*
import com.example.timezonemembership.viewmodel.MemberViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberScreen(
    viewModel: MemberViewModel,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isAdminRole by remember { mutableStateOf(false) }

    var nameErrorMsg by remember { mutableStateOf<String?>(null) }
    var emailErrorMsg by remember { mutableStateOf<String?>(null) }
    var phoneErrorMsg by remember { mutableStateOf<String?>(null) }
    var passwordErrorMsg by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val uiMessage by viewModel.uiMessage.collectAsState()

    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it); viewModel.clearMessage() }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Tambah Member", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBackground, titleContentColor = OnLightSurface, navigationIconContentColor = OnLightSurface)
            )
        },
        containerColor = LightBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)
        ) {
            // Header
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(listOf(BluePrimary.copy(alpha = 0.08f), BluePrimaryLight.copy(alpha = 0.04f))),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Text(text = "Tambah Akun Baru", style = MaterialTheme.typography.headlineMedium, color = OnLightSurface, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Masukkan detail akun member atau administrator baru", style = MaterialTheme.typography.bodyMedium, color = OnLightSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LightCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Nama Lengkap", style = MaterialTheme.typography.labelLarge, color = OnLightSurface, modifier = Modifier.padding(bottom = 4.dp))
                    OutlinedTextField(
                        value = name, onValueChange = { name = it; nameErrorMsg = null }, modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Masukkan nama lengkap") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BluePrimary) },
                        isError = nameErrorMsg != null, supportingText = nameErrorMsg?.let { { Text(it) } }, singleLine = true, shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BluePrimary, unfocusedBorderColor = OnLightSurfaceVariant.copy(alpha = 0.3f))
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Email", style = MaterialTheme.typography.labelLarge, color = OnLightSurface, modifier = Modifier.padding(bottom = 4.dp))
                    OutlinedTextField(
                        value = email, onValueChange = { email = it; emailErrorMsg = null }, modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("contoh@email.com") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = BluePrimary) },
                        isError = emailErrorMsg != null, supportingText = emailErrorMsg?.let { { Text(it) } }, singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BluePrimary, unfocusedBorderColor = OnLightSurfaceVariant.copy(alpha = 0.3f))
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "No. Handphone", style = MaterialTheme.typography.labelLarge, color = OnLightSurface, modifier = Modifier.padding(bottom = 4.dp))
                    OutlinedTextField(
                        value = phone, onValueChange = { phone = it; phoneErrorMsg = null }, modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("08xxxxxxxxxx") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = BluePrimary) },
                        isError = phoneErrorMsg != null, supportingText = phoneErrorMsg?.let { { Text(it) } }, singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BluePrimary, unfocusedBorderColor = OnLightSurfaceVariant.copy(alpha = 0.3f))
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Password (Min 6 karakter)", style = MaterialTheme.typography.labelLarge, color = OnLightSurface, modifier = Modifier.padding(bottom = 4.dp))
                    OutlinedTextField(
                        value = password, onValueChange = { password = it; passwordErrorMsg = null }, modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Masukkan password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BluePrimary) },
                        isError = passwordErrorMsg != null, supportingText = passwordErrorMsg?.let { { Text(it) } }, singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BluePrimary, unfocusedBorderColor = OnLightSurfaceVariant.copy(alpha = 0.3f))
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isAdminRole, onCheckedChange = { isAdminRole = it }, colors = CheckboxDefaults.colors(checkedColor = GoldSecondary, uncheckedColor = OnLightSurfaceVariant))
                        Text(text = "Daftar sebagai Admin", style = MaterialTheme.typography.bodyMedium, color = OnLightSurface)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    var isValid = true
                    if (name.isBlank()) { nameErrorMsg = "Nama wajib diisi"; isValid = false }
                    if (email.isBlank()) { emailErrorMsg = "Email wajib diisi"; isValid = false } else if (!emailRegex.matches(email.trim())) { emailErrorMsg = "Format email tidak valid"; isValid = false }
                    val np = phone.trim()
                    if (phone.isBlank()) { phoneErrorMsg = "No. HP wajib diisi"; isValid = false }
                    else if (!np.startsWith("08") && !np.startsWith("+62")) { phoneErrorMsg = "No. HP harus diawali '08' atau '+62'"; isValid = false }
                    else if (np.replace("+", "").any { !it.isDigit() }) { phoneErrorMsg = "No. HP hanya boleh berisi angka"; isValid = false }
                    else if (np.length < 10 || np.length > 15) { phoneErrorMsg = "Panjang No. HP harus 10-15 digit"; isValid = false }
                    if (password.length < 6) { passwordErrorMsg = "Password minimal 6 karakter"; isValid = false }

                    if (isValid) {
                        viewModel.addMemberByAdmin(name.trim(), email.trim(), phone.trim(), password, if (isAdminRole) "ADMIN" else "MEMBER")
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary, contentColor = LightSurface)
            ) {
                Text(text = "Tambah Akun", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
