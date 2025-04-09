package app.controller;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import app.dto.AccederDiscusionDTO;
import app.dto.AnotacionesDTO;
import app.enums.DecisionRevisor;
import app.enums.Rol;
import app.model.ParticiparDiscusionesCoordModel;
import app.util.UserUtil;
import app.view.ParticiparDiscusionesCoordView;
import giis.demo.util.SwingUtil;

public class ParticiparDiscusionesCoordController {

	// Atributos de la clase
	private ParticiparDiscusionesCoordModel model;
	private ParticiparDiscusionesCoordView view;
	private String email;
	private String fecha;
	private DefaultListModel<AccederDiscusionDTO> listModel;
	private List<AccederDiscusionDTO> articulosDTO;
	private List<AnotacionesDTO> anotacionesDTO;
	private Map<Integer, List<AnotacionesDTO>> anotacionesArticulos = new HashMap<>();
	private static final Rol ROL = Rol.COORDINADOR;

	/**
	 * Constructor del controller.
	 *
	 * <p>
	 * Inicializa el controlador con el modelo, la vista y el correo del revisor.
	 * Llama al backend para cargar los artículos en discusión y sus anotaciones
	 * asociadas, y posteriormente inicializa la vista.
	 * </p>
	 *
	 * @param m     Modelo que maneja la lógica de negocio de las discusiones.
	 * @param v     Vista que presenta la información de los artículos y
	 *              anotaciones.
	 * @param email Correo electrónico del revisor.
	 */
	public ParticiparDiscusionesCoordController(ParticiparDiscusionesCoordModel m, ParticiparDiscusionesCoordView v,
			String email) {
		this.model = m;
		this.view = v;
		this.email = email;
		this.fecha = UserUtil.getFechaActual();

		// Detener la inicialización si el email es inválido.
		if (!UserUtil.checkEmail(email, ROL.getNombre(), model.getDbUtil())) {
			return;
		}

		// Llamar al backend para obtener información de los artículos en discusión.
		if (!obtenerArticulos()) {
			return;
		}

		// Llamar al backend para obtener las anotaciones asociadas a los artículos.
		if (!obtenerAnotaciones()) {
			return;
		}

		// Inicializar la vista una vez que los datos están cargados.
		this.initView();
	}

