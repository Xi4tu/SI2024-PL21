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
import app.model.SubrevisorArticulosModel;
import app.util.UserUtil;
import app.view.GestionarDiscusionesCoordinadorView;
import app.view.PedirColaboradorView;
import app.view.RevisionArticuloRevisorView;
import app.view.SubrevisorArticulosView;
import giis.demo.util.SwingUtil;

public class SubrevisorArticulosController {
	private SubrevisorArticulosModel model;
	private SubrevisorArticulosView view;
	private List<RevisionArticuloRevisorDTO> articulos;
	private List<RevisionArticuloRevisorDTO> articulosCoolab;
	private List<RevisionArticuloRevisorDTO> nombre;
	private List<RevisionArticuloRevisorDTO> nombreRevisor;
	private List<RevisionArticuloRevisorDTO> emailSuyo;
	private List<RevisionArticuloRevisorDTO> comentarios;
	DefaultListModel<RevisionArticuloRevisorDTO> listModel;
	private String email;
	private static final Rol ROL = Rol.REVISOR;

	/*
	 * Constructor del controlador
	 */
	public SubrevisorArticulosController(SubrevisorArticulosModel m, SubrevisorArticulosView v, String email) {
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
			nombreRevisor = model.obtenerNombreRevisor(nombre.get(0).getNombre());
			emailSuyo = model.obtenerEmailNombre(nombreRevisor.get(0).getNombre());
			comentarios = model.obtenerComentarios(view.getListArticulos().getSelectedValue().getId(),
					emailSuyo.get(0).getEmail());
			if (!e.getValueIsAdjusting()) {
				int index = view.getListArticulos().getSelectedIndex();
				if (index >= 0) {
					RevisionArticuloRevisorDTO articulo = articulos.get(index);
				}
			}
			cargarMensajesChat();
			view.getTxtComentariosAutores().setText(comentarios.get(0).getComentariosParaAutor());
			view.getTxtComentariosCoordinadores().setText(comentarios.get(0).getComentariosParaCoordinador());
		});
		
		view.getBtnEnviarChat().addActionListener(e -> {
		    String mensaje = view.getInputChat().getText().trim();
		    if (!mensaje.isEmpty()) {
		    	model.chatMensajes(view.getListArticulos().getSelectedValue().getId(), nombre.get(0).getNombre(), nombreRevisor.get(0).getNombre(), mensaje);  // Guarda el mensaje
	            view.getInputChat().setText("");  // Limpia campo de texto
	            cargarMensajesChat();

		    }
		});

		// Listener para alternar entre "Pendientes" y "Revisados"

		// Listener al cambiar el revisor seleccionado en el combo

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
		model.actualizarViejo(view.getTxtComentariosAutores().getText(),
				view.getTxtComentariosCoordinadores().getText(), view.getListArticulos().getSelectedValue().getId(),
				emailSuyo.get(0).getEmail());
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
		nombre = model.obtenerNombreEmail(email);
		articulos = model.obtenerCooperadores(nombre.get(0).getNombre());
		// Convertir cada Articulo a ArticuloDTO
		List<RevisionArticuloRevisorDTO> listaDTO = new ArrayList<>();
		for (RevisionArticuloRevisorDTO articulo : articulos) {
			RevisionArticuloRevisorDTO dto = new RevisionArticuloRevisorDTO(articulo.getId(), articulo.getTitulo(),
					articulo.getEstado(), articulo.getNombreRevisor());
			listaDTO.add(dto);
		}
		/*
		 * for (RevisionArticuloRevisorDTO articulo : articulosCoolab) {
		 * RevisionArticuloRevisorDTO dto = new
		 * RevisionArticuloRevisorDTO(articulo.getId(), articulo.getTitulo(),
		 * articulo.getNombre(), articulo.getNombreFichero()); if
		 * (articulo.getNombre().equals(nombre.get(0).getNombre())) { listaDTO.add(dto);
		 * } }
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

	private void cargarMensajesChat() {
	    List<RevisionArticuloRevisorDTO> mensajes = model.obtenerMensajesChat(view.getListArticulos().getSelectedValue().getId(), nombre.get(0).getNombre(), nombreRevisor.get(0).getNombre());

	    StringBuilder textoChat = new StringBuilder();
	    for (RevisionArticuloRevisorDTO msg : mensajes) {
	        textoChat.append("[").append(msg.getNumeroMensaje()).append("] ");
	        textoChat.append(msg.getRemitente()).append(": ");
	        textoChat.append(msg.getMensaje()).append("\n");
	    }

	    view.getChatArea().setText(textoChat.toString());
	}
	
	private void actualizarListaArticulos(boolean soloPendientes) {
		listModel.clear();
		List<RevisionArticuloRevisorDTO> nuevaLista;

		if (soloPendientes) {
			nuevaLista = model.obtenerCooperadores(email);
		} else {
			nuevaLista = model.obtenerCooperadores(email);
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
