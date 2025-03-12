package app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import app.dto.RevisionArticuloAutorDTO;

import javax.swing.BorderFactory;

public class GestionarDiscusionesCoordinadorView {
	
	// -----------------------------------------------------
	// Atributos de la vista
	// -----------------------------------------------------
	private JFrame frame;                       // Marco principal

	// Panel contenedor principal
	private JPanel contentPane;

	// Lista de artículos controversiales
	private JList<RevisionArticuloAutorDTO> listArticulos;

	// Etiqueta para mostrar la valoración global
	private JLabel lblValoracionGlobal;

	// Panel que contendrá las "tarjetas" de revisiones conflictivas
	private JPanel panelRevisiones;
	private JScrollPane scrollRevisiones;

	// Botones
	private JButton btnPonerEnDiscusion;
	private JButton btnCerrar;

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

		// -----------------------------------------------------
		// Panel Izquierdo: Lista de artículos
		// -----------------------------------------------------
		JPanel panelArticulos = new JPanel(new BorderLayout());
		panelArticulos.setBorder(BorderFactory.createTitledBorder("Artículos aptos para discusión"));

		listArticulos = new JList<>();
		listArticulos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollArticulos = new JScrollPane(listArticulos);
		panelArticulos.add(scrollArticulos, BorderLayout.CENTER);
		panelArticulos.setPreferredSize(new Dimension(250, 0));

		contentPane.add(panelArticulos, BorderLayout.WEST);

		// -----------------------------------------------------
		// Panel Derecho: Detalles del artículo
		// -----------------------------------------------------
		JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
		contentPane.add(panelDerecho, BorderLayout.CENTER);

		// Panel superior: valoración global
		JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelSuperior.setBorder(BorderFactory.createTitledBorder("Valoración Global"));

		lblValoracionGlobal = new JLabel("");
		lblValoracionGlobal.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblValoracionGlobal.setHorizontalAlignment(SwingConstants.CENTER);

		panelSuperior.add(lblValoracionGlobal);
		panelDerecho.add(panelSuperior, BorderLayout.NORTH);

		// Panel central: revisiones conflictivas
		panelRevisiones = new JPanel();
		panelRevisiones.setLayout(new BoxLayout(panelRevisiones, BoxLayout.Y_AXIS));

		scrollRevisiones = new JScrollPane(panelRevisiones);
		scrollRevisiones.setBorder(BorderFactory.createTitledBorder("Revisiones conflictivas"));
		scrollRevisiones.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollRevisiones.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		panelDerecho.add(scrollRevisiones, BorderLayout.CENTER);

		// Panel inferior: botones
		JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnPonerEnDiscusion = new JButton("Poner en Discusión");
		panelInferior.add(btnPonerEnDiscusion);

		btnCerrar = new JButton("Cerrar");
		panelInferior.add(btnCerrar);

		panelDerecho.add(panelInferior, BorderLayout.SOUTH);

		// Seleccionar el primer artículo por defecto (ejemplo)
		if (listArticulos.getModel().getSize() > 0) {
			listArticulos.setSelectedIndex(0);
		}
	}

	// -----------------------------------------------------
	// Métodos de la "lógica de UI" que el controlador puede invocar
	// -----------------------------------------------------

	/**
	 * Muestra la ventana principal
	 */
	public void showWindow() {
		this.frame.setVisible(true);
	}

	/**
	 * Oculta o cierra la ventana
	 */
	public void hideWindow() {
		this.frame.setVisible(false);
	}

	/**
	 * Limpia el panel de revisiones
	 */
	public void clearRevisiones() {
		panelRevisiones.removeAll();
		panelRevisiones.revalidate();
		scrollRevisiones.getVerticalScrollBar().setValue(0);
	}

	/**
	 * Agrega una "tarjeta" de revisión al panel de revisiones.
	 * NOTA: Se eliminó la lógica de colorear la decisión.
	 */
	public void addRevisionCard(String revisor, String nivel, String decision, String comentario) {
		JPanel tarjetaRevision = new JPanel(new BorderLayout(5, 5));
		tarjetaRevision.setBorder(BorderFactory.createCompoundBorder(
			new EmptyBorder(5, 5, 5, 5),
			BorderFactory.createCompoundBorder(
				new LineBorder(new Color(200, 200, 200), 1, true),
				new EmptyBorder(10, 10, 10, 10)
			)
		));
		tarjetaRevision.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
		tarjetaRevision.setBackground(new Color(250, 250, 250));

		// Construimos encabezado con HTML, pero sin color.  
		String encabezadoHtml = String.format("<html><body>"
			+ "<b>%s</b> | <b>Nivel %s</b> | <b>%s</b>"
			+ "</body></html>", revisor, nivel, decision);

		JLabel lblEncabezado = new JLabel(encabezadoHtml);
		lblEncabezado.setFont(new Font("SansSerif", Font.PLAIN, 12));

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

	public JList<RevisionArticuloAutorDTO> getListArticulos() {
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

}
