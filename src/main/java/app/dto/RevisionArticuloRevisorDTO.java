package app.dto;

public class RevisionArticuloRevisorDTO {

	private int id;
	private String titulo;
	private String nombreFichero;
	private String nombre;
	private String estado;
	private String nombreRevisor;
	private String comentariosParaAutor;
	private String comentariosParaCoordinador;
	private String email;
	
	public RevisionArticuloRevisorDTO() {}
	
	public RevisionArticuloRevisorDTO(int id, String titulo, String nombreFichero, String nombre) {
		this.id = id;
		this.titulo = titulo;
		this.nombre = nombre;
	}

	public RevisionArticuloRevisorDTO(String nombre, String titulo, String estado, String nombreRevisor) {
		this.nombre = nombre;
		this.titulo = titulo;
		this.estado = estado;
		this.nombreRevisor = nombreRevisor;
	}

	// Getters y setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getNombreFichero() {
		return nombreFichero;
	}
	
	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getNombreRevisor() {
		return nombreRevisor;
	}
	
	public void setNombreRevisor(String nombreRevisor) {
		this.nombreRevisor = nombreRevisor;
	}
	
	public String getComentariosParaAutor() {
		return comentariosParaAutor;
	}
	
	public void setComentariosParaAutor(String comentariosParaAutor) {
		this.comentariosParaAutor = comentariosParaAutor;
	}
	
	public String getComentariosParaCoordinador() {
		return comentariosParaCoordinador;
	}
	
	public void setComentariosParaCoordinador(String comentariosParaCoordinador) {
		this.comentariosParaCoordinador = comentariosParaCoordinador;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return titulo + " - " + id;
	}
}
