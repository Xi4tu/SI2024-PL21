package app.dto;

public class RevisionArticuloRevisionDTO {
	
	// Atributos a mostrar en la vista
	private int idArticulo;
	private String nombre;
	private String nivelExperto;
	private int decisionRevisor;
	private String comentariosParaCoordinador;
	
	// Constructor
	public RevisionArticuloRevisionDTO() {}
	
	// Constructor con atributos
	public RevisionArticuloRevisionDTO(int idArticulo, String nombre, String nivelExperto, int decisionRevisor,
			String comentariosParaCoordinador) {
		this.idArticulo = idArticulo;
		this.nombre = nombre;
		this.nivelExperto = nivelExperto;
		this.decisionRevisor = decisionRevisor;
		this.comentariosParaCoordinador = comentariosParaCoordinador;
	}
	
	// Getters y setters
	public int getIdArticulo() {
		return idArticulo;
	}
	
	public void setIdArticulo(int idArticulo) {
		this.idArticulo = idArticulo;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNivelExperto() {
		return nivelExperto;
	}
	
	public void setNivelExperto(String nivelExperto) {
		this.nivelExperto = nivelExperto;
	}
	
	public int getDecisionRevisor() {
		return decisionRevisor;
	}
	
	public void setDecisionRevisor(int decisionRevisor) {
		this.decisionRevisor = decisionRevisor;
	}
	
	public String getComentariosParaCoordinador() {
		return comentariosParaCoordinador;
	}
	
	public void setComentariosParaCoordinador(String comentariosParaCoordinador) {
		this.comentariosParaCoordinador = comentariosParaCoordinador;
	}
	
}
