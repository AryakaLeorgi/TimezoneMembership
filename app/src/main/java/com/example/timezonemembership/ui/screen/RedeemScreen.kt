package com.example.timezonemembership.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.timezonemembership.data.model.Constants
import com.example.timezonemembership.ui.theme.*
import com.example.timezonemembership.viewmodel.MemberViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedeemScreen(
    viewModel: MemberViewModel,
    memberId: Int,
    onNavigateBack: () -> Unit
) {
    val member by viewModel.selectedMember.collectAsState()
    val uiMessage by viewModel.uiMessage.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedReward by remember { mutableStateOf<Constants.RedeemReward?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val tabs = listOf("Gratis Bermain", "Merchandise", "Diskon")

    LaunchedEffect(memberId) { viewModel.loadMember(memberId) }
    LaunchedEffect(uiMessage) {
        uiMessage?.let { scope.launch { snackbarHostState.showSnackbar(it); viewModel.clearMessage() } }
    }

    selectedReward?.let { reward ->
        AlertDialog(
            onDismissRequest = { selectedReward = null },
            title = { Text("Konfirmasi Penukaran", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Apakah Anda yakin ingin menukarkan poin?", color = OnLightSurfaceVariant)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(reward.name, color = OnLightSurface, fontWeight = FontWeight.SemiBold)
                    Text("-${reward.pointCost} Poin", color = ErrorRed)
                    if (reward.description.isNotEmpty()) {
                        Text(reward.description, color = OnLightSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                    }
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.performRedeem(memberId, reward); selectedReward = null }, colors = ButtonDefaults.buttonColors(containerColor = GoldSecondary)) { Text("Tukarkan", color = LightSurface) }
            },
            dismissButton = { TextButton(onClick = { selectedReward = null }) { Text("Batal") } },
            containerColor = LightCard,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Tukar Poin (Redeem)", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBackground, titleContentColor = OnLightSurface, navigationIconContentColor = OnLightSurface)
            )
        },
        containerColor = LightBackground
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            member?.let { m ->
                // Points Balance
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LightCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .background(Brush.horizontalGradient(listOf(GoldSecondary.copy(alpha = 0.08f), GoldSecondaryLight.copy(alpha = 0.04f))))
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = PointsColor, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Reward Points Anda", style = MaterialTheme.typography.labelLarge, color = OnLightSurfaceVariant)
                            }
                            Text("${m.rewardPoints}", style = MaterialTheme.typography.displayMedium, color = PointsColor, fontWeight = FontWeight.Black)
                            Text("Poin tersedia untuk ditukarkan", style = MaterialTheme.typography.bodySmall, color = OnLightSurfaceVariant)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = LightBackground,
                    contentColor = BluePrimary,
                    edgePadding = 16.dp
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(title, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal, color = if (selectedTab == index) BluePrimary else OnLightSurfaceVariant)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                val currentRewards = when (selectedTab) {
                    0 -> Constants.REDEEM_FREE_PLAY
                    1 -> Constants.REDEEM_MERCHANDISE
                    2 -> Constants.REDEEM_DISCOUNT
                    else -> emptyList()
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(currentRewards) { reward ->
                        val canRedeem = m.rewardPoints >= reward.pointCost
                        val icon = when (reward.category) {
                            Constants.RedeemCategory.FREE_PLAY -> Icons.Default.PlayArrow
                            Constants.RedeemCategory.MERCHANDISE -> Icons.Default.ShoppingCart
                            Constants.RedeemCategory.DISCOUNT -> Icons.Default.Refresh
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = LightCard, disabledContainerColor = LightSurfaceVariant),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (canRedeem) 1.dp else 0.dp),
                            onClick = { selectedReward = reward },
                            enabled = canRedeem
                        ) {
                            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(icon, contentDescription = null, tint = if (canRedeem) BluePrimary else OnLightSurfaceVariant, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = reward.name, style = MaterialTheme.typography.titleMedium, color = if (canRedeem) OnLightSurface else OnLightSurfaceVariant, fontWeight = FontWeight.SemiBold)
                                    if (reward.description.isNotEmpty()) {
                                        Text(text = reward.description, style = MaterialTheme.typography.bodySmall, color = OnLightSurfaceVariant)
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = "${reward.pointCost}", style = MaterialTheme.typography.titleLarge, color = if (canRedeem) PointsColor else PointsColor.copy(alpha = 0.4f), fontWeight = FontWeight.Bold)
                                    Text(text = "Poin", style = MaterialTheme.typography.labelSmall, color = if (canRedeem) OnLightSurfaceVariant else OnLightSurfaceVariant.copy(alpha = 0.4f))
                                    if (!canRedeem) {
                                        Text(text = "Kurang", style = MaterialTheme.typography.labelSmall, color = ErrorRed.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}
