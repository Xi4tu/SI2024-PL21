-- DATOS DE EJEMPLO PARA LA TABLA USUARIO
INSERT INTO USUARIO (email, nombre, organizacion, grupoInvestigacion)
VALUES
  ('alice@example.com', 'Alice', 'Univ. Example', 'G1'),
  ('bob@example.com', 'Bob', 'Univ. Example', NULL),
  ('charlie@example.com', 'Charlie', 'Another University', 'BigData');

-- DATOS DE EJEMPLO PARA ROL
INSERT INTO ROL (rol) VALUES ('Autor');
INSERT INTO ROL (rol) VALUES ('Revisor');
INSERT INTO ROL (rol) VALUES ('Coordinador');

-- EJEMPLO USUARIO_ROL
INSERT INTO USUARIO_ROL (emailUsuario, idRol)
VALUES
  ('alice@example.com', 1),  -- Alice como Autor
  ('alice@example.com', 2),  -- y también Revisor
  ('bob@example.com', 1),    -- Bob como Autor
  ('charlie@example.com', 3);-- Charlie como Coordinador

-- DATOS DE EJEMPLO PARA ARTICULO
INSERT INTO ARTICULO (titulo, palabrasClave, resumen, nombreFichero, fechaEnvio)
VALUES
  ('Articulo de prueba', 'AI,Machine Learning', 'Resumen del artículo', 'paper1.pdf', '2023-12-01'),
  ('Segundo Articulo', 'Computer Vision,Deep Learning', 'Otro resumen', 'paper2.doc', '2023-12-05');

-- EJEMPLO ARTICULO_USUARIO
INSERT INTO ARTICULO_USUARIO (idArticulo, emailUsuario, esEnviador)
VALUES
  (1, 'alice@example.com', 1),  -- Alice envía el primer artículo
  (1, 'bob@example.com', 0),    -- Bob es coautor
  (2, 'bob@example.com', 1);    -- Bob envía el segundo artículo, sin coautores

-- EJEMPLO REVISION
INSERT INTO REVISION (idArticulo, emailUsuario, comentariosParaAutor, comentariosParaCoordinador, 
                      nivelExperto, decisionRevisor, fechaRevision)
VALUES
  (1, 'alice@example.com', 'Buen trabajo', 'Podría mejorar la introducción', 'ALTO', 'ACEPTABLE', '2023-12-10');
