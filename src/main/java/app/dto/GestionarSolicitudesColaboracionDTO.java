package app.dto;

public class GestionarSolicitudesColaboracionDTO {

	private String titulo;
	private String revisorColaborador;
	private String nombreTrack;
	private String palabrasClaveTrack;
	private String nombre;
	private int idArticulo;
	
	public GestionarSolicitudesColaboracionDTO() {
    }
	
	public GestionarSolicitudesColaboracionDTO(int idArticulo, String titulo, String nombreTrack, String palabrasClaveTrack, String revisorColaborador) {
		this.idArticulo = idArticulo;
		this.titulo = titulo;
		this.nombreTrack = nombreTrack;
		this.palabrasClaveTrack = palabrasClaveTrack;
		this.revisorColaborador = revisorColaborador;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getRevisorColaborador() {
		return revisorColaborador;
	}
	
	public void setRevisorColaborador(String revisorColaborador) {
		this.revisorColaborador = revisorColaborador;
	}
	
	public String getNombreTrack() {
		return nombreTrack;
	}
	
	public void setNombreTrack(String nombreTrack) {
		this.nombreTrack = nombreTrack;
	}
	
	public String getPalabrasClaveTrack() {
		return palabrasClaveTrack;
	}
	
	public void setPalabrasClaveTrack(String palabrasClaveTrack) {
		this.palabrasClaveTrack = palabrasClaveTrack;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public int getIdArticulo() {
		return idArticulo;
	}
	
	public void setIdArticulo(int idArticulo) {
		this.idArticulo = idArticulo;
	}
	
	@Override
	public String toString() {
		return idArticulo + ". - " + titulo;
	}
}
