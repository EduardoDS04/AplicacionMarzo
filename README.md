# Aplicación de Restaurantes
## Descripción
Esta es una aplicación móvil para gestionar restaurantes. Pueden visualizar información sobre restaurantes y comidas.
## Características
- Visualización de restaurantes con sus comidas y detalles.
- Uso de `RecyclerView` para mostrar una lista interactiva.
- Eliminación de entradas con un botón de borrar.
- Diseño moderno utilizando CardView y Imagen, Texto, Botón.
- 
## **Estructura del Proyecto**
El proyecto está organizado en paquetes según la responsabilidad de cada componente:
### **1. Paquete `adapter`**
Contiene la clase encargada de gestionar cómo se renderizan los datos en el `RecyclerView`.
- **Clase: `AdapterRestaurante`**
  - Se conecta al `RecyclerView` y renderiza cada restaurante en un `CardView`.
  - Escucha eventos como el clic en el botón de eliminar.

### **2. Paquete `controller`**
Gestiona la lógica principal de la app, como inicializar datos y manejar interacciones.
- **Clase: `ControllerRestaurante`**
  - Inicializa los datos de los restaurantes.
  - Proporciona un adaptador al `RecyclerView`.
  - Implementa la funcionalidad para eliminar restaurantes.

### **3. Paquete `dao`**
Almacena y gestiona los datos locales de los restaurantes.
- **Clase: `DaoRestaurantes`**
  - Contiene una lista inicial de restaurantes, simulando una fuente de datos estática.
  - Proporciona métodos para acceder y manipular estos datos.

### **4. Paquete `dialogues`**
Contiene los diálogos personalizados usados en la app.
- **Clase: `DialogEliminarRestaurante`**
  - Podría usarse para confirmar la eliminación de un restaurante.

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
La app tiene dos actividades principales:
- **Clase: `LoginActivity`**
  - Permite al usuario ingresar credenciales para acceder a la app.
  - Implementa validaciones básicas para el inicio de sesión.
  - Redirige al usuario a `MainActivity` tras un inicio de sesión exitoso.
- **Clase: `MainActivity`**
  - Contiene el `RecyclerView` que muestra los restaurantes.
  - Inicializa el `ControllerRestaurante` y configura el adaptador del `RecyclerView`.

### Siguiente versiones sería : 
Versión1.2.- Ccrud completo con alta, edicion y borrado en memoria y a partir de repositorio. Implementación de los DialogFragment para la inserción y edición de datos.
Version1.3.- Anadiremos una autenticacion y creacion de usuario en Firebase. Trataremos el tema del registro con validacion por email, logueo y recuperacion de contrasena.
