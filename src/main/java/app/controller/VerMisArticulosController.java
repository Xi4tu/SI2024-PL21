package app.controller;

import java.util.ArrayList;
import java.util.List;

import app.dto.ArticuloDTO;
import app.dto.AutorDTO;
import app.enums.Rol;
import app.model.EnviarArticuloModel;
import app.model.RevisionArticuloAutorModel;
import app.model.VerMisArticulosModel;
import app.util.UserUtil;
import app.view.EnviarArticuloView;
import app.view.RevisionArticuloAutorView;
import app.view.VerMisArticulosView;
import giis.demo.util.SwingUtil;

public class VerMisArticulosController {

	// Variables
	private VerMisArticulosModel model;
	private VerMisArticulosView view;
	private String email;
	private static final Rol ROL = Rol.AUTOR;

	private List<ArticuloDTO> articulos;
	private AutorDTO enviador;
	private List<AutorDTO> autores;

	EnviarArticuloView vistaEdicion = new EnviarArticuloView();
	EnviarArticuloModel modeloEdicion = new EnviarArticuloModel();
	ArticuloDTO articulo; // Articulo seleccionado en la tabla de articulos

	// Constructor del controlador
	public VerMisArticulosController(VerMisArticulosModel m, VerMisArticulosView v, String email) {
		this.model = m;
		this.view = v;
		this.email = email;

		if (!UserUtil.checkEmail(email, ROL.getNombre(), model.getDbUtil())) {
			// Detener la inicialización si el email es inválido.
			return;
		}

		// Busco para el email del autor todos los articulos que ha enviado
		articulos = model.obtenerArticulos(email);
		// Relleno la tabla de articulos con los articulos encontrados
		for (ArticuloDTO articulo : articulos) {
			view.agregarArticulo(articulo.getIdArticulo(), articulo.getTitulo(), articulo.getPalabrasClave(),
					articulo.getPalabrasClaveTrack(), articulo.getResumen(), articulo.getNombreFichero());
		}

		// Inicializar la vista una vez que los datos están cargados.
		this.initView();
	}

