package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa la base de datos
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "todo-db"
        ).build()
        val taskDao = db.taskDao() // Objeto dao creado

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    // PASO 1: Pasa la instancia 'taskDao' a HomeScreen
                    composable("home") { HomeScreen(navController, taskDao) }
                    composable("agregar_Tarea") { AddTaskScreen(navController, taskDao) }
                    composable("ver_Lista") { TaskListScreen(navController, taskDao) }
                    composable("detalle/{taskId}") { backStackEntry ->
                        val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
                        var selectedTask by remember { mutableStateOf<Task?>(null) }
                        val scope = rememberCoroutineScope()

                        LaunchedEffect(taskId) {
                            if (taskId != null) {
                                scope.launch(Dispatchers.IO) {
                                    selectedTask = taskDao.getAll().find { it.uid == taskId }
                                }
                            }
                        }

                        selectedTask?.let { task ->
                            TaskDetailScreen(
                                navController = navController,
                                task = task,
                                onDelete = {
                                    scope.launch(Dispatchers.IO) {
                                        taskDao.delete(task)
                                        // después de borrar, regresa a la lista
                                        withContext(Dispatchers.Main) {
                                            navController.navigate("ver_Lista") {
                                                popUpTo("ver_Lista") { inclusive = true }
                                            }
                                        }
                                    }
                                },
                                onUpdate = { updatedTask ->
                                    scope.launch(Dispatchers.IO) {
                                        taskDao.update(updatedTask) // guarda el cambio en BD
                                    }
                                }
                            )
                        }

                    }


                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, taskDao: TaskDao) {
    var tasks by remember { mutableStateOf(listOf<Task>()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            tasks = taskDao.getAll()
        }
    }

    val completadas = tasks.count { it.estado }
    val pendientes = tasks.count { !it.estado }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inicio") },
                //centrar titulo
                navigationIcon = {
                    Spacer(Modifier.width(150.dp))
                },

                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172B))
            )
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF0F172B)
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 50.dp, vertical = 200.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tarjetas de conteo
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(
                    text = "Tareas completadas: $completadas",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp)) // espacio entre tarjetas

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(
                    text = "Tareas pendientes: $pendientes",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botones
            Button(
                onClick = { navController.navigate("agregar_Tarea") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E9274))
            ) {
                Text("Agregar Tarea")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("ver_Lista") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE4C4A),
                    )
            ) {
                Text("Ver Lista")
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Para el preview, necesitamos un 'TaskDao' falso que no haga nada
    val dummyDao = object : TaskDao {
        override fun getAll(): List<Task> = emptyList()
        override fun insertAll(vararg tasks: Task) {}
        override fun update(task: Task) {
        }

        override fun delete(task: Task) {}
    }
    HomeScreen(navController = rememberNavController(), taskDao = dummyDao)
}

