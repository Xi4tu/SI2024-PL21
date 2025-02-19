package app.controller;

import javax.swing.JOptionPane;

import app.model.RevisionArticuloRevisorModel;
import app.view.RevisionArticuloRevisorView;
import giis.demo.util.SwingUtil;

public class RevisionArticuloRevisorController {
	
	private RevisionArticuloRevisorModel model;
	private RevisionArticuloRevisorView view;
	
	public RevisionArticuloRevisorController(RevisionArticuloRevisorModel m, RevisionArticuloRevisorView v) {
		this.model = m;
		this.view = v;
		this.initView();
	}
	
	public void initController() {
		view.getBtnEnviarRevision().addActionListener(e -> SwingUtil.exceptionWrapper(() -> enviarRevision()));
	}
	
	public void initView() {
		view.getFrame().setVisible(true);
	}
	

	private void enviarRevision() {
		if (validarDatos()) {
			// Mensaje de éxito y ocultar ventana
			model.insertarRevision(null, null, null, null, null);
			SwingUtil.showMessage("El periodo se ha creado exitosamente", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			view.getFrame().setVisible(false);
		}
	}

	private boolean validarDatos() {
		
		return false;
	}

}
