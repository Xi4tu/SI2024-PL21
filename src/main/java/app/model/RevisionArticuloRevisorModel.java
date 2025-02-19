package app.model;

import giis.demo.util.Database;

public class RevisionArticuloRevisorModel {
	
	private Database db = new Database();
	
	public void insertarRevision(String nombre, String descripcion, String fechaInicio, String fechaFinSocios, String fechaFinNoSocios) {
		String sql = "INSERT INTO periodoInscripcion(nombre,descripcion,fechaInicio,fechaFinSocios,fechaFinNoSocios) "
				+ "values(?,?,?,?,?)";
		db.executeUpdate(sql, nombre,descripcion,fechaInicio,fechaFinSocios,fechaFinNoSocios);
	}

}
