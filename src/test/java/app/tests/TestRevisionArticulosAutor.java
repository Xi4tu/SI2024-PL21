package app.tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import app.dto.RevisionArticuloAutorDTO;
import app.model.RevisionArticuloAutorModel;
import giis.demo.util.Database;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * Pruebas unitarias del proceso de negocio encargado de recuperar los artículos
 * que un usuario (autor o coautor) puede visualizar tras ser evaluados.
 * 
 * <p>
 * Solo se mostrarán los artículos:
 * <ul>
 *   <li>En los que el usuario figura como autor o coautor (esEnviador = 1 o 0).</li>
 *   <li>Y que tienen una decisión final registrada (Aceptado o Rechazado).</li>
 * </ul>
 * 
 * No deben mostrarse:
 * <ul>
 *   <li>Artículos donde la decisión es "Pendiente".</li>
 *   <li>Artículos en los que el usuario no esté relacionado como autor o coautor.</li>
 * </ul>
 */
@RunWith(JUnitParamsRunner.class)
public class TestRevisionArticulosAutor {

	private static Database db = new Database();
	private RevisionArticuloAutorModel model;

	/**
	 * Crea una base de datos limpia antes de cada prueba e inserta los datos mínimos necesarios
	 * para garantizar integridad referencial: una conferencia, un track y dos usuarios (autor y no autor).
	 */
	@Before
	public void setUp() {
		db.createDatabase(true);
		model = new RevisionArticuloAutorModel();

		// Insertamos una conferencia, un track, y dos usuarios
		db.executeBatch(new String[] {
			"DELETE FROM Articulo",
			"DELETE FROM Articulo_Usuario",
			"DELETE FROM Usuario",
			"DELETE FROM Track",
			"DELETE FROM Conferencia",

			"INSERT INTO Conferencia(idConferencia, nombre, deadlineEnvio, deadlineDiscusion, deadlineRevision) " +
			"VALUES (1, 'ConfTest', '2025-06-01', '2025-06-15', '2025-06-30')",

			"INSERT INTO Track(idTrack, idConferencia, nombre, palabrasClave) " +
			"VALUES (1, 1, 'TrackTest', 'IA')",

			"INSERT INTO Usuario(email, nombre, organizacion, grupoInvestigacion) " +
			"VALUES ('autor@example.com', 'Autor Uno', 'Uni', 'GI')," +
			"('noautor@example.com', 'No Autor', 'Uni', 'GI')"
		});
	}

	/**
	 * Prueba parametrizada que verifica qué artículos son visibles para un autor
	 * dependiendo de su relación con el artículo (autor principal, coautor o ninguno)
	 * y del estado de la decisión final del mismo.
	 * 
	 * @param email        Email del usuario que realiza la consulta
	 * @param esEnviador   Indica si el usuario está relacionado con el artículo:
	 *                     - 1 = autor principal, 0 = coautor, -1 = no está relacionado
	 * @param decision     Valor de la decisión final del artículo (Aceptado, Rechazado, Pendiente)
	 * @param idArticulo   Identificador del artículo a insertar y verificar
	 * @param esperado     true si el artículo debería ser visible para el usuario, false si no
	 */
	@Test
	@Parameters({
		// emailUsuario, esEnviador, decisionFinal, idArticulo, esperado
		"autor@example.com, 1, Aceptado, 1, true",    // autor principal, aceptado
		"autor@example.com, 0, Rechazado, 2, true",   // coautor, rechazado
		"autor@example.com, 1, Pendiente, 3, false",  // autor pero pendiente
		"autor@example.com, 0, Pendiente, 4, false",  // coautor pero pendiente
		"autor@example.com, -1, Aceptado, 5, false",  // no está relacionado
		"autor@example.com, -1, Rechazado, 6, false"  // no está relacionado
	})
	public void testArticulosAutor(String email, int esEnviador, String decision, int idArticulo, boolean esperado) {
		// Insertar artículo con los datos especificados
		db.executeUpdate(
			"INSERT INTO Articulo(idArticulo, idTrack, titulo, palabrasClave, palabrasClaveTrack, resumen, nombreFichero, fechaEnvio, decisionFinal) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
			idArticulo, 1, "Artículo " + idArticulo, "IA", "IA", "Resumen", "fichero" + idArticulo + ".pdf", "2025-05-01", decision
		);

		// Si el usuario está relacionado, se inserta como autor o coautor
		if (esEnviador != -1) {
			db.executeUpdate(
				"INSERT INTO Articulo_Usuario(idArticulo, emailUsuario, esEnviador) VALUES (?, ?, ?)",
				idArticulo, email, esEnviador
			);
		}

		// Se recuperan los artículos visibles para ese usuario y se comprueba si el actual aparece
		List<RevisionArticuloAutorDTO> articulos = model.getArticulosAutor(email);
		boolean encontrado = articulos.stream().anyMatch(a -> a.getIdArticulo() == idArticulo);
		assertEquals(esperado, encontrado);
	}
}
