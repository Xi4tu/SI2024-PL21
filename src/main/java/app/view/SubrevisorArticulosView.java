package app.view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import app.dto.RevisionArticuloRevisorDTO;
import javax.swing.DefaultComboBoxModel;

public class SubrevisorArticulosView {
	// Crear las variables de la vista
	private JFrame frame;
	private JPanel contentPane;
	private JList<RevisionArticuloRevisorDTO> listArticulos;
	private JLabel lblFileName;
	private JTextArea txtComentariosAutores;
	private JTextArea txtComentariosCoordinadores;
	private JComboBox<String> comboNivelExperto;
	private JComboBox<String> comboDecision;
	private JComboBox<String> comboBoxPendientes;
	private JComboBox comboBoxRevisor;
	private JButton btnEnviarRevision;
	private JButton btnPedirColaborador;
	private JTextArea chatArea;
	private JButton btnEnviarChat;
	private JPanel panelInputChat;
	private JTextField inputChat;
	private JScrollPane scrollChat;
	private JPanel panelChat;
	private JPanel panelBoton;

	public SubrevisorArticulosView() {
		initialize();
	}
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Revisión de Artículo Asignado por Subrevisor");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setResizable(false);
		frame.setBounds(100, 100, 1000, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		frame.setContentPane(contentPane);
		// ---------------------------------------------------------------------
		// Panel Izquierdo: Lista de artículos asignados
		// ---------------------------------------------------------------------
		JPanel panelIzquierdo = new JPanel(new BorderLayout());
		panelIzquierdo.setBorder(new TitledBorder("Artículos Asignados"));
		listArticulos = new JList<>();
		listArticulos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollArticulos = new JScrollPane(listArticulos);
		panelIzquierdo.add(scrollArticulos, BorderLayout.CENTER);
		

		panelIzquierdo.setPreferredSize(new Dimension(250, 0));
		contentPane.add(panelIzquierdo, BorderLayout.WEST);
		// ---------------------------------------------------------------------
		// Panel Derecho: Formulario de revisión
		// ---------------------------------------------------------------------
		JPanel panelDerecho = new JPanel();
		panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
		panelDerecho.setBorder(new TitledBorder("Revisión del Artículo"));
		contentPane.add(panelDerecho, BorderLayout.CENTER);
		// --- Sección de descarga ---
		JPanel panelDownload = new JPanel(new FlowLayout(FlowLayout.LEFT));


		panelDerecho.add(panelDownload);
		panelDerecho.add(Box.createVerticalStrut(10));
		// --- Comentarios para los Autores ---
		JPanel panelComentariosAutores = new JPanel(new BorderLayout());
		panelComentariosAutores.setBorder(new TitledBorder("Comentarios para los Autores"));
		txtComentariosAutores = new JTextArea(5, 30);
		txtComentariosAutores.setLineWrap(true);
		txtComentariosAutores.setWrapStyleWord(true);
		JScrollPane scrollComentariosAutores = new JScrollPane(txtComentariosAutores);
		panelComentariosAutores.add(scrollComentariosAutores, BorderLayout.CENTER);
		panelDerecho.add(panelComentariosAutores);
		panelDerecho.add(Box.createVerticalStrut(10));
		// --- Comentarios para los Coordinadores ---
		JPanel panelComentariosCoord = new JPanel(new BorderLayout());
		panelComentariosCoord.setBorder(new TitledBorder("Comentarios para los Coordinadores"));
		txtComentariosCoordinadores = new JTextArea(5, 30);
		txtComentariosCoordinadores.setLineWrap(true);
		txtComentariosCoordinadores.setWrapStyleWord(true);
		JScrollPane scrollComentariosCoord = new JScrollPane(txtComentariosCoordinadores);
		panelComentariosCoord.add(scrollComentariosCoord, BorderLayout.CENTER);
		panelDerecho.add(panelComentariosCoord);
		panelDerecho.add(Box.createVerticalStrut(10));
		// --- Panel de Selección: Nivel de Experto y Decisión ---
		
		
		
		
		panelDerecho.add(Box.createVerticalStrut(10));

		// --- Botón para enviar la revisión ---
		panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		
		btnEnviarRevision = new JButton("Enviar Revisión");
		panelBoton.add(btnEnviarRevision);
		panelDerecho.add(panelBoton);
		
		// --- Panel de Chat ---
		panelChat = new JPanel(new BorderLayout());
		panelChat.setBorder(new TitledBorder("Chat con Coordinador"));

		chatArea = new JTextArea(5, 30);
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		scrollChat = new JScrollPane(chatArea);
		panelChat.add(scrollChat, BorderLayout.CENTER);

		inputChat = new JTextField();
		btnEnviarChat = new JButton("Enviar");

		panelInputChat = new JPanel(new BorderLayout());
		panelInputChat.add(inputChat, BorderLayout.CENTER);
		panelInputChat.add(btnEnviarChat, BorderLayout.EAST);

		panelChat.add(panelInputChat, BorderLayout.SOUTH);
		panelDerecho.add(panelChat);

	}

	// Getters y Setters
	public JFrame getFrame() {
		return this.frame;
	}
	public JList<RevisionArticuloRevisorDTO> getListArticulos() {
		return this.listArticulos;
	}
	public JLabel getLblFileName() {
		return this.lblFileName;
	}
	public JTextArea getTxtComentariosAutores() {
		return this.txtComentariosAutores;
	}
	public JTextArea getTxtComentariosCoordinadores() {
		return this.txtComentariosCoordinadores;
	}
	public JComboBox<String> getComboNivelExperto() {
		return this.comboNivelExperto;
	}
	public JComboBox<String> getComboDecision() {
		return this.comboDecision;
	}
	public JButton getBtnEnviarRevision() {
		return this.btnEnviarRevision;
	}
	
	public JButton getBtnPedirColaborador() {
		return this.btnPedirColaborador;
	}
	
	public JComboBox<String> getComboBoxPendientes() {
		return this.comboBoxPendientes;
	}
	
	public void setComboBoxPendientes(JComboBox<String> comboBoxPendientes) {
		this.comboBoxPendientes = comboBoxPendientes;
	}
	public JComboBox getComboBoxRevisor() {
		return comboBoxRevisor;
	}
	public void setComboBoxRevisor(JComboBox comboBoxRevisor) {
		this.comboBoxRevisor = comboBoxRevisor;
	}

	public JButton getBtnEnviarChat() {
		return btnEnviarChat;
	}
	
	public JTextArea getChatArea() {
		return chatArea;
	}
	
	public JTextField getInputChat() {
		return inputChat;
	}
}
