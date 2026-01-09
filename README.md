
# Franchise Management API (franchise-management-api)

Esta es una API REST reactiva desarrollada con **Spring Boot 3**, diseñada para gestionar franquicias, sucursales y productos. La solución está optimizada para ser escalable, utilizando programación no bloqueante y despliegue automatizado mediante contenedores.

## Despliegue y pruebas en la Nube (AWS)
La aplicación se encuentra desplegada y operativa para pruebas inmediatas en:
- **Base URL:** `http://18.216.126.187:8080`
- **Health Check:** `GET http://18.216.126.187:8080/franchises`

---

## Stack Tecnológico
- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3.5.9 (WebFlux)
- **Base de Datos:** PostgreSQL para acceso reactivo vía R2DBC.
- **Contenerización:** Docker & Docker Compose
- **Infraestructura:** AWS EC2 (Amazon Linux 2023)

---

## Despliegue y pruebas en entorno local - Docker

Para ejecutar esta aplicación en su máquina local, asegúrese de tener instalado **Docker Desktop**.

### Pasos para iniciar:

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/danielCarrilloDaza20/franchise-management-api.git]
   cd [NOMBRE_CARPETA_PROYECTO]
2. **Levantar la infraestructura:** Ejecute el siguiente comando en la raíz del proyecto. Este comando compilará el código Java, creará la imagen de la aplicación y levantará una instancia de PostgreSQL automáticamente:
   ```bash
   docker-compose up --build
3. **Acceso:** Una vez que los logs muestren que la aplicación ha iniciado, puede acceder a:

	-   API: `http://localhost:8080`
	-   La base de datos se inicializa automáticamente con el esquema necesario (`schema.sql`).
---
## Guía de Pruebas (Flujo Sugerido)

Debido al uso de UUIDs para garantizar la integridad de los datos, se recomienda seguir este flujo en Postman o cURL:

1.  **Crear una Franquicia:** `POST /franchises`
    
2.  **Listar Franquicias:** `GET /franchises` (Aquí obtendrá el `franchiseId, branchId y productID` para los siguientes pasos).
    
3.  **Agregar Sucursal a una Franquicia:** `POST /franchises/{franchiseId}/branches`
    
4.  **Agregar Producto a una Sucursal:** `POST /franchises/{franchiseId}/branches/{branchId}/products`

5.  **Eliminar un Producto de una Sucursal:** `DELETE /franchises/{franchiseId}/branches/{branchId}/products/{productId}`
    
6.  **Actualizar Stock de un Producto:** `PUT /franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock`

7. **Listar Producto con mayor stock por Sucursal para una Franquicia puntual:** `GET /franchises/{franchiseId}/products/top-stock`

8. **Actualizar nombre de una Franquicia** `PUT franchises/{franchiseId}`

9. **Actualizar nombre de una Sucursal** `PUT /franchises/{franchiseId}/branches/{branchId}`

10. **Actualizar nombre de un Producto** `PUT /franchises/{franchiseId}/branches/{branchId}/products/{productId}`
    

_Nota: Se incluye una colección de Postman en la carpeta `/docs` para facilitar las pruebas._

-----
## Guía de Pruebas con Postman

Se incluye una colección de Postman en la carpeta `/docs` para facilitar la validación de los endpoints. 

### Flujo de pruebas sugerido:
Dado que la API sigue una estructura jerárquica y los endpoints de creación (POST) retornan un estado `201 Created` sin cuerpo, lo cual sigue principios de diseño de comandos, el flujo de prueba recomendado es:

1. **Configuración:** Al importar la colección, asegúrese de que la variable `baseUrl` apunte a la IP de AWS (ya configurada por defecto) o a `http://localhost:8080` si lo ejecuta localmente.
2. **Creación:** Ejecute los endpoints de creación de Franquicia, Sucursal o Producto según desee.
3. **Identificación:** Ejecute el endpoint `GET /franchises`. Este devolverá el árbol completo de datos.
4. **Validación y Gestión:** Copie manualmente los UUIDs generados desde la respuesta del listado y utilícelos en las variables de las URLs para probar los endpoints de:
   - Agregar sucursales a una franquicia específica.
   - Agregar productos a una sucursal específica.
   - Actualizar stock o nombre de productos.
   - Actualizar nombre de Franquicia, Sucursal o Producto
   - Obtener Stock

----------

## Arquitectura y Decisiones de Diseño

-   **Clean Architecture:** Separación clara entre dominio, casos de uso e infraestructura.
    
-   **Programación Reactiva:** Se utilizó el stack reactivo de Spring para manejar una alta concurrencia con un uso eficiente de recursos.
    
-   **Persistencia:** Se implementaron volúmenes de Docker para asegurar que los datos de PostgreSQL no se pierdan al reiniciar los contenedores.
