-- INSERTAR USUARIOS DE PRUEBA

INSERT INTO Usuario (email, nombre, organizacion, grupoInvestigacion)
VALUES ('juan.perez@ejemplo.com', 'Juan Pérez', 'Universidad Ejemplo', 'Grupo Alpha');

INSERT INTO Usuario (email, nombre, organizacion, grupoInvestigacion)
VALUES ('maria.lopez@ejemplo.com', 'María López', 'Instituto de Pruebas', 'Grupo Beta');

INSERT INTO Usuario (email, nombre, organizacion, grupoInvestigacion)
VALUES ('carlos.sanchez@ejemplo.com', 'Carlos Sánchez', 'Centro de Estudios', 'Grupo Gamma');

INSERT INTO Usuario (email, nombre, organizacion, grupoInvestigacion)
VALUES ('laura.martinez@ejemplo.com', 'Laura Martínez', 'Universidad de la Vida', 'Grupo Delta');

INSERT INTO Usuario (email, nombre, organizacion, grupoInvestigacion)
VALUES ('andres.gomez@ejemplo.com', 'Andrés Gómez', 'Instituto Avanzado', 'Grupo Epsilon');

INSERT INTO Usuario (email, nombre, organizacion, grupoInvestigacion)
VALUES ('paquin@ejemplo.com', 'PACO', 'Epi', 'Grupo Omega');

INSERT INTO Usuario (email, nombre, organizacion, grupoInvestigacion)
VALUES ('bea@ejemplo.com', 'BEA', 'Orquestada', 'Grupo Beatriz');

-- INSERTAR ROLES DISPONIBLES

INSERT INTO Rol (rol) VALUES ('Autor');
INSERT INTO Rol (rol) VALUES ('Revisor');
INSERT INTO Rol (rol) VALUES ('Coordinador');

-- ASIGNAR ROLES A USUARIOS

-- Asignación de rol a Juan Pérez
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('juan.perez@ejemplo.com', 1);

-- Asignación de roles a María López
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('maria.lopez@ejemplo.com', 2);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('maria.lopez@ejemplo.com', 3);

-- Asignación de roles a Carlos Sánchez
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('carlos.sanchez@ejemplo.com', 1);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('carlos.sanchez@ejemplo.com', 2);

-- Asignación de rol a Laura Martínez
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('laura.martinez@ejemplo.com', 3);

-- Asignación de roles a Andrés Gómez
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('andres.gomez@ejemplo.com', 1);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('andres.gomez@ejemplo.com', 2);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('andres.gomez@ejemplo.com', 3);

-- Asignación de roles a pACO
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('paquin@ejemplo.com', 2);
INSERT INTO Usuario_Rol (emailUsuario, idRol) VALUES ('bea@ejemplo.com', 2);

-- INSERTAR ARTÍCULOS DE PRUEBA

-- Artículos revisados
INSERT INTO Articulo (titulo, palabrasClave, resumen, nombreFichero, fechaEnvio, decisionFinal, valoracionGlobal)
VALUES ('Avances en IA', 'IA, Machine Learning', 'Este artículo trata sobre avances en inteligencia artificial.', 'avances_ia.pdf', '2025-02-01', 'Aceptado', 3);

INSERT INTO Articulo (titulo, palabrasClave, resumen, nombreFichero, fechaEnvio, decisionFinal, valoracionGlobal)
VALUES ('Innovación en Energías', 'Energía, Renovable', 'Analiza innovaciones en energías renovables.', 'innovacion_energias.pdf', '2025-02-05', 'Rechazado', -1);

INSERT INTO Articulo (titulo, palabrasClave, resumen, nombreFichero, fechaEnvio, decisionFinal, valoracionGlobal)
VALUES ('Nuevas Técnicas de Programación', 'Programación, Software', 'Explora técnicas modernas en desarrollo de software.', 'nuevas_tecnicas_programacion.pdf', '2025-01-20', 'Aceptado', 5);

INSERT INTO Articulo (titulo, palabrasClave, resumen, nombreFichero, fechaEnvio, decisionFinal, valoracionGlobal)
VALUES ('Impacto Ambiental', 'Medio Ambiente, Sustentabilidad', 'Discusión sobre impacto ambiental de la tecnología.', 'impacto_ambiental.pdf', '2025-02-10', 'Rechazado', -3);

-- Artículos pendientes de revisión
INSERT INTO Articulo (titulo, palabrasClave, resumen, nombreFichero, fechaEnvio, decisionFinal, valoracionGlobal)
VALUES ('Futuro de la Robótica', 'Robótica, Tecnología', 'Examina el futuro de la robótica.', 'futuro_robotica.pdf', '2025-02-15', 'Pendiente', NULL);

INSERT INTO Articulo (titulo, palabrasClave, resumen, nombreFichero, fechaEnvio, decisionFinal, valoracionGlobal)
VALUES ('Estrategias de Marketing', 'Marketing, Ventas', 'Revisión de estrategias de marketing digital.', 'estrategias_marketing.pdf', '2025-02-18', 'Pendiente', NULL);

INSERT INTO Articulo (titulo, palabrasClave, resumen, nombreFichero, fechaEnvio, decisionFinal, valoracionGlobal)
VALUES ('Tendencias en Ciberseguridad', 'Ciberseguridad, IT', 'Análisis de tendencias en ciberseguridad.', 'tendencias_ciberseguridad.pdf', '2025-02-20', 'Pendiente', NULL);