	/**
	 * Inicializa los listeners y comportamientos de la vista.
	 *
	 * <p>
	 * Configura los listeners para los botones (mantener firme, agregar nota) y
	 * para la selección de un artículo en la lista, además de asignar acciones para
	 * enviar la anotación al pulsar Enter en el campo de texto.
	 * </p>
	 */
	@SuppressWarnings("serial")
	public void initController() {
		// Botón de mantenerse firme.
		view.getBtnMantenerFirme().addActionListener(e -> mantenerFirme());
		// Botón de agregar nota.
		view.getBtnAgregarNota().addActionListener(e -> agregarNota());
		// Cuando se selecciona un artículo, mostrar anotaciones y la decisión del
		// revisor.
		view.getListArticulos().addListSelectionListener(e -> {
			AccederDiscusionDTO articuloSeleccionado = view.getListArticulos().getSelectedValue();
			if (articuloSeleccionado == null || articuloSeleccionado.getIdArticulo() == 0) {
				return;
			}

			// Limpiar el panel de anotaciones.
			view.clearAnnotations();

			// Mostrar la decisión del revisor en el ComboBox.
			DecisionRevisor decisionEnum = DecisionRevisor.fromValue(articuloSeleccionado.getDecisionRevisor());
			view.getComboDecision().setSelectedItem(decisionEnum);

			// Habilitar o deshabilitar controles según si el revisor se ha mantenido firme.
			if (articuloSeleccionado.getMantenerseFirme() == 1) {
				view.getComboDecision().setEnabled(false);
				view.getBtnMantenerFirme().setEnabled(false);
			} else {
				view.getComboDecision().setEnabled(true);
				view.getBtnMantenerFirme().setEnabled(true);
			}

			// Cargar y mostrar las anotaciones asociadas al artículo.
			anotacionesDTO = anotacionesArticulos.get(articuloSeleccionado.getIdArticulo());
			mostrarAnotaciones();
		});

		// Asignar acción de "enviar anotación" al pulsar Enter.
		view.getTextNuevaAnotacion().getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "sendNote");
		view.getTextNuevaAnotacion().getActionMap().put("sendNote", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				agregarNota();
			}
		});
		// Permitir que Shift+Enter inserte un salto de línea.
		view.getTextNuevaAnotacion().getInputMap().put(KeyStroke.getKeyStroke("shift ENTER"),
				view.getTextNuevaAnotacion().getInputMap().get(KeyStroke.getKeyStroke("ENTER")));
	}

	/**
	 * Inicializa la vista.
	 *
	 * <p>
	 * Hace visible el frame de la vista y establece el modelo de la lista con los
	 * artículos en discusión.
	 * </p>
	 */
	public void initView() {
		view.getFrame().setVisible(true);
		// Agregar artículos a la lista.
		view.getListArticulos().setModel(listModel);
	}

	/**
	 * Mantiene firme la decisión del revisor para el artículo seleccionado.
	 *
	 * <p>
	 * Verifica que se haya seleccionado un artículo y que se haya elegido una
	 * decisión. Si la decisión es diferente a la actual, actualiza la decisión en
	 * la base de datos, marca al revisor como "firme" y bloquea el ComboBox y el
	 * botón de mantenerse firme para ese artículo.
	 * </p>
	 */
	private void mantenerFirme() {
		AccederDiscusionDTO articuloSeleccionado = view.getListArticulos().getSelectedValue();
		if (articuloSeleccionado == null || articuloSeleccionado.getIdArticulo() == 0) {
			SwingUtil.showMessage("Selecciona un artículo para mantener firme la decisión", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		DecisionRevisor decision = (DecisionRevisor) view.getComboDecision().getSelectedItem();
		if (decision == null) {
			SwingUtil.showMessage("Selecciona una decisión válida", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Actualizar la decisión en la base de datos.
		model.actualizarDecisionRevisor(articuloSeleccionado.getIdArticulo(), email, decision.getValue());
		model.mantenerDecisionFirme(email, articuloSeleccionado.getIdArticulo());

		// Actualizar el DTO.
		articuloSeleccionado.setMantenerseFirme(1);
		articuloSeleccionado.setDecisionRevisor(decision.getValue());

		// Reemplazar el DTO en el modelo de la lista.
		int idx = view.getListArticulos().getSelectedIndex();
		listModel.set(idx, articuloSeleccionado);

		// Bloquear el ComboBox y el botón de "mantener firme".
		view.getComboDecision().setEnabled(false);
		view.getBtnMantenerFirme().setEnabled(false);
	}

	/**
	 * Agrega una nueva anotación a la discusión del artículo seleccionado.
	 *
	 * <p>
	 * Valida que se haya seleccionado un artículo y que el comentario no esté
	 * vacío. Luego llama al modelo para insertar la nueva anotación en la base de
	 * datos, limpia el campo de texto y refresca las anotaciones mostrando la
	 * nueva.
	 * </p>
	 */
	private void agregarNota() {
		AccederDiscusionDTO articuloSeleccionado = view.getListArticulos().getSelectedValue();
		if (articuloSeleccionado == null || articuloSeleccionado.getIdArticulo() == 0) {
			SwingUtil.showMessage("Selecciona un artículo para agregar una anotación", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String comentario = view.getTextNuevaAnotacion().getText();
		if (comentario.isEmpty()) {
			SwingUtil.showMessage("El comentario no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			// Agregar la nueva anotación en la base de datos.
			model.agregarAnotacion(articuloSeleccionado.getIdArticulo(), email, comentario, fecha,
					UserUtil.getHoraActual());

			// Limpiar el campo de texto.
			view.getTextNuevaAnotacion().setText("");

			// Volver a obtener todas las anotaciones.
			anotacionesArticulos.clear();
			obtenerAnotaciones();

			// Reasignar las anotaciones para el artículo seleccionado.
			anotacionesDTO = anotacionesArticulos.get(articuloSeleccionado.getIdArticulo());

			// Actualizar la vista.
			view.clearAnnotations();
			mostrarAnotaciones();

		} catch (Exception e) {
			SwingUtil.showMessage("No se ha podido agregar la anotación\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Obtiene los artículos en discusión para el revisor desde el backend.
	 *
	 * <p>
	 * Llama al modelo para obtener los artículos en discusión según el email del
	 * revisor y la fecha actual. Si no hay artículos, muestra un mensaje y retorna
	 * false. Convierte los datos a DTOs y los agrega a un DefaultListModel para la
	 * vista.
	 * </p>
	 *
	 * @return {@code true} si se encontraron artículos; de lo contrario,
	 *         {@code false}.
	 */
	private boolean obtenerArticulos() {
		List<AccederDiscusionDTO> articulos = model.getArticulos(email, fecha);
		if (articulos.isEmpty()) {
			SwingUtil.showMessage("No tienes ningún artículo que discutir", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		articulosDTO = new ArrayList<>();
		for (AccederDiscusionDTO e : articulos) {
			AccederDiscusionDTO dto = new AccederDiscusionDTO(e.getIdArticulo(), e.getTitulo(), e.getDecisionRevisor(),
					e.getMantenerseFirme());
			articulosDTO.add(dto);
		}
		listModel = new DefaultListModel<>();
		for (AccederDiscusionDTO dto : articulosDTO) {
			listModel.addElement(dto);
		}
		return true;
	}

	/**
	 * Obtiene las anotaciones de los artículos en discusión para el revisor desde
	 * el backend.
	 *
	 * <p>
	 * Llama al modelo para obtener las anotaciones asociadas a los artículos en
	 * discusión y las organiza en un mapa con el id del artículo como clave. Si no
	 * se encuentran anotaciones, muestra un mensaje y retorna false.
	 * </p>
	 *
	 * @return {@code true} si se encontraron anotaciones; de lo contrario,
	 *         {@code false}.
	 */
	private boolean obtenerAnotaciones() {
		List<AnotacionesDTO> anotaciones = model.getAnotaciones(email, fecha);
		if (anotaciones.isEmpty()) {
			SwingUtil.showMessage("No se han encontrado revisiones", "Información", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		for (AnotacionesDTO r : anotaciones) {
			if (anotacionesArticulos.containsKey(r.getIdArticulo())) {
				anotacionesArticulos.get(r.getIdArticulo()).add(r);
			} else {
				List<AnotacionesDTO> lista = new ArrayList<>();
				lista.add(r);
				anotacionesArticulos.put(r.getIdArticulo(), lista);
			}
		}
		return true;
	}

	/**
	 * Muestra las anotaciones en la vista para el artículo actualmente
	 * seleccionado.
	 *
	 * <p>
	 * Si existen anotaciones en la variable {@code anotacionesDTO}, las itera y
	 * agrega cada una a la vista. Si no existen, limpia el panel de anotaciones.
	 * </p>
	 */
	private void mostrarAnotaciones() {
		if (anotacionesDTO != null) {
			for (AnotacionesDTO r : anotacionesDTO) {
				view.addAnnotationCard(r.getEmailUsuario(), r.getFecha(), r.getHora(), r.getComentario());
			}
		} else {
			view.clearAnnotations();
		}
	}

}
