package app.controller;

import java.util.ArrayList;

import app.dto.AutorDTO;
import app.enums.Rol;
import app.model.EnviarArticuloModel;
import app.util.UserUtil;
import app.view.EnviarArticuloView;
import giis.demo.util.SwingUtil;

public class EnviarArticuloController {
	
	// Variables
	private EnviarArticuloModel model;
	private EnviarArticuloView view;
	private String email;
	private static final Rol ROL = Rol.AUTOR;
	
	private AutorDTO autor;
	private ArrayList<AutorDTO> autores = new ArrayList<AutorDTO>();
	
	
	/*
	 * Constructor del controlador
	 */
	
	public EnviarArticuloController(EnviarArticuloModel m, EnviarArticuloView v, String email) {
		this.model = m;
		this.view = v;
		this.email = email;
		
		if (!UserUtil.checkEmail(email, ROL.getNombre(), model.getDbUtil())) {
			// Detener la inicialización si el email es inválido.
			return;
		}
		
		// Informacion del autor
		autor = model.obtenerAutor(email);
		//Añado el autor a la lista de autores
		autores.add(autor);
		//Inserto el DTO del autor como una nueva fila en la tabla de autores con el metodo agregarAutor
		view.agregarAutor(autor.getEmail(), autor.getNombre(), autor.getOrganizacion(), autor.getGrupoInvestigacion());

		// Inicializar la vista una vez que los datos están cargados.
		this.initView();
		
	}
	
	public void initController() {
		view.getBtnCancelar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> view.getFrame().dispose()));
		
		// Listener del botón de añadir autores
		view.getBtnAnadirAutor().addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			// Si los datos del autor son correctos
			if (validarDatosAutor(view.getTextfCorreoCoautor().getText(), view.getTextfNombreCoautor().getText(), view.getTextfOrganizacionCoautor().getText(), view.getTextfGrupoInvestigacionCoautor().getText())) {
				// Si los datos son correctos, se añade el autor al ArrayList de autoresDTO
				autores.add(new AutorDTO(view.getTextfCorreoCoautor().getText(), view.getTextfNombreCoautor().getText(), view.getTextfOrganizacionCoautor().getText(), view.getTextfGrupoInvestigacionCoautor().getText()));
				// Se añade el autor a la tabla de autores con el metodo agregarAutor
				view.agregarAutor(view.getTextfCorreoCoautor().getText(), view.getTextfNombreCoautor().getText(), view.getTextfOrganizacionCoautor().getText(), view.getTextfGrupoInvestigacionCoautor().getText());
				//Se borran los datos de los campos de texto
				limpiarCampos();
				
			} else {
				// Si los datos no son correctos, se muestra un mensaje de error
				view.mostrarMensajeError("Datos de autor incorrectos o inválidos");
			}
		}));
		
		// Listener del botón de enviar artículo
		view.getBtnEnviar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			// Si los datos del articulo son correctos
			if (validarDatosArticulo(view.getTextfTituloArticulo().getText(), view.getTextfPalabrasClaveArticulo().getText(), view.getTextfResumenArticulo().getText(), view.getTextfArchivoArticulo().getText())) {
				// Si el articulo ya existe, se muestra un mensaje de error
				if (existeArticulo(view.getTextfTituloArticulo().getText())) {
					view.mostrarMensajeError("El artículo ya existe en la base de datos");
					return;
				}
				// Si los datos son correctos y el articulo no existe ya, se envia el articulo
				// No verifico si la lista de autores esta vacia porque el autor que envia el articulo ya esta en la lista por defecto
				model.enviarArticulo(view.getTextfTituloArticulo().getText(), view.getTextfPalabrasClaveArticulo().getText(), view.getTextfResumenArticulo().getText(), view.getTextfArchivoArticulo().getText(), autores);
				view.getFrame().dispose();
			} else {
				// Si los datos no son correctos, se muestra un mensaje de error
				view.mostrarMensajeError("Datos de artículo incorrectos o inválidos");
			}
		}));
	}

	/*
	 * // Si los datos son correctos, se envia el articulo
				model.enviarArticulo(view.getTextfTituloArticulo().getText(), view.getTextfPalabrasClaveArticulo().getText(), view.getTextfResumenArticulo().getText(), view.getTextfArchivoArticulo().getText(), );
				view.getFrame().dispose();
	 * 
	 */

	//Valida si los datos del autor son correctos y no nulos
	// GRUPO DE INVESTIGACION PUEDE SER NULO
	public boolean validarDatosAutor(String email, String nombre, String organizacion, String grupoInvestigacion) {
		if ((email == null || nombre == null || organizacion == null)
				|| (email.isEmpty() || nombre.isEmpty() || organizacion.isEmpty())) {
			return false;
		}
		return true;
	}
	
	//Valida si los datos del articulo son correctos y no nulos
	public boolean validarDatosArticulo(String titulo, String palabrasClave, String resumen, String archivo) {
		if ((titulo == null || palabrasClave == null || resumen == null || archivo == null)
				|| (titulo.isEmpty() || palabrasClave.isEmpty() || resumen.isEmpty() || archivo.isEmpty())) {
			return false;
		}
		return true;
	}
	
	//Valida si el articulo ya existe en la base de datos (hago un metodo separado para distinguir el mensaje de error)
	public boolean existeArticulo(String titulo) {
		return model.existeArticulo(titulo);
	}
	
	private void initView() {
		view.getFrame().setVisible(true);// TODO Auto-generated method stub
		
	}
	
	private void limpiarCampos() {
		view.getTextfCorreoCoautor().setText("");
		view.getTextfNombreCoautor().setText("");
		view.getTextfOrganizacionCoautor().setText("");
		view.getTextfGrupoInvestigacionCoautor().setText("");
	}
	
	
}
