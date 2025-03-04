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
}
