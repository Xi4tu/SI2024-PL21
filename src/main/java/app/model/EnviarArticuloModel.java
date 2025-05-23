package app.model;

import java.util.ArrayList;
import java.util.List;

import app.dto.ArticuloDTO;
import app.dto.AutorDTO;
import app.dto.ConferenciaDTO;
import app.dto.TrackDTO;
import giis.demo.util.Database;
import giis.demo.util.DbUtil;

public class EnviarArticuloModel {
	
	private Database db = new Database();
	private int enviador = 0;

	public DbUtil getDbUtil() {
		return db;
	}
	
	// Enviar artícilo a la base de datos
	public void enviarArticulo(int idTrack, String titulo, String palabrasClave, String palabrasClaveTrack, String resumen, String archivo, ArrayList<AutorDTO> autores) {
		
		// Inserta el artículo en la base de datos incluyendo la fecha generada automáticamente con la funcion fecha()
		String sql = "INSERT INTO Articulo (idTrack, titulo, palabrasClave, palabrasClaveTrack, resumen, nombreFichero, fechaEnvio) VALUES (?, ?, ?, ?, ?, ?, ?)";
		db.executeUpdate(sql, idTrack, titulo, palabrasClave, palabrasClaveTrack, resumen, archivo, fecha());
		
		//Para ese articulo, insertar los autores en la tabla de Usuario y en la tabla de Articulo_Usuario
		for (AutorDTO autor : autores) {
			
			// Inserta el autor en la tabla de Usuario (si no existe ya)
			sql = "INSERT OR IGNORE INTO Usuario (email, nombre, organizacion, grupoInvestigacion) VALUES (?, ?, ?, ?)";
			db.executeUpdate(sql, autor.getEmail(), autor.getNombre(), autor.getOrganizacion(), autor.getGrupoInvestigacion());
			
			// Inserta el autor en la tabla de Usuario_Rol como AUTOR (idRol = 1) (si no existe ya)
			sql = "INSERT OR IGNORE INTO Usuario_Rol (emailUsuario, idRol) VALUES (?, 1)";
			db.executeUpdate(sql, autor.getEmail());
			
			
			// Inserta el autor en la tabla de Articulo_Usuario
			sql = "INSERT INTO Articulo_Usuario (emailUsuario, idArticulo, esEnviador) VALUES (?, (SELECT MAX(idArticulo) FROM Articulo), ?)";
			//Si es el primer autor, se marca como enviador (enviador = 1)
			if (enviador == 0) {
				db.executeUpdate(sql, autor.getEmail(), 1);
				enviador = 1;
			} else {
				db.executeUpdate(sql, autor.getEmail(), 0);
			}
		}
	}
	
