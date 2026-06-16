package com.example.timezonemembership.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.timezonemembership.ui.screen.*
import com.example.timezonemembership.ui.theme.LightBackground
import com.example.timezonemembership.viewmodel.MemberViewModel
import com.example.timezonemembership.viewmodel.TransactionViewModel

object Routes {
    const val SPLASH = "splash"
    const val AUTH = "auth"
    const val ADMIN_DASHBOARD = "adminDashboard"
    const val ADD_MEMBER = "addMember"
    const val MEMBER_DETAIL = "memberDetail/{memberId}"
    const val TOP_UP = "topUp/{memberId}"
    const val PLAY = "play/{memberId}"
    const val REDEEM = "redeem/{memberId}"
    const val HISTORY = "history/{memberId}"

    fun memberDetail(memberId: Int) = "memberDetail/$memberId"
    fun topUp(memberId: Int) = "topUp/$memberId"
    fun play(memberId: Int) = "play/$memberId"
    fun redeem(memberId: Int) = "redeem/$memberId"
    fun history(memberId: Int) = "history/$memberId"
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val memberViewModel: MemberViewModel = viewModel()
    val transactionViewModel: TransactionViewModel = viewModel()

    val currentUser by memberViewModel.currentUser.collectAsState()
    val isCheckingAuth by memberViewModel.isCheckingAuth.collectAsState()

    // Handle auto-login / navigation updates on currentUser state change
    LaunchedEffect(currentUser, isCheckingAuth) {
        if (!isCheckingAuth) {
            val user = currentUser
            if (user != null) {
                if (user.role == "ADMIN") {
                    navController.navigate(Routes.ADMIN_DASHBOARD) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                        popUpTo(Routes.AUTH) { inclusive = true }
                    }
                } else {
                    navController.navigate(Routes.memberDetail(user.id)) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                        popUpTo(Routes.AUTH) { inclusive = true }
                    }
                }
            } else {
                navController.navigate(Routes.AUTH) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        // ==================== SPLASH SCREEN ====================
        composable(Routes.SPLASH) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightBackground),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        // ==================== AUTHENTICATION ====================
        composable(Routes.AUTH) {
            AuthScreen(
                viewModel = memberViewModel,
                onAuthSuccess = { role, id ->
                    val destination = if (role == "ADMIN") {
                        Routes.ADMIN_DASHBOARD
                    } else {
                        Routes.memberDetail(id)
                    }
                    navController.navigate(destination) {
                        popUpTo(Routes.AUTH) { inclusive = true }
                    }
                }
            )
        }

        // ==================== ADMIN DASHBOARD ====================
        composable(Routes.ADMIN_DASHBOARD) {
            HomeScreen(
                viewModel = memberViewModel,
                onAddMember = {
                    navController.navigate(Routes.ADD_MEMBER)
                },
                onMemberClick = { memberId ->
                    navController.navigate(Routes.memberDetail(memberId))
                },
                onLogout = {
                    navController.navigate(Routes.AUTH) {
                        popUpTo(Routes.ADMIN_DASHBOARD) { inclusive = true }
                    }
                }
            )
        }

        // ==================== ADD MEMBER (ADMIN ONLY) ====================
        composable(Routes.ADD_MEMBER) {
            AddMemberScreen(
                viewModel = memberViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ==================== MEMBER DETAIL ====================
        composable(
            route = Routes.MEMBER_DETAIL,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: return@composable
            MemberDetailScreen(
                viewModel = memberViewModel,
                memberId = memberId,
                onNavigateBack = { navController.popBackStack() },
                onTopUp = { navController.navigate(Routes.topUp(memberId)) },
                onPlay = { navController.navigate(Routes.play(memberId)) },
                onRedeem = { navController.navigate(Routes.redeem(memberId)) },
                onHistory = { navController.navigate(Routes.history(memberId)) },
                onLogout = {
                    navController.navigate(Routes.AUTH) {
                        popUpTo(Routes.memberDetail(memberId)) { inclusive = true }
                    }
                }
            )
        }

        // ==================== TOP-UP ====================
        composable(
            route = Routes.TOP_UP,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: return@composable
            TopUpScreen(
                viewModel = memberViewModel,
                memberId = memberId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ==================== PLAY ====================
        composable(
            route = Routes.PLAY,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: return@composable
            PlayScreen(
                viewModel = memberViewModel,
                memberId = memberId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ==================== REDEEM ====================
        composable(
            route = Routes.REDEEM,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: return@composable
            RedeemScreen(
                viewModel = memberViewModel,
                memberId = memberId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ==================== HISTORY ====================
        composable(
            route = Routes.HISTORY,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: return@composable
            HistoryScreen(
                viewModel = transactionViewModel,
                memberId = memberId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
