package app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import app.dto.AccederDiscusionDTO;
import app.enums.DecisionRevisor;

/**
 * Vista para la participación del coordinador en discusiones de artículos.
 * <p>
 * Permite seleccionar un artículo en discusión y:
 * <ul>
 * <li>Modificar la decisión de un revisor elegido (comboRevisor +
 * comboDecision).</li>
 * <li>Ver las anotaciones existentes en orden cronológico.</li>
 * <li>Agregar nuevas anotaciones.</li>
 * </ul>
 * </p>
 */
public class ParticiparDiscusionesCoordView {

	// -----------------------------------------------------
	// Atributos de la vista (Componentes de UI)
	// -----------------------------------------------------
	private JFrame frame; // Marco principal
	private JPanel contentPane; // Panel contenedor principal

	// Lista de artículos en discusión
	private JList<AccederDiscusionDTO> listArticulos;

	// Panel donde se muestran las anotaciones (en orden cronológico)
	private JPanel panelAnotaciones;

	// Campo para añadir nueva anotación
	private JTextArea textNuevaAnotacion;

	// ComboBox para elegir al revisor
	private JComboBox<String> comboRevisor;

	// ComboBox para cambiar la decisión del revisor seleccionado
	private JComboBox<DecisionRevisor> comboDecision;

	// Botones
	private JButton btnAgregarNota;
	private JButton btnModificarDecision;

	// -----------------------------------------------------
	// Constructor
	// -----------------------------------------------------
	public ParticiparDiscusionesCoordView() {
		initialize();
	}

	/**
	 * Inicializa la interfaz gráfica.
	 * <p>
	 * Adapta el rectángulo superior para que el coordinador pueda:
	 * <ul>
	 * <li>Seleccionar un revisor en el comboRevisor</li>
	 * <li>Elegir una nueva decisión en comboDecision</li>
	 * <li>Pulsar "Modificar" para aplicar el cambio</li>
	 * </ul>
	 * El resto de la vista permanece igual, con la lista de artículos a la
	 * izquierda y la sección de anotaciones en la parte central/derecha.
	 * </p>
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setTitle("Discusión de Artículos - Coordinador");
		frame.setBounds(100, 100, 900, 500);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		frame.setContentPane(contentPane);

		// -------------------------------------------------------
		// Panel Izquierdo: Lista de artículos en discusión
		// -------------------------------------------------------
		JPanel panelIzquierdo = new JPanel(new BorderLayout());
		panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Artículos en Discusión"));
		listArticulos = new JList<>();
		listArticulos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollArticulos = new JScrollPane(listArticulos);
		panelIzquierdo.add(scrollArticulos, BorderLayout.CENTER);
		panelIzquierdo.setPreferredSize(new Dimension(250, 0));
		contentPane.add(panelIzquierdo, BorderLayout.WEST);

		// -------------------------------------------------------
		// Panel Derecho: Contenido principal (decisiones, anotaciones)
		// -------------------------------------------------------
		JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
		contentPane.add(panelDerecho, BorderLayout.CENTER);

		// -------------------------------------------------------
		// Panel Superior: Cambiar decisión de un revisor
		// -------------------------------------------------------
		JPanel panelCambiarDecision = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		panelCambiarDecision.setBorder(BorderFactory.createTitledBorder("Cambiar decisión de revisor"));

		// Etiqueta y ComboBox de Revisor
		JLabel lblRevisor = new JLabel("Revisor:");
		lblRevisor.setFont(new Font("SansSerif", Font.BOLD, 12));
		panelCambiarDecision.add(lblRevisor);

		comboRevisor = new JComboBox<>();
		// Se deja vacío inicialmente; el controlador lo llenará según corresponda
		panelCambiarDecision.add(comboRevisor);

		// Etiqueta y ComboBox de Decisión
		JLabel lblDecision = new JLabel("Decisión:");
		lblDecision.setFont(new Font("SansSerif", Font.BOLD, 12));
		panelCambiarDecision.add(lblDecision);

		comboDecision = new JComboBox<>();
		comboDecision.setModel(new DefaultComboBoxModel<>(DecisionRevisor.values()));
		comboDecision.setSelectedIndex(-1); // sin selección por defecto
		panelCambiarDecision.add(comboDecision);

		// Botón "Modificar" para cambiar la decisión del revisor
		btnModificarDecision = new JButton("Modificar");
		panelCambiarDecision.add(btnModificarDecision);

		// Se añade el panel superior al panel derecho
		panelDerecho.add(panelCambiarDecision, BorderLayout.NORTH);

		// -------------------------------------------------------
		// Panel Central: Anotaciones + campo para nueva anotación
		// -------------------------------------------------------
		JPanel panelCentral = new JPanel(new BorderLayout(5, 5));

		// Panel con anotaciones (orden cronológico)
		panelAnotaciones = new JPanel();
		panelAnotaciones.setLayout(new BoxLayout(panelAnotaciones, BoxLayout.Y_AXIS));
		JScrollPane scrollAnotaciones = new JScrollPane(panelAnotaciones);
		scrollAnotaciones.setBorder(BorderFactory.createTitledBorder("Anotaciones (Orden Cronológico)"));
		panelCentral.add(scrollAnotaciones, BorderLayout.CENTER);

		// Panel para añadir nueva anotación
		JPanel panelNuevaAnotacion = new JPanel(new BorderLayout(5, 5));
		panelNuevaAnotacion.setBorder(BorderFactory.createTitledBorder("Añadir nueva anotación"));

		textNuevaAnotacion = new JTextArea(3, 30);
		textNuevaAnotacion.setWrapStyleWord(true);
		textNuevaAnotacion.setLineWrap(true);
		JScrollPane scrollNuevaAnotacion = new JScrollPane(textNuevaAnotacion);
		panelNuevaAnotacion.add(scrollNuevaAnotacion, BorderLayout.CENTER);

		btnAgregarNota = new JButton("Agregar Nota");
		panelNuevaAnotacion.add(btnAgregarNota, BorderLayout.SOUTH);

		panelCentral.add(panelNuevaAnotacion, BorderLayout.SOUTH);
		panelDerecho.add(panelCentral, BorderLayout.CENTER);
	}

	// -----------------------------------------------------
	// Métodos auxiliares para la actualización de la UI (para uso del controlador)
	// -----------------------------------------------------

	/**
	 * Agrega una "tarjeta" de anotación al panel de anotaciones.
	 * 
	 * @param emailRevisor Email o nombre del autor de la anotación (p.ej.
	 *                     "dra.marta@x.com").
	 * @param fecha        Fecha de la anotación (p.ej., "2026-03-11").
	 * @param hora         Hora de la anotación (p.ej., "10:30").
	 * @param contenido    Contenido de la anotación.
	 */
	public void addAnnotationCard(String emailRevisor, String fecha, String hora, String contenido) {
		String encabezado = emailRevisor + " (" + fecha + " - " + hora + ")";
		JPanel tarjeta = new JPanel(new BorderLayout(5, 5));
		tarjeta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		JLabel lblAutor = new JLabel(encabezado);
		lblAutor.setFont(new Font("SansSerif", Font.BOLD, 12));
		tarjeta.add(lblAutor, BorderLayout.NORTH);

		JLabel lblContenido = new JLabel("<html><p style='width:400px'>" + contenido + "</p></html>");
		tarjeta.add(lblContenido, BorderLayout.CENTER);

		panelAnotaciones.add(tarjeta);
		panelAnotaciones.add(Box.createVerticalStrut(10));
		panelAnotaciones.revalidate();
		panelAnotaciones.repaint();
	}

