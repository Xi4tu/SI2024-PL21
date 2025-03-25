package app.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import app.dto.RevisionArticuloAutorDTO;
import app.dto.RevisionAutorDTO;
import app.enums.DecisionFinal;
import app.enums.Rol;
import app.model.RevisionArticuloAutorModel;
import app.util.UserUtil;
import app.view.RevisionArticuloAutorView;
import giis.demo.util.SwingUtil;

/**
 * Controller encargado de gestionar la lógica de la vista de revisiones de
 * artículos para el autor.
 */
public class RevisionArticuloAutorController {

	// Atributos de la clase
	private RevisionArticuloAutorModel model;
	private RevisionArticuloAutorView view;
	private String email;
	private DefaultListModel<RevisionArticuloAutorDTO> listModel;
	private List<RevisionArticuloAutorDTO> articulosDTO;
	private List<RevisionAutorDTO> revisionesDTO;
	private Map<Integer, List<RevisionAutorDTO>> revisionesArticulos = new HashMap<>();
	private static final Rol ROL = Rol.AUTOR;
	private int idArticulo = -1; // Id del articulo a mostrar directamente, -1 si no se ha pasado idArticulo

	/**
	 * Constructor del controller.
	 *
	 * @param m     Modelo que maneja la lógica de negocio de los artículos.
	 * @param v     Vista que presenta la información de los artículos y revisiones.
	 * @param email Correo electrónico del autor.
	 */
	public RevisionArticuloAutorController(RevisionArticuloAutorModel m, RevisionArticuloAutorView v, String email) {
		this.model = m;
		this.view = v;
		this.email = email;

		if (!UserUtil.checkEmail(email, ROL.getNombre(), model.getDbUtil())) {
			// Detener la inicialización si el email es inválido.
			return;
		}

		// Llamar al backend para cargar los artículos que tengan revisión.
		if (!obtenerArticulos()) {
			return;
		}

		// Llamar al backend para cargar las revisiones de los artículos.
		if (!obtenerRevisiones()) {
			return;
		}

		// Inicializar la vista una vez que los datos están cargados.
		this.initView();
	}
	
	/**
	 * Constructor del controller incluyendo idArticulo, para mostrarlo directamente elegido en la vista
	 * @param m
	 * @param v
	 * @param email
	 */
	public RevisionArticuloAutorController(RevisionArticuloAutorModel m, RevisionArticuloAutorView v, String email, int idArticulo) {
		this(m, v, email);
		this.idArticulo = idArticulo;
	}

	/**
	 * Inicializa el controller, configurando los eventos de la vista.
	 */
	public void initController() {
		// Botón de cerrar.
		view.getBtnCerrar().addActionListener(e -> view.getFrame().dispose());

		// Cuando se selecciona un artículo en la lista, mostrar las revisiones
		// asociadas.
		view.getListArticulos().addListSelectionListener(e -> {

			// Obtener el DTO del artículo seleccionado.
			RevisionArticuloAutorDTO articuloSeleccionado = view.getListArticulos().getSelectedValue();

			// Verifica si el objeto o su id son nulos.
			if (articuloSeleccionado == null || articuloSeleccionado.getIdArticulo() == 0) {
				return;
			}
			// Limpiar el panel de revisiones antes de agregar las nuevas.
			view.getPanelRevisiones().removeAll();

			// Mostrar la decisión final del artículo.
			view.getLblDecisionFinal().setText(articuloSeleccionado.getDecisionFinal());

			// Obtener la decisión final a partir del texto del artículo.
			DecisionFinal decisionFinal = DecisionFinal.fromLabel(articuloSeleccionado.getDecisionFinal());

			// Si se encuentra una decisión válida, establece el color asociado.
			if (decisionFinal != null) {
			    view.getLblDecisionFinal().setForeground(decisionFinal.getColor());
			} else {
			    // Opción por defecto en caso de que no se encuentre (opcional)
			    view.getLblDecisionFinal().setForeground(new Color(0, 0, 0));
			}

			// Guardar las revisiones asociadas al artículo seleccionado
			revisionesDTO = revisionesArticulos.get(articuloSeleccionado.getIdArticulo());

			String decision;
			// Iterar sobre cada uno de las revisiones del articulo seleccionado para
			// mostrarlas en la vista.
			for (RevisionAutorDTO r : revisionesDTO) {
				// Obtener la decisión del revisor usando el enum
				decision = app.enums.DecisionRevisor.getLabelByValue(r.getDecisionRevisor());
				// Agregar la revisión a la vista.
				view.agregarRevision(r.getComentariosParaAutor(), r.getNivelExperto(), decision);
			}

		});
		
		
		// Si se ha pasado un idArticulo, seleccionarlo y mostrarlo directamente
		if (idArticulo != -1) {
			// Buscar el articulo con el idArticulo
			for (int i = 0; i < articulosDTO.size(); i++) {
				if (articulosDTO.get(i).getIdArticulo() == idArticulo) {
					view.getListArticulos().setSelectedIndex(i);
					break;
				}
			}
		}

	}

	/**
	 * Inicializa la vista, haciendo visible el frame y estableciendo el modelo de
	 * la lista.
	 */
	public void initView() {
		view.getFrame().setVisible(true);
		// Agregar los artículos al JList.
		view.getListArticulos().setModel(listModel);
	}

	/**
	 * Obtiene los artículos en los que participa el autor y que tienen revisión.
	 *
	 * @return true si se obtuvieron artículos, false en caso contrario.
	 */
	private boolean obtenerArticulos() {

		// Llamar al backend para obtener los artículos en los que participa el autor y
		// tienen decisión final.
		List<RevisionArticuloAutorDTO> articulos = model.getArticulosAutor(email);

		// Si no hay artículos asignados, mostrar un mensaje y cerrar la vista.
		if (articulos.isEmpty()) {
			SwingUtil.showMessage("No tienes ningún artículo que ver", "Información", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// Convertir cada elemento a DTO y almacenarlos en una lista.
		articulosDTO = new ArrayList<>();
		for (RevisionArticuloAutorDTO e : articulos) {
			RevisionArticuloAutorDTO dto = new RevisionArticuloAutorDTO(e.getIdArticulo(), e.getTitulo(),
					e.getDecisionFinal());
			articulosDTO.add(dto);
		}

		// Crear un modelo para el JList y agregar los DTOs.
		listModel = new DefaultListModel<>();
		for (RevisionArticuloAutorDTO dto : articulosDTO) {
			listModel.addElement(dto);
		}

		return true;
	}

	/**
	 * Obtiene las revisiones asociadas a los artículos del autor.
	 *
	 * @return true si se obtuvieron revisiones, false en caso contrario.
	 */
	private boolean obtenerRevisiones() {
		
		// Llamar al backend para obtener las revisiones asociadas a los artículos
		List<RevisionAutorDTO> revisiones = model.getRevisionesArticulos(email);

		// Si no se han encontrado revisiones, se muestra un mensaje.
		if (revisiones.isEmpty()) {
			SwingUtil.showMessage("No se han encontrado revisiones", "Información", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// Convertir cada elemento a DTO y almacenarlos en un mapa con el idArticulo
		// como clave.
		for (RevisionAutorDTO r : revisiones) {
			if (revisionesArticulos.containsKey(r.getIdArticulo())) {
				revisionesArticulos.get(r.getIdArticulo()).add(r);
			} else {
				List<RevisionAutorDTO> lista = new ArrayList<>();
				lista.add(r);
				revisionesArticulos.put(r.getIdArticulo(), lista);
			}
		}

		return true;
	}
}
