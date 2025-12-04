# Pastelería DAM

Aplicación móvil desarrollada en **Android Studio (Kotlin + Jetpack Compose + Room)**  
para la asignatura **Aplicaciones Móviles en Informática (DSY1105)**.

---

## Integrantes
- David Arana 


**Docente:** IGNACIO ANDRES CUTURRUFO GONZALEZ 
**Institución:** DuocUC

---

Descripción General

Aplicación móvil desarrollada en Android Kotlin + Jetpack Compose, con arquitectura MVVM, que consume información desde:
Una API externa publicada en GitHub Pages.

Aplicación Móvil
Pantalla que muestra una lista de publicaciones (Posts) consumidas desde una API externa (GitHub Pages).
Arquitectura MVVM:

PostViewModel maneja estado y lógica de presentación.
PostRepository consume los endpoints remotos.
Manejo de estados en UI con:

Loading
Success(posts)
Error(mensaje)
Uso de Retrofit + Coroutines para llamadas HTTP asíncronas.
Interfaz moderna con Jetpack Compose.

Backend 
Respuestas estructuradas en JSON.
Compatible con consumo desde la app móvil.

API Externa (GitHub Pages)

Base URL:

https://donchato.github.io/pasteleria-api/

Endpoint consumido:
GET posts.json

Código fuente relacionado:

@GET("posts.json")
suspend fun getPosts(): List<Post>

Pasos para Ejecutar el Proyecto
Ejecutar APP Móvil

Clonar el repositorio:

git clone https://github.com/tu-repo/pasteleria-milsabores.git


Abrir el proyecto en Android Studio.
Sincronizar Gradle automáticamente.
Ejecutar en un emulador o dispositivo físico:
Requiere Android 7.0 (API 24) o superior.
Asegurar conexión a Internet (API externa requiere conexión).
