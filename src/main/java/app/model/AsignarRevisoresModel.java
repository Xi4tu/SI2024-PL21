package app.model;

import java.util.List;

import app.dto.ArticuloRevisionDTO;
import app.dto.AutorDTO;
import app.dto.RevisorDTO;
import giis.demo.util.Database;
import giis.demo.util.DbUtil;

public class AsignarRevisoresModel {

	
	private Database db = new Database();
	
	public DbUtil getDbUtil() {
		return db;
	}
	
	/**
     * Obtiene la lista de artículos que aún no tienen revisores asignados.
     * 
     * @return Lista de ArticuloDTO sin revisores asignados.
     */
	public List<ArticuloRevisionDTO> obtenerArticulosSinRevisores() {
	    String sql = "SELECT \r\n"
	    		+ "    a.idArticulo AS id, \r\n"
	    		+ "    a.titulo, \r\n"
	    		+ "    a.palabrasClave, \r\n"
	    		+ "    a.resumen, \r\n"
	    		+ "    a.nombreFichero, \r\n"
	    		+ "    a.fechaEnvio, \r\n"
	    		+ "    GROUP_CONCAT(u.email, ', ') AS autoresTexto\r\n"
	    		+ "FROM Articulo a\r\n"
	    		+ "LEFT JOIN Revision r ON a.idArticulo = r.idArticulo\r\n"
	    		+ "JOIN Articulo_Usuario au ON a.idArticulo = au.idArticulo\r\n"
	    		+ "JOIN Usuario u ON au.emailUsuario = u.email\r\n"
	    		+ "WHERE r.idArticulo IS NULL\r\n"
	    		+ "GROUP BY a.idArticulo;\r\n"
	    		+ "";
	    


	    return db.executeQueryPojo(ArticuloRevisionDTO.class, sql);
	}
	
	/**
	 * Obtiene la lista de artículos que ya tienen revisores asignados.
	 * 
	 * @return Lista de ArticuloDTO con revisores asignados.
	 */
	public List<ArticuloRevisionDTO> obtenerArticulosConRevisores() {
		  String sql = "SELECT \r\n"
		    		+ "    a.idArticulo AS id, \r\n"
		    		+ "    a.titulo, \r\n"
		    		+ "    a.palabrasClave, \r\n"
		    		+ "    a.resumen, \r\n"
		    		+ "    a.nombreFichero, \r\n"
		    		+ "    a.fechaEnvio, \r\n"
		    		+ "    GROUP_CONCAT(u.email, ', ') AS autoresTexto\r\n"
		    		+ "FROM Articulo a\r\n"
		    		+ "LEFT JOIN Revision r ON a.idArticulo = r.idArticulo\r\n"
		    		+ "JOIN Articulo_Usuario au ON a.idArticulo = au.idArticulo\r\n"
		    		+ "JOIN Usuario u ON au.emailUsuario = u.email\r\n"
		    		+ "WHERE r.idArticulo IS not NULL\r\n"
		    		+ "GROUP BY a.idArticulo;\r\n"
		    		+ "";
	    return db.executeQueryPojo(ArticuloRevisionDTO.class, sql);
	}
    /**
     * Obtiene la lista de revisores disponibles para un artículo específico.
     *
     * @param idArticulo ID del artículo para el que se buscan revisores.
     * @return Lista de revisores disponibles.
     */
	public List<RevisorDTO> obtenerRevisoresDisponibles(int idArticulo) {
	
	    
	    String sql = "SELECT u.email, u.nombre, u.organizacion, u.grupoInvestigacion "
	            + "FROM Usuario u "
	            + "JOIN Usuario_Rol ur ON u.email = ur.emailUsuario "
	            + "JOIN Rol r ON ur.idRol = r.idRol "
	            + "WHERE r.rol = 'Revisor' "
	            + "AND u.email NOT IN ( "
	            + "    SELECT au.emailUsuario "
	            + "    FROM Articulo_Usuario au "
	            + "    WHERE au.idArticulo = ? "
	            + ") "
	            + "AND u.grupoInvestigacion NOT IN ( "
	            + "    SELECT a.grupoInvestigacion "
	            + "    FROM Articulo_Usuario au "
	            + "    JOIN Usuario a ON au.emailUsuario = a.email "
	            + "    WHERE au.idArticulo = ? "
	            + ") "
	            + "AND u.email NOT IN ( "
	            + "    SELECT r.emailUsuario "
	            + "    FROM Revision r "
	            + "    WHERE r.idArticulo = ? "
	            + ")";


	    // Ejecutar la consulta con el idArticulo
	    return db.executeQueryPojo(RevisorDTO.class, sql, idArticulo, idArticulo, idArticulo);
	}

    
	/**
	 * Asigna un revisor a un artículo.
	 * 
	 * @param idArticulo Identificador del artículo.
	 * @param emailRevisor Correo electrónico del revisor.
	 */
	public void asignarRevisor(int idArticulo, String emailUsuario) {
		String sql = "INSERT INTO Revision (idArticulo, emailUsuario) VALUES (?, ?)";
		db.executeUpdate(sql, idArticulo, emailUsuario);
		}
	
	/**
	 * Eliminar un revisor de un artículo.
	 * 
	 * @param idArticulo Identificador del artículo.
	 * @param emailRevisor Correo electrónico del revisor.
	 */
	public void eliminarRevisor(int idArticulo, String emailUsuario) {
		String sql = "DELETE FROM Revision WHERE idArticulo = ? AND emailUsuario = ?";
		db.executeUpdate(sql, idArticulo, emailUsuario);
	}
	
	
	//Obtener la informacion del autor en un autorDTO
	public AutorDTO obtenerAutor(String email) {
		
		String sql = "SELECT * FROM Usuario WHERE email = ?";
		
		// Ejecuta la consulta y obtiene la lista de AutorDTOs
		List<AutorDTO> listaAutores = db.executeQueryPojo(AutorDTO.class, sql, email);
	    return listaAutores.get(0);		
	}
	
	//Obtener los revisores de un articulo
	public List<RevisorDTO> obtenerRevisoresDeArticulo(int idArticulo) {
	    String sql = "SELECT DISTINCT u.email, u.nombre, u.organizacion, u.grupoInvestigacion " +
	                 "FROM Usuario u " +
	                 "JOIN Revision r ON u.email = r.emailUsuario " +
	                 "WHERE r.idArticulo = ?";

	    return db.executeQueryPojo(RevisorDTO.class, sql, idArticulo);
	}

	
		
	
	
}