	// Edita un artículo en la base de datos
	// Los siguientes campos no se pueden editar y por ende no deberia hacer falta actualizarlos:
	/*
	 * - idArticulo
	 * - fechaEnvio
	 * - Autor que enviaba el articulo
	 */
public void editarArticulo(int idArticulo, int idTrack, String titulo, String palabrasClave, String palabrasClaveTrack, String resumen, String archivo, ArrayList<AutorDTO> autores) {
		
		// Actualiza el artículo en la base de datos incluyendo la fecha de hoy (usando fecha()) al campo ultimaModificacion
		String sql = "UPDATE Articulo SET idTrack = ?, titulo = ?, palabrasClave = ?, palabrasClaveTrack = ?, resumen = ?, nombreFichero = ?, fechaModificacion = ? WHERE idArticulo = ?";
		db.executeUpdate(sql, idTrack, titulo, palabrasClave, palabrasClaveTrack, resumen, archivo, fecha(), idArticulo);
		
		// Borra los autores del artículo en la tabla de Articulo_Usuario
		sql = "DELETE FROM Articulo_Usuario WHERE idArticulo = ?";
		db.executeUpdate(sql, idArticulo);
		
		//Para ese articulo, insertar los autores en la tabla de Usuario y en la tabla de Articulo_Usuario
		for (AutorDTO autor : autores) {
			
			// Inserta el autor en la tabla de Usuario (si no existe ya)
			sql = "INSERT OR IGNORE INTO Usuario (email, nombre, organizacion, grupoInvestigacion) VALUES (?, ?, ?, ?)";
			db.executeUpdate(sql, autor.getEmail(), autor.getNombre(), autor.getOrganizacion(), autor.getGrupoInvestigacion());
			
			// Inserta el autor en la tabla de Usuario_Rol como AUTOR (idRol = 1) (si no existe ya)
			sql = "INSERT OR IGNORE INTO Usuario_Rol (emailUsuario, idRol) VALUES (?, 1)";
			db.executeUpdate(sql, autor.getEmail());
			
			// Inserta el autor en la tabla de Articulo_Usuario
			sql = "INSERT INTO Articulo_Usuario (emailUsuario, idArticulo, esEnviador) VALUES (?, ?, ?)";
			//Si es el primer autor, se marca como enviador (enviador = 1)
			if (enviador == 0) {
				db.executeUpdate(sql, autor.getEmail(), idArticulo, 1);
				enviador = 1;
			} else {
				db.executeUpdate(sql, autor.getEmail(), idArticulo, 0);
			}
		}
		
	}
	
	
	//Obtener el articuloDTO con el titulo
	public ArticuloDTO obtenerArticulo(String titulo) {
		String sql = "SELECT * FROM Articulo WHERE titulo = ?";
		List<ArticuloDTO> listaArticulos = db.executeQueryPojo(ArticuloDTO.class, sql, titulo);
		return listaArticulos.get(0);
	}
	
	//Obtener la informacion del autor en un autorDTO
	public AutorDTO obtenerAutor(String email) {
		
		String sql = "SELECT * FROM Usuario WHERE email = ?";
		
		// Ejecuta la consulta y obtiene la lista de AutorDTOs
		List<AutorDTO> listaAutores = db.executeQueryPojo(AutorDTO.class, sql, email);
	    return listaAutores.get(0);		
	}
	
	//Metodo para comprobar si un articulo ya existe en la base de datos con su titulo
	public boolean existeArticulo(String titulo) {
		String sql = "SELECT * FROM Articulo WHERE titulo = ?";
		List<AutorDTO> listaAutores = db.executeQueryPojo(AutorDTO.class, sql, titulo);
		if (listaAutores.size() > 0) { //Si la lista no esta vacia, el articulo ya existe
			return true;
		}
		return false;
	}
	
	//Metodo para obtener el trackDTO segun la id del track
	public TrackDTO obtenerTrackPorId(int idTrack) {
		String sql = "SELECT * FROM Track WHERE idTrack = ?";
		List<TrackDTO> listaTracks = db.executeQueryPojo(TrackDTO.class, sql, idTrack);
		// Cojo el primer track de la lista (deberia ser solo uno) y le aprico el metodo separarPalabrasClave()
		listaTracks.get(0).separarPalabrasClave();
		return listaTracks.get(0);
	}
	
	//Metodo para obtener lista de autorDTOs segun la id del articulo
	public List<AutorDTO> obtenerAutoresPorId(int idArticulo) {
		String sql = "SELECT u.* FROM Usuario u JOIN Articulo_Usuario au ON u.email = au.emailUsuario WHERE au.idArticulo = ?";
		List<AutorDTO> listaAutores = db.executeQueryPojo(AutorDTO.class, sql, idArticulo);
		return listaAutores;
	}
	
	//Metodo para obtener la fecha actual como "yyyy-MM-dd"
	// Necesito este metodo aqui porque es directamente el modelo el que la comprueba justo antes de enviar el articulo
	// (porque es fecha de envio jeje)
		public String fecha() {
			java.util.Date fecha = new java.util.Date();
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(fecha);
		}
		
