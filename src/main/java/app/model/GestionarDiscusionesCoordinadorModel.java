package app.model;

import giis.demo.util.DbUtil;
import giis.demo.util.Database;

public class GestionarDiscusionesCoordinadorModel {
	
	private Database db = new Database();
	
	/**
	 * Obtiene la instancia de {@link DbUtil} utilizada para acceder a la base de
	 * datos.
	 *
	 * @return Instancia de {@link DbUtil} que facilita la interacción con la base
	 *         de datos.
	 */
	public DbUtil getDbUtil() {
		return db;
	}
}
