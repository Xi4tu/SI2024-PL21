package app.dto;

public class RevisionArticuloAutorDTO {
	
	private int idArticulo;
	private String titulo;
	private String decisionFinal;
	
	public RevisionArticuloAutorDTO() {}
	
	public RevisionArticuloAutorDTO(int idArticulo, String titulo, String decisionFinal) {
		this.idArticulo = idArticulo;
		this.titulo = titulo;
		this.decisionFinal = decisionFinal;
	}
	
	// Getters y setters
	public int getIdArticulo() {
		return idArticulo;
	}
	
	public void setIdArticulo(int idArticulo) {
		this.idArticulo = idArticulo;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getDecisionFinal() {
		return decisionFinal;
	}
	
	public void setDecisionFinal(String decisionFinal) {
		this.decisionFinal = decisionFinal;
	}
	
	@Override
	public String toString() {
		return "Artículo" + idArticulo + ": " + titulo + " (" + decisionFinal + ")";
	}
	

}
