package com.ud.parcial2componentes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ud.parcial2componentes.data.model.Member
import com.ud.parcial2componentes.viewmodel.PlanDetailUiState
import com.ud.parcial2componentes.viewmodel.PlanDetailViewModel
import com.ud.parcial2componentes.viewmodel.PaymentsViewModel
import com.ud.parcial2componentes.viewmodel.RegisterPaymentState

/**
 * Pantalla mejorada para registrar un nuevo pago.
 * Permite seleccionar el miembro de una lista desplegable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPaymentScreen(
    planId: String,
    onNavigateBack: () -> Unit,
    onPaymentSuccess: () -> Unit,
    planDetailViewModel: PlanDetailViewModel = viewModel(),
    paymentsViewModel: PaymentsViewModel = viewModel()
) {
    var amount by remember { mutableStateOf("") }
    var selectedMember by remember { mutableStateOf<Member?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val planDetailState by planDetailViewModel.uiState.collectAsState()
    val registerState by paymentsViewModel.registerState.collectAsState()

    // Cargar miembros del plan
    LaunchedEffect(planId) {
        planDetailViewModel.loadPlanDetails(planId)
    }

    // Manejar éxito
    LaunchedEffect(registerState) {
        if (registerState is RegisterPaymentState.Success) {
            onPaymentSuccess()
            paymentsViewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Pago") },
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = planDetailState) {
                is PlanDetailUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is PlanDetailUiState.Success -> {
                    RegisterPaymentContent(
                        members = state.members,
                        selectedMember = selectedMember,
                        onMemberSelected = { selectedMember = it },
                        amount = amount,
                        onAmountChange = { amount = it },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        registerState = registerState,
                        onRegisterClick = {
                            selectedMember?.let { member ->
                                val amountDouble = amount.toDoubleOrNull() ?: 0.0
                                paymentsViewModel.registerPayment(
                                    memberId = member.id,
                                    planId = planId,
                                    amount = amountDouble
                                )
                            }
                        }
                    )
                }
                is PlanDetailUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error cargando miembros",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { planDetailViewModel.loadPlanDetails(planId) }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPaymentContent(
    members: List<Member>,
    selectedMember: Member?,
    onMemberSelected: (Member) -> Unit,
    amount: String,
    onAmountChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    registerState: RegisterPaymentState,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Selecciona el miembro y monto",
            style = MaterialTheme.typography.titleLarge
        )

        // Dropdown de miembros
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { onExpandedChange(!expanded) }
        ) {
            OutlinedTextField(
                value = selectedMember?.name ?: "Selecciona un miembro",
                onValueChange = {},
                readOnly = true,
                label = { Text("Miembro") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Desplegar"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                if (members.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No hay miembros disponibles") },
                        onClick = {},
                        enabled = false
                    )
                } else {
                    members.forEach { member ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = member.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "Aporte: $${"%,.0f".format(member.contributionPerMonth)}/mes",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            onClick = {
                                onMemberSelected(member)
                                onExpandedChange(false)
                            }
                        )
                    }
                }
            }
        }

        // Campo de monto
        OutlinedTextField(
            value = amount,
            onValueChange = onAmountChange,
            label = { Text("Monto del Pago") },
            placeholder = { Text("Ej: 50000") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            prefix = { Text("$") },
            singleLine = true
        )

        // Mostrar información del miembro seleccionado
        selectedMember?.let { member ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Información del Miembro",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Nombre: ${member.name}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Aporte mensual: $${"%,.0f".format(member.contributionPerMonth)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Mostrar error si existe
        if (registerState is RegisterPaymentState.Error) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = registerState.message,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón registrar
        Button(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = registerState !is RegisterPaymentState.Loading &&
                    selectedMember != null &&
                    amount.isNotBlank() &&
                    (amount.toDoubleOrNull() ?: 0.0) > 0
        ) {
            if (registerState is RegisterPaymentState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Registrar Pago", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}