package app.dto;

public class AceptarDenegarArticulosDTO {
	
	private int id;
	private String titulo;
	private String nombreFichero;
	
	public AceptarDenegarArticulosDTO() {}
	
	public AceptarDenegarArticulosDTO(int id, String titulo, String nombreFichero) {
		this.id = id;
		this.titulo = titulo;
	}
	
	public int getId() {
		return id;
	}
	
}
