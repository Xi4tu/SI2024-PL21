package app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import app.dto.AceptarDenegarArticuloDTO;
import app.dto.ArticuloDiscusionDTO;
import app.dto.PedirColaboradorDTO;
import app.dto.RevisionArticuloAutorDTO;
import app.dto.RevisionArticuloRevisionDTO;
import app.dto.RevisionArticuloRevisorDTO;
import app.enums.Rol;
import app.model.GestionarDiscusionesCoordinadorModel;
import app.model.PedirColaboradorModel;
import app.view.GestionarDiscusionesCoordinadorView;
import app.view.PedirColaboradorView;
import giis.demo.util.SwingUtil;

/**
 * Controller para la gestión de discusiones a cargo del coordinador.
 * 
 * <p>
 * Esta clase gestiona la interacción entre la vista y el modelo en el contexto
 * de la gestión de discusiones de artículos, permitiendo al coordinador:
 * <ul>
 * <li>Visualizar los artículos aptos para discusión.</li>
 * <li>Consultar las revisiones asociadas a cada artículo.</li>
 * <li>Poner un artículo en discusión, notificando a los revisores
 * correspondientes.</li>
 * </ul>
 * </p>
 * 
 * @see GestionarDiscusionesCoordinadorModel
 * @see GestionarDiscusionesCoordinadorView
 * @see ArticuloDiscusionDTO
 * @see RevisionArticuloRevisionDTO
 */
public class PedirColaboradorController {

	// Atributos de la clase
	private PedirColaboradorModel model;
	private PedirColaboradorView view;
	private String email;
	private int id;
	private DefaultListModel<PedirColaboradorDTO> listModel;
	private List<PedirColaboradorDTO> articulosDTO;
	private List<PedirColaboradorDTO> revisionesDTO;
	private Map<Integer, List<PedirColaboradorDTO>> revisionesArticulos = new HashMap<>();
	private List<PedirColaboradorDTO> trackRevisores;
	private List<PedirColaboradorDTO> trackRevisores2;
	private List<PedirColaboradorDTO> revisores;
	private static final Rol ROL = Rol.COORDINADOR;

	/**
	 * Constructor del controller.
	 *
	 * <p>
	 * Inicializa el controlador con el modelo, la vista y el correo del
	 * coordinador. Llama al backend para cargar los artículos aptos para discusión
	 * y sus revisiones asociadas, y posteriormente inicializa la vista.
	 * </p>
	 *
	 * @param pedirColaboradorModel Modelo que maneja la lógica de negocio de las
	 *                              discusiones.
	 * @param pedirColaboradorView  Vista que presenta la información de los
	 *                              artículos y revisiones.
	 * @param email                 Correo electrónico del coordinador.
	 * @param revisionArticuloRevisorDTO 
	 */
	public PedirColaboradorController(PedirColaboradorModel pedirColaboradorModel,
			PedirColaboradorView pedirColaboradorView, String email, int id) {
		this.model = pedirColaboradorModel;
		this.view = pedirColaboradorView;
		this.email = email;
		this.id = id;

		// Llamar al backend para cargar los artículos aptos para discusión
		// if (!obtenerArticulos()) {
		// return;
		// }
		if (!obtenerRevisores()) {
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
	 * <li>Cerrar la ventana cuando se pulsa el botón "Cerrar".</li>
	 * <li>Mostrar las revisiones asociadas al seleccionar un artículo en la
	 * lista.</li>
	 * <li>Poner un artículo en discusión al pulsar el botón "Poner en
	 * Discusión".</li>
	 * </ul>
	 * </p>
	 */
	public void initController() {

		// Botón de cerrar.
		// view.getBtnCerrar().addActionListener(e -> view.getFrame().dispose());

		// Cuando se selecciona un artículo en la lista, mostrar las revisiones
		// asociadas.

	}

	/**
	 * Inicializa la vista.
	 *
	 * <p>
	 * Hace visible el frame de la vista y establece el modelo de la lista con los
	 * artículos aptos para discusión.
	 * </p>
	 */
	public void initView() {
		view.getFrame().setVisible(true);
		// Agregar los artículos al JList.
		// view.getListArticulos().setModel(listModel);
	}

	/**
	 * Obtiene las revisiones asociadas a los artículos aptos para discusión.
	 *
	 * <p>
	 * Llama al backend para obtener la lista de revisiones y las agrupa en un mapa,
	 * utilizando el identificador del artículo como clave. Si no se encuentran
	 * revisiones, se muestra un mensaje informativo.
	 * </p>
	 *
	 * @return <code>true</code> si se obtuvieron las revisiones correctamente;
	 *         <code>false</code> en caso contrario.
	 */

	private boolean obtenerRevisores() {
		// Llamar al backend para obtener los artículos asignados
		trackRevisores = model.obtenerTrack(email);
		//trackRevisores2 = model.obtenerTrackNombre(articulo.getNombre());
		// Convertir cada Articulo a ArticuloDTO
		
		for (int i = 0; i < trackRevisores.size(); i++) {
			if (trackRevisores.get(i).getIdTrack() == 1) {
				
			}
		}
		List<PedirColaboradorDTO> listaDTO = new ArrayList<>();
		for (PedirColaboradorDTO articulo : trackRevisores) {
			
			//PedirColaboradorDTO dto = new PedirColaboradorDTO(articulo.getId(), articulo.getTitulo(),
			//		articulo.getNombreFichero(), articulo.getNombre());
			System.out.println(articulo.getIdTrack());
			//listaDTO.add(dto);
		}

		// Crear un modelo para el JList y agregar los DTOs
		listModel = new DefaultListModel<>();
		for (PedirColaboradorDTO dto : listaDTO) {
			listModel.addElement(dto);
		}

		// Si no hay articulos asignados, mostrar un mensaje y cerrar la vista
		if (trackRevisores.isEmpty()) {
			SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		return true;
	}

}
