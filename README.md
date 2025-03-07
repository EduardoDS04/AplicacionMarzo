# Aplicación de Gestión de Restaurantes

## 📌 Descripción
Esta es una aplicación móvil desarrollada en **Kotlin** para gestionar una lista de restaurantes. Permite agregar, actualizar, eliminar y visualizar restaurantes, además de gestionar comentarios de los usuarios.

La aplicación utiliza **MVVM (Model-View-ViewModel)**, **Hilt** para inyección de dependencias, y **Firebase Authentication** para el sistema de inicio de sesión y registro.

---

## 🚀 Características
- 📋 **Listado de Restaurantes**: Visualización de los restaurantes con sus detalles.
- ➕ **Agregar Restaurantes**: Formulario para agregar nuevos restaurantes.
-  **Editar Restaurantes**: Modificación de la información de los restaurantes.
- ❌ **Eliminar Restaurantes**: Opción para borrar restaurantes con confirmación.
- 💬 **Gestión de Comentarios**: Los usuarios pueden agregar y eliminar comentarios.
- 🔐 **Autenticación de Usuarios**: Inicio de sesión, registro y recuperación de contraseña con **Firebase Authentication**.
-  **Arquitectura MVVM**: Separación clara de responsabilidades.
-  **Inyección de Dependencias**: Uso de **Dagger Hilt** para gestionar dependencias.
---

## 🛠 Tecnologías y Librerías Utilizadas
- **Kotlin** 
- **MVVM** 
- **Dagger Hilt** 🏗
- **Firebase Authentication** 🔑
- **RecyclerView** 
- **Glide** (para cargar imágenes)
- **LiveData y ViewModel** 

---
## 📂 Estructura del Proyecto
Está dividido en las siguientes carpetas:
---

## 📁 **data** (Capa de Datos)
Esta capa es responsable de manejar los datos de la aplicación, ya sea desde una base de datos local, una API o cualquier otra fuente de datos.

- 📁 **datasource**
  - `DaoRestaurantes.kt` → Actúa como la capa de acceso a datos, proporcionando métodos para obtener, agregar, actualizar y eliminar restaurantes.

- 📁 **repository**
  - `RestauranteRepositoryImpl.kt` → Implementa la interfaz del repositorio y usa `DaoRestaurantes` para acceder a los datos de los restaurantes.

---

## 📁 **domain** (Capa de Dominio)
Contiene la lógica de negocio de la aplicación y define los modelos y casos de uso.

- 📁 **models**
  - `Restaurante.kt` → Representa la estructura de un restaurante con atributos como nombre, tipo de comida, tiempo de entrega, precio, etc.
  - `Comment.kt` → Modelo que representa un comentario, con información sobre el usuario, el texto y la fecha del comentario.

- 📁 **repository**
  - `RestauranteRepository.kt` → Define la interfaz del repositorio, estableciendo los métodos necesarios para gestionar los restaurantes.

- 📁 **usecase** (Casos de Uso)
  - `GetRestaurantesUseCase.kt` → Recupera la lista de restaurantes.
  - `AddRestauranteUseCase.kt` → Permite agregar un nuevo restaurante.
  - `UpdateRestauranteUseCase.kt` → Modifica un restaurante existente.
  - `DeleteRestauranteUseCase.kt` → Elimina un restaurante de la lista.

---

## 📁 **ui** (Capa de Presentación)
Aquí se manejan los componentes visuales y la interacción con el usuario.

- 📁 **adapters** (Adaptadores para RecyclerView)
  - `AdapterRestaurante.kt` → Se encarga de manejar la lista de restaurantes en un RecyclerView.
  - `CommentAdapter.kt` → Administra los comentarios en un RecyclerView.
  - `ViewHRestaurante.kt` → ViewHolder que define la visualización de un restaurante en la lista.

- 📁 **dialogs** (Diálogos Emergentes)
  - `DialogRestaurante.kt` → Formulario emergente para agregar o editar un restaurante, además ahora se encarga de capturar imagen a partir de la cámara y dejar en galería.
    Solicita los permisos de cámara en ejecución,Podemos seleccionar una imagen de la galería y contiene un método en el que la imagen en formato bitmap, lo convierta a Base64.
  - `DialogEliminarRestaurante.kt` → Muestra una alerta de confirmación antes de eliminar un restaurante.

