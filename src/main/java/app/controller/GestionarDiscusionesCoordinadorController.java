package app.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import app.dto.ArticuloDiscusionDTO;
import app.dto.RevisionArticuloRevisionDTO;
import app.enums.Rol;
import app.model.GestionarDiscusionesCoordinadorModel;
import app.view.GestionarDiscusionesCoordinadorView;
import giis.demo.util.SwingUtil;

/**
 * Controller para la gestión de discusiones a cargo del coordinador.
 * 
 * <p>
 * Esta clase gestiona la interacción entre la vista y el modelo en el contexto de la 
 * gestión de discusiones de artículos, permitiendo al coordinador:
 * <ul>
 *   <li>Visualizar los artículos aptos para discusión.</li>
 *   <li>Consultar las revisiones asociadas a cada artículo.</li>
 *   <li>Poner un artículo en discusión, notificando a los revisores correspondientes.</li>
 * </ul>
 * </p>
 * 
 * @see GestionarDiscusionesCoordinadorModel
 * @see GestionarDiscusionesCoordinadorView
 * @see ArticuloDiscusionDTO
 * @see RevisionArticuloRevisionDTO
 */
public class GestionarDiscusionesCoordinadorController {

	// Atributos de la clase
	private GestionarDiscusionesCoordinadorModel model;
	private GestionarDiscusionesCoordinadorView view;
	private String email;
	private DefaultListModel<ArticuloDiscusionDTO> listModel;
	private List<ArticuloDiscusionDTO> articulosDTO;
	private List<RevisionArticuloRevisionDTO> revisionesDTO;
	private Map<Integer, List<RevisionArticuloRevisionDTO>> revisionesArticulos = new HashMap<>();
	private static final Rol ROL = Rol.COORDINADOR;

	/**
	 * Constructor del controller.
	 *
	 * <p>
	 * Inicializa el controlador con el modelo, la vista y el correo del coordinador.
	 * Llama al backend para cargar los artículos aptos para discusión y sus revisiones asociadas,
	 * y posteriormente inicializa la vista.
	 * </p>
	 *
	 * @param m     Modelo que maneja la lógica de negocio de las discusiones.
	 * @param v     Vista que presenta la información de los artículos y revisiones.
	 * @param email Correo electrónico del coordinador.
	 */
	public GestionarDiscusionesCoordinadorController(GestionarDiscusionesCoordinadorModel m,
			GestionarDiscusionesCoordinadorView v, String email) {
		this.model = m;
		this.view = v;
		this.email = email;

		// Llamar al backend para cargar los artículos aptos para discusión
		if (!obtenerArticulos()) {
			return;
		}

		// Llamar al backend para cargar las revisiones de los artículos cargados
		// anteriormente
		if (!obtenerRevisiones()) {
			return;
		}

		// Inicializar la vista una vez que los datos están cargados.
		this.initView();
	}

