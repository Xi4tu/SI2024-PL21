package app.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import app.controller.AceptarDenegarArticuloController;
import app.controller.EnviarArticuloController;
import app.controller.RevisionArticuloAutorController;
import app.controller.RevisionArticuloRevisorController;
import app.controller.VerMisArticulosController;
import app.model.AceptarDenegarArticuloModel;
import app.model.EnviarArticuloModel;
import app.model.RevisionArticuloAutorModel;
import app.model.RevisionArticuloRevisorModel;
import app.model.VerMisArticulosModel;
import giis.demo.util.Database;

public class AppMain {

	private JFrame frame;
	private JPanel contentPane;
	private JTextField textEmail;

	/**
	 * Lanzar la aplicación.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppMain window = new AppMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Crear la aplicación.
	 */
	public AppMain() {
		initialize();
	}

	/**
	 * Inicializar los elementos del frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setTitle("Gestor de conferencias");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 400, 600);
		

		EnviarArticuloModel model = new EnviarArticuloModel();

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		frame.setContentPane(contentPane);

		// Panel superior para el email
		JPanel panelEmail = new JPanel(new BorderLayout(5, 5));
		JLabel lblEmail = new JLabel("Email:");
		textEmail = new JTextField();
		panelEmail.add(lblEmail, BorderLayout.WEST);
		panelEmail.add(textEmail, BorderLayout.CENTER);
		contentPane.add(panelEmail, BorderLayout.NORTH);

		// Panel central para los botones (alineados verticalmente)
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));

		// Crear lista de definiciones de botones
		List<ButtonProvider> buttons = new ArrayList<>();
		buttons.add(new ButtonProvider("Iniciar BBDD en blanco", e -> {
			Database db = new Database();
			db.createDatabase(false);
		}));
		buttons.add(new ButtonProvider("Cargar datos en BBDD", e -> {
			Database db = new Database();
			db.createDatabase(false);
			db.loadDatabase();
		}));
		buttons.add(new ButtonProvider("Enviar artículo conferencia - Autor", e -> {
			EnviarArticuloController controller = new EnviarArticuloController(new EnviarArticuloModel(),
					new EnviarArticuloView(), textEmail.getText());
			controller.initController();
		}));
		buttons.add(new ButtonProvider("Revisar artículos asignados para revisión - Revisor", e -> {
			RevisionArticuloRevisorController controller = new RevisionArticuloRevisorController(
					new RevisionArticuloRevisorModel(), new RevisionArticuloRevisorView(), textEmail.getText());
			controller.initController();
		}));
		buttons.add(new ButtonProvider("Asignar revisores a artículo - Coordinador",
				e -> System.out.println("Acción de Funcionalidad 5")));

		buttons.add(new ButtonProvider("Aceptar o denegar artículos - Coordinador", e -> {
        	AceptarDenegarArticuloController controller = new AceptarDenegarArticuloController(
        			new AceptarDenegarArticuloModel(), new AceptarDenegarArticuloView(), textEmail.getText());
        	controller.initController();
        }));
		buttons.add(new ButtonProvider("Ver revisión de mis articulos - Autor", e -> {
			RevisionArticuloAutorController controller = new RevisionArticuloAutorController(
					new RevisionArticuloAutorModel(), new RevisionArticuloAutorView(), textEmail.getText());
			controller.initController();
		}));
		buttons.add(new ButtonProvider("Visualizar mis artículos - Autor", e -> {
			VerMisArticulosController controller = new VerMisArticulosController(new VerMisArticulosModel(), 
					new VerMisArticulosView(), textEmail.getText());
			controller.initController();
		}));

		// Agregar botones al panel
		for (ButtonProvider bd : buttons) {
			panelBotones.add(bd.createButton());
			panelBotones.add(Box.createVerticalStrut(5));
		}

		contentPane.add(panelBotones, BorderLayout.CENTER);
	}

}