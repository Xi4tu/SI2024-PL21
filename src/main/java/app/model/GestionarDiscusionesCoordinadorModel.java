package app.model;

import giis.demo.util.DbUtil;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import app.dto.ArticuloDiscusionDTO;
import app.dto.RevisionArticuloRevisionDTO;
import giis.demo.util.Database;

/**
 * Modelo para la gestión de discusiones por parte del coordinador.
 * 
 * <p>
 * Esta clase proporciona métodos para:
 * <ul>
 *   <li>Obtener los artículos aptos para discusión, es decir, aquellos que cumplen
 *       criterios específicos y que aún no han sido puestos en discusión.</li>
 *   <li>Obtener todas las revisiones asociadas a los artículos aptos para discusión.</li>
 *   <li>Poner un artículo en discusión, lo que implica:
 *     <ol>
 *       <li>Insertar una entrada en la tabla <code>Discusion</code> con el <code>idArticulo</code>.</li>
 *       <li>Recuperar el <code>idDiscusion</code> recién generado.</li>
 *       <li>Obtener los correos de los revisores asociados al artículo (desde la tabla <code>Revision</code>).</li>
 *       <li>Insertar entradas en la tabla <code>Usuario_Discusion</code> asociando cada revisor al <code>idDiscusion</code>.</li>
 *     </ol>
 *   </li>
 * </ul>
 * </p>
 * 
 * <p>
 * Utiliza una instancia de {@link Database} para acceder a la base de datos mediante la abstracción
 * provista por {@link DbUtil}.
 * </p>
 * 
 * @see DbUtil
 * @see Database
 */
public class GestionarDiscusionesCoordinadorModel {
	
	private Database db = new Database();
	
	/**
	 * Obtiene la instancia de {@link DbUtil} utilizada para acceder a la base de datos.
	 *
	 * @return Instancia de {@link DbUtil} que facilita la interacción con la base de datos.
	 */
	public DbUtil getDbUtil() {
		return db;
	}
	
	/**
	 * Obtiene una lista de artículos aptos para discusión.
	 * 
	 * <p>
	 * Un artículo se considera apto para discusión si cumple alguno de los siguientes criterios:
	 * <ul>
	 *   <li><code>valoracionGlobal = 1</code>, <code>decisionRevisor = -2</code> y <code>nivelExperto</code> en ('Normal', 'Medio', 'Alto').</li>
	 *   <li><code>valoracionGlobal = 1</code>, <code>decisionRevisor = -1</code> y <code>nivelExperto</code> en ('Medio', 'Alto').</li>
	 *   <li><code>valoracionGlobal = 0</code>, <code>decisionRevisor = 2</code> y <code>nivelExperto = 'Alto'</code>.</li>
	 *   <li><code>valoracionGlobal = 0</code>, <code>decisionRevisor = -1</code> y <code>nivelExperto</code> en ('Normal', 'Bajo').</li>
	 * </ul>
	 * Además, se excluyen aquellos artículos que ya están en discusión (presentes en la tabla <code>Discusion</code>).
	 * </p>
	 *
	 * @return Lista de objetos {@link ArticuloDiscusionDTO} que representan los artículos aptos para discusión.
	 */
	public List<ArticuloDiscusionDTO> getArticulosAptosDiscusion() {
	    String sql = "SELECT DISTINCT a.idArticulo AS idArticulo, "
	        + "       a.titulo AS titulo, "
	        + "       a.valoracionGlobal AS valoracionGlobal "
	        + "FROM Articulo a "
	        + "JOIN Revision r ON a.idArticulo = r.idArticulo "
	        + "WHERE ( (a.valoracionGlobal = 1 "
	        + "         AND r.decisionRevisor = -2 "
	        + "         AND r.nivelExperto IN ('Normal', 'Medio', 'Alto')) "
	        + "       OR (a.valoracionGlobal = 1 "
	        + "           AND r.decisionRevisor = -1 "
	        + "           AND r.nivelExperto IN ('Medio', 'Alto')) "
	        + "       OR (a.valoracionGlobal = 0 "
	        + "           AND r.decisionRevisor = 2 "
	        + "           AND r.nivelExperto = 'Alto') "
	        + "       OR (a.valoracionGlobal = 0 "
	        + "           AND r.decisionRevisor = -1 "
	        + "           AND r.nivelExperto IN ('Normal', 'Bajo')) ) "
	        // Evitar artículos ya presentes en la tabla Discusion
	        + "  AND a.idArticulo NOT IN (SELECT d.idArticulo FROM Discusion d);";
	    
	    return db.executeQueryPojo(ArticuloDiscusionDTO.class, sql);
	}

