package app.model;

import java.util.ArrayList;
import java.util.List;

import app.dto.ArticuloDTO;
import app.dto.ArticuloDTOlite;
import app.dto.AutorDTO;
import app.dto.ConferenciaDTO;
import app.dto.TrackDTO;
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
	public List<ArticuloDTO> obtenerArticulos(String email) {
	    // Consulta SQL con JOIN para obtener los datos de los artículos del usuario
	    String sql = "SELECT a.* " +
	                 "FROM Articulo a " +
	                 "JOIN Articulo_Usuario au ON a.idArticulo = au.idArticulo " +
	                 "WHERE au.emailUsuario = ?";
	    // Ejecutar la consulta y mapear el resultado a un ArticuloDTO en la lista
	    List<ArticuloDTO> articulos = db.executeQueryPojo(ArticuloDTO.class, sql, email);
	    
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
	public List<ArticuloDTO> obtenerArticulosEnviados(String email) {
	    String sql = "SELECT a.* " +
	                 "FROM Articulo a " +
	                 "JOIN Articulo_Usuario au ON a.idArticulo = au.idArticulo " +
	                 "WHERE au.emailUsuario = ? " +
	                 "AND au.esEnviador = 1";
	    List<ArticuloDTO> articulos = db.executeQueryPojo(ArticuloDTO.class, sql, email);
	    return articulos;
	}
	

	// Metodo que devuelve el trackDTO pasado por id
	public TrackDTO obtenerTrackPorId(int idTrack) {
		// Consulta SQL para obtener los datos del track
		String sql = "SELECT * FROM Track WHERE idTrack = ?";
		// Ejecutar la consulta y mapear el resultado a un TrackDTO
		List<TrackDTO> tracks = db.executeQueryPojo(TrackDTO.class, sql, idTrack);
		return tracks.get(0);
	}
	
	
	// Metodo booleano que comprueba si la fecha de hoy es anterior al deadline del track del articulo pasado por Id
	public boolean fechaAntesDeDeadline(int idTrack) {
		//Con la id del track, obtengo el id de la conferencia
		String sql = "SELECT idConferencia FROM Track WHERE idTrack = ?";
		List<TrackDTO> tracks = db.executeQueryPojo(TrackDTO.class, sql, idTrack);
		String idConferencia = tracks.get(0).getIdConferencia();
		// Con la id de la conferencia, obtengo la fecha de su deadline	
		sql = "SELECT deadlineEnvio FROM Conferencia WHERE idConferencia = ?";
		List<ConferenciaDTO> conferencias = db.executeQueryPojo(ConferenciaDTO.class, sql, idConferencia);
		String deadline = conferencias.get(0).getDeadlineEnvio();
		// Devuelve positivo si la fecha actual es anterior a la fecha de deadline
		return fecha().compareTo(deadline) < 0;		
	}
	
	
	// Metodo que devuelve la fecha actual como string en formato "yyyy-MM-dd"
	public String fecha() {
		java.util.Date fecha = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(fecha);
	}
	


	
}