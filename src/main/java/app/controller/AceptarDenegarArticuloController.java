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
import app.model.GestionarDiscusionesCoordinadorModel;
import app.util.UserUtil;
import app.view.AceptarDenegarArticuloView;
import app.view.GestionarDiscusionesCoordinadorView;
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
	DefaultListModel<AceptarDenegarArticuloDTO> listModelAutoDenegar;
	DefaultListModel<AceptarDenegarArticuloDTO> listModelCambios;

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

		if (!obtenerArticulosCambios()) {
			return;
		}

		if (!obtenerArticulosAutomaticosDenegar()) {
			return;
		}

		// Inicializar la vista una vez que los datos están cargados.
		this.initView();
	}

	public void initController() {

		if (view.getListAutomaticos().getModel().getSize() == 0) {
			view.gettpListas().setEnabledAt(1, false);
		} else {
			view.gettpListas().setEnabledAt(1, true);
		}

		if (view.getListAceptarConCambios().getModel().getSize() == 0) {
			view.gettpListas().setEnabledAt(2, false);
		} else {
			view.gettpListas().setEnabledAt(2, true);
		}

		if (view.getListAutomaticosDenegar().getModel().getSize() == 0) {
			view.gettpListas().setEnabledAt(3, false);
		} else {
			view.gettpListas().setEnabledAt(3, true);
		}

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

		// Listener para el botón de discusiones que va a abrir la ventana de
		// GestionarDiscusionesCoordinadorView
		view.getBtnAbrirDiscusiones().addActionListener(e -> {
			// Crear el controlador de la vista de discusiones
			GestionarDiscusionesCoordinadorController controller = new GestionarDiscusionesCoordinadorController(
					new GestionarDiscusionesCoordinadorModel(), new GestionarDiscusionesCoordinadorView(), email);
			// Inicializar el controlador
			controller.initController();
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

		view.getListAutomaticosDenegar().addListSelectionListener(e -> {
			AceptarDenegarArticuloDTO articuloSeleccionado = view.getListAutomaticosDenegar().getSelectedValue();
			// Verifica si el objeto o su id son nulos
			if (articuloSeleccionado == null || articuloSeleccionado.getId() == 0) {
				return;
			}

			// Guarda el id en una variable (objeto Integer)
			Integer idSeleccionado = articuloSeleccionado.getId();
			llenarComboBoxAutores(view.getListAutomaticosDenegar().getSelectedValue().getTitulo());

		});

		view.getListAceptarConCambios().addListSelectionListener(e -> {
			AceptarDenegarArticuloDTO articuloSeleccionado = view.getListAceptarConCambios().getSelectedValue();
			// Verifica si el objeto o su id son nulos
			if (articuloSeleccionado == null || articuloSeleccionado.getId() == 0) {
				return;
			}

			// Guarda el id en una variable (objeto Integer)
			Integer idSeleccionado = articuloSeleccionado.getId();
			llenarComboBoxAutores(view.getListAceptarConCambios().getSelectedValue().getTitulo());
		});

		view.getcbRevisor().addActionListener(e -> {
			introducirDatos();

		});

		// Botón de aceptar, el cual cambia el campo decisión final a 'Aceptado'
		view.getbtnAceptar().addActionListener(e -> {
			// Si no se ha seleccionado nada en ninguna lista salta un error
			if (view.getListArticulos().getSelectedValue() == null
					&& view.getListAutomaticos().getSelectedValue() == null
					&& view.getListAutomaticosDenegar().getSelectedValue() == null) {
				JOptionPane.showMessageDialog(null, "No hay ningún artículo seleccionado", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				// Si el artículo seleccionado pertenece a la primera lista
				if (view.gettpListas().getSelectedIndex() == 0) {
					int indice = view.getListArticulos().getSelectedIndex();
					model.actualizarDecisionFinal("Aceptado", view.getListArticulos().getSelectedValue().getTitulo());
					SwingUtil.showMessage("Artículo aceptado correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					// Elimina el artículo de las dos listas
					for (int i = 0; i < view.getListAutomaticos().getModel().getSize(); i++) {

						if (view.getListArticulos().getSelectedValue().getTitulo()
								.equals(view.getListAutomaticos().getModel().getElementAt(i).getTitulo())) {
							listModelAuto.removeElement(view.getListAutomaticos().getModel().getElementAt(i));
						}
					}

					for (int i = 0; i < view.getListAceptarConCambios().getModel().getSize(); i++) {
						if (view.getListArticulos().getSelectedValue().getTitulo()
								.equals(view.getListAceptarConCambios().getModel().getElementAt(i).getTitulo())) {
							listModelCambios.removeElement(view.getListAceptarConCambios().getModel().getElementAt(i));
						}
					}

					for (int i = 0; i < view.getListAutomaticosDenegar().getModel().getSize(); i++) {
						if (view.getListArticulos().getSelectedValue().getTitulo()
								.equals(view.getListAutomaticosDenegar().getModel().getElementAt(i).getTitulo())) {
							listModelAutoDenegar
									.removeElement(view.getListAutomaticosDenegar().getModel().getElementAt(i));
						}
					}

					listModel.removeElement(view.getListArticulos().getSelectedValue());
					// Si está vacía se cierra la pantalla
					if (listModel.isEmpty()) {
						SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
								JOptionPane.INFORMATION_MESSAGE);
						view.getFrame().dispose();
					}
					// Cuando se elimina un elemento se va seleccionando el elemento anterior
					// excepto si el índice es 0
					if (indice != 0) {
						view.getListArticulos().setSelectedIndex(indice - 1);
					} else {
						view.getListArticulos().setSelectedIndex(0);
					}
					if (view.getListAutomaticos().getModel().getSize() == 0) {
						view.gettpListas().setEnabledAt(1, false);
						view.gettpListas().setSelectedIndex(0);
					}

					// Si el artículo seleccionado pertenece a la segunda lista
				} else if (view.gettpListas().getSelectedIndex() == 1) {
					int indice = view.getListAutomaticos().getSelectedIndex();
					model.actualizarDecisionFinal("Aceptado", view.getListAutomaticos().getSelectedValue().getTitulo());
					SwingUtil.showMessage("Artículo aceptado correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					// Si el artículo está en la otra lista, lo elimina también
					for (int i = 0; i < view.getListArticulos().getModel().getSize(); i++) {

						if (view.getListAutomaticos().getSelectedValue().getTitulo()
								.equals(view.getListArticulos().getModel().getElementAt(i).getTitulo())) {
							listModel.removeElement(view.getListArticulos().getModel().getElementAt(i));
						}
					}

					for (int i = 0; i < view.getListAceptarConCambios().getModel().getSize(); i++) {
						if (view.getListAutomaticos().getSelectedValue().getTitulo()
								.equals(view.getListAceptarConCambios().getModel().getElementAt(i).getTitulo())) {
							listModelCambios.removeElement(view.getListAceptarConCambios().getModel().getElementAt(i));
						}
					}

					for (int i = 0; i < view.getListAutomaticosDenegar().getModel().getSize(); i++) {
						if (view.getListAutomaticos().getSelectedValue().getTitulo()
								.equals(view.getListAutomaticosDenegar().getModel().getElementAt(i).getTitulo())) {
							listModelAutoDenegar
									.removeElement(view.getListAutomaticosDenegar().getModel().getElementAt(i));
						}
					}

					listModelAuto.removeElement(view.getListAutomaticos().getSelectedValue());
					// Si no hay elementos en las listas, se cierra la ventana
					if (listModelAuto.isEmpty() && listModel.isEmpty()) {
						SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
								JOptionPane.INFORMATION_MESSAGE);
						view.getFrame().dispose();
					}
					// Cuando se elimina un elemento se va seleccionando el elemento anterior
					// excepto si el índice es 0
					if (indice != 0) {
						view.getListAutomaticos().setSelectedIndex(indice - 1);
					} else {
						view.getListAutomaticos().setSelectedIndex(0);
					}
					if (view.getListAutomaticos().getModel().getSize() == 0) {
						view.gettpListas().setEnabledAt(1, false);
						view.gettpListas().setSelectedIndex(0);
					}
				} else if (view.gettpListas().getSelectedIndex() == 3) {
					int indice = view.getListAutomaticosDenegar().getSelectedIndex();
					model.actualizarDecisionFinal("Aceptado",
							view.getListAutomaticosDenegar().getSelectedValue().getTitulo());
					SwingUtil.showMessage("Artículo aceptado correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					// Si el artículo está en la otra lista, lo elimina también
					for (int i = 0; i < view.getListArticulos().getModel().getSize(); i++) {

						if (view.getListAutomaticosDenegar().getSelectedValue().getTitulo()
								.equals(view.getListArticulos().getModel().getElementAt(i).getTitulo())) {
							listModel.removeElement(view.getListArticulos().getModel().getElementAt(i));
						}
					}

					for (int i = 0; i < view.getListAceptarConCambios().getModel().getSize(); i++) {
						if (view.getListAutomaticosDenegar().getSelectedValue().getTitulo()
								.equals(view.getListAceptarConCambios().getModel().getElementAt(i).getTitulo())) {
							listModelCambios.removeElement(view.getListAceptarConCambios().getModel().getElementAt(i));
						}
					}

					for (int i = 0; i < view.getListAutomaticos().getModel().getSize(); i++) {
						if (view.getListAutomaticosDenegar().getSelectedValue().getTitulo()
								.equals(view.getListAutomaticos().getModel().getElementAt(i).getTitulo())) {
							listModelAuto.removeElement(view.getListAutomaticos().getModel().getElementAt(i));
						}
					}

					listModelAutoDenegar.removeElement(view.getListAutomaticosDenegar().getSelectedValue());
					// Si no hay elementos en las listas, se cierra la ventana
					if (listModelAuto.isEmpty() && listModel.isEmpty() && listModelAutoDenegar.isEmpty()) {
						SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
								JOptionPane.INFORMATION_MESSAGE);
						view.getFrame().dispose();
					}
					// Cuando se elimina un elemento se va seleccionando el elemento anterior
					// excepto si el índice es 0
					if (indice != 0) {
						view.getListAutomaticosDenegar().setSelectedIndex(indice - 1);
					} else {
						view.getListAutomaticosDenegar().setSelectedIndex(0);
					}
					if (view.getListAutomaticosDenegar().getModel().getSize() == 0) {
						view.gettpListas().setEnabledAt(3, false);
						view.gettpListas().setSelectedIndex(0);
					}
				}

			}

		});

		view.getbtnAceptarConCambios().addActionListener(e -> {
			// Si no se ha seleccionado nada en ninguna lista salta un error

			// Si el artículo seleccionado pertenece a la primera lista

			int indice = view.getListAceptarConCambios().getSelectedIndex();
			model.actualizarDecisionFinal("Aceptado con cambios",
					view.getListAceptarConCambios().getSelectedValue().getTitulo());
			SwingUtil.showMessage("Artículo aceptado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
			// Elimina el artículo de las dos listas
			for (int i = 0; i < view.getListArticulos().getModel().getSize(); i++) {

				if (view.getListAceptarConCambios().getSelectedValue().getTitulo()
						.equals(view.getListArticulos().getModel().getElementAt(i).getTitulo())) {
					listModel.removeElement(view.getListArticulos().getModel().getElementAt(i));
				}
			}

			for (int i = 0; i < view.getListAutomaticos().getModel().getSize(); i++) {

				if (view.getListAceptarConCambios().getSelectedValue().getTitulo()
						.equals(view.getListAutomaticos().getModel().getElementAt(i).getTitulo())) {
					listModelAuto.removeElement(view.getListAutomaticos().getModel().getElementAt(i));
				}
			}

			for (int i = 0; i < view.getListAutomaticosDenegar().getModel().getSize(); i++) {

				if (view.getListAceptarConCambios().getSelectedValue().getTitulo()
						.equals(view.getListAutomaticosDenegar().getModel().getElementAt(i).getTitulo())) {
					listModelAutoDenegar.removeElement(view.getListAutomaticosDenegar().getModel().getElementAt(i));
				}
			}

			listModelCambios.removeElement(view.getListAceptarConCambios().getSelectedValue());
			// Si está vacía se cierra la pantalla

			if (listModel.isEmpty() && listModelCambios.isEmpty() && listModelAuto.isEmpty()) {
				SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
						JOptionPane.INFORMATION_MESSAGE);
				view.getFrame().dispose();
			}

			if (view.getListAceptarConCambios().getModel().getSize() == 0) {
				view.gettpListas().setSelectedIndex(0);
				view.gettpListas().setEnabledAt(2, false);
			}
			// Cuando se elimina un elemento se va seleccionando el elemento anterior
			// excepto si el índice es 0
			if (indice != 0) {
				view.getListAceptarConCambios().setSelectedIndex(indice - 1);
			} else {
				view.getListAceptarConCambios().setSelectedIndex(0);
			}
			// Si el artículo seleccionado pertenece a la segunda lista
		});

		// Botón de rechazar, el cual cambia el campo decisión final a 'Rechazado'
		view.getbtnRechazar().addActionListener(e -> {
			// Si no se ha seleccionado nada en ninguna lista salta un error
			if (view.getListArticulos().getSelectedValue() == null
					&& view.getListAutomaticos().getSelectedValue() == null
					&& view.getListAceptarConCambios().getSelectedValue() == null
					&& view.getListAutomaticosDenegar().getSelectedValue() == null) {
				JOptionPane.showMessageDialog(null, "No hay ningún artículo seleccionado", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				// Si el artículo seleccionado pertenece a la primera lista
				if (view.gettpListas().getSelectedIndex() == 0) {
					int indice = view.getListArticulos().getSelectedIndex();
					model.actualizarDecisionFinal("Rechazado", view.getListArticulos().getSelectedValue().getTitulo());
					SwingUtil.showMessage("Artículo rechazado correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					// Elimina el artículo de las dos listas
					for (int i = 0; i < view.getListAutomaticos().getModel().getSize(); i++) {

						if (view.getListArticulos().getSelectedValue().getTitulo()
								.equals(view.getListAutomaticos().getModel().getElementAt(i).getTitulo())) {
							listModelAuto.removeElement(view.getListAutomaticos().getModel().getElementAt(i));
						}
					}

					for (int i = 0; i < view.getListAceptarConCambios().getModel().getSize(); i++) {
						if (view.getListArticulos().getSelectedValue().getTitulo()
								.equals(view.getListAceptarConCambios().getModel().getElementAt(i).getTitulo())) {
							listModelCambios.removeElement(view.getListAceptarConCambios().getModel().getElementAt(i));
						}
					}

					for (int i = 0; i < view.getListAutomaticosDenegar().getModel().getSize(); i++) {
						if (view.getListArticulos().getSelectedValue().getTitulo()
								.equals(view.getListAutomaticosDenegar().getModel().getElementAt(i).getTitulo())) {
							listModelAutoDenegar
									.removeElement(view.getListAutomaticosDenegar().getModel().getElementAt(i));
						}
					}

					listModel.removeElement(view.getListArticulos().getSelectedValue());

					// Si está vacía se cierra la pantalla
					if (listModel.isEmpty()) {
						SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
								JOptionPane.INFORMATION_MESSAGE);
						view.getFrame().dispose();
					}
					// Cuando se elimina un elemento se va seleccionando el elemento anterior
					// excepto si el índice es 0
					if (indice != 0) {
						view.getListArticulos().setSelectedIndex(indice - 1);
					} else {
						view.getListArticulos().setSelectedIndex(0);
					}
					// Si el artículo seleccionado pertenece a la segunda lista
				} else if (view.gettpListas().getSelectedIndex() == 1) {
					int indice = view.getListAutomaticos().getSelectedIndex();
					model.actualizarDecisionFinal("Rechazado",
							view.getListAutomaticos().getSelectedValue().getTitulo());
					SwingUtil.showMessage("Artículo rechazado correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					// Si el artículo está en la otra lista, lo elimina también
					for (int i = 0; i < view.getListArticulos().getModel().getSize(); i++) {

						if (view.getListAutomaticos().getSelectedValue().getTitulo()
								.equals(view.getListArticulos().getModel().getElementAt(i).getTitulo())) {
							listModel.removeElement(view.getListArticulos().getModel().getElementAt(i));
						}
					}

					for (int i = 0; i < view.getListAceptarConCambios().getModel().getSize(); i++) {

						if (view.getListAutomaticos().getSelectedValue().getTitulo()
								.equals(view.getListAceptarConCambios().getModel().getElementAt(i).getTitulo())) {
							listModelCambios.removeElement(view.getListAceptarConCambios().getModel().getElementAt(i));
						}
					}

					for (int i = 0; i < view.getListAutomaticosDenegar().getModel().getSize(); i++) {

						if (view.getListAutomaticos().getSelectedValue().getTitulo()
								.equals(view.getListAutomaticosDenegar().getModel().getElementAt(i).getTitulo())) {
							listModelAutoDenegar
									.removeElement(view.getListAutomaticosDenegar().getModel().getElementAt(i));
						}
					}

					listModelAuto.removeElement(view.getListAutomaticos().getSelectedValue());
					// Si no hay elementos en las listas, se cierra la ventana
					if (listModelAuto.isEmpty() && listModel.isEmpty() && listModelCambios.isEmpty()) {
						SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
								JOptionPane.INFORMATION_MESSAGE);
						view.getFrame().dispose();
					}
					// Cuando se elimina un elemento se va seleccionando el elemento anterior
					// excepto si el índice es 0
					if (indice != 0) {
						view.getListAutomaticos().setSelectedIndex(indice - 1);
					} else {
						view.getListAutomaticos().setSelectedIndex(0);
					}
					if (view.getListAutomaticos().getModel().getSize() == 0) {
						view.gettpListas().setSelectedIndex(0);
						view.gettpListas().setEnabledAt(1, false);
					}
				} else if (view.gettpListas().getSelectedIndex() == 2) {
					int indice = view.getListAceptarConCambios().getSelectedIndex();
					model.actualizarDecisionFinal("Rechazado",
							view.getListAceptarConCambios().getSelectedValue().getTitulo());
					SwingUtil.showMessage("Artículo rechazado correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					// Si el artículo está en la otra lista, lo elimina también
					for (int i = 0; i < view.getListArticulos().getModel().getSize(); i++) {

						if (view.getListAceptarConCambios().getSelectedValue().getTitulo()
								.equals(view.getListArticulos().getModel().getElementAt(i).getTitulo())) {
							listModel.removeElement(view.getListArticulos().getModel().getElementAt(i));
						}
					}

					for (int i = 0; i < view.getListAutomaticos().getModel().getSize(); i++) {

						if (view.getListAceptarConCambios().getSelectedValue().getTitulo()
								.equals(view.getListAutomaticos().getModel().getElementAt(i).getTitulo())) {
							listModelAuto.removeElement(view.getListAutomaticos().getModel().getElementAt(i));
						}
					}

					for (int i = 0; i < view.getListAutomaticosDenegar().getModel().getSize(); i++) {

						if (view.getListAceptarConCambios().getSelectedValue().getTitulo()
								.equals(view.getListAutomaticosDenegar().getModel().getElementAt(i).getTitulo())) {
							listModelAutoDenegar
									.removeElement(view.getListAutomaticosDenegar().getModel().getElementAt(i));
						}
					}

					listModelCambios.removeElement(view.getListAceptarConCambios().getSelectedValue());
					// Si no hay elementos en las listas, se cierra la ventana
					if (listModelAuto.isEmpty() && listModel.isEmpty() && listModelCambios.isEmpty()) {
						SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
								JOptionPane.INFORMATION_MESSAGE);
						view.getFrame().dispose();
					}
					// Cuando se elimina un elemento se va seleccionando el elemento anterior
					// excepto si el índice es 0
					if (indice != 0) {
						view.getListAceptarConCambios().setSelectedIndex(indice - 1);
					} else {
						view.getListAceptarConCambios().setSelectedIndex(0);
					}
					if (view.getListAceptarConCambios().getModel().getSize() == 0) {
						view.gettpListas().setSelectedIndex(0);
						view.gettpListas().setEnabledAt(2, false);
					}

				} else {
					int indice = view.getListAutomaticosDenegar().getSelectedIndex();
					model.actualizarDecisionFinal("Rechazado",
							view.getListAutomaticosDenegar().getSelectedValue().getTitulo());
					SwingUtil.showMessage("Artículo rechazado correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					// Si el artículo está en la otra lista, lo elimina también
					for (int i = 0; i < view.getListArticulos().getModel().getSize(); i++) {

						if (view.getListAutomaticosDenegar().getSelectedValue().getTitulo()
								.equals(view.getListArticulos().getModel().getElementAt(i).getTitulo())) {
							listModel.removeElement(view.getListArticulos().getModel().getElementAt(i));
						}
					}

					for (int i = 0; i < view.getListAutomaticos().getModel().getSize(); i++) {

						if (view.getListAutomaticosDenegar().getSelectedValue().getTitulo()
								.equals(view.getListAutomaticos().getModel().getElementAt(i).getTitulo())) {
							listModelAuto.removeElement(view.getListAutomaticos().getModel().getElementAt(i));
						}
					}

					for (int i = 0; i < view.getListAceptarConCambios().getModel().getSize(); i++) {

						if (view.getListAutomaticosDenegar().getSelectedValue().getTitulo()
								.equals(view.getListAceptarConCambios().getModel().getElementAt(i).getTitulo())) {
							listModelCambios.removeElement(view.getListAceptarConCambios().getModel().getElementAt(i));
						}
					}

					listModelAutoDenegar.removeElement(view.getListAutomaticosDenegar().getSelectedValue());
					// Si no hay elementos en las listas, se cierra la ventana
					if (listModelAuto.isEmpty() && listModel.isEmpty() && listModelCambios.isEmpty()
							&& listModelAutoDenegar.isEmpty()) {
						SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
								JOptionPane.INFORMATION_MESSAGE);
						view.getFrame().dispose();
					}
					// Cuando se elimina un elemento se va seleccionando el elemento anterior
					// excepto si el índice es 0
					if (indice != 0) {
						view.getListAutomaticosDenegar().setSelectedIndex(indice - 1);
					} else {
						view.getListAutomaticosDenegar().setSelectedIndex(0);
					}
					if (view.getListAutomaticosDenegar().getModel().getSize() == 0) {
						view.gettpListas().setSelectedIndex(0);
						view.gettpListas().setEnabledAt(3, false);
					}
				}
			}

		});

		// Cada vez que se selecciona una lista, cambia el botón aceptar y se
		// deseleccionan las demás listas
		view.gettpListas().addChangeListener(e -> {

			if (view.gettpListas().getSelectedIndex() == 0 && !listModel.isEmpty()) {

				if (view.getbtnAceptar().getParent() == null) {
					view.getContentPane().remove(view.getbtnAceptarConCambios());
					view.getContentPane().add(view.getbtnAceptar(), "cell 10 13");
					view.getContentPane().repaint();

				}
				view.getbtnAceptarTodos().setEnabled(false);
				view.getbtnDenegarTodos().setEnabled(false);
				view.getListArticulos().setSelectedIndex(0);
				introducirDatos();
				view.getListAutomaticos().clearSelection();
				view.getListAceptarConCambios().clearSelection();
			} else if (view.gettpListas().getSelectedIndex() == 1) {
				if (view.getbtnAceptar().getParent() == null) {
					view.getContentPane().remove(view.getbtnAceptarConCambios());
					view.getContentPane().add(view.getbtnAceptar(), "cell 10 13");
					view.getContentPane().repaint();

				}
				view.getbtnAceptarTodos().setEnabled(true);
				view.getbtnDenegarTodos().setEnabled(false);
				view.getListArticulos().clearSelection();
				if (view.getListAutomaticos().getModel().getSize() > 0) {
					view.getListAutomaticos().setSelectedIndex(0);
					introducirDatos();
				}
			} else if (view.gettpListas().getSelectedIndex() == 2) {
				view.getListArticulos().clearSelection();
				view.getListAceptarConCambios().clearSelection();
				view.getbtnAceptarTodos().setEnabled(false);
				view.getbtnDenegarTodos().setEnabled(false);
				if (view.getListAceptarConCambios().getModel().getSize() > 0) {
					view.getListAceptarConCambios().setSelectedIndex(0);
					introducirDatos();
				}

				view.getContentPane().remove(view.getbtnAceptar());
				view.getContentPane().add(view.getbtnAceptarConCambios(), "cell 10 13");
				view.getContentPane().revalidate();
				view.getContentPane().repaint();
			} else {
				if (view.getbtnAceptar().getParent() == null) {
					view.getContentPane().remove(view.getbtnAceptarConCambios());
					view.getContentPane().add(view.getbtnAceptar(), "cell 10 13");
					view.getContentPane().repaint();

				}
				view.getbtnAceptarTodos().setEnabled(false);
				view.getbtnDenegarTodos().setEnabled(true);
				view.getListArticulos().clearSelection();
				if (view.getListAutomaticosDenegar().getModel().getSize() > 0) {
					view.getListAutomaticosDenegar().setSelectedIndex(0);
					introducirDatos();
				}
			}
		});

		view.getbtnAceptarTodos().addActionListener(e -> {
			if (!listModelAuto.isEmpty()) {
				for (int i = 0; i < view.getListAutomaticos().getModel().getSize(); i++) {
					model.actualizarDecisionFinal("Aceptado",
							view.getListAutomaticos().getModel().getElementAt(i).getTitulo());
					// Si los artículos coinciden, los quito de la otra lista
					for (int j = 0; j < view.getListArticulos().getModel().getSize(); j++) {
						if (view.getListAutomaticos().getModel().getElementAt(i).getTitulo()
								.equals(view.getListArticulos().getModel().getElementAt(j).getTitulo())) {
							listModel.removeElement(view.getListArticulos().getModel().getElementAt(j));
						}
					}

					for (int j = 0; j < view.getListAceptarConCambios().getModel().getSize(); j++) {
						if (view.getListAutomaticos().getModel().getElementAt(i).getTitulo()
								.equals(view.getListAceptarConCambios().getModel().getElementAt(j).getTitulo())) {
							listModelCambios.removeElement(view.getListAceptarConCambios().getModel().getElementAt(j));
						}
					}

					for (int j = 0; j < view.getListAutomaticosDenegar().getModel().getSize(); j++) {
						if (view.getListAutomaticos().getModel().getElementAt(i).getTitulo()
								.equals(view.getListAutomaticosDenegar().getModel().getElementAt(j).getTitulo())) {
							listModelAutoDenegar
									.removeElement(view.getListAutomaticosDenegar().getModel().getElementAt(j));
						}
					}
				}
				// Si al quitar el elemento la lista de articulos sin decisión está vacía,se
				// cierra la ventana
				if (listModel.isEmpty()) {
					SwingUtil.showMessage("Artículos aceptados correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					view.getFrame().dispose();
				} else {
					// Si no, se eliminan todos los elementos y se pasa a la otra lista
					listModelAuto.removeAllElements();
					view.gettpListas().setSelectedIndex(0);
					view.getListArticulos().setSelectedIndex(0);
					SwingUtil.showMessage("Artículos aceptados correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);
				}
				if (view.getListAutomaticos().getModel().getSize() == 0) {
					view.gettpListas().setSelectedIndex(0);
					view.gettpListas().setEnabledAt(1, false);
				}
			} else {
				JOptionPane.showMessageDialog(null, "No quedan artículos que aceptar", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			if (view.getListAutomaticos().getModel().getSize() == 0) {
				view.gettpListas().setEnabledAt(1, false);
			}
			if (view.getListAceptarConCambios().getModel().getSize() == 0) {
				view.gettpListas().setEnabledAt(2, false);
			}
			if (view.getListAutomaticosDenegar().getModel().getSize() == 0) {
				view.gettpListas().setEnabledAt(3, false);
			}
		});

		view.getbtnDenegarTodos().addActionListener(e -> {
			if (!listModelAutoDenegar.isEmpty()) {
				for (int i = 0; i < view.getListAutomaticosDenegar().getModel().getSize(); i++) {
					model.actualizarDecisionFinal("Rechazado",
							view.getListAutomaticosDenegar().getModel().getElementAt(i).getTitulo());
					// Si los artículos coinciden, los quito de la otra lista
					for (int j = 0; j < view.getListArticulos().getModel().getSize(); j++) {
						if (view.getListAutomaticosDenegar().getModel().getElementAt(i).getTitulo()
								.equals(view.getListArticulos().getModel().getElementAt(j).getTitulo())) {
							listModel.removeElement(view.getListArticulos().getModel().getElementAt(j));
						}
					}

					for (int j = 0; j < view.getListAceptarConCambios().getModel().getSize(); j++) {
						if (view.getListAutomaticosDenegar().getModel().getElementAt(i).getTitulo()
								.equals(view.getListAceptarConCambios().getModel().getElementAt(j).getTitulo())) {
							listModelCambios.removeElement(view.getListAceptarConCambios().getModel().getElementAt(j));
						}
					}

					for (int j = 0; j < view.getListAutomaticos().getModel().getSize(); j++) {
						if (view.getListAutomaticosDenegar().getModel().getElementAt(i).getTitulo()
								.equals(view.getListAutomaticos().getModel().getElementAt(j).getTitulo())) {
							listModelAuto.removeElement(view.getListAutomaticos().getModel().getElementAt(j));
						}
					}
				}
				// Si al quitar el elemento la lista de articulos sin decisión está vacía,se
				// cierra la ventana
				if (listModel.isEmpty()) {
					SwingUtil.showMessage("Artículos aceptados correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					view.getFrame().dispose();
				} else {
					// Si no, se eliminan todos los elementos y se pasa a la otra lista
					listModelAutoDenegar.removeAllElements();
					view.gettpListas().setSelectedIndex(0);
					view.getListArticulos().setSelectedIndex(0);
					SwingUtil.showMessage("Artículos aceptados correctamente", "Información",
							JOptionPane.INFORMATION_MESSAGE);
				}
				if (view.getListAutomaticosDenegar().getModel().getSize() == 0) {
					view.gettpListas().setSelectedIndex(0);
					view.gettpListas().setEnabledAt(3, false);
				}
			} else {
				JOptionPane.showMessageDialog(null, "No quedan artículos que aceptar", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			if (view.getListAutomaticos().getModel().getSize() == 0) {
				view.gettpListas().setEnabledAt(1, false);
			}
			if (view.getListAceptarConCambios().getModel().getSize() == 0) {
				view.gettpListas().setEnabledAt(2, false);
			}
			if (view.getListAutomaticosDenegar().getModel().getSize() == 0) {
				view.gettpListas().setEnabledAt(3, false);
			}
		});

	}

	public void initView() {
		view.getFrame().setVisible(true);
		// Asignar el modelo al JList de la vista
		view.getListArticulos().setModel(listModel);
		view.getListAutomaticos().setModel(listModelAuto);
		view.getListAutomaticosDenegar().setModel(listModelAutoDenegar);
		view.getListAceptarConCambios().setModel(listModelCambios);

	}

	// Método para obtener los artículos cuya decisión final sea Pendiente e
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

	private boolean obtenerArticulosAutomaticosDenegar() {
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
		listModelAutoDenegar = new DefaultListModel<>();
		for (AceptarDenegarArticuloDTO dto : listaDTO) {
			if (valoracion(dto.getTitulo()) <= -2) {
				listModelAutoDenegar.addElement(dto);
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

	private boolean obtenerArticulosCambios() {
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
		listModelCambios = new DefaultListModel<>();
		for (AceptarDenegarArticuloDTO dto : listaDTO) {

			if (valoracion(dto.getTitulo()) == 2 || (valoracion(dto.getTitulo()) == 1 && condicionRevisor(dto))) {
				listModelCambios.addElement(dto);
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

	// Método para rellenar el combo box con los revisores asignados a ese artículo
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

	// Método para insertar el comentario del autor en su TextPane
	public void insertaComentarioAutor() {
		if (view.gettpListas().getSelectedIndex() == 0) {
			comentarioAutor = model.obtenerComentariosParaAutor((String) view.getcbRevisor().getSelectedItem(),
					view.getListArticulos().getSelectedValue().getTitulo());
		} else if (view.gettpListas().getSelectedIndex() == 1) {
			comentarioAutor = model.obtenerComentariosParaAutor((String) view.getcbRevisor().getSelectedItem(),
					view.getListAutomaticos().getSelectedValue().getTitulo());
		} else if (view.gettpListas().getSelectedIndex() == 2) {
			comentarioAutor = model.obtenerComentariosParaAutor((String) view.getcbRevisor().getSelectedItem(),
					view.getListAceptarConCambios().getSelectedValue().getTitulo());
		} else {
			comentarioAutor = model.obtenerComentariosParaAutor((String) view.getcbRevisor().getSelectedItem(),
					view.getListAutomaticosDenegar().getSelectedValue().getTitulo());
		}

		if (comentarioAutor != null && !comentarioAutor.isEmpty()) {
			view.gettpComentariosParaAutores().setText(comentarioAutor.get(0).getComentariosParaAutor());
		}

	}

	// Método para insertar el comentario del coordinador en su TextPane
	public void insertaComentarioCoordinador() {
		if (view.gettpListas().getSelectedIndex() == 0) {
			comentarioCoordinador = model.obtenerComentariosParaCoordinador(
					(String) view.getcbRevisor().getSelectedItem(),
					view.getListArticulos().getSelectedValue().getTitulo());
		} else if (view.gettpListas().getSelectedIndex() == 1) {
			comentarioCoordinador = model.obtenerComentariosParaCoordinador(
					(String) view.getcbRevisor().getSelectedItem(),
					view.getListAutomaticos().getSelectedValue().getTitulo());
		} else if (view.gettpListas().getSelectedIndex() == 2) {
			comentarioCoordinador = model.obtenerComentariosParaCoordinador(
					(String) view.getcbRevisor().getSelectedItem(),
					view.getListAceptarConCambios().getSelectedValue().getTitulo());
		} else {
			comentarioCoordinador = model.obtenerComentariosParaCoordinador(
					(String) view.getcbRevisor().getSelectedItem(),
					view.getListAutomaticosDenegar().getSelectedValue().getTitulo());
		}

		if (comentarioCoordinador != null && !comentarioCoordinador.isEmpty()) {
			view.gettpComentariosParaCoordinadores()
					.setText(comentarioCoordinador.get(0).getComentariosParaCoordinador());
		}

	}

	// Método para insertar el nivel de experto en su TextField
	public void insertaNivelExperto() {
		if (view.gettpListas().getSelectedIndex() == 0) {
			nivelExperto = model.obtenerNivelExperto((String) view.getcbRevisor().getSelectedItem(),
					view.getListArticulos().getSelectedValue().getTitulo());
		} else if (view.gettpListas().getSelectedIndex() == 1) {
			nivelExperto = model.obtenerNivelExperto((String) view.getcbRevisor().getSelectedItem(),
					view.getListAutomaticos().getSelectedValue().getTitulo());
		} else if (view.gettpListas().getSelectedIndex() == 2) {
			nivelExperto = model.obtenerNivelExperto((String) view.getcbRevisor().getSelectedItem(),
					view.getListAceptarConCambios().getSelectedValue().getTitulo());
		} else {
			nivelExperto = model.obtenerNivelExperto((String) view.getcbRevisor().getSelectedItem(),
					view.getListAutomaticosDenegar().getSelectedValue().getTitulo());
		}

		if (nivelExperto != null && !nivelExperto.isEmpty()) {
			view.gettfNivelDeExperto().setText(nivelExperto.get(0).getNivelExperto());
		}
	}

	// Método para insertar la decisión del revisor en su TextField
	public void insertaDecision() {
		if (view.gettpListas().getSelectedIndex() == 0) {
			decision = model.obtenerDecisionRevisor((String) view.getcbRevisor().getSelectedItem(),
					view.getListArticulos().getSelectedValue().getTitulo());
		} else if (view.gettpListas().getSelectedIndex() == 1) {
			decision = model.obtenerDecisionRevisor((String) view.getcbRevisor().getSelectedItem(),
					view.getListAutomaticos().getSelectedValue().getTitulo());
		} else if (view.gettpListas().getSelectedIndex() == 2) {
			decision = model.obtenerDecisionRevisor((String) view.getcbRevisor().getSelectedItem(),
					view.getListAceptarConCambios().getSelectedValue().getTitulo());
		} else {
			decision = model.obtenerDecisionRevisor((String) view.getcbRevisor().getSelectedItem(),
					view.getListAutomaticosDenegar().getSelectedValue().getTitulo());
		}

		if (decision != null && !decision.isEmpty()) {
			view.gettfDecision().setText(String.valueOf(decision.get(0).getDecisionRevisor()));
			if (decision.get(0).getDecisionRevisor() == 0) {
				view.gettfDecision().setText("");
			}
		}

	}

	// Método para calcular la suma de todas las decisiones de los revisores de un
	// artículo e
	// insértala en su TexFIeld
	public void calculaValoracion() {
		valoracionFinal = 0;
		view.gettfValoracion().removeAll();

		if (view.gettpListas().getSelectedIndex() == 0) {
			valoraciones = model.obtenerTodasDecisiones(view.getListArticulos().getSelectedValue().getTitulo());
		} else if (view.gettpListas().getSelectedIndex() == 1) {
			valoraciones = model.obtenerTodasDecisiones(view.getListAutomaticos().getSelectedValue().getTitulo());
		} else if (view.gettpListas().getSelectedIndex() == 2) {
			valoraciones = model.obtenerTodasDecisiones(view.getListAceptarConCambios().getSelectedValue().getTitulo());
		} else {
			valoraciones = model
					.obtenerTodasDecisiones(view.getListAutomaticosDenegar().getSelectedValue().getTitulo());
		}

		if (decision != null && !decision.isEmpty()) {
			for (AceptarDenegarArticuloDTO decisiones : valoraciones) {
				valoracionFinal += decisiones.getDecisionRevisor();
			}
			view.gettfValoracion().setText(String.valueOf(valoracionFinal));
		}
	}

	// Método para calcular la suma de todas las decisiones de los revisores de un
	// artículo
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

	public boolean condicionRevisor(AceptarDenegarArticuloDTO dto) {
		// Obtiene todos los revisores vinculados a ese artículo
		revisores = model.obtenerRevisoresPorTitulo(dto.getTitulo());
		// Mira si paracada uno de los revisores se cumple la condición
		for (int i = 0; i < revisores.size(); i++) {
			decision = model.obtenerDecisionRevisor(revisores.get(i).getNombre(), dto.getTitulo());
			nivelExperto = model.obtenerNivelExperto(revisores.get(i).getNombre(), dto.getTitulo());
			if (decision.get(0).getDecisionRevisor() == -2 && nivelExperto.get(0).getNivelExperto().equals("Bajo")) {
				return true;
			} else if (decision.get(0).getDecisionRevisor() == -1
					&& (nivelExperto.get(0).getNivelExperto().equals("Bajo")
							|| nivelExperto.get(0).getNivelExperto().equals("Normal"))) {
				return true;
			}
		}
		return false;
	}

	// Método que inserta los datos en pantalla
	public void introducirDatos() {
		insertaNivelExperto();
		insertaDecision();
		insertaComentarioAutor();
		insertaComentarioCoordinador();
		calculaValoracion();
	}

}
