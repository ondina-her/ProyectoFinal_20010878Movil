# To-do-list App

Aplicación móvil desarrollada en Android Studio con Kotlin y Jetpack Compose para gestionar tareas personales, laborales y de negocio. Permite agregar, visualizar, completar y eliminar tareas de forma intuitiva.

## 📱 Características principales

- Agregar tareas con título, descripción y tipo (Trabajo, Casa, Negocio).
- Marcar tareas como completadas o pendientes.
- Eliminar tareas desde la lista o vista de detalles.
- Visualizar estadísticas de tareas completadas y pendientes.
- Interfaz accesible, con colores contrastantes y navegación clara.

## 🚀 Instalación

### Requisitos
- Android Studio instalado (versión compatible con Kotlin 1.9.22 y Compose).
- Dispositivo Android o emulador.

### Clonación del repositorio

Puedes clonar el proyecto:

git clone https://github.com/ondina-her/ProyectoFinal_20010878Movil.git

### Ejecución
Abre el proyecto en Android Studio.

Espera a que Gradle sincronice.

Ejecuta la app en un dispositivo físico o emulador.

### Instalación manual del APK
Descarga el archivo .apk desde el paquete .zip adjunto.

En tu dispositivo Android, habilita “Instalar apps de fuentes desconocidas” si es necesario.

Abre el archivo .apk para instalar la aplicación.

### 🔐 Seguridad
La aplicación no solicita permisos sensibles. Solo gestiona tareas ingresadas por el usuario, sin acceder a contactos, ubicación ni archivos personales.

### 👩‍💻 Autoría
- Ondina Victoria Hernández Jacinto 
- Universidad Galileo
- Técnico en Desarrollo de Software
- Curso: Introducción al desarrollo de aplicaciones móviles
- Docente: Ing. MSc. Francisco Antonio Retana Barco

📂 Estructura del proyecto
MainActivity.kt: punto de entrada de la app.

AddTaskScreen.kt: pantalla para agregar tareas.

TaskListScreen.kt: lista de tareas.

TaskDetailScreen.kt: vista de detalles.

AppDatabase.kt, Task.kt, TaskDao.kt: configuración de Room para persistencia local.

AndroidManifest.xml: configuración de la app.

📌 Notas
El proyecto incluye un archivo .gitignore para mantener el repositorio limpio.

El código contiene advertencias menores (TODO, warnings), pero no afectan la funcionalidad.
