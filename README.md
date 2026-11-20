# 🎮 BequianBuster – Sistema Full Stack de Videojuegos

Tienda de videojuegos Full Stack compuesta por **dos microservicios Spring Boot** con persistencia en **Oracle Autonomous Database** (OCI), un **frontend Angular** responsivo con Bootstrap y un **arquetipo Maven** propio. Toda la solución se ejecuta en contenedores con **Docker Compose**, usando **Nginx** como servidor del frontend y proxy hacia las APIs.

> Desarrollo Full Stack 3 – Duoc UC.

---

## 📌 Historial de versiones

### v5.0 – Semana 5: Docker y frontend completo (Sumativa 2)
- **Dockerización**: Dockerfile multi-stage para ambos microservicios y para Angular + Nginx; `compose.yaml` con red propia, wallet montada como volumen de solo lectura y credenciales por variables de entorno.
- **Nginx** sirve la SPA y actúa como proxy (`/api/videojuegos`, `/api/usuarios`): el navegador habla con un solo origen.
- **Bootstrap 5** con grid de 12 columnas: diseño adaptable a móvil, tablet y escritorio; identidad visual propia (logo, paleta, tipografía y carátulas).
- Pantallas nuevas: **registro**, **recuperar contraseña** (simulada), **mi perfil**, **carrito**, **checkout** y **gestión de usuarios**.
- **6 reglas de contraseña**, iguales en backend y frontend, con una lista visual que se marca mientras el usuario escribe.
- `ms-usuarios`: registro público como `CLIENTE` (`POST /auth/registro`), consulta (`GET /auth/cuenta`) y edición del perfil propio (`PUT /auth/perfil`).
- `ms-videojuegos`: compra con pago simulado que **descuenta stock en Oracle** de forma transaccional (`POST /videojuego/compra`).
- Patrón **Factory** en los guards por rol (`rolGuard`) y **directiva validadora** reutilizable para contraseñas.
- **Pruebas unitarias** con JUnit 5 y Mockito para las reglas de negocio de ambos microservicios.
- Desarrollo en ramas `feature/s5-docker` y `feature/s5-frontend`, integradas por *pull request*.

### v4.0 – Semana 4: Arquetipo Maven y reconstrucción del microservicio
- Arquetipo Maven propio **`bb-microservicio-archetype`** (versiones 1.0.0 y 1.1.0), creado a partir del proyecto base `bb-base`.
- El arquetipo incluye la infraestructura estándar: conexión a Oracle con wallet, capas, manejo global de errores y el **patrón Builder** en `ErrorResponse`.
- Propiedades propias del arquetipo: `nombreClase` (clase principal) y `puerto`.
- Nuevo microservicio **`ms-catalogo`** generado desde el arquetipo, con el CRUD de videojuegos reconstruido sobre Oracle.
- **Patrón Strategy** para ordenar el catálogo (`?orden=titulo|precio|fecha`).
- Desarrollo en la rama `feature/arquetipo-maven`.

### v3.0 – Semana 3: Frontend Angular con patrones de diseño
- Nuevo proyecto **`frontend-bequianbuster`** en Angular 22, conectado a los microservicios.
- CRUD completo desde la interfaz: listar, buscar, ver detalle, crear, editar y eliminar videojuegos.
- Login contra `ms-usuarios`, sesión con rol y opciones de administración visibles solo para `ADMIN`.
- Patrones aplicados: **MVC**, **Facade**, **Observer** y **Singleton** (ver sección Frontend).
- Interceptor HTTP que agrega las credenciales y un guard que protege las rutas de administración.
- CORS habilitado en ambos microservicios para el origen `http://localhost:4200`.
- Desarrollo en la rama `feature/frontend-angular`, integrada a `main` mediante *pull request*.

