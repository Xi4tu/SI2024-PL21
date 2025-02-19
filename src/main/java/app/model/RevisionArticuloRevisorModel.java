package app.model;

import java.util.List;
import java.util.Map;

import app.dto.RevisionArticuloRevisorDTO;
import giis.demo.util.Database;
import giis.demo.util.DbUtil;

public class RevisionArticuloRevisorModel {

	private Database db = new Database();

	public DbUtil getDbUtil() {
		return db;
	}

	/**
	 * Método que se encarga de actualizar una revisión que estaba pendiente en la
	 * base de datos.
	 * 
	 * @param idArticulo         ID del artículo revisado.
	 * @param revisorEmail       Email del revisor.
	 * @param comentariosAutores Comentarios para los autores.
	 * @param comentariosCoord   Comentarios para los coordinadores.
	 * @param nivelExperto       Nivel de experiencia del revisor (Alto, Medio,
	 *                           Normal, Bajo).
	 * @param decision           Decisión del revisor (2 = Aceptar Fuerte, 1 =
	 *                           Aceptar Débil, -1 = Rechazar Débil, -2 = Rechazar
	 *                           Fuerte).
	 */
	public void actualizarRevision(int idArticulo, String revisorEmail, String comentariosAutores,
			String comentariosCoord, String nivelExperto, int decision) {
		// Actualiza la revisión pendiente del revisor para el artículo específico
		String sql = "UPDATE Revision " + "SET comentariosParaAutor = ?, " + "    comentariosParaCoordinador = ?, "
				+ "    nivelExperto = ?, " + "    decisionRevisor = ?, " + "    fechaRevision = CURRENT_DATE " + // Guarda
																													// la
																													// fecha
																													// actual
																													// de
																													// revisión
				"WHERE idArticulo = ? " + "  AND emailUsuario = ? " + "  AND decisionRevisor IS NULL"; // Solo
																										// revisiones
																										// que aún no
																										// están
																										// completadas

		// Ejecuta la actualización en la base de datos
		db.executeUpdate(sql, comentariosAutores, comentariosCoord, nivelExperto, decision, idArticulo, revisorEmail);
	}

	/**
	 * Método que se encarga de obtener los artículos asignados a un revisor y que
	 * aún están pendientes de revisión. Devuelve un DTO con (id, titulo,
	 * nombreFichero) donde: - id = el id del artículo - titulo = el título del
	 * artículo - nombreFichero = el nombre del fichero del artículo
	 *
	 * @param email El correo del revisor
	 * @return Lista de RevisionArticuloRevisorDTO
	 */
	public List<RevisionArticuloRevisorDTO> obtenerArticulosAsignados(String email) {
		String sql = "SELECT a.idArticulo AS id, " + "       a.titulo AS titulo, "
				+ "       a.nombreFichero AS nombreFichero " + "FROM Revision r "
				+ "JOIN Articulo a ON r.idArticulo = a.idArticulo " + "WHERE r.emailUsuario = ? "
				+ "  AND r.decisionRevisor IS NULL"; // Solo artículos que aún no han sido evaluados

		// Ejecutamos la consulta asegurándonos de filtrar por el email del revisor.
		return db.executeQueryPojo(RevisionArticuloRevisorDTO.class, sql, email);
	}

	/**
	 * Método que se encarga de comprobar si un email está asociado al rol que se le
	 * pasa como parámetro.
	 *
	 * @param email El email a comprobar
	 * @param rol   El rol a comprobar
	 * @return True si el email está registrado con ese rol, false en caso
	 *         contrario.
	 */
	public boolean checkRol(String email, String rol) {
		// Se utiliza un alias "cnt" para facilitar el acceso al valor del COUNT(*)
		String sql = "SELECT COUNT(*) AS cnt " + "FROM Usuario_Rol ur " + "JOIN Rol r ON ur.idRol = r.idRol "
				+ "WHERE ur.emailUsuario = ? " + "  AND r.rol = ?";

		// Se ejecuta la consulta y se obtiene una lista de mapas, donde cada mapa
		// representa una fila
		List<Map<String, Object>> result = db.executeQueryMap(sql, email, rol);

		// Si se obtuvo algún resultado, extraemos el valor del conteo
		if (result != null && !result.isEmpty()) {
			Map<String, Object> row = result.get(0);
			Number count = (Number) row.get("cnt");
			return count.intValue() > 0;
		}

		// En caso de no obtener resultados, se retorna false
		return false;
	}

}
