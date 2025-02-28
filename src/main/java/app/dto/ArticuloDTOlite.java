package app.dto;

import java.util.Date;
import java.util.List;

public class ArticuloDTOlite {

    // Número único asignado al artículo (se asigna al aceptar el envío)
    private int idArticulo;
    
    // Título del artículo (debe ser único en el sistema)
    private String titulo;
    
    // Lista de palabras clave
    private String palabrasClave;
    
    // Resumen del artículo
    private String resumen;
    
    // Nombre del fichero que contiene el contenido del artículo
    private String nombreFichero;
    
    // Lista de autores, en la que se incluye el autor que hace el envío
    private List<AutorDTO> autores;
    
    // Fecha en la que se efectuó el envío del artículo
    private String fechaEnvio;
    

    public ArticuloDTOlite() {
    }

    public ArticuloDTOlite(int idArticulo, String titulo, String palabrasClave, String resumen, String nombreFichero,
            List<AutorDTO> autores, String fechaEnvio) {
        this.idArticulo = idArticulo;
        this.titulo = titulo;
        this.palabrasClave = palabrasClave;
        this.resumen = resumen;
        this.nombreFichero = nombreFichero;
        this.autores = autores;
        this.fechaEnvio = fechaEnvio;
    }

    // Getters y setters

    public int getidArticulo() {
        return idArticulo;
    }

    public void setidArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPalabrasClave() {
        return palabrasClave;
    }

    public void setPalabrasClave(String palabrasClave) {
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

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }


    @Override
    public String toString() {
        return "ArticuloDTO{" +
                "id=" + idArticulo +
                ", titulo='" + titulo + '\'' +
                ", palabrasClave=" + palabrasClave +
                ", resumen='" + resumen + '\'' +
                ", nombreFichero='" + nombreFichero + '\'' +
                ", autores=" + autores +
                ", fechaEnvio=" + fechaEnvio +
                "'}'\njuan.perez@ejemplo.com";
    }
}
