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

public class ParticiparDiscusionesCoordView {
	
	// -----------------------------------------------------
    // Atributos de la vista (Componentes de UI)
    // -----------------------------------------------------
    private JFrame frame;                       // Marco principal
    private JPanel contentPane;                 // Panel contenedor principal

    // Lista de artículos en discusión (vacía; el controlador llenará los datos)
    private JList<AccederDiscusionDTO> listArticulos;

    // Panel donde se muestran las anotaciones en orden cronológico
    private JPanel panelAnotaciones;

    // Campo para añadir nueva anotación
    private JTextArea textNuevaAnotacion;

    // ComboBox para cambiar la decisión (se llenará con los valores del enum)
    private JComboBox<DecisionRevisor> comboDecision;

    // Botones
    private JButton btnAgregarNota;
    private JButton btnMantenerFirme;

    // -----------------------------------------------------
    // Constructor
    // -----------------------------------------------------
    public ParticiparDiscusionesCoordView() {
        initialize();
    }

    // -----------------------------------------------------
    // Inicialización de la interfaz gráfica (solo presentación)
    // -----------------------------------------------------
    private void initialize() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setTitle("Discusión de Artículos - Revisor");
        frame.setBounds(100, 100, 900, 500);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        frame.setContentPane(contentPane);

        // -------------------------------------------------------
        // Panel Izquierdo: Lista de artículos en discusión
        // -------------------------------------------------------
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Mis Artículos en Discusión"));
        // La lista se inicializa vacía; el controlador la llenará con datos.
        listArticulos = new JList<>();
        listArticulos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollArticulos = new JScrollPane(listArticulos);
        panelIzquierdo.add(scrollArticulos, BorderLayout.CENTER);
        panelIzquierdo.setPreferredSize(new Dimension(250, 0));
        contentPane.add(panelIzquierdo, BorderLayout.WEST);

        // -------------------------------------------------------
        // Panel Derecho: Detalles de la discusión
        // -------------------------------------------------------
        JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
        contentPane.add(panelDerecho, BorderLayout.CENTER);

        // Panel Superior: Sección para la decisión del revisor
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Tu Decisión Actual"));

        JLabel lblDecision = new JLabel("Decisión:");
        lblDecision.setFont(new Font("SansSerif", Font.BOLD, 12));
        panelSuperior.add(lblDecision);

        // Inicializamos el ComboBox con los valores del enum DecisionRevisor
        comboDecision = new JComboBox<>();
        comboDecision.setModel(new DefaultComboBoxModel<>(DecisionRevisor.values()));
        // Se deja sin selección por defecto para que el controlador lo establezca
        comboDecision.setSelectedIndex(-1);
        panelSuperior.add(comboDecision);

        // Botón para "mantener firme" la decisión (el controlador asignará el listener)
        btnMantenerFirme = new JButton("Mantenerme Firme");
        panelSuperior.add(btnMantenerFirme);

        panelDerecho.add(panelSuperior, BorderLayout.NORTH);

        // Panel Central: Anotaciones y campo para nueva anotación
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));

        // Panel con anotaciones (en orden cronológico)
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

        // Nota: El controlador se encargará de habilitar o deshabilitar componentes según el deadline y estado del revisor.
    }

    // -----------------------------------------------------
    // Métodos auxiliares para la actualización de la UI (para uso del controlador)
    // -----------------------------------------------------

    /**
     * Agrega una "tarjeta" de anotación al panel de anotaciones.
     *
     * @param nombreRevisor Nombre del autor de la anotación (p.ej., "Dra. Marta Sanz").
     * @param fecha         Fecha de la anotación (p.ej., "14/05").
     * @param hora          Hora de la anotación (p.ej., "10:30am").
     * @param contenido     Contenido de la anotación.
     */
    public void addAnnotationCard(String emailRevisor, String fecha, String hora, String contenido) {
        // Construimos el texto para el encabezado:
        // Nombre seguido de la fecha y hora entre paréntesis.
        String encabezado = emailRevisor + " (" + fecha + " - " + hora + ")";

        JPanel tarjeta = new JPanel(new BorderLayout(5, 5));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel lblAutor = new JLabel(encabezado);
        lblAutor.setFont(new Font("SansSerif", Font.BOLD, 12));
        tarjeta.add(lblAutor, BorderLayout.NORTH);

        JLabel lblContenido = new JLabel("<html><p style='width:400px'>" + contenido + "</p></html>");
        tarjeta.add(lblContenido, BorderLayout.CENTER);

        panelAnotaciones.add(tarjeta);
        panelAnotaciones.add(Box.createVerticalStrut(10));
        panelAnotaciones.revalidate();
    }


    /**
     * Limpia el panel de anotaciones.
     */
    public void clearAnnotations() {
        panelAnotaciones.removeAll();
        panelAnotaciones.revalidate();
        panelAnotaciones.repaint();
    }

    // -----------------------------------------------------
    // Getters para que el controlador acceda a los componentes
    // -----------------------------------------------------

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public JList<AccederDiscusionDTO> getListArticulos() {
        return listArticulos;
    }

    public JTextArea getTextNuevaAnotacion() {
        return textNuevaAnotacion;
    }

    public JComboBox<DecisionRevisor> getComboDecision() {
        return comboDecision;
    }

    public JButton getBtnAgregarNota() {
        return btnAgregarNota;
    }

    public JButton getBtnMantenerFirme() {
        return btnMantenerFirme;
    }

}
