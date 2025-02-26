package app.dto;

import java.util.Date;
import java.util.List;

public class ArticuloDTO {

    // Número único asignado al artículo (se asigna al aceptar el envío)
    private int id;
    
    // Título del artículo (debe ser único en el sistema)
    private String titulo;
    
    // Lista de palabras clave
    private List<String> palabrasClave;
    
    // Resumen del artículo
    private String resumen;
    
    // Nombre del fichero que contiene el contenido del artículo
    private String nombreFichero;
    
    // Lista de autores, en la que se incluye el autor que hace el envío
    private List<AutorDTO> autores;
    
    // Fecha en la que se efectuó el envío del artículo
    private Date fechaEnvio;
    
    // Autor que hace el envío (ya registrado en el sistema)
    private AutorDTO autorEnvio;

    public ArticuloDTO() {
    }

    public ArticuloDTO(int id, String titulo, List<String> palabrasClave, String resumen, String nombreFichero,
            List<AutorDTO> autores, Date fechaEnvio, AutorDTO autorEnvio) {
        this.id = id;
        this.titulo = titulo;
        this.palabrasClave = palabrasClave;
        this.resumen = resumen;
        this.nombreFichero = nombreFichero;
        this.autores = autores;
        this.fechaEnvio = fechaEnvio;
        this.autorEnvio = autorEnvio;
    }

    // Getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getPalabrasClave() {
        return palabrasClave;
    }

    public void setPalabrasClave(List<String> palabrasClave) {
        this.palabrasClave = palabrasClave;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getNombreFichero() {
        return nombreFichero;
    }

    public void setNombreFichero(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }

    public List<AutorDTO> getAutores() {
        return autores;
    }

    public void setAutores(List<AutorDTO> autores) {
        this.autores = autores;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public AutorDTO getAutorEnvio() {
        return autorEnvio;
    }

    public void setAutorEnvio(AutorDTO autorEnvio) {
        this.autorEnvio = autorEnvio;
    }

    @Override
    public String toString() {
        return "ArticuloDTO{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", palabrasClave=" + palabrasClave +
                ", resumen='" + resumen + '\'' +
                ", nombreFichero='" + nombreFichero + '\'' +
                ", autores=" + autores +
                ", fechaEnvio=" + fechaEnvio +
                ", autorEnvio=" + autorEnvio +
                '}';
    }
}
