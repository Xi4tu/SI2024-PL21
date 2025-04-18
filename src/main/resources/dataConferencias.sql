BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Anotacion" (
	"idAnotacion"	INTEGER NOT NULL UNIQUE,
	"idDiscusion"	INTEGER NOT NULL,
	"emailUsuario"	INTEGER NOT NULL,
	"comentario"	TEXT NOT NULL,
	"fecha"	TEXT NOT NULL,
	"hora"	TEXT NOT NULL,
	PRIMARY KEY("idAnotacion" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Articulo" (
	"idArticulo"	INTEGER,
	"idTrack"	INTEGER NOT NULL,
	"titulo"	TEXT NOT NULL UNIQUE,
	"palabrasClave"	TEXT NOT NULL,
	"palabrasClaveTrack"	TEXT NOT NULL,
	"resumen"	TEXT NOT NULL,
	"nombreFichero"	TEXT NOT NULL UNIQUE,
	"fechaEnvio"	TEXT NOT NULL,
	"decisionFinal"	TEXT DEFAULT 'Pendiente' CHECK("decisionFinal" IN ("Pendiente", "Aceptado", "Rechazado", "Aceptado con cambios")),
	"valoracionGlobal"	INTEGER DEFAULT NULL,
	"revisorColaborador"	TEXT NOT NULL DEFAULT 'No asignado',
	PRIMARY KEY("idArticulo" AUTOINCREMENT),
	FOREIGN KEY("idTrack") REFERENCES "Track"("idTrack")
);
CREATE TABLE IF NOT EXISTS "Articulo_Usuario" (
	"idArticulo"	INTEGER NOT NULL,
	"emailUsuario"	TEXT NOT NULL,
	"esEnviador"	INTEGER NOT NULL CHECK("esEnviador" IN (0, 1)),
	PRIMARY KEY("idArticulo","emailUsuario"),
	FOREIGN KEY("emailUsuario") REFERENCES "Usuario"("email") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY("idArticulo") REFERENCES "Articulo"("idArticulo") ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS "Conferencia" (
	"idConferencia"	INTEGER NOT NULL UNIQUE,
	"nombre"	TEXT NOT NULL,
	"deadlineEnvio"	TEXT NOT NULL,
	"deadlineDiscusion"	TEXT NOT NULL,
	"deadlineRevision"	TEXT NOT NULL,
	PRIMARY KEY("idConferencia" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Discusion" (
	"idDiscusion"	INTEGER NOT NULL UNIQUE,
	"idArticulo"	INTEGER NOT NULL,
	PRIMARY KEY("idDiscusion" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Preferencia" (
	"idPreferencia"	INTEGER NOT NULL UNIQUE,
	"idArticulo"	INTEGER,
	"decision"	TEXT CHECK("decision" IN ("Lo quiero revisar", "Puedo revisar", "No quiero revisar", "Conflicto")),
	PRIMARY KEY("idPreferencia"),
	FOREIGN KEY("idArticulo") REFERENCES "Articulo"("idArticulo")
);
CREATE TABLE IF NOT EXISTS "Revision" (
	"idRevision"	INTEGER,
	"idArticulo"	INTEGER NOT NULL,
	"emailUsuario"	TEXT NOT NULL,
	"comentariosParaAutor"	TEXT DEFAULT NULL,
	"comentariosParaCoordinador"	TEXT DEFAULT NULL,
	"nivelExperto"	INTEGER DEFAULT NULL CHECK("nivelExperto" IN ('Alto', 'Medio', 'Normal', 'Bajo') OR "nivelExperto" IS NULL),
	"decisionRevisor"	TEXT DEFAULT NULL CHECK("decisionRevisor" IN (2, 1, -1, -2) OR "decisionRevisor" IS NULL),
	"fechaRevision"	TEXT DEFAULT NULL,
	PRIMARY KEY("idRevision" AUTOINCREMENT),
	FOREIGN KEY("emailUsuario") REFERENCES "Usuario"("email") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY("idArticulo") REFERENCES "Articulo"("idArticulo") ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS "Rol" (
	"idRol"	INTEGER,
	"rol"	TEXT NOT NULL,
	PRIMARY KEY("idRol" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Track" (
	"idTrack"	INTEGER NOT NULL UNIQUE,
	"idConferencia"	INTEGER NOT NULL,
	"nombre"	TEXT NOT NULL,
	"palabrasClave"	TEXT NOT NULL,
	PRIMARY KEY("idTrack" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Usuario" (
	"email"	TEXT NOT NULL,
	"nombre"	TEXT NOT NULL,
	"organizacion"	TEXT NOT NULL,
	"grupoInvestigacion"	TEXT,
	PRIMARY KEY("email")
);
CREATE TABLE IF NOT EXISTS "Usuario_Discusion" (
	"emailUsuario"	TEXT NOT NULL,
	"idDiscusion"	INTEGER NOT NULL,
	PRIMARY KEY("emailUsuario","idDiscusion")
);
CREATE TABLE IF NOT EXISTS "Usuario_Preferencia" (
	"emailUsuario"	INTEGER,
	"idPreferencia"	INTEGER,
	PRIMARY KEY("emailUsuario","idPreferencia"),
	FOREIGN KEY("emailUsuario") REFERENCES "Usuario"("email"),
	FOREIGN KEY("idPreferencia") REFERENCES "Preferencia"("idPreferencia")
);
CREATE TABLE IF NOT EXISTS "Usuario_Rol" (
	"emailUsuario"	TEXT NOT NULL,
	"idRol"	INTEGER NOT NULL,
	PRIMARY KEY("emailUsuario","idRol"),
	FOREIGN KEY("emailUsuario") REFERENCES "Usuario"("email") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY("idRol") REFERENCES "Rol"("idRol") ON DELETE CASCADE ON UPDATE CASCADE
);
INSERT INTO "Anotacion" VALUES (1,1,'maria.lopez@ejemplo.com','Creo que habría que profundizar más en la introducción.','2025-03-10','10:30');
INSERT INTO "Anotacion" VALUES (2,1,'andres.gomez@ejemplo.com','Estoy de acuerdo, añadiré referencias adicionales.','2025-03-10','10:45');
INSERT INTO "Anotacion" VALUES (3,2,'andres.gomez@ejemplo.com','La sección de resultados necesita más ejemplos.','2025-03-12','09:15');
INSERT INTO "Anotacion" VALUES (4,2,'maria.lopez@ejemplo.com','Concuerdo contigo acerca de los ejemplos.','2025-03-12','09:20');
INSERT INTO "Anotacion" VALUES (5,2,'carlos.sanchez@ejemplo.com','He revisado esa sección, puedo añadir ejemplos de proyectos reales.','2025-03-12','09:25');
INSERT INTO "Articulo" VALUES (1,1,'Avances en IA','Inteligentes','IA','Este artículo trata sobre avances en inteligencia artificial.','avances_ia.pdf','2025-02-01','Aceptado',1,'No asignado');
INSERT INTO "Articulo" VALUES (2,2,'Innovación en Energías','Fabrica,Progreso','Renovable','Analiza innovaciones en energías renovables.','innovacion_energias.pdf','2025-02-05','Rechazado',-1,'No asignado');
INSERT INTO "Articulo" VALUES (3,3,'Nuevas Técnicas de Programación','Futuro,Desempleo,PHP','Programación,Software','Explora técnicas modernas en desarrollo de software.','nuevas_tecnicas_programacion.pdf','2025-01-20','Aceptado',1,'No asignado');
INSERT INTO "Articulo" VALUES (4,4,'Impacto Ambiental','Desarrollo,Naturaleza','Medio Ambiente','Discusión sobre impacto ambiental de la tecnología.','impacto_ambiental.pdf','2025-02-10','Rechazado',0,'No asignado');
INSERT INTO "Articulo" VALUES (5,1,'Futuro de la Robótica','Pilas,Enchufes,Luces','Robótica,Tecnología','Examina el futuro de la robótica.','futuro_robotica.pdf','2025-02-15','Pendiente',NULL,'No asignado');
INSERT INTO "Articulo" VALUES (6,2,'Estrategias de Marketing','Emprender,Decision,Publicidad','Marketing,Ventas','Revisión de estrategias de marketing digital.','estrategias_marketing.pdf','2025-02-18','Pendiente',NULL,'No asignado');
INSERT INTO "Articulo" VALUES (7,3,'Tendencias en Ciberseguridad','Hackers,Programacion','Ciberseguridad','Análisis de tendencias en ciberseguridad.','tendencias_ciberseguridad.pdf','2025-02-20','Pendiente',NULL,'No asignado');
INSERT INTO "Articulo" VALUES (8,5,'Análisis de Grandes Volúmenes de Datos','Datos,Muchos datos','Big Data,Analytics','Un estudio sobre el procesamiento masivo de datos.','analisis_big_data.pdf','2025-03-20','Pendiente',NULL,'No asignado');
INSERT INTO "Articulo" VALUES (9,6,'Aplicaciones IoT en la Industria','Bolsonaro,Bolsocaro','IoT,Edge','Examina casos de uso IoT en entornos industriales.','iot_industria.pdf','2025-03-22','Pendiente',NULL,'No asignado');
INSERT INTO "Articulo_Usuario" VALUES (1,'juan.perez@ejemplo.com',1);
INSERT INTO "Articulo_Usuario" VALUES (1,'carlos.sanchez@ejemplo.com',0);
INSERT INTO "Articulo_Usuario" VALUES (1,'andres.gomez@ejemplo.com',0);
INSERT INTO "Articulo_Usuario" VALUES (2,'maria.lopez@ejemplo.com',1);
INSERT INTO "Articulo_Usuario" VALUES (2,'laura.martinez@ejemplo.com',0);
INSERT INTO "Articulo_Usuario" VALUES (2,'carlos.sanchez@ejemplo.com',1);
INSERT INTO "Articulo_Usuario" VALUES (3,'juan.perez@ejemplo.com',0);
INSERT INTO "Articulo_Usuario" VALUES (3,'andres.gomez@ejemplo.com',0);
INSERT INTO "Articulo_Usuario" VALUES (4,'laura.martinez@ejemplo.com',1);
INSERT INTO "Articulo_Usuario" VALUES (4,'maria.lopez@ejemplo.com',0);
INSERT INTO "Articulo_Usuario" VALUES (4,'andres.gomez@ejemplo.com',0);
INSERT INTO "Articulo_Usuario" VALUES (5,'andres.gomez@ejemplo.com',1);
INSERT INTO "Articulo_Usuario" VALUES (6,'maria.lopez@ejemplo.com',1);
INSERT INTO "Articulo_Usuario" VALUES (7,'juan.perez@ejemplo.com',1);
INSERT INTO "Articulo_Usuario" VALUES (7,'laura.martinez@ejemplo.com',0);
INSERT INTO "Articulo_Usuario" VALUES (7,'andres.gomez@ejemplo.com',0);
INSERT INTO "Articulo_Usuario" VALUES (8,'carla.torres@ejemplo.com',1);
INSERT INTO "Articulo_Usuario" VALUES (9,'pablo.rodriguez@ejemplo.com',1);
INSERT INTO "Conferencia" VALUES (1,'Conferencia A','2026-03-01','2026-01-10','2026-01-05');
INSERT INTO "Conferencia" VALUES (2,'Conferencia B','2025-03-27','2025-04-10','2025-04-05');
INSERT INTO "Conferencia" VALUES (3,'Conferencia C','2025-01-01','2025-01-10','2025-01-05');
INSERT INTO "Discusion" VALUES (1,1);
INSERT INTO "Discusion" VALUES (2,3);
INSERT INTO "Preferencia" VALUES (1,5,'Lo quiero revisar');
INSERT INTO "Preferencia" VALUES (2,5,'No quiero revisar');
INSERT INTO "Preferencia" VALUES (3,5,'Puedo revisar');
INSERT INTO "Preferencia" VALUES (4,5,'Conflicto');
INSERT INTO "Preferencia" VALUES (5,6,'Lo quiero revisar');
INSERT INTO "Preferencia" VALUES (6,6,'No quiero revisar');
INSERT INTO "Preferencia" VALUES (7,6,'Lo quiero revisar');
INSERT INTO "Preferencia" VALUES (8,6,'Conflicto');
INSERT INTO "Preferencia" VALUES (9,7,'Lo quiero revisar');
INSERT INTO "Preferencia" VALUES (10,7,'Lo quiero revisar');
INSERT INTO "Preferencia" VALUES (11,7,'Puedo revisar');
INSERT INTO "Preferencia" VALUES (12,7,'Lo quiero revisar');
INSERT INTO "Revision" VALUES (1,1,'maria.lopez@ejemplo.com','Buen trabajo, se destaca la metodología.','Revisión adecuada para coordinación.','Alto','2','2025-02-02');
INSERT INTO "Revision" VALUES (2,1,'carlos.sanchez@ejemplo.com','El enfoque es innovador, pero podría mejorar en detalles.','Observaciones pertinentes para coordinación.','Medio','-1','2025-02-03');
INSERT INTO "Revision" VALUES (3,2,'andres.gomez@ejemplo.com','Falta profundidad en análisis de fuentes.','Coordinación recomendada.','Normal','-1','2025-02-06');
INSERT INTO "Revision" VALUES (4,3,'maria.lopez@ejemplo.com','Muy completo, enfoque actualizado.','Listo para publicación.','Alto','-1','2025-01-25');
INSERT INTO "Revision" VALUES (5,2,'carlos.sanchez@ejemplo.com','Se sugiere ampliar ejemplos prácticos.','Sin observaciones adicionales.','Medio','1','2025-01-26');
INSERT INTO "Revision" VALUES (6,3,'andres.gomez@ejemplo.com','Excelente redacción y estructura.','Aprobado por coordinación.','Alto','1','2025-01-27');
INSERT INTO "Revision" VALUES (7,4,'carlos.sanchez@ejemplo.com','El artículo no profundiza en la problemática.','Requiere revisión de estilo.','Bajo','-2','2025-02-11');
INSERT INTO "Revision" VALUES (8,4,'maria.lopez@ejemplo.com','Observaciones menores en conclusión.','Sugerencias incluidas.','Alto','2','2025-02-12');
INSERT INTO "Revision" VALUES (9,5,'carlos.sanchez@ejemplo.com',NULL,NULL,NULL,NULL,NULL);
INSERT INTO "Revision" VALUES (10,6,'maria.lopez@ejemplo.com',NULL,NULL,NULL,NULL,NULL);
INSERT INTO "Revision" VALUES (11,6,'andres.gomez@ejemplo.com',NULL,NULL,NULL,NULL,NULL);
INSERT INTO "Revision" VALUES (12,7,'andres.gomez@ejemplo.com',NULL,NULL,NULL,NULL,NULL);
INSERT INTO "Revision" VALUES (13,8,'paquin@ejemplo.com',NULL,NULL,NULL,NULL,NULL);
INSERT INTO "Revision" VALUES (14,9,'bea@ejemplo.com',NULL,NULL,NULL,NULL,NULL);
INSERT INTO "Rol" VALUES (1,'Autor');
INSERT INTO "Rol" VALUES (2,'Revisor');
INSERT INTO "Rol" VALUES (3,'Coordinador');
INSERT INTO "Track" VALUES (1,1,'Track IA','IA,Machine Learning');
INSERT INTO "Track" VALUES (2,1,'Track Energía','Energía,Renovable');
INSERT INTO "Track" VALUES (3,2,'Track Software','Programación,Software');
INSERT INTO "Track" VALUES (4,2,'Track MedioAmbiente','Medio Ambiente,Sustentabilidad');
INSERT INTO "Track" VALUES (5,3,'Track Big Data','Big Data,Analytics');
INSERT INTO "Track" VALUES (6,3,'Track IoT','IoT,Edge');
INSERT INTO "Usuario" VALUES ('juan.perez@ejemplo.com','Juan Pérez','Universidad Ejemplo','Grupo Alpha');
INSERT INTO "Usuario" VALUES ('maria.lopez@ejemplo.com','María López','Instituto de Pruebas','Grupo Beta');
INSERT INTO "Usuario" VALUES ('carlos.sanchez@ejemplo.com','Carlos Sánchez','Centro de Estudios','Grupo Gamma');
INSERT INTO "Usuario" VALUES ('laura.martinez@ejemplo.com','Laura Martínez','Universidad de la Vida','Grupo Delta');
INSERT INTO "Usuario" VALUES ('andres.gomez@ejemplo.com','Andrés Gómez','Instituto Avanzado','Grupo Epsilon');
INSERT INTO "Usuario" VALUES ('paquin@ejemplo.com','PACO','Epi','Grupo Omega');
INSERT INTO "Usuario" VALUES ('bea@ejemplo.com','BEA','Orquestada','Grupo Beatriz');
INSERT INTO "Usuario" VALUES ('carla.torres@ejemplo.com','Carla Torres','Universidad Nueva','Grupo Theta');
INSERT INTO "Usuario" VALUES ('pablo.rodriguez@ejemplo.com','Pablo Rodríguez','Empresa Innovadora','Grupo Kappa');
INSERT INTO "Usuario_Discusion" VALUES ('maria.lopez@ejemplo.com',1);
INSERT INTO "Usuario_Discusion" VALUES ('carlos.sanchez@ejemplo.com',1);
INSERT INTO "Usuario_Discusion" VALUES ('maria.lopez@ejemplo.com',2);
INSERT INTO "Usuario_Discusion" VALUES ('carlos.sanchez@ejemplo.com',2);
INSERT INTO "Usuario_Discusion" VALUES ('andres.gomez@ejemplo.com',2);
INSERT INTO "Usuario_Preferencia" VALUES ('maria.lopez@ejemplo.com',1);
INSERT INTO "Usuario_Preferencia" VALUES ('carlos.sanchez@ejemplo.com',2);
INSERT INTO "Usuario_Preferencia" VALUES ('andres.gomez@ejemplo.com',3);
INSERT INTO "Usuario_Preferencia" VALUES ('paquin@ejemplo.com',4);
INSERT INTO "Usuario_Preferencia" VALUES ('carlos.sanchez@ejemplo.com',5);
INSERT INTO "Usuario_Preferencia" VALUES ('andres.gomez@ejemplo.com',6);
INSERT INTO "Usuario_Preferencia" VALUES ('maria.lopez@ejemplo.com',7);
INSERT INTO "Usuario_Preferencia" VALUES ('paquin@ejemplo.com',8);
INSERT INTO "Usuario_Preferencia" VALUES ('andres.gomez@ejemplo.com',9);
INSERT INTO "Usuario_Preferencia" VALUES ('maria.lopez@ejemplo.com',10);
INSERT INTO "Usuario_Preferencia" VALUES ('paquin@ejemplo.com',11);
INSERT INTO "Usuario_Preferencia" VALUES ('carlos.sanchez@ejemplo.com',12);
INSERT INTO "Usuario_Rol" VALUES ('juan.perez@ejemplo.com',1);
INSERT INTO "Usuario_Rol" VALUES ('maria.lopez@ejemplo.com',2);
INSERT INTO "Usuario_Rol" VALUES ('maria.lopez@ejemplo.com',3);
INSERT INTO "Usuario_Rol" VALUES ('carlos.sanchez@ejemplo.com',1);
INSERT INTO "Usuario_Rol" VALUES ('carlos.sanchez@ejemplo.com',2);
INSERT INTO "Usuario_Rol" VALUES ('laura.martinez@ejemplo.com',3);
INSERT INTO "Usuario_Rol" VALUES ('andres.gomez@ejemplo.com',1);
INSERT INTO "Usuario_Rol" VALUES ('andres.gomez@ejemplo.com',2);
INSERT INTO "Usuario_Rol" VALUES ('andres.gomez@ejemplo.com',3);
INSERT INTO "Usuario_Rol" VALUES ('paquin@ejemplo.com',2);
INSERT INTO "Usuario_Rol" VALUES ('bea@ejemplo.com',2);
INSERT INTO "Usuario_Rol" VALUES ('juan.perez@ejemplo.com',2);
INSERT INTO "Usuario_Rol" VALUES ('laura.martinez@ejemplo.com',2);
INSERT INTO "Usuario_Rol" VALUES ('carla.torres@ejemplo.com',1);
INSERT INTO "Usuario_Rol" VALUES ('carla.torres@ejemplo.com',2);
INSERT INTO "Usuario_Rol" VALUES ('pablo.rodriguez@ejemplo.com',1);
COMMIT;
