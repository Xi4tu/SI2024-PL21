package app.view;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import app.dto.AutorDTO;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EnviarArticuloView {

	// Variables
	private JPanel contentPane;
	private JTextField textfTituloArticulo;
	private JTextField textfPalabrasClaveArticulo;
	private JTextField textfResumenArticulo;
	private JTextField textfArchivoArticulo;
	private JTable tableListaDeAutores;
	private JTextField textfNombreCoautor;
	private JTextField textfCorreoCoautor;
	private JTextField textfOrganizacionCoautor;
	private JTextField textfGrupoInvestigacionCoautor;
	private JButton btnEnviar;
	private JButton btnCancelar;
	private JButton btnAnadirAutor;
	private JFrame frame;
	private List<AutorDTO> listaDeAutores = new ArrayList<AutorDTO>();

	public EnviarArticuloView() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();

		frame.setTitle("Nuevo envío de artículo");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setBounds(100, 100, 886, 615);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		frame.setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Titulo");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 10, 36, 17);
		contentPane.add(lblNewLabel);

		JLabel lblPalabrasClave = new JLabel("Palabras clave:");
		lblPalabrasClave.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPalabrasClave.setBounds(10, 48, 93, 17);
		contentPane.add(lblPalabrasClave);

		JLabel lblResumen = new JLabel("Resumen: ");
		lblResumen.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblResumen.setBounds(10, 93, 67, 17);
		contentPane.add(lblResumen);

		JLabel lblArticulo = new JLabel("Archivo:");
		lblArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblArticulo.setBounds(10, 138, 61, 17);
		contentPane.add(lblArticulo);

		JLabel lblListaDeAutories = new JLabel("Lista de autores");
		lblListaDeAutories.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblListaDeAutories.setBounds(10, 188, 100, 17);
		contentPane.add(lblListaDeAutories);

		textfTituloArticulo = new JTextField();
		textfTituloArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textfTituloArticulo.setBounds(56, 11, 806, 19);
		contentPane.add(textfTituloArticulo);
		textfTituloArticulo.setColumns(10);

		textfPalabrasClaveArticulo = new JTextField();
		String textoPorDefecto = "separa las palabras por coma: palabra1,palabra2,palabra3...";
		textfPalabrasClaveArticulo.setText(textoPorDefecto);
		textfPalabrasClaveArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textfPalabrasClaveArticulo.setColumns(10);
		textfPalabrasClaveArticulo.setBounds(113, 49, 749, 19);
		contentPane.add(textfPalabrasClaveArticulo);
		//Borrar el texto por defecto al hacer click
		textfPalabrasClaveArticulo.addFocusListener(new java.awt.event.FocusAdapter() {
			// Solo cuando el texto es el texto por defecto, claro
			public void focusGained(java.awt.event.FocusEvent evt) {
				if (textfPalabrasClaveArticulo.getText().equals(textoPorDefecto)) {
					textfPalabrasClaveArticulo.setText("");
				}
			}
		});

		textfResumenArticulo = new JTextField();
		textfResumenArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textfResumenArticulo.setColumns(10);
		textfResumenArticulo.setBounds(87, 94, 775, 19);
		contentPane.add(textfResumenArticulo);

		textfArchivoArticulo = new JTextField();
		textfArchivoArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textfArchivoArticulo.setColumns(10);
		textfArchivoArticulo.setBounds(81, 139, 781, 19);
		contentPane.add(textfArchivoArticulo);

		tableListaDeAutores = new JTable();
		tableListaDeAutores.setFillsViewportHeight(true);
		tableListaDeAutores.setShowGrid(false);
		tableListaDeAutores.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Nombre", "Correo", "Organizacion", "GrupoInv" }) {
			Class[] columnTypes = new Class[] { String.class, Object.class, Object.class, Object.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tableListaDeAutores.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tableListaDeAutores.setBounds(120, 205, 742, 107);
		contentPane.add(tableListaDeAutores);

		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNombre.setBounds(158, 188, 100, 17);
		contentPane.add(lblNombre);

		JLabel lblCorreo = new JLabel("Correo");
		lblCorreo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCorreo.setBounds(365, 188, 52, 17);
		contentPane.add(lblCorreo);

		JLabel lblOrganizacion = new JLabel("Organizacion");
		lblOrganizacion.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblOrganizacion.setBounds(536, 188, 100, 17);
		contentPane.add(lblOrganizacion);

		JLabel lblGrupoinv = new JLabel("GrupoInv");
		lblGrupoinv.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGrupoinv.setBounds(723, 188, 100, 17);
		contentPane.add(lblGrupoinv);

		JLabel lblCoautores = new JLabel("Autores:");
		lblCoautores.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCoautores.setBounds(120, 322, 100, 17);
		contentPane.add(lblCoautores);

		JLabel lblNombre_1 = new JLabel("Nombre:");
		lblNombre_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNombre_1.setBounds(176, 349, 100, 17);
		contentPane.add(lblNombre_1);

		JLabel lblCorreo_1 = new JLabel("Correo:");
		lblCorreo_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCorreo_1.setBounds(176, 376, 100, 17);
		contentPane.add(lblCorreo_1);

		JLabel lblOrganizacin = new JLabel("Organización:");
		lblOrganizacin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblOrganizacin.setBounds(176, 403, 100, 17);
		contentPane.add(lblOrganizacin);

		JLabel lblGrupoInvest = new JLabel("Grupo Invest:");
		lblGrupoInvest.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGrupoInvest.setBounds(176, 430, 100, 17);
		contentPane.add(lblGrupoInvest);

		textfNombreCoautor = new JTextField();
		textfNombreCoautor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textfNombreCoautor.setColumns(10);
		textfNombreCoautor.setBounds(274, 348, 588, 19);
		contentPane.add(textfNombreCoautor);

		textfCorreoCoautor = new JTextField();
		textfCorreoCoautor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textfCorreoCoautor.setColumns(10);
		textfCorreoCoautor.setBounds(274, 377, 588, 19);
		contentPane.add(textfCorreoCoautor);

		textfOrganizacionCoautor = new JTextField();
		textfOrganizacionCoautor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textfOrganizacionCoautor.setColumns(10);
		textfOrganizacionCoautor.setBounds(274, 403, 588, 19);
		contentPane.add(textfOrganizacionCoautor);

		textfGrupoInvestigacionCoautor = new JTextField();
		textfGrupoInvestigacionCoautor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textfGrupoInvestigacionCoautor.setColumns(10);
		textfGrupoInvestigacionCoautor.setBounds(274, 432, 588, 19);
		contentPane.add(textfGrupoInvestigacionCoautor);

		btnEnviar = new JButton("Enviar");
		btnEnviar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnEnviar.setBounds(752, 547, 110, 21);
		contentPane.add(btnEnviar);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCancelar.setBounds(633, 547, 110, 21);
		contentPane.add(btnCancelar);

		btnAnadirAutor = new JButton("Incluir");
		btnAnadirAutor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnAnadirAutor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAnadirAutor.setBounds(120, 457, 110, 21);
		contentPane.add(btnAnadirAutor);

	}


	//Manejo de tabla
	public void agregarAutor(String nombre, String correo, String organizacion, String grupoInv) {
	    DefaultTableModel modelo = (DefaultTableModel) tableListaDeAutores.getModel();
	    modelo.addRow(new Object[] { nombre, correo, organizacion, grupoInv });
	}
	
	//Getters y Setters

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
	public JTextField getTextfTituloArticulo() {
		return textfTituloArticulo;
	}

	public void setTextfTituloArticulo(JTextField textfTituloArticulo) {
		this.textfTituloArticulo = textfTituloArticulo;
	}

	public JTextField getTextfPalabrasClaveArticulo() {
		return textfPalabrasClaveArticulo;
	}

	public void setTextfPalabrasClaveArticulo(JTextField textfPalabrasClaveArticulo) {
		this.textfPalabrasClaveArticulo = textfPalabrasClaveArticulo;
	}

	public JTextField getTextfResumenArticulo() {
		return textfResumenArticulo;
	}

	public void setTextfResumenArticulo(JTextField textfResumenArticulo) {
		this.textfResumenArticulo = textfResumenArticulo;
	}

	public JTextField getTextfArchivoArticulo() {
		return textfArchivoArticulo;
	}

	public void setTextfArchivoArticulo(JTextField textfArchivoArticulo) {
		this.textfArchivoArticulo = textfArchivoArticulo;
	}

	public JTable getTableListaDeAutores() {
		return tableListaDeAutores;
	}

	public void setTableListaDeAutores(JTable tableListaDeAutores) {
		this.tableListaDeAutores = tableListaDeAutores;
	}

	public JTextField getTextfNombreCoautor() {
		return textfNombreCoautor;
	}

	public void setTextfNombreCoautor(JTextField textfNombreCoautor) {
		this.textfNombreCoautor = textfNombreCoautor;
	}

	public JTextField getTextfCorreoCoautor() {
		return textfCorreoCoautor;
	}

	public void setTextfCorreoCoautor(JTextField textfCorreoCoautor) {
		this.textfCorreoCoautor = textfCorreoCoautor;
	}

	public JTextField getTextfOrganizacionCoautor() {
		return textfOrganizacionCoautor;
	}

	public void setTextfOrganizacionCoautor(JTextField textfOrganizacionCoautor) {
		this.textfOrganizacionCoautor = textfOrganizacionCoautor;
	}

	public JTextField getTextfGrupoInvestigacionCoautor() {
		return textfGrupoInvestigacionCoautor;
	}

	public void setTextfGrupoInvestigacionCoautor(JTextField textfGrupoInvestigacionCoautor) {
		this.textfGrupoInvestigacionCoautor = textfGrupoInvestigacionCoautor;
	}

	public JButton getBtnEnviar() {
		return btnEnviar;
	}

	public void setBtnEnviar(JButton btnEnviar) {
		this.btnEnviar = btnEnviar;
	}

	public JButton getBtnCancelar() {
		return btnCancelar;
	}

	public void setBtnCancelar(JButton btnCancelar) {
		this.btnCancelar = btnCancelar;
	}

	public JButton getBtnAnadirAutor() {
		return btnAnadirAutor;
	}

	public void setBtnAnadirAutor(JButton btnAnadirAutor) {
		this.btnAnadirAutor = btnAnadirAutor;
	}

	public List<AutorDTO> getListaDeAutores() {
		return listaDeAutores;
	}

	public void setListaDeAutores(List<AutorDTO> listaDeAutores) {
		this.listaDeAutores = listaDeAutores;
	}

	
	//Método para mostrar un mensaje de error sencillito
	public void mostrarMensajeError(String mensaje) {
		JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}

}
