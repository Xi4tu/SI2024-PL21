package app.model;

import java.util.List;
import java.util.Map;

import app.dto.AceptarDenegarArticuloDTO;
import app.dto.GestionarSolicitudesColaboracionDTO;
import giis.demo.util.Database;
import giis.demo.util.DbUtil;

public class GestionarSolicitudesColaboracionModel {
	private Database db = new Database();

	public DbUtil getDbUtil() {
		return db;
	}

	public List<GestionarSolicitudesColaboracionDTO> obtenerColaboradores() {
		String sql =    "SELECT a.titulo, " +
	            "       a.revisorColaborador, " +
	            "       t.nombre            AS nombreTrack, " +
	            "       t.palabrasClave     AS palabrasClaveTrack, " +
	            "		a.idArticulo" +
	            "  FROM Articulo a " +
	            " INNER JOIN Track   t ON a.idTrack = t.idTrack"
				; // Solo artículos que aún no han sido evaluados

		// Ejecutamos la consulta asegurándonos de filtrar por el email del revisor.
		return db.executeQueryPojo(GestionarSolicitudesColaboracionDTO.class, sql);
	}
	
	public List<GestionarSolicitudesColaboracionDTO> obtenerColaboradores2(String nombre) {
		String sql = 
			    "SELECT " +
			    "    c.nombre AS nombreColaborador, " +
			    "    c.titulo, " +
			    "    a.idArticulo, " +
			    "    a.idTrack, " +
			    "    a.palabrasClaveTrack, " +
			    "    t.nombre AS nombreTrack " +
			    "FROM Colaboradores c " +
			    "JOIN Articulo a ON c.titulo = a.titulo " +
			    "JOIN Track t ON a.idTrack = t.idTrack " +
			    "WHERE c.nombre = ? " +
	            "AND c.estado = 'Pendiente'";
		
		return db.executeQueryPojo(GestionarSolicitudesColaboracionDTO.class, sql, nombre);
	}
	
	public List<GestionarSolicitudesColaboracionDTO> obtenerNombre(String email) {
		String sql = "SELECT nombre " + "  FROM Usuario " + " WHERE email = ?"
				; // Solo artículos que aún no han sido evaluados

		// Ejecutamos la consulta asegurándonos de filtrar por el email del revisor.
		return db.executeQueryPojo(GestionarSolicitudesColaboracionDTO.class, sql, email);
	}
	
	public void insertarSubrevisor(String nombre, int idArticulo, String titulo) {
		String sql = ""
	            + "INSERT INTO Subrevisores (nombre, idArticulo, tituloArticulo) "
	            + "VALUES (?, ?, ?)";
	    db.executeUpdate(sql, nombre, idArticulo, titulo);
	}
	
	public void actualizarEstadoColaborador(String nuevoEstado, String nombre, String titulo) {
	    String sql = "UPDATE Colaboradores SET estado = ? WHERE nombre = ? AND titulo = ?";
	    db.executeUpdate(sql, nuevoEstado, nombre, titulo);
	}

	
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
