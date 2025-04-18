package app.dto;

import java.util.Date;
import java.util.List;

public class ArticuloDTO {

    // Número único asignado al artículo (se asigna al aceptar el envío)
    private int idArticulo;
    
    // Id del track al que pertenece el articulo
    private int idTrack;
    
    // Título del artículo (debe ser único en el sistema)
    private String titulo;
    
    // String de palabras clave
    private String palabrasClave;
    // String de palabras clave del track
    private String palabrasClaveTrack;
    
    // Lista de palabras clave como String[]
    private String[] palabrasClaveLista;
    // Lista de palabras clave del track como String[]
    private String[] palabrasClaveTrackLista;
    
    
    // Resumen del artículo
    private String resumen;
    
    // Nombre del fichero que contiene el contenido del artículo
    private String nombreFichero;
    
    // Lista de autores, en la que se incluye el autor que hace el envío
    private List<AutorDTO> autores;
    
    // Fecha en la que se efectuó el envío del artículo
    private String fechaEnvio;
    
    // Fecha en la que se hizo la ultima modificacion del articulo
    private String fechaModificacion;
    
    // Decision final del articulo
    private String decisionFinal;
    
    // Valoracion global del articulo
    private String valoracionGlobal;
    
    

    public ArticuloDTO() {
    }

    public ArticuloDTO(int idArticulo, int idTrack, String titulo, String palabrasClave, String palabrasClaveTrack, String resumen, String nombreFichero,
            List<AutorDTO> autores, String fechaEnvio, String fechaModificacion, String decisionFinal, String valoracionGlobal) {
        this.idArticulo = idArticulo;
        this.idTrack = idTrack;
        this.titulo = titulo;
        this.palabrasClave = palabrasClave;
        this.palabrasClaveTrack = palabrasClaveTrack;
        this.resumen = resumen;
        this.nombreFichero = nombreFichero;
        this.autores = autores;
        this.fechaEnvio = fechaEnvio;
        this.fechaModificacion = fechaModificacion;
        this.decisionFinal = decisionFinal;
        this.valoracionGlobal = valoracionGlobal;
    }

    // Getters y setters

    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int id) {
        this.idArticulo = id;
    }

    public int getIdTrack() {
		return idTrack;
	}

	public void setIdTrack(int idTrack) {
		this.idTrack = idTrack;
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
    
    public String getFechaModificacion() {
    	return fechaModificacion;
    }
    
    public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

    public String getDecisionFinal() {
    	return decisionFinal;
    }
    
    public void setDecisionFinal(String decisionFinal) {
    	this.decisionFinal = decisionFinal;
    }

    public String getValoracionGlobal() {
		return valoracionGlobal;
	}

	public void setValoracionGlobal(String valoracionGlobal) {
		this.valoracionGlobal = valoracionGlobal;
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
                ", fechaModificacion=" + fechaModificacion +
                ", decisionFinal=" + decisionFinal +
                ", valoracionGlobal=" + valoracionGlobal +
                '}';
    }
    
    // Metodo que separa las palabras clave en un array de Strings y lo guarda en la lista que se indique
    public void separarPalabrasClave(String lista) {
		if (lista.equals("palabrasClave")) {
			this.palabrasClaveLista = palabrasClave.split(",");
		} else if (lista.equals("palabrasClaveTrack")) {
			this.palabrasClaveTrackLista = palabrasClaveTrack.split(",");
		}
	}
    
    // Metodo que separa las palabras clave del track en un array de Strings y lo guarda en la lista que se indique
    public void separarPalabrasClaveTrack(String lista) {
   		if (lista.equals("palabrasClave")) {
    		this.palabrasClaveLista = palabrasClave.split(",");
		} else if (lista.equals("palabrasClaveTrack")) {
			this.palabrasClaveTrackLista = palabrasClaveTrack.split(",");
    	}
    }    
}
