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
 * Pruebas JUnit del proceso de negocio: Enviar un nuevo artículo.
 * 
 * Se comprueban los siguientes aspectos:
 * - Inserción correcta con datos válidos.
 * - Error si el título ya existe.
 * - Error si no hay autores.
 * - Error si un autor no tiene email.
 */
@RunWith(JUnitParamsRunner.class)
public class TestEnviarArticulo {

	private static Database db = new Database();
	private EnviarArticuloModel model;

	/**
	 * Antes de cada prueba se crea una base de datos limpia con los datos necesarios
	 * para garantizar la integridad referencial.
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

			// Insertar conferencia y track asociado
			"INSERT INTO Conferencia(idConferencia, nombre, deadlineEnvio, deadlineDiscusion, deadlineRevision) " +
			"VALUES (1, 'ConfTest', '2025-06-01', '2025-06-15', '2025-06-30')",

			"INSERT INTO Track(idTrack, idConferencia, nombre, palabrasClave) " +
			"VALUES (1, 1, 'TrackTest', 'IA')",

			// Insertar usuario válido
			"INSERT INTO Usuario(email, nombre, organizacion, grupoInvestigacion) " +
			"VALUES ('autor@uniovi.es', 'Autor Uno', 'Uni', 'GI')",

			// Insertar artículo con título duplicado para CP4
			"INSERT INTO Articulo(idArticulo, idTrack, titulo, palabrasClave, palabrasClaveTrack, resumen, nombreFichero, fechaEnvio) " +
			"VALUES (99, 1, 'Duplicado', 'IA', 'IA', 'Resumen', 'fichero.pdf', '2025-05-01')"
		});
	}

	/**
	 * Prueba principal parametrizada. Se prueba la inserción de un artículo nuevo
	 * con distintas combinaciones de autores y títulos.
	 */
	@Test
	@Parameters(method = "getCasosEnvio")
	public void testEnvioArticulo(String titulo, ArrayList<AutorDTO> autores, boolean debeFallar) {

		if (debeFallar) {
			// Si se espera fallo, comprobamos que se lanza excepción
			assertThrows(Exception.class, () -> {
				model.enviarArticulo(1, titulo, "clave1", "IA", "Resumen", "ficheroX.pdf", autores);
			});
		} else {
			// Si se espera éxito, se verifica que no se lanza excepción
			try {
				model.enviarArticulo(1, titulo, "clave1", "IA", "Resumen", "ficheroX.pdf", autores);
			} catch (Exception e) {
				fail("No debería fallar: " + e.getMessage());
			}
		}
	}

	/**
	 * Casos de prueba:
	 * - CP1: Todo válido
	 * - CP2: Sin autores
	 * - CP3: Autor sin email
	 * - CP4: Título duplicado
	 */
	@SuppressWarnings("unused")
	private Object[] getCasosEnvio() {
		ArrayList<AutorDTO> autorValido = new ArrayList<>();
		autorValido.add(new AutorDTO("autor@uniovi.es", "Autor Uno", "Uni", "GI"));

		ArrayList<AutorDTO> sinAutores = new ArrayList<>();

		ArrayList<AutorDTO> autorSinEmail = new ArrayList<>();
		autorSinEmail.add(new AutorDTO("", "Nombre", "Uni", "GI"));

		return new Object[] {
			new Object[] { "Titulo OK", autorValido, false },            // CP1
			new Object[] { "Duplicado", autorValido, true },             // CP4
			new Object[] { "Nuevo sin autores", sinAutores, true },      // CP2
			new Object[] { "Otro sin email", autorSinEmail, true }       // CP3
		};
	}
}
