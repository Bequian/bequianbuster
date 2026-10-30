-- =========================================
-- Creación de la tabla VIDEOJUEGO
-- =========================================
CREATE TABLE videojuego (
    id                 NUMBER GENERATED ALWAYS AS IDENTITY,
    titulo             VARCHAR2(100)  NOT NULL,
    desarrollador      VARCHAR2(100)  NOT NULL,
    fecha_lanzamiento  VARCHAR2(10)   NOT NULL,
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

-- =========================================
-- Inserción de datos con ID autogenerado
-- =========================================
INSERT INTO videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('The Witcher 3: Wild Hunt', 'CD Projekt Red', '19-05-2019', 'PC', 'RPG',
        'Geralt de Rivia recorre un mundo abierto buscando a Ciri, su hija adoptiva.',
        19990.00, 35, 'img/videojuegos/the_witcher_3.jpg');

INSERT INTO videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('Elden Ring', 'FromSoftware', '25-02-2022', 'PS5', 'Acción RPG',
        'Un mundo abierto lleno de jefes difíciles en las Tierras Intermedias.',
        44990.00, 20, 'img/videojuegos/elden_ring.jpg');

INSERT INTO videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('The Legend of Zelda: Breath of the Wild', 'Nintendo', '03-03-2017', 'Nintendo Switch', 'Aventura',
        'Link despierta tras un largo sueño y explora Hyrule libremente para derrotar a Ganon.',
        49990.00, 15, 'img/videojuegos/zelda_botw.jpg');

INSERT INTO videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('God of War Ragnarök', 'Santa Monica Studio', '09-11-2022', 'PS5', 'Acción',
        'Kratos y Atreus enfrentan a los dioses nórdicos mientras se acerca el fin del mundo.',
        54990.00, 12, 'img/videojuegos/god_of_war_ragnarok.jpg');

INSERT INTO videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('Halo Infinite', '343 Industries', '08-12-2021', 'Xbox Series X|S', 'Shooter',
        'El Jefe Maestro vuelve para enfrentar a los Desterrados en el anillo Zeta Halo.',
        29990.00, 18, 'img/videojuegos/halo_infinite.jpg');

INSERT INTO videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('Red Dead Redemption 2', 'Rockstar Games', '26-10-2018', 'PS4', 'Acción y Aventura',
        'Arthur Morgan y la banda de Van der Linde intentan sobrevivir en el ocaso del Lejano Oeste.',
        24990.00, 25, 'img/videojuegos/red_dead_redemption_2.jpg');

INSERT INTO videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('Forza Horizon 5', 'Playground Games', '09-11-2021', 'Xbox One', 'Carreras',
        'Carreras en mundo abierto ambientadas en un México lleno de paisajes variados.',
        34990.00, 10, 'img/videojuegos/forza_horizon_5.jpg');

INSERT INTO videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('Super Mario Odyssey', 'Nintendo', '27-10-2017', 'Nintendo Switch', 'Plataformas',
        'Mario viaja por distintos reinos junto a Cappy para rescatar a la princesa Peach.',
        44990.00, 22, 'img/videojuegos/super_mario_odyssey.jpg');

INSERT INTO videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('Terraria', 'Re-Logic', '16-05-2011', 'PC', 'Sandbox',
        'Exploración, construcción y combate en un mundo 2D generado aleatoriamente.',
        7990.00, 50, 'img/videojuegos/terraria.jpg');

INSERT INTO videojuego (titulo, desarrollador, fecha_lanzamiento, plataforma, genero, descripcion, precio, stock, imagen)
VALUES ('Hollow Knight', 'Team Cherry', '24-02-2017', 'PC', 'Metroidvania',
        'Un pequeño caballero explora el reino en ruinas de Hallownest, habitado por insectos.',
        11990.00, 40, 'img/videojuegos/hollow_knight.jpg');

COMMIT;

-- =========================================
-- Verificación
-- =========================================
SELECT id, titulo, desarrollador,fecha_lanzamiento,
       plataforma, genero, precio, stock, imagen
FROM videojuego
ORDER BY id;