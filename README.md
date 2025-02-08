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
- **Integración del Navigation Drawer:**
-Navegación entre diferentes secciones de la aplicación: Pedidos, Configuración, Principal y Comentarios.
-Opción para cerrar sesión desde el drawer_menu.
-Diseño personalizado con íconos y colores ajustados.
-**Navegación con nav_graph:**
-Implementación de navegación entre los fragmentos usando Android Navigation Component.
-Fragmentos disponibles: Pedido, Configuración, Principal y Comentarios.

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
- **Clase: `CommentAdapter`**
  - Adaptador para renderizar los comentarios en un `RecyclerView` dentro del fragmento de comentarios.
  - Maneja eventos de eliminación de comentarios.

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
### **5. Paquete `fragment`**
Contiene los fragmentos de navegación de la aplicación.
- **Fragmento: `FragmentComments`**
  - Muestra una lista de comentarios con la posibilidad de eliminarlos.
  - Incluye un diseño interactivo utilizando `RecyclerView`.
- **Otros fragmentos:**
  - `FragmentPedido`, `FragmentConf`, y `FragmentPpal` con funciones específicas según el flujo de la app.

### **6. Paquete `interfaces`**
Define interfaces que estandarizan la comunicación entre componentes.
- **Clase: `RestauranteInterface`**
  - Define los métodos básicos para gestionar los datos de restaurantes.

### **7. Paquete `models`**
Define las clases modelo (POJOs) que representan los datos.
- **Clase: `Restaurante`**
  - Propiedades:
    - `nombre`: Nombre del restaurante.
    - `comida`: Tipo de comida.
    - `tiempoEntrega`: Tiempo estimado de entrega.
    - `cantidad`: Cantidad de pedidos.
    - `precio`: Precio del pedido.
    - `imagen`: URL o recurso de la imagen del restaurante.
- **Clase: `Comment`**
  - Representa un comentario con:
    - `username`: Usuario que realizó el comentario.
    - `commentText`: Texto del comentario.
    - `date`: Fecha del comentario.

### **8. Paquete `objects_models`**
Contiene objetos de configuración o almacenamiento temporal.
- **Clase: `Repository`**
  - Gestiona la lista inicial de restaurantes como una fuente de datos para pruebas.

### **9. Actividades**
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
  - Configura el `Navigation Drawer` con opciones como Pedido, Configuración, Principal, Comentarios y Logout.
  - Gestiona la navegación entre los fragmentos usando `nav_graph`.
  - Incluye el `RecyclerView` principal para mostrar los restaurantes y permite agregar nuevos mediante un FAB.

## **Versión Actual: 1.4**
### Cambios Introducidos:
- **Integración del Navigation Drawer:**
  - Agregado un menú lateral para navegar entre las secciones de la aplicación.
  - Opciones personalizadas con íconos y texto ajustado al diseño.
  - Opción de cerrar sesión desde el menú lateral.
  - Nuevo fragmento que muestra una lista genérica de comentarios interactivos.
- **Navegación con `nav_graph`:**
  - Implementado un `nav_graph` para gestionar la navegación entre fragmentos.
- **Toolbar:**
  - Integración de un toolbar que muestra el título dinámico según el fragmento actual.
  - Opción para buscar por precio desde el menú del toolbar.
  - Opción de cerrar sesión.
  - Opción del carro de compra.

## 🚀 **Versiones del proyecto anteriores**
- **Versión1.1:** Utilización del `RecyclerView` y posibilidad de borrar. Añadimos nuestras propias clases POJO y adaptadores.
- **Versión1.2:** CRUD completo con alta, edición y borrado en memoria y desde un repositorio. Implementación de los `DialogFragment` para la inserción y edición de datos.
- **Versión1.3:** Autenticación con Firebase, gestión de usuarios, y persistencia de sesión.