	/**
	 * Limpia todas las anotaciones del panel.
	 */
	public void clearAnnotations() {
		panelAnotaciones.removeAll();
		panelAnotaciones.revalidate();
		panelAnotaciones.repaint();
	}

	// -----------------------------------------------------
	// Getters para que el controlador acceda a los componentes
	// -----------------------------------------------------

	/**
	 * @return El frame principal de la vista.
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * @return El panel contenedor principal.
	 */
	public JPanel getContentPane() {
		return contentPane;
	}

	/**
	 * @return La lista de artículos en discusión que se muestra a la izquierda.
	 */
	public JList<AccederDiscusionDTO> getListArticulos() {
		return listArticulos;
	}

	/**
	 * @return El área de texto para introducir una nueva anotación.
	 */
	public JTextArea getTextNuevaAnotacion() {
		return textNuevaAnotacion;
	}

	/**
	 * @return El comboBox en el que el coordinador elige el revisor.
	 */
	public JComboBox<String> getComboRevisor() {
		return comboRevisor;
	}

	/**
	 * @return El comboBox en el que el coordinador elige la nueva decisión para el
	 *         revisor seleccionado.
	 */
	public JComboBox<DecisionRevisor> getComboDecision() {
		return comboDecision;
	}

	/**
	 * @return El botón para agregar una nueva anotación.
	 */
	public JButton getBtnAgregarNota() {
		return btnAgregarNota;
	}

	/**
	 * @return El botón para modificar (actualizar) la decisión del revisor
	 *         seleccionado.
	 */
	public JButton getBtnModificarDecision() {
		return btnModificarDecision;
	}
}