	/**
	 * Inicializa el controller configurando los eventos de la vista.
	 *
	 * <p>
	 * Se configuran los listeners para:
	 * <ul>
	 *   <li>Cerrar la ventana cuando se pulsa el botón "Cerrar".</li>
	 *   <li>Mostrar las revisiones asociadas al seleccionar un artículo en la lista.</li>
	 *   <li>Poner un artículo en discusión al pulsar el botón "Poner en Discusión".</li>
	 * </ul>
	 * </p>
	 */
	public void initController() {
		// Botón de cerrar.
		view.getBtnCerrar().addActionListener(e -> view.getFrame().dispose());

		// Cuando se selecciona un artículo en la lista, mostrar las revisiones asociadas.
		view.getListArticulos().addListSelectionListener(e -> {

			// Obtener el DTO del artículo seleccionado.
			ArticuloDiscusionDTO articuloSeleccionado = view.getListArticulos().getSelectedValue();

			// Verifica si el objeto o su id son nulos.
			if (articuloSeleccionado == null || articuloSeleccionado.getIdArticulo() == 0) {
				return;
			}
			// Limpiar el panel de revisiones antes de agregar las nuevas.
			view.getPanelRevisiones().removeAll();

			// Mostrar la decisión final del artículo.
			view.getLblValoracionGlobal().setText(Integer.toString(articuloSeleccionado.getValoracionGlobal()));

			// Guardar las revisiones asociadas al artículo seleccionado.
			revisionesDTO = revisionesArticulos.get(articuloSeleccionado.getIdArticulo());

			String decision;
			// Iterar sobre cada una de las revisiones del artículo seleccionado para mostrarlas.
			for (RevisionArticuloRevisionDTO r : revisionesDTO) {
				// Obtener la decisión del revisor usando el enum.
				decision = app.enums.DecisionRevisor.getLabelByValue(r.getDecisionRevisor());
				// Agregar la revisión a la vista.
				view.addRevisionCard(r.getNombre(), r.getNivelExperto(), decision, r.getComentariosParaCoordinador());
			}

		});

		// Botón de poner en discusión.
		view.getBtnPonerEnDiscusion().addActionListener(e -> {
			// Obtener el artículo seleccionado.
			ArticuloDiscusionDTO articulo = view.getListArticulos().getSelectedValue();
			if (articulo == null) {
				SwingUtil.showMessage("No se ha seleccionado ningún artículo", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			// Llamar al backend para poner en discusión el artículo seleccionado.
			if (model.ponerEnDiscusion(articulo.getIdArticulo())) {
				SwingUtil.showMessage("Artículo puesto en discusión, se ha notificado a los revisores", "Información", JOptionPane.INFORMATION_MESSAGE);
				
				// Eliminar del listModel el artículo puesto en discusión.
				listModel.removeElement(articulo);
				
				// Limpiar los campos de texto.
				view.getLblValoracionGlobal().setText("");
				view.getPanelRevisiones().removeAll();
				
				// Comprobar si el listModel está vacío.
				if (listModel.isEmpty()) {
					SwingUtil.showMessage("No hay ningún artículo apto para discusión", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					view.getFrame().dispose();
				}
				
			} else {
				SwingUtil.showMessage("No se ha podido poner en discusión el artículo", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	/**
	 * Inicializa la vista.
	 *
	 * <p>
	 * Hace visible el frame de la vista y establece el modelo de la lista con los artículos aptos para discusión.
	 * </p>
	 */
	public void initView() {
		view.getFrame().setVisible(true);
		// Agregar los artículos al JList.
		view.getListArticulos().setModel(listModel);
	}

	/**
	 * Obtiene las revisiones asociadas a los artículos aptos para discusión.
	 *
	 * <p>
	 * Llama al backend para obtener la lista de revisiones y las agrupa en un mapa,
	 * utilizando el identificador del artículo como clave. Si no se encuentran revisiones,
	 * se muestra un mensaje informativo.
	 * </p>
	 *
	 * @return <code>true</code> si se obtuvieron las revisiones correctamente; <code>false</code> en caso contrario.
	 */
	private boolean obtenerRevisiones() {
		// Llamar al backend para obtener las revisiones asociadas a los artículos aptos para discusión.
		List<RevisionArticuloRevisionDTO> revisiones = model.getRevisionesArticulos();

		// Si no se han encontrado revisiones, se muestra un mensaje.
		if (revisiones.isEmpty()) {
			SwingUtil.showMessage("No se han encontrado revisiones", "Información", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// Convertir cada elemento a DTO y almacenarlos en un mapa con el idArticulo como clave.
		for (RevisionArticuloRevisionDTO r : revisiones) {
			if (revisionesArticulos.containsKey(r.getIdArticulo())) {
				revisionesArticulos.get(r.getIdArticulo()).add(r);
			} else {
				List<RevisionArticuloRevisionDTO> lista = new ArrayList<>();
				lista.add(r);
				revisionesArticulos.put(r.getIdArticulo(), lista);
			}
		}

		return true;
	}

	/**
	 * Obtiene los artículos aptos para discusión.
	 *
	 * <p>
	 * Llama al backend para obtener los artículos que cumplen los criterios de aptitud para discusión,
	 * y actualiza el modelo de la lista en la vista. Si no se encuentran artículos aptos, se muestra un mensaje informativo.
	 * </p>
	 *
	 * @return <code>true</code> si se obtuvieron los artículos correctamente; <code>false</code> en caso contrario.
	 */
	private boolean obtenerArticulos() {

		// Llamar al backend para obtener los artículos aptos para discusión.
		List<ArticuloDiscusionDTO> articulos = model.getArticulosAptosDiscusion();

		// Si no hay artículos asignados, mostrar un mensaje y cerrar la vista.
		if (articulos.isEmpty()) {
			SwingUtil.showMessage("No hay ningún artículo apto para discusión", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// Convertir cada elemento a DTO y almacenarlos en una lista.
		articulosDTO = new ArrayList<>();
		for (ArticuloDiscusionDTO e : articulos) {
			ArticuloDiscusionDTO dto = new ArticuloDiscusionDTO(e.getIdArticulo(), e.getTitulo(),
					e.getValoracionGlobal());
			articulosDTO.add(dto);
		}

		// Crear un modelo para el JList y agregar los DTOs.
		listModel = new DefaultListModel<>();
		for (ArticuloDiscusionDTO dto : articulosDTO) {
			listModel.addElement(dto);
		}

		return true;
	}
}
