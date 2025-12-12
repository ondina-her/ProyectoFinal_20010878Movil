package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavHostController, taskDao: TaskDao) {
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            tasks = taskDao.getAll()
        }
    }

    var refreshTrigger by remember { mutableStateOf(false) }
    LaunchedEffect(refreshTrigger) {
        scope.launch(Dispatchers.IO) {
            tasks = taskDao.getAll()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tareas") },
                navigationIcon = {
                    //centrar titulo

                    Spacer(Modifier.width(160.dp))
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
        containerColor = Color(0xFF0F172B),
        // ✅ Botón flotante SIEMPRE visible
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(
                onClick = { navController.navigate("agregar_Tarea") },
                containerColor = Color(0xFF0E9274)
            ) {
                Text("+", color = Color.White)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            items(tasks) { task ->
                var isChecked by remember { mutableStateOf(task.estado) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                isChecked = checked
                                scope.launch(Dispatchers.IO) {
                                    taskDao.update(task.copy(estado = checked))
                                    refreshTrigger = !refreshTrigger
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF0F172B),
                                uncheckedColor = Color.Gray
                            )
                        )

                        Text(task.titulo, style = MaterialTheme.typography.titleMedium, color = Color.Black)
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isChecked) "Completada" else "Pendiente",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { navController.navigate("detalle/${task.uid}") },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E9274))
                        ) {
                            Text("Ver Detalles")
                        }

                        OutlinedButton(
                            onClick = { showDialog = true },
                            modifier = Modifier.weight(1f),
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

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Confirmar eliminación") },
                        text = { Text("¿Seguro que deseas eliminar esta tarea?") },
                        confirmButton = {
                            OutlinedButton(
                                onClick = {
                                    showDialog = false
                                    scope.launch(Dispatchers.IO) {
                                        taskDao.delete(task)
                                        val updatedTasks = taskDao.getAll()
                                        withContext(Dispatchers.Main) {
                                            tasks = updatedTasks
                                        }
                                    }
                                },
                                border = BorderStroke(1.dp, Color(0xFFFE4C4A))
                            ) {
                                Text("Sí, eliminar", color = Color(0xFFFE4C4A))
                            }
                        },
                        dismissButton = {
                            OutlinedButton(
                                onClick = { showDialog = false },
                                border = BorderStroke(1.dp, Color.Gray)
                            ) {
                                Text("Cancelar", color = Color.Gray)
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TaskListScreenPreview() {
    // Preview con datos de prueba para que puedas ver el diseño sin ejecutar la app
    val dummyTasks = listOf(
        Task(uid = 1, titulo = "Comprar pan", descripcion = "Ir a la tienda", tipo = "Casa"),
        Task(uid = 2, titulo = "Revisar código", descripcion = "Proyecto Android", tipo = "Trabajo")
    )

    // Para la preview, creamos un 'TaskDao' falso que devuelve los datos de prueba
    TaskListScreen(
        navController = rememberNavController(),
        taskDao = object : TaskDao {
            override fun getAll(): List<Task> = dummyTasks
            override fun insertAll(vararg tasks: Task) {}
            override fun update(task: Task) { /* no-op en preview */ }


            override fun delete(task: Task) {}
        }
    )
}
