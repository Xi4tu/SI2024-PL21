package app.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VerMisArticulosView{

	private static final long serialVersionUID = 1L;
	private JTable tableArticulosDelAutor;
	private JTable tableAutoresDelArticulo;
	
	//Declaracion de las labels
	private JLabel lblAutorBusqueda;
	private JLabel lblIdArticulo;
	private JLabel lblTituloArticulo;
	private JLabel lblTituloTrackArticulo;
	private JLabel lblPalabrasClaveArticulo;
	private JLabel lblPalabrasClaveTrackArticulo;
	private JLabel lblResumenArticulo;
	private JLabel lblFicheroArticulo;
	private JLabel lblFechaEnvioArticulo;
	private JLabel lblEnviadoPorArticulo;
	private JLabel lblFechaModificacionArticulo;
	private JLabel lblEstado;
	private JLabel lblDecision;
	
	private JButton btnEditarArticulo;
	private JButton btnVerDecision;
	
	private JCheckBox chckbxSoloEnviadosPorMi;

	private JFrame frame;
	
	
	public VerMisArticulosView() {
		initialize();
	}
	

	/**
	 * Create the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		
		frame.setTitle("Listado de mis artículos");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 864, 578);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Lista de artículos de:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(10, 10, 274, 13);
		frame.getContentPane().add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(294, 10, 2, 521);
		frame.getContentPane().add(separator);
		
		lblAutorBusqueda = new JLabel("");
		lblAutorBusqueda.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAutorBusqueda.setBounds(10, 33, 274, 13);
		frame.getContentPane().add(lblAutorBusqueda);
		
		JLabel lblId = new JLabel("Id.");
		lblId.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId.setBounds(10, 81, 39, 13);
		frame.getContentPane().add(lblId);
		
		JLabel lblNombre = new JLabel("Título");
		lblNombre.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNombre.setBounds(55, 81, 60, 13);
		frame.getContentPane().add(lblNombre);
		
		tableArticulosDelAutor = new JTable();
		tableArticulosDelAutor.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"New column", "New column"
			}
		));
		tableArticulosDelAutor.getColumnModel().getColumn(0).setPreferredWidth(40);
		tableArticulosDelAutor.getColumnModel().getColumn(0).setMinWidth(20);
		tableArticulosDelAutor.getColumnModel().getColumn(0).setMaxWidth(60);
		tableArticulosDelAutor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tableArticulosDelAutor.setBounds(10, 96, 274, 404);
		frame.getContentPane().add(tableArticulosDelAutor);
		
		JLabel lblId_1 = new JLabel("Id.");
		lblId_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId_1.setBounds(306, 10, 21, 13);
		frame.getContentPane().add(lblId_1);
		
		 lblIdArticulo = new JLabel("");
		lblIdArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblIdArticulo.setBounds(337, 10, 45, 13);
		frame.getContentPane().add(lblIdArticulo);
		
		JLabel lblTtulo = new JLabel("Título:");
		lblTtulo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTtulo.setBounds(392, 10, 60, 13);
		frame.getContentPane().add(lblTtulo);
		
		 lblTituloArticulo = new JLabel("");
		lblTituloArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTituloArticulo.setBounds(444, 10, 396, 13);
		frame.getContentPane().add(lblTituloArticulo);
		
		JLabel lblId_1_1 = new JLabel("Palabras clave:");
		lblId_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId_1_1.setBounds(306, 58, 247, 13);
		frame.getContentPane().add(lblId_1_1);
		
		 lblPalabrasClaveArticulo = new JLabel("");
		lblPalabrasClaveArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPalabrasClaveArticulo.setBounds(306, 81, 247, 13);
		frame.getContentPane().add(lblPalabrasClaveArticulo);
		
		JLabel lblId_1_1_1 = new JLabel("Resumen:");
		lblId_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId_1_1_1.setBounds(306, 104, 450, 13);
		frame.getContentPane().add(lblId_1_1_1);
		
		 lblResumenArticulo = new JLabel("");
		lblResumenArticulo.setVerticalAlignment(SwingConstants.TOP);
		lblResumenArticulo.setHorizontalAlignment(SwingConstants.LEFT);
		lblResumenArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblResumenArticulo.setBounds(306, 127, 534, 29);
		frame.getContentPane().add(lblResumenArticulo);
		
		JLabel lblId_1_1_1_1 = new JLabel("Fichero:");
		lblId_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId_1_1_1_1.setBounds(306, 166, 60, 13);
		frame.getContentPane().add(lblId_1_1_1_1);
		
		 lblFicheroArticulo = new JLabel("");
		lblFicheroArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFicheroArticulo.setBounds(376, 166, 464, 13);
		frame.getContentPane().add(lblFicheroArticulo);
		
		JLabel lblId_1_1_1_1_1 = new JLabel("Fecha envío:");
		lblId_1_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId_1_1_1_1_1.setBounds(306, 189, 92, 13);
		frame.getContentPane().add(lblId_1_1_1_1_1);
		
		JLabel lblId_1_1_1_1_2 = new JLabel("Enviado por:");
		lblId_1_1_1_1_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId_1_1_1_1_2.setBounds(306, 258, 92, 13);
		frame.getContentPane().add(lblId_1_1_1_1_2);
		
		 lblFechaEnvioArticulo = new JLabel("");
		lblFechaEnvioArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFechaEnvioArticulo.setBounds(408, 189, 432, 13);
		frame.getContentPane().add(lblFechaEnvioArticulo);
		
		 lblEnviadoPorArticulo = new JLabel("");
		lblEnviadoPorArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEnviadoPorArticulo.setBounds(408, 258, 432, 13);
		frame.getContentPane().add(lblEnviadoPorArticulo);
		
		JLabel lblId_1_1_1_2 = new JLabel("Lista de autores:");
		lblId_1_1_1_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId_1_1_1_2.setBounds(306, 281, 450, 13);
		frame.getContentPane().add(lblId_1_1_1_2);
		
		tableAutoresDelArticulo = new JTable();
		tableAutoresDelArticulo.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"New column", "New column", "New column", "New column"
			}
		));
		tableAutoresDelArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tableAutoresDelArticulo.setBounds(306, 402, 534, 129);
		frame.getContentPane().add(tableAutoresDelArticulo);
		
		JLabel lblId_1_1_1_1_3 = new JLabel("Nombre");
		lblId_1_1_1_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblId_1_1_1_1_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId_1_1_1_1_3.setBounds(306, 304, 128, 13);
		frame.getContentPane().add(lblId_1_1_1_1_3);
		
		JLabel lblId_1_1_1_1_3_1 = new JLabel("email");
		lblId_1_1_1_1_3_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblId_1_1_1_1_3_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId_1_1_1_1_3_1.setBounds(444, 304, 124, 13);
		frame.getContentPane().add(lblId_1_1_1_1_3_1);
		
		JLabel lblId_1_1_1_1_3_2 = new JLabel("Organización");
		lblId_1_1_1_1_3_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblId_1_1_1_1_3_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId_1_1_1_1_3_2.setBounds(578, 304, 121, 13);
		frame.getContentPane().add(lblId_1_1_1_1_3_2);
		
		JLabel lblId_1_1_1_1_3_3 = new JLabel("Grupo inv.");
		lblId_1_1_1_1_3_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblId_1_1_1_1_3_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId_1_1_1_1_3_3.setBounds(709, 281, 131, 13);
		frame.getContentPane().add(lblId_1_1_1_1_3_3);
		
		chckbxSoloEnviadosPorMi = new JCheckBox("Ver solo artículos enviados por mí");
		chckbxSoloEnviadosPorMi.setFont(new Font("Tahoma", Font.BOLD, 14));
		chckbxSoloEnviadosPorMi.setBounds(10, 54, 274, 21);
		frame.getContentPane().add(chckbxSoloEnviadosPorMi);
		
		JLabel lblId_1_1_1_1_1_1 = new JLabel("Última modificación:");
		lblId_1_1_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId_1_1_1_1_1_1.setBounds(306, 212, 148, 13);
		frame.getContentPane().add(lblId_1_1_1_1_1_1);
		
		lblFechaModificacionArticulo = new JLabel("");
		lblFechaModificacionArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFechaModificacionArticulo.setBounds(457, 212, 383, 13);
		frame.getContentPane().add(lblFechaModificacionArticulo);
		
		btnEditarArticulo = new JButton("Editar Articulo");
		btnEditarArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnEditarArticulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnEditarArticulo.setBounds(163, 510, 121, 21);
		frame.getContentPane().add(btnEditarArticulo);
		
		JLabel lblId_1_1_2 = new JLabel("Palabras clave del Track:");
		lblId_1_1_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId_1_1_2.setBounds(578, 58, 247, 13);
		frame.getContentPane().add(lblId_1_1_2);
		
		lblPalabrasClaveTrackArticulo = new JLabel("");
		lblPalabrasClaveTrackArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPalabrasClaveTrackArticulo.setBounds(578, 81, 247, 13);
		frame.getContentPane().add(lblPalabrasClaveTrackArticulo);
		
		JLabel lblTrack = new JLabel("Track:");
		lblTrack.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTrack.setBounds(306, 33, 53, 13);
		frame.getContentPane().add(lblTrack);
		
		lblTituloTrackArticulo = new JLabel("");
		lblTituloTrackArticulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTituloTrackArticulo.setBounds(360, 33, 480, 13);
		frame.getContentPane().add(lblTituloTrackArticulo);
		
		JLabel lblId_1_1_1_1_1_1_1 = new JLabel("Estado actual:");
		lblId_1_1_1_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId_1_1_1_1_1_1_1.setBounds(304, 235, 102, 13);
		frame.getContentPane().add(lblId_1_1_1_1_1_1_1);
		
		 lblEstado = new JLabel("");
		lblEstado.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEstado.setBounds(408, 235, 145, 13);
		frame.getContentPane().add(lblEstado);
		
		JLabel lblId_1_1_1_1_1_1_1_1 = new JLabel("Decisión:");
		lblId_1_1_1_1_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId_1_1_1_1_1_1_1_1.setBounds(563, 235, 67, 13);
		frame.getContentPane().add(lblId_1_1_1_1_1_1_1_1);
		
		 lblDecision = new JLabel("");
		lblDecision.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDecision.setBounds(630, 235, 69, 13);
		frame.getContentPane().add(lblDecision);
		
		 btnVerDecision = new JButton("Ver Decisión");
		btnVerDecision.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnVerDecision.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnVerDecision.setBounds(709, 227, 121, 21);
		frame.getContentPane().add(btnVerDecision);
	}

	//Getters y Setters de las tablas
	public JTable getTableArticulosDelAutor() {
		return tableArticulosDelAutor;
	}

	public void setTableArticulosDelAutor(JTable tableArticulosDelAutor) {
		this.tableArticulosDelAutor = tableArticulosDelAutor;
	}

	public JTable getTableAutoresDelArticulo() {
		return tableAutoresDelArticulo;
	}

	public void setTableAutoresDelArticulo(JTable tableAutoresDelArticulo) {
		this.tableAutoresDelArticulo = tableAutoresDelArticulo;
	}


	//Getters y Setters de las labels
	public JLabel getLblAutorBusqueda() {
		return lblAutorBusqueda;
	}

	public void setLblAutorBusqueda(JLabel lblAutorBusqueda) {
		this.lblAutorBusqueda = lblAutorBusqueda;
	}

	public JLabel getLblIdArticulo() {
		return lblIdArticulo;
	}

	public void setLblIdArticulo(JLabel lblIdArticulo) {
		this.lblIdArticulo = lblIdArticulo;
	}

	public JLabel getLblTituloArticulo() {
		return lblTituloArticulo;
	}

	public void setLblTituloArticulo(JLabel lblTituloArticulo) {
		this.lblTituloArticulo = lblTituloArticulo;
	}
	
	public JLabel getLblTituloTrackArticulo() {
		return lblTituloTrackArticulo;
	}
	
	public void setLblTituloTrackArticulo(JLabel lblTituloTrackArticulo) {
		this.lblTituloTrackArticulo = lblTituloTrackArticulo;
	}

	public JLabel getLblPalabrasClaveArticulo() {
		return lblPalabrasClaveArticulo;
	}

	public void setLblPalabrasClaveArticulo(JLabel lblPalabrasClaveArticulo) {
		this.lblPalabrasClaveArticulo = lblPalabrasClaveArticulo;
	}
	
	public JLabel getLblPalabrasClaveTrackArticulo() {
		return lblPalabrasClaveTrackArticulo;
	}
	
	public void setLblPalabrasClaveTrackArticulo(JLabel lblPalabrasClaveTrackArticulo) {
		this.lblPalabrasClaveTrackArticulo = lblPalabrasClaveTrackArticulo;
	}

	public JLabel getLblResumenArticulo() {
		return lblResumenArticulo;
	}

	public void setLblResumenArticulo(JLabel lblResumenArticulo) {
		this.lblResumenArticulo = lblResumenArticulo;
	}

	public JLabel getLblFicheroArticulo() {
		return lblFicheroArticulo;
	}

	public void setLblFicheroArticulo(JLabel lblFicheroArticulo) {
		this.lblFicheroArticulo = lblFicheroArticulo;
	}

	public JLabel getLblFechaEnvioArticulo() {
		return lblFechaEnvioArticulo;
	}

	public void setLblFechaEnvioArticulo(JLabel lblFechaEnvioArticulo) {
		this.lblFechaEnvioArticulo = lblFechaEnvioArticulo;
	}
	
	public JLabel getLblFechaModificacionArticulo() {
		return lblFechaModificacionArticulo;
	}
	
	public void setLblFechaModificacionArticulo(JLabel lblFechaModificacionArticulo) {
		this.lblFechaModificacionArticulo = lblFechaModificacionArticulo;
	}
	
	public JLabel getLblEstado() {
		return lblEstado;
	}
	
	public void setLblEstado(JLabel lblEstado) {
		this.lblEstado = lblEstado;
	}
	
	public JLabel getLblDecision() {
		return lblDecision;
	}
	
	public void setLblDecision(JLabel lblDecision) {
		this.lblDecision = lblDecision;
	}
	
	public JButton getBtnVerDecision() {
		return btnVerDecision;
	}
	
	public void setBtnVerDecision(JButton btnVerDecision) {
		this.btnVerDecision = btnVerDecision;
	}

	public JLabel getLblEnviadoPorArticulo() {
		return lblEnviadoPorArticulo;
	}

	public void setLblEnviadoPorArticulo(JLabel lblEnviadoPorArticulo) {
		this.lblEnviadoPorArticulo = lblEnviadoPorArticulo;
	}

	public JFrame getFrame() {
		return frame;
	}
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
	public JCheckBox getChckbxSoloEnviadosPorMi() {
		return chckbxSoloEnviadosPorMi;
	}
	public void setChckbxSoloEnviadosPorMi(JCheckBox chckbxSoloEnviadosPorMi) {
		this.chckbxSoloEnviadosPorMi = chckbxSoloEnviadosPorMi;
	}
	
	public JButton getBtnEditarArticulo() {
		return btnEditarArticulo;
	}
	public void setBtnEditarArticulo(JButton btnEditarArticulo) {
		this.btnEditarArticulo = btnEditarArticulo;
	}
	
	
	
	//Añadir lista de articulos a la tabla de articulos
	public void agregarArticulo(int id, String titulo, String palabrasClave, String palabrasClaveTrack, String resumen, String nombreFichero) {
		DefaultTableModel model = (DefaultTableModel) tableArticulosDelAutor.getModel();
		//Añade la fila solo con el Id y el titulo
		model.addRow(new Object[] {id, titulo});		
	}

	// Limpiar la tabla de autores
	public void limpiarTablaAutores() {
		DefaultTableModel model = (DefaultTableModel) tableAutoresDelArticulo.getModel();
		model.setRowCount(0);
	}


	public void agregarAutor(String email, String nombre, String organizacion, String grupoInvestigacion) {
		DefaultTableModel model = (DefaultTableModel) tableAutoresDelArticulo.getModel();
		//Añade la fila con los datos del autor
		model.addRow(new Object[] {nombre, email, organizacion, grupoInvestigacion});
	}

	// Limpiar la tabla de articulos
	public void limpiarTablaArticulos() {
		DefaultTableModel model = (DefaultTableModel) tableArticulosDelAutor.getModel();
		model.setRowCount(0);
	}
	
	//Método para mostrar un mensaje de error sencillito
		public void mostrarMensajeError(String mensaje) {
			JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
		}
}
