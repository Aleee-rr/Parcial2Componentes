package com.ud.parcial2componentes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ud.parcial2componentes.viewmodel.CreatePlanState
import com.ud.parcial2componentes.viewmodel.CreatePlanViewModel

/**
 * Pantalla para crear un nuevo plan de ahorro.
 * Permite agregar nombre, motivo, meta, duración y miembros.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlanScreen(
    onNavigateBack: () -> Unit,
    onPlanCreated: () -> Unit,
    viewModel: CreatePlanViewModel = viewModel()
) {
    var planName by remember { mutableStateOf("") }
    var planMotive by remember { mutableStateOf("") }
    var targetAmount by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }

    // Lista de miembros a agregar
    var members by remember { mutableStateOf(listOf<MemberInput>()) }

    // Para agregar nuevo miembro
    var showAddMemberDialog by remember { mutableStateOf(false) }

    val createState by viewModel.createState.collectAsState()

    // Manejar éxito
    LaunchedEffect(createState) {
        if (createState is CreatePlanState.Success) {
            onPlanCreated()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nuevo Plan") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddMemberDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Miembro")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Información del Plan",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // Campos del plan
            item {
                OutlinedTextField(
                    value = planName,
                    onValueChange = { planName = it },
                    label = { Text("Nombre del Plan") },
                    placeholder = { Text("Ej: Vacaciones 2025") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = planMotive,
                    onValueChange = { planMotive = it },
                    label = { Text("Motivo") },
                    placeholder = { Text("Ej: Viaje familiar") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = targetAmount,
                    onValueChange = { targetAmount = it },
                    label = { Text("Meta de Ahorro") },
                    placeholder = { Text("Ej: 5000000") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    prefix = { Text("$") },
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = months,
                    onValueChange = { months = it },
                    label = { Text("Duración (meses)") },
                    placeholder = { Text("Ej: 8") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            // Sección de miembros
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Miembros del Plan (${members.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (members.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = "No hay miembros agregados.\nToca el botón + para agregar.",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                itemsIndexed(members) { index, member ->
                    MemberInputCard(
                        member = member,
                        onDelete = {
                            members = members.filterIndexed { i, _ -> i != index }
                        }
                    )
                }
            }

            // Mostrar error si existe
            item {
                if (createState is CreatePlanState.Error) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = (createState as CreatePlanState.Error).message,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            // Botón crear
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        viewModel.createPlan(
                            name = planName,
                            motive = planMotive,
                            targetAmount = targetAmount.toDoubleOrNull() ?: 0.0,
                            months = months.toIntOrNull() ?: 0,
                            members = members
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = createState !is CreatePlanState.Loading &&
                            planName.isNotBlank() &&
                            planMotive.isNotBlank() &&
                            targetAmount.isNotBlank() &&
                            months.isNotBlank() &&
                            members.isNotEmpty()
                ) {
                    if (createState is CreatePlanState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Crear Plan", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }

    // Dialog para agregar miembro
    if (showAddMemberDialog) {
        AddMemberDialog(
            onDismiss = { showAddMemberDialog = false },
            onAdd = { member ->
                members = members + member
                showAddMemberDialog = false
            }
        )
    }
}

/**
 * Card que muestra un miembro en la lista
 */
@Composable
fun MemberInputCard(
    member: MemberInput,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = member.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Aporte: $${"%,.0f".format(member.contributionPerMonth)}/mes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Dialog para agregar un nuevo miembro
 */
@Composable
fun AddMemberDialog(
    onDismiss: () -> Unit,
    onAdd: (MemberInput) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var contribution by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Miembro") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    placeholder = { Text("Ej: Carlos") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = contribution,
                    onValueChange = { contribution = it },
                    label = { Text("Aporte Mensual") },
                    placeholder = { Text("Ej: 50000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    prefix = { Text("$") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && contribution.isNotBlank()) {
                        onAdd(
                            MemberInput(
                                name = name,
                                contributionPerMonth = contribution.toDoubleOrNull() ?: 0.0
                            )
                        )
                    }
                },
                enabled = name.isNotBlank() && contribution.isNotBlank()
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

/**
 * Data class para representar un miembro en el formulario
 */
data class MemberInput(
    val name: String,
    val contributionPerMonth: Double
)