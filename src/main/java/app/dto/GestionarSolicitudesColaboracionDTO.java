package app.dto;

public class GestionarSolicitudesColaboracionDTO {

	private String titulo;
	private String nombreColaborador;
	private String nombreTrack;
	private String palabrasClaveTrack;
	private String nombre;
	private int idArticulo;
	
	public GestionarSolicitudesColaboracionDTO() {
    }
	
	public GestionarSolicitudesColaboracionDTO(int idArticulo, String titulo, String nombreTrack, String palabrasClaveTrack, String nombreColaborador) {
		this.idArticulo = idArticulo;
		this.titulo = titulo;
		this.nombreTrack = nombreTrack;
		this.palabrasClaveTrack = palabrasClaveTrack;
		this.nombreColaborador = nombreColaborador;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getNombreColaborador() {
		return nombreColaborador;
	}
	
	public void setNombreColaborador(String revisorColaborador) {
		this.nombreColaborador = revisorColaborador;
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
