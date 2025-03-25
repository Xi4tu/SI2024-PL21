package app.dto;

public class AnotacionesDTO {
	
	// Atributos de la clase
	private int idArticulo;
	private String emailUsuario;
	private String comentario;
	private String fecha;
	private String hora;
	
	// Constructor de la clase
	public AnotacionesDTO() {}
	
	// Constructor de la clase
	public AnotacionesDTO(int idArticulo, String emailUsuario, String comentario, String fecha, String hora) {
		this.idArticulo = idArticulo;
		this.emailUsuario = emailUsuario;
		this.comentario = comentario;
		this.fecha = fecha;
		this.hora = hora;
	}
	
	// Getters y setters
	public int getIdArticulo() {
		return idArticulo;
	}
	
	public void setIdArticulo(int idArticulo) {
		this.idArticulo = idArticulo;
	}
	
	public String getEmailUsuario() {
		return emailUsuario;
	}
	
	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}
	
	public String getComentario() {
		return comentario;
	}
	
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	public String getFecha() {
		return fecha;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public String getHora() {
		return hora;
	}
	
	public void setHora(String hora) {
		this.hora = hora;
	}
	
	@Override
	public String toString() {
		return "Anotación del artículo " + idArticulo + " por " + emailUsuario;
	}
}
