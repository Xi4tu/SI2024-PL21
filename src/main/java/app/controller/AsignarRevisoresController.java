package app.controller;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import app.dto.ArticuloRevisionDTO;
import app.dto.AutorDTO;
import app.dto.RevisorDTO;
import app.enums.Rol;
import app.model.AsignarRevisoresModel;
import app.util.UserUtil;
import app.view.AsignarRevisoresView;
import giis.demo.util.SwingUtil;

public class AsignarRevisoresController {
	private AsignarRevisoresModel model;
	private AsignarRevisoresView view;
	private List<ArticuloRevisionDTO> articulos;
	private List<RevisorDTO> revisores;
	private boolean controllerIniciado;
	private static final Rol ROL = Rol.COORDINADOR;

	public AsignarRevisoresController(AsignarRevisoresModel model, AsignarRevisoresView view, String email) {
	
		;
		this.model = model;
		this.view = view;

		
		if (!UserUtil.checkEmail(email, ROL.getNombre(), model.getDbUtil())) {
			// Detener la inicialización si el email es inválido.
			return;
		}
			
		// Obtener los artículos sin revisores y cargar la vista
		if (!obtenerArticulosSinRevisores(true)) {
			return;
		}

		this.initView();
	    if (!controllerIniciado) {
	        this.initController();
	    }
	}

	// Método para inicializar la vista
	public void initView() {
		view.getFrame().setVisible(true);
	}

