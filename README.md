# Aplicación de Restaurantes

## Descripción
Esta es una aplicación móvil para gestionar restaurantes. Los usuarios pueden visualizar información sobre restaurantes, sus comidas y realizar acciones como agregar, editar o eliminar restaurantes.

## Características
- Visualización de restaurantes con sus comidas y detalles.
- Uso de `RecyclerView` para mostrar una lista interactiva.
- Funcionalidad para agregar, editar y eliminar restaurantes.
- Diálogos personalizados para la inserción y edición de datos.
- Diseño moderno utilizando `CardView` e integración de imágenes, texto y botones.
- **Autenticación y registro de usuarios con Firebase:**
  - Registro con validación por correo electrónico.
  - Inicio de sesión de usuarios registrados.
  - Recuperación de contraseñas mediante Firebase.

## 📂 **Estructura del Proyecto**
El proyecto está organizado en paquetes según la responsabilidad de cada componente:

### **1. Paquete `adapter`**
- **Clase: `AdapterRestaurante`**
  - Se conecta al `RecyclerView` y renderiza cada restaurante en un `CardView`.
  - Escucha eventos como clics en botones para acciones específicas (eliminar o editar).
- **Clase: `ViewHRestaurante`**
  - Clase encargada de renderizar cada restaurante en un item del `RecyclerView`.
  - Contiene lógica para manejar los clics en los botones de editar y eliminar.
  - Utiliza Glide para cargar las imágenes de los restaurantes de manera eficiente.

### **2. Paquete `controller`**
Gestiona la lógica principal de la app, como inicializar datos y manejar interacciones.
- **Clase: `ControllerRestaurante`**
  - Inicializa los datos de los restaurantes.
  - Proporciona un adaptador al `RecyclerView`.
  - Implementa la funcionalidad para agregar, editar y eliminar restaurantes.

### **3. Paquete `dao`**
Almacena y gestiona los datos locales de los restaurantes.
- **Clase: `DaoRestaurantes`**
  - Contiene una lista inicial de restaurantes, simulando una fuente de datos estática.
  - Proporciona métodos para acceder y manipular estos datos.

### **4. Paquete `dialogues`**
Contiene los diálogos personalizados usados en la app.
- **Clase: `DialogEliminarRestaurante`**
  - Se utiliza para confirmar la eliminación de un restaurante.
- **Clase: `DialogRestaurante`**
  - Muestra formularios para agregar o editar un restaurante.
  - Recibe datos iniciales y devuelve los cambios al controlador.

### **5. Paquete `interfaces`**
Define interfaces que estandarizan la comunicación entre componentes.
- **Clase: `RestauranteInterface`**
  - Define los métodos básicos para gestionar los datos de restaurantes.

### **6. Paquete `models`**
Define las clases modelo (POJOs) que representan los datos.
- **Clase: `Restaurante`**
  - Propiedades:
    - `nombre`: Nombre del restaurante.
    - `comida`: Tipo de comida.
    - `tiempoEntrega`: Tiempo estimado de entrega.
    - `cantidad`: Cantidad de pedidos.
    - `precio`: Precio del pedido.
    - `imagen`: URL o recurso de la imagen del restaurante.

### **7. Paquete `objects_models`**
Contiene objetos de configuración o almacenamiento temporal.
- **Clase: `Repository`**
  - Gestiona la lista inicial de restaurantes como una fuente de datos para pruebas.

### **8. Actividades**
La app tiene las siguientes actividades principales:
- **Clase: `LoginActivity`**
  - Permite al usuario ingresar credenciales para acceder a la app.
  - Implementa validaciones básicas para el inicio de sesión.
  - Redirige al usuario a `MainActivity` tras un inicio de sesión exitoso.
  - Guarda la sesión del usuario utilizando `SharedPreferences`.
- **Clase: `RegisterActivity`**
  - Permite registrar un nuevo usuario utilizando Firebase.
  - Incluye validación por correo electrónico.
  - Desloguea al usuario tras el registro y le solicita verificar su correo electrónico antes de iniciar sesión.
- **Clase: `MainActivity`**
  - Contiene el `RecyclerView` que muestra los restaurantes.
  - Inicializa el `ControllerRestaurante` y configura el adaptador del `RecyclerView`.
  - Incluye un botón para cerrar sesión que elimina las preferencias compartidas y redirige al `LoginActivity`.

## **Versión Actual: 1.3**
### Cambios Introducidos:
- **Integración con Firebase para autenticación:**
  - Registro de usuarios con validación por correo electrónico.
  - Inicio de sesión de usuarios registrados.
  - Recuperación de contraseñas olvidadas.
- **Persistencia de sesión:**
  - Implementación de `SharedPreferences` para guardar la sesión del usuario y redirigir automáticamente al `MainActivity` si ya está logueado.
- **Botón de Cerrar Sesión:**
  - Agregado en el `MainActivity` para permitir al usuario desloguearse, eliminando la sesión guardada.

## 🚀 **Versiones del proyecto anteriores**
- **Version1.1:**- Utilizacion del RecyclerView y posibilidad de borrar. Anadiremos nuestras propias clases POJO y Adaptadores.
- **Versión1.2:**- Crud completo con alta, edicion y borrado en memoria y a partir de repositorio. Implementación de los DialogFragment para la inserción y edición de datos.

