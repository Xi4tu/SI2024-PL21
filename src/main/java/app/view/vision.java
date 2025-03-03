package app.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import app.dto.AceptarDenegarArticuloDTO;
import app.dto.RevisionArticuloRevisorDTO;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JTabbedPane;

public class vision extends JFrame {

	private JPanel contentPane;
	private JTextField tfNivelDeExperto;
	private JTextField tfDecision;
	private JComboBox cbRevisor;
	private JLabel lbComentariosParaAutores;
	private JLabel lbComentariosCoordinadores;
	private JTextPane tpComentariosParaAutores;
	private JTextPane tpComentariosParaCoordinadores;
	private JLabel lbValoraciónGlobal;
	private JTextField tfValoracionGlobal;
	private JButton btnAceptar;
	private JButton btnRechazar;
	private JLabel lbArticulosSinDecisionRegistrada;
	private JList<AceptarDenegarArticuloDTO> lstArticulosSinDecisionRegistrada;

	private JFrame frame;
	private JTabbedPane tpListas;
	private JList<AceptarDenegarArticuloDTO> lstAutomaticos;
	private JButton btnNewButton;

	public vision() {
		initialize();
	}


	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Aceptar o denegar artículos");
		frame.setBounds(100, 100, 757, 533);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		frame.setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][grow][][][grow][][][][][215.00,grow][][grow]", "[][][::25px,grow][][][][][][grow][][grow][][][]"));

		lbArticulosSinDecisionRegistrada = new JLabel("Artículos sin decisión registrada");
		lbArticulosSinDecisionRegistrada.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lbArticulosSinDecisionRegistrada, "cell 1 1 7 1");

		JLabel lbRevisor = new JLabel("Revisor");
		lbRevisor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lbRevisor, "cell 9 1,alignx left");

		cbRevisor = new JComboBox();

		cbRevisor.setModel(new DefaultComboBoxModel(new String[] { "Revisor 1", "Revisor 2", "Revisor 3" }));
		cbRevisor.setSelectedIndex(0);
		contentPane.add(cbRevisor, "cell 10 1,growx");

		JLabel lbNivelDeExperto = new JLabel("Nivel de experto");
		lbNivelDeExperto.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lbNivelDeExperto, "cell 9 3");

		tfNivelDeExperto = new JTextField();
		tfNivelDeExperto.setEditable(false);
		contentPane.add(tfNivelDeExperto, "cell 10 3,growx");
		tfNivelDeExperto.setColumns(10);

		JLabel lblDecisión = new JLabel("Decisión");
		lblDecisión.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblDecisión, "cell 9 5");

		tfDecision = new JTextField();
		tfDecision.setEditable(false);
		contentPane.add(tfDecision, "cell 10 5,growx");
		tfDecision.setColumns(10);

		lbComentariosParaAutores = new JLabel("Comentarios para los autores");
		lbComentariosParaAutores.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lbComentariosParaAutores, "cell 9 7");

		tpComentariosParaAutores = new JTextPane();
		tpComentariosParaAutores.setEditable(false);
		contentPane.add(tpComentariosParaAutores, "cell 9 8 3 1,grow");

		lbComentariosCoordinadores = new JLabel("Comentarios para los coordinadores");
		lbComentariosCoordinadores.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lbComentariosCoordinadores, "cell 9 9");
		
		tpListas = new JTabbedPane(JTabbedPane.TOP);
		tpListas.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tpListas.setTitleAt(0, "Sin registro");
        tpListas.setTitleAt(1, "Automáticos");

		contentPane.add(tpListas, "cell 1 2 7 9,grow");
		
				//Lista
				lstArticulosSinDecisionRegistrada = new JList<>();
				tpListas.addTab("New tab", null, lstArticulosSinDecisionRegistrada, null);
				lstArticulosSinDecisionRegistrada.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				
				lstAutomaticos = new JList();
				tpListas.addTab("New tab", null, lstAutomaticos, null);

		tpComentariosParaCoordinadores = new JTextPane();
		tpComentariosParaCoordinadores.setEditable(false);
		contentPane.add(tpComentariosParaCoordinadores, "cell 9 10 3 1,grow");
		
		btnNewButton = new JButton("Aceptar todos");
		btnNewButton.setEnabled(false);
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(btnNewButton, "cell 1 11 4 1");

		lbValoraciónGlobal = new JLabel("Valoración global");
		lbValoraciónGlobal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lbValoraciónGlobal, "cell 9 11,alignx left,aligny baseline");

		tfValoracionGlobal = new JTextField();
		tfValoracionGlobal.setEditable(false);
		tfValoracionGlobal.setColumns(10);
		contentPane.add(tfValoracionGlobal, "cell 10 11,growx");

		btnAceptar = new JButton("Aceptar");
		btnAceptar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(btnAceptar, "cell 10 13");

		btnRechazar = new JButton("Rechazar");
		btnRechazar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(btnRechazar, "cell 11 13");
	}

	// Getters

	public JFrame getFrame() {
		return this.frame;
	}
	
	public JList<AceptarDenegarArticuloDTO> getListArticulos() {
		return this.lstArticulosSinDecisionRegistrada;
	}

	public JButton getbtnAceptar() {
		return this.btnAceptar;
	}
	
	public JButton getbtnRechazar() {
		return this.btnRechazar;
	}

	public JComboBox getcbRevisor() {
		return this.cbRevisor;
	}

	public JTextField gettfNivelDeExperto() {
		return this.tfNivelDeExperto;
	}
	
	public JTextPane gettpComentariosParaAutores() {
		return this.tpComentariosParaAutores;
	}
	
	public JTextPane gettpComentariosParaCoordinadores() {
		return this.tpComentariosParaCoordinadores;
	}
	
	public JTextField gettfDecision() {
		return this.tfDecision;
	}
	
	public JTextField gettfValoracion() {
		return this.tfValoracionGlobal;
	}
	

}
