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
=======
/***********************************************************************
  1. INSERTAR CONFERENCIAS Y TRACKS
***********************************************************************/
INSERT INTO Conferencia (nombre, deadlineEnvio, deadlineDiscusion, deadlineRevision)
VALUES 
('Conferencia A', '2026-03-01', '2026-01-10', '2026-01-05'),
('Conferencia B', '2025-03-27', '2025-04-10', '2025-04-05');

INSERT INTO Track (idConferencia, nombre, palabrasClave)
VALUES
(1, 'Track IA', 'IA,Machine Learning'),
(1, 'Track Energía', 'Energía,Renovable'),
(2, 'Track Software', 'Programación,Software'),
(2, 'Track MedioAmbiente', 'Medio Ambiente,Sustentabilidad');

INSERT INTO Conferencia (nombre, deadlineEnvio, deadlineDiscusion, deadlineRevision)
VALUES ('Conferencia C', '2025-01-01', '2025-01-10', '2025-01-05');

INSERT INTO Track (idConferencia, nombre, palabrasClave)
VALUES
(3, 'Track Big Data', 'Big Data,Analytics'),
(3, 'Track IoT', 'IoT,Edge');

/***********************************************************************
  2. INSERTAR USUARIOS
***********************************************************************/
INSERT INTO Usuario (email, nombre, organizacion, grupoInvestigacion)
VALUES 
('juan.perez@ejemplo.com', 'Juan Pérez', 'Universidad Ejemplo', 'Grupo Alpha'),
('carlos.sanchez@ejemplo.com', 'Carlos Sánchez', 'Centro de Estudios', 'Grupo Gamma'),
('laura.martinez@ejemplo.com', 'Laura Martínez', 'Universidad de la Vida', 'Grupo Delta'),
('andres.gomez@ejemplo.com', 'Andrés Gómez', 'Instituto Avanzado', 'Grupo Epsilon'),
('paquin@ejemplo.com', 'PACO', 'Epi', 'Grupo Omega'),
('bea@ejemplo.com', 'BEA', 'Orquestada', 'Grupo Beatriz');

INSERT INTO Usuario (email, nombre, organizacion, grupoInvestigacion)
VALUES 
('carla.torres@ejemplo.com', 'Carla Torres', 'Universidad Nueva', 'Grupo Theta'),
('pablo.rodriguez@ejemplo.com', 'Pablo Rodríguez', 'Empresa Innovadora', 'Grupo Kappa');

INSERT INTO Usuario (email, nombre, organizacion, grupoInvestigacion, palabrasClave)
VALUES
('maria.lopez@ejemplo.com', 'María López', 'Instituto de Pruebas', 'Grupo Beta', 'Pilas, Robótica');
/***********************************************************************
  3. ROLES Y ASIGNACIÓN (Usuario_Rol)
***********************************************************************/
INSERT INTO Rol (rol) VALUES ('Autor');
INSERT INTO Rol (rol) VALUES ('Revisor');
INSERT INTO Rol (rol) VALUES ('Coordinador');

INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('juan.perez@ejemplo.com', 1);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('maria.lopez@ejemplo.com', 2);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('maria.lopez@ejemplo.com', 3);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('carlos.sanchez@ejemplo.com', 1);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('carlos.sanchez@ejemplo.com', 2);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('laura.martinez@ejemplo.com', 3);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('andres.gomez@ejemplo.com', 1);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('andres.gomez@ejemplo.com', 2);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('andres.gomez@ejemplo.com', 3);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('paquin@ejemplo.com', 2);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('bea@ejemplo.com', 2);

INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('juan.perez@ejemplo.com', 2);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('laura.martinez@ejemplo.com', 2);

INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('carla.torres@ejemplo.com', 1);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('carla.torres@ejemplo.com', 2);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('pablo.rodriguez@ejemplo.com', 1);

