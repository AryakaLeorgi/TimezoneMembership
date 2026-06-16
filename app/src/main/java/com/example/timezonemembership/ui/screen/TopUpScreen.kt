package com.example.timezonemembership.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.timezonemembership.data.model.Constants
import com.example.timezonemembership.ui.theme.*
import com.example.timezonemembership.viewmodel.MemberViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopUpScreen(
    viewModel: MemberViewModel,
    memberId: Int,
    onNavigateBack: () -> Unit
) {
    val member by viewModel.selectedMember.collectAsState()
    val uiMessage by viewModel.uiMessage.collectAsState()
    var selectedTier by remember { mutableStateOf<Constants.TopUpTier?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(memberId) { viewModel.loadMember(memberId) }

    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it); viewModel.clearMessage() }
        }
    }

    // Confirmation Dialog
    selectedTier?.let { tier ->
        AlertDialog(
            onDismissRequest = { selectedTier = null },
            title = { Text("Konfirmasi Top-Up", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Apakah Anda yakin ingin melakukan Top-Up?", color = OnLightSurfaceVariant)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(tier.name, color = OnLightSurface, fontWeight = FontWeight.SemiBold)
                    Text("Harga: Rp${formatCurrency(tier.priceRp)}", color = OnLightSurfaceVariant)
                    Text("+${tier.tizoAmount} Tizo", color = TizoColor)
                    Text("+${tier.rewardPoints} Poin", color = PointsColor)
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.performTopUp(memberId, tier); selectedTier = null },
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                ) { Text("Konfirmasi", color = LightSurface) }
            },
            dismissButton = {
                TextButton(onClick = { selectedTier = null }) { Text("Batal", color = OnLightSurfaceVariant) }
            },
            containerColor = LightCard,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Top-Up Tizo", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBackground, titleContentColor = OnLightSurface, navigationIconContentColor = OnLightSurface)
            )
        },
        containerColor = LightBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp)
        ) {
            // Current Balance
            member?.let { m ->
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
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Pilih Paket Top-Up", style = MaterialTheme.typography.headlineMedium, color = OnLightSurface, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(Constants.TOP_UP_TIERS) { tier ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = LightCard),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        onClick = { selectedTier = tier }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = tier.name, style = MaterialTheme.typography.labelLarge, color = GoldSecondary, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Rp${formatCurrency(tier.priceRp)}", style = MaterialTheme.typography.titleLarge, color = OnLightSurface, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "+${tier.tizoAmount} Tizo", style = MaterialTheme.typography.bodyMedium, color = TizoColor, fontWeight = FontWeight.SemiBold)
                            Text(text = "+${tier.rewardPoints} Poin", style = MaterialTheme.typography.bodySmall, color = PointsColor)
                        }
                    }
                }
            }
        }
    }
}

private fun formatCurrency(amount: Int): String {
    return String.format("%,d", amount).replace(',', '.')
}
