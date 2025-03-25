package app.controller;

import java.util.ArrayList;
import java.util.List;

import app.dto.ArticuloDTO;
import app.dto.AutorDTO;
import app.enums.Rol;
import app.model.EnviarArticuloModel;
import app.model.VerMisArticulosModel;
import app.util.UserUtil;
import app.view.EnviarArticuloView;
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
				view.agregarArticulo(articulo.getIdArticulo(), articulo.getTitulo(), articulo.getPalabrasClave(), articulo.getPalabrasClaveTrack(), articulo.getResumen(), articulo.getNombreFichero());
			}
			
			
			
			// Inicializar la vista una vez que los datos están cargados.
			this.initView();
		}
		
		
		public void initController() {
			//Si se pulsa la checkbox de mostrar solo los articulos enviados por el autor, limpia la tabla y muestra solo los articulos enviados por el autor
			view.getChckbxSoloEnviadosPorMi().addActionListener(e -> {
				if (!view.getChckbxSoloEnviadosPorMi().isSelected()) {
					view.limpiarTablaArticulos();
					articulos = model.obtenerArticulos(email);
					for (ArticuloDTO articulo : articulos) {
						view.agregarArticulo(articulo.getIdArticulo(), articulo.getTitulo(), articulo.getPalabrasClave(), articulo.getPalabrasClaveTrack(), articulo.getResumen(), articulo.getNombreFichero());
					}
				} else { // Si no esta seleccionado, muestra todos los articulos en los que participa el autor
					view.limpiarTablaArticulos();
					articulos = model.obtenerArticulosEnviados(email);
					for (ArticuloDTO articulo : articulos) {
						view.agregarArticulo(articulo.getIdArticulo(), articulo.getTitulo(), articulo.getPalabrasClave(), articulo.getPalabrasClaveTrack(), articulo.getResumen(), articulo.getNombreFichero());
					}
				}
			});
			
			
			//Listener para cuando se selecciona un articulo de la tabla de articulos
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
						// Obtengo el id del track del articulo y con ese id obtengo el trackDTO, del que obtengo el nombre y lo muestro en la vista
						view.getLblTituloTrackArticulo().setText(model.obtenerTrackPorId(articulo.getIdTrack()).getNombre());
						// Miro QUIEN envio el articulo y lo muestro en la vista
						enviador = model.quienEnvia(id);
						view.getLblEnviadoPorArticulo().setText(enviador.getNombre());
						// Obtengo la lista de autores del articulo
						autores = model.obtenerAutores(id);
						// Relleno la tabla de autores con los autores encontrados
						view.limpiarTablaAutores();
						for (AutorDTO autor : autores) {
							view.agregarAutor(autor.getEmail(), autor.getNombre(), autor.getOrganizacion(), autor.getGrupoInvestigacion());
						}
					}
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
				}
				else {
					//Lo primero que hago es cerrar la ventana actual
					view.getFrame().dispose();
					//Creo el controlador de EnviarArticulo enviando los datos del articulo
					EnviarArticuloController controlador = new EnviarArticuloController(modeloEdicion, vistaEdicion, articulo);
					//Inicializo el controlador
					controlador.initController();
				}
			});
			
		}
		
		
		private void initView() {

			view.getFrame().setVisible(true);// TODO Auto-generated method stub
			// Relleno la label que indica el email del autor
			view.getLblAutorBusqueda().setText(email);
		}
	
}
