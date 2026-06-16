package com.example.timezonemembership.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.timezonemembership.data.model.Transaction
import com.example.timezonemembership.ui.theme.*
import com.example.timezonemembership.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: TransactionViewModel,
    memberId: Int,
    onNavigateBack: () -> Unit
) {
    val transactions by viewModel.transactions.collectAsState()

    LaunchedEffect(memberId) { viewModel.loadTransactions(memberId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Transaksi", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBackground, titleContentColor = OnLightSurface, navigationIconContentColor = OnLightSurface)
            )
        },
        containerColor = LightBackground
    ) { paddingValues ->
        if (transactions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = OnLightSurfaceVariant, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Belum ada transaksi", style = MaterialTheme.typography.titleMedium, color = OnLightSurfaceVariant)
                    Text(text = "Transaksi akan muncul setelah Top-Up, Bermain, atau Redeem", style = MaterialTheme.typography.bodySmall, color = OnLightSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }
                items(transactions) { transaction -> TransactionCard(transaction = transaction) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun TransactionCard(transaction: Transaction) {
    val (badgeColor, badgeText, icon) = when (transaction.transactionType) {
        "TOPUP" -> Triple(SuccessGreen, "TOP-UP", Icons.Default.ShoppingCart)
        "PLAY" -> Triple(BluePrimary, "BERMAIN", Icons.Default.PlayArrow)
        "REDEEM" -> Triple(GoldSecondary, "REDEEM", Icons.Default.Refresh)
        else -> Triple(OnLightSurfaceVariant, "LAIN", Icons.Default.DateRange)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = LightCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(badgeColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = badgeColor, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = transaction.description, style = MaterialTheme.typography.titleMedium, color = OnLightSurface, fontWeight = FontWeight.SemiBold, maxLines = 2)
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(badgeColor.copy(alpha = 0.12f)).padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = badgeText, style = MaterialTheme.typography.labelSmall, color = badgeColor, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = formatTimestamp(transaction.timestamp), style = MaterialTheme.typography.labelSmall, color = OnLightSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                if (transaction.tizoMutation != 0) {
                    Text(
                        text = "${if (transaction.tizoMutation > 0) "+" else ""}${transaction.tizoMutation} Tizo",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (transaction.tizoMutation > 0) TizoColor else ErrorRed,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (transaction.pointMutation != 0) {
                    Text(
                        text = "${if (transaction.pointMutation > 0) "+" else ""}${transaction.pointMutation} Poin",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (transaction.pointMutation > 0) PointsColor else ErrorRed,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
    return sdf.format(Date(timestamp))
}
