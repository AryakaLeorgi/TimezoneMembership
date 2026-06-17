package com.example.timezonemembership.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timezonemembership.ui.theme.*
import com.example.timezonemembership.viewmodel.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDetailScreen(
    viewModel: MemberViewModel,
    memberId: Int,
    onNavigateBack: () -> Unit,
    onTopUp: () -> Unit,
    onPlay: () -> Unit,
    onRedeem: () -> Unit,
    onHistory: () -> Unit,
    onLogout: () -> Unit
) {
    val member by viewModel.selectedMember.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(memberId) {
        viewModel.loadMember(memberId)
    }

    val isSelf = currentUser?.id == memberId
    val isAdmin = currentUser?.role == "ADMIN"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isSelf) "Profil Saya" else "Kartu Member",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    if (isAdmin && !isSelf) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                        }
                    }
                },
                actions = {
                    if (isSelf) {
                        IconButton(onClick = {
                            viewModel.logout()
                            onLogout()
                        }) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = ErrorRed)
                        }
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
        member?.let { m ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                // ==================== MEMBER CARD ====================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = BluePrimaryDark),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        BluePrimaryDark,
                                        BluePrimary,
                                        BluePrimaryLight
                                    )
                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "TIMEZONE",
                                style = MaterialTheme.typography.titleMedium,
                                color = GoldSecondary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = if (m.role == "ADMIN") "ADMIN CARD" else "MEMBERSHIP CARD",
                                style = MaterialTheme.typography.labelSmall,
                                color = LightSurface.copy(alpha = 0.7f),
                                letterSpacing = 4.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(LightSurface.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = m.name.take(1).uppercase(),
                                    style = MaterialTheme.typography.displayMedium,
                                    color = LightSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = m.name,
                                style = MaterialTheme.typography.headlineMedium,
                                color = LightSurface,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Email, contentDescription = null, tint = LightSurface.copy(alpha = 0.7f), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = m.email, style = MaterialTheme.typography.bodySmall, color = LightSurface.copy(alpha = 0.7f))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = LightSurface.copy(alpha = 0.7f), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = m.phone, style = MaterialTheme.typography.bodySmall, color = LightSurface.copy(alpha = 0.7f))
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // QR Code Container
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
                                modifier = Modifier.size(100.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize().padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    SimulatedQRCode(
                                        modifier = Modifier.fillMaxSize(),
                                        color = androidx.compose.ui.graphics.Color.Black
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Barcode Container (White card background to make the black barcode pop)
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
                                modifier = Modifier.width(180.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    SimulatedBarcode(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(36.dp),
                                        color = androidx.compose.ui.graphics.Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "TZ-%04d".format(m.id),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = androidx.compose.ui.graphics.Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 2.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ==================== BALANCE CARDS (MEMBER only) ====================
                if (m.role == "MEMBER") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = LightCard),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = TizoColor, modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Saldo Tizo", style = MaterialTheme.typography.labelMedium, color = OnLightSurfaceVariant)
                                Text(text = "${m.tizoBalance}", style = MaterialTheme.typography.displayMedium, color = TizoColor, fontWeight = FontWeight.Black)
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = LightCard),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = PointsColor, modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Reward Points", style = MaterialTheme.typography.labelMedium, color = OnLightSurfaceVariant)
                                Text(text = "${m.rewardPoints}", style = MaterialTheme.typography.displayMedium, color = PointsColor, fontWeight = FontWeight.Black)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Menu Layanan",
                        style = MaterialTheme.typography.headlineMedium,
                        color = OnLightSurface,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ActionButton(modifier = Modifier.weight(1f), icon = Icons.Default.ShoppingCart, label = "Top-Up", accentColor = SuccessGreen, onClick = onTopUp)
                        ActionButton(modifier = Modifier.weight(1f), icon = Icons.Default.PlayArrow, label = "Bermain", accentColor = BluePrimary, onClick = onPlay)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ActionButton(modifier = Modifier.weight(1f), icon = Icons.Default.Refresh, label = "Redeem", accentColor = GoldSecondary, onClick = onRedeem)
                        ActionButton(modifier = Modifier.weight(1f), icon = Icons.Default.List, label = "Riwayat", accentColor = OnLightSurfaceVariant, onClick = onHistory)
                    }
                } else {
                    Spacer(modifier = Modifier.height(20.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = LightCard),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Akun Administrator", style = MaterialTheme.typography.titleMedium, color = GoldSecondary, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Sebagai Administrator, Anda memiliki akses penuh ke seluruh database keanggotaan Timezone.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnLightSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BluePrimary)
            }
        }
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    accentColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(accentColor.copy(alpha = 0.06f))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(icon, contentDescription = label, tint = accentColor, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = label, style = MaterialTheme.typography.titleMedium, color = OnLightSurface, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SimulatedBarcode(
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Black
) {
    // Standard barcode sequence simulator
    val barPattern = listOf(
        2, 1, 3, 1, 1, 2, 4, 1, 1, 3, 2, 1, 4, 2, 1, 1, 
        3, 1, 2, 2, 1, 4, 1, 1, 2, 3, 1, 2, 1, 1, 4, 2
    )
    
    androidx.compose.foundation.Canvas(modifier = modifier) {
        var currentX = 0f
        val scale = size.width / barPattern.sum()
        
        barPattern.forEachIndexed { index, widthRatio ->
            val barWidth = widthRatio * scale
            if (index % 2 == 0) {
                drawRect(
                    color = color,
                    topLeft = androidx.compose.ui.geometry.Offset(currentX, 0f),
                    size = androidx.compose.ui.geometry.Size(barWidth, size.height)
                )
            }
            currentX += barWidth
        }
    }
}

@Composable
private fun SimulatedQRCode(
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Black
) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val gridSize = 17
        val cellSize = size.width / gridSize
        
        // Draw white background
        drawRect(color = androidx.compose.ui.graphics.Color.White, size = size)
        
        // Seeded random generator for consistent rendering
        val random = java.util.Random(42)
        
        // Draw simulated random data pixels
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val isTopLeft = row < 7 && col < 7
                val isTopRight = row < 7 && col >= gridSize - 7
                val isBottomLeft = row >= gridSize - 7 && col < 7
                
                if (!isTopLeft && !isTopRight && !isBottomLeft) {
                    if (random.nextBoolean()) {
                        drawRect(
                            color = color,
                            topLeft = androidx.compose.ui.geometry.Offset(col * cellSize, row * cellSize),
                            size = androidx.compose.ui.geometry.Size(cellSize, cellSize)
                        )
                    }
                }
            }
        }
        
        // Helper to draw standard QR finder pattern (7x7 pixels)
        fun drawFinderPattern(startCol: Int, startRow: Int) {
            // Outer block
            drawRect(
                color = color,
                topLeft = androidx.compose.ui.geometry.Offset(startCol * cellSize, startRow * cellSize),
                size = androidx.compose.ui.geometry.Size(cellSize * 7, cellSize * 7)
            )
            // Inner hollow white space
            drawRect(
                color = androidx.compose.ui.graphics.Color.White,
                topLeft = androidx.compose.ui.geometry.Offset((startCol + 1) * cellSize, (startRow + 1) * cellSize),
                size = androidx.compose.ui.geometry.Size(cellSize * 5, cellSize * 5)
            )
            // Inner solid block
            drawRect(
                color = color,
                topLeft = androidx.compose.ui.geometry.Offset((startCol + 2) * cellSize, (startRow + 2) * cellSize),
                size = androidx.compose.ui.geometry.Size(cellSize * 3, cellSize * 3)
            )
        }
        
        // Render 3 corner finder patterns
        drawFinderPattern(0, 0)
        drawFinderPattern(gridSize - 7, 0)
        drawFinderPattern(0, gridSize - 7)
    }
}
