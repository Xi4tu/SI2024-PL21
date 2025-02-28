package app.controller;

import java.util.ArrayList;
import java.util.List;

import app.dto.ArticuloDTOlite;
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
		
		private List<ArticuloDTOlite> articulos;
		private AutorDTO enviador;
		private List<AutorDTO> autores;
		
		
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
			for (ArticuloDTOlite articulo : articulos) {
				view.agregarArticulo(articulo.getidArticulo(), articulo.getTitulo(), articulo.getPalabrasClave(), articulo.getResumen(), articulo.getNombreFichero());
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
					for (ArticuloDTOlite articulo : articulos) {
						view.agregarArticulo(articulo.getidArticulo(), articulo.getTitulo(), articulo.getPalabrasClave(), articulo.getResumen(), articulo.getNombreFichero());
					}
				} else { // Si no esta seleccionado, muestra todos los articulos en los que participa el autor
					view.limpiarTablaArticulos();
					articulos = model.obtenerArticulosEnviados(email);
					for (ArticuloDTOlite articulo : articulos) {
						view.agregarArticulo(articulo.getidArticulo(), articulo.getTitulo(), articulo.getPalabrasClave(), articulo.getResumen(), articulo.getNombreFichero());
					}
				}
			});
			
			
			//Cuando se pulsa un articulo de la tabla, se muestra la informacion del articulo
			view.getTableArticulosDelAutor().getSelectionModel().addListSelectionListener(e -> {
				if (!e.getValueIsAdjusting()) {
					int fila = view.getTableArticulosDelAutor().getSelectedRow();
					if (fila != -1) {
						// Obtengo el id del articulo seleccionado
						int id = (int) view.getTableArticulosDelAutor().getValueAt(fila, 0);
						// Busco el articulo en la lista de articulos
						ArticuloDTOlite articulo = articulos.stream().filter(a -> a.getidArticulo() == id).findFirst().get();
						// Relleno los campos de la vista con la informacion del articulo
						view.getLblIdArticulo().setText(String.valueOf(articulo.getidArticulo()));
						view.getLblTituloArticulo().setText(articulo.getTitulo());
						view.getLblPalabrasClaveArticulo().setText(articulo.getPalabrasClave());
						view.getLblResumenArticulo().setText(articulo.getResumen());
						view.getLblFicheroArticulo().setText(articulo.getNombreFichero());
						view.getLblFechaEnvioArticulo().setText(articulo.getFechaEnvio());
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
		}
		
		
		private void initView() {

			view.getFrame().setVisible(true);// TODO Auto-generated method stub
			// Relleno la label que indica el email del autor
			view.getLblAutorBusqueda().setText(email);
		}
	
}
