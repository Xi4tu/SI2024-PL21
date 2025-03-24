package app.model;

import java.util.List;
import java.util.Map;

import app.dto.AccederDiscusionDTO;
import app.dto.AnotacionesDTO;
import giis.demo.util.Database;
import giis.demo.util.DbUtil;

/**
 * Modelo para gestionar el acceso a las discusiones de artículos.
 * 
 * <p>
 * Esta clase se encarga de obtener la información necesaria para que el revisor 
 * acceda a los artículos en discusión y a las anotaciones asociadas, además de 
 * actualizar la decisión del revisor y marcar cuando este se ha mantenido firme.
 * </p>
 * 
 * <p>
 * Las restricciones principales para las consultas son:
 * <ul>
 *   <li>El usuario (revisor) debe estar asignado al artículo (se verifica en la tabla Revision y Usuario_Discusion).</li>
 *   <li>El artículo debe estar en discusión (existe una entrada en la tabla Discusion) y la fecha actual debe ser menor o igual 
 *       que la deadlineDiscusion de la conferencia correspondiente.</li>
 * </ul>
 * </p>
 *
 */
public class AccederDiscusionModel {
	
	private Database db = new Database();

	/**
	 * Retorna el objeto DbUtil para ejecutar consultas.
	 *
	 * @return Un objeto {@link DbUtil} para facilitar la interacción con la base de datos.
	 */
	public DbUtil getDbUtil() {
		return db;
	}
	
	/**
	 * Obtiene los artículos en discusión en los que el usuario (revisor) coincide con el email proporcionado
	 * y la conferencia asociada aún no ha pasado su deadline de discusión.
	 * 
	 * <p>
	 * La consulta retorna la lista mapeada a {@link AccederDiscusionDTO} con los siguientes campos:
	 * <ul>
	 *   <li><code>idArticulo</code>: Identificador del artículo.</li>
	 *   <li><code>titulo</code>: Título del artículo.</li>
	 *   <li><code>decisionRevisor</code>: Valor numérico de la decisión del revisor (2, 1, -1, -2).</li>
	 *   <li><code>mantenerseFirme</code>: Valor entero (1 si el revisor está firme, 0 en caso contrario).</li>
	 * </ul>
	 * </p>
	 *
	 * @param email Correo electrónico del revisor.
	 * @param fecha Fecha actual en formato "yyyy-MM-dd".
	 * @return Lista de {@link AccederDiscusionDTO} que cumplen las restricciones.
	 */
	public List<AccederDiscusionDTO> getArticulos(String email, String fecha) {
	    String sql = 
	        "SELECT a.idArticulo AS idArticulo, " +
	        "       a.titulo AS titulo, " +
	        "       CAST(r.decisionRevisor AS INT) AS decisionRevisor, " +
	        "       ud.mantenerseFirme AS mantenerseFirme " +
	        "  FROM Articulo a " +
	        "  JOIN Track t ON a.idTrack = t.idTrack " +
	        "  JOIN Conferencia c ON t.idConferencia = c.idConferencia " +
	        "  JOIN Revision r ON r.idArticulo = a.idArticulo " +
	        "  JOIN Discusion d ON d.idArticulo = a.idArticulo " +
	        "  JOIN Usuario_Discusion ud ON ud.idDiscusion = d.idDiscusion " +
	        "       AND ud.emailUsuario = r.emailUsuario " +
	        " WHERE r.emailUsuario = ? " +
	        "   AND c.deadlineDiscusion >= ? ";
		
		return db.executeQueryPojo(AccederDiscusionDTO.class, sql, email, fecha);
	}
	