- 📁 **viewmodel** (Gestión de Datos en la UI)
  - `RestauranteViewModel.kt` → Maneja los datos de los restaurantes, interactuando con los casos de uso.
  - `CommentViewModel.kt` → Gestiona los comentarios, permitiendo agregar y eliminar.

- 📁 **views.activities** (Pantallas Principales)
  - `LoginActivity.kt` → Pantalla de inicio de sesión con autenticación de Firebase.
  - `RegisterActivity.kt` → Pantalla para registrar nuevos usuarios.
  - `MainActivity.kt` → Pantalla principal donde se muestra la lista de restaurantes y la navegación.

- 📁 **views.fragments** (Fragmentos para la navegación)
  - `FragmentComments.kt` → Muestra los comentarios de los usuarios en un RecyclerView.
  - `FragmentConf.kt` → Fragmento de configuración, donde el usuario puede modificar sus datos.
  - `FragmentPedido.kt` → Fragmento donde se gestionan los pedidos.
  - `FragmentPpal.kt` → Fragmento principal de la aplicación.

---

## 📁 **di** (Inyección de Dependencias con Dagger Hilt)
- `AppModule.kt` → Configura los módulos de inyección de dependencias para el repositorio y otros componentes.

---

## 📁 **aplicación** (Configuración Global)
- `MyApp.kt` → Configuración inicial de la aplicación con **HiltAndroidApp**.

---
## Funcionalidad añadida
## 📁 **Captura de imagen y selección de imagen en galería**
Captura de imágenes desde la cámara.
Selección de imágenes desde la galería.
ImageView para previsualizar la imagen seleccionada.
Método convertBitmapToBase64() para convertir imágenes a Base64 (listo para uso futuro).
Gestión de permisos en tiempo de ejecución.


## 📁 **Configuración del Usuario**
El fragmento de **Configuración (`FragmentConf`)** permite al usuario gestionar sus datos personales y almacenarlos en la app.  

- 📩 **Correo Electrónico:** Se obtiene automáticamente desde Firebase y **no es editable**.  
- 📝 **Nombre y Número de Teléfono:** El usuario puede ingresar y guardar sus datos, los cuales se almacenan en `SharedPreferences`.  
- 🌍 **País:** Nuevo campo agregado donde el usuario puede ingresar su país y almacenarlo de manera persistente.  

Estos datos se **cargan automáticamente** cuando el usuario vuelve a la Configuración y permanecen almacenados aunque cierre la app.

## 📌 **Búsqueda de Restaurantes por Precio**
En la barra de herramientas (`Toolbar`), se ha implementado una opción de **"Buscar por precio"**.  

- 🔎 **Funcionalidad**:  
  - Se muestra un cuadro de diálogo donde el usuario ingresa un rango de precio.  
  - Se filtran los restaurantes cuyo precio sea menor o igual al ingresado.  
  - Se **ordena la lista por precio ascendente** y se actualiza en tiempo real en el `RecyclerView`.

- 🛠 **Implementación en `MainActivity.kt`**:  
  - Se maneja la opción de búsqueda en el `Toolbar`.  
  - Se usa `AlertDialog` con un campo de entrada (`EditText`) para solicitar el precio.  
  - Se actualiza el `RecyclerView` con la nueva lista filtrada.  

## **Versión Actual: 3.1**
Captura de imagen y selección de imagen en galería.

## 🚀 **Versiones del proyecto anteriores**
- **Versión1.1:** Utilización del `RecyclerView` y posibilidad de borrar. Añadimos nuestras propias clases POJO y adaptadores.
- **Versión1.2:** CRUD completo con alta, edición y borrado en memoria y desde un repositorio. Implementación de los `DialogFragment` para la inserción y edición de datos.
- **Versión1.3:** Autenticación con Firebase, gestión de usuarios, y persistencia de sesión.
- **Version1.4:** Adaptación de vuestro proyecto con Navigation Drawer.
- **Versión2.1** Adaptación de vuestro proyecto con mvvm e inyección de dependencias con Hilt y toda la funcionalidad.

