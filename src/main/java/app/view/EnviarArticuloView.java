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
import app.dto.TrackDTO;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JComboBox;

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
	private JButton btnAgregarPalabrasClaveDelTrack;
	private JFrame frame;
	private JLabel lblPalabrasDelTrackArticulo;
	private List<AutorDTO> listaDeAutores = new ArrayList<AutorDTO>();
	
	private JComboBox<String> comboBoxSelectorTrackArticulo;
	private JComboBox<String> comboBoxSelectorPalabrasDelTrack;

	private String estadoInicialLabelPalabrasClaveTrack = "";
	
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

		JLabel lblPalabrasClave = new JLabel("Palabras clave del artículo:");
		lblPalabrasClave.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPalabrasClave.setBounds(10, 128, 166, 17);
		contentPane.add(lblPalabrasClave);

		JLabel lblResumen = new JLabel("Resumen: ");
		lblResumen.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblResumen.setBounds(10, 158, 67, 17);
		contentPane.add(lblResumen);

		JLabel lblArticulo = new JLabel("Archivo:");
		lblArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblArticulo.setBounds(10, 188, 61, 17);
		contentPane.add(lblArticulo);

		JLabel lblListaDeAutories = new JLabel("Lista de autores");
		lblListaDeAutories.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblListaDeAutories.setBounds(10, 218, 100, 17);
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
		textfPalabrasClaveArticulo.setBounds(176, 129, 686, 19);
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
		textfResumenArticulo.setBounds(87, 159, 775, 19);
		contentPane.add(textfResumenArticulo);

		textfArchivoArticulo = new JTextField();
		textfArchivoArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textfArchivoArticulo.setColumns(10);
		textfArchivoArticulo.setBounds(81, 189, 781, 19);
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
		tableListaDeAutores.setBounds(120, 235, 742, 107);
		contentPane.add(tableListaDeAutores);

		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNombre.setBounds(158, 218, 100, 17);
		contentPane.add(lblNombre);

		JLabel lblCorreo = new JLabel("Correo");
		lblCorreo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCorreo.setBounds(365, 218, 52, 17);
		contentPane.add(lblCorreo);

		JLabel lblOrganizacion = new JLabel("Organizacion");
		lblOrganizacion.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblOrganizacion.setBounds(536, 218, 100, 17);
		contentPane.add(lblOrganizacion);

		JLabel lblGrupoinv = new JLabel("GrupoInv");
		lblGrupoinv.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGrupoinv.setBounds(723, 218, 100, 17);
		contentPane.add(lblGrupoinv);

		JLabel lblCoautores = new JLabel("Autores:");
		lblCoautores.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCoautores.setBounds(120, 352, 100, 17);
		contentPane.add(lblCoautores);

		JLabel lblNombre_1 = new JLabel("Nombre:");
		lblNombre_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNombre_1.setBounds(176, 379, 100, 17);
		contentPane.add(lblNombre_1);

		JLabel lblCorreo_1 = new JLabel("Correo:");
		lblCorreo_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCorreo_1.setBounds(176, 406, 100, 17);
		contentPane.add(lblCorreo_1);

		JLabel lblOrganizacin = new JLabel("Organización:");
		lblOrganizacin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblOrganizacin.setBounds(176, 433, 100, 17);
		contentPane.add(lblOrganizacin);

		JLabel lblGrupoInvest = new JLabel("Grupo Invest:");
		lblGrupoInvest.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGrupoInvest.setBounds(176, 460, 100, 17);
		contentPane.add(lblGrupoInvest);

		textfNombreCoautor = new JTextField();
		textfNombreCoautor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textfNombreCoautor.setColumns(10);
		textfNombreCoautor.setBounds(274, 378, 588, 19);
		contentPane.add(textfNombreCoautor);

		textfCorreoCoautor = new JTextField();
		textfCorreoCoautor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textfCorreoCoautor.setColumns(10);
		textfCorreoCoautor.setBounds(274, 407, 588, 19);
		contentPane.add(textfCorreoCoautor);

		textfOrganizacionCoautor = new JTextField();
		textfOrganizacionCoautor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textfOrganizacionCoautor.setColumns(10);
		textfOrganizacionCoautor.setBounds(274, 433, 588, 19);
		contentPane.add(textfOrganizacionCoautor);

		textfGrupoInvestigacionCoautor = new JTextField();
		textfGrupoInvestigacionCoautor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textfGrupoInvestigacionCoautor.setColumns(10);
		textfGrupoInvestigacionCoautor.setBounds(274, 462, 588, 19);
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
		btnAnadirAutor.setBounds(120, 487, 110, 21);
		contentPane.add(btnAnadirAutor);
		
		JLabel lblTrack = new JLabel("Track:");
		lblTrack.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTrack.setBounds(10, 37, 44, 17);
		contentPane.add(lblTrack);
		
		comboBoxSelectorTrackArticulo = new JComboBox();
		comboBoxSelectorTrackArticulo.setBounds(56, 37, 806, 19);
		contentPane.add(comboBoxSelectorTrackArticulo);
		
		JLabel lblPalabrasClaveDel = new JLabel("Palabras clave del track:");
		lblPalabrasClaveDel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPalabrasClaveDel.setBounds(10, 64, 151, 17);
		contentPane.add(lblPalabrasClaveDel);
		
		lblPalabrasDelTrackArticulo = new JLabel(estadoInicialLabelPalabrasClaveTrack);
		lblPalabrasDelTrackArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPalabrasDelTrackArticulo.setBounds(139, 94, 723, 17);
		contentPane.add(lblPalabrasDelTrackArticulo);
		
		comboBoxSelectorPalabrasDelTrack = new JComboBox();
		comboBoxSelectorPalabrasDelTrack.setBounds(168, 64, 226, 19);
		contentPane.add(comboBoxSelectorPalabrasDelTrack);
		
		JLabel lblMarcadas = new JLabel("Seleccionadas:");
		lblMarcadas.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMarcadas.setBounds(43, 94, 91, 17);
		contentPane.add(lblMarcadas);
		
		btnAgregarPalabrasClaveDelTrack = new JButton("Agregar");
		btnAgregarPalabrasClaveDelTrack.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAgregarPalabrasClaveDelTrack.setBounds(406, 63, 110, 21);
		contentPane.add(btnAgregarPalabrasClaveDelTrack);

	}


	//Manejo de tabla
	public void agregarAutor(String nombre, String correo, String organizacion, String grupoInv) {
	    DefaultTableModel modelo = (DefaultTableModel) tableListaDeAutores.getModel();
	    modelo.addRow(new Object[] { nombre, correo, organizacion, grupoInv });
	}
	
	//Getters y Setters
	
	public JComboBox getComboBoxSelectorTrackArticulo() {
		return comboBoxSelectorTrackArticulo;
	}
	
	public void setComboBoxSelectorTrackArticulo(JComboBox comboBoxSelectorTrackArticulo) {
		this.comboBoxSelectorTrackArticulo = comboBoxSelectorTrackArticulo;
	}
	
	public JComboBox getComboBoxSelectorPalabrasDelTrack() {
		return comboBoxSelectorPalabrasDelTrack;
	}
	
	public void setComboBoxSelectorPalabrasDelTrack(JComboBox comboBoxSelectorPalabrasDelTrack) {
		this.comboBoxSelectorPalabrasDelTrack = comboBoxSelectorPalabrasDelTrack;
	}

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
	
	public JButton getBtnAgregarPalabrasClaveDelTrack() {
		return btnAgregarPalabrasClaveDelTrack;
	}
	
	public void setBtnAgregarPalabrasClaveDelTrack(JButton btnAgregarPalabrasClaveDelTrack) {
		this.btnAgregarPalabrasClaveDelTrack = btnAgregarPalabrasClaveDelTrack;
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
	
	//Metodo para rellenar el comboBox de tracks
	public void setTracks(List<TrackDTO> obtenerTracks) {
		for (TrackDTO track : obtenerTracks) {
			comboBoxSelectorTrackArticulo.addItem(track.getNombre());
		}
	}
	
	// Metodo para rellenar el comboBox de palabras clave de un track
	public void setPalabrasClaveTrack(String[] palabrasClave) {
		for (String palabra : palabrasClave) {
			comboBoxSelectorPalabrasDelTrack.addItem(palabra);
		}
	}
	
	// Metodo para limpiar las palabras clave de un track
	public void limpiarPalabrasClaveTrack() {
		comboBoxSelectorPalabrasDelTrack.removeAllItems();
	}
	
	// Metodo para agregar la palabra clave seleccionada a label de palabras clave del track
	// SI NO ESTA YA EN LA LISTA DE PALABRAS CLAVE DEL TRACK
	public void agregarPalabraClave(String palabraClave) {
		// Si es la primera que añaades (el label esta en su estado inicial) elimina el label
		if (lblPalabrasDelTrackArticulo.getText().equals(estadoInicialLabelPalabrasClaveTrack)) {
			lblPalabrasDelTrackArticulo.setText(palabraClave);
		} else
		if (!lblPalabrasDelTrackArticulo.getText().contains(palabraClave)) {
			lblPalabrasDelTrackArticulo.setText(lblPalabrasDelTrackArticulo.getText() + "," + palabraClave);
		}
	}
	
	// Metodo para resetear el label de palabras clave del track a "..."
	public void resetearPalabrasClaveTrack() {
		lblPalabrasDelTrackArticulo.setText(estadoInicialLabelPalabrasClaveTrack);
	}
	
	// Metodo que devuelve el texto del campo de texto de palabras clave del articulo como una lista de Strings
	public String[] getPalabrasClaveArticuloLista() {
		// Si esta como el texto por defecto, devuelvo un array vacio
		if (lblPalabrasDelTrackArticulo.getText().equals(estadoInicialLabelPalabrasClaveTrack)) {
			return new String[0];
		}
		// Si no, devuelvo un array con las palabras clave separadas por comas
		return lblPalabrasDelTrackArticulo.getText().split(",");
	}
	
	// Metodo que devuelve el texto del campo de texto de palabras clave del articulo como una unica String separada por comas
	public String getPalabrasClaveArticuloString() {
		return lblPalabrasDelTrackArticulo.getText();
	}
}