### v2.1 – Semana 2: Validaciones de usuarios
- Validación de **correo** con patrón `usuario@dominio.ext` (el `@Email` estándar aceptaba direcciones sin extensión).
- **Política de contraseñas**: entre 8 y 72 caracteres, con al menos una mayúscula, una minúscula y un número.
- **Username** debe comenzar con una letra.
- Reglas centralizadas en la clase `ValidationRules`, compartidas por los DTOs y la carga inicial.
- El enum `Rol` acepta mayúsculas o minúsculas (`cliente` → `CLIENTE`) y responde un mensaje claro ante un rol inexistente.
- `InitUsers` valida las contraseñas de las variables de entorno con la misma política antes de crear las cuentas.
- El login ignora mayúsculas en el username (se normaliza a minúsculas, igual que al registrar).
- Mensajes de validación personalizados en español.

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
                 Navegador
          http://localhost:4200
                    │
   ┌────────────────┴────────────────┐   red Docker: bequianbuster-red
   │  frontend (Nginx :80)           │
   │  · sirve la aplicación Angular  │
   │  · /api/videojuegos/ ──┐        │
   │  · /api/usuarios/   ───┼──┐     │
   └────────────────────────┼──┼─────┘
                            ▼  ▼
         ┌──────────────────┐   ┌──────────────────┐
         │  ms-videojuegos  │──►│   ms-usuarios    │
         │  :8080 catálogo  │   │  :8081 identidad │
         └────────┬─────────┘   └────────┬─────────┘
                  │  GET /auth/perfil     │
                  ▼                       ▼
           ┌─────────────────────────────────────┐
           │  Oracle Autonomous Database (OCI)   │
           │     bb_videojuego · bb_usuarios     │
           └─────────────────────────────────────┘
              (wallet montada en /opt/wallet)
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
| Angular | 22 |
| TypeScript | 6 |
| Node.js / npm | LTS |
| Bootstrap / Bootstrap Icons | 5.3 / 1.13 |
| Docker Desktop / Docker Compose | 4.99 / v5 |
| Nginx | stable-alpine |
| JUnit 5 / Mockito / AssertJ | Pruebas unitarias |

---

## 📁 Estructura del repositorio

```
bequianbuster/
├── compose.yaml                   # Orquestación de los 3 contenedores
├── .env.example                   # Plantilla de variables (el .env real no se sube)
├── arquetipos/
│   ├── bb-base/                       # Proyecto origen del arquetipo
│   └── bb-microservicio-archetype/    # Arquetipo Maven (plantillas)
├── ms-catalogo/                       # Generado desde el arquetipo – puerto 8082
├── Postman/
│   ├── Bequian Buster.postman_collection.json
│   └── Entorno BB.postman_environment.json   # Plantilla sin contraseñas
├── frontend-bequianbuster/        # Angular – puerto 4200
│   ├── Dockerfile                 # Node 24 (build) + Nginx (ejecución)
│   ├── nginx.conf                 # SPA + proxy hacia las APIs
│   ├── public/covers/             # Carátulas de los videojuegos
│   └── src/app
│       ├── components/            # ReglasPassword (reutilizable)
│       ├── guards/                # adminGuard, authGuard, rolGuard (Factory)
│       ├── validators/            # Reglas de contraseña y directiva PasswordSegura
│       ├── models/                # Videojuego, Sesion, ErrorResponse
│       ├── services/              # VideojuegoApi (HTTP), Auth (sesión)
│       ├── facades/               # CatalogoFacade (patrón Facade)
│       ├── interceptors/          # authInterceptor
│       ├── guards/                # adminGuard
│       └── pages/                 # catalogo, detalle, formulario, login
├── Archivos SQL/
│   ├── crear_videojuegos.sql      # Tabla bb_videojuego + 10 registros
│   └── crear_usuarios.sql         # Tabla bb_usuarios
├── ms-videojuegos/                # Puerto 8080
│   ├── Dockerfile                 # Maven (build) + JRE 21 (ejecución)
│   └── src/main/java/com/example/ms_videojuegos
│       ├── controller/            # VideojuegoController
│       ├── dto/                   # VideojuegoRequest / VideojuegoResponse
│       ├── error/                 # ErrorHandler, ErrorResponse, ResourceNotFoundException
│       ├── model/                 # Entidad Videojuego
│       ├── repository/            # VideojuegoRepository
│       ├── security/              # SecurityConfig, RemoteUserProvider, RemoteProfile
│       └── service/               # VideojuegoService
└── ms-usuarios/                   # Puerto 8081
    ├── Dockerfile                 # Maven (build) + JRE 21 (ejecución)
    └── src/main/java/com/example/ms_usuarios
        ├── config/                # InitUsers (carga inicial de cuentas)
        ├── controller/            # AuthController, UserController, EstadoController
        ├── dto/                   # UserCreateRequest, UserUpdateRequest, UserResponse,
        │                          # ProfileResponse, ValidationRules
        ├── error/                 # ErrorHandler, ErrorResponse, BussinessException
        ├── model/                 # Entidad User, enum Rol
        ├── repository/            # UserRepository
        ├── security/              # SecurityConfig, UserDetailService
        └── service/               # UserService
```