-- ASIGNAR ARTÍCULOS A USUARIOS

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (1, 'juan.perez@ejemplo.com', 1);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (1, 'carlos.sanchez@ejemplo.com', 0);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (1, 'andres.gomez@ejemplo.com', 0);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (2, 'maria.lopez@ejemplo.com', 1);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (2, 'laura.martinez@ejemplo.com', 0);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (3, 'carlos.sanchez@ejemplo.com', 1);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (3, 'juan.perez@ejemplo.com', 0);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (3, 'andres.gomez@ejemplo.com', 0);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (4, 'laura.martinez@ejemplo.com', 1);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (4, 'maria.lopez@ejemplo.com', 0);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (4, 'andres.gomez@ejemplo.com', 0);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (5, 'andres.gomez@ejemplo.com', 1);

--INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
--VALUES (5, 'juan.perez@ejemplo.com', 0);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (6, 'maria.lopez@ejemplo.com', 1);

--INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
--VALUES (6, 'carlos.sanchez@ejemplo.com', 0);

--INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
--VALUES (6, 'laura.martinez@ejemplo.com', 0);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (7, 'juan.perez@ejemplo.com', 1);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (7, 'laura.martinez@ejemplo.com', 0);

INSERT INTO Articulo_Usuario (idArticulo, emailUsuario, esEnviador)
VALUES (7, 'andres.gomez@ejemplo.com', 0);


-- REVISIONES COMPLETADAS (todos los campos tienen valores)

-- Artículo 1: "Avances en IA" (Aceptado) - 2 revisiones
INSERT INTO Revision (idArticulo, emailUsuario, comentariosParaAutor, comentariosParaCoordinador, nivelExperto, decisionRevisor, fechaRevision)
VALUES (1, 'maria.lopez@ejemplo.com', 'Buen trabajo, se destaca la metodología.', 'Revisión adecuada para coordinación.', 'Alto', 2, '2025-02-02');

INSERT INTO Revision (idArticulo, emailUsuario, comentariosParaAutor, comentariosParaCoordinador, nivelExperto, decisionRevisor, fechaRevision)
VALUES (1, 'carlos.sanchez@ejemplo.com', 'El enfoque es innovador, pero podría mejorar en detalles.', 'Observaciones pertinentes para coordinación.', 'Medio', 1, '2025-02-03');

-- Artículo 2: "Innovación en Energías" (Rechazado) - 1 revisión
INSERT INTO Revision (idArticulo, emailUsuario, comentariosParaAutor, comentariosParaCoordinador, nivelExperto, decisionRevisor, fechaRevision)
VALUES (2, 'andres.gomez@ejemplo.com', 'Falta profundidad en análisis de fuentes.', 'Coordinación recomendada.', 'Normal', -1, '2025-02-06');

-- Artículo 3: "Nuevas Técnicas de Programación" (Aceptado) - 3 revisiones
INSERT INTO Revision (idArticulo, emailUsuario, comentariosParaAutor, comentariosParaCoordinador, nivelExperto, decisionRevisor, fechaRevision)
VALUES (3, 'maria.lopez@ejemplo.com', 'Muy completo, enfoque actualizado.', 'Listo para publicación.', 'Alto', 2, '2025-01-25');

INSERT INTO Revision (idArticulo, emailUsuario, comentariosParaAutor, comentariosParaCoordinador, nivelExperto, decisionRevisor, fechaRevision)
VALUES (3, 'carlos.sanchez@ejemplo.com', 'Se sugiere ampliar ejemplos prácticos.', 'Sin observaciones adicionales.', 'Medio', 1, '2025-01-26');

INSERT INTO Revision (idArticulo, emailUsuario, comentariosParaAutor, comentariosParaCoordinador, nivelExperto, decisionRevisor, fechaRevision)
VALUES (3, 'andres.gomez@ejemplo.com', 'Excelente redacción y estructura.', 'Aprobado por coordinación.', 'Alto', 2, '2025-01-27');

-- Artículo 4: "Impacto Ambiental" (Rechazado) - 2 revisiones
INSERT INTO Revision (idArticulo, emailUsuario, comentariosParaAutor, comentariosParaCoordinador, nivelExperto, decisionRevisor, fechaRevision)
VALUES (4, 'carlos.sanchez@ejemplo.com', 'El artículo no profundiza en la problemática.', 'Requiere revisión de estilo.', 'Bajo', -2, '2025-02-11');

INSERT INTO Revision (idArticulo, emailUsuario, comentariosParaAutor, comentariosParaCoordinador, nivelExperto, decisionRevisor, fechaRevision)
VALUES (4, 'maria.lopez@ejemplo.com', 'Observaciones menores en conclusión.', 'Sugerencias incluidas.', 'Normal', -1, '2025-02-12');


-- REVISIONES PENDIENTES (solo se asignan idArticulo y emailUsuario)

-- Para artículos pendientes (con decisionFinal = 'Pendiente'):
-- Artículo 5: "Futuro de la Robótica" - 1 revisión pendiente
INSERT INTO Revision (idArticulo, emailUsuario)
VALUES (5, 'carlos.sanchez@ejemplo.com');

-- Artículo 6: "Estrategias de Marketing" - 2 revisiones pendientes
INSERT INTO Revision (idArticulo, emailUsuario)
VALUES (6, 'maria.lopez@ejemplo.com');

INSERT INTO Revision (idArticulo, emailUsuario)
VALUES (6, 'andres.gomez@ejemplo.com');

-- Artículo 7: "Tendencias en Ciberseguridad" - 1 revisión pendiente
INSERT INTO Revision (idArticulo, emailUsuario)
VALUES (7, 'andres.gomez@ejemplo.com'); 