/***********************************************************************
  4. ARTÍCULOS
***********************************************************************/
INSERT INTO Articulo (idTrack, titulo, palabrasClave, palabrasClaveTrack, resumen, nombreFichero, fechaEnvio, decisionFinal, valoracionGlobal)
VALUES 
(1, 'Avances en IA', 'Inteligentes', 'IA', 
    'Este artículo trata sobre avances en inteligencia artificial.', 
    'avances_ia.pdf', '2025-02-01', 'Aceptado', 1),
(2, 'Innovación en Energías', 'Fabrica,Progreso', 'Renovable', 
    'Analiza innovaciones en energías renovables.', 
    'innovacion_energias.pdf', '2025-02-05', 'Rechazado', -1),
(3, 'Nuevas Técnicas de Programación', 'Futuro,Desempleo,PHP','Programación,Software', 
    'Explora técnicas modernas en desarrollo de software.', 
    'nuevas_tecnicas_programacion.pdf', '2025-01-20', 'Aceptado', 1),
(4, 'Impacto Ambiental', 'Desarrollo,Naturaleza','Medio Ambiente', 
    'Discusión sobre impacto ambiental de la tecnología.', 
    'impacto_ambiental.pdf', '2025-02-10', 'Rechazado', 0),
(1, 'Futuro de la Robótica', 'Pilas,Enchufes,Luces','Robótica,Tecnología', 
    'Examina el futuro de la robótica.', 
    'futuro_robotica.pdf', '2025-02-15', 'Pendiente', NULL),
(2, 'Estrategias de Marketing', 'Emprender,Decision,Publicidad', 'Marketing,Ventas', 
    'Revisión de estrategias de marketing digital.', 
    'estrategias_marketing.pdf', '2025-02-18', 'Pendiente', NULL),
(3, 'Tendencias en Ciberseguridad', 'Hackers,Programacion', 'Ciberseguridad', 
    'Análisis de tendencias en ciberseguridad.', 
    'tendencias_ciberseguridad.pdf', '2025-02-20', 'Pendiente', NULL);

INSERT INTO Articulo (idTrack, titulo, palabrasClave, palabrasClaveTrack, resumen, nombreFichero, fechaEnvio, decisionFinal, valoracionGlobal)
VALUES 
(5, 'Análisis de Grandes Volúmenes de Datos', 'Datos,Muchos datos', 'Big Data,Analytics', 
    'Un estudio sobre el procesamiento masivo de datos.', 
    'analisis_big_data.pdf', '2025-03-20', 'Pendiente', NULL);

INSERT INTO Articulo (idTrack, titulo, palabrasClave, palabrasClaveTrack, resumen, nombreFichero, fechaEnvio, decisionFinal, valoracionGlobal)
VALUES 
(6, 'Aplicaciones IoT en la Industria', 'Bolsonaro,Bolsocaro', 'IoT,Edge', 
    'Examina casos de uso IoT en entornos industriales.', 
    'iot_industria.pdf', '2025-03-22', 'Pendiente', NULL);

/***********************************************************************
  5. RELACIÓN Articulo_Usuario
***********************************************************************/
INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES 
(1, 'juan.perez@ejemplo.com', 1),
(1, 'carlos.sanchez@ejemplo.com', 0),
(1, 'andres.gomez@ejemplo.com', 0),
(2, 'maria.lopez@ejemplo.com', 1),
(2, 'laura.martinez@ejemplo.com', 0),
(3, 'carlos.sanchez@ejemplo.com', 1),
(3, 'juan.perez@ejemplo.com', 0),
(3, 'andres.gomez@ejemplo.com', 0),
(4, 'laura.martinez@ejemplo.com', 1),
(4, 'maria.lopez@ejemplo.com', 0),
(4, 'andres.gomez@ejemplo.com', 0),
(5, 'andres.gomez@ejemplo.com', 1),
(6, 'maria.lopez@ejemplo.com', 1),
(7, 'juan.perez@ejemplo.com', 1),
(7, 'laura.martinez@ejemplo.com', 0),
(7, 'andres.gomez@ejemplo.com', 0);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES 
(8, 'carla.torres@ejemplo.com', 1),
(9, 'pablo.rodriguez@ejemplo.com', 1);