Arquitectura en capas en cada servicio: **Controller → Service → Repository → Base de datos**.

---

## 🖥️ Frontend Angular (puerto 4200)

### Equivalencia con el caso oficial

La actividad plantea la gestión de *libros*. El caso se adaptó a videojuegos (autorizado por el docente), manteniendo los atributos solicitados:

| Libro (pauta) | Videojuego (API y frontend) |
|---|---|
| ID | `id` |
| Título | `titulo` |
| Autor | `desarrollador` |
| Año de publicación | `fechaLanzamiento` |
| Género | `genero` |

El modelo incluye además `plataforma`, `descripcion`, `precio`, `stock` e `imagen`.

### Pantallas y operaciones

### Pantallas y operaciones

| Pantalla | Ruta | Operación | Acceso |
|---|---|---|---|
| Catálogo y búsqueda | `/catalogo` | `GET /videojuego` y `/videojuego/buscar` | Público |
| Detalle | `/videojuego/:id` | `GET /videojuego/{id}` | Público |
| Inicio de sesión | `/login` | `POST /auth/login` | Público |
| Registro | `/registro` | `POST /auth/registro` + login automático | Público |
| Recuperar contraseña | `/recuperar` | Simulado (sin servicio de correo) | Público |
| Mi perfil | `/perfil` | `GET /auth/cuenta` y `PUT /auth/perfil` | Autenticado |
| Carrito | `/carrito` | Lista en memoria (señales) | `CLIENTE` |
| Checkout | `/checkout` | `POST /videojuego/compra` (pago simulado) | `CLIENTE` |
| Nuevo / editar videojuego | `/admin/nuevo`, `/admin/editar/:id` | `POST` / `PUT /videojuego` | `ADMIN` |
| Eliminar videojuego | Desde el catálogo | `DELETE /videojuego/{id}` | `ADMIN` |
| Gestión de usuarios | `/admin/usuarios` | `GET`, `PUT` y `DELETE /usuarios` | `ADMIN` |

### Roles

| Rol | Funcionalidades |
|---|---|
| Visitante | Catálogo, búsqueda, detalle, registro y recuperar contraseña |
| `CLIENTE` | Lo anterior + carrito, compra simulada y mi perfil |
| `ADMIN` | Lo anterior (sin carrito) + CRUD de videojuegos y gestión de usuarios (indicadores, filtros, cambio de rol y eliminación) |

### Diseño responsivo (grid de 12 columnas)

| Pantalla | Breakpoint | Catálogo | Navegación |
|---|---|---|---|
| Móvil | < 576 px | 1 columna (`col-12`) | Menú hamburguesa |
| Tablet | ≥ 576 px | 2 columnas (`col-sm-6`) | Menú hamburguesa |
| Laptop | ≥ 992 px | 3 columnas (`col-lg-4`) | Barra completa |
| Escritorio | ≥ 1200 px | 4 columnas (`col-xl-3`) | Barra completa |

La gestión de usuarios muestra una tabla desde 768 px y tarjetas en móvil (`d-none d-md-block` / `d-md-none`).

### Identidad visual

- **Logo** propio en SVG (`public/logo.svg`), también usado como ícono de la pestaña.
- **Paleta** en variables CSS (`--bb-primario` violeta, `--bb-acento` cian, fondo oscuro), definida en `src/styles.css`.
- **Tipografías:** Rajdhani para títulos y marca, Inter para el texto.
- **Carátulas** de los videojuegos en `public/covers/`, con el logo como respaldo si una imagen no carga.

### Validaciones de formularios

Todos los formularios validan con HTML y Angular (`required`, `minlength`, `maxlength`, `pattern`, `min`, `max`), marcan los campos inválidos con Bootstrap (`is-invalid`) y muestran también los errores por campo que devuelve el backend.

