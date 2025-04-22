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
	private String titulo;
	private int idArticulo;
	private DefaultListModel<PedirColaboradorDTO> listModel;
	private List<PedirColaboradorDTO> articulosDTO;
	private List<PedirColaboradorDTO> revisionesDTO;
	private Map<Integer, List<PedirColaboradorDTO>> revisionesArticulos = new HashMap<>();
	private List<PedirColaboradorDTO> trackRevisores;
	private List<PedirColaboradorDTO> trackRevisoresMio;
	private List<PedirColaboradorDTO> articuloRevisor;
	private List<PedirColaboradorDTO> revisores;
	private List<PedirColaboradorDTO> decision;
	private List<PedirColaboradorDTO> revisorAsig;
	private List<PedirColaboradorDTO> nombreRevisor;
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
	 * @param pedirColaboradorModel      Modelo que maneja la lógica de negocio de
	 *                                   las discusiones.
	 * @param pedirColaboradorView       Vista que presenta la información de los
	 *                                   artículos y revisiones.
	 * @param email                      Correo electrónico del coordinador.
	 * @param titulo 
	 * @param revisionArticuloRevisorDTO
	 */
	public PedirColaboradorController(PedirColaboradorModel pedirColaboradorModel,
			PedirColaboradorView pedirColaboradorView, String email, int idArticulo, String titulo) {
		this.model = pedirColaboradorModel;
		this.view = pedirColaboradorView;
		this.email = email;
		this.idArticulo = idArticulo;		
        this.titulo = titulo;
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

		view.getBtnConfirmar().addActionListener(e -> {
			if (view.getListRevisores().getSelectedValue() != null) {
				SwingUtil.showMessage("Se ha enviado la petición", "Información",
						JOptionPane.INFORMATION_MESSAGE);
				nombreRevisor = model.obtenerNombreRevisor(email);
				model.insertarColaborador(view.getListRevisores().getSelectedValue().getNombre(),
						titulo, "Pendiente", nombreRevisor.get(0).getNombre());
				listModel.removeElement(view.getListRevisores().getSelectedValue());

			} else {
				SwingUtil.showMessage("No has seleccionado a ningún colaborador", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		});

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
		view.getListRevisores().setModel(listModel);
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
		// Consigo el email de los revisores asociados a ese artículo
		trackRevisoresMio = model.obtenerTrack(email);
		listModel = new DefaultListModel<>();
		List<PedirColaboradorDTO> listaDTO = new ArrayList<>();
		
		if (trackRevisoresMio.size() > 0) {
			for (int i = 0; i < trackRevisoresMio.size(); i++) {
				// Da las personas que tienen uno de los tracks del usuario que además no tienen
				// el artículo seleccionado
				revisores = model.obtenerRevisores(trackRevisoresMio.get(i).getIdTrack(), idArticulo);
				for (PedirColaboradorDTO rev : revisores) {

					trackRevisores = model.obtenerTrack(rev.getEmailUsuario());
					//imprimir el tamaño de la lista trackRevisores
					for (PedirColaboradorDTO revTrack : trackRevisores) {
						if (revTrack.getIdTrack() == trackRevisoresMio.get(i).getIdTrack()) {
							decision = model.obtenerDeicision(rev.getEmailUsuario(), idArticulo);

							if (decision.size() > 0) {
								revisorAsig = model.obtenerRevisoresAsignado(titulo);
								if (decision.get(0).getDecision().equals("Lo quiero revisar") && !listModelContains(listModel, revTrack.getNombre())
										&& revisorAsig.isEmpty()) {
									System.out.println("El titulo es: " + revTrack.getTitulo());
									PedirColaboradorDTO dto = new PedirColaboradorDTO(revTrack.getId(), revTrack.getTitulo(),
											revTrack.getNombreFichero(), revTrack.getNombre(), revTrack.getIdTrack(), revTrack.getEmailUsuario());
									listaDTO.add(dto);
								}
							}
						}
					}
				}
				for (PedirColaboradorDTO dto : listaDTO) {
					if (!listModel.contains(dto)) {
						listModel.addElement(dto);
					}

				}

			}
		}

		// Compruebo que track está asociado a estos revisores

		// trackRevisores2 = model.obtenerTrackNombre(articulo.getNombre());
		// Convertir cada Articulo a ArticuloDTO

		// Crear un modelo para el JList y agregar los DTOs

		// Si no hay articulos asignados, mostrar un mensaje y cerrar la vista
		/*
		 * if (trackRevisores.isEmpty()) {
		 * SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar",
		 * "Información", JOptionPane.INFORMATION_MESSAGE); return false; }
		 */

		return true;
	}
	private boolean listModelContains(DefaultListModel<PedirColaboradorDTO> listModel, String nombre) {
	    for (int i = 0; i < listModel.size(); i++) {
	        if (listModel.get(i).getNombre().equals(nombre)) {
	            return true;
	        }
	    }
	    return false;
	}

}