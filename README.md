# 🎮 BequianBuster – Microservicio de Videojuegos

Microservicio REST desarrollado con **Spring Boot** para gestionar el catálogo de videojuegos de una tienda. Implementa operaciones CRUD conectadas a una **Oracle Autonomous Database** en Oracle Cloud (OCI), usando **Spring Data JPA**.

> Formativa Semana 1 – Desarrollo Full Stack 3 (Duoc UC).

---

## 🛠️ Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.2 |
| Spring Data JPA / Hibernate | 7.x |
| Oracle Autonomous Database | 19c (OCI – Santiago) |
| Driver JDBC | ojdbc17 + oraclepki (wallet) |
| Maven | Apache Maven 3.9.16 |
| Postman | Pruebas de endpoints |

---

## 📁 Estructura del proyecto

```
src/main/java/com/example/bequianbuster
├── BequianbusterApplication.java   # Clase principal
├── controller
│   └── VideojuegoController.java   # Endpoints REST
├── model
│   └── Videojuego.java             # Entidad JPA
├── repository
│   └── VideojuegoRepository.java   # Acceso a datos (JpaRepository)
└── service
    └── VideojuegoService.java      # Lógica de negocio

src/main/resources
└── application.properties          # Configuración de la conexión

sql_base.sql                        # Script de creación de tabla y datos iniciales
```

Arquitectura en capas: **Controller → Service → Repository → Base de datos**.

---

## 🧩 Entidad `Videojuego`

| Atributo | Tipo Java | Columna Oracle | Restricciones |
|---|---|---|---|
| id | Long | NUMBER (IDENTITY) | PK, autogenerado |
| titulo | String | VARCHAR2(100) | Obligatorio |
| desarrollador | String | VARCHAR2(100) | Obligatorio |
| fechaLanzamiento | String | VARCHAR2(10) | Obligatorio |
| plataforma | String | VARCHAR2(50) | Obligatorio, valores permitidos* |
| genero | String | VARCHAR2(50) | Obligatorio |
| descripcion | String | VARCHAR2(500) | |
| precio | BigDecimal | NUMBER(12,2) | Obligatorio, >= 0 |
| stock | Integer | NUMBER(10) | Obligatorio, >= 0 |
| imagen | String | VARCHAR2(255) | Ruta de la imagen |

\* Plataformas permitidas: `PC`, `PS4`, `PS5`, `Xbox One`, `Xbox Series X|S`, `Nintendo Switch`.

---

## 🔌 Endpoints

URL base: `http://localhost:8080/videojuego`

| Método | Ruta | Descripción | Respuesta exitosa |
|---|---|---|---|
| GET | `/videojuego` | Lista todos los videojuegos | 200 OK |
| GET | `/videojuego/{id}` | Obtiene un videojuego por su ID | 200 OK / 404 Not Found |
| POST | `/videojuego` | Crea un nuevo videojuego | 201 Created |
| PUT | `/videojuego/{id}` | Actualiza un videojuego existente | 200 OK / 404 Not Found |
| DELETE | `/videojuego/{id}` | Elimina un videojuego por su ID | 204 No Content / 404 Not Found |

### Ejemplo de body (POST / PUT)

```json
{
  "titulo": "The Witcher 2: Assassins of Kings Enhanced Edition",
  "desarrollador": "CD Projekt Red",
  "fechaLanzamiento": "17-04-2012",
  "plataforma": "PC", 
  "genero": "Action RPG",
  "descripcion": "Geralt de Rivia es acusado falsamente del asesinato del rey de Temeria.",
  "precio": 9990,
  "stock": 35,
  "imagen": "img/videojuegos/the_witcher_2_EE.jpg"
}
```

---

## ☁️ Conexión a Oracle Autonomous Database

La conexión usa la **wallet** descargada desde la consola de OCI (conexión mTLS).

### 1. Preparar la wallet
- Descomprimir la wallet en una ruta **sin espacios** y **fuera del proyecto** (ej: `C:/wallets/Wallet_MiBD`).
- Revisar el alias de conexión en `tnsnames.ora` (se usa el alias `_tp`).

### 2. Variables de entorno

```powershell
setx TNS_ADMIN "C:/wallets/Wallet_MiBD"
setx DB_USER "MI_USUARIO"
setx DB_PASSWORD "MI_CLAVE"
```

### 3. `application.properties`

```properties
spring.datasource.url=jdbc:oracle:thin:@ntflny1cififighx_tp?TNS_ADMIN=${TNS_ADMIN}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.datasource.hikari.maximum-pool-size=5
spring.datasource.hikari.connection-timeout=30000

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
```

Las credenciales **no se guardan en el repositorio**: se leen desde variables de entorno.

### 4. Dependencias necesarias (`pom.xml`)

```xml
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc17</artifactId>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>com.oracle.database.security</groupId>
    <artifactId>oraclepki</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

## 🗄️ Base de datos

El archivo `sql_base.sql` contiene:
- La creación de la tabla `VIDEOJUEGO` con sus restricciones (`CHECK` de plataforma, precio y stock).
- 10 registros de ejemplo.
- Una consulta de verificación.


## 👤 Autor

**Angelo Benvenuto**
Ingeniería en Desarrollo de Software – Duoc UC