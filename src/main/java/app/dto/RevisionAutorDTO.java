package app.dto;

public class RevisionAutorDTO {
	
	private int idArticulo;
	private String comentariosParaAutor;
	private String nivelExperto;
	private int decisionRevisor;

	public RevisionAutorDTO() {
	}

	public RevisionAutorDTO(String comentariosParaAutor, String nivelExperto, int decisionRevisor, int idArticulo) {
		this.comentariosParaAutor = comentariosParaAutor;
		this.nivelExperto = nivelExperto;
		this.decisionRevisor = decisionRevisor;
	}
	
	public int getIdArticulo() {
		return idArticulo;
	}
	
	public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
	}
        
	public String getComentariosParaAutor() {
		return comentariosParaAutor;
	}

	public void setComentariosParaAutor(String comentariosParaAutor) {
		this.comentariosParaAutor = comentariosParaAutor;
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

	@Override
	public String toString() {
		return "RevisionAutorDTO{" + "comentariosParaAutor='" + comentariosParaAutor + '\'' + ", nivelExperto='"
				+ nivelExperto + '\'' + ", decisionRevisor=" + decisionRevisor + '}';
	}
}
