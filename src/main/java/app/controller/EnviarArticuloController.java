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
	//Flag parasaber si he iniciado el controlador con un articulo para enviar nueva version
	private boolean nuevaVersion = false;
	
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
	// Rellena los campos con los datos del articulo
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
	
	//Constructor de un controlador para cuando se envia una nueva version de un articulo
		// Rellena los campos con los datos del articulo
		public EnviarArticuloController(EnviarArticuloModel m, EnviarArticuloView v, ArticuloDTO articulo, boolean nuevaVersion) {
			this.nuevaVersion = true; //Lo primero: estoy mandando nueva version, asi que flag a true
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
				// Obtengo el email del autor seleccionado
				String emailAutor = (String) view.getTableListaDeAutores().getValueAt(fila, 0);
				// Si el autor seleccionado no es el autor principal que envía el artículo, se elimina de la lista de autores
				if (!emailAutor.equals(autor.getEmail())) {
					// Elimino el autor de la lista de autores
					autores.removeIf(a -> a.getEmail().equals(emailAutor));
					// Elimino el autor de la tabla de autores
					view.borrarAutor(fila);
				}
				else {
					view.mostrarMensajeError("No puedes eliminar al autor principal que envía el artículo");
				}
		}));
		
		// Listener del textField de busqueda de autores que crea una lista de autores que coincidan en su email/nombre
		// con lo buscado y que rellena el comboBox de autores con dicha lista de AutorDTO
		view.getTextfBusquedaAutor().addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			// Obtengo el texto del textField
			String texto = view.getTextfBusquedaAutor().getText();
			// Si el texto no es nulo o vacío, busco los autores que coincidan con el texto
			if (texto != null && !texto.isEmpty()) {
				// Busco los autores que coincidan con el texto
				List<AutorDTO> autoresFiltrados = model.buscarAutores(texto);
				// Relleno el comboBox de autores con la lista de autores filtrados
				view.setAutores(autoresFiltrados);
			}
		}));
		
		// Listener del comboBox de autores que rellena los campos de texto con los datos del autor seleccionado
		view.getCombobBusquedaAutor().addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			//Primero comprueba que el seleccionado no sea nulo (que por defecto lo sera, mete un autor vacio)
			if (view.getCombobBusquedaAutor().getSelectedItem() != null) {
				if (view.getCombobBusquedaAutor().getSelectedItem() != "") {
					// Obtengo el autor seleccionado (getSelectedItem() devuelve un Object que es SOLO el nombre del autor)
					String autorSeleccionadoNombre = (String) view.getCombobBusquedaAutor().getSelectedItem();
					// Busco el autor en la lista de autores
					AutorDTO autorSeleccionado = model.obtenerAutorPorNombre(autorSeleccionadoNombre);
					// Relleno los campos de texto con los datos del autor seleccionado
					view.getTextfCorreoCoautor().setText(autorSeleccionado.getEmail());
					view.getTextfNombreCoautor().setText(autorSeleccionado.getNombre());
					view.getTextfOrganizacionCoautor().setText(autorSeleccionado.getOrganizacion());
					view.getTextfGrupoInvestigacionCoautor().setText(autorSeleccionado.getGrupoInvestigacion());
				}
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
				
				//PUEDEN PASAR 3 COSAS: QUE ESTE ENVIANDO ARTICULO NUEVO
				// QUE ESTE EDITANDO UN ARTICULO YA EXISTENTE
				// O QUE ESTE ENVIANDO UNA NUEVA VERSION DE UN ARTICULO YA EXISTENTE
				if (!editando && !nuevaVersion) {
					// si NO estoy editando ni enviando nueva version, envio el articulo normalmente
					model.enviarArticulo(idTrack, view.getTextfTituloArticulo().getText(), view.getTextfPalabrasClaveArticulo().getText(), view.getPalabrasClaveArticuloString(), view.getTextfResumenArticulo().getText(), view.getTextfArchivoArticulo().getText(), autores);
				}
				else if (editando){
					// si SI estoy editando, actualizo el articulo
					model.editarArticulo(articuloEditandose.getIdArticulo(), idTrack, view.getTextfTituloArticulo().getText(), view.getTextfPalabrasClaveArticulo().getText(), view.getPalabrasClaveArticuloString(), view.getTextfResumenArticulo().getText(), view.getTextfArchivoArticulo().getText(), autores);
				}
				else if (nuevaVersion) {
					// si SI estoy enviando nueva version, añado la informacion a la tabla de versionArticulo
					model.enviarNuevaVersion(articuloEditandose.getIdArticulo(), view.getTextfPalabrasClaveArticulo().getText(), view.getTextfResumenArticulo().getText(), view.getTextfArchivoArticulo().getText());
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
		if ((editando || nuevaVersion) && !titulo.equals(articuloEditandose.getTitulo())) {
			view.mostrarMensajeError("No se puede modificar el título del artículo");
			return false;
		}
		
		
		
		// Y si NO estoy editando, compruebo que el articulo no exista ya en la base de datos
		if (!editando && !nuevaVersion && existeArticulo(view.getTextfTituloArticulo().getText())) {
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
		// si "nuevaVersion" es true, bloqueo todo lo siguiente:
		if (nuevaVersion) {
			// Campo de texto del titulo del articulo
			view.getTextfTituloArticulo().setEnabled(false);
			view.getTextfTituloArticulo().setEditable(false);
			view.getTextfTituloArticulo().setFocusable(false);
			// ComboBox de tracks
			view.getComboBoxSelectorTrackArticulo().setEnabled(false);
			view.getComboBoxSelectorTrackArticulo().setFocusable(false);
			// Boton de añadir autor
			view.getBtnAnadirAutor().setEnabled(false);
			view.getBtnAnadirAutor().setFocusable(false);
			// Boton de eliminar autor
			view.getBtnBorrarAutor().setEnabled(false);
			view.getBtnBorrarAutor().setFocusable(false);
			// Cuadro de busqueda de autores
			view.getTextfBusquedaAutor().setEnabled(false);
			view.getTextfBusquedaAutor().setFocusable(false);
			// ComboBox de autores
			view.getCombobBusquedaAutor().setEnabled(false);
			view.getCombobBusquedaAutor().setFocusable(false);
			// Combobox de palabras clave del track
			view.getComboBoxSelectorPalabrasDelTrack().setEnabled(false);
			view.getComboBoxSelectorPalabrasDelTrack().setFocusable(false);
			// Boton de agregar palabras clave del track
			view.getBtnAgregarPalabrasClaveDelTrack().setEnabled(false);
			view.getBtnAgregarPalabrasClaveDelTrack().setFocusable(false);		
			
		}
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
