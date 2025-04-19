package app.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.model.AsignarRevisoresModel;
import giis.demo.util.Util;

public class ArticuloRevisionDTO {

    // Número único asignado al artículo (se asigna al aceptar el envío)
    private int id;
    
    // Título del artículo (debe ser único en el sistema)
    private String titulo;
    
    // Lista de palabras clave
    private String palabrasClave;
    
    //Lista de palabras clave del track
    private String palabrasClaveTrack;
    
    // Resumen del artículo
    private String resumen;
    
    // Nombre del fichero que contiene el contenido del artículo
    private String nombreFichero;
    
    // Lista de autores, en la que se incluye el autor que hace el envío
    private List<AutorDTO> autores;
    private String autoresTexto;
    
    // Fecha en la que se efectuó el envío del artículo
    private Date fechaEnvio;
    
    // Autor que hace el envío (ya registrado en el sistema)
    private AutorDTO autorEnvio;

    public ArticuloRevisionDTO() {
    }

    public ArticuloRevisionDTO(int id, String titulo, String palabrasClave, String resumen, String nombreFichero,
            String autoresTexto, String fechaEnvio, AutorDTO autorEnvio) {
        this.id = id;
        this.titulo = titulo;
        this.palabrasClave = palabrasClave;
        this.resumen = resumen;
        this.nombreFichero = nombreFichero;
        this.autoresTexto = autoresTexto;
        setFechaEnvio(fechaEnvio); 
        
        
        this.autorEnvio = autorEnvio;
    }
    
    public ArticuloRevisionDTO(int id, String titulo, String palabrasClave,String palabrasClaveTrack, String resumen, String nombreFichero,
            String autoresTexto, String fechaEnvio, AutorDTO autorEnvio) {
        this.id = id;
        this.titulo = titulo;
        this.palabrasClave = palabrasClave;
        this.palabrasClaveTrack= palabrasClaveTrack;
        this.resumen = resumen;
        this.nombreFichero = nombreFichero;
        this.autoresTexto = autoresTexto;
        setFechaEnvio(fechaEnvio); 
        
        
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
    
    public String getAutoresTexto() {
    	
        return autoresTexto;
    }

    public void setAutoresTexto(String autoresTexto) {
        this.autoresTexto = autoresTexto;
        setAutoresFromEmails(autoresTexto);
    }

    public String getPalabrasClave() {
        return palabrasClave;
    }

    public void setPalabrasClave(String palabrasClave) {
        this.palabrasClave = palabrasClave;
    }

    public String getPalabrasClaveTrack() {
        return palabrasClaveTrack;
    }

    public void setPalabrasClaveTrack(String palabrasClaveTrack) {
        this.palabrasClaveTrack = palabrasClaveTrack;
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
    	if (autoresTexto == null || autoresTexto.trim().isEmpty()) {
            this.autores = new ArrayList<>();
        }
        return autores;
    }

    public void setAutores(List<AutorDTO> autores) {
        this.autores = autores;
    }

    public String getFechaEnvio() {
        return Util.dateToIsoString(fechaEnvio);
    }

    public void setFechaEnvio(String fechaEnvio) {
    	 this.fechaEnvio = Util.isoStringToDate(fechaEnvio);
    }

    public AutorDTO getAutorEnvio() {
        return autorEnvio;
    }

    public void setAutorEnvio(AutorDTO autorEnvio) {
        this.autorEnvio = autorEnvio;
    }
    
    private void setAutoresFromEmails(String autoresTexto) {
    	if (autoresTexto == null || autoresTexto.trim().isEmpty()) {
            this.autores = new ArrayList<>();
            return;
        }
	    // Separamos los emails usando la coma como delimitador
	    String[] emails = autoresTexto.split(", ");
	    List<AutorDTO> listaAutores = new ArrayList<>();
	    Set<String> emailsUnicos = new HashSet<>(); // Para evitar duplicados

	    // Creamos una instancia del modelo para obtener los autores por email
	    AsignarRevisoresModel model = new AsignarRevisoresModel();

	    // Por cada email, verificamos si ya ha sido agregado antes de obtener el autor
	    for (String email : emails) {
	        if (emailsUnicos.add(email)) { // add() devuelve false si el email ya existe en el Set
	            AutorDTO autor = model.obtenerAutor(email);
	            if (autor != null) {
	                listaAutores.add(autor);
	            }
	        }
	    }

	    // Asignamos la lista de autores
	    this.autores = listaAutores;
	}
 

    @Override
    public String toString() {
        return "ArticuloDTO{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", palabrasClave=" + palabrasClave +
                ", resumen='" + resumen + '\'' +
                ", nombreFichero='" + nombreFichero + '\'' +
                ", autores=" + autoresTexto +
                ", fechaEnvio=" + fechaEnvio +
                ", autorEnvio=" + autorEnvio +
                '}';
    }
}
