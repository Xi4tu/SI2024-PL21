package app.controller;

import app.model.AceptarDenegarArticulosModel;
import app.view.AceptarDenegarArticulosView;

public class AceptarDenegarArticulosController {
	
	private AceptarDenegarArticulosModel model;
	private AceptarDenegarArticulosView view;
	private String email;
	
	public AceptarDenegarArticulosController(AceptarDenegarArticulosModel m, AceptarDenegarArticulosView v,
		String email) {
			this.model = m;
			this.view = v;
			this.email = email;
		}

	
	public void initController() {
		//view.getAceptar().addActionListener(e -> view.getFrame().setVisible(false));
		view.getAceptar().addActionListener(e -> view.getFrame().setVisible(false));
		
	}

	public void initView() {
		view.getFrame().setVisible(true);
	}

}
