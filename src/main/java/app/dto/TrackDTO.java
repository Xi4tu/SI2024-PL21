package app.dto;

import java.util.Date;
import java.util.List;

public class TrackDTO {
	
	// Número único asignado al track (se asigna al aceptar el envío)
	private int idTrack;
	
	// Conferencia a la que pertecene(debe ser único en el sistema)
	private String idConferencia;
	
	// nombre del track
	private String nombre;
	
	// palabras Clave del track
	private String palabrasClave;
	
	// lista de palabras clave como String[]
	private String[] palabrasClaveLista;

	// Constructor vacío
	public TrackDTO() {
	}
	
	// Constructor con todos los parametros
	public TrackDTO(int idTrack, String idConferencia, String nombre, String palabrasClave) {
		this.idTrack = idTrack;
		this.idConferencia = idConferencia;
		this.nombre = nombre;
		this.palabrasClave = palabrasClave;
		// Separar las palabras clave en un array de Strings
		separarPalabrasClave();
	}
	
	
	
	//Getters  Setters
	
	public int getIdTrack() {
		return idTrack;
	}

	public void setIdTrack(int idTrack) {
		this.idTrack = idTrack;
	}

	public String getIdConferencia() {
		return idConferencia;
	}

	public void setIdConferencia(String idConferencia) {
		this.idConferencia = idConferencia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPalabrasClave() {
		return palabrasClave;
	}

	public void setPalabrasClave(String palabrasClave) {
		this.palabrasClave = palabrasClave;
	}
	
	public String[] getPalabrasClaveLista() {
		return palabrasClaveLista;
	}
	
	public String[] setPalabrasClaveLista(String[] palabrasClaveLista) {
		return this.palabrasClaveLista = palabrasClaveLista;
	}
	
	// Metodo que separa las palabras clave en un array de Strings y lo guarda en palabrasClaveLista
	public void separarPalabrasClave() {
		this.palabrasClaveLista = palabrasClave.split(",");
	}
	
	// toString que muestre Nombre y la lista de palabras clave por linea
	@Override
	public String toString() {
		String palabras = "";
		for (String palabra : palabrasClaveLista) {
			palabras += palabra + "\n";
		}
		return "Nombre: " + nombre + "\nPalabras Clave: \n" + palabras;
	}
	
}
