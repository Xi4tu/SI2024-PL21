package app.model;

import java.util.List;
import java.util.Map;

import app.dto.AccederDiscusionDTO;
import app.dto.AnotacionesDTO;
import app.dto.RevisionArticuloRevisionDTO;
import giis.demo.util.Database;
import giis.demo.util.DbUtil;

public class ParticiparDiscusionesCoordModel {

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
     * Obtiene los artículos en discusión (con discusión abierta).
     * <p>
     * Se seleccionan los artículos que tengan discusión abierta (d.isCerrada = 0) y se añade el campo
     * deadlineDiscusion para permitir comparar si ya ha finalizado.
     * </p>
     *
     * @param email Ignorado para coordinador.
     * @param fecha Ignorado para coordinador.
     * @return Lista de {@link AccederDiscusionDTO} con los artículos en discusión.
     */
    public List<AccederDiscusionDTO> getArticulos(String email, String fecha) {
        String sql = "SELECT a.idArticulo AS idArticulo, " +
                     "       a.titulo AS titulo, " +
                     "       MIN(CAST(r.decisionRevisor AS INT)) AS decisionRevisor, " +
                     "       MIN(ud.mantenerseFirme) AS mantenerseFirme, " +
                     "       MIN(c.deadlineDiscusion) AS deadlineDiscusion " +
                     "FROM Articulo a " +
                     "JOIN Track t ON a.idTrack = t.idTrack " +
                     "JOIN Conferencia c ON t.idConferencia = c.idConferencia " +
                     "JOIN Revision r ON r.idArticulo = a.idArticulo " +
                     "JOIN Discusion d ON d.idArticulo = a.idArticulo " +
                     "JOIN Usuario_Discusion ud ON ud.idDiscusion = d.idDiscusion " +
                     "WHERE d.isCerrada = 0 " +
                     "GROUP BY a.idArticulo, a.titulo";
        return db.executeQueryPojo(AccederDiscusionDTO.class, sql);
    }


    /**
     * Obtiene las anotaciones de los artículos en discusión para el coordinador.
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
     * @param fecha Fecha actual en formato "yyyy-MM-dd" (ya no se utiliza en este caso).
     * @return Lista de {@link AnotacionesDTO} con las anotaciones correspondientes.
     */
    public List<AnotacionesDTO> getAnotaciones(String email, String fecha) {
        String sql =
            "SELECT a.idArticulo AS idArticulo, " +
            "       an.emailUsuario AS emailUsuario, " +
            "       an.comentario AS comentario, " +
            "       an.fecha AS fecha, " +
            "       an.hora AS hora " +
            "FROM Anotacion an " +
            "JOIN Discusion d ON an.idDiscusion = d.idDiscusion " +
            "JOIN Articulo a ON a.idArticulo = d.idArticulo " +
            "JOIN Track t ON t.idTrack = a.idTrack " +
            "JOIN Conferencia c ON c.idConferencia = t.idConferencia " +
            "JOIN Revision r ON r.idArticulo = a.idArticulo " +
            "WHERE r.emailUsuario = ? " +
            "ORDER BY an.fecha DESC, an.hora DESC";
        return db.executeQueryPojo(AnotacionesDTO.class, sql, email);
    }


    /**
     * Agrega una anotación al artículo en discusión.
     *
     * @param idArticulo El id del artículo.
     * @param email      Email del usuario que agrega la anotación.
     * @param comentario Texto de la anotación.
     * @param fecha      Fecha de la anotación.
     * @param hora       Hora de la anotación.
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
     * Actualiza la decisión del revisor para un artículo.
     *
     * @param idArticulo El id del artículo.
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
     * Actualiza el campo mantenerseFirme a 1 para el revisor en la discusión correspondiente.
     *
     * @param email      Email del revisor.
     * @param idArticulo Id del artículo.
     */
    public void mantenerDecisionFirme(String email, int idArticulo) {
        String sqlDisc = "SELECT idDiscusion FROM Discusion WHERE idArticulo = ?";
        List<Map<String, Object>> rows = db.executeQueryMap(sqlDisc, idArticulo);
        if (rows.isEmpty()) return;
        int idDiscusion = (int) rows.get(0).get("idDiscusion");

        String sql = "UPDATE Usuario_Discusion " +
                     "SET mantenerseFirme = 1 " +
                     "WHERE emailUsuario = ? " +
                     "  AND idDiscusion = ? ";
        db.executeUpdate(sql, email, idDiscusion);
    }

    /**
     * Obtiene los revisores asociados a un artículo y sus decisiones.
     *
     * @param idArticulo Id del artículo.
     * @return Lista de {@link RevisionArticuloRevisionDTO} con los datos de los revisores.
     */
    public List<RevisionArticuloRevisionDTO> getRevisoresArticulo(int idArticulo) {
        String sql = "SELECT r.idArticulo AS idArticulo, " +
                     "       u.email AS email, " +
                     "       u.nombre AS nombre, " +
                     "       r.decisionRevisor AS decisionRevisor " +
                     "FROM Revision r " +
                     "JOIN Usuario u ON r.emailUsuario = u.email " +
                     "WHERE r.idArticulo = ?";
        return db.executeQueryPojo(RevisionArticuloRevisionDTO.class, sql, idArticulo);
    }
}
