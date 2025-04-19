package app.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import app.dto.GestionarSolicitudesColaboracionDTO;
import net.miginfocom.swing.MigLayout;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GestionarSolicitudesColaboracionView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfBuscador;
	private JFrame frame;
	private JList<GestionarSolicitudesColaboracionDTO> lstColaboradores;
	private JRadioButton rdbtnFiltrarPorTrack;
	private JRadioButton rdbtnFiltrarPorPalabrasAsociadas;
	private JButton btnFiltrar;
	private JButton btnBorrarFiltro;
	/**
	 * Create the frame.
	 */
	
	public GestionarSolicitudesColaboracionView() {
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		
		frame.setTitle("Gestionar Solicitudes de Colaboración");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setBounds(100, 100, 641, 526);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		frame.setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][240px:n,grow][grow][::100px,grow][::100px,grow]", "[grow][][grow][grow][grow][grow][grow][grow][grow][grow][grow][30px:n:100px][::40px,grow]"));
		
		JLabel lblArticulosConColaboracionSolicitada = new JLabel("Artículos con colaboración solicitada");
		lblArticulosConColaboracionSolicitada.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblArticulosConColaboracionSolicitada, "cell 1 0");
		
		JSeparator separator = new JSeparator();
		contentPane.add(separator, "cell 1 1 4 1");
		
		lstColaboradores = new JList();
		contentPane.add(lstColaboradores, "cell 1 2 1 10,grow");
		
		rdbtnFiltrarPorTrack = new JRadioButton("Filtrar por track");
		rdbtnFiltrarPorTrack.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(rdbtnFiltrarPorTrack, "cell 2 4 3 1");
		
		rdbtnFiltrarPorPalabrasAsociadas = new JRadioButton("Filtrar por palabras asociadas al track");
		rdbtnFiltrarPorPalabrasAsociadas.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(rdbtnFiltrarPorPalabrasAsociadas, "cell 2 5 3 1");
		
		tfBuscador = new JTextField();
		tfBuscador.setEnabled(false);
		tfBuscador.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(tfBuscador, "cell 2 6 3 1,growx");
		tfBuscador.setColumns(10);
		
		btnFiltrar = new JButton("Filtrar");
		btnFiltrar.setEnabled(false);
		btnFiltrar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnFiltrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.add(btnFiltrar, "cell 2 7,alignx center,aligny center");
		
		btnBorrarFiltro = new JButton("Borrar filtro");
		btnBorrarFiltro.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(btnBorrarFiltro, "cell 3 7");
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(btnAceptar, "cell 3 11,grow");
		
		JButton btnDenegar = new JButton("Denegar");
		btnDenegar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(btnDenegar, "cell 4 11,grow");
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
	public JList<GestionarSolicitudesColaboracionDTO> getListColaboradores() {
		return this.lstColaboradores;
	}
	
	public JRadioButton getRdbtnFiltrarPorTrack() {
		return this.rdbtnFiltrarPorTrack;
	}
	
	public JRadioButton getRdbtnFiltrarPorPalabrasAsociadas() {
		return this.rdbtnFiltrarPorPalabrasAsociadas;
	}
	
	public JTextField getTfBuscador() {
		return this.tfBuscador;
	}
	
	public JButton getBtnFiltrar() {
		return this.btnFiltrar;
	}
	
	public JButton getBtnBorrarFiltro() {
		return this.btnBorrarFiltro;
	}

}
