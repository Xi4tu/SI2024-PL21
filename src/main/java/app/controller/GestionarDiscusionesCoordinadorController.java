package app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;

import app.dto.RevisionArticuloAutorDTO;
import app.dto.RevisionAutorDTO;
import app.enums.Rol;
import app.model.GestionarDiscusionesCoordinadorModel;
import app.model.RevisionArticuloAutorModel;
import app.util.UserUtil;
import app.view.GestionarDiscusionesCoordinadorView;
import app.view.RevisionArticuloAutorView;

public class GestionarDiscusionesCoordinadorController {
	
	// Atributos de la clase
	private GestionarDiscusionesCoordinadorModel model;
	private GestionarDiscusionesCoordinadorView view;
	private String email;
	private DefaultListModel<RevisionArticuloAutorDTO> listModel;
	private List<RevisionArticuloAutorDTO> articulosDTO;
	//private List<> revisionesDTO; // Crear un nuevo DTO para las revisiones
	//private Map<Integer, List<>> revisionesArticulos = new HashMap<>(); // Crear un nuevo DTO para las revisiones
	private static final Rol ROL = Rol.COORDINADOR;
	
	
	/**
	 * Constructor del controller.
	 *
	 * @param m     Modelo que maneja la lógica de negocio de los artículos.
	 * @param v     Vista que presenta la información de los artículos y revisiones.
	 * @param email Correo electrónico del autor.
	 */
	public GestionarDiscusionesCoordinadorController(GestionarDiscusionesCoordinadorModel m, GestionarDiscusionesCoordinadorView v, String email) {
		this.model = m;
		this.view = v;
		this.email = email;

		/*if (!UserUtil.checkEmail(email, ROL.getNombre(), model.getDbUtil())) {
			// Detener la inicialización si el email es inválido.
			return;
		}*/

		// Llamar al backend para cargar los artículos aptos para discusión
		if (!obtenerArticulos()) {
			return;
		}

		// Llamar al backend para cargar las revisiones de los artículos aptos para discusión
		if (!obtenerRevisiones()) {
			return;
		}

		// Inicializar la vista una vez que los datos están cargados.
		this.initView();
	}

	/**
	 * Inicializa el controller, configurando los eventos de la vista.
	 */
	public void initController() {
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
	
	private boolean obtenerRevisiones() {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean obtenerArticulos() {
		// TODO Auto-generated method stub
		return true;
	}

}
