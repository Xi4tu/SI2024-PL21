package app.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import app.dto.AutorDTO;
import app.model.EnviarArticuloModel;
import giis.demo.util.Database;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class TestEnviarArticulo {

	private static Database db = new Database();
	private EnviarArticuloModel model;

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
			"VALUES ('autor@uniovi.es', 'Autor Uno', 'Uni', 'GI')",

			"INSERT INTO Articulo(idArticulo, idTrack, titulo, palabrasClave, palabrasClaveTrack, resumen, nombreFichero, fechaEnvio) " +
			"VALUES (99, 1, 'Duplicado', 'IA', 'IA', 'Resumen', 'fichero.pdf', '2025-05-01')"
		});
	}

	@Test
	@Parameters(method = "getCasosEnvio")
	public void testEnvioArticulo(String titulo, ArrayList<AutorDTO> autores, boolean debeFallar) {
		
		if (debeFallar) {
			assertThrows(Exception.class, () -> {
				model.enviarArticulo(1, titulo, "clave1", "IA", "Resumen", "ficheroX.pdf", autores);
			});
		} else {
			try {
				model.enviarArticulo(1, titulo, "clave1", "IA", "Resumen", "ficheroX.pdf", autores);
			} catch (Exception e) {
				fail("No debería fallar: " + e.getMessage());
			}
		}
	}

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
