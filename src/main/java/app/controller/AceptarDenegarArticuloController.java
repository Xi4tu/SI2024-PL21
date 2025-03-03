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
	DefaultListModel<AceptarDenegarArticuloDTO> listModelAuto;

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

		if (!obtenerArticulosAutomaticos()) {
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

		view.getListAutomaticos().addListSelectionListener(e -> {
			AceptarDenegarArticuloDTO articuloSeleccionado = view.getListAutomaticos().getSelectedValue();
			// Verifica si el objeto o su id son nulos
			if (articuloSeleccionado == null || articuloSeleccionado.getId() == 0) {
				return;
			}

			// Guarda el id en una variable (objeto Integer)
			Integer idSeleccionado = articuloSeleccionado.getId();
			llenarComboBoxAutores(view.getListAutomaticos().getSelectedValue().getTitulo());

		});

		view.getcbRevisor().addActionListener(e -> {
			insertaNivelExperto();
			insertaDecision();
			insertaComentarioAutor();
			insertaComentarioCoordinador();
			calculaValoracion();

		});

		view.getbtnAceptar().addActionListener(e -> {
			if (view.getListArticulos().getSelectedValue() == null
					&& view.getListAutomaticos().getSelectedValue() == null) {
				JOptionPane.showMessageDialog(null, "No hay ningñun artículo seleccionado", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				if (view.getListArticulos().getSelectedValue() != null) {
					int indice = view.getListArticulos().getSelectedIndex();
					model.actualizarDecisionFinal("Aceptado", view.getListArticulos().getSelectedValue().getTitulo());
					SwingUtil.showMessage("Artículo aceptado correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < view.getListAutomaticos().getModel().getSize(); i++) {

						if (view.getListArticulos().getSelectedValue().getTitulo()
								.equals(view.getListAutomaticos().getModel().getElementAt(i).getTitulo())) {
							listModelAuto.removeElement(view.getListAutomaticos().getModel().getElementAt(i));
						}
					}
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

				} else {
					int indice = view.getListAutomaticos().getSelectedIndex();
					model.actualizarDecisionFinal("Aceptado", view.getListAutomaticos().getSelectedValue().getTitulo());
					SwingUtil.showMessage("Artículo aceptado correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);

					for (int i = 0; i < view.getListArticulos().getModel().getSize(); i++) {

						if (view.getListAutomaticos().getSelectedValue().getTitulo()
								.equals(view.getListArticulos().getModel().getElementAt(i).getTitulo())) {
							listModel.removeElement(view.getListArticulos().getModel().getElementAt(i));
						}
					}

					listModelAuto.removeElement(view.getListAutomaticos().getSelectedValue());

					if (listModelAuto.isEmpty() && listModel.isEmpty()) {
						SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
								JOptionPane.INFORMATION_MESSAGE);
						view.getFrame().dispose();
					}
					if (indice != 0) {
						view.getListAutomaticos().setSelectedIndex(indice - 1);
					} else {
						view.getListAutomaticos().setSelectedIndex(0);
					}
				}

			}
		});

		view.getbtnRechazar().addActionListener(e -> {
			if (view.getListArticulos().getSelectedValue() == null
					&& view.getListAutomaticos().getSelectedValue() == null) {
				JOptionPane.showMessageDialog(null, "No hay ningñun artículo seleccionado", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				if (view.getListArticulos().getSelectedValue() != null) {
					int indice = view.getListArticulos().getSelectedIndex();
					model.actualizarDecisionFinal("Rechazado", view.getListArticulos().getSelectedValue().getTitulo());
					SwingUtil.showMessage("Artículo rechazado correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					for (int i = 0; i < view.getListAutomaticos().getModel().getSize(); i++) {

						if (view.getListArticulos().getSelectedValue().getTitulo()
								.equals(view.getListAutomaticos().getModel().getElementAt(i).getTitulo())) {
							listModelAuto.removeElement(view.getListAutomaticos().getModel().getElementAt(i));
						}
					}
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

				} else {
					int indice = view.getListAutomaticos().getSelectedIndex();
					model.actualizarDecisionFinal("Rechazado",
							view.getListAutomaticos().getSelectedValue().getTitulo());
					SwingUtil.showMessage("Artículo rechazado correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);

					for (int i = 0; i < view.getListArticulos().getModel().getSize(); i++) {

						if (view.getListAutomaticos().getSelectedValue().getTitulo()
								.equals(view.getListArticulos().getModel().getElementAt(i).getTitulo())) {
							listModel.removeElement(view.getListArticulos().getModel().getElementAt(i));
						}
					}

					listModelAuto.removeElement(view.getListAutomaticos().getSelectedValue());

					if (listModelAuto.isEmpty() && listModel.isEmpty()) {
						SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
								JOptionPane.INFORMATION_MESSAGE);
						view.getFrame().dispose();
					}
					if (indice != 0) {
						view.getListAutomaticos().setSelectedIndex(indice - 1);
					} else {
						view.getListAutomaticos().setSelectedIndex(0);
					}
				}

			}
		});

		view.gettpListas().addChangeListener(e -> {

			if (view.gettpListas().getSelectedIndex() == 0) {
				view.getbtnAceptarTodos().setEnabled(false);
				view.getListArticulos().setSelectedIndex(0);
				introducirDatos();
				view.getListAutomaticos().clearSelection();
			} else {
				view.getbtnAceptarTodos().setEnabled(true);
				view.getListArticulos().clearSelection();
				if (view.getListAutomaticos().getModel().getSize() > 0) {
					view.getListAutomaticos().setSelectedIndex(0);
					introducirDatos();
				}
			}
		});

		view.getbtnAceptarTodos().addActionListener(e -> {
			if (!listModelAuto.isEmpty()) {
				for (int i = 0; i < view.getListAutomaticos().getModel().getSize(); i++) {
					model.actualizarDecisionFinal("Aceptado",
							view.getListAutomaticos().getModel().getElementAt(i).getTitulo());
					// Si coinciden, quito los artículos de la otra lista
					for (int j = 0; j < view.getListArticulos().getModel().getSize(); j++) {
						if (view.getListAutomaticos().getModel().getElementAt(i).getTitulo()
								.equals(view.getListArticulos().getModel().getElementAt(j).getTitulo())) {
							listModel.removeElement(view.getListArticulos().getModel().getElementAt(j));
						}
					}
				}
				if (listModel.isEmpty()) {
					SwingUtil.showMessage("Artículos aceptados correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					view.getFrame().dispose();
				} else {
					listModelAuto.removeAllElements();
					view.gettpListas().setSelectedIndex(0);
					view.getListArticulos().setSelectedIndex(0);
					SwingUtil.showMessage("Artículos aceptados correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "No quedan artículos que aceptar", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});

	}

	public void initView() {
		view.getFrame().setVisible(true);
		// Asignar el modelo al JList de la vista
		view.getListArticulos().setModel(listModel);
		view.getListAutomaticos().setModel(listModelAuto);

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

	private boolean obtenerArticulosAutomaticos() {
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
		listModelAuto = new DefaultListModel<>();
		for (AceptarDenegarArticuloDTO dto : listaDTO) {
			if (valoracion(dto.getTitulo()) >= 3) {
				listModelAuto.addElement(dto);
			}
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
		if (view.getListArticulos().getSelectedValue() != null) {
			comentarioAutor = model.obtenerComentariosParaAutor((String) view.getcbRevisor().getSelectedItem(),
					view.getListArticulos().getSelectedValue().getTitulo());
		} else {
			comentarioAutor = model.obtenerComentariosParaAutor((String) view.getcbRevisor().getSelectedItem(),
					view.getListAutomaticos().getSelectedValue().getTitulo());
		}

		if (comentarioAutor != null && !comentarioAutor.isEmpty()) {
			view.gettpComentariosParaAutores().setText(comentarioAutor.get(0).getComentariosParaAutor());
		}

	}

	// Función para insertar el comentario del coordinador en su TextPane
	public void insertaComentarioCoordinador() {
		if (view.getListArticulos().getSelectedValue() != null) {
			comentarioCoordinador = model.obtenerComentariosParaCoordinador(
					(String) view.getcbRevisor().getSelectedItem(),
					view.getListArticulos().getSelectedValue().getTitulo());
		} else {
			comentarioCoordinador = model.obtenerComentariosParaCoordinador(
					(String) view.getcbRevisor().getSelectedItem(),
					view.getListAutomaticos().getSelectedValue().getTitulo());
		}

		if (comentarioCoordinador != null && !comentarioCoordinador.isEmpty()) {
			view.gettpComentariosParaCoordinadores()
					.setText(comentarioCoordinador.get(0).getComentariosParaCoordinador());
		}

	}

	// Función para insertar el nivel de experto en su TextField
	public void insertaNivelExperto() {
		if (view.getListArticulos().getSelectedValue() != null) {
			nivelExperto = model.obtenerNivelExperto((String) view.getcbRevisor().getSelectedItem(),
					view.getListArticulos().getSelectedValue().getTitulo());
		} else {
			nivelExperto = model.obtenerNivelExperto((String) view.getcbRevisor().getSelectedItem(),
					view.getListAutomaticos().getSelectedValue().getTitulo());
		}

		if (nivelExperto != null && !nivelExperto.isEmpty()) {
			view.gettfNivelDeExperto().setText(nivelExperto.get(0).getNivelExperto());
		}
	}

	// Función para insertar la decisión del revisor en su TextField
	public void insertaDecision() {
		if (view.getListArticulos().getSelectedValue() != null) {
			decision = model.obtenerDecisionRevisor((String) view.getcbRevisor().getSelectedItem(),
					view.getListArticulos().getSelectedValue().getTitulo());
		} else {
			decision = model.obtenerDecisionRevisor((String) view.getcbRevisor().getSelectedItem(),
					view.getListAutomaticos().getSelectedValue().getTitulo());
		}

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

		if (view.getListArticulos().getSelectedValue() != null) {
			valoraciones = model.obtenerTodasDecisiones(view.getListArticulos().getSelectedValue().getTitulo());
		} else {
			valoraciones = model.obtenerTodasDecisiones(view.getListAutomaticos().getSelectedValue().getTitulo());
		}

		if (decision != null && !decision.isEmpty()) {
			for (AceptarDenegarArticuloDTO decisiones : valoraciones) {
				valoracionFinal += decisiones.getDecisionRevisor();
			}
			view.gettfValoracion().setText(String.valueOf(valoracionFinal));
		}
	}

	public int valoracion(String titulo) {
		int valoracionFinal = 0;
		valoraciones = model.obtenerTodasDecisiones(titulo);

		if (valoraciones != null && !valoraciones.isEmpty()) {
			for (AceptarDenegarArticuloDTO decisiones : valoraciones) {
				valoracionFinal += decisiones.getDecisionRevisor();
			}
		}
		return valoracionFinal;
	}

	public void introducirDatos() {
		insertaNivelExperto();
		insertaDecision();
		insertaComentarioAutor();
		insertaComentarioCoordinador();
		calculaValoracion();
	}

}
