package app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import app.dto.RevisionArticuloRevisorDTO;
import app.enums.Rol;
import app.model.RevisionArticuloRevisorModel;
import app.util.UserUtil;
import app.view.RevisionArticuloRevisorView;
import giis.demo.util.SwingUtil;

public class RevisionArticuloRevisorController {

	private RevisionArticuloRevisorModel model;
	private RevisionArticuloRevisorView view;
	private List<RevisionArticuloRevisorDTO> articulos;
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
		// Agrear listener al botón de enviar revisión
		view.getBtnEnviarRevision().addActionListener(e -> SwingUtil.exceptionWrapper(() -> enviarRevision()));
		// Cuando se selecciona un artículo, mostrar el nombre del fichero
		view.getListArticulos().addListSelectionListener(e -> {
			RevisionArticuloRevisorDTO articuloSeleccionado = view.getListArticulos().getSelectedValue();
			// Verifica si el objeto o su id son nulos
			if (articuloSeleccionado == null || articuloSeleccionado.getId() == 0) {
				return;
			}

			// Guarda el id en una variable (objeto Integer)
			Integer idSeleccionado = articuloSeleccionado.getId();

			articulos.forEach(art -> {
				// Si art.getId() es primitivo int, conviértelo a Integer para compararlo con
				// equals
				if (idSeleccionado.equals(Integer.valueOf(art.getId()))) {
					articuloSeleccionado.setNombreFichero(art.getNombreFichero());
				}
			});

			view.getLblFileName().setText(articuloSeleccionado.getNombreFichero());
		});

	}

	/*
	 * Método que se encarga de inicializar la vista
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

			// Llamar al backend para insertar la revisión
			model.actualizarRevision(idArticulo, email, comentariosAutores, comentariosCoordinadores, nivelExperto,
					decision);
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

		// Convertir cada Articulo a ArticuloDTO
		List<RevisionArticuloRevisorDTO> listaDTO = new ArrayList<>();
		for (RevisionArticuloRevisorDTO articulo : articulos) {
			RevisionArticuloRevisorDTO dto = new RevisionArticuloRevisorDTO(articulo.getId(), articulo.getTitulo(),
					articulo.getNombreFichero());
			listaDTO.add(dto);
		}

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

}
