package dto;

import java.util.List;

import app.view.AutorDTO;

public class EnviarArticuloDTO {
	private String titulo;
	private List<String> palabrasClave;
    private String resumen;
    private String nombreFichero;
	private String autor;
	
	private List<AutorDTO> coautores;
	private String coautor_nombre;
	private String coautor_correo;
	private String coautor_organizacion;
}
