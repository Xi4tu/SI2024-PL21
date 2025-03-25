package app.dto;

public class UsuarioPreferenciaDTO {
	private String email;
	private int idPreferencia;
	
	public UsuarioPreferenciaDTO() {
	}
	
	public UsuarioPreferenciaDTO(String email, int idPreferencia) {
		this.email = email;
		this.idPreferencia = idPreferencia;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIdPreferencia() {
		return idPreferencia;
	}

	public void setIdPreferencia(int idPreferencia) {
		this.idPreferencia = idPreferencia;
	}
	
	
}
