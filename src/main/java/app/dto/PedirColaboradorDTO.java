package app.dto;

public class PedirColaboradorDTO {
	private int id;
	private String titulo;
	private String nombreFichero;
	private String nombre;
	private String nivelExperto;
	private int decisionRevisor;
	private String comentariosParaAutor;
	private String comentariosParaCoordinador;
	private String decisionFinal;
	private String conCambios;
	private String grupoInvestigacion;
	private int idTrack;
	private String emailUsuario;
	private String decision;
	private String revisorColaborador;

	public PedirColaboradorDTO() {}

	public PedirColaboradorDTO(int id, String titulo, String nombreFichero, String nombre, int idTrack, String emailUsuario) {
		this.id = id;
		this.titulo = titulo;
		this.nombre= nombre;
		this.idTrack = idTrack;
		this.emailUsuario = emailUsuario;
	}

	public PedirColaboradorDTO(String nivelExperto) {
	    this.nivelExperto = nivelExperto;
	}

	// Getters y setters

	public String getGrupoInvestigacion() {
		return grupoInvestigacion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdTrack() {
		return idTrack;
	}

	public void setIdTrack(int idTrack) {
		this.idTrack = idTrack;
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

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getRevisorColaborador() {
		return revisorColaborador;
	}
	
	public void setRevisorColaborador(String revisorColaborador) {
		this.revisorColaborador = revisorColaborador;
	}
	
	@Override
	public String toString() {
		return " - " + nombre;
	}
}