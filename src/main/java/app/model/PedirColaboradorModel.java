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

	public List<PedirColaboradorDTO> obtenerTrack(String nombreRevisor) {
		String sql = "SELECT DISTINCT idTrack FROM articulo a " + 
	             "JOIN revision r ON a.idArticulo = r.idArticulo " +
	             "WHERE emailUsuario = ? "
				; // Solo artículos que aún no han sido evaluados
		// Ejecutamos la consulta asegurándonos de filtrar por el email del revisor.
		return db.executeQueryPojo(PedirColaboradorDTO.class, sql, nombreRevisor);
		
		
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
	
	public List<PedirColaboradorDTO> obtenerRevisores(String nombreArticulo) {
		String sql = "SELECT u.nombre " + 
	             "FROM Usuario u " +  
	             "JOIN Revision r ON r.emailUsuario = u.email " +  
	             "JOIN Articulo a ON r.idArticulo = a.idArticulo " +  
	             "WHERE a.titulo = ?";
				; // Solo artículos que aún no han sido evaluados
		// Ejecutamos la consulta asegurándonos de filtrar por el email del revisor.
		return db.executeQueryPojo(PedirColaboradorDTO.class, sql, nombreArticulo);
		
		
	}
	
	public List<AceptarDenegarArticuloDTO> obtenerNivelExperto(String nombreUsuario, String tituloArticulo) {
	    String sql = "SELECT r.nivelExperto " +
	                 "FROM Revision r " +
	                 "JOIN Articulo a ON r.idArticulo = a.idArticulo " +
	                 "JOIN Usuario u ON r.emailUsuario = u.email " +
	                 "WHERE u.nombre = ? AND a.titulo = ?";

	    return db.executeQueryPojo(AceptarDenegarArticuloDTO.class, sql, nombreUsuario, tituloArticulo);
	}

	public List<AceptarDenegarArticuloDTO> obtenerDecisionRevisor(String nombreUsuario, String tituloArticulo) {
	    String sql = "SELECT r.decisionRevisor " +
	                 "FROM Revision r " +
	                 "JOIN Articulo a ON r.idArticulo = a.idArticulo " +
	                 "JOIN Usuario u ON r.emailUsuario = u.email " +
	                 "WHERE u.nombre = ? AND a.titulo = ?";

	    return db.executeQueryPojo(AceptarDenegarArticuloDTO.class, sql, nombreUsuario, tituloArticulo);
	}

	public List<AceptarDenegarArticuloDTO> obtenerTodasDecisiones(String tituloArticulo) {
	    String sql = "SELECT r.decisionRevisor " +
	                 "FROM Revision r " +
	                 "JOIN Articulo a ON r.idArticulo = a.idArticulo " +
	                 "JOIN Usuario u ON r.emailUsuario = u.email " +
	                 "WHERE a.titulo = ?";

	    return db.executeQueryPojo(AceptarDenegarArticuloDTO.class, sql, tituloArticulo);
	}
	
	public List<AceptarDenegarArticuloDTO> obtenerComentariosParaAutor(String nombreUsuario, String tituloArticulo) {
	    String sql = "SELECT r.comentariosParaAutor " +
	                 "FROM Revision r " +
	                 "JOIN Articulo a ON r.idArticulo = a.idArticulo " +
	                 "JOIN Usuario u ON r.emailUsuario = u.email " +
	                 "WHERE u.nombre = ? AND a.titulo = ?";
	    
	    return db.executeQueryPojo(AceptarDenegarArticuloDTO.class, sql, nombreUsuario, tituloArticulo);
	}
	
	public List<AceptarDenegarArticuloDTO> obtenerComentariosParaCoordinador(String nombreUsuario, String tituloArticulo) {
	    String sql = "SELECT r.comentariosParaCoordinador " +
	                 "FROM Revision r " +
	                 "JOIN Articulo a ON r.idArticulo = a.idArticulo " +
	                 "JOIN Usuario u ON r.emailUsuario = u.email " +
	                 "WHERE u.nombre = ? AND a.titulo = ?";
	    
	    return db.executeQueryPojo(AceptarDenegarArticuloDTO.class, sql, nombreUsuario, tituloArticulo);
	}
	
	public void actualizarDecisionFinal(String decisionFinal, String tituloArticulo) {
		String sql =  "UPDATE Articulo " +
					  "SET decisionFinal = ? " +
					  "WHERE titulo = ?";
	    db.executeUpdate(sql, decisionFinal, tituloArticulo);
	}
	
	public List<AceptarDenegarArticuloDTO> obtenerRevisoresPorTitulo(String tituloArticulo) {
	    String sql = "SELECT DISTINCT u.nombre " +
	                 "FROM Articulo a " +
	                 "JOIN Revision r ON a.idArticulo = r.idArticulo " +
	                 "JOIN Usuario u ON r.emailUsuario = u.email " +
	                 "WHERE a.titulo = ?";
	    
	    return db.executeQueryPojo(AceptarDenegarArticuloDTO.class, sql, tituloArticulo);
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
