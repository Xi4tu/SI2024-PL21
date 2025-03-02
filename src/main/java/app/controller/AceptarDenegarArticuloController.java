package app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import app.dto.AceptarDenegarArticuloDTO;
import app.dto.RevisionArticuloRevisorDTO;
import app.enums.Rol;
import app.model.AceptarDenegarArticuloModel;
import app.util.UserUtil;
import app.view.AceptarDenegarArticuloView;
import giis.demo.util.SwingUtil;

public class AceptarDenegarArticuloController {

	private AceptarDenegarArticuloModel model;
	private AceptarDenegarArticuloView view;
	private String email;
	private List<AceptarDenegarArticuloDTO> articulos;
	private List<AceptarDenegarArticuloDTO> revisores;
	private List<AceptarDenegarArticuloDTO> experto;
	private List<AceptarDenegarArticuloDTO> decision;
	private List<AceptarDenegarArticuloDTO> comentarioAutor;
	private List<AceptarDenegarArticuloDTO> comentarioCoordinador;
	private List<AceptarDenegarArticuloDTO> nivelExperto;
	private List<AceptarDenegarArticuloDTO> valoraciones;
	private int valoracionFinal;
	DefaultListModel<AceptarDenegarArticuloDTO> listModel;

	private static final Rol ROL = Rol.COORDINADOR;

	public AceptarDenegarArticuloController(AceptarDenegarArticuloModel m, AceptarDenegarArticuloView v, String email) {
		this.model = m;
		this.view = v;
		this.email = email;

		if (!UserUtil.checkEmail(email, ROL.getNombre(), model.getDbUtil())) {
			// Detener la inicialización si el email es inválido.
			return;
		}

		// Llamar al backend para cargar los datos necesarios.
		if (!obtenerArticulosSinDecisionFinal()) {
			return;
		}

		// Inicializar la vista una vez que los datos están cargados.
		this.initView();

	}

