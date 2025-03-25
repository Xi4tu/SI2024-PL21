package app.dto;

public class PreferenciaDTO {

	private String preferencia;
	private int idArticulo;
	private int id;
	
	public PreferenciaDTO() {
	}
	
	public PreferenciaDTO(int id, String preferencia, int idArticulo) {
		this.idArticulo = id;
		this.preferencia = preferencia;
		this.idArticulo = idArticulo;
	}

	
	//Getters y setters
	public String getPreferencia() {
		return preferencia;
	}

	public void setPreferencia(String preferencia) {
		this.preferencia = preferencia;
	}

	public int getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(int idArticulo) {
		this.idArticulo = idArticulo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
