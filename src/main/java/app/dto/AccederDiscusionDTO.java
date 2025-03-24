package app.dto;

public class AccederDiscusionDTO {
	
	// Atributos de la clase
	private int idArticulo;
	private String titulo;
	private int decisionRevisor;
	private int mantenerseFirme;
	
	// Constructor de la clase
	public AccederDiscusionDTO() {}
	
	// Constructor de la clase
	public AccederDiscusionDTO(int idArticulo, String titulo, int decisionRevisor, int mantenerseFirme) {
		this.idArticulo = idArticulo;
		this.titulo = titulo;
		this.decisionRevisor = decisionRevisor;
		this.mantenerseFirme = mantenerseFirme;
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
	
	public int getDecisionRevisor() {
		return decisionRevisor;
	}
	
	public void setDecisionRevisor(int decisionRevisor) {
		this.decisionRevisor = decisionRevisor;
	}
	
	public int getMantenerseFirme() {
		return mantenerseFirme;
	}
	
	public void setMantenerseFirme(int mantenerseFirme) {
		this.mantenerseFirme = mantenerseFirme;
	}
	
	@Override
	public String toString() {
		return "Artículo " + idArticulo + ": " + titulo;
	}
	
}
