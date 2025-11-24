package com.ud.parcial2componentes.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ud.parcial2componentes.ui.screens.*
import com.ud.parcial2componentes.viewmodel.PlansViewModel

/**
 * Este archivo implementa el sistema completo de navegación de la aplicación usando Jetpack Compose Navigation,
 * definiendo cómo el usuario se desplaza entre las distintas pantallas y cómo se pasan los datos entre ellas.
 * La función AppNavigation crea un NavHost con un NavController y establece la pantalla de inicio como la lista de planes;
 * dentro del NavHost, cada pantalla se registra mediante composable, indicando su ruta y las acciones que permiten
 * navegar a otras vistas. Ahora incluye actualización automática de la lista de planes al regresar de crear uno.
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
        composable(Screen.PlansList.route) { backStackEntry ->
            // CAMBIO A: Crear ViewModel una sola vez para esta pantalla
            val viewModel: PlansViewModel = viewModel()

            // CAMBIO A: Escuchar cuando volvemos a esta pantalla
            val currentBackStackEntry = navController.currentBackStackEntry
            val savedStateHandle = currentBackStackEntry?.savedStateHandle

            // Observar cambio desde CreatePlan
            savedStateHandle?.getLiveData<Boolean>("plan_created")?.observe(backStackEntry) { created ->
                if (created == true) {
                    viewModel.loadPlans()  // Recargar lista
                    savedStateHandle.remove<Boolean>("plan_created")  // Limpiar flag
                }
            }

            PlansListScreen(
                viewModel = viewModel,
                onPlanClick = { planId ->
                    navController.navigate(Screen.PlanDetail.createRoute(planId))
                },
                onCreatePlan = {
                    navController.navigate(Screen.CreatePlan.route)
                }
            )
        }

        // Pantalla: Crear plan
        composable(Screen.CreatePlan.route) {
            CreatePlanScreen(
                onNavigateBack = { navController.popBackStack() },
                onPlanCreated = {
                    // CAMBIO A: Marcar que se creo un plan antes de volver
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("plan_created", true)

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
    object CreatePlan : Screen("create_plan")

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