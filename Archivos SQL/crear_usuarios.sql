CREATE TABLE bb_usuarios (
    ID              NUMBER(19) GENERATED ALWAYS AS IDENTITY,
    USERNAME        VARCHAR2(50 CHAR) NOT NULL,
    EMAIL           VARCHAR2(120 CHAR) NOT NULL,
    PASS_HASH       VARCHAR2(100 CHAR) NOT NULL,
    ROL             VARCHAR2(20 CHAR) NOT NULL,
    CONSTRAINT PK_DP_USUARIOS PRIMARY KEY (ID),
    CONSTRAINT UQ_DP_USER UNIQUE (USERNAME),
    CONSTRAINT UQ_DP_EMAIL UNIQUE (EMAIL),
    CONSTRAINT CK_DP_ROL CHECK (ROL IN ('ADMIN','CLIENTE'))
);

-- =========================================
-- Registros iniciales
-- =========================================
-- Las 3 cuentas iniciales (admin, cliente1, cliente2) NO se insertan aquí:
-- las crea automáticamente ms-usuarios (clase InitUsers) al arrancar con la
-- tabla vacía, usando contraseñas desde variables de entorno y guardándolas
-- con hash BCrypt. Así ninguna contraseña queda escrita en el repositorio.

-- =========================================
-- Verificación (ejecutar después de iniciar ms-usuarios)
-- =========================================

SELECT id, username, email, rol
FROM bb_usuarios
ORDER BY id;