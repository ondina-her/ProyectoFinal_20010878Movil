package com.example.myapplication

// PASO 1: Importa el AlertDialog de Material 3, no el de appcompat
// PASO 2: Importa las funciones que faltaban
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    navController: NavHostController,
    task: Task,
    onDelete: () -> Unit,
    onUpdate: (Task) -> Unit   // ✅ nuevo callback para actualizar
) {
    var showDialog by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(task.estado) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de la Tarea") },
                navigationIcon = {
                    //centrar titulo

                    Spacer(Modifier.width(80.dp))
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172B))
            )
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF0F172B)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            isChecked = checked
                            onUpdate(task.copy(estado = checked)) // ✅ actualiza BD
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF0F172B),
                            uncheckedColor = Color.Gray
                        )
                    )

                    Text(task.titulo, style = MaterialTheme.typography.titleMedium, color = Color.Black)
                }

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(task.descripcion, color = Color.Black)

                    // Tipo con fondo azul y texto azul
                    Text(
                        text = task.tipo,
                        color = Color(0xFF0F172B),
                        modifier = Modifier
                            .padding(4.dp)
                            .background(
                                Color(0xFFE6EBF5),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp) // redondeado
                            )
                            .padding(8.dp)

                    )

                    // Estado con fondo redondeado
                    val estadoColor = if (isChecked) Color(0xFFA3E3D4) else Color(0xFFF9F4AE)
                    val textoColor = if (isChecked) Color(0xFF0E9274) else Color(0xFFCE9A48)

                    Text(
                        text = if (isChecked) "Completada" else "Pendiente",
                        color = textoColor,
                        modifier = Modifier
                            .padding(4.dp)
                            .background(
                                color = estadoColor,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp) // redondeado
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Botón Eliminar con fondo blanco
            OutlinedButton(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFFFE4C4A)
                ),
                border = BorderStroke(1.dp, Color(0xFFFE4C4A))
            ) {
                Text("Eliminar")
            }
        }
    }

    // Diálogo de confirmación
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Seguro que deseas eliminar esta tarea?") },
            confirmButton = {
                OutlinedButton(
                    onClick = {
                        showDialog = false
                        onDelete()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFFE4C4A)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFFE4C4A))
                ) {
                    Text("Sí, eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Gray
                    ),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