	public void initController() {
		// Si se pulsa la checkbox de mostrar solo los articulos enviados por el autor,
		// limpia la tabla y muestra solo los articulos enviados por el autor
		view.getChckbxSoloEnviadosPorMi().addActionListener(e -> {
			if (!view.getChckbxSoloEnviadosPorMi().isSelected()) {
				view.limpiarTablaArticulos();
				articulos = model.obtenerArticulos(email);
				for (ArticuloDTO articulo : articulos) {
					view.agregarArticulo(articulo.getIdArticulo(), articulo.getTitulo(), articulo.getPalabrasClave(),
							articulo.getPalabrasClaveTrack(), articulo.getResumen(), articulo.getNombreFichero());
				}
			} else { // Si esta seleccionado, muestra solo los articulos enviados por el autor
				view.limpiarTablaArticulos();
				articulos = model.obtenerArticulosEnviados(email);
				for (ArticuloDTO articulo : articulos) {
					view.agregarArticulo(articulo.getIdArticulo(), articulo.getTitulo(), articulo.getPalabrasClave(),
							articulo.getPalabrasClaveTrack(), articulo.getResumen(), articulo.getNombreFichero());
				}
			}
		});
		
		// Si se pulsa la checkbox de mostrar solo los articulos con versiones
		// limpia la tabla y muestra solo los articulos con version
		view.getChckbxConVersion().addActionListener(e -> {
			if (!view.getChckbxConVersion().isSelected()) {
				view.limpiarTablaArticulos();
				articulos = model.obtenerArticulos(email);
				for (ArticuloDTO articulo : articulos) {
					view.agregarArticulo(articulo.getIdArticulo(), articulo.getTitulo(), articulo.getPalabrasClave(),
							articulo.getPalabrasClaveTrack(), articulo.getResumen(), articulo.getNombreFichero());
				}
			} else { // Si esta seleccionado, muestra solo los articulos con version
				// los articulos con version son aquellos que tienen una entrada en la tabla versionArticulo para su idArticulo
				view.limpiarTablaArticulos();
				articulos = model.obtenerArticulosConVersionAutor(email);
				for (ArticuloDTO articulo : articulos) {
					view.agregarArticulo(articulo.getIdArticulo(), articulo.getTitulo(), articulo.getPalabrasClave(),
							articulo.getPalabrasClaveTrack(), articulo.getResumen(), articulo.getNombreFichero());
				}
			}
		});
		
		// Listener para el boton de eliminar version, que elimina la version del articulo de la tabla VersionArticulo
		view.getBtnEliminarVersion().addActionListener(e -> {
			// Si articulo es null, no se ha seleccionado ningun articulo, asiq error
			if (articulo == null) {
				view.mostrarMensajeError("No se ha seleccionado ningún artículo");
				return;
			}
			// Si el autor actual no es el que envio el articulo, no se puede editar
			if (!enviador.getEmail().equals(email)) {
				view.mostrarMensajeError("No se puede eliminar la versión del artículo. No fue enviado por usted.");
				return;
			}
			// Si el articulo no tiene version, no se puede eliminar
			if (!model.tieneVersion(articulo.getIdArticulo())) {
				view.mostrarMensajeError("Este artículo no tiene versiones para eliminar.");
				return;
			} else {
				// Elimino la version del articulo
				model.eliminarVersion(articulo.getIdArticulo());
				// Muestro un mensaje de exito
				view.mostrarMensajeError("Versión eliminada correctamente");
				// Elimino el articulo de la tabla SOLO si la opcion de mostrar solo los articulos con versiones esta seleccionada
				if (view.getChckbxConVersion().isSelected()) {
					view.eliminarArticulo(articulo.getIdArticulo());
				}
			}
		});

		// Listener para cuando se selecciona un articulo de la tabla de articulos
		view.getTableArticulosDelAutor().getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int fila = view.getTableArticulosDelAutor().getSelectedRow();
				if (fila != -1) {
					// Obtengo el id del articulo seleccionado
					int id = (int) view.getTableArticulosDelAutor().getValueAt(fila, 0);
					// Busco el articulo en la lista de articulos
					articulo = articulos.stream().filter(a -> a.getIdArticulo() == id).findFirst().get();
					// Relleno los campos de la vista con la informacion del articulo
					view.getLblIdArticulo().setText(String.valueOf(articulo.getIdArticulo()));
					view.getLblTituloArticulo().setText(articulo.getTitulo());
					view.getLblPalabrasClaveArticulo().setText(articulo.getPalabrasClave());
					view.getLblPalabrasClaveTrackArticulo().setText(articulo.getPalabrasClaveTrack());
					view.getLblResumenArticulo().setText(articulo.getResumen());
					view.getLblFicheroArticulo().setText(articulo.getNombreFichero());
					view.getLblFechaEnvioArticulo().setText(articulo.getFechaEnvio());
					view.getLblFechaModificacionArticulo().setText(articulo.getFechaModificacion());
					view.getLblDecision().setText(articulo.getDecisionFinal());
					view.getLblEstado().setText(convertirEstado(articulo.getValoracionGlobal()));
					// Obtengo el id del track del articulo y con ese id obtengo el trackDTO, del
					// que obtengo el nombre y lo muestro en la vista
					view.getLblTituloTrackArticulo()
							.setText(model.obtenerTrackPorId(articulo.getIdTrack()).getNombre());
					// Miro QUIEN envio el articulo y lo muestro en la vista
					enviador = model.quienEnvia(id);
					view.getLblEnviadoPorArticulo().setText(enviador.getNombre());
					// Obtengo la lista de autores del articulo
					autores = model.obtenerAutores(id);
					// Relleno la tabla de autores con los autores encontrados
					view.limpiarTablaAutores();
					for (AutorDTO autor : autores) {
						view.agregarAutor(autor.getEmail(), autor.getNombre(), autor.getOrganizacion(),
								autor.getGrupoInvestigacion());
					}
				}
			}
		});

		// Listener para el boton de Ver Decision
		view.getBtnVerDecision().addActionListener(e -> {
			// Reacciona SOLO si la decision esta tomada (osea, decisionFinal = Aceptado o Rechazado)
			if (articulo.getDecisionFinal().equals("Aceptado") || articulo.getDecisionFinal().equals("Rechazado")) {
				// Voy a mostrar la vista de la Revision del articulo, pasandole ya el email del autor
				// Creo el controlador de RevisionArticuloAutorController enviando el mail del autor 
				// y el id del articulo para que lo elija la vista inmediatamente para mostrar sus datos
				RevisionArticuloAutorController controlador = new RevisionArticuloAutorController(
						new RevisionArticuloAutorModel(), new RevisionArticuloAutorView(), email, articulo.getIdArticulo());
				// Inicializo el controlador
				controlador.initController();
				
			} else {
				// Si no esta tomada, muestro un mensaje de error
				view.mostrarMensajeError("La decisión final de este artículo aún no ha sido tomada.");
			}
		});

		// Listener para cuando se pulsa el boton de editar articulo
		view.getBtnEditarArticulo().addActionListener(e -> {
			// Si articulo es null, no se ha seleccionado ningun articulo, asiq error
			if (articulo == null) {
				view.mostrarMensajeError("No se ha seleccionado ningún artículo");
				return;
			}
			// Si ha pasado la fecha de deadline, no se puede editar
			if (!model.fechaAntesDeDeadline(articulo.getIdTrack())) {
				view.mostrarMensajeError("No se puede editar el artículo. Fuera de fecha de envío.");
				return;
			}
			// Si el autor actual no es el que envio el articulo, no se puede editar
			if (!enviador.getEmail().equals(email)) {
				view.mostrarMensajeError("No se puede editar el artículo. No fue enviado por usted.");
				return;
			} else {
				// Lo primero que hago es cerrar la ventana actual
				view.getFrame().dispose();
				// Creo el controlador de EnviarArticulo enviando los datos del articulo
				EnviarArticuloController controlador = new EnviarArticuloController(modeloEdicion, vistaEdicion,
						articulo);
				// Inicializo el controlador
				controlador.initController();
			}
		});
		
		// Listener para cuando se pulsa el boton de nueva version
		view.getBtnNuevaVersion().addActionListener(e -> {
			// Si articulo es null, no se ha seleccionado ningun articulo, asiq error
			if (articulo == null) {
				view.mostrarMensajeError("No se ha seleccionado ningún artículo");
				return;
			}
			// Si el autor actual no es el que envio el articulo, no se puede editar, y se menciona el nombre del autor que si envio
			if (!enviador.getEmail().equals(email)) {
				// Obtengo el nombre del autor que SI envio el articulo
				String nombreEnviador = model.quienEnvia(articulo.getIdArticulo()).getNombre();
				view.mostrarMensajeError("Artículo enviado por " + nombreEnviador + ". No se puede editar.");
				return;
			} else {
				// Lo primero que hago es cerrar la ventana actual
				view.getFrame().dispose();
				// Creo el controlador de EnviarArticulo enviando los datos del articulo
				EnviarArticuloController controlador = new EnviarArticuloController(modeloEdicion, vistaEdicion,
						articulo, true); // true porque es una nueva version, para elegir el constructor correcto
				// Inicializo el controlador
				controlador.initController();
			}
		});

	}

	private void initView() {

		view.getFrame().setVisible(true);// TODO Auto-generated method stub
		// Relleno la label que indica el email del autor
		view.getLblAutorBusqueda().setText(email);
	}

	// Metodo que convierte el estado del articulo a un string mas legible segun el
	// enum DecisionRevisor
	private String convertirEstado(String estado) {
		// -2 -1 0 1 2 = Rechazar Fuerte, Rechazar Debil, Sin Valorar, Aceptar Debil,
		// Aceptar Fuerte
		//Si estado es null, devuelvo "Sin Valorar" de una
		if (estado == null) {
			return "Sin Valorar";
		}
		switch (estado) {
		case "-2":
			return "Rechazar Fuerte";
		case "-1":
			return "Rechazar Débil";
		case "0":
			return "Sin Valorar";
		case "1":
			return "Aceptar Débil";
		case "2":
			return "Aceptar Fuerte";
		default:
			return "Sin Valorar";
		}
	}

}
