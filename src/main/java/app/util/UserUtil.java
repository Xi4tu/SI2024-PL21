package app.util;

import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import giis.demo.util.DbUtil;
import giis.demo.util.SwingUtil;

public class UserUtil {

    /**
     * Método que se encarga de comprobar que el email no llega vacío, que tiene un formato correcto
     * y que el usuario con este email puede acceder a esta funcionalidad.
     *
     * @param email  El email a comprobar.
     * @param rol    El rol requerido para acceder a la funcionalidad.
     * @param dbUtil Instancia de DbUtil para ejecutar la consulta.
     * @return True si el email es válido y el usuario tiene el rol, false en caso contrario.
     */
    public static boolean checkEmail(String email, String rol, DbUtil dbUtil) {
        // Comprobar que el email no es nulo/vacío y cumple el regex
        if (email == null || email.isEmpty() ||
                !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            SwingUtil.showMessage("No se ha podido obtener el email del usuario", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Comprobar si el usuario tiene permisos para acceder a esta funcionalidad
        if (!checkRol(email, rol, dbUtil)) {
            SwingUtil.showMessage("No tienes permisos para acceder a esta funcionalidad", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    /**
	 * Método que se encarga de comprobar si un email está registrado en la base de datos.
	 * SOLO COMPRUEBA EL EMAIL, NO EL ROL COMO LA FUNCION checkEmail
	 *
	 * @param email  El email a comprobar.
	 * @return True si el email está registrado, false en caso contrario.
	 */
    public static boolean checkFormatoEmail(String email) {
		// Comprobar que el email no es nulo/vacío y cumple el regex
		if (email == null || email.isEmpty() ||
				!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
			SwingUtil.showMessage("No se ha podido obtener el email del usuario", "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

    /**
     * Método que se encarga de comprobar si un email está asociado al rol que se le pasa como parámetro.
     *
     * @param email  El email a comprobar.
     * @param rol    El rol a comprobar.
     * @param dbUtil Instancia de DbUtil para ejecutar la consulta.
     * @return True si el email está registrado con ese rol, false en caso contrario.
     */
    private static boolean checkRol(String email, String rol, DbUtil dbUtil) {
        String sql = "SELECT COUNT(*) AS cnt " +
                     "FROM Usuario_Rol ur " +
                     "JOIN Rol r ON ur.idRol = r.idRol " +
                     "WHERE ur.emailUsuario = ? " +
                     "  AND r.rol = ?";
        
        // Ejecutar la consulta y obtener el resultado como una lista de mapas
        List<Map<String, Object>> result = dbUtil.executeQueryMap(sql, email, rol);
        
        if (result != null && !result.isEmpty()) {
            Map<String, Object> row = result.get(0);
            Number count = (Number) row.get("cnt");
            return count.intValue() > 0;
        }
        return false;
    }
    
    /**
     * Retorna la fecha actual formateada según el patrón "yyyy-MM-dd".
     * <p>
     * Este método crea una nueva instancia de {@link java.util.Date} y la formatea utilizando
     * {@link java.text.SimpleDateFormat} para obtener la fecha del día actual en el formato deseado.
     * </p>
     *
     * @return un {@link String} que representa la fecha actual en el formato "yyyy-MM-dd"
     */
    public static String getFechaActual() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    }

    /**
     * Retorna la hora actual del sistema utilizando el formato <strong>HH:mm</strong>.
     * <p>
     * El método crea una instancia de {@link java.util.Date} con la hora actual y la
     * formatea mediante {@link java.text.SimpleDateFormat} para devolver una cadena
     * en formato de 24 horas (por ejemplo, "14:35").
     * </p>
     *
     * @return un {@link String} que representa la hora actual en el formato "HH:mm"
     */
    public static String getHoraActual() {
        return new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
    }



}
