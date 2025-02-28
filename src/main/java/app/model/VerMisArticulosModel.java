package app.model;

import java.util.ArrayList;
import java.util.List;

import app.dto.ArticuloDTOlite;
import app.dto.AutorDTO;
import giis.demo.util.Database;
import giis.demo.util.DbUtil;

public class VerMisArticulosModel {
	
	private Database db = new Database();
	private int enviador = 0;

	public DbUtil getDbUtil() {
		return db;
	}

	
	//Aqui mis metodos
	
	//Metodo que devuelve una lista de articulos DTO que ha enviado un autor
	public List<ArticuloDTOlite> obtenerArticulos(String email) {
	    // Consulta SQL con JOIN para obtener los datos de los artículos del usuario
	    String sql = "SELECT a.* " +
	                 "FROM Articulo a " +
	                 "JOIN Articulo_Usuario au ON a.idArticulo = au.idArticulo " +
	                 "WHERE au.emailUsuario = ?";
	    // Ejecutar la consulta y mapear el resultado a un ArticuloDTO en la lista
	    List<ArticuloDTOlite> articulos = db.executeQueryPojo(ArticuloDTOlite.class, sql, email);
	    
	    return articulos;
	}

	// Metodo que devuelve el autor DTO que ha enviado un articulo
	public AutorDTO quienEnvia(int id) {
		// Consulta SQL con JOIN para obtener los datos del autor que ha enviado el artículo
		String sql = "SELECT u.* " +
					 "FROM Usuario u " +
					 "JOIN Articulo_Usuario au ON u.email = au.emailUsuario " +
					 "WHERE au.idArticulo = ? AND au.esEnviador = 1";
		// Ejecutar la consulta y mapear el resultado a un AutorDTO
		List<AutorDTO> autores = db.executeQueryPojo(AutorDTO.class, sql, id);
		return autores.get(0);
	}

	// Metodo que devuelve una lista de autores DTO que han escrito un articulo
	public List<AutorDTO> obtenerAutores(int id) {
		// Consulta SQL con JOIN para obtener los datos de los autores del artículo
		String sql = "SELECT u.* " +
					 "FROM Usuario u " +
					 "JOIN Articulo_Usuario au ON u.email = au.emailUsuario " +
					 "WHERE au.idArticulo = ?";
		// Ejecutar la consulta y mapear el resultado a una lista de AutorDTO
		List<AutorDTO> autores = db.executeQueryPojo(AutorDTO.class, sql, id);
		return autores;
	}

	// Metodo que devuelve todos los articulos enviados por un autor
	public List<ArticuloDTOlite> obtenerArticulosEnviados(String email) {
	    String sql = "SELECT a.* " +
	                 "FROM Articulo a " +
	                 "JOIN Articulo_Usuario au ON a.idArticulo = au.idArticulo " +
	                 "WHERE au.emailUsuario = ? " +
	                 "AND au.esEnviador = 1";
	    List<ArticuloDTOlite> articulos = db.executeQueryPojo(ArticuloDTOlite.class, sql, email);
	    return articulos;
	}


	
}