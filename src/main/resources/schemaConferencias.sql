DROP TABLE IF EXISTS Articulo;
DROP TABLE IF EXISTS Articulo_Usuario;
DROP TABLE IF EXISTS Revision;
DROP TABLE IF EXISTS Rol;
DROP TABLE IF EXISTS Usuario;
DROP TABLE IF EXISTS Usuario_Rol;


CREATE TABLE "Articulo" (
	"idArticulo"	INTEGER,
	"titulo"	TEXT NOT NULL UNIQUE,
	"palabrasClave"	TEXT NOT NULL,
	"resumen"	TEXT NOT NULL,
	"nombreFichero"	TEXT NOT NULL UNIQUE,
	"fechaEnvio"	TEXT NOT NULL,
	"decisionFinal"	TEXT DEFAULT 'Pendiente' CHECK("decisionFinal" IN ("Pendiente", "Aceptado", "Rechazado")),
	"valoracionGlobal"	INTEGER DEFAULT NULL,
	PRIMARY KEY("idArticulo" AUTOINCREMENT)
);

CREATE TABLE "Articulo_Usuario" (
	"idArticulo"	INTEGER NOT NULL,
	"emailUsuario"	TEXT NOT NULL,
	"esEnviador"	INTEGER NOT NULL CHECK("esEnviador" IN (0, 1)),
	FOREIGN KEY("emailUsuario") REFERENCES "Usuario"("email") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY("idArticulo") REFERENCES "Articulo"("idArticulo") ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY("idArticulo","emailUsuario")
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

CREATE TABLE "Rol" (
	"idRol"	INTEGER,
	"rol"	TEXT NOT NULL,
	PRIMARY KEY("idRol" AUTOINCREMENT)
);

CREATE TABLE "Usuario" (
	"email"	TEXT NOT NULL,
	"nombre"	TEXT NOT NULL,
	"organizacion"	TEXT NOT NULL,
	"grupoInvestigacion"	TEXT,
	PRIMARY KEY("email")
);

CREATE TABLE "Usuario_Rol" (
	"emailUsuario"	TEXT NOT NULL,
	"idRol"	INTEGER NOT NULL,
	FOREIGN KEY("idRol") REFERENCES "Rol"("idRol") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY("emailUsuario") REFERENCES "Usuario"("email") ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY("emailUsuario","idRol")
);