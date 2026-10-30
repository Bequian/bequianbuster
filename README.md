# 🎮 BequianBuster – Sistema de Microservicios

Sistema backend desarrollado con **Spring Boot** para gestionar el catálogo de videojuegos de una tienda y las cuentas de sus usuarios. Está compuesto por **dos microservicios** independientes que se comunican por HTTP y persisten sus datos en una **Oracle Autonomous Database** en Oracle Cloud (OCI).

> Desarrollo Full Stack 3 – Duoc UC.

---

## 📌 Historial de versiones

### v2.0 – Semana 2: Separación en microservicios y seguridad
- El proyecto monolítico se separó en **dos microservicios**: `ms-videojuegos` y `ms-usuarios`.
- Nuevo microservicio **`ms-usuarios`**: CRUD de cuentas, roles `ADMIN` / `CLIENTE` y contraseñas cifradas con **BCrypt**.
- **Spring Security** con autenticación **HTTP Basic** en ambos servicios.
- `ms-videojuegos` valida las credenciales **consultando a `ms-usuarios`** (`GET /auth/perfil`) mediante `RestClient`.
- Autorización por rol: solo `ADMIN` puede crear, editar o eliminar videojuegos; la consulta es pública.
- Se incorporaron **DTOs** (`Request` / `Response`) y validaciones con **Bean Validation**.
- Manejo centralizado de errores con `@RestControllerAdvice` y respuestas JSON uniformes.
- `fechaLanzamiento` migró de `VARCHAR2` a **`DATE`** (`LocalDate` en Java), con formato `dd-MM-yyyy` en la API.
- Tablas renombradas con prefijo `bb_` y scripts SQL agrupados en la carpeta `Archivos SQL`.

### v1.0 – Semana 1: Microservicio CRUD
- Microservicio REST con CRUD de videojuegos usando **Spring Data JPA**.
- Conexión a **Oracle Autonomous Database** mediante wallet (mTLS).
- Credenciales leídas desde variables de entorno.

---

## 🏗️ Arquitectura

```
                 ┌──────────────────────┐
   Postman  ───► │   ms-videojuegos     │  :8080
                 │  (catálogo)          │
                 └──────────┬───────────┘
                            │ GET /auth/perfil (HTTP Basic)
                            ▼
                 ┌──────────────────────┐
   Postman  ───► │   ms-usuarios        │  :8081
                 │  (identidad / roles) │
                 └──────────┬───────────┘
                            │
                            ▼
              ┌───────────────────────────────┐
              │ Oracle Autonomous Database    │
              │  bb_videojuego · bb_usuarios  │
              └───────────────────────────────┘
```

Cada microservicio es dueño de su propia tabla. `ms-videojuegos` **no consulta** la tabla de usuarios: le pide a `ms-usuarios` que valide las credenciales y le devuelva el rol.

### Flujo de autenticación

1. El cliente envía la petición a `ms-videojuegos` con `Authorization: Basic ...`.
2. `RemoteUserProvider` reenvía esas credenciales a `ms-usuarios` → `GET /auth/perfil`.
3. `ms-usuarios` valida contra Oracle (hash BCrypt) y responde `{ "user": "...", "rol": "..." }`.
4. `ms-videojuegos` crea la autenticación con la autoridad `ROLE_<rol>` y Spring Security decide:
   - Credenciales inválidas o ausentes → **401 Unauthorized**
   - Autenticado pero sin rol `ADMIN` → **403 Forbidden**

---

## 🛠️ Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.2 |
| Spring Data JPA / Hibernate | 7.x |
| Spring Security | HTTP Basic + BCrypt |
| Bean Validation | Jakarta Validation |
| Oracle Autonomous Database | 19c (OCI – Santiago) |
| Driver JDBC | ojdbc17 + oraclepki (wallet) |
| Maven | Apache Maven 3.9.16 (wrapper incluido) |
| Postman | Pruebas de endpoints |

---

