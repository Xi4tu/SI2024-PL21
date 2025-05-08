package app.tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import giis.demo.util.Database;
import app.model.GestionarDiscusionesCoordinadorModel;
import app.dto.ArticuloDiscusionDTO;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * Clase de pruebas unitarias para verificar el proceso de negocio que determina
 * si un artículo debe entrar en discusión, basado en las reglas definidas en el
 * método {@code getArticulosAptosDiscusion} del modelo {@code GestionarDiscusionesCoordinadorModel}.
 * 
 * <p>La lógica de negocio a validar incluye:</p>
 * <ul>
 *   <li>Valoración global del artículo (0 o 1)</li>
 *   <li>Tipo de revisión recibida (rechazo fuerte/débil, aceptación fuerte)</li>
 *   <li>Nivel del revisor (Bajo, Normal, Medio, Alto)</li>
 * </ul>
 * 
 * <p>Las pruebas están diseñadas usando JUnitParams para ejecutar múltiples
 * combinaciones de entrada como tests parametrizados.</p>
 */
@RunWith(JUnitParamsRunner.class)
public class TestDiscusionesControvertidas {

	private static Database db = new Database();
	private GestionarDiscusionesCoordinadorModel model;

	/**
	 * Configura una base de datos en limpio antes de cada prueba e inserta los
	 * datos mínimos necesarios para cumplir las restricciones de integridad:
	 * una conferencia, un track y varios revisores.
	 */
	@Before
	public void setUp() {
		db.createDatabase(true);
		model = new GestionarDiscusionesCoordinadorModel();

		db.executeBatch(new String[] {
			// Limpiar tablas necesarias
			"DELETE FROM Articulo", "DELETE FROM Revision", "DELETE FROM Discusion",
			"DELETE FROM Track", "DELETE FROM Conferencia", "DELETE FROM Usuario",

			// Insertar conferencia y track
			"INSERT INTO Conferencia(idConferencia, nombre, deadlineEnvio, deadlineDiscusion, deadlineRevision) "
					+ "VALUES (1, 'TestConf', '2025-06-01', '2025-06-15', '2025-06-30')",

			"INSERT INTO Track(idTrack, idConferencia, nombre, palabrasClave) "
					+ "VALUES (1, 1, 'TestTrack', 'IA,Software')",

			// Insertar revisores
			"INSERT INTO Usuario(email, nombre, organizacion, grupoInvestigacion) "
					+ "VALUES ('rev1@example.com', 'Revisor 1', 'Uni', 'GI'),"
					+ "('rev2@example.com', 'Revisor 2', 'Uni', 'GI'),"
					+ "('rev3@example.com', 'Revisor 3', 'Uni', 'GI'),"
					+ "('rev4@example.com', 'Revisor 4', 'Uni', 'GI'),"
					+ "('rev5@example.com', 'Revisor 5', 'Uni', 'GI'),"
					+ "('rev6@example.com', 'Revisor 6', 'Uni', 'GI')"
		});
	}

	/**
	 * Prueba parametrizada que verifica si un artículo debe entrar en discusión
	 * en función de su valoración global, la decisión del revisor y su nivel.
	 * 
	 * <p>Se insertan dinámicamente artículos y revisiones con los parámetros indicados
	 * y se verifica si el artículo aparece en la lista de artículos aptos para discusión.</p>
	 *
	 * @param valoracion    Valoración global del artículo (0 o 1)
	 * @param decision      Decisión del revisor (-2 rechazo fuerte, -1 débil, 2 aceptación fuerte)
	 * @param nivel         Nivel del revisor (Bajo, Normal, Medio, Alto)
	 * @param idArticulo    ID del artículo para insertar y verificar
	 * @param debeEntrar    Si se espera que el artículo aparezca en la lista de aptos
	 */
	@Test
	@Parameters({
		// valoracion, decision, nivel, idArticulo, debeEntrar
		"1, -2, Normal, 1, true",  // Rechazo fuerte y nivel normal
		"1, -1, Bajo, 2, false",   // Rechazo débil pero nivel bajo → no entra
		"0,  2, Alto, 3, true",    // Aceptación fuerte y nivel alto
		"0, -1, Normal, 4, true",  // Rechazo débil y nivel normal
		"0, -1, Bajo, 5, true",    // Rechazo débil y nivel bajo
		"0,  2, Medio, 6, false"   // Aceptación fuerte pero nivel medio → no entra
	})
	public void testArticulosAptosDiscusion(int valoracion, int decision, String nivel, int idArticulo, boolean debeEntrar) {
		// Insertar artículo válido completo
		db.executeUpdate(
			"INSERT INTO Articulo(idArticulo, idTrack, titulo, palabrasClave, palabrasClaveTrack, resumen, nombreFichero, fechaEnvio, valoracionGlobal) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
			idArticulo, 1, "Artículo " + idArticulo, "IA", "IA", "Resumen corto", "fichero" + idArticulo + ".pdf",
			"2025-05-01", valoracion
		);

		// Insertar revisión para el artículo
		db.executeUpdate(
			"INSERT INTO Revision(idArticulo, emailUsuario, decisionRevisor, nivelExperto) VALUES (?, ?, ?, ?)",
			idArticulo, "rev" + idArticulo + "@example.com", decision, nivel
		);

		// Ejecutar lógica de negocio
		List<ArticuloDiscusionDTO> articulos = model.getArticulosAptosDiscusion();

		// Verificar si el artículo aparece en la lista
		boolean encontrado = articulos.stream().anyMatch(a -> a.getIdArticulo() == idArticulo);
		assertEquals(debeEntrar, encontrado);
	}
}
