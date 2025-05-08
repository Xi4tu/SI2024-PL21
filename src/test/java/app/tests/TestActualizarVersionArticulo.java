package app.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import app.dto.AutorDTO;
import app.model.EnviarArticuloModel;
import giis.demo.util.Database;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * Pruebas JUnit del proceso de negocio: Enviar una nueva versión de un artículo.
 * 
 * Dado que el modelo no distingue entre versión y envío nuevo, se simula la subida
 * de una versión mediante un nuevo envío con el mismo título.
 * 
 * Se comprueba:
 * - Que solo el autor original puede subir la versión.
 * - Que no se permita si ha vencido el deadline de la conferencia.
 * - Que el sistema gestione correctamente un intento válido (aunque actualmente falla).
 */
@RunWith(JUnitParamsRunner.class)
public class TestActualizarVersionArticulo {

	private static Database db = new Database();
	private EnviarArticuloModel model;

	private final int ID_TRACK = 1;
	private final int ID_ARTICULO = 1;

	/**
	 * Preparación de la base de datos con datos mínimos:
	 * - Una conferencia con track.
	 * - Dos usuarios (uno autor, otro no).
	 * - Un artículo con un título dado.
	 */
	@Before
	public void setUp() {
		db.createDatabase(true);
		model = new EnviarArticuloModel();

		db.executeBatch(new String[] {
			"DELETE FROM Articulo_Usuario",
			"DELETE FROM Articulo",
			"DELETE FROM Usuario",
			"DELETE FROM Track",
			"DELETE FROM Conferencia",

			"INSERT INTO Conferencia(idConferencia, nombre, deadlineEnvio, deadlineDiscusion, deadlineRevision) " +
			"VALUES (1, 'ConfTest', '2025-06-01', '2025-06-15', '2025-06-30')",

			"INSERT INTO Track(idTrack, idConferencia, nombre, palabrasClave) " +
			"VALUES (1, 1, 'TrackTest', 'IA')",

			"INSERT INTO Usuario(email, nombre, organizacion, grupoInvestigacion) " +
			"VALUES ('autor@uniovi.es', 'Autor Uno', 'Uni', 'GI')," +
			"('otro@uniovi.es', 'Otro Usuario', 'Uni', 'GI')",

			"INSERT INTO Articulo(idArticulo, idTrack, titulo, palabrasClave, palabrasClaveTrack, resumen, nombreFichero, fechaEnvio) " +
			"VALUES (1, 1, 'Articulo Original', 'IA', 'IA', 'Resumen original', 'original.pdf', '2025-05-01')",

			"INSERT INTO Articulo_Usuario(idArticulo, emailUsuario, esEnviador) " +
			"VALUES (1, 'autor@uniovi.es', 1)"
		});
	}

	/**
	 * Prueba parametrizada que simula el intento de subir una nueva versión
	 * mediante una nueva llamada a enviarArticulo con el mismo título.
	 */
	@Test
	@Parameters(method = "getCasosVersion")
	public void testNuevaVersion(String email, String fechaDeadline, boolean debeFallar) {
		// Modificamos el deadline para simular que estamos dentro o fuera de plazo
		db.executeUpdate("UPDATE Conferencia SET deadlineEnvio = ?", fechaDeadline);

		ArrayList<AutorDTO> autores = new ArrayList<>();
		autores.add(new AutorDTO(email, "Nombre", "Uni", "GI"));

		if (debeFallar) {
			assertThrows(Exception.class, () -> {
				model.enviarArticulo(ID_TRACK, "Articulo Original", "claveModificada", "IA", "Resumen nuevo", "nuevo.pdf", autores);
			});
		} else {
			try {
				model.enviarArticulo(ID_TRACK, "Articulo Original", "claveModificada", "IA", "Resumen nuevo", "nuevo.pdf", autores);
			} catch (Exception e) {
				fail("No deber\u00eda lanzar excepci\u00f3n: " + e.getMessage());
			}
		}
	}

	/**
	 * Casos de prueba:
	 * - CP1: Usuario correcto y dentro de plazo.
	 * - CP2: Otro usuario (no autorizado).
	 * - CP3: Usuario válido pero fuera de plazo.
	 */
	@SuppressWarnings("unused")
	private Object[] getCasosVersion() {
		return new Object[] {
			new Object[] { "autor@uniovi.es", "2025-06-01", false }, // CP1: correcto
			new Object[] { "otro@uniovi.es", "2025-06-01", true },   // CP2: no es autor
			new Object[] { "autor@uniovi.es", "2025-04-01", true }   // CP3: fuera de plazo
		};
	}
}
