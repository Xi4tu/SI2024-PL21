package app.dto;

public class RevisionArticuloRevisorDTO {

	private int id;
	private String titulo;
	private String nombreFichero;
	private String nombre;
	
	public RevisionArticuloRevisorDTO() {}
	
	public RevisionArticuloRevisorDTO(int id, String titulo, String nombreFichero, String nombre) {
		this.id = id;
		this.titulo = titulo;
		this.nombre = nombre;
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

	@Override
	public String toString() {
		return titulo + " - " + id;
	}
}
