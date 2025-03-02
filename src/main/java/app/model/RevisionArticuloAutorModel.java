package app.model;

import java.util.List;

import app.dto.RevisionArticuloAutorDTO;
import app.dto.RevisionAutorDTO;
import giis.demo.util.Database;
import giis.demo.util.DbUtil;

/**
 * Modelo encargado de obtener los datos relacionados con los artículos y las
 * revisiones del autor.
 * <p>
 * Esta clase proporciona métodos para obtener los artículos en los que
 * participa un autor y las revisiones asociadas a dichos artículos a través de
 * consultas a la base de datos.
 * </p>
 */
public class RevisionArticuloAutorModel {

	private Database db = new Database();

	/**
	 * Obtiene la instancia de {@link DbUtil} utilizada para acceder a la base de
	 * datos.
	 *
	 * @return Instancia de {@link DbUtil} que facilita la interacción con la base
	 *         de datos.
	 */
	public DbUtil getDbUtil() {
		return db;
	}

	/**
	 * Obtiene los artículos en los que participa el autor especificado y que tienen
	 * una decisión final de "Aceptado" o "Rechazado".
	 * <p>
	 * La consulta recupera el identificador del artículo, el título y la decisión
	 * final.
	 * </p>
	 *
	 * @param email Correo electrónico del autor.
	 * @return Lista de {@link RevisionArticuloAutorDTO} con los datos de cada
	 *         artículo.
	 */
	public List<RevisionArticuloAutorDTO> getArticulosAutor(String email) {
		String sql = "SELECT a.idArticulo AS idArticulo, " + "       a.titulo AS titulo, "
				+ "       a.decisionFinal AS decisionFinal " + "FROM Articulo a "
				+ "JOIN Articulo_Usuario au ON a.idArticulo = au.idArticulo " + "WHERE au.emailUsuario = ? "
				+ "  AND a.decisionFinal IN ('Aceptado', 'Rechazado')";

		return db.executeQueryPojo(RevisionArticuloAutorDTO.class, sql, email);
	}

	/**
	 * Obtiene las revisiones completas asociadas a los artículos en los que
	 * participa el autor especificado.
	 * <p>
	 * Se consideran revisiones completas aquellas que no contienen valores nulos en
	 * los campos: comentariosParaAutor, comentariosParaCoordinador, nivelExperto,
	 * decisionRevisor y fechaRevision. La consulta devuelve el identificador del
	 * artículo, el comentario para el autor, el nivel de experiencia y la decisión
	 * del revisor.
	 * </p>
	 *
	 * @param email Correo electrónico del autor.
	 * @return Lista de {@link RevisionAutorDTO} con los datos de cada revisión.
	 */
	public List<RevisionAutorDTO> getRevisionesArticulos(String email) {
		String sql = "SELECT a.idArticulo AS idArticulo, " + "       r.comentariosParaAutor AS comentariosParaAutor, "
				+ "       r.nivelExperto AS nivelExperto, " + "       r.decisionRevisor AS decisionRevisor "
				+ "FROM Articulo a " + "JOIN Articulo_Usuario au ON a.idArticulo = au.idArticulo "
				+ "LEFT JOIN Revision r ON a.idArticulo = r.idArticulo " + "    AND r.comentariosParaAutor IS NOT NULL "
				+ "    AND r.comentariosParaCoordinador IS NOT NULL " + "    AND r.nivelExperto IS NOT NULL "
				+ "    AND r.decisionRevisor IS NOT NULL " + "    AND r.fechaRevision IS NOT NULL "
				+ "WHERE au.emailUsuario = ? " + "  AND a.decisionFinal IN ('Aceptado', 'Rechazado')";

		return db.executeQueryPojo(RevisionAutorDTO.class, sql, email);
	}
}