/***********************************************************************
  6. REVISIONES
***********************************************************************/
INSERT INTO Revision (idArticulo, emailUsuario, comentariosParaAutor, comentariosParaCoordinador, nivelExperto, decisionRevisor, fechaRevision)
VALUES 
(1, 'maria.lopez@ejemplo.com', 'Buen trabajo, se destaca la metodología.', 
    'Revisión adecuada para coordinación.', 'Alto', 2, '2025-02-02'),
(1, 'carlos.sanchez@ejemplo.com', 'El enfoque es innovador, pero podría mejorar en detalles.', 
    'Observaciones pertinentes para coordinación.', 'Medio', -1, '2025-02-03'),
(2, 'andres.gomez@ejemplo.com', 'Falta profundidad en análisis de fuentes.', 
    'Coordinación recomendada.', 'Normal', -1, '2025-02-06'),
(3, 'maria.lopez@ejemplo.com', 'Muy completo, enfoque actualizado.', 
    'Listo para publicación.', 'Alto', -1, '2025-01-25'),
(3, 'carlos.sanchez@ejemplo.com', 'Se sugiere ampliar ejemplos prácticos.', 
    'Sin observaciones adicionales.', 'Medio', 1, '2025-01-26'),
(3, 'andres.gomez@ejemplo.com', 'Excelente redacción y estructura.', 
    'Aprobado por coordinación.', 'Alto', 1, '2025-01-27'),
(4, 'carlos.sanchez@ejemplo.com', 'El artículo no profundiza en la problemática.', 
    'Requiere revisión de estilo.', 'Bajo', -2, '2025-02-11'),
(4, 'maria.lopez@ejemplo.com', 'Observaciones menores en conclusión.', 
    'Sugerencias incluidas.', 'Alto', 2, '2025-02-12');

INSERT INTO Revision (idArticulo, emailUsuario)
VALUES 
(5, 'carlos.sanchez@ejemplo.com'),
(6, 'maria.lopez@ejemplo.com'),
(6, 'andres.gomez@ejemplo.com'),
(7, 'andres.gomez@ejemplo.com');

INSERT INTO Revision (idArticulo, emailUsuario)
VALUES 
(8, 'paquin@ejemplo.com'),
(9, 'bea@ejemplo.com');

/***********************************************************************
  7. DISCUSIONES, USUARIOS EN DISCUSIÓN Y ANOTACIONES
***********************************************************************/
INSERT INTO Discusion (idArticulo) VALUES (1);
INSERT INTO Discusion (idArticulo) VALUES (3);

INSERT INTO Usuario_Discusion (emailUsuario, idDiscusion)
VALUES 
('maria.lopez@ejemplo.com', 1),
('carlos.sanchez@ejemplo.com', 1),
('maria.lopez@ejemplo.com', 2),
('carlos.sanchez@ejemplo.com', 2),
('andres.gomez@ejemplo.com', 2);

INSERT INTO Anotacion (idDiscusion, emailUsuario, comentario, fecha, hora)
VALUES 
(1, 'maria.lopez@ejemplo.com', 
    'Creo que habría que profundizar más en la introducción.', 
    '2025-03-10', '10:30'),
(1, 'andres.gomez@ejemplo.com', 
    'Estoy de acuerdo, añadiré referencias adicionales.', 
    '2025-03-10', '10:45'),
(2, 'andres.gomez@ejemplo.com', 
    'La sección de resultados necesita más ejemplos.', 
    '2025-03-12', '09:15'),
(2, 'maria.lopez@ejemplo.com', 
    'Concuerdo contigo acerca de los ejemplos.', 
    '2025-03-12', '09:20'),
