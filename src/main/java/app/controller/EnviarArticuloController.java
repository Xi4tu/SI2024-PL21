package app.controller;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import app.dto.ArticuloDTO;
import app.dto.AutorDTO;
import app.dto.ConferenciaDTO;
import app.dto.TrackDTO;
import app.enums.Rol;
import app.model.EnviarArticuloModel;
import app.util.UserUtil;
import app.view.EnviarArticuloView;
import giis.demo.util.SwingUtil;
import app.util.UserUtil;

public class EnviarArticuloController {
	// MODIFICACION (prueba)
	// Variables
	private EnviarArticuloModel model;
	private EnviarArticuloView view;
	private String email;
	private static final Rol ROL = Rol.AUTOR;
	
	private AutorDTO autor;
	private ArrayList<AutorDTO> autores = new ArrayList<AutorDTO>();
	// lista de tracks
	private List<TrackDTO> tracks;
	
	//Articulo por si lo cargan para editar
	private ArticuloDTO articuloEditandose;
	
	//Flag para saber si he iniciado el controlador con un articulo para editar
	private boolean editando = false;
	
	/*
	 * Constructor del controlador
	 */
	
	public EnviarArticuloController(EnviarArticuloModel m, EnviarArticuloView v, String email) {
		this.model = m;
		this.view = v;
		this.email = email;
		
		// Informacion del autor
		autor = model.obtenerAutor(email);
		//Añado el autor a la lista de autores
		autores.add(autor);
		//Inserto el DTO del autor como una nueva fila en la tabla de autores con el metodo agregarAutor
		view.agregarAutor(autor.getEmail(), autor.getNombre(), autor.getOrganizacion(), autor.getGrupoInvestigacion());
		
		// Guardo la lista de tracks
		tracks = model.obtenerTracks();
		// Inserto los tracks en el comboBox de la vista
		view.setTracks(tracks);
		// Inserto las palabras clave del primer track en el comboBox de la vista
		view.setPalabrasClaveTrack(tracks.get(0).getPalabrasClaveLista());
		

		// Inicializar la vista una vez que los datos están cargados.
		this.initView();
	}
	
