package app.dto;

public class AceptarDenegarArticuloDTO {

	private int id;
	private String titulo;
	private String nombreFichero;
	private String nombre;
	private String nivelExperto;
	private int decisionRevisor;
	private String comentariosParaAutor;
	private String comentariosParaCoordinador;
	private String decisionFinal;
	
	public AceptarDenegarArticuloDTO() {}
	
	public AceptarDenegarArticuloDTO(int id, String titulo, String nombreFichero, String nombre) {
		this.id = id;
		this.titulo = titulo;
		this.nombre= nombre;
	}
	
	public AceptarDenegarArticuloDTO(String nivelExperto) {
	    this.nivelExperto = nivelExperto;
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

	@Override
	public String toString() {
		return titulo + " - " + id;
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
	
	public String getDecisionFinal() {
		return decisionFinal;
	}
	
	public void setDecisionFinal(String decisionFinal) {
		this.decisionFinal = decisionFinal;
	}
}
