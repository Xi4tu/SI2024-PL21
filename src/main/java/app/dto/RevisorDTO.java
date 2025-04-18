package app.dto;

public class RevisorDTO {
    private String email;
    private String nombre;
    private String organizacion;
    private String grupoInvestigacion;
    private String palabrasClave;
    
    // Constructor vacío
    public RevisorDTO() {}

    // Constructor con parámetros
    public RevisorDTO(String email, String nombre, String organizacion, String grupoInvestigacion) {
        this.email = email;
        this.nombre = nombre;
        this.organizacion = organizacion;
        this.grupoInvestigacion = grupoInvestigacion;
    }
    public RevisorDTO(String email, String nombre, String organizacion, String grupoInvestigacion, String palabrasClave) {
        this.email = email;
        this.nombre = nombre;
        this.organizacion = organizacion;
        this.grupoInvestigacion = grupoInvestigacion;
        this.palabrasClave = palabrasClave;
    }

    // Getters y Setters
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
    public String getPalabrasClave() {
        return palabrasClave;
    }
    public void setPalabrasClave(String palabrasClave) {
        this.palabrasClave = palabrasClave;
    }

    // Método toString (opcional, útil para depuración)
    @Override
    public String toString() {
        return "RevisorDTO{" +
               "email='" + email + '\'' +
               ", nombre='" + nombre + '\'' +
               ", organizacion='" + organizacion + '\'' +
               ", grupoInvestigacion='" + grupoInvestigacion + '\'' +
               '}';
    }
}
