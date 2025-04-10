package app.dto;

public class RevisionArticuloRevisionDTO {
	
	// Atributos a mostrar en la vista
	private int idArticulo;
	private String nombre;
	private String nivelExperto;
	private int decisionRevisor;
	private String comentariosParaCoordinador;
	
	// Nuevo campo para almacenar el email del revisor
	private String email;
	
	/**
	 * Constructor vacío.
	 */
	public RevisionArticuloRevisionDTO() {}
	
	/**
	 * Constructor con atributos originales.
	 * 
	 * @param idArticulo              Identificador del artículo.
	 * @param nombre                  Nombre del revisor.
	 * @param nivelExperto            Nivel de expertise del revisor.
	 * @param decisionRevisor         Valor numérico de la decisión del revisor.
	 * @param comentariosParaCoordinador Comentarios enviados al coordinador.
	 */
	public RevisionArticuloRevisionDTO(int idArticulo, String nombre, String nivelExperto, int decisionRevisor,
			String comentariosParaCoordinador) {
		this.idArticulo = idArticulo;
		this.nombre = nombre;
		this.nivelExperto = nivelExperto;
		this.decisionRevisor = decisionRevisor;
		this.comentariosParaCoordinador = comentariosParaCoordinador;
	}
	
	/**
	 * Constructor con atributos extendidos, incluyendo el email del revisor.
	 * 
	 * @param idArticulo              Identificador del artículo.
	 * @param nombre                  Nombre del revisor.
	 * @param nivelExperto            Nivel de expertise del revisor.
	 * @param decisionRevisor         Valor numérico de la decisión del revisor.
	 * @param comentariosParaCoordinador Comentarios enviados al coordinador.
	 * @param email                   Email del revisor.
	 */
	public RevisionArticuloRevisionDTO(int idArticulo, String nombre, String nivelExperto, int decisionRevisor,
			String comentariosParaCoordinador, String email) {
		// Llamamos al constructor original para inicializar los atributos existentes.
		this(idArticulo, nombre, nivelExperto, decisionRevisor, comentariosParaCoordinador);
		this.email = email;
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
	
	/**
	 * @return El email del revisor.
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Establece el email del revisor.
	 * 
	 * @param email Email del revisor.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}