**Reglas de contraseña** (registro y cambio de contraseña), las mismas que valida `ms-usuarios`:

1. Entre 8 y 72 caracteres
2. Al menos una letra mayúscula
3. Al menos una letra minúscula
4. Al menos un número
5. Al menos un carácter especial
6. Sin espacios en blanco

### Patrones de diseño aplicados

| Patrón | Dónde | Problema que resuelve |
|---|---|---|
| **MVC** (arquetipo) | `models/` (Modelo), `pages/` (Vista), `facades/` + `services/` (Controlador) | Separa los datos, la presentación y la lógica |
| **Facade** | `facades/catalogo-facade.ts` | Los componentes llaman métodos simples (`cargar`, `buscar`, `guardar`, `eliminar`) sin conocer HTTP, URLs, conversión de fechas ni códigos de error |
| **Observer** | `Observable` de `HttpClient` y señales (`signal`, `computed`) en fachada, sesión y carrito | Las vistas se actualizan solas cuando cambian los datos (por ejemplo, el contador del carrito en la barra) |
| **Singleton** | Servicios con `providedIn: 'root'` (`VideojuegoApi`, `Auth`, `Carrito`, `CatalogoFacade`) | Una única instancia compartida de la sesión, el carrito y el acceso a las APIs |
| **Factory** | `guards/rol-guard.ts` → `rolGuard('ADMIN')`, `rolGuard('CLIENTE')` | Crea guards por rol desde una sola función, en vez de duplicar un guard por cada rol |
| **Directiva validadora** | `validators/password-segura.ts` + `components/reglas-password` | Las reglas de contraseña se definen una vez y se reutilizan en registro y perfil |

### Flujo de una operación

Ejemplo, al crear un videojuego:

1. `Formulario` (vista) llama a `facade.guardar(null, form, ...)`.
2. `CatalogoFacade` convierte la fecha de `yyyy-MM-dd` a `dd-MM-yyyy` y llama a `VideojuegoApi.crear()`.
3. `authInterceptor` agrega `Authorization: Basic ...` y `X-Requested-With`.
4. `ms-videojuegos` valida el rol `ADMIN` con `ms-usuarios`, guarda en Oracle y responde **201**.
5. La fachada recibe la respuesta y la vista navega al detalle del videojuego creado.
   Si hay un error, la fachada lo traduce a un mensaje y lo muestra en el formulario.

### Seguridad en el frontend

- `authInterceptor` envía las credenciales solo a las URLs de los microservicios.
- `authGuard` protege las páginas que requieren sesión, y `rolGuard('ADMIN' | 'CLIENTE')` las de cada rol.
- Ocultar botones y proteger rutas mejora la experiencia, pero **la seguridad real está en el backend**: un `CLIENTE` recibe **403** aunque intente llamar a la API directamente.

## 🧬 Arquetipo Maven `bb-microservicio-archetype`

Plantilla para crear nuevos microservicios BequianBuster con la misma estructura y los mismos lineamientos, sin configurarlos a mano cada vez.

### Ciclo de creación

```
bb-base (Spring Initializr + infraestructura)
   │  archetype:create-from-project
   ▼
bb-microservicio-archetype (plantillas)
   │  clean install
   ▼
Repositorio local ~/.m2
   │  archetype:generate
   ▼
ms-catalogo (nuevo microservicio) → se agrega la lógica de negocio
```

### Qué incluye la plantilla

| Elemento | Lineamiento |
|---|---|
| `pom.xml` | Spring Boot 4.1.2, Java 21, Web, JPA, Validation, `ojdbc17` y `oraclepki` |
| `application.properties` | Oracle con wallet; credenciales y alias desde variables de entorno (`DB_USER`, `DB_PASSWORD`, `TNS_ADMIN`, `DB_SERVICE`); `ddl-auto=validate` |
| Capas | `controller`, `service`, `repository`, `model`, `dto`, `error` (documentadas con `package-info.java`) |
| `error/ErrorResponse` | **Patrón Builder**: respuestas de error con formato único |
| `error/GlobalErrorHandler` | Errores 400, 404, 405, 415 y 500 construidos con el Builder |
| `controller/EstadoController` | `GET /estado` para verificar que el servicio está activo |

