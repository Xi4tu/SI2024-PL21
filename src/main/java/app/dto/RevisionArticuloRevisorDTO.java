package app.dto;

public class RevisionArticuloRevisorDTO {

	private int id;
	private String titulo;
	private String nombreFichero;
	
	public RevisionArticuloRevisorDTO() {}
	
	public RevisionArticuloRevisorDTO(int id, String titulo, String nombreFichero) {
		this.id = id;
		this.titulo = titulo;
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

	@Override
	public String toString() {
		return titulo + " - " + id;
	}
}