	/**
	 * Obtiene las anotaciones de los artículos en discusión en los que el usuario (revisor)
	 * participa y la conferencia correspondiente aún no ha pasado su deadline de discusión.
	 * 
	 * <p>
	 * La consulta retorna la lista mapeada a {@link AnotacionesDTO} con los siguientes campos:
	 * <ul>
	 *   <li><code>idArticulo</code>: Identificador del artículo.</li>
	 *   <li><code>emailUsuario</code>: Email del usuario que hizo la anotación.</li>
	 *   <li><code>comentario</code>: Texto de la anotación.</li>
	 *   <li><code>fecha</code>: Fecha de la anotación.</li>
	 *   <li><code>hora</code>: Hora de la anotación.</li>
	 * </ul>
	 * Se ordenan las anotaciones de la más reciente a la más antigua.
	 * </p>
	 *
	 * @param email Correo electrónico del revisor.
	 * @param fecha Fecha actual en formato "yyyy-MM-dd".
	 * @return Lista de {@link AnotacionesDTO} con las anotaciones correspondientes.
	 */
	public List<AnotacionesDTO> getAnotaciones(String email, String fecha) {
	    String sql =
	        "SELECT a.idArticulo AS idArticulo, " +
	        "       an.emailUsuario AS emailUsuario, " +
	        "       an.comentario AS comentario, " +
	        "       an.fecha AS fecha, " +
	        "       an.hora AS hora " +
	        "  FROM Anotacion an " +
	        "  JOIN Discusion d ON an.idDiscusion = d.idDiscusion " +
	        "  JOIN Articulo a ON a.idArticulo = d.idArticulo " +
	        "  JOIN Track t ON t.idTrack = a.idTrack " +
	        "  JOIN Conferencia c ON c.idConferencia = t.idConferencia " +
	        "  JOIN Revision r ON r.idArticulo = a.idArticulo " +
	        " WHERE r.emailUsuario = ? " +
	        "   AND c.deadlineDiscusion >= ? " +
	        " ORDER BY an.fecha DESC, an.hora DESC";

	    return db.executeQueryPojo(AnotacionesDTO.class, sql, email, fecha);
	}
	
	/**
	 * Agrega una anotación a la discusión asociada al artículo seleccionado.
	 * 
	 * <p>
	 * Realiza lo siguiente:
	 * <ul>
	 *   <li>Inserta la anotación en la tabla <code>Anotacion</code>. El campo <code>idAnotacion</code> es generado automáticamente.</li>
	 *   <li>El <code>idDiscusion</code> se obtiene mediante un subselect de la tabla <code>Discusion</code> para el artículo dado.</li>
	 * </ul>
	 * </p>
	 *
	 * @param idArticulo El identificador del artículo al que pertenece la discusión.
	 * @param email      Email del usuario que realiza la anotación.
	 * @param comentario Texto de la anotación.
	 * @param fecha      Fecha de la anotación (formato "yyyy-MM-dd").
	 * @param hora       Hora de la anotación (formato "HH:mm:ss").
	 */
	public void agregarAnotacion(int idArticulo, String email, String comentario, String fecha, String hora) {
	    String sql = 
	        "INSERT INTO Anotacion (idDiscusion, emailUsuario, comentario, fecha, hora) " +
	        "VALUES ( (SELECT d.idDiscusion " +
	        "          FROM Discusion d " +
	        "          WHERE d.idArticulo = ?), " +
	        "         ?, ?, ?, ? )";
	    
	    db.executeUpdate(sql, idArticulo, email, comentario, fecha, hora);
	}

	/**
	 * Actualiza la decisión del revisor para el artículo seleccionado.
	 * 
	 * <p>
	 * Se actualiza el campo <code>decisionRevisor</code> en la tabla <code>Revision</code> con el nuevo valor.
	 * </p>
	 *
	 * @param idArticulo El identificador del artículo.
	 * @param email      Email del revisor.
	 * @param value      Nuevo valor numérico de la decisión.
	 */
	public void actualizarDecisionRevisor(int idArticulo, String email, int value) {
		String sql = "UPDATE Revision " +
		             "SET decisionRevisor = ? " +
		             "WHERE idArticulo = ? " +
		             "  AND emailUsuario = ? ";
		
		db.executeUpdate(sql, value, idArticulo, email);
	}
	
	/**
	 * Actualiza el campo <code>mantenerseFirme</code> en la tabla <code>Usuario_Discusion</code>
	 * para indicar que el revisor se ha mantenido firme en su decisión.
	 * 
	 * <p>
	 * Primero se obtiene el <code>idDiscusion</code> correspondiente al artículo dado.
	 * Luego se actualiza <code>mantenerseFirme</code> a 1 para ese revisor en dicha discusión.
	 * </p>
	 *
	 * @param email      Email del revisor.
	 * @param idArticulo Identificador del artículo.
	 */
	public void mantenerDecisionFirme(String email, int idArticulo) {
	    String sqlDisc = "SELECT idDiscusion FROM Discusion WHERE idArticulo = ?";
	    List<Map<String,Object>> rows = db.executeQueryMap(sqlDisc, idArticulo);
	    if(rows.isEmpty()) return;
	    int idDiscusion = (int) rows.get(0).get("idDiscusion");

	    String sql = "UPDATE Usuario_Discusion " +
	               "   SET mantenerseFirme = 1 " +
	               " WHERE emailUsuario = ? " +
	               "   AND idDiscusion = ? ";
	    db.executeUpdate(sql, email, idDiscusion);
	}

}
