package app.dto;

public class ArticuloDiscusionDTO {
	
	private int idArticulo;
	private String titulo;
	private int valoracionGlobal;
	
	public ArticuloDiscusionDTO() {}
	
	public ArticuloDiscusionDTO(int idArticulo, String titulo, int valoracionGlobal) {
		this.idArticulo = idArticulo;
		this.titulo = titulo;
		this.valoracionGlobal = valoracionGlobal;
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
	
	public int getValoracionGlobal() {
		return valoracionGlobal;
	}
	
	public void setValoracionGlobal(int valoracionGlobal) {
		this.valoracionGlobal = valoracionGlobal;
	}
	
	@Override
	public String toString() {
		return "Artículo" + idArticulo + ": " + titulo;
	}
}
