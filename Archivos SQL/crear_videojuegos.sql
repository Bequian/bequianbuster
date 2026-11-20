-- =====================================================================
-- BequianBuster - Script de base de datos: catálogo de videojuegos
-- Base de datos: Oracle Autonomous Database (OCI)
-- Tabla:         BB_VIDEOJUEGO
-- Usado por:     ms-videojuegos (Semanas 1-5) y ms-catalogo (Semana 4)
--
-- Orden de ejecución: 1) Configuración  2) Tabla  3) Datos  4) Verificación
-- =====================================================================


-- =====================================================================
-- 1. Configuración de la sesión
-- =====================================================================
-- Oracle Autonomous Database ejecuta las operaciones DML en paralelo
-- por defecto. En ese modo, una tabla modificada no puede volver a
-- leerse ni modificarse en la misma transacción hasta el COMMIT
-- (error ORA-12838). Se desactiva solo para esta sesión.
ALTER SESSION DISABLE PARALLEL DML;


-- =====================================================================
-- 2. Creación de la tabla
-- =====================================================================
-- Si necesitas recrear la tabla desde cero, descomenta la línea siguiente.
-- ¡CUIDADO! Elimina la tabla y todos sus datos.
-- DROP TABLE bb_videojuego CASCADE CONSTRAINTS PURGE;

CREATE TABLE bb_videojuego (
    id                 NUMBER GENERATED ALWAYS AS IDENTITY,
    titulo             VARCHAR2(100)  NOT NULL,
    desarrollador      VARCHAR2(100)  NOT NULL,
    fecha_lanzamiento  DATE           NOT NULL,
    plataforma         VARCHAR2(50)   NOT NULL,
    genero             VARCHAR2(50)   NOT NULL,
    descripcion        VARCHAR2(500),
    precio             NUMBER(12,2)   NOT NULL,
    stock              NUMBER(10,0)   NOT NULL,
    imagen             VARCHAR2(255),

    CONSTRAINT pk_videojuego PRIMARY KEY (id),
    CONSTRAINT ck_videojuego_plataforma CHECK (
        plataforma IN ('PC', 'PS4', 'PS5', 'Xbox One', 'Xbox Series X|S', 'Nintendo Switch')
    ),
    CONSTRAINT ck_videojuego_precio CHECK (precio >= 0),
    CONSTRAINT ck_videojuego_stock  CHECK (stock >= 0)
);

COMMENT ON TABLE  bb_videojuego        IS 'Catálogo de videojuegos de la tienda BequianBuster';
COMMENT ON COLUMN bb_videojuego.imagen IS 'Ruta de la carátula, relativa a la carpeta public del frontend (ej: covers/pc_terraria.jpg)';


-- =====================================================================
-- 3. Datos iniciales (el ID lo genera Oracle)
-- =====================================================================
INSERT INTO bb_videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('The Witcher 3: Wild Hunt', 'CD Projekt Red', TO_DATE('19-05-2015', 'DD-MM-YYYY'), 'PC', 'RPG',
        'Geralt de Rivia recorre un mundo abierto buscando a Ciri, su hija adoptiva.',
        19990, 35, 'covers/pc_tw3_wh.jpg');

INSERT INTO bb_videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('Elden Ring', 'FromSoftware', TO_DATE('25-02-2022', 'DD-MM-YYYY'), 'PS5', 'Acción RPG',
        'Un mundo abierto lleno de jefes difíciles en las Tierras Intermedias.',
        44990, 20, 'covers/ps5_elden_ring.jpg');

INSERT INTO bb_videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('The Legend of Zelda: Breath of the Wild', 'Nintendo', TO_DATE('03-03-2017', 'DD-MM-YYYY'), 'Nintendo Switch', 'Aventura',
        'Link despierta tras un largo sueño y explora Hyrule libremente para derrotar a Ganon.',
        49990, 15, 'covers/switch_zelda_bow.jpg');

INSERT INTO bb_videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('God of War Ragnarök', 'Santa Monica Studio', TO_DATE('09-11-2022', 'DD-MM-YYYY'), 'PS5', 'Acción',
        'Kratos y Atreus enfrentan a los dioses nórdicos mientras se acerca el fin del mundo.',
        54990, 12, 'covers/ps5_gow_ragnarok.jpg');

INSERT INTO bb_videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('Halo Infinite', '343 Industries', TO_DATE('08-12-2021', 'DD-MM-YYYY'), 'Xbox Series X|S', 'Shooter',
        'El Jefe Maestro vuelve para enfrentar a los Desterrados en el anillo Zeta Halo.',
        29990, 18, 'covers/xbox_halo_infinite.jpg');

INSERT INTO bb_videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('Red Dead Redemption 2', 'Rockstar Games', TO_DATE('26-10-2018', 'DD-MM-YYYY'), 'PS4', 'Acción y Aventura',
        'Arthur Morgan y la banda de Van der Linde intentan sobrevivir en el ocaso del Lejano Oeste.',
        24990, 25, 'covers/ps4_rdr2.jpg');

INSERT INTO bb_videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('Forza Horizon 5', 'Playground Games', TO_DATE('09-11-2021', 'DD-MM-YYYY'), 'Xbox One', 'Carreras',
        'Carreras en mundo abierto ambientadas en un México lleno de paisajes variados.',
        34990, 10, 'covers/xbox_forza_horizon_5.jpg');

INSERT INTO bb_videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('Super Mario Odyssey', 'Nintendo', TO_DATE('27-10-2017', 'DD-MM-YYYY'), 'Nintendo Switch', 'Plataformas',
        'Mario viaja por distintos reinos junto a Cappy para rescatar a la princesa Peach.',
        44990, 22, 'covers/switch_sm_odyssey.jpg');

INSERT INTO bb_videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('Terraria', 'Re-Logic', TO_DATE('16-05-2011', 'DD-MM-YYYY'), 'PC', 'Sandbox',
        'Exploración, construcción y combate en un mundo 2D generado aleatoriamente.',
        7990, 50, 'covers/pc_terraria.jpg');

INSERT INTO bb_videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('Hollow Knight', 'Team Cherry', TO_DATE('24-02-2017', 'DD-MM-YYYY'), 'PC', 'Metroidvania',
        'Un pequeño caballero explora el reino en ruinas de Hallownest, habitado por insectos.',
        11990, 40, 'covers/pc_hollow_knight.jpg');

COMMIT;


-- =====================================================================
-- 4. Verificación
-- =====================================================================
SELECT id,
       titulo,
       desarrollador,
       TO_CHAR(fecha_lanzamiento, 'DD-MM-YYYY') AS fecha_lanzamiento,
       plataforma,
       genero,
       precio,
       stock,
       imagen
FROM bb_videojuego
ORDER BY id;

-- Total esperado después de la carga inicial: 10 registros
SELECT COUNT(*) AS total_videojuegos FROM bb_videojuego;