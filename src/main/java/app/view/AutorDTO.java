package app.view;

public class AutorDTO {

    private String nombre;
    private String correo;
    private String organizacion;
    private String grupoInvestigacion;

    // Constructor vacío (por defecto)
    public AutorDTO() {
    }

    // Constructor con parámetros para inicializar todos los campos
    public AutorDTO(String nombre, String correo, String organizacion, String grupoInvestigacion) {
        this.nombre = nombre;
        this.correo = correo;
        this.organizacion = organizacion;
        this.grupoInvestigacion = grupoInvestigacion;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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

    @Override
    public String toString() {
        return "AutorDTO [nombre=" + nombre + ", correo=" + correo 
               + ", organizacion=" + organizacion 
               + ", grupoInvestigacion=" + grupoInvestigacion + "]";
    }
}
