package app.model;

import java.util.List;
import java.util.Map;

import app.dto.RevisionArticuloRevisionDTO;
import app.dto.RevisionArticuloRevisorDTO;
import app.dto.RevisionAutorDTO;
import giis.demo.util.Database;
import giis.demo.util.DbUtil;

import java.util.ArrayList;
import java.util.Date;
import giis.demo.util.Util;
import app.util.UserUtil;

public class RevisorOriginalArticulosModel {

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
	
	public List<RevisionArticuloRevisorDTO> obtenerArticulosRevisados(String email) {
	    String sql = "SELECT a.idArticulo AS id, a.titulo AS titulo, a.nombreFichero AS nombreFichero " +
	                 "FROM Revision r " +
	                 "JOIN Articulo a ON r.idArticulo = a.idArticulo " +
	                 "WHERE r.emailUsuario = ? AND r.decisionRevisor IS NOT NULL";
	    return db.executeQueryPojo(RevisionArticuloRevisorDTO.class, sql, email);
	}
	
	public RevisionArticuloRevisionDTO obtenerRevision(int idArticulo, String email) {
	    String sql = "SELECT * FROM Revision WHERE idArticulo = ? AND emailUsuario = ?";
	    List<RevisionArticuloRevisionDTO> resultado = db.executeQueryPojo(RevisionArticuloRevisionDTO.class, sql, idArticulo, email);
	    return resultado.isEmpty() ? null : resultado.get(0);
	}
	
	// Para obtener comentarios para autor
	public RevisionAutorDTO obtenerRevisionAutor(int idArticulo, String email) {
	    String sql = "SELECT * FROM Revision WHERE idArticulo = ? AND emailUsuario = ?";
	    List<RevisionAutorDTO> resultado = db.executeQueryPojo(RevisionAutorDTO.class, sql, idArticulo, email);
	    return resultado.isEmpty() ? null : resultado.get(0);
	}

	// Para obtener comentarios para coordinador
	public RevisionArticuloRevisionDTO obtenerRevisionCoordinador(int idArticulo, String email) {
	    String sql = "SELECT * FROM Revision WHERE idArticulo = ? AND emailUsuario = ?";
	    List<RevisionArticuloRevisionDTO> resultado = db.executeQueryPojo(RevisionArticuloRevisionDTO.class, sql, idArticulo, email);
	    return resultado.isEmpty() ? null : resultado.get(0);
	}