(2, 'carlos.sanchez@ejemplo.com', 
    'He revisado esa sección, puedo añadir ejemplos de proyectos reales.', 
    '2025-03-12', '09:25');
    
/***********************************************************************
  8. Agregar preferencias de Articulos a Revisores
***********************************************************************/
INSERT INTO Preferencia (decision, idArticulo)
VALUES
('Lo quiero revisar', 5),
('No quiero revisar', 5),
('Puedo revisar', 5),
('Conflicto', 5),
('Lo quiero revisar', 6),
('No quiero revisar', 6),
('Puedo revisar', 6),
('Conflicto', 6),
('Lo quiero revisar', 7),
('No quiero revisar', 7),
('Puedo revisar', 7),
('Conflicto', 7);

INSERT INTO Usuario_Preferencia (emailUsuario, idPreferencia)
VALUES
('maria.lopez@ejemplo.com', 1),
('carlos.sanchez@ejemplo.com', 2),
('andres.gomez@ejemplo.com', 3),
('paquin@ejemplo.com', 4),
('carlos.sanchez@ejemplo.com', 5),
('andres.gomez@ejemplo.com', 6),
('maria.lopez@ejemplo.com', 7),
('paquin@ejemplo.com', 8),
('andres.gomez@ejemplo.com', 9),
('maria.lopez@ejemplo.com', 10),
('paquin@ejemplo.com', 11),
('carlos.sanchez@ejemplo.com', 12);
INSERT INTO Preferencia (decision, idArticulo) VALUES ('Lo quiero revisar', 2);

-- luego con ese ID:
INSERT INTO Usuario_Preferencia (emailUsuario, idPreferencia) VALUES ('maria.lopez@ejemplo.com', 13);


INSERT INTO Revision (idArticulo, emailUsuario, comentariosParaAutor, comentariosParaCoordinador, nivelExperto, decisionRevisor, fechaRevision)
VALUES 
(8, 'maria.lopez@ejemplo.com', 
 'Este artículo tiene buen enfoque pero debe profundizar más.', 
 'Se recomienda revisión por pares.', 
 'Medio', 1, '2025-01-04');
 
 
-- ==============================================
-- EJEMPLO DE PRUEBA: CERRAR Y DECIDIR ARTÍCULO 5
-- ==============================================

-- 1) Inserto una discusión cerrada para el artículo id=5
INSERT INTO Discusion (idArticulo, isCerrada)
VALUES (5, 1);

-- 2) Asocio a dos revisores ya existentes a esa discusión
INSERT INTO Usuario_Discusion (emailUsuario, idDiscusion)
VALUES
  ('juan.perez@ejemplo.com', last_insert_rowid()),
  ('maria.lopez@ejemplo.com', last_insert_rowid());

-- 3) Verificación inicial (en consola):
--    SELECT idArticulo, titulo, decisionFinal
--      FROM Articulo
--     WHERE idArticulo = 5;
--    -- Debe devolver: 'Pendiente'

-- 4) Simulación de model.aceptarArticulo(5):
UPDATE Articulo
   SET decisionFinal = 'Aceptado'
 WHERE idArticulo = 5;

-- 5) Verificar que fue aceptado:
--    SELECT decisionFinal
--      FROM Articulo
--     WHERE idArticulo = 5;
--    -- Ahora debe devolver: 'Aceptado'

-- 6) Restaurar a 'Pendiente' para probar rechazo:
UPDATE Articulo
   SET decisionFinal = 'Pendiente'
 WHERE idArticulo = 5;

-- 7) Simulación de model.rechazarArticulo(5):
UPDATE Articulo
   SET decisionFinal = 'Rechazado'
 WHERE idArticulo = 5;

-- 8) Verificar que fue rechazado:
--    SELECT decisionFinal
--      FROM Articulo
--     WHERE idArticulo = 5;
--    -- Ahora debe devolver: 'Rechazado'
