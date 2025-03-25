package app.model;

import java.util.List;

import app.dto.ArticuloRevisionDTO;
import app.dto.LastIdDTO;
import app.dto.PreferenciaDTO;
import giis.demo.util.Database;
import giis.demo.util.DbUtil;

public class IndicarPreferenciaArticulosModel {

	
	private Database db = new Database();
	
	public DbUtil getDbUtil() {
		return db;
	}
	
	
	/**
	* Obtiene la lista de articulos del track al que pertenece el revisor
	* retocar
	* @return Lista de ArticuloDTO del track del revisor
	*/
	public List<ArticuloRevisionDTO> obtenerArticulosTrackRevisor(String email) {
	    String sql = "SELECT \r\n"
	    		+ "    a.idArticulo AS id, \r\n"
	    		+ "    a.titulo, \r\n"
	    		+ "    a.palabrasClave, \r\n"
	    		+ "    a.resumen, \r\n"
	    		+ "    a.nombreFichero, \r\n"
	    		+ "    a.fechaEnvio, \r\n"
	    		+ "    GROUP_CONCAT(u.email, ', ') AS autoresTexto\r\n"
	    		+ "FROM Articulo a\r\n"
	    		+ "JOIN Articulo_Usuario au ON a.idArticulo = au.idArticulo\r\n"
	    		+ "JOIN Usuario u ON au.emailUsuario = u.email\r\n"
	    		+ "WHERE a.idTrack = (SELECT idTrack FROM Usuario WHERE email = ?)\r\n"
	    		+ "GROUP BY a.idArticulo;\r\n"
	    		+ "";
	    
	    return db.executeQueryPojo(ArticuloRevisionDTO.class, sql, email);
	}
	
	//metodo para obtener la preferencia del articulo a partir del articulo seleccionado
	public PreferenciaDTO obtenerPreferenciaDTO(int idArticulo, String emailUsuario) {
		String sql = "SELECT p.decision AS preferencia, p.idArticulo, p.idPreferencia AS id " +
	             "FROM Preferencia p " +
	             "JOIN Usuario_Preferencia up ON p.idPreferencia = up.idPreferencia " +
	             "WHERE p.idArticulo = ? AND up.emailUsuario = ?";
	    
	    List<PreferenciaDTO> lista = db.executeQueryPojo(PreferenciaDTO.class, sql, idArticulo, emailUsuario);
	    
	    if (!lista.isEmpty()) {
	        return lista.get(0);
	    } else {
	        return null;
	    }
	}
	
	public void guardarOActualizarPreferencia(int idArticulo, String emailUsuario, String decision) {
	    PreferenciaDTO prefExistente = obtenerPreferenciaDTO(idArticulo, emailUsuario);

	    if (prefExistente != null) {
	        // Si ya existe, actualiza
	        db.executeUpdate(
	            "UPDATE Preferencia SET decision = ? WHERE idPreferencia = ?",
	            decision, prefExistente.getId()
	        );
	        System.out.println("🔄 Preferencia actualizada: ID = " + prefExistente.getId());
	    } else {
	        // Si no existe, inserta nueva preferencia Y recupera el ID insertado
	        String sqlInsert = "INSERT INTO Preferencia (decision, idArticulo) " +
	                   "VALUES (?, ?) " +
	                   "RETURNING idPreferencia AS id";

	        List<PreferenciaDTO> result = db.executeQueryPojo(PreferenciaDTO.class, sqlInsert, decision, idArticulo);
	        int idPreferencia = result.get(0).getId();

	        System.out.println("✅ Nueva preferencia insertada: ID = " + idPreferencia);

	        // Inserta la relación usuario ↔ preferencia
	        db.executeUpdate(
	            "INSERT INTO Usuario_Preferencia (emailUsuario, idPreferencia) VALUES (?, ?)",
	            emailUsuario, idPreferencia
	        );

	        System.out.println("✅ Relación creada: " + emailUsuario + " ↔ " + idPreferencia);
	    }
	}
	
	
	
	
}
