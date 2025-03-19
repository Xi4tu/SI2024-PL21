/***********************************************************************
  1. INSERTAR CONFERENCIAS Y TRACKS
***********************************************************************/
INSERT INTO Conferencia (nombre, deadline)
VALUES 
('Conferencia A', '2025-01-01'),
('Conferencia B', '2025-09-15');

INSERT INTO Track (idConferencia, nombre, palabrasClave)
VALUES
(1, 'Track IA', 'IA,Machine Learning'),
(1, 'Track Energía', 'Energía,Renovable'),
(2, 'Track Software', 'Programación,Software'),
(2, 'Track MedioAmbiente', 'Medio Ambiente,Sustentabilidad');

INSERT INTO Conferencia (nombre, deadline)
VALUES ('Conferencia C', '2025-07-10');

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
('maria.lopez@ejemplo.com', 'María López', 'Instituto de Pruebas', 'Grupo Beta'),
('carlos.sanchez@ejemplo.com', 'Carlos Sánchez', 'Centro de Estudios', 'Grupo Gamma'),
('laura.martinez@ejemplo.com', 'Laura Martínez', 'Universidad de la Vida', 'Grupo Delta'),
('andres.gomez@ejemplo.com', 'Andrés Gómez', 'Instituto Avanzado', 'Grupo Epsilon'),
('paquin@ejemplo.com', 'PACO', 'Epi', 'Grupo Omega'),
('bea@ejemplo.com', 'BEA', 'Orquestada', 'Grupo Beatriz');

INSERT INTO Usuario (email, nombre, organizacion, grupoInvestigacion)
VALUES 
('carla.torres@ejemplo.com', 'Carla Torres', 'Universidad Nueva', 'Grupo Theta'),
('pablo.rodriguez@ejemplo.com', 'Pablo Rodríguez', 'Empresa Innovadora', 'Grupo Kappa');

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
    'avances_ia.pdf', '2025-02-01', 'Aceptado', 3),
(2, 'Innovación en Energías', 'Fabrica,Progreso', 'Renovable', 
    'Analiza innovaciones en energías renovables.', 
    'innovacion_energias.pdf', '2025-02-05', 'Rechazado', -1),
(3, 'Nuevas Técnicas de Programación', 'Futuro,Desempleo,PHP','Programación,Software', 
    'Explora técnicas modernas en desarrollo de software.', 
    'nuevas_tecnicas_programacion.pdf', '2025-01-20', 'Aceptado', 5),
(4, 'Impacto Ambiental', 'Desarrollo,Naturaleza','Medio Ambiente', 
    'Discusión sobre impacto ambiental de la tecnología.', 
    'impacto_ambiental.pdf', '2025-02-10', 'Rechazado', -3),
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
    'Observaciones pertinentes para coordinación.', 'Medio', 1, '2025-02-03'),
(2, 'andres.gomez@ejemplo.com', 'Falta profundidad en análisis de fuentes.', 
    'Coordinación recomendada.', 'Normal', -1, '2025-02-06'),
(3, 'maria.lopez@ejemplo.com', 'Muy completo, enfoque actualizado.', 
    'Listo para publicación.', 'Alto', 2, '2025-01-25'),
(3, 'carlos.sanchez@ejemplo.com', 'Se sugiere ampliar ejemplos prácticos.', 
    'Sin observaciones adicionales.', 'Medio', 1, '2025-01-26'),
(3, 'andres.gomez@ejemplo.com', 'Excelente redacción y estructura.', 
    'Aprobado por coordinación.', 'Alto', 2, '2025-01-27'),
(4, 'carlos.sanchez@ejemplo.com', 'El artículo no profundiza en la problemática.', 
    'Requiere revisión de estilo.', 'Bajo', -2, '2025-02-11'),
(4, 'maria.lopez@ejemplo.com', 'Observaciones menores en conclusión.', 
    'Sugerencias incluidas.', 'Normal', -1, '2025-02-12');

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
INSERT INTO Discusion (idArticulo) VALUES (5);

INSERT INTO Usuario_Discusion (emailUsuario, idDiscusion)
VALUES 
('maria.lopez@ejemplo.com', 1),
('andres.gomez@ejemplo.com', 1),
('juan.perez@ejemplo.com', 2),
('carlos.sanchez@ejemplo.com', 2),
('laura.martinez@ejemplo.com', 3);

INSERT INTO Anotacion (idDiscusion, emailUsuario, comentario, fecha, hora)
VALUES 
(1, 'maria.lopez@ejemplo.com', 
    'Creo que habría que profundizar más en la introducción.', 
    '2025-03-10', '10:30'),
(1, 'andres.gomez@ejemplo.com', 
    'Estoy de acuerdo, añadiré referencias adicionales.', 
    '2025-03-10', '10:45'),
(2, 'juan.perez@ejemplo.com', 
    'La sección de resultados necesita más ejemplos.', 
    '2025-03-12', '09:15'),
(2, 'carlos.sanchez@ejemplo.com', 
    'He revisado esa sección, puedo añadir ejemplos de proyectos reales.', 
    '2025-03-12', '09:25'),
(3, 'laura.martinez@ejemplo.com', 
    'Necesitamos más datos de las pruebas con prototipos.', 
    '2025-03-15', '11:00');

INSERT INTO Discusion (idArticulo) VALUES (8);
INSERT INTO Discusion (idArticulo) VALUES (9);

INSERT INTO Usuario_Discusion (emailUsuario, idDiscusion)
VALUES
('carla.torres@ejemplo.com', 4),
('paquin@ejemplo.com', 4),
('pablo.rodriguez@ejemplo.com', 5),
('bea@ejemplo.com', 5);

INSERT INTO Anotacion (idDiscusion, emailUsuario, comentario, fecha, hora)
VALUES
(4, 'carla.torres@ejemplo.com', 
    'Revisé los métodos estadísticos; se pueden optimizar.', 
    '2025-03-25', '10:00'),
(4, 'paquin@ejemplo.com', 
    'Coincido. Falta clarificar la arquitectura de procesamiento.', 
    '2025-03-25', '10:15'),
(5, 'bea@ejemplo.com', 
    'El enfoque IoT está bien, pero falta detalle en seguridad.', 
    '2025-03-26', '09:45');