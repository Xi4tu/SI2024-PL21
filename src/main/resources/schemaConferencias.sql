-- Eliminamos las tablas en el orden apropiado para evitar conflictos de FK:
DROP TABLE IF EXISTS REVISION;
DROP TABLE IF EXISTS ARTICULO_USUARIO;
DROP TABLE IF EXISTS USUARIO_ROL;
DROP TABLE IF EXISTS ROL;
DROP TABLE IF EXISTS ARTICULO;
DROP TABLE IF EXISTS USUARIO;


-- Tabla USUARIO
CREATE TABLE "USUARIO" (
    "email"               TEXT PRIMARY KEY,
    "nombre"              TEXT NOT NULL,
    "organizacion"        TEXT,
    "grupoInvestigacion"  TEXT
);

-- Tabla ROL (según el diagrama, con idRol e identificación del rol)
CREATE TABLE "ROL" (
    "idRol"   INTEGER PRIMARY KEY AUTOINCREMENT,
    "rol"     TEXT NOT NULL
);

-- Tabla USUARIO_ROL (relación N a N entre Usuario y Rol)
CREATE TABLE "USUARIO_ROL" (
    "emailUsuario" TEXT NOT NULL,
    "idRol"        INTEGER NOT NULL,
    PRIMARY KEY("emailUsuario","idRol"),
    FOREIGN KEY("emailUsuario") REFERENCES "USUARIO"("email"),
    FOREIGN KEY("idRol")        REFERENCES "ROL"("idRol")
);

-- Tabla ARTICULO
CREATE TABLE "ARTICULO" (
    "idArticulo"      INTEGER PRIMARY KEY AUTOINCREMENT,
    "titulo"          TEXT NOT NULL UNIQUE,
    "palabrasClave"   TEXT NOT NULL,
    "resumen"         TEXT NOT NULL,
    "nombreFichero"   TEXT NOT NULL,
    "fechaEnvio"      TEXT NOT NULL,
    "decisionFinal"   TEXT CHECK("decisionFinal" IN ('ACEPTADO','RECHAZADO')),
    "valoracionFinal" TEXT
);

-- Tabla ARTICULO_USUARIO (indica qué usuarios participan en un Artículo y cuál de ellos es el enviador)
CREATE TABLE "ARTICULO_USUARIO" (
    "idArticulo"   INTEGER NOT NULL,
    "emailUsuario" TEXT NOT NULL,
    "esEnviador"   INTEGER NOT NULL CHECK("esEnviador" IN (0,1)),
    PRIMARY KEY("idArticulo","emailUsuario"),
    FOREIGN KEY("idArticulo")   REFERENCES "ARTICULO"("idArticulo"),
    FOREIGN KEY("emailUsuario") REFERENCES "USUARIO"("email")
);

-- Tabla REVISION
CREATE TABLE "REVISION" (
    "idRevision"              INTEGER PRIMARY KEY AUTOINCREMENT,
    "idArticulo"              INTEGER NOT NULL,
    "emailUsuario"            TEXT NOT NULL,
    "comentariosParaAutor"    TEXT,
    "comentariosParaCoordinador"  TEXT,
    "nivelExperto"            TEXT NOT NULL CHECK("nivelExperto" IN ('ALTO','MEDIO','NORMAL','BAJO')),
    "decisionRevisor"         TEXT NOT NULL CHECK("decisionRevisor" IN ('ACEPTABLE','MARGINAL','RECHAZAR')),
    "fechaRevision"           TEXT,
    -- Evita que el mismo usuario revise el mismo artículo más de una vez
    UNIQUE("idArticulo","emailUsuario"),
    FOREIGN KEY("idArticulo")   REFERENCES "ARTICULO"("idArticulo"),
    FOREIGN KEY("emailUsuario") REFERENCES "USUARIO"("email")
);