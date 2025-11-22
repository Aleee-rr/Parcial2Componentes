package com.ud.parcial2componentes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ud.parcial2componentes.ui.screens.*

/**
 * Sistema de navegación de la aplicación.
 * Define todas las rutas y transiciones entre pantallas.
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PlansList.route
    ) {
        // Pantalla principal: Lista de planes
        composable(Screen.PlansList.route) {
            PlansListScreen(
                onPlanClick = { planId ->
                    navController.navigate(Screen.PlanDetail.createRoute(planId))
                },
                onCreatePlan = {  // ← NUEVO
                    navController.navigate(Screen.CreatePlan.route)
                }
            )
        }

        // ← NUEVA PANTALLA: Crear plan
        composable(Screen.CreatePlan.route) {
            CreatePlanScreen(
                onNavigateBack = { navController.popBackStack() },
                onPlanCreated = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de detalle de plan
        composable(
            route = Screen.PlanDetail.route,
            arguments = listOf(
                navArgument("planId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId") ?: return@composable
            PlanDetailScreen(
                planId = planId,
                onNavigateBack = { navController.popBackStack() },
                onRegisterPayment = { planId ->
                    navController.navigate(Screen.RegisterPayment.createRoute(planId))
                }
            )
        }

        // Pantalla de registro de pago
        composable(
            route = Screen.RegisterPayment.route,
            arguments = listOf(
                navArgument("planId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId") ?: return@composable
            RegisterPaymentScreen(
                planId = planId,
                onNavigateBack = { navController.popBackStack() },
                onPaymentSuccess = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de lista de pagos
        composable(
            route = Screen.PaymentsList.route,
            arguments = listOf(
                navArgument("planId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId") ?: return@composable
            PaymentsListScreen(
                planId = planId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Sealed class que define todas las rutas de navegación.
 */
sealed class Screen(val route: String) {
    object PlansList : Screen("plans_list")
    object CreatePlan : Screen("create_plan")  // ← NUEVA RUTA

    object PlanDetail : Screen("plan_detail/{planId}") {
        fun createRoute(planId: String) = "plan_detail/$planId"
    }

    object RegisterPayment : Screen("register_payment/{planId}") {
        fun createRoute(planId: String) = "register_payment/$planId"
    }

    object PaymentsList : Screen("payments_list/{planId}") {
        fun createRoute(planId: String) = "payments_list/$planId"
    }
}