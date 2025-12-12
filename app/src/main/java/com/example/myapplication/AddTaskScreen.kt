package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(navController: NavHostController, taskDao: TaskDao) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Trabajo") }
    var expanded by remember { mutableStateOf(false) }

    val options = listOf("Trabajo", "Casa", "Negocios")
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Tarea") },
                navigationIcon = {
                    //centrar titulo

                    Spacer(Modifier.width(100.dp))
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
        snackbarHost = { SnackbarHost(snackbarHostState) }, // Agrega el SnackbarHost
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF0F172B)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(50.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // --- TEXTFIELD DE TÍTULO ---
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // --- TEXTFIELD DE DESCRIPCIÓN ---
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            // Dropdown para el tipo de tarea
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { expanded = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Tipo: $type",
                            color = Color.Gray,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Desplegar",
                            tint = Color.Gray
                        )
                    }
                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            colors = MenuDefaults.itemColors(textColor = Color.Gray),
                            modifier = Modifier.background(color = Color.White),
                            onClick = {
                                type = option
                                expanded = false
                            }
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(100.dp))

            // Botón para guardar
            Button(
                onClick = {
                    if (title.isBlank() || description.isBlank() || type.isBlank()) {
                        // ✅ Validación: no permitir campos vacíos
                        scope.launch {
                            snackbarHostState.showSnackbar("Completa todos los campos antes de guardar")
                        }
                    } else {
                        scope.launch(Dispatchers.IO) {
                            taskDao.insertAll(
                                Task(
                                    titulo = title,
                                    descripcion = description,
                                    tipo = type,
                                    estado = false
                                )
                            )
                        }
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE4C4A))
            ) {
                Text("Guardar")
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AddTaskScreenPreview() {
    // Para preview no tenemos un dao real, así que usamos un dummy
    AddTaskScreen(navController = rememberNavController(), taskDao = object : TaskDao {
        override fun getAll(): List<Task> = emptyList()
        override fun insertAll(vararg tasks: Task) {}
        override fun update(task: Task) {
            TODO("Not yet implemented")
        }

        override fun delete(task: Task) {}
    })
}