## 📁 Estructura del repositorio

```
bequianbuster/
├── Archivos SQL/
│   ├── crear_videojuegos.sql      # Tabla bb_videojuego + 10 registros
│   └── crear_usuarios.sql         # Tabla bb_usuarios
├── ms-videojuegos/                # Puerto 8080
│   └── src/main/java/com/example/ms_videojuegos
│       ├── controller/            # VideojuegoController
│       ├── dto/                   # VideojuegoRequest / VideojuegoResponse
│       ├── error/                 # ErrorHandler, ErrorResponse, ResourceNotFoundException
│       ├── model/                 # Entidad Videojuego
│       ├── repository/            # VideojuegoRepository
│       ├── security/              # SecurityConfig, RemoteUserProvider, RemoteProfile
│       └── service/               # VideojuegoService
└── ms-usuarios/                   # Puerto 8081
    └── src/main/java/com/example/ms_usuarios
        ├── config/                # InitUsers (carga inicial de cuentas)
        ├── controller/            # AuthController, UserController, EstadoController
        ├── dto/                   # UserCreateRequest, UserUpdateRequest, UserResponse, ProfileResponse
        ├── error/                 # ErrorHandler, ErrorResponse, BussinessException
        ├── model/                 # Entidad User, enum Rol
        ├── repository/            # UserRepository
        ├── security/              # SecurityConfig, UserDetailService
        └── service/               # UserService
```

Arquitectura en capas en cada servicio: **Controller → Service → Repository → Base de datos**.

---

## 🎮 ms-videojuegos (puerto 8080)

### Entidad `Videojuego` – tabla `BB_VIDEOJUEGO`

| Atributo | Tipo Java | Columna Oracle | Restricciones |
|---|---|---|---|
| id | Long | NUMBER (IDENTITY) | PK, autogenerado |
| titulo | String | VARCHAR2(100) | Obligatorio |
| desarrollador | String | VARCHAR2(100) | Obligatorio |
| fechaLanzamiento | LocalDate | DATE | Obligatorio, formato `dd-MM-yyyy` |
| plataforma | String | VARCHAR2(50) | Obligatorio, valores permitidos* |
| genero | String | VARCHAR2(50) | Obligatorio |
| descripcion | String | VARCHAR2(500) | Obligatorio en la API |
| precio | BigDecimal | NUMBER(12,2) | Obligatorio, > 0 |
| stock | Integer | NUMBER(10) | Obligatorio, >= 0 |
| imagen | String | VARCHAR2(255) | Opcional |

\* Plataformas permitidas: `PC`, `PS4`, `PS5`, `Xbox One`, `Xbox Series X|S`, `Nintendo Switch`.

### Endpoints

| Método | Ruta | Acceso | Respuestas |
|---|---|---|---|
| GET | `/videojuego` | Público | 200 |
| GET | `/videojuego/{id}` | Público | 200 / 404 |
| POST | `/videojuego` | `ADMIN` | 201 / 400 / 401 / 403 |
| PUT | `/videojuego/{id}` | `ADMIN` | 200 / 400 / 401 / 403 / 404 |
| DELETE | `/videojuego/{id}` | `ADMIN` | 204 / 401 / 403 / 404 |

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

## 👤 ms-usuarios (puerto 8081)

### Entidad `User` – tabla `BB_USUARIOS`

| Atributo | Tipo Java | Columna Oracle | Restricciones |
|---|---|---|---|
| id | Long | NUMBER(19) (IDENTITY) | PK, autogenerado |
| username | String | VARCHAR2(50) | Obligatorio, único |
| email | String | VARCHAR2(120) | Obligatorio, único, formato email |
| passHash | String | VARCHAR2(100) | Hash BCrypt (nunca se devuelve en el JSON) |
| rol | Rol (enum) | VARCHAR2(20) | `ADMIN` o `CLIENTE` |

### Endpoints

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/estado` | Público | Verifica que el servicio esté activo |
| GET | `/auth/perfil` | Autenticado | Devuelve usuario y rol (usado por `ms-videojuegos`) |
| POST | `/auth/login` | Autenticado | Comprueba credenciales |
| GET | `/usuarios` | `ADMIN` | Lista las cuentas |
| GET | `/usuarios/{id}` | `ADMIN` | Obtiene una cuenta |
| POST | `/usuarios` | `ADMIN` | Crea una cuenta (201 / 409 si ya existe) |
| PUT | `/usuarios/{id}` | `ADMIN` | Actualiza email, contraseña o rol |
| DELETE | `/usuarios/{id}` | `ADMIN` | Elimina una cuenta (204) |

**Reglas de negocio:** no se permite duplicar username o email, ni quitar o eliminar al último `ADMIN` (responde **409 Conflict**).

### Ejemplo de body (POST `/usuarios`)

```json
{
  "username": "cliente3",
  "email": "cliente3@duocplay.test",
  "password": "{{nuevaPassword}}", //Utiliza variable de Entorno en Postman 
  "rol": "CLIENTE"
}
```

### Carga inicial de cuentas

Si la tabla está vacía, `InitUsers` crea tres cuentas de demostración (`admin`, `cliente1`, `cliente2`). Toma las contraseñas desde variables de entorno (mínimo 8 caracteres) y nunca las escribe en los logs.

---

## ☁️ Configuración

### 1. Wallet de Oracle
- Descomprimir la wallet en una ruta **sin espacios** y **fuera del repositorio** (ej: `C:/wallets/Wallet_MiBD`).
- Ambos microservicios usan la misma wallet y el alias `_tp` del `tnsnames.ora`.

### 2. Variables de entorno

```powershell
# Base de datos (ambos servicios)
setx TNS_ADMIN "C:/wallets/Wallet_MiBD"
setx DB_USER "MI_USUARIO"
setx DB_PASSWORD "MI_CLAVE"

# Carga inicial de cuentas (ms-usuarios)
setx BB_ADMIN_PASS "********"
setx BB_CLIENTE1_PASS "********"
setx BB_CLIENTE2_PASS "********"

# Opcionales (ms-videojuegos) – tienen valor por defecto
setx BB_USERS_URL "http://localhost:8081"
setx BB_CORS_ORIGIN "http://localhost:4200"
```

---

## ▶️ Ejecución

Levantar primero `ms-usuarios`, porque `ms-videojuegos` depende de él para autenticar.

```powershell
cd ms-usuarios
.\mvnw.cmd spring-boot:run
```

```powershell
cd ms-videojuegos
.\mvnw.cmd spring-boot:run
```

También se pueden iniciar desde el **Spring Boot Dashboard** de VSCode.

---

## 🧪 Pruebas de seguridad en Postman

| Caso | Petición | Credenciales | Resultado esperado |
|---|---|---|---|
| Consulta pública | `GET :8080/videojuego` | Ninguna | 200 OK |
| Sin autenticación | `POST :8080/videojuego` | Ninguna | 401 Unauthorized |
| Rol insuficiente | `POST :8080/videojuego` | `cliente1` | 403 Forbidden |
| Rol correcto | `POST :8080/videojuego` | `admin` | 201 Created |
| Validación | `POST :8080/videojuego` con datos inválidos | `admin` | 400 Bad Request |
| Perfil | `GET :8081/auth/perfil` | `cliente1` | 200 OK |

---

## 🗄️ Base de datos

El archivo `crear_videojuegos.sql` contiene:
- La creación de la tabla `bb_videojuego` con sus restricciones (`CHECK` de plataforma, precio y stock).
- 10 registros de ejemplo.
- Una consulta de verificación.

El archivo `crear_usuario.sql` contiene:
- Creación de tabla `bb_usuaris` con restricciones (`CHECK` de rol).

---

## 👤 Autor

**Angelo Benvenuto**
Ingeniería en Desarrollo de Software – Duoc UC