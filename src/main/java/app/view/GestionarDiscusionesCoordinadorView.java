package app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import app.dto.ArticuloDiscusionDTO;
import app.enums.DecisionRevisor;

public class GestionarDiscusionesCoordinadorView {

	// -----------------------------------------------------
	// Atributos de la vista
	// -----------------------------------------------------
	private JFrame frame; // Marco principal
	private JPanel contentPane;
	private JList<ArticuloDiscusionDTO> listArticulos;
	private JLabel lblValoracionGlobal;
	private JPanel panelRevisiones;
	private JScrollPane scrollRevisiones;
	private JButton btnPonerEnDiscusion;
	private JButton btnCerrar;
	private JButton btnAceptarArticulo;
	private JButton btnRechazarArticulo;
	private JButton btnRecordatorio;
	private JButton btnCerrarDiscusion;
	private JComboBox<String> comboFiltro;
	private JButton btnAccederDiscusion;

	// -----------------------------------------------------
	// Constructor
	// -----------------------------------------------------
	public GestionarDiscusionesCoordinadorView() {
		initialize();
	}

	// -----------------------------------------------------
	// Inicializar la interfaz gráfica
	// -----------------------------------------------------
	private void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setTitle("Gestión de Discusiones de Artículos");
		frame.setBounds(100, 100, 900, 500);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		frame.setContentPane(contentPane);

		// Panel Izquierdo: Lista de artículos
		JPanel panelArticulos = new JPanel(new BorderLayout(5, 5));
		panelArticulos.setBorder(BorderFactory.createTitledBorder("Artículos aptos para discusión"));

		comboFiltro = new JComboBox<>();
		comboFiltro.addItem("Aptas para discusión");
		comboFiltro.addItem("Abiertas");
		comboFiltro.addItem("Cerradas");
		comboFiltro.addItem("Abiertas firmes");
		comboFiltro.addItem("Abiertas c/ deadline pasado");
		comboFiltro.addItem("Abiertas sin anotaciones");
		panelArticulos.add(comboFiltro, BorderLayout.NORTH);

		listArticulos = new JList<>();
		listArticulos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollArticulos = new JScrollPane(listArticulos);
		panelArticulos.add(scrollArticulos, BorderLayout.CENTER);
		panelArticulos.setPreferredSize(new Dimension(250, 0));
		contentPane.add(panelArticulos, BorderLayout.WEST);

		// Panel Derecho: Detalles del artículo
		JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
		contentPane.add(panelDerecho, BorderLayout.CENTER);

