package com.ud.parcial2componentes.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ud.parcial2componentes.data.model.Member

/**
 * Componente reutilizable que muestra información de un [Member].
 *
 * @param member El miembro cuyos datos se van a mostrar.
 * @param modifier [Modifier] opcional para personalizar el layout del componente.
 */
@Composable
fun MemberCard(
    member: Member,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                // Nombre del miembro
                Text(
                    text = member.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                // Aporte mensual del miembro
                Text(
                    text = "Aporte mensual: $${"%,.0f".format(member.contributionPerMonth)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
