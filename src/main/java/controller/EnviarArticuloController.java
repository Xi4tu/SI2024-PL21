package controller;

import model.EnviarArticuloModel;
import app.view.EnviarArticuloView;

public class EnviarArticuloController {

	
	private EnviarArticuloModel model;
	private EnviarArticuloView view;
	private String email;
	
	public EnviarArticuloController(EnviarArticuloModel m, EnviarArticuloView v,
		String email) {
			this.model = m;
			this.view = v;
			this.email = email;
		}

	
	public void initController() {
		//view.getAceptar().addActionListener(e -> view.getFrame().setVisible(false));
		view.getEnviar().addActionListener(e -> view.getFrame().setVisible(false));
		
		
	}

	public void initView() {
		view.getFrame().setVisible(true);
	}
	
	
}