	public boolean periodoRevisionActivo(int idArticulo, String email) {
	    String sql = "SELECT fechaRevision FROM Revision WHERE idArticulo = ? AND emailUsuario = ?";
	    List<Object[]> result = db.executeQueryArray(sql, idArticulo, email);

	    if (result.isEmpty() || result.get(0)[0] == null)
	        return false;

	    try {
	        String fechaRevisionStr = result.get(0)[0].toString();
	        String fechaActualStr = UserUtil.getFechaActual(); // yyyy-MM-dd

	        Date fechaRevision = Util.isoStringToDate(fechaRevisionStr);
	        Date fechaActual = Util.isoStringToDate(fechaActualStr);

	        return !fechaActual.after(fechaRevision); // true si la fecha actual ≤ fechaRevision
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public void guardarOActualizarRevision(int idArticulo, String email, String comentariosAutor, String comentariosCoord,
            String nivelExperto, int decision, String fechaActual) {
	// Verificar si ya hay revisión
	String sqlCheck = "SELECT COUNT(*) FROM Revision WHERE idArticulo = ? AND emailUsuario = ?";
	List<Object[]> check = db.executeQueryArray(sqlCheck, idArticulo, email);
	int existe = Integer.parseInt(check.get(0)[0].toString());
		
	if (existe > 0) {
		// Ya existe → actualizar
		String sqlUpdate = "UPDATE Revision SET comentariosParaAutor = ?, comentariosParaCoordinador = ?, " +
		"nivelExperto = ?, decisionRevisor = ?, fechaRevision = ? " +
		"WHERE idArticulo = ? AND emailUsuario = ?";
		db.executeUpdate(sqlUpdate, comentariosAutor, comentariosCoord, nivelExperto, decision, fechaActual, idArticulo, email);
		} else {
		// No existe → insertar
		String sqlInsert = "INSERT INTO Revision (idArticulo, emailUsuario, comentariosParaAutor, comentariosParaCoordinador, " +
		"nivelExperto, decisionRevisor, fechaRevision) " +
		"VALUES (?, ?, ?, ?, ?, ?, ?)";
		db.executeUpdate(sqlInsert, idArticulo, email, comentariosAutor, comentariosCoord, nivelExperto, decision, fechaActual);
	}
}
	public List<RevisionArticuloRevisionDTO> obtenerRevisionesDeOtrosRevisores(int idArticulo, String emailActual) {
	    String sql = "SELECT * FROM Revision WHERE idArticulo = ? AND emailUsuario <> ?";
	    return db.executeQueryPojo(RevisionArticuloRevisionDTO.class, sql, idArticulo, emailActual);
	}
	
	public List<RevisionArticuloRevisorDTO> obtenerNombreEmail(String email) {
	    String sql = "SELECT nombre FROM Usuario WHERE email = ?";
	    return db.executeQueryPojo(RevisionArticuloRevisorDTO.class, sql, email);
	}
	
	public List<RevisionArticuloRevisorDTO> obtenerEmailNombre(String nombre) {
	    String sql = "SELECT email FROM Usuario WHERE nombre = ?";
	    return db.executeQueryPojo(RevisionArticuloRevisorDTO.class, sql, nombre);
	}
	

	public List<String> obtenerRevisoresDelArticulo(int idArticulo) {
	    String sql = "SELECT DISTINCT emailUsuario FROM Revision WHERE idArticulo = ?";
	    List<Object[]> resultados = db.executeQueryArray(sql, idArticulo);

	    List<String> revisores = new ArrayList<>();
	    for (Object[] fila : resultados) {
	        if (fila[0] != null)
	            revisores.add(fila[0].toString());
	    }

	    System.out.println("🔍 Revisores encontrados para artículo " + idArticulo + ": " + revisores);
	    return revisores;
	}
	

	public boolean periodoRevisionActivoPorConferencia(int idArticulo) {
	    String sql = "SELECT c.deadlineRevision " +
	                 "FROM Articulo a " +
	                 "JOIN Track t ON a.idTrack = t.idTrack " +
	                 "JOIN Conferencia c ON t.idConferencia = c.idConferencia " +
	                 "WHERE a.idArticulo = ?";

	    List<Object[]> resultado = db.executeQueryArray(sql, idArticulo);

	    if (resultado.isEmpty() || resultado.get(0)[0] == null)
	        return false;

	    String deadline = resultado.get(0)[0].toString();
	    String hoy = UserUtil.getFechaActual(); // usa tu método aquí

	    return hoy.compareTo(deadline) <= 0;
	}

	public List<RevisionArticuloRevisorDTO> obtenerArticulosColaboradores(String email) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<RevisionArticuloRevisorDTO> obtenerCooperadores(String email) {
		String sql = "SELECT A.idArticulo AS id, A.titulo, c.nombre " +
                "FROM Articulo A " +
                "JOIN Colaboradores C ON A.titulo = C.titulo " +
                "WHERE C.nombreRevisor = ? " + 
                "AND C.estado = 'Aceptado'";
	    return db.executeQueryPojo(RevisionArticuloRevisorDTO.class, sql, email);
	}
	
	public List<RevisionArticuloRevisorDTO> obtenerComentarios(int idArticulo, String email) {
		String sql = "SELECT comentariosParaAutor, comentariosParaCoordinador " +
	               "FROM Revision " +
	               "WHERE idArticulo = ? AND emailUsuario = ?";
	    return db.executeQueryPojo(RevisionArticuloRevisorDTO.class, sql, idArticulo, email);
	}
	
	public List<RevisionArticuloRevisorDTO> obtenerNombreRevisor(String nombre) {
		String sql = "SELECT nombreRevisor AS nombre " +
	            "FROM Colaboradores c " +
	            "WHERE c.nombre = ?";
	    return db.executeQueryPojo(RevisionArticuloRevisorDTO.class, sql, nombre);
	}
	
	public List<RevisionArticuloRevisorDTO> obtenerNombreSubrevisor(String nombre) {
		String sql = "SELECT nombre AS nombre " +
	            "FROM Colaboradores c " +
	            "WHERE c.nombreRevisor = ?";
	    return db.executeQueryPojo(RevisionArticuloRevisorDTO.class, sql, nombre);
	}
	
	public void actualizarViejo(String comentariosParaAutor, String comentariosParaCoordinadorint, int id, String email) {
		String sqlUpdate = "UPDATE Revision " +
                "SET comentariosParaAutor = ?, " +
                "comentariosParaCoordinador = ? " +
                "WHERE idArticulo = ? " +
                "AND emailUsuario = ?";
	             ;
	    db.executeUpdate(sqlUpdate, comentariosParaAutor, comentariosParaCoordinadorint, id, email);
	}
	
	public void chatMensajes(int idArticulo, String remitente, String destinatario, String mensaje) {
	    // Obtener el número de mensaje más alto actual
	    String sqlCount = "SELECT MAX(numeroMensaje) FROM ChatMensajes WHERE idArticulo = ?";
	    List<Object[]> result = db.executeQueryArray(sqlCount, idArticulo);
	    
	    int numeroMensaje = 1; // por defecto
	    if (result != null && !result.isEmpty() && result.get(0)[0] != null) {
	        numeroMensaje = ((Number) result.get(0)[0]).intValue() + 1;
	    }

	    // Insertar el nuevo mensaje
	    String sqlInsert = "INSERT INTO ChatMensajes (idArticulo, remitente, destinatario, mensaje, numeroMensaje) VALUES (?, ?, ?, ?, ?)";
	    db.executeUpdate(sqlInsert, idArticulo, remitente, destinatario, mensaje, numeroMensaje);
	}
	
	public List<RevisionArticuloRevisorDTO> obtenerMensajesChat(int idArticulo, String remitente, String destinatario) {
	    String sql = "SELECT numeroMensaje, remitente, mensaje FROM ChatMensajes " +
	                 "WHERE idArticulo = ? AND ((remitente = ? AND destinatario = ?) OR (remitente = ? AND destinatario = ?)) " +
	                 "ORDER BY numeroMensaje ASC";
	    return db.executeQueryPojo(RevisionArticuloRevisorDTO.class, sql, idArticulo, remitente, destinatario, destinatario, remitente);
	}
	
	public List<RevisionArticuloRevisorDTO> obtenerRevisoresPendientes(String nombreRevisor) {
		String sql = "SELECT c.nombre, c.titulo, a.idArticulo AS id " +
	             "FROM Colaboradores c " +
	             "JOIN Articulo a ON c.titulo = a.titulo " +
	             "WHERE c.nombreRevisor = ? AND c.estado = 'Pendiente'";
	    return db.executeQueryPojo(RevisionArticuloRevisorDTO.class, sql, nombreRevisor);
	}

}
