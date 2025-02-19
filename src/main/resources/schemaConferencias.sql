DROP TABLE IF EXISTS ARTICULO;
DROP TABLE IF EXISTS ARTICULO_AUTOR;
DROP TABLE IF EXISTS REVISION;
DROP TABLE IF EXISTS USUARIO;
DROP TABLE IF EXISTS USUARIO_ROL;


CREATE TABLE "ARTICULO" (
	"id"	INTEGER,
	"titulo"	TEXT NOT NULL UNIQUE,
	"palabrasClave"	TEXT NOT NULL,
	"resumen"	TEXT NOT NULL,
	"nombreFichero"	TEXT NOT NULL,
	"fechaEnvio"	TEXT NOT NULL,
	"decisionFinal"	TEXT DEFAULT NULL CHECK("decisionFinal" IN ('ACEPTADO', 'RECHAZADO')),
	"submitter"	TEXT NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT),
	FOREIGN KEY("submitter") REFERENCES "USUARIO"("email")
);

CREATE TABLE "ARTICULO_AUTOR" (
	"email"	TEXT NOT NULL,
	"articulo_id"	INTEGER NOT NULL,
	PRIMARY KEY("email","articulo_id"),
	FOREIGN KEY("email") REFERENCES "USUARIO"("email"),
	FOREIGN KEY("articulo_id") REFERENCES "ARTICULO_AUTOR"("id")
);

CREATE TABLE "REVISION" (
	"id"	INTEGER,
	"articulo_id"	INTEGER NOT NULL,
	"revisor"	TEXT NOT NULL,
	"comentariosAutores"	TEXT DEFAULT NULL,
	"comentariosCoordinadores"	TEXT DEFAULT NULL,
	"nivelExperto"	TEXT NOT NULL CHECK("nivelExperto" IN ('ALTO', 'MEDIO', 'NORMAL', 'BAJO')),
	"decision"	INTEGER NOT NULL CHECK("decision" IN (2, 1, -1, -2)),
	PRIMARY KEY("id" AUTOINCREMENT),
	UNIQUE("articulo_id","revisor"),
	FOREIGN KEY("articulo_id") REFERENCES "ARTICULO"("id"),
	FOREIGN KEY("revisor") REFERENCES "USUARIO"("email")
);

CREATE TABLE "USUARIO" (
	"email"	TEXT,
	"nombre"	TEXT NOT NULL,
	"organizacion"	TEXT,
	"grupoInvestigacion"	TEXT,
	PRIMARY KEY("email")
);

CREATE TABLE "USUARIO_ROL" (
	"email"	TEXT NOT NULL,
	"rol"	TEXT NOT NULL CHECK("rol" IN ('AUTOR', 'REVISOR', 'COORDINADOR')),
	PRIMARY KEY("email","rol"),
	FOREIGN KEY("email") REFERENCES "USUARIO"("email")
);