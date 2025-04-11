package app.dto;

public class AccederDiscusionDTO {
	
	// Atributos de la clase
	private int idArticulo;
	private String titulo;
	private int decisionRevisor;
	private int mantenerseFirme;
	
	// Nuevo campo para almacenar el deadline de la discusión (en formato "yyyy-MM-dd")
	private String deadlineDiscusion;
	
	/**
	 * Constructor vacío.
	 */
	public AccederDiscusionDTO() {}

	/**
	 * Constructor original sin el campo deadline.
	 * 
	 * @param idArticulo       Identificador del artículo.
	 * @param titulo           Título del artículo.
	 * @param decisionRevisor  Valor numérico de la decisión del revisor.
	 * @param mantenerseFirme  Indica si el revisor se mantiene firme (1) o no (0).
	 */
	public AccederDiscusionDTO(int idArticulo, String titulo, int decisionRevisor, int mantenerseFirme) {
		this.idArticulo = idArticulo;
		this.titulo = titulo;
		this.decisionRevisor = decisionRevisor;
		this.mantenerseFirme = mantenerseFirme;
	}

	/**
	 * Constructor extendido que incluye el deadline de la discusión.
	 * 
	 * @param idArticulo       Identificador del artículo.
	 * @param titulo           Título del artículo.
	 * @param decisionRevisor  Valor numérico de la decisión del revisor.
	 * @param mantenerseFirme  Indica si el revisor se mantiene firme (1) o no (0).
	 * @param deadlineDiscusion Fecha límite de la discusión (formato "yyyy-MM-dd").
	 */
	public AccederDiscusionDTO(int idArticulo, String titulo, int decisionRevisor, int mantenerseFirme, String deadlineDiscusion) {
		this(idArticulo, titulo, decisionRevisor, mantenerseFirme);
		this.deadlineDiscusion = deadlineDiscusion;
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
	
	/**
	 * @return La fecha límite de discusión (deadline) en formato "yyyy-MM-dd".
	 */
	public String getDeadlineDiscusion() {
		return deadlineDiscusion;
	}
	
	/**
	 * Establece la fecha límite de discusión (deadline).
	 * 
	 * @param deadlineDiscusion Fecha en formato "yyyy-MM-dd".
	 */
	public void setDeadlineDiscusion(String deadlineDiscusion) {
		this.deadlineDiscusion = deadlineDiscusion;
	}

	@Override
	public String toString() {
		return "Artículo " + idArticulo + ": " + titulo;
	}
}
