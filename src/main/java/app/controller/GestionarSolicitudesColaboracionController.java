package app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import app.dto.AccederDiscusionDTO;
import app.dto.AceptarDenegarArticuloDTO;
import app.dto.GestionarSolicitudesColaboracionDTO;
import app.enums.Rol;
import app.model.AceptarDenegarArticuloModel;
import app.model.GestionarSolicitudesColaboracionModel;
import app.util.UserUtil;
import app.view.AceptarDenegarArticuloView;
import app.view.GestionarSolicitudesColaboracionView;
import giis.demo.util.SwingUtil;

public class GestionarSolicitudesColaboracionController {

	private GestionarSolicitudesColaboracionModel model;
	private GestionarSolicitudesColaboracionView view;
	private String email;
	private List<GestionarSolicitudesColaboracionDTO> colaboradores;
	DefaultListModel<GestionarSolicitudesColaboracionDTO> listModel;
	private List<GestionarSolicitudesColaboracionDTO> nombre;

	private static final Rol ROL = Rol.REVISOR;

	public GestionarSolicitudesColaboracionController(GestionarSolicitudesColaboracionModel m,
			GestionarSolicitudesColaboracionView v, String email) {
		this.model = m;
		this.view = v;
		this.email = email;

		if (!UserUtil.checkEmail(email, ROL.getNombre(), model.getDbUtil())) {
			// Detener la inicialización si el email es inválido.
			return;
		}
		nombre = model.obtenerNombre(email);
		if (!obtenerColaboradores()) {
			return;
		}
		initView();
	}

	public void initController() {
		view.getRdbtnFiltrarPorTrack().addActionListener(e -> {
			view.getRdbtnFiltrarPorPalabrasAsociadas().setSelected(false);
			if (view.getRdbtnFiltrarPorTrack().isSelected()) {
				view.getTfBuscador().setEnabled(true);
				view.getBtnFiltrar().setEnabled(true);
			} else {
				view.getTfBuscador().setEnabled(false);
				view.getBtnFiltrar().setEnabled(false);
			}
		});
		view.getRdbtnFiltrarPorPalabrasAsociadas().addActionListener(e -> {
			view.getRdbtnFiltrarPorTrack().setSelected(false);
			if (view.getRdbtnFiltrarPorPalabrasAsociadas().isSelected()) {
				view.getTfBuscador().setEnabled(true);
				view.getBtnFiltrar().setEnabled(true);
			} else {
				view.getTfBuscador().setEnabled(false);
				view.getBtnFiltrar().setEnabled(false);
			}
		});

		view.getBtnBorrarFiltro().addActionListener(e -> {
			view.getTfBuscador().setEnabled(false);
			view.getBtnFiltrar().setEnabled(false);
			view.getRdbtnFiltrarPorTrack().setSelected(false);
			view.getRdbtnFiltrarPorPalabrasAsociadas().setSelected(false);
			view.getTfBuscador().setText("");
			listModel.clear();
			for (GestionarSolicitudesColaboracionDTO dto : colaboradores) {
				if (dto.getNombreColaborador().equals(nombre.get(0).getNombre())) {
					listModel.addElement(dto);
				}
			}
		});

		view.getBtnFiltrar().addActionListener(e -> {
			if (view.getRdbtnFiltrarPorTrack().isSelected()) {
				String busqueda = view.getTfBuscador().getText();
				if (busqueda.isEmpty()) {
					SwingUtil.showMessage("Introduce un texto para filtrar", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				List<GestionarSolicitudesColaboracionDTO> listaFiltrada = new ArrayList<>();
				for (GestionarSolicitudesColaboracionDTO colaborador : colaboradores) {
					if (colaborador.getNombreTrack().toLowerCase().contains(busqueda.toLowerCase())
							&& colaborador.getNombreColaborador().equals(nombre.get(0).getNombre())) {
						listaFiltrada.add(colaborador);
					}
				}
				listModel.clear();
				if (listaFiltrada.isEmpty()) {
					SwingUtil.showMessage("No hay tracks con ese nombre", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					for (GestionarSolicitudesColaboracionDTO dto : listaFiltrada) {
						listModel.addElement(dto);
					}
				}
			} else if (view.getRdbtnFiltrarPorPalabrasAsociadas().isSelected()) {
				String busqueda = view.getTfBuscador().getText();
				// Lista de palabras clave
				if (busqueda.isEmpty()) {
					SwingUtil.showMessage("Introduce un texto para filtrar", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				List<GestionarSolicitudesColaboracionDTO> listaFiltrada = new ArrayList<>();
				for (GestionarSolicitudesColaboracionDTO colaborador : colaboradores) {
					if (colaborador.getPalabrasClaveTrack().toLowerCase().contains(busqueda.toLowerCase())
							&& colaborador.getNombreColaborador().equals(nombre.get(0).getNombre())) {
						listaFiltrada.add(colaborador);
						break;
					}
				}
				listModel.clear();
				if (listaFiltrada.isEmpty()) {
					SwingUtil.showMessage("No hay tracks con ese nombre", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					for (GestionarSolicitudesColaboracionDTO dto : listaFiltrada) {
						listModel.addElement(dto);
					}
				}
			}
		});

		view.getBtnAceptar().addActionListener(e -> {
			int indice = view.getListColaboradores().getSelectedIndex();
			if (view.getListColaboradores().getSelectedValue() == null) {
				SwingUtil.showMessage("Selecciona un artículo", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				model.actualizarEstadoColaborador("Aceptado", view.getListColaboradores().getSelectedValue().getNombreColaborador(),
						view.getListColaboradores().getSelectedValue().getTitulo());
				SwingUtil.showMessage("Artículo aceptado correctamente", "Información",
						JOptionPane.INFORMATION_MESSAGE);
				listModel.removeElement(view.getListColaboradores().getSelectedValue());
				if (indice != 0) {
					view.getListColaboradores().setSelectedIndex(indice - 1);
				} else {
					view.getListColaboradores().setSelectedIndex(0);
				}
				// Si está vacía se cierra la pantalla
				if (listModel.isEmpty()) {
					SwingUtil.showMessage("No tienes ningún artículo pendiente de gestionar", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					view.getFrame().dispose();
				}
			}
		});
		
		view.getBtnRechazar().addActionListener(e -> {
			int indice = view.getListColaboradores().getSelectedIndex();
			if (view.getListColaboradores().getSelectedValue() == null) {
				SwingUtil.showMessage("Selecciona un artículo", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				model.actualizarEstadoColaborador("Rechazado", view.getListColaboradores().getSelectedValue().getNombreColaborador(),
						view.getListColaboradores().getSelectedValue().getTitulo());
				SwingUtil.showMessage("Artículo rechazado correctamente", "Información",
						JOptionPane.INFORMATION_MESSAGE);
				listModel.removeElement(view.getListColaboradores().getSelectedValue());
				if (indice != 0) {
					view.getListColaboradores().setSelectedIndex(indice - 1);
				} else {
					view.getListColaboradores().setSelectedIndex(0);
				}
				// Si está vacía se cierra la pantalla
				if (listModel.isEmpty()) {
					SwingUtil.showMessage("No tienes ningún artículo pendiente de gestionar", "Información",
							JOptionPane.INFORMATION_MESSAGE);
					view.getFrame().dispose();
				}
			}
		});
	}

	public void initView() {
		view.getFrame().setVisible(true);
		view.getListColaboradores().setModel(listModel);
	}

	public boolean obtenerColaboradores() {
		colaboradores = model.obtenerColaboradores2(nombre.get(0).getNombre());
		List<GestionarSolicitudesColaboracionDTO> listaDTO = new ArrayList<>();
		for (GestionarSolicitudesColaboracionDTO colaborador : colaboradores) {
			GestionarSolicitudesColaboracionDTO dto = new GestionarSolicitudesColaboracionDTO(
					colaborador.getIdArticulo(), colaborador.getTitulo(), colaborador.getNombreTrack(),
					colaborador.getPalabrasClaveTrack(), colaborador.getNombreColaborador());
			if (dto.getNombreColaborador().equals(nombre.get(0).getNombre())) {
				listaDTO.add(dto);
			}

		}
		listModel = new DefaultListModel<>();
		for (GestionarSolicitudesColaboracionDTO dto : listaDTO) {
			listModel.addElement(dto);
		}

		if (colaboradores.isEmpty()) {
			SwingUtil.showMessage("No tienes ningún artículo pendiente de registrar", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;
	}
}