	public void initController() {

		view.getcbRevisor().removeAllItems();

		view.getListArticulos().addListSelectionListener(e -> {
			AceptarDenegarArticuloDTO articuloSeleccionado = view.getListArticulos().getSelectedValue();
			// Verifica si el objeto o su id son nulos
			if (articuloSeleccionado == null || articuloSeleccionado.getId() == 0) {
				return;
			}

			// Guarda el id en una variable (objeto Integer)
			Integer idSeleccionado = articuloSeleccionado.getId();
			llenarComboBoxAutores(view.getListArticulos().getSelectedValue().getTitulo());

		});

		view.getcbRevisor().addActionListener(e -> {
			insertaNivelExperto();
			insertaDecision();
			insertaComentarioAutor();
			insertaComentarioCoordinador();
			calculaValoracion();

		});

		view.getbtnAceptar().addActionListener(e -> {
			int indice = view.getListArticulos().getSelectedIndex();
			if (view.getListArticulos().getSelectedValue() == null) {
				JOptionPane.showMessageDialog(null, "No hay ningñun artículo seleccionado", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				model.actualizarDecisionFinal("Aceptado", view.getListArticulos().getSelectedValue().getTitulo());
				SwingUtil.showMessage("Artículo aceptado correctamente", "Información",
						JOptionPane.INFORMATION_MESSAGE);
				listModel.removeElement(view.getListArticulos().getSelectedValue());

				if (listModel.isEmpty()) {
					SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					view.getFrame().dispose();
				}
				if (indice != 0) {
					view.getListArticulos().setSelectedIndex(indice - 1);
				} else {
					view.getListArticulos().setSelectedIndex(0);
				}
			}
		});

		view.getbtnRechazar().addActionListener(e -> {
			int indice = view.getListArticulos().getSelectedIndex();
			if (view.getListArticulos().getSelectedValue() == null) {
				JOptionPane.showMessageDialog(null, "No hay ningñun artículo seleccionado", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				model.actualizarDecisionFinal("Rechazado", view.getListArticulos().getSelectedValue().getTitulo());
				SwingUtil.showMessage("Artículo rechazado correctamente", "Información",
						JOptionPane.INFORMATION_MESSAGE);

				listModel.removeElement(view.getListArticulos().getSelectedValue());

				if (listModel.isEmpty()) {
					SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					view.getFrame().dispose();
				}
				if (indice != 0) {
					view.getListArticulos().setSelectedIndex(indice - 1);
				} else {
					view.getListArticulos().setSelectedIndex(0);
				}
			}
		});

	}

	public void initView() {
		view.getFrame().setVisible(true);
		// Asignar el modelo al JList de la vista
		view.getListArticulos().setModel(listModel);
	}

	// Función para obtener los artículos cuya decisión final sea Pendiente e
	// insertarlos en la JList
	private boolean obtenerArticulosSinDecisionFinal() {
		// Llamar al backend para obtener los artículos asignados
		articulos = model.obtenerArticulosSinDecisionFinal();
		// Convertir cada Articulo a ArticuloDTO
		List<AceptarDenegarArticuloDTO> listaDTO = new ArrayList<>();
		for (AceptarDenegarArticuloDTO articulo : articulos) {
			AceptarDenegarArticuloDTO dto = new AceptarDenegarArticuloDTO(articulo.getId(), articulo.getTitulo(),
					articulo.getNombreFichero(), articulo.getNombre());
			listaDTO.add(dto);
		}

		// Crear un modelo para el JList y agregar los DTOs
		listModel = new DefaultListModel<>();
		for (AceptarDenegarArticuloDTO dto : listaDTO) {
			listModel.addElement(dto);
		}

		// Si no hay articulos asignados, mostrar un mensaje y cerrar la vista
		if (articulos.isEmpty()) {
			SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		return true;
	}

	// Función para rellenar el combo box con los revisores asignados a ese artículo
	public void llenarComboBoxAutores(String nombreArticulo) {
		// Obtener los revisores asociados al artículo (debería ser un método filtrado)
		revisores = model.obtenerRevisores(nombreArticulo);
		// Limpiar el ComboBox antes de llenarlo
		view.getcbRevisor().removeAllItems();
		// Verificar si hay datos antes de llenar
		if (revisores != null && !revisores.isEmpty()) {
			for (AceptarDenegarArticuloDTO revisor : revisores) {
				view.getcbRevisor().addItem(revisor.getNombre());
			}
		} else {
			System.out.println("No hay revisores para el artículo: " + nombreArticulo);
		}

	}

	// Función para insertar el comentario del autor en su TextPane
	public void insertaComentarioAutor() {
		comentarioAutor = model.obtenerComentariosParaAutor((String) view.getcbRevisor().getSelectedItem(),
				view.getListArticulos().getSelectedValue().getTitulo());
		if (comentarioAutor != null && !comentarioAutor.isEmpty()) {
			view.gettpComentariosParaAutores().setText(comentarioAutor.get(0).getComentariosParaAutor());
		}

	}

	// Función para insertar el comentario del coordinador en su TextPane
	public void insertaComentarioCoordinador() {
		comentarioCoordinador = model.obtenerComentariosParaCoordinador((String) view.getcbRevisor().getSelectedItem(),
				view.getListArticulos().getSelectedValue().getTitulo());
		if (comentarioCoordinador != null && !comentarioCoordinador.isEmpty()) {
			view.gettpComentariosParaCoordinadores()
					.setText(comentarioCoordinador.get(0).getComentariosParaCoordinador());
		}

	}

	// Función para insertar el nivel de experto en su TextField
	public void insertaNivelExperto() {
		nivelExperto = model.obtenerNivelExperto((String) view.getcbRevisor().getSelectedItem(),
				view.getListArticulos().getSelectedValue().getTitulo());

		if (nivelExperto != null && !nivelExperto.isEmpty()) {
			view.gettfNivelDeExperto().setText(nivelExperto.get(0).getNivelExperto());
		}
	}

	// Función para insertar la decisión del revisor en su TextField
	public void insertaDecision() {
		decision = model.obtenerDecisionRevisor((String) view.getcbRevisor().getSelectedItem(),
				view.getListArticulos().getSelectedValue().getTitulo());

		if (decision != null && !decision.isEmpty()) {
			view.gettfDecision().setText(String.valueOf(decision.get(0).getDecisionRevisor()));
			if (decision.get(0).getDecisionRevisor() == 0) {
				view.gettfDecision().setText("");
			}
		}

	}

	// Función para calcular la suma de todas las decisiones de los revisores e
	// insertala en su TexFIeld
	public void calculaValoracion() {
		valoracionFinal = 0;
		view.gettfValoracion().removeAll();
		valoraciones = model.obtenerTodasDecisiones(view.getListArticulos().getSelectedValue().getTitulo());

		if (decision != null && !decision.isEmpty()) {
			for (AceptarDenegarArticuloDTO decisiones : valoraciones) {
				valoracionFinal += decisiones.getDecisionRevisor();
			}
			view.gettfValoracion().setText(String.valueOf(valoracionFinal));
		}
	}

}