		// Panel superior combinado: Valoración + Botones de acción
		JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));

		// Panel Valoración Global
		JPanel panelValoracion = new JPanel();
		panelValoracion.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelValoracion.setBorder(BorderFactory.createTitledBorder("Valoración Global"));
		panelValoracion.setPreferredSize(new Dimension(200, 60));

		lblValoracionGlobal = new JLabel("0");
		lblValoracionGlobal.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblValoracionGlobal.setHorizontalAlignment(SwingConstants.CENTER);
		panelValoracion.add(lblValoracionGlobal);

		// Panel de acción (aceptar/rechazar)
		JPanel panelAccion = new JPanel();
		panelAccion.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		panelAccion.setBorder(BorderFactory.createTitledBorder("Acción sobre el artículo"));
		panelAccion.setPreferredSize(new Dimension(300, 60));

		btnAceptarArticulo = new JButton("Aceptar artículo");
		btnRechazarArticulo = new JButton("Rechazar artículo");
		panelAccion.add(btnAceptarArticulo);
		panelAccion.add(btnRechazarArticulo);

		panelSuperior.add(panelValoracion);
		panelSuperior.add(panelAccion);
		panelDerecho.add(panelSuperior, BorderLayout.NORTH);

		// Panel central: revisiones conflictivas
		panelRevisiones = new JPanel();
		panelRevisiones.setLayout(new BoxLayout(panelRevisiones, BoxLayout.Y_AXIS));

		scrollRevisiones = new JScrollPane(panelRevisiones);
		scrollRevisiones.setBorder(BorderFactory.createTitledBorder("Revisiones"));
		scrollRevisiones.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollRevisiones.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panelDerecho.add(scrollRevisiones, BorderLayout.CENTER);

		// Panel inferior: botones
		JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
		btnRecordatorio = new JButton("Enviar recordatorio");
		btnCerrarDiscusion = new JButton("Cerrar discusión");
		btnPonerEnDiscusion = new JButton("Poner en discusión");
		btnCerrar = new JButton("Cerrar");
		btnAccederDiscusion = new JButton("Acceder Discusión");
		
		panelInferior.add(btnAccederDiscusion);
		panelInferior.add(btnRecordatorio);
		panelInferior.add(btnCerrarDiscusion);
		panelInferior.add(btnPonerEnDiscusion);
		panelInferior.add(btnCerrar);
		panelDerecho.add(panelInferior, BorderLayout.SOUTH);
	}

	// -----------------------------------------------------
	// Métodos de la "lógica de UI" que el controlador puede invocar
	// -----------------------------------------------------

	public void showWindow() {
		this.frame.setVisible(true);
	}

	public void hideWindow() {
		this.frame.setVisible(false);
	}

	public void clearRevisiones() {
		panelRevisiones.removeAll();
		panelRevisiones.revalidate();
		scrollRevisiones.getVerticalScrollBar().setValue(0);
	}

	public void addRevisionCard(String revisor, String nivel, String decision, String comentario) {
		JPanel tarjetaRevision = new JPanel(new BorderLayout(5, 5));
		tarjetaRevision.setBorder(BorderFactory.createCompoundBorder(
				new EmptyBorder(5, 5, 5, 5),
				BorderFactory.createCompoundBorder(
						new LineBorder(new Color(200, 200, 200), 1, true),
						new EmptyBorder(10, 10, 10, 10))));
		tarjetaRevision.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
		tarjetaRevision.setBackground(new Color(250, 250, 250));

		DecisionRevisor decisionRevisor = DecisionRevisor.fromLabel(decision);

		String encabezadoHtml = String.format("<html><body>"
				+ "<b>%s</b> | <b>Nivel %s</b> | <b>%s</b>"
				+ "</body></html>", revisor, nivel, decision);

		JLabel lblEncabezado = new JLabel(encabezadoHtml);
		lblEncabezado.setFont(new Font("SansSerif", Font.PLAIN, 12));

		if (decisionRevisor != null) {
			lblEncabezado.setForeground(decisionRevisor.getColor());
		}

		JPanel panelEncabezado = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelEncabezado.setOpaque(false);
		panelEncabezado.add(lblEncabezado);
		tarjetaRevision.add(panelEncabezado, BorderLayout.NORTH);

		JSeparator separador = new JSeparator();
		separador.setForeground(new Color(220, 220, 220));
		tarjetaRevision.add(separador, BorderLayout.CENTER);

		JPanel panelComentario = new JPanel(new BorderLayout(5, 5));
		panelComentario.setOpaque(false);

		JLabel lblComentarioTitulo = new JLabel("Comentario:");
		lblComentarioTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));
		JLabel lblComentarioValor = new JLabel("<html><p style='width:400px'>" + comentario + "</p></html>");

		panelComentario.add(lblComentarioTitulo, BorderLayout.NORTH);
		panelComentario.add(lblComentarioValor, BorderLayout.CENTER);

		tarjetaRevision.add(panelComentario, BorderLayout.SOUTH);
		panelRevisiones.add(tarjetaRevision);
		panelRevisiones.add(Box.createRigidArea(new Dimension(0, 10)));

		panelRevisiones.revalidate();
		scrollRevisiones.getVerticalScrollBar().setValue(0);
	}

	// -----------------------------------------------------
	// Getters y Setters para que el controlador acceda a los componentes
	// -----------------------------------------------------

	public JFrame getFrame() {
		return frame;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public JList<ArticuloDiscusionDTO> getListArticulos() {
		return listArticulos;
	}

	public JLabel getLblValoracionGlobal() {
		return lblValoracionGlobal;
	}

	public JPanel getPanelRevisiones() {
		return panelRevisiones;
	}

	public JScrollPane getScrollRevisiones() {
		return scrollRevisiones;
	}

	public JButton getBtnPonerEnDiscusion() {
		return btnPonerEnDiscusion;
	}

	public JButton getBtnCerrar() {
		return btnCerrar;
	}

	public JButton getBtnAceptarArticulo() {
		return btnAceptarArticulo;
	}

	public JButton getBtnRechazarArticulo() {
		return btnRechazarArticulo;
	}

	public JButton getBtnRecordatorio() {
		return btnRecordatorio;
	}

	public JButton getBtnCerrarDiscusion() {
		return btnCerrarDiscusion;
	}

	public JComboBox<String> getComboFiltro() {
		return comboFiltro;
	}
	
	public JButton getBtnAccederDiscusion() {
		return btnAccederDiscusion;
	}
	
}