		// Metodo para obtener la fecha actual como Date
		public java.util.Date fechaDate() {
			java.util.Date fecha = new java.util.Date();
			return fecha;
		}

		//Metodo para obtener una lista con los tracks disponibles
		//Devuelve una lista con los tracksDTO
		public List<TrackDTO> obtenerTracks() {
			String sql = "SELECT * FROM Track";
			List<TrackDTO> listaTracks = db.executeQueryPojo(TrackDTO.class, sql);
			
			// A cada listaTracks le ejecuto el metodo separarPalabrasClave() para separar las palabras clave en un array de Strings
			for (TrackDTO track : listaTracks) {
				track.separarPalabrasClave();
			} //asi relleno su array de palabras clave
			
			return listaTracks;
			
		}
		
		//Metodo para obtener la fecha limite de envio de un articulo a una conferencia como String
		public String obtenerDeadlineConferenciaString(String idConferencia) {
			String sql = "SELECT deadline FROM Conferencia WHERE idConferencia = ?";
			List<String> listaFechas = db.executeQueryPojo(String.class, sql, idConferencia);
			return listaFechas.get(0);
		}
		
		// Metodo para obtener el DTO de una conferencia a partir de su id
		public ConferenciaDTO obtenerConferencia(String idConferencia) {
			String sql = "SELECT * FROM Conferencia WHERE idConferencia = ?";
			List<ConferenciaDTO> listaConferencias = db.executeQueryPojo(ConferenciaDTO.class, sql, idConferencia);
			// Guarda la conferencia en un objeto ConferenciaDTO
			ConferenciaDTO conferencia = listaConferencias.get(0);
			// Relleno su fecha de deadline como Date
			conferencia.setFechaDeadline();
			return conferencia;
		}

		// Metodo para obtener el autorDTO que ha enviado un articulo a partir de la id del articulo
		public AutorDTO obtenerAutorPorIdArticulo(int idArticulo) {
			String sql = "SELECT u.* FROM Usuario u JOIN Articulo_Usuario au ON u.email = au.emailUsuario WHERE au.idArticulo = ? AND au.esEnviador = 1";
			List<AutorDTO> listaAutores = db.executeQueryPojo(AutorDTO.class, sql, idArticulo);
			return listaAutores.get(0);
		}
		
		//Metodo para obtener la lista de autorDTO cuyo nombre/email CONTENGA el texto de busqueda
		public List<AutorDTO> buscarAutores(String texto) {
			String sql = "SELECT * FROM Usuario WHERE nombre LIKE ? OR email LIKE ?";
			List<AutorDTO> listaAutores = db.executeQueryPojo(AutorDTO.class, sql, "%" + texto + "%", "%" + texto + "%");
			return listaAutores;
		}
		
		//Metodo para obtener el autorDTO por el nombre
		public AutorDTO obtenerAutorPorNombre(String nombre) {
			// Si te han pasado un nombre vacio, no se puede buscar, asiq retorna nada
			if (nombre == null) {
				return null;
			} else if (nombre.equals("")) {
				return null;
			}
			String sql = "SELECT * FROM Usuario WHERE nombre = ?";
			List<AutorDTO> listaAutores = db.executeQueryPojo(AutorDTO.class, sql, nombre);
			return listaAutores.get(0);
		}

		//Metodo para añadir una nueva entrada a la tabla VersionArticulo
		public void enviarNuevaVersion(int idArticulo, String resumen, String palabrasClave, String archivo) {
			// En caso de que ya exista una version para este articulo, la borro
			String sql = "DELETE FROM VersionArticulo WHERE idArticulo = ?";
			db.executeUpdate(sql, idArticulo);
			
			// Inserto la nueva version
			sql = "INSERT INTO VersionArticulo (idArticulo, resumen, palabrasClave, nombreFichero) VALUES (?, ?, ?, ?)";
			db.executeUpdate(sql, idArticulo, resumen, palabrasClave, archivo);
		}
		

}