	//Constructor de un controlador para cuando se edita un articulo
	//Llama al constructor anterior y ademas rellena los campos con los datos del articulo
	public EnviarArticuloController(EnviarArticuloModel m, EnviarArticuloView v, ArticuloDTO articulo) {
		editando = true; //Lo primero: estoy editando, asi que flag a true
		this.model = m;
		this.view = v;
		this.articuloEditandose = articulo;
		
		// Guardo el autor del articulo
		this.autor = model.obtenerAutorPorIdArticulo(articulo.getIdArticulo());
		this.email = autor.getEmail();
		// Guardo la lista de tracks
		tracks = model.obtenerTracks();
		// Inserto los tracks en el comboBox de la vista
		view.setTracks(tracks);
		
		// Relleno los campos con los datos del articulo
		view.getTextfTituloArticulo().setText(articulo.getTitulo());
		view.getTextfPalabrasClaveArticulo().setText(articulo.getPalabrasClave());
		view.getTextfResumenArticulo().setText(articulo.getResumen());
		view.getTextfArchivoArticulo().setText(articulo.getNombreFichero());
		
		// Obtengo el track del articulo
		TrackDTO track = model.obtenerTrackPorId(articulo.getIdTrack());
		// Lo marco en el comboBox de tracks
		view.getComboBoxSelectorTrackArticulo().setSelectedItem(track.getNombre());
		// Inserto las palabras clave del track marcado en el comboBox de la vista
		view.setPalabrasClaveTrack(track.getPalabrasClaveLista());
		// Relleno el label de texto de palabras clave del track del articulo con las palabras clave del track del articulo
		view.setPalabrasClaveTrack(articulo.getPalabrasClaveTrack());
		
		//Relleno la lista de autores con los autores del articulo
		List<AutorDTO> autoresArticulo = model.obtenerAutoresPorId(articulo.getIdArticulo());
		for (AutorDTO autor : autoresArticulo) {
			autores.add(autor);
			view.agregarAutor(autor.getEmail(), autor.getNombre(), autor.getOrganizacion(), autor.getGrupoInvestigacion());
		}
		
		// Inicializar la vista una vez que los datos están cargados.
		this.initView();
	}
	
	
	public void initController() {
		// Listener del comboBox de tracks que muestra las palabras clave del track seleccionado 
		// y resetea las palabras clave del articulo seleccionadas de tracks previamente seleccionados
		view.getComboBoxSelectorTrackArticulo().addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			// Obtengo el nombre del track seleccionado
			String nombreTrack = (String) view.getComboBoxSelectorTrackArticulo().getSelectedItem();
			// Guardo el track seleccionado
			TrackDTO track = obtenerTrackSeleccionado();			
			// Limpio el comboBox de palabras clave del track
			view.limpiarPalabrasClaveTrack();
			// Muestro las palabras clave del track seleccionado
			view.setPalabrasClaveTrack(track.getPalabrasClaveLista());
			// Al seleccionar un track, limpio el label de texto de palabras clave del articulo seleccionadas
			// de tracks previamente seleccionados
			view.resetearPalabrasClaveTrack();
		}));

		// Listener del botón de agregar palabras clave que añade la palabra seleccionada del comboBox 
		// de palabras clave del track al label de texto de palabras clave del articulo
		view.getBtnAgregarPalabrasClaveDelTrack().addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			// Obtengo la palabra clave seleccionada
			String palabraClave = (String) view.getComboBoxSelectorPalabrasDelTrack().getSelectedItem();
			// Añado la palabra clave al label de texto de palabras clave del articulo
			view.agregarPalabraClave(palabraClave);
		}));
		
		// Listener del botón de cancelar
		view.getBtnCancelar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> view.getFrame().dispose()));
		
		// Listener del botón de añadir autores que añade un autor a la lista de autores
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
		
		// Listener del botón de eliminar autor que elimina el autor seleccionado de la lista de autores
		view.getBtnBorrarAutor().addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			// Obtengo la fila seleccionada
			int fila = view.getTableListaDeAutores().getSelectedRow();
				//Elimino solo si no es el autor principal que envia
				if (fila != 0) {
					// Elimino el autor de la lista de autores
					autores.remove(fila);
					// Elimino la fila seleccionada de la tabla de autores
					view.borrarAutor(fila);
				}
				else {
					view.mostrarMensajeError("No puedes eliminar al autor principal que envía el artículo");
				}
		}));
		
		// Listener del botón de enviar artículo que envía el artículo a la base de datos
		view.getBtnEnviar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			// Si los datos del articulo son correctos
			if (validarDatosArticulo(view.getTextfTituloArticulo().getText(), view.getTextfPalabrasClaveArticulo().getText(), view.getTextfResumenArticulo().getText(), view.getTextfArchivoArticulo().getText())) {
						
				// Si los datos son correctos y el articulo no existe ya, se envia el articulo
				// No verifico si la lista de autores esta vacia porque el autor que envia el articulo ya esta en la lista por defecto
				
				// Pero antes, cojo el track seleccionado y guardo su id para enviarlo al modelo
				TrackDTO track = obtenerTrackSeleccionado();
				int idTrack = track.getIdTrack();
				
				//PUEDEN PASAR 2 COSAS: QUE ESTE EDITANDO O QUE NO
				if (!editando) {
					// si NO estoy editando, envio el articulo normalmente
					model.enviarArticulo(idTrack, view.getTextfTituloArticulo().getText(), view.getTextfPalabrasClaveArticulo().getText(), view.getPalabrasClaveArticuloString(), view.getTextfResumenArticulo().getText(), view.getTextfArchivoArticulo().getText(), autores);
				}
				else {
					// si SI estoy editando, actualizo el articulo
					model.editarArticulo(articuloEditandose.getIdArticulo(), idTrack, view.getTextfTituloArticulo().getText(), view.getTextfPalabrasClaveArticulo().getText(), view.getPalabrasClaveArticuloString(), view.getTextfResumenArticulo().getText(), view.getTextfArchivoArticulo().getText(), autores);
				}
				
				
				
				// Cierro la ventana
				view.getFrame().dispose();
			} else {
				// Si los datos no son correctos, se muestra un mensaje de error
				view.mostrarMensajeError("Datos de artículo incorrectos o inválidos");
			}
		}));
	}


	//Valida si los datos del autor son correctos y no nulos
	// GRUPO DE INVESTIGACION PUEDE SER NULO
	public boolean validarDatosAutor(String email, String nombre, String organizacion, String grupoInvestigacion) {
		if ((nombre == null || organizacion == null)
				|| nombre.isEmpty() || organizacion.isEmpty()
				|| (!UserUtil.checkFormatoEmail(email))) { // Compruebo que el email tenga un formato correcto
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
		
		// Compruebo que la lista de palabras del track no esté vacía
		if (view.getPalabrasClaveArticuloLista().length == 0) {
			view.mostrarMensajeError("Debes seleccionar al menos una palabra clave del track");
			return false;
		}
		
		// Si estoy editando, compruebo que no se haya modificado el titulo del articulo porque no se puede modificar, da error
		if (editando && !titulo.equals(articuloEditandose.getTitulo())) {
			view.mostrarMensajeError("No se puede modificar el título del artículo");
			return false;
		}
		
		// Y si NO estoy editando, compruebo que el articulo no exista ya en la base de datos
		if (!editando && existeArticulo(view.getTextfTituloArticulo().getText())) {
			view.mostrarMensajeError("El artículo ya existe en la base de datos");
			return false;
		}	

		// Compruebo que no se pase la fecha límite de envío de artículos
		TrackDTO track = obtenerTrackSeleccionado();
		// Antes de seguir, extraigo el id Conferencia del track seleccionado
		String idConferencia = track.getIdConferencia();
		// Extraigo la conferenciaDTO con esa id
		ConferenciaDTO conferencia = model.obtenerConferencia(idConferencia);
		// Extraigo el deadline de la conferencia
		Date deadline = conferencia.getDeadlineDate();
		// Extraigo la fecha actual 
		Date fechaActual = model.fechaDate();
		// Si la fecha actual es posterior al deadline, muestro un mensaje de error
		if (fechaActual.after(deadline)) {
			view.mostrarMensajeError("La fecha límite de envío de artículos ha pasado");
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
	
	//Metodo para obtener el DTO del track seleccionado en el comboBox
	public TrackDTO obtenerTrackSeleccionado() {
		// Obtengo el nombre del track seleccionado
		String nombreTrack = (String) view.getComboBoxSelectorTrackArticulo().getSelectedItem();
		// Busco el track seleccionado en la lista de tracks
		TrackDTO track = tracks.stream().filter(t -> t.getNombre().equals(nombreTrack)).findFirst().orElse(null);
		return track;
	}
	

	
}
