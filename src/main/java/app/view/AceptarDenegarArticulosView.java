package app.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class AceptarDenegarArticulosView {

	private JPanel contentPane;
	private JTextField tfNivelDeExperto;
	private JTextField tfDecision;
	private JComboBox cbRevisor;
	private JLabel lbComentariosParaAutores;
	private JLabel lbComentariosCoordinadores;
	private JTextPane tpComentariosParaAutores;
	private JTextPane tpComentariosParaCoordinadores;
	private JLabel lbValoraciónGlobal;
	private JTextField tfValoraciónGlobal;
	private JButton btnAceptar;
	private JButton btnRechazar;
	private JLabel lbArticulosSinDecisionRegistrada;
	private JList lstArticulosSinDecisionRegistrada;
	
	private JFrame frame;
	
	public AceptarDenegarArticulosView() {
		initialize();
	}
	

	/**
	 * Launch the application.
	 */
	/**
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AceptarDenegarArticulosView frame = new AceptarDenegarArticulosView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Aceptar o denegar artículos");
		frame.setBounds(100, 100, 757, 533);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		frame.setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][grow][][][][][][][][215.00,grow][][grow]", "[][][::25px,grow][][][][][][grow][][grow][][][]"));
		
		lbArticulosSinDecisionRegistrada = new JLabel("Artículos sin decisión registrada");
		lbArticulosSinDecisionRegistrada.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lbArticulosSinDecisionRegistrada, "cell 1 1 7 1");
		
		JLabel lbRevisor = new JLabel("Revisor");
		lbRevisor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lbRevisor, "cell 9 1,alignx left");
		
		cbRevisor = new JComboBox();
		
		cbRevisor.setModel(new DefaultComboBoxModel(new String[] {"Revisor 1", "Revisor 2", "Revisor 3"}));
		cbRevisor.setSelectedIndex(0);
		contentPane.add(cbRevisor, "cell 10 1,growx");
		
		lstArticulosSinDecisionRegistrada = new JList();
		contentPane.add(lstArticulosSinDecisionRegistrada, "cell 1 2 7 10,grow");
		
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
		
		tpComentariosParaCoordinadores = new JTextPane();
		tpComentariosParaCoordinadores.setEditable(false);
		contentPane.add(tpComentariosParaCoordinadores, "cell 9 10 3 1,grow");
		
		lbValoraciónGlobal = new JLabel("Valoración global");
		lbValoraciónGlobal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lbValoraciónGlobal, "cell 9 11,alignx left,aligny baseline");
		
		tfValoraciónGlobal = new JTextField();
		tfValoraciónGlobal.setEditable(false);
		tfValoraciónGlobal.setColumns(10);
		contentPane.add(tfValoraciónGlobal, "cell 10 11,growx");
		
		
		
		btnAceptar = new JButton("Aceptar"); 
		btnAceptar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(btnAceptar, "cell 10 13");
		
		btnRechazar = new JButton("Rechazar");
		btnRechazar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(btnRechazar, "cell 11 13");
	}
	
	//Getters
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public JButton getAceptar() {
		return this.btnAceptar;
	}
	
	public JComboBox getRevisor() {
		return this.cbRevisor;
	}

	public JTextField getNivelDeExperto() {
		return this.tfNivelDeExperto;
	}
	
}