### Patrón Builder en el arquetipo

`ErrorResponse` se construye paso a paso con métodos encadenados, en vez de un constructor con cinco parámetros:

```java
ErrorResponse.builder()
    .estado(HttpStatus.NOT_FOUND)
    .mensaje("Videojuego no encontrado con id: 99")
    .ruta("/videojuego/99")
    .build();
```

El builder completa valores por defecto (`momento`, `datos`) y valida que no falten `estado` ni `mensaje`. Como está en la plantilla, **todo microservicio generado responde los errores con el mismo formato JSON**.

### Propiedades del arquetipo

| Propiedad | Uso | Valor por defecto |
|---|---|---|
| `nombreClase` | Nombre de la clase principal: `Catalogo` → `CatalogoApplication` | `Microservicio` |
| `puerto` | `server.port` del proyecto generado | `8090` |

### Versiones

| Versión | Cambios |
|---|---|
| 1.0.0 | Primera versión: infraestructura Oracle, capas, Builder y `/estado` |
| 1.1.0 | Manejo de errores 405 (método no permitido) y 415 (tipo de contenido no soportado), que antes respondían 500 |

### Instalar y usar

```powershell
# Instalar en el repositorio local de Maven
cd arquetipos\bb-microservicio-archetype
..\bb-base\mvnw.cmd clean install

# Generar un microservicio nuevo (desde la raíz del repositorio)
.\arquetipos\bb-base\mvnw.cmd org.apache.maven.plugins:maven-archetype-plugin:3.4.1:generate -B `
  "-DarchetypeGroupId=cl.bequianbuster" `
  "-DarchetypeArtifactId=bb-microservicio-archetype" `
  "-DarchetypeVersion=1.1.0" `
  "-DgroupId=cl.bequianbuster" `
  "-DartifactId=ms-nuevo" `
  "-Dpackage=cl.bequianbuster.nuevo" `
  "-DnombreClase=Nuevo" `
  "-Dpuerto=8083" `
  "-DarchetypeCatalog=local"
```

> Si `create-from-project` falla con *"The specified user settings file does not exist"*, crear un archivo vacío `<settings></settings>` en `%USERPROFILE%\.m2\settings.xml`.

## 📦 ms-catalogo (puerto 8082)

Reconstrucción del microservicio de la Semana 1, **generado con `bb-microservicio-archetype` 1.0.0** (ver el commit *"Generar microservicio ms-catalogo desde arquetipo"*). Usa la misma tabla `BB_VIDEOJUEGO` y no tiene seguridad, igual que el microservicio original.

### Endpoints

| Método | Ruta | Respuestas |
|---|---|---|
| GET | `/videojuego` | 200 |
| GET | `/videojuego?orden=titulo\|precio\|fecha` | 200 / 400 si el orden no existe |
| GET | `/videojuego/{id}` | 200 / 404 |
| POST | `/videojuego` | 201 / 400 |
| PUT | `/videojuego/{id}` | 200 / 400 / 404 |
| DELETE | `/videojuego/{id}` | 204 / 404 |
| GET | `/estado` | 200 (heredado del arquetipo) |

### Patrón Strategy: orden del catálogo

| Clase | Rol |
|---|---|
| `orden/EstrategiaOrden` | Interfaz común: `clave()` y `comparador()` |
| `OrdenPorTitulo`, `OrdenPorPrecio`, `OrdenPorFecha` | Estrategias concretas (`@Component`) |
| `orden/SelectorOrden` | Recibe todas las estrategias por inyección y elige según `?orden=` |

**Problema que resuelve:** evita un `if/else` o `switch` en el servicio que crecería con cada criterio nuevo. Para agregar un orden basta con crear una clase que implemente `EstrategiaOrden`; el servicio y el controlador no cambian.

### Patrones en el microservicio

| Patrón | Origen | Dónde |
|---|---|---|
| Builder | Heredado del arquetipo | `error/ErrorResponse`, usado en `GlobalErrorHandler` |
| Strategy | Propio de ms-catalogo | Paquete `orden` |
| Singleton | Spring | Beans `@Service`, `@Component` y `@RestController` (una instancia por contexto) |

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
| POST | `/videojuego/compra` | `CLIENTE` | 200 / 400 / 401 / 403 / 404 / 409 |

**Compra transaccional:** `POST /videojuego/compra` recibe `{"items":[{"id":1,"cantidad":2}]}` y descuenta el stock de cada producto dentro de una transacción (`@Transactional`). Si un producto no tiene stock suficiente, responde **409** y no se descuenta ninguno.

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
| username | String | VARCHAR2(50) | Obligatorio, único, se guarda en minúsculas |
| email | String | VARCHAR2(120) | Obligatorio, único, se guarda en minúsculas |
| passHash | String | VARCHAR2(100) | Hash BCrypt (nunca se devuelve en el JSON) |
| rol | Rol (enum) | VARCHAR2(20) | `ADMIN` o `CLIENTE` |

### Reglas de validación

Las expresiones regulares están centralizadas en `dto/ValidationRules.java` y se reutilizan en los DTOs y en `InitUsers`.

| Campo | Regla | Ejemplo válido | Ejemplo inválido |
|---|---|---|---|
| username | 3 a 50 caracteres, comienza con letra; admite letras, números, `.`, `-`, `_` | `cliente3` | `1usuario`, `ab` |
| email | Formato `usuario@dominio.ext`, máximo 120 caracteres | `cliente3@duoc.cl` | `cliente3@duoc` |
| password | 8 a 72 caracteres, con mayúscula, minúscula, número y carácter especial, sin espacios | `Clave#2026` | `Clave2026`, `clave #26` |
| rol | `ADMIN` o `CLIENTE` (sin distinguir mayúsculas) | `cliente` | `VENDEDOR` |

