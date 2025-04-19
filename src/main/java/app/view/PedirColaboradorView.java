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

import app.dto.PedirColaboradorDTO;
import app.dto.RevisionArticuloAutorDTO;
import app.enums.DecisionRevisor;

public class PedirColaboradorView {

    // Variables de instancia
	private JFrame frmColaboradores;
    private JPanel contentPane;
    private JList<PedirColaboradorDTO> listRevisores;
    private JButton btnConfirmar;

    // Constructor
    public PedirColaboradorView() {
        initialize();
    }

    // Método para inicializar la vista
    private void initialize() {
    	frmColaboradores = new JFrame();
        frmColaboradores.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frmColaboradores.setTitle("Colaboradores");
        frmColaboradores.setBounds(100, 100, 283, 300);

        // Panel principal
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        frmColaboradores.setContentPane(contentPane);

        // ---------------------------------------------------------------------
        // Panel Izquierdo: Lista de artículos
        // ---------------------------------------------------------------------
        JPanel panelRevisores = new JPanel(new BorderLayout());
        panelRevisores.setBorder(BorderFactory.createTitledBorder("Lista de colaboradores"));

        // Inicializar lista de artículos sin datos de prueba (se cargará desde el controlador)
        listRevisores = new JList<>(new DefaultListModel<>());
        listRevisores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollRevisores = new JScrollPane(listRevisores);
        panelRevisores.add(scrollRevisores, BorderLayout.CENTER);
        panelRevisores.setPreferredSize(new Dimension(250, 0));
        contentPane.add(panelRevisores, BorderLayout.WEST);

        btnConfirmar = new JButton("Confirmar");
        contentPane.add(btnConfirmar, BorderLayout.SOUTH);
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
    }

    // Getters y Setters para los elementos de la vista

	public JFrame getFrame() {
		return frmColaboradores;
	}

    public JPanel getContentPaneView() {
        return contentPane;
    }

    public JList<PedirColaboradorDTO> getListRevisores() {
        return listRevisores;
    }

    public JButton getBtnConfirmar() {
    	return btnConfirmar;
    }
}
