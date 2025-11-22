package com.ud.parcial2componentes.ui.screens

import android.R.attr.onClick
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ud.parcial2componentes.viewmodel.PlanListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PlanListScreen(
    viewModel: PlanListViewModel = PlanListViewModel(),
    onPlanClick: (String) -> Unit
) {
    val plans by viewModel.plans.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPlans()
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when {
            loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            error != null -> Text(
                text = "Error: $error",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )

            plans.isEmpty() -> Text(
                text = "No hay planes disponibles",
                modifier = Modifier.align(Alignment.Center)
            )

            else -> {
                // Mostrar la cantidad de planes para debug
                Text(
                    text = "Número de planes: ${plans.size}",
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(plans) { plan ->
                        PlanItem(plan.name, plan._id) {
                            onPlanClick(plan._id)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlanItem(name: String, planId: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = "$name (ID: $planId)",
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold
        )
    }
}
