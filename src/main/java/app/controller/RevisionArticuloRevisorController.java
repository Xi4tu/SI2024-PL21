package app.controller;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import app.dto.RevisionArticuloRevisionDTO;
import app.dto.RevisionArticuloRevisorDTO;
import app.dto.RevisionAutorDTO;
import app.enums.Rol;
import app.model.GestionarDiscusionesCoordinadorModel;
import app.model.PedirColaboradorModel;
import app.model.RevisionArticuloRevisorModel;
import app.util.UserUtil;
import app.view.GestionarDiscusionesCoordinadorView;
import app.view.PedirColaboradorView;
import app.view.RevisionArticuloRevisorView;
import giis.demo.util.SwingUtil;

public class RevisionArticuloRevisorController {
	private RevisionArticuloRevisorModel model;
	private RevisionArticuloRevisorView view;
	private List<RevisionArticuloRevisorDTO> articulos;
	private List<RevisionArticuloRevisorDTO> articulosCoolab;
	private List<RevisionArticuloRevisorDTO> nombre;
	DefaultListModel<RevisionArticuloRevisorDTO> listModel;
	private String email;
	private static final Rol ROL = Rol.REVISOR;

	/*
	 * Constructor del controlador
	 */
	public RevisionArticuloRevisorController(RevisionArticuloRevisorModel m, RevisionArticuloRevisorView v,
			String email) {
		this.model = m;
		this.view = v;
		this.email = email;
		if (!UserUtil.checkEmail(email, ROL.getNombre(), model.getDbUtil())) {
			// Detener la inicialización si el email es inválido.
			return;
		}
		// Llamar al backend para cargar los datos necesarios.
		if (!obtenerArticulosAsignados()) {
			return;
		}
		// Inicializar la vista una vez que los datos están cargados.
		this.initView();
	}

