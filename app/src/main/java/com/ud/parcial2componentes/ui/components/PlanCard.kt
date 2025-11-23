package com.ud.parcial2componentes.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ud.parcial2componentes.data.model.Plan

/**
 * Componente visual que representa un [Plan] de ahorro en forma de card interactiva.
 *
 * Esta card muestra la siguiente información del plan:
 * 1. **Nombre del plan**: resaltado en tipografía grande y negrita.
 * 2. **Motivo**: descripción del objetivo o razón del plan.
 * 3. **Meta financiera**: monto total a ahorrar, resaltado con color primario.
 * 4. **Duración**: número de meses en los que se planea completar la meta.
 *
 * La card es **clickeable**, ejecutando el lambda [onClick] al pulsarla.
 */
@Composable
fun PlanCard(
    plan: Plan,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 1️ Nombre del plan
            Text(
                text = plan.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                // Motivo del plan
                text = "Motivo: ${plan.motive}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(

                //  Meta y Duración del plan
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Meta: $${"%,.0f".format(plan.targetAmount)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${plan.months} meses",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}