> El máximo de 72 caracteres en la contraseña se debe a que **BCrypt solo procesa los primeros 72 bytes**.
> En `PUT /usuarios/{id}` la contraseña es opcional: si se omite, se conserva la actual.

Si un dato no cumple las reglas, la API responde **400 Bad Request** con el detalle de cada campo en `campos`:

```json
{
  "timestamp": "2026-10-30T21:30:00Z",
  "status": 400,
  "mensaje": "Solicitud invalida",
  "ruta": "/usuarios",
  "campos": {
    "email": "El correo debe tener un formato válido (ej: usuario@dominio.cl)",
    "password": "La contraseña debe incluir al menos una mayúscula, una minúscula y un número"
  }
}
```

### Endpoints

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/estado` | Público | Verifica que el servicio esté activo |
| GET | `/auth/perfil` | Autenticado | Devuelve usuario y rol (usado por `ms-videojuegos`) |
| POST | `/auth/login` | Autenticado | Comprueba credenciales |
| GET | `/usuarios` | `ADMIN` | Lista las cuentas |
| GET | `/usuarios/{id}` | `ADMIN` | Obtiene una cuenta |
| POST | `/usuarios` | `ADMIN` | Crea una cuenta (201 / 400 / 409 si ya existe) |
| PUT | `/usuarios/{id}` | `ADMIN` | Actualiza email, contraseña o rol |
| DELETE | `/usuarios/{id}` | `ADMIN` | Elimina una cuenta (204) |
| POST | `/auth/registro` | Público | Registro propio, siempre como `CLIENTE` (201 / 400 / 409) |
| GET | `/auth/cuenta` | Autenticado | Datos de la cuenta propia |
| PUT | `/auth/perfil` | Autenticado | Cambia el correo y, confirmando la contraseña actual, la contraseña |

**Reglas de negocio:** no se permite duplicar username o email, ni quitar o eliminar al último `ADMIN` (responde **409 Conflict**).

### Ejemplo de body (POST `/usuarios`)

```json
{
  "username": "cliente3",
  "email": "cliente3@duocplay.test",
  "password": "{{nuevaPassword}}",
  "rol": "CLIENTE"
}
```

> `{{nuevaPassword}}` es una variable de entorno de Postman, para no dejar contraseñas escritas en la colección.

### Carga inicial de cuentas

Si la tabla está vacía, `InitUsers` crea tres cuentas de demostración (`admin`, `cliente1`, `cliente2`). Toma las contraseñas desde variables de entorno y verifica que cumplan la **misma política** de los DTOs (8 a 72 caracteres, mayúscula, minúscula y número). Si alguna no la cumple, no crea ninguna cuenta y lo informa en el log, sin escribir nunca las contraseñas.

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

# Carga inicial de cuentas (ms-usuarios) – deben cumplir la política de contraseñas
$seg = Read-Host "Clave de demostración CLIENTE1 (8+ caracteres)" -AsSecureString
$env:BB_ADMIN_PASS = [System.Net.NetworkCredential]::new('', $seg).Password
Remove-Variable seg

$seg = Read-Host "Clave de demostración CLIENTE1 (8+ caracteres)" -AsSecureString
$env:BB_CLIENTE1_PASS = [System.Net.NetworkCredential]::new('', $seg).Password
Remove-Variable seg

$seg = Read-Host "Clave de demostración CLIENTE2 (8+ caracteres)" -AsSecureString
$env:BB_CLIENTE2_PASS = [System.Net.NetworkCredential]::new('', $seg).Password
Remove-Variable seg

# Opcionales (ms-videojuegos) – tienen valor por defecto
BB_USERS_URL "http://localhost:8081"
BB_CORS_ORIGIN "http://localhost:4200"
```

---

## ▶️ Ejecución

**Opción recomendada:** Docker Compose (ver la sección 🐳 Ejecución con Docker).

**Desarrollo local**, sin contenedores. Levantar primero `ms-usuarios`, porque `ms-videojuegos` depende de él para autenticar:

```powershell
cd ms-usuarios
.\mvnw.cmd spring-boot:run
```

```powershell
cd ms-videojuegos
.\mvnw.cmd spring-boot:run
```

Luego el frontend (usa `environment.development.ts`, con las URLs de `localhost`):

```powershell
cd frontend-bequianbuster
npm install      # solo la primera vez
ng serve
```

> Durante el desarrollo del frontend se pueden mantener los microservicios en Docker y detener solo el contenedor del frontend (`docker compose stop frontend`), porque `ng serve` y Nginx usan el mismo puerto 4200.

Abrir `http://localhost:4200`.

Microservicio generado desde el arquetipo (independiente, sin autenticación):

```powershell
cd ms-catalogo
.\mvnw.cmd spring-boot:run
```

---

## 🧪 Pruebas en Postman

### Seguridad

| Caso | Petición | Credenciales | Resultado esperado |
|---|---|---|---|
| Consulta pública | `GET :8080/videojuego` | Ninguna | 200 OK |
| Sin autenticación | `POST :8080/videojuego` | Ninguna | 401 Unauthorized |
| Rol insuficiente | `POST :8080/videojuego` | `cliente1` | 403 Forbidden |
| Rol correcto | `POST :8080/videojuego` | `admin` | 201 Created |
| Validación | `POST :8080/videojuego` con datos inválidos | `admin` | 400 Bad Request |
| Perfil | `GET :8081/auth/perfil` | `cliente1` | 200 OK |

### Validaciones de usuarios (`POST :8081/usuarios` con `admin`)

| Caso | Dato enviado | Resultado esperado |
|---|---|---|
| Correo sin extensión | `"email": "test@duoc"` | 400 – formato de correo |
| Contraseña débil | `"password": "12345678"` | 400 – mayúscula, minúscula y número |
| Rol inexistente | `"rol": "VENDEDOR"` | 400 – debe ser ADMIN o CLIENTE |
| Rol en minúsculas | `"rol": "cliente"` | 201 – se guarda como CLIENTE |
| Username con número al inicio | `"username": "1usuario"` | 400 – debe comenzar con letra |
| Username o correo repetido | Datos de una cuenta existente | 409 Conflict |
| Datos válidos | Todos los campos correctos | 201 Created |

---

## 🗄️ Base de datos

El archivo `crear_videojuegos.sql` contiene:
- La creación de la tabla `bb_videojuego` con sus restricciones (`CHECK` de plataforma, precio y stock).
- 10 registros de ejemplo.
- Una consulta de verificación.

El archivo `crear_usuarios.sql` contiene:
- La creación de la tabla `bb_usuarios` con sus restricciones (`UNIQUE` de username y email, `CHECK` de rol).

---

## 👤 Autor

**Angelo Benvenuto**
Ingeniería en Desarrollo de Software – Duoc UC