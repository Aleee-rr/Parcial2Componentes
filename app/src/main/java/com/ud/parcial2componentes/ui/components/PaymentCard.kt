package com.ud.parcial2componentes.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ud.parcial2componentes.data.model.Payment

/**
 * Componente reutilizable que muestra información de un [Payment].
 *
 * @param payment El pago cuyos datos se van a mostrar.
 * @param modifier [Modifier] opcional para personalizar el layout del componente.
 */
@Composable
fun PaymentCard(
    payment: Payment,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Etiqueta del pago
                Text(
                    text = "Pago registrado",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Fecha del pago (solo fecha, sin hora)
                Text(
                    text = payment.date.take(10),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Monto del pago
            Text(
                text = "$${"%,.0f".format(payment.amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