	/**
	 * Obtiene una lista de todas las revisiones asociadas a los artículos aptos para discusión.
	 * 
	 * <p>
	 * Se devuelven todas las revisiones de aquellos artículos que cumplen con los criterios de aptitud para discusión,
	 * excluyendo aquellos que ya están en discusión. Los datos de cada revisión se mapean a un objeto
	 * {@link RevisionArticuloRevisionDTO}.
	 * </p>
	 *
	 * @return Lista de objetos {@link RevisionArticuloRevisionDTO} que contienen la información de las revisiones.
	 */
	public List<RevisionArticuloRevisionDTO> getRevisionesArticulos() {
	    String sql = 
	        "SELECT r.idArticulo AS idArticulo, "
	      + "       u.nombre AS nombre, "
	      + "       r.nivelExperto AS nivelExperto, "
	      + "       r.decisionRevisor AS decisionRevisor, "
	      + "       r.comentariosParaCoordinador AS comentariosParaCoordinador "
	      + "FROM Revision r "
	      + "JOIN Usuario u ON r.emailUsuario = u.email "
	      + "JOIN Articulo a ON a.idArticulo = r.idArticulo "
	      + "WHERE a.idArticulo IN ( "
	      + "    SELECT DISTINCT a2.idArticulo "
	      + "    FROM Articulo a2 "
	      + "    JOIN Revision r2 ON a2.idArticulo = r2.idArticulo "
	      + "    WHERE ( (a2.valoracionGlobal = 1 "
	      + "             AND r2.decisionRevisor = -2 "
	      + "             AND r2.nivelExperto IN ('Normal', 'Medio', 'Alto')) "
	      + "           OR (a2.valoracionGlobal = 1 "
	      + "               AND r2.decisionRevisor = -1 "
	      + "               AND r2.nivelExperto IN ('Medio', 'Alto')) "
	      + "           OR (a2.valoracionGlobal = 0 "
	      + "               AND r2.decisionRevisor = 2 "
	      + "               AND r2.nivelExperto = 'Alto') "
	      + "           OR (a2.valoracionGlobal = 0 "
	      + "               AND r2.decisionRevisor = -1 "
	      + "               AND r2.nivelExperto IN ('Normal', 'Bajo')) ) "
	      + "      AND a2.idArticulo NOT IN (SELECT d.idArticulo FROM Discusion d) "
	      + ") "
	      + "ORDER BY r.idArticulo"; 

	    return db.executeQueryPojo(RevisionArticuloRevisionDTO.class, sql);
	}
	
	/**
	 * Pone un artículo en discusión.
	 * 
	 * <p>
	 * Para poner un artículo en discusión se realiza lo siguiente:
	 * <ol>
	 *   <li>Se inserta una nueva entrada en la tabla <code>Discusion</code> con el <code>idArticulo</code>
	 *       proporcionado.</li>
	 *   <li>Se recupera el <code>idDiscusion</code> recién generado utilizando
	 *       <code>last_insert_rowid()</code> en la misma conexión.</li>
	 *   <li>Se obtienen los correos de todos los revisores asociados a dicho artículo desde la tabla <code>Revision</code>.</li>
	 *   <li>Se insertan entradas en la tabla <code>Usuario_Discusion</code> para cada revisor, asociándolos al
	 *       <code>idDiscusion</code> obtenido.</li>
	 * </ol>
	 * Todas estas operaciones se realizan en la misma conexión para asegurar que el método
	 * <code>last_insert_rowid()</code> devuelva el id correcto.
	 * </p>
	 * 
	 * @param idArticulo Identificador del artículo que se desea poner en discusión.
	 * @return <code>true</code> si el artículo se puso en discusión correctamente; <code>false</code> en caso de error.
	 */
	public boolean ponerEnDiscusion(int idArticulo) {
	    Connection conn = null;
	    try {
	        // 1) Obtenemos la conexión manualmente de la instancia db.
	        conn = db.getConnection();
	        QueryRunner qr = new QueryRunner();

	        // 2) Insertamos el artículo en discusión en la tabla Discusion.
	        String sqlInsertDiscusion = "INSERT INTO Discusion (idArticulo) VALUES (?)";
	        qr.update(conn, sqlInsertDiscusion, idArticulo);

	        // 3) Recuperamos el idDiscusion recién generado usando last_insert_rowid() en la misma conexión.
	        String sqlLastId = "SELECT last_insert_rowid() AS id";
	        List<Map<String, Object>> result = qr.query(conn, sqlLastId, new MapListHandler());
	        if (result.isEmpty()) {
	            // No se pudo recuperar el id generado.
	            return false;
	        }
	        long idDiscusion = ((Number) result.get(0).get("id")).longValue();

	        // 4) Obtenemos los correos de todos los revisores asociados a este artículo.
	        String sqlSelectRevisores = "SELECT DISTINCT emailUsuario FROM Revision WHERE idArticulo = ?";
	        List<Map<String, Object>> revisores = qr.query(conn, sqlSelectRevisores, new MapListHandler(), idArticulo);

	        // 5) Insertamos en la tabla Usuario_Discusion cada revisor, asociándolos al idDiscusion obtenido.
	        String sqlInsertUsuarioDiscusion = "INSERT INTO Usuario_Discusion (emailUsuario, idDiscusion) VALUES (?, ?)";
	        for (Map<String, Object> row : revisores) {
	            String email = (String) row.get("emailUsuario");
	            qr.update(conn, sqlInsertUsuarioDiscusion, email, idDiscusion);
	        }
	        
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        // Cerramos la conexión.
	        DbUtils.closeQuietly(conn);
	    }
	}
}