	/*
	 * Método que se encarga de inicializar el controlador
	 */
	public void initController() {
		// Listener botón "Enviar Revisión"
		view.getBtnEnviarRevision().addActionListener(e -> SwingUtil.exceptionWrapper(() -> enviarRevision()));

		// Listener al seleccionar un artículo
		view.getListArticulos().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int index = view.getListArticulos().getSelectedIndex();
				if (index >= 0) {
					RevisionArticuloRevisorDTO articulo = articulos.get(index);

					// 🔄 Cargar revisores del artículo al combo
					List<String> revisores = model.obtenerRevisoresDelArticulo(articulo.getId());
					JComboBox<String> comboRevisor = view.getComboBoxRevisor();

					comboRevisor.removeAllItems();
					for (String r : revisores) {
						comboRevisor.addItem(r);
					}

					// Seleccionar por defecto al usuario actual
					comboRevisor.setSelectedItem(email);
				}
			}
		});

		view.getBtnPedirColaborador().addActionListener(e -> {
			if (view.getListArticulos().getSelectedValue() == null) {
				SwingUtil.showMessage("No has seleccionado ningún artículo", "ERROR", JOptionPane.ERROR_MESSAGE);
			} else {
				PedirColaboradorController controller = new PedirColaboradorController(new PedirColaboradorModel(),
						new PedirColaboradorView(), email, view.getListArticulos().getSelectedValue().getId());
				controller.initController();
			}
		});

		// Listener para alternar entre "Pendientes" y "Revisados"
		view.getComboBoxPendientes().addItemListener(e -> {
			if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
				String opcion = (String) e.getItem();
				boolean esPendientes = opcion.equals("Pendientes");

				actualizarListaArticulos(esPendientes);

				// 🧹 Limpiar combo revisores si estamos en "Pendientes"
				JComboBox<String> comboRevisor = view.getComboBoxRevisor();
				if (esPendientes) {
					comboRevisor.removeAllItems();
					comboRevisor.setEnabled(false); // 🔒 Desactivar
				} else {
					comboRevisor.setEnabled(true); // 🔓 Activar solo en "Ya revisados"
				}
			}
		});

		// Listener al cambiar el revisor seleccionado en el combo
		view.getComboBoxRevisor().addActionListener(e -> {
			int indexArticulo = view.getListArticulos().getSelectedIndex();
			if (indexArticulo < 0)
				return;

			RevisionArticuloRevisorDTO articulo = articulos.get(indexArticulo);
			String revisorSeleccionado = (String) view.getComboBoxRevisor().getSelectedItem();
			if (revisorSeleccionado == null || revisorSeleccionado.isEmpty())
				return;

			// 🔍 Obtener la revisión del revisor seleccionado
			RevisionAutorDTO revisionAutor = model.obtenerRevisionAutor(articulo.getId(), revisorSeleccionado);
			RevisionArticuloRevisionDTO revisionCoord = model.obtenerRevisionCoordinador(articulo.getId(),
					revisorSeleccionado);

			// Cargar comentarios para autor
			if (revisionAutor != null) {
				view.getTxtComentariosAutores().setText(revisionAutor.getComentariosParaAutor());
				view.getComboNivelExperto().setSelectedItem(revisionAutor.getNivelExperto());
				view.getComboDecision().setSelectedItem(obtenerTextoDecision(revisionAutor.getDecisionRevisor()));
			} else {
				view.getTxtComentariosAutores().setText("");
				view.getComboNivelExperto().setSelectedIndex(0);
				view.getComboDecision().setSelectedIndex(0);
			}

			// Cargar comentarios para coordinador
			if (revisionCoord != null) {
				view.getTxtComentariosCoordinadores().setText(revisionCoord.getComentariosParaCoordinador());
			} else {
				view.getTxtComentariosCoordinadores().setText("");
			}

			// 🔒 Activar o desactivar edición según revisor y fecha
			boolean esRevisorActual = revisorSeleccionado.equals(email);
			boolean periodoAbierto = model.periodoRevisionActivoPorConferencia(articulo.getId());
			boolean permitirEdicion = esRevisorActual && periodoAbierto;

			view.getTxtComentariosAutores().setEnabled(permitirEdicion);
			view.getTxtComentariosCoordinadores().setEnabled(permitirEdicion);
			view.getComboNivelExperto().setEnabled(permitirEdicion);
			view.getComboDecision().setEnabled(permitirEdicion);
			view.getBtnEnviarRevision().setEnabled(permitirEdicion);
		});
	}

	/*
	 * Método que se encarga de inicializar la vista Método que se encarga de
	 * inicializar la vista
	 */
	public void initView() {
		view.getFrame().setVisible(true);
		// Asignar el modelo al JList de la vista
		view.getListArticulos().setModel(listModel);
	}

	/*
	 * Método que se encarga de enviar la revisión del artículo seleccionado
	 */
	private void enviarRevision() {
		if (validarDatos()) {
			// Obtiene el id del artículo seleccionado que viene en el DTO
			RevisionArticuloRevisorDTO articuloSeleccionado = view.getListArticulos().getSelectedValue();
			int idArticulo = articuloSeleccionado.getId();
			// Obtiene el comentario de los autores
			String comentariosAutores = view.getTxtComentariosAutores().getText();
			// Obtiene el comentario de los coordinadores
			String comentariosCoordinadores = view.getTxtComentariosCoordinadores().getText();
			// Obtiene el nivel de experto y lo convierte a uppercase
			String nivelExperto = ((String) view.getComboNivelExperto().getSelectedItem());
			// Obtiene la decisión, pero solo el número
			int decision = Integer
					.parseInt(((String) view.getComboDecision().getSelectedItem()).split(" ")[2].split("\\(|\\)")[1]);

			String fechaHoy = UserUtil.getFechaActual();

			// Llamar al backend para insertar la revisión
			model.actualizarRevision(idArticulo, email, comentariosAutores, comentariosCoordinadores, nivelExperto,
					decision);
			// Guardar o actualizar revisión
			model.guardarOActualizarRevision(idArticulo, email, comentariosAutores, comentariosCoordinadores,
					nivelExperto, decision, fechaHoy);
			SwingUtil.showMessage("La revisión se ha enviado correctamente", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			// Eliminar del listModel el artículo revisado
			listModel.removeElement(articuloSeleccionado);
			// Limpiar los campos de texto
			view.getTxtComentariosAutores().setText("");
			view.getTxtComentariosCoordinadores().setText("");
			// Comprobar si el listmodel está vacío
			if (listModel.isEmpty()) {
				SwingUtil.showMessage("No tienes ningún artículo pendiente de revisión", "Información",
						JOptionPane.INFORMATION_MESSAGE);
				view.getFrame().dispose();
			}

		} else {
			SwingUtil.showMessage("Debes de rellenar toda la información", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * Método que se encarga de validar los datos introducidos en la vista
	 * 
	 * @return true si los datos son correctos, false en caso contrario
	 */
	private boolean validarDatos() {
		return view.getListArticulos().getSelectedIndex() != -1 && !view.getTxtComentariosAutores().getText().isEmpty()
				&& !view.getTxtComentariosCoordinadores().getText().isEmpty()
				&& view.getComboNivelExperto().getSelectedIndex() != -1
				&& view.getComboDecision().getSelectedIndex() != -1;
	}

	/*
	 * Método que se encarga de obtener los artículos asignados al revisor
	 */
	private boolean obtenerArticulosAsignados() {
		// Llamar al backend para obtener los artículos asignados

		articulos = model.obtenerArticulosAsignados(email);
		articulosCoolab = model.obtenerRevisionesDeOtrosRevisores();
		nombre = model.obtenerNombreEmail(email);
		// Convertir cada Articulo a ArticuloDTO
		List<RevisionArticuloRevisorDTO> listaDTO = new ArrayList<>();
		for (RevisionArticuloRevisorDTO articulo : articulos) {
			RevisionArticuloRevisorDTO dto = new RevisionArticuloRevisorDTO(articulo.getId(), articulo.getTitulo(),
					articulo.getNombre(), articulo.getNombreFichero());
			listaDTO.add(dto);
		}
		/*
		for (RevisionArticuloRevisorDTO articulo : articulosCoolab) {
			RevisionArticuloRevisorDTO dto = new RevisionArticuloRevisorDTO(articulo.getId(), articulo.getTitulo(),
					articulo.getNombre(), articulo.getNombreFichero());
			if (articulo.getNombre().equals(nombre.get(0).getNombre())) {
				listaDTO.add(dto);
			}
		}
		*/
		// Crear un modelo para el JList y agregar los DTOs
		listModel = new DefaultListModel<>();
		for (RevisionArticuloRevisorDTO dto : listaDTO) {
			listModel.addElement(dto);
		}
		// Si no hay articulos asignados, mostrar un mensaje y cerrar la vista
		if (articulos.isEmpty()) {
			SwingUtil.showMessage("No tienes ningún artículo pendiente de revisión", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		return true;
	}

	private void actualizarListaArticulos(boolean soloPendientes) {
		listModel.clear();
		List<RevisionArticuloRevisorDTO> nuevaLista;

		if (soloPendientes) {
			nuevaLista = model.obtenerArticulosAsignados(email);
		} else {
			nuevaLista = model.obtenerArticulosRevisados(email);
		}

		for (RevisionArticuloRevisorDTO dto : nuevaLista) {
			listModel.addElement(dto);
		}

		// Actualiza también la lista interna por si se necesita
		this.articulos = nuevaLista;
	}

	private String obtenerTextoDecision(int decision) {
		switch (decision) {
		case 2:
			return "Aceptar Fuerte (2)";
		case 1:
			return "Aceptar Débil (1)";
		case -1:
			return "Rechazar Débil (-1)";
		case -2:
			return "Rechazar Fuerte (-2)";
		default:
			return "No asignada";
		}
	}

}
