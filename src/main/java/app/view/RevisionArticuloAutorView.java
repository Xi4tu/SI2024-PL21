package app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import app.dto.RevisionArticuloAutorDTO;
import app.enums.DecisionRevisor;

public class RevisionArticuloAutorView {

    // Variables de instancia
	private JFrame frame;
    private JPanel contentPane;
    private JList<RevisionArticuloAutorDTO> listArticulos;
    private JLabel lblDecisionFinal;
    private JButton btnCerrar;
    private JPanel panelRevisiones;
    private JScrollPane scrollRevisiones;

    // Constructor
    public RevisionArticuloAutorView() {
        initialize();
    }

    // Método para inicializar la vista
    private void initialize() {
    	frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setTitle("Revisiones de mis Artículos");
        frame.setBounds(100, 100, 900, 500);

        // Panel principal
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        frame.setContentPane(contentPane);

        // ---------------------------------------------------------------------
        // Panel Izquierdo: Lista de artículos
        // ---------------------------------------------------------------------
        JPanel panelArticulos = new JPanel(new BorderLayout());
        panelArticulos.setBorder(BorderFactory.createTitledBorder("Mis Artículos con Decisión"));

        // Inicializar lista de artículos sin datos de prueba (se cargará desde el controlador)
        listArticulos = new JList<>(new DefaultListModel<>());
        listArticulos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
        JScrollPane scrollArticulos = new JScrollPane(listArticulos);
        panelArticulos.add(scrollArticulos, BorderLayout.CENTER);
        panelArticulos.setPreferredSize(new Dimension(250, 0));
        contentPane.add(panelArticulos, BorderLayout.WEST);

        // ---------------------------------------------------------------------
        // Panel Derecho: Detalles de la revisión
        // ---------------------------------------------------------------------
        JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
        contentPane.add(panelDerecho, BorderLayout.CENTER);

        // Panel superior para la decisión final
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Decisión Final del Artículo"));
        panelSuperior.setLayout(new FlowLayout(FlowLayout.CENTER));

        lblDecisionFinal = new JLabel("");
        lblDecisionFinal.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblDecisionFinal.setHorizontalAlignment(SwingConstants.CENTER);
        panelSuperior.add(lblDecisionFinal);
        panelDerecho.add(panelSuperior, BorderLayout.NORTH);

        // Panel de revisiones con layout vertical
        panelRevisiones = new JPanel();
        panelRevisiones.setLayout(new BoxLayout(panelRevisiones, BoxLayout.Y_AXIS));

        scrollRevisiones = new JScrollPane(panelRevisiones);
        scrollRevisiones.setBorder(BorderFactory.createTitledBorder("Comentarios de Revisores"));
        scrollRevisiones.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollRevisiones.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelDerecho.add(scrollRevisiones, BorderLayout.CENTER);

        // Panel inferior con botón "Cerrar"
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCerrar = new JButton("Cerrar");
        panelInferior.add(btnCerrar);
        panelDerecho.add(panelInferior, BorderLayout.SOUTH);
    }


    // Método público para agregar una revisión (para ser invocado desde el controlador)
    public void agregarRevision(String comentario, String nivelExperiencia, String decision) {
        JPanel tarjetaRevision = new JPanel();
        tarjetaRevision.setLayout(new BorderLayout(5, 5));
        tarjetaRevision.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(5, 5, 5, 5),
                BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(200, 200, 200), 1, true),
                        new EmptyBorder(10, 10, 10, 10)
                )
        ));
        tarjetaRevision.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        tarjetaRevision.setBackground(new Color(250, 250, 250));

        // Panel para el encabezado (nivel de experiencia y decisión)
        JPanel panelEncabezado = new JPanel(new GridLayout(1, 2, 5, 0));

        // Nivel de experiencia
        JPanel panelNivel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblNivelTitulo = new JLabel("Nivel de experiencia: ");
        lblNivelTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        JLabel lblNivelValor = new JLabel(nivelExperiencia);
        panelNivel.add(lblNivelTitulo);
        panelNivel.add(lblNivelValor);
        panelNivel.setOpaque(false);

        // Decisión del revisor
        JPanel panelDecision = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblDecisionTitulo = new JLabel("Decisión: ");
        lblDecisionTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        JLabel lblDecisionValor = new JLabel(decision);
        DecisionRevisor decisionEnum = DecisionRevisor.fromLabel(decision);
        if (decisionEnum != null) {
            lblDecisionValor.setForeground(decisionEnum.getColor());
        } else {
            // Color por defecto si no se encuentra la decisión en el enum.
            lblDecisionValor.setForeground(new Color(205, 133, 0));
        }
        panelDecision.add(lblDecisionTitulo);
        panelDecision.add(lblDecisionValor);
        panelDecision.setOpaque(false);

        panelEncabezado.add(panelNivel);
        panelEncabezado.add(panelDecision);
        panelEncabezado.setOpaque(false);
        tarjetaRevision.add(panelEncabezado, BorderLayout.NORTH);

        // Separador
        JSeparator separador = new JSeparator();
        separador.setForeground(new Color(220, 220, 220));
        tarjetaRevision.add(separador, BorderLayout.CENTER);

        // Comentario del revisor
        JPanel panelComentario = new JPanel(new BorderLayout(5, 5));
        JLabel lblComentarioTitulo = new JLabel("Comentario:");
        lblComentarioTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        JLabel lblComentarioValor = new JLabel("<html><p style='width:400px'>" + comentario + "</p></html>");
        panelComentario.add(lblComentarioTitulo, BorderLayout.NORTH);
        panelComentario.add(lblComentarioValor, BorderLayout.CENTER);
        panelComentario.setOpaque(false);
        tarjetaRevision.add(panelComentario, BorderLayout.SOUTH);

        panelRevisiones.add(tarjetaRevision);
        panelRevisiones.add(Box.createRigidArea(new Dimension(0, 10)));

        panelRevisiones.revalidate();
        panelRevisiones.repaint();
    }

    // Getters y Setters para los elementos de la vista
    
	public JFrame getFrame() {
		return frame;
	}

    public JPanel getContentPaneView() {
        return contentPane;
    }

    public JList<RevisionArticuloAutorDTO> getListArticulos() {
        return listArticulos;
    }

    public JLabel getLblDecisionFinal() {
        return lblDecisionFinal;
    }

    public JButton getBtnCerrar() {
        return btnCerrar;
    }

    public JPanel getPanelRevisiones() {
        return panelRevisiones;
    }

    public JScrollPane getScrollRevisiones() {
        return scrollRevisiones;
    }
}

