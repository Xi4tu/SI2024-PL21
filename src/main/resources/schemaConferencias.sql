DROP TABLE IF EXISTS Conferencia;
DROP TABLE IF EXISTS Track;
DROP TABLE IF EXISTS Articulo;
DROP TABLE IF EXISTS VersionArticulo;
DROP TABLE IF EXISTS Discusion;
DROP TABLE IF EXISTS Anotacion;
DROP TABLE IF EXISTS Revision;
DROP TABLE IF EXISTS Usuario;
DROP TABLE IF EXISTS Rol;
DROP TABLE IF EXISTS Preferencia;
DROP TABLE IF EXISTS Usuario_Preferencia;
DROP TABLE IF EXISTS Usuario_Discusion;
DROP TABLE IF EXISTS Articulo_Usuario;
DROP TABLE IF EXISTS Usuario_Rol;


CREATE TABLE "Conferencia" (
	"idConferencia"	INTEGER NOT NULL UNIQUE,
	"nombre"	TEXT NOT NULL,
	"deadlineEnvio"	TEXT NOT NULL,
	"deadlineDiscusion" TEXT NOT NULL,
	"deadlineRevision" TEXT NOT NULL,
	PRIMARY KEY("idConferencia" AUTOINCREMENT)
);

CREATE TABLE "Track" (
	"idTrack"	INTEGER NOT NULL UNIQUE,
	"idConferencia"	INTEGER NOT NULL,
	"nombre"	TEXT NOT NULL,
	"palabrasClave"	TEXT NOT NULL,
	PRIMARY KEY("idTrack" AUTOINCREMENT)
);

CREATE TABLE "Articulo" (
	"idArticulo"	INTEGER,
	"idTrack"	INTEGER NOT NULL,
	"titulo"	TEXT NOT NULL UNIQUE,
	"palabrasClave"	TEXT NOT NULL,
	"palabrasClaveTrack"	TEXT NOT NULL,
	"resumen"	TEXT NOT NULL,
	"nombreFichero"	TEXT NOT NULL UNIQUE,
	"fechaEnvio"	TEXT NOT NULL,
	"fechaModificacion"	TEXT,
	"decisionFinal"	TEXT DEFAULT 'Pendiente' CHECK("decisionFinal" IN ("Pendiente", "Aceptado", "Rechazado", "Aceptado con cambios")),
	"valoracionGlobal"	INTEGER DEFAULT NULL,
	PRIMARY KEY("idArticulo" AUTOINCREMENT),
	FOREIGN KEY("idTrack") REFERENCES "Track"("idTrack")
);

CREATE TABLE "VersionArticulo" (
    "idArticulo"    INTEGER NOT NULL,
    "resumen"       TEXT NOT NULL,
    "palabrasClave" TEXT NOT NULL,
    "nombreFichero" TEXT NOT NULL,
    FOREIGN KEY("idArticulo") REFERENCES "Articulo"("idArticulo")
        ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY("idArticulo")
);

CREATE TABLE "Discusion" (
	"idDiscusion"	INTEGER NOT NULL UNIQUE,
	"idArticulo"	INTEGER NOT NULL,
	"isCerrada"	INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY("idDiscusion" AUTOINCREMENT)
);

CREATE TABLE "Anotacion" (
	"idAnotacion"	INTEGER NOT NULL UNIQUE,
	"idDiscusion"	INTEGER NOT NULL,
	"emailUsuario"	INTEGER NOT NULL,
	"comentario"	TEXT NOT NULL,
	"fecha"	TEXT NOT NULL,
	"hora"	TEXT NOT NULL,
	PRIMARY KEY("idAnotacion" AUTOINCREMENT)
);

CREATE TABLE "Revision" (
	"idRevision"	INTEGER,
	"idArticulo"	INTEGER NOT NULL,
	"emailUsuario"	TEXT NOT NULL,
	"comentariosParaAutor"	TEXT DEFAULT NULL,
	"comentariosParaCoordinador"	TEXT DEFAULT NULL,
	"nivelExperto"	INTEGER DEFAULT NULL CHECK("nivelExperto" IN ('Alto', 'Medio', 'Normal', 'Bajo') OR "nivelExperto" IS NULL),
	"decisionRevisor"	TEXT DEFAULT NULL CHECK("decisionRevisor" IN (2, 1, -1, -2) OR "decisionRevisor" IS NULL),
	"fechaRevision"	TEXT DEFAULT NULL,
	FOREIGN KEY("idArticulo") REFERENCES "Articulo"("idArticulo") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY("emailUsuario") REFERENCES "Usuario"("email") ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY("idRevision" AUTOINCREMENT)
);

CREATE TABLE "Usuario" (
	"email"	TEXT NOT NULL,
	"nombre"	TEXT NOT NULL,
	"organizacion"	TEXT NOT NULL,
	"grupoInvestigacion"	TEXT,
	PRIMARY KEY("email")
);

CREATE TABLE "Rol" (
	"idRol"	INTEGER,
	"rol"	TEXT NOT NULL,
	PRIMARY KEY("idRol" AUTOINCREMENT)
);

CREATE TABLE "Preferencia" (
	"idPreferencia"	INTEGER NOT NULL UNIQUE,
	"idArticulo"	INTEGER,
	"decision"	TEXT CHECK("decision" IN ("Lo quiero revisar", "Puedo revisar", "No quiero revisar","Conflicto")),
	FOREIGN KEY("idArticulo") REFERENCES "Articulo"("idArticulo"),
	PRIMARY KEY("idPreferencia")
);


CREATE TABLE "Usuario_Discusion" (
	"emailUsuario"	TEXT NOT NULL,
	"idDiscusion"	INTEGER NOT NULL,
	"mantenerseFirme"	INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY("emailUsuario","idDiscusion")
);

CREATE TABLE "Articulo_Usuario" (
	"idArticulo"	INTEGER NOT NULL,
	"emailUsuario"	TEXT NOT NULL,
	"esEnviador"	INTEGER NOT NULL CHECK("esEnviador" IN (0, 1)),
	FOREIGN KEY("emailUsuario") REFERENCES "Usuario"("email") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY("idArticulo") REFERENCES "Articulo"("idArticulo") ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY("idArticulo","emailUsuario")
);

CREATE TABLE "Usuario_Rol" (
	"emailUsuario"	TEXT NOT NULL,
	"idRol"	INTEGER NOT NULL,
	FOREIGN KEY("idRol") REFERENCES "Rol"("idRol") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY("emailUsuario") REFERENCES "Usuario"("email") ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY("emailUsuario","idRol")
);

CREATE TABLE "Usuario_Preferencia" (
	"emailUsuario"	INTEGER,
	"idPreferencia"	INTEGER,
	PRIMARY KEY("emailUsuario","idPreferencia"),
	FOREIGN KEY("idPreferencia") REFERENCES "Preferencia"("idPreferencia"),
	FOREIGN KEY("emailUsuario") REFERENCES "Usuario"("email")
);