	// Método para inicializar los controladores de eventos
	public void initController() {
		
		if (controllerIniciado) return; // Si ya se ejecutó, no lo hace de nuevo
		controllerIniciado = true;
	
		System.out.println("initController ejecutado");
		
		
		view.getBtnAsignarRevisor().addActionListener(e -> SwingUtil.exceptionWrapper(() -> asignarRevisor()));

		//Evento que elimina un revisor de la lista de revisores asignados
		view.getBtnEliminar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> eliminarRevisor()));
		
		//Evento cuando se selecciona un ComboBox 
		view.getComboBoxAticulosAsignadosoNo().addItemListener(e -> {
			if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
				actualizarCombobox();
			}else limpiarCampos();
		});
		// Evento cuando se selecciona un artículo en el ComboBox
		view.getComboArticulosNoAsignados().addItemListener(e -> {
			if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
				actualizarDetallesArticulo();
			} else limpiarCampos();
		});
	}
	
	// Método para actualizar el ComboBox 
	private void actualizarCombobox() {
		// Obtener el índice seleccionado en el ComboBox
		int selectedIndex = view.getComboBoxAticulosAsignadosoNo().getSelectedIndex();

		// Limpiar el ComboBox antes de llenarlo con nuevos datos
		view.getComboArticulosNoAsignados().removeAllItems();

		if (selectedIndex == 0) { // Artículos con revisores (Opción 1)
			// Llamar al método para obtener artículos con revisores
			obtenerArticulosSinRevisores(true);
		} else if (selectedIndex == 1) { // Artículos sin revisores (Opción 2)
			// Llamar al método para obtener artículos sin revisores
			obtenerArticulosSinRevisores(false);
		}
	}


	// Método para actualizar palabras clave y resumen cuando cambia la selección
	private void actualizarDetallesArticulo() {
		int selectedIndex = view.getComboArticulosNoAsignados().getSelectedIndex();

		if (selectedIndex != -1) {
			ArticuloRevisionDTO articuloSeleccionado = articulos.get(selectedIndex);
			obtenerRevisoresDisponibles(articuloSeleccionado.getId());
			// Limpiar la tabla de autores antes de agregar los nuevos autores
	        DefaultTableModel modelAutores = (DefaultTableModel) view.getTableAutores().getModel();
	        modelAutores.setRowCount(0); // Limpiar tabla de autores

	        // Agregar los autores del artículo seleccionado
	        for (AutorDTO autor : articuloSeleccionado.getAutores()) {
	            modelAutores.addRow(new Object[] { autor.getEmail(), autor.getNombre(), autor.getOrganizacion(), autor.getGrupoInvestigacion() });
	        }

			// Actualizar palabras clave y resumen en la vista
			view.getTxtPalabraClave().setText(articuloSeleccionado.getPalabrasClave());
			view.getTxtResumen().setText(articuloSeleccionado.getResumen());
			//Actualizar revisores seleccionados
			actualizarRevisoresSeleccionados(articuloSeleccionado.getId());
			
			
		}
	}

	
	
	// Método que obtiene los artículos sin revisores y los carga en la vista
	private boolean obtenerArticulosSinRevisores(boolean verRevisiones) {
		if (!verRevisiones)
			articulos = model.obtenerArticulosSinRevisores();
		else
			articulos = model.obtenerArticulosConRevisores();

		// Limpiar ComboBox antes de llenarlo
		view.getComboArticulosNoAsignados().removeAllItems();

	    // Cargar artículos con ID y palabras clave en el ComboBox
	    for (ArticuloRevisionDTO articulo : articulos) {
	        String textoCombo = "ID: " + articulo.getId() + " - " + articulo.getTitulo() + " ("
	                + articulo.getPalabrasClave() + ")";
	        view.getComboArticulosNoAsignados().addItem(textoCombo);
	    }

	    

	    // Actualizar los detalles del primer artículo automáticamente
	    actualizarDetallesArticulo();
	    
	    return true;
	}


	private boolean obtenerRevisoresDisponibles(int id) {
		revisores = model.obtenerRevisoresDisponibles(id);

		// Cargar revisores en la tabla
		DefaultTableModel modelRevisores = (DefaultTableModel) view.getTableRevisoresDisponibles().getModel();
		modelRevisores.setRowCount(0); // Limpiar la tabla antes de llenarla
		for (RevisorDTO revisor : revisores) {
			modelRevisores.addRow(new Object[] { revisor.getEmail(), revisor.getNombre(), revisor.getOrganizacion(), revisor.getGrupoInvestigacion() });
		}

		return true;
	}

	// Método para asignar un revisor al artículo seleccionado
	private void asignarRevisor() {
		int selectedIndex = view.getComboArticulosNoAsignados().getSelectedIndex();

		if (selectedIndex == -1) {
			SwingUtil.showMessage("Seleccione un artículo para asignar un revisor.", "INFORMACIÓN", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// Obtener ID del artículo seleccionado
		int idArticulo = articulos.get(selectedIndex).getId();
		
		 // Obtener los revisores ya asignados
	    List<RevisorDTO> revisoresAsignados = model.obtenerRevisoresDeArticulo(idArticulo);

	    // Verificar si ya hay 3 revisores asignados
	    if (revisoresAsignados.size() >= 3) {
	        SwingUtil.showMessage("No se pueden asignar más de 3 revisores a un artículo.", "Límite alcanzado", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

		// Obtener el email del revisor seleccionado en la tabla
		int selectedRow = view.getTableRevisoresDisponibles().getSelectedRow();

		if (selectedRow == -1) {
			SwingUtil.showMessage("Seleccione un revisor disponible.", "INFORMACIÓN", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String emailRevisor = (String) view.getTableRevisoresDisponibles().getValueAt(selectedRow, 0);

		// Asignar el revisor al artículo
		model.asignarRevisor(idArticulo, emailRevisor);
		
		// Actualizar la lista de revisores seleccionados en la tabla
	    actualizarRevisoresSeleccionados(idArticulo);
	    
	    
	    obtenerRevisoresDisponibles(idArticulo);
	    
		SwingUtil.showMessage("Revisor asignado con éxito.", "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE);

	}
	
	
	// Método para eliminar un revisor de un artículo
	private void eliminarRevisor() {
		int selectedIndex = view.getComboArticulosNoAsignados().getSelectedIndex();

		if (selectedIndex == -1) {
			SwingUtil.showMessage("Seleccione un artículo para eliminar un revisor.", "INFORMACIÓN", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Obtener ID del artículo seleccionado
		int idArticulo = articulos.get(selectedIndex).getId();

		// Obtener el email del revisor seleccionado en la tabla
		int selectedRow = view.getTableRevisoresSeleccionados().getSelectedRow();

		if (selectedRow == -1) {
			SwingUtil.showMessage("Seleccione un revisor asignado.", "INFORMACIÓN", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String emailRevisor = (String) view.getTableRevisoresSeleccionados().getValueAt(selectedRow, 0);

		// Eliminar el revisor del artículo
		model.eliminarRevisor(idArticulo, emailRevisor);
		
		// Actualizar la lista de revisores seleccionados en la tabla
	    actualizarRevisoresSeleccionados(idArticulo);
	    
	    obtenerRevisoresDisponibles(idArticulo);
	    
		SwingUtil.showMessage("Revisor eliminado con éxito.", "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE);
	}
	
	// Método para actualizar la tabla de revisores seleccionados
	private void actualizarRevisoresSeleccionados(int idArticulo) {
	    // Suponiendo que tienes un modelo en la vista que puede obtener los revisores asignados al artículo
	    List<RevisorDTO> revisores = model.obtenerRevisoresDeArticulo(idArticulo);

	    // Crear un modelo de tabla actualizado
	    DefaultTableModel tableModel = (DefaultTableModel) view.getTableRevisoresSeleccionados().getModel();

	    // Limpiar la tabla actual
	    tableModel.setRowCount(0);

	    // Verificar si la lista de revisores está vacía
	  
	    // Llenar la tabla con los nuevos revisores
	    for (RevisorDTO revisor : revisores) {
	
	        Object[] row = { revisor.getEmail(), revisor.getNombre(), revisor.getOrganizacion(), revisor.getGrupoInvestigacion() };
	        tableModel.addRow(row);
	    }
	    
	    
	}
	
	//funcion para limpiar los campos de la vista
	public void limpiarCampos() {
		view.getTxtPalabraClave().setText("");
		view.getTxtResumen().setText("");
		DefaultTableModel modelAutores = (DefaultTableModel) view.getTableAutores().getModel();
		modelAutores.setRowCount(0);
		DefaultTableModel modelRevisores = (DefaultTableModel) view.getTableRevisoresDisponibles().getModel();
		modelRevisores.setRowCount(0);
		DefaultTableModel modelRevisoresSeleccionados = (DefaultTableModel) view.getTableRevisoresSeleccionados().getModel();
		modelRevisoresSeleccionados.setRowCount(0);
	}

}
