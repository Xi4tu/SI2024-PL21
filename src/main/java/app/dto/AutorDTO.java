package app.dto;

public class AutorDTO {

	private String email;
	private String nombre;
	private String organizacion;
	private String grupoInvestigacion;

	public AutorDTO() {
	}

	public AutorDTO(String email, String nombre, String organizacion, String grupoInvestigacion) {
		this.email = email;
		this.nombre = nombre;
		this.organizacion = organizacion;
		this.grupoInvestigacion = grupoInvestigacion;
	}


	
	// Getters y setters
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getOrganizacion() {
		return organizacion;
	}

	public void setOrganizacion(String organizacion) {
		this.organizacion = organizacion;
	}

	public String getGrupoInvestigacion() {
		return grupoInvestigacion;
	}

	public void setGrupoInvestigacion(String grupoInvestigacion) {
		this.grupoInvestigacion = grupoInvestigacion;
	}
	
	// toString con todos los datos separados por guiones
	@Override
	public String toString() {
		return email + " - " + nombre + " - " + organizacion + " - " + grupoInvestigacion;
	}

}
