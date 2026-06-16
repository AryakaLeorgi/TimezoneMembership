package com.example.timezonemembership.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.timezonemembership.data.model.Constants
import com.example.timezonemembership.ui.theme.*
import com.example.timezonemembership.viewmodel.MemberViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayScreen(
    viewModel: MemberViewModel,
    memberId: Int,
    onNavigateBack: () -> Unit
) {
    val member by viewModel.selectedMember.collectAsState()
    val uiMessage by viewModel.uiMessage.collectAsState()
    var selectedMachine by remember { mutableStateOf<Constants.MachineCategory?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(memberId) { viewModel.loadMember(memberId) }
    LaunchedEffect(uiMessage) {
        uiMessage?.let { scope.launch { snackbarHostState.showSnackbar(it); viewModel.clearMessage() } }
    }

    selectedMachine?.let { machine ->
        AlertDialog(
            onDismissRequest = { selectedMachine = null },
            title = { Text("Konfirmasi Bermain", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Apakah Anda yakin ingin bermain?", color = OnLightSurfaceVariant)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(machine.name, color = OnLightSurface, fontWeight = FontWeight.SemiBold)
                    Text("-${machine.tizoCost} Tizo", color = ErrorRed)
                    Text("+${machine.rewardPoints} Poin", color = PointsColor)
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.performPlay(memberId, machine); selectedMachine = null }, colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)) { Text("Bermain!", color = LightSurface) }
            },
            dismissButton = { TextButton(onClick = { selectedMachine = null }) { Text("Batal") } },
            containerColor = LightCard,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Simulator Bermain", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBackground, titleContentColor = OnLightSurface, navigationIconContentColor = OnLightSurface)
            )
        },
        containerColor = LightBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)
        ) {
            member?.let { m ->
                // Balance
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LightCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = TizoColor, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Saldo Tizo", style = MaterialTheme.typography.labelMedium, color = OnLightSurfaceVariant)
                            }
                            Text("${m.tizoBalance}", style = MaterialTheme.typography.headlineMedium, color = TizoColor, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = PointsColor, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reward Points", style = MaterialTheme.typography.labelMedium, color = OnLightSurfaceVariant)
                            }
                            Text("${m.rewardPoints}", style = MaterialTheme.typography.headlineMedium, color = PointsColor, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text("Pilih Mesin", style = MaterialTheme.typography.headlineMedium, color = OnLightSurface, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Constants.MACHINE_CATEGORIES.forEach { machine ->
                    val isAffordable = m.tizoBalance >= machine.tizoCost

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = LightCard, disabledContainerColor = LightSurfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isAffordable) 2.dp else 0.dp),
                        onClick = { selectedMachine = machine },
                        enabled = isAffordable
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = if (isAffordable) BluePrimary else OnLightSurfaceVariant,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = machine.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = if (isAffordable) OnLightSurface else OnLightSurfaceVariant,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(text = machine.description, style = MaterialTheme.typography.bodySmall, color = OnLightSurfaceVariant)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Text(text = "-${machine.tizoCost} Tizo", style = MaterialTheme.typography.labelLarge, color = if (isAffordable) ErrorRed else ErrorRed.copy(alpha = 0.5f), fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(text = "+${machine.rewardPoints} Poin", style = MaterialTheme.typography.labelLarge, color = if (isAffordable) PointsColor else PointsColor.copy(alpha = 0.5f), fontWeight = FontWeight.SemiBold)
                                }
                            }
                            if (!isAffordable) {
                                Text(text = "Saldo\nKurang", style = MaterialTheme.typography.labelSmall, color = ErrorRed.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
