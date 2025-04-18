package app.model;

import java.util.List;
import java.util.Map;

import app.dto.AceptarDenegarArticuloDTO;
import app.dto.PedirColaboradorDTO;
import giis.demo.util.Database;
import giis.demo.util.DbUtil;

public class PedirColaboradorModel {

	private Database db = new Database();

	public DbUtil getDbUtil() {
		return db;
	}

	public List<PedirColaboradorDTO> obtenerTrack(String emailUsuario) {
		String sql = "SELECT DISTINCT a.idTrack, u.nombre, r.emailUsuario " +
	             "FROM articulo a " +
	             "JOIN revision r ON a.idArticulo = r.idArticulo " +
	             "JOIN usuario u ON r.emailUsuario = u.email " +
	             "WHERE r.emailUsuario = ?;";
				; // Solo artículos que aún no han sido evaluados
		// Ejecutamos la consulta asegurándonos de filtrar por el email del revisor.
		return db.executeQueryPojo(PedirColaboradorDTO.class, sql, emailUsuario);


	}

	public List<PedirColaboradorDTO> obtenerTrackNombre(String nombreRevisor) {
		String sql = "SELECT DISTINCT idTrack FROM articulo a " + 
	             "JOIN revision r ON a.idArticulo = r.idArticulo " +
				 "JOIN usuario u ON u.email = r.emailUsuario\r\n " + 
	             "WHERE nombre = ? "
				; // Solo artículos que aún no han sido evaluados
		// Ejecutamos la consulta asegurándonos de filtrar por el email del revisor.
		return db.executeQueryPojo(PedirColaboradorDTO.class, sql, nombreRevisor);


	}

	public List<PedirColaboradorDTO> obtenerRevisores(int idTrack, int idArticulo) {
		String sql = "SELECT DISTINCT r.emailUsuario " +
                "FROM revision r " +
                "JOIN articulo a ON r.idArticulo = a.idArticulo " +
                "WHERE a.idTrack = ? " +
                "AND r.emailUsuario NOT IN ( " +
                "    SELECT DISTINCT r2.emailUsuario " +
                "    FROM revision r2 " +
                "    WHERE r2.idArticulo = ? " +
                ")";  
				; // Solo artículos que aún no han sido evaluados
		// Ejecutamos la consulta asegurándonos de filtrar por el email del revisor.
		return db.executeQueryPojo(PedirColaboradorDTO.class, sql, idTrack, idArticulo);


	}

	public List<PedirColaboradorDTO> obtenerDeicision(String emailUsuario, int idArticulo) {
		String sql = "SELECT p.decision " +
	             "FROM Usuario_Preferencia up " +
	             "JOIN Preferencia p ON up.idPreferencia = p.idPreferencia " +
	             "WHERE up.emailUsuario = ? AND p.idArticulo = ?";   
				; // Solo artículos que aún no han sido evaluados
		// Ejecutamos la consulta asegurándonos de filtrar por el email del revisor.
		return db.executeQueryPojo(PedirColaboradorDTO.class, sql, emailUsuario, idArticulo);
	}
	
	public void actualizarRevisor(String nombre, int idArticulo) {
		String sql =  "UPDATE Articulo " +
					  "SET revisorColaborador = ? " +
					  "WHERE idArticulo = ?";
	    db.executeUpdate(sql, nombre, idArticulo);
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

	public List<PedirColaboradorDTO> obtenerRevisoresAsignado(int idArticulo) {
		String sql =  "SELECT revisorColaborador " +
					  "FROM Articulo " +
				      "WHERE idArticulo = ?";
		return db.executeQueryPojo(PedirColaboradorDTO.class, sql, idArticulo);
	}

}
