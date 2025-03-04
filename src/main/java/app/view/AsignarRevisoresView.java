package app.view;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

public class AsignarRevisoresView {

	// Crear las variables de la vista
	private JFrame frame;
	private JPanel contentPane;
	private JComboBox<String> comboArticulosNoAsignados;
	private JTextField txtPalabraClave;
	private JTextArea txtResumen;
	private JTable tableAutores;
	private JTable tableRevisoresDisponibles;
	private JButton btnAsignarRevisor;
	private JTable tableRevisoresSeleccionados;
	private JComboBox<String> comboBoxAticulosAsignadosoNo;
	private JButton btnEliminar;
	/**
	 * Create the frame.
	 */
	public AsignarRevisoresView() {
		initialize();
		
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Asignar Revisores");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setBounds(100, 100, 1000, 600);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		frame.setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[::250px,grow][grow]", "[][][grow][100px:100px:150px,grow][][100px:100px:150px,grow][][][100px:100px:150px,grow][]"));
		
		comboBoxAticulosAsignadosoNo = new JComboBox<String>();
		comboBoxAticulosAsignadosoNo.setMaximumRowCount(2);
		comboBoxAticulosAsignadosoNo.setModel(new DefaultComboBoxModel<String>(new String[] {"Artículos asignados", "Artículos no asigandos"}));
		contentPane.add(comboBoxAticulosAsignadosoNo, "cell 0 0,alignx right");
		
		comboArticulosNoAsignados = new JComboBox<String>();
		comboArticulosNoAsignados.setModel(new DefaultComboBoxModel<String>());
		contentPane.add(comboArticulosNoAsignados, "cell 1 0,growx");
		
		JLabel lblPC = new JLabel("Palabras clave:");
		contentPane.add(lblPC, "cell 0 1,alignx trailing");
		
		txtPalabraClave = new JTextField();
		txtPalabraClave.setEditable(false);
		txtPalabraClave.setText("palabra 1, palabra2 , palabra 3");
		contentPane.add(txtPalabraClave, "cell 1 1,growx");
		txtPalabraClave.setColumns(10);
		
		JLabel lblResumen = new JLabel("Resumen:");
		contentPane.add(lblResumen, "cell 0 2,alignx right,aligny top");
		
		txtResumen = new JTextArea();
		txtResumen.setEditable(false);
		txtResumen.setText("resumen");
		contentPane.add(txtResumen, "cell 1 2,grow");
		
		JLabel lblLA = new JLabel("Lista de autores:");
		contentPane.add(lblLA, "cell 0 3,alignx right,aligny top");
		
		JScrollPane scrollPane_LA = new JScrollPane();
		contentPane.add(scrollPane_LA, "cell 1 3,growx,aligny top");
		
		tableAutores = new JTable();
		tableAutores.setEnabled(false);
		tableAutores.setModel(new DefaultTableModel(
			null,
			new String[] {
					"Email", "Nombre", "Organizacion", "Grupo de Investigacion"
			}
		));
		tableAutores.setToolTipText("");
		tableAutores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_LA.setViewportView(tableAutores);
		
		JLabel lblLRD = new JLabel("Lista de revisores disponibles:");
		contentPane.add(lblLRD, "cell 0 5,alignx trailing,aligny top");
		
		JScrollPane scrollPane_LRD = new JScrollPane();
		contentPane.add(scrollPane_LRD, "cell 1 5,grow");
		
		tableRevisoresDisponibles = new JTable();
		tableRevisoresDisponibles.setModel(new DefaultTableModel(
			    new Object[][] {},
			    new String[] {
			        "Email", "Nombre", "Organización", "Grupo de Investigación"
			    }
			) {
		    /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // Hace que todas las celdas sean NO editables
		    }
		});
		tableRevisoresDisponibles.getColumnModel().getColumn(1).setResizable(false);
		scrollPane_LRD.setViewportView(tableRevisoresDisponibles);
		
		// --- Botón para anyadir revisor ---
		btnAsignarRevisor = new JButton("Añadir");
		contentPane.add(btnAsignarRevisor, "cell 1 6");
		//---------------------------------------------------------------------
		
		JLabel lblLRS = new JLabel("Lista de revisores es seleccionados:");
		contentPane.add(lblLRS, "cell 0 8,alignx trailing,aligny top");
		
		JScrollPane scrollPane_LRS = new JScrollPane();
		contentPane.add(scrollPane_LRS, "cell 1 8,grow");
		
		tableRevisoresSeleccionados = new JTable();
		tableRevisoresSeleccionados.setModel(new DefaultTableModel(
			    new Object[][] {},
			    new String[] {
			        "Email", "Nombre", "Organización", "Grupo de Investigación"
			    }
			) {
		    /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false; // Hace que todas las celdas sean NO editables
		    }
		});
		scrollPane_LRS.setViewportView(tableRevisoresSeleccionados);
		
		btnEliminar = new JButton("Eliminar");
		contentPane.add(btnEliminar, "cell 1 9");
		
		
		
	}
	

	//getters y setters

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public void setContentPane(JPanel contentPane) {
		this.contentPane = contentPane;
	}

	public JComboBox<String> getComboArticulosNoAsignados() {
		return comboArticulosNoAsignados;
	}

	public void setComboArticulosNoAsignados(JComboBox<String> comboArticulosNoAsignados) {
		this.comboArticulosNoAsignados = comboArticulosNoAsignados;
	}

	public JTextField getTxtPalabraClave() {
		return txtPalabraClave;
	}

	public void setTxtPalabraClave(JTextField txtPalabraClave) {
		this.txtPalabraClave = txtPalabraClave;
	}

	public JTextArea getTxtResumen() {
		return txtResumen;
	}

	public void setTxtResumen(JTextArea txtResumen) {
		this.txtResumen = txtResumen;
	}

	public JTable getTableAutores() {
		return tableAutores;
	}

	public void setTableAutores(JTable tableAutores) {
		this.tableAutores = tableAutores;
	}

	public JTable getTableRevisoresDisponibles() {
		return tableRevisoresDisponibles;
	}

	public void setTableRevisoresDisponibles(JTable tableRevisoresDisponibles) {
		this.tableRevisoresDisponibles = tableRevisoresDisponibles;
	}

	public JButton getBtnAsignarRevisor() {
		return btnAsignarRevisor;
	}

	public void setBtnAsignarRevisor(JButton btnAsignarRevisor) {
		this.btnAsignarRevisor = btnAsignarRevisor;
	}

	public JTable getTableRevisoresSeleccionados() {
		return tableRevisoresSeleccionados;
	}

	public void setTableRevisoresSeleccionados(JTable tableRevisoresSeleccionados) {
		this.tableRevisoresSeleccionados = tableRevisoresSeleccionados;
	}

	public JComboBox<String> getComboBoxAticulosAsignadosoNo() {
		return comboBoxAticulosAsignadosoNo;
	}

	public void setComboBoxAticulosAsignadosoNo(JComboBox<String> comboBoxAticulosAsignadosoNo) {
		this.comboBoxAticulosAsignadosoNo = comboBoxAticulosAsignadosoNo;
	}

	public JButton getBtnEliminar() {
		return btnEliminar;
	}

	public void setBtnEliminar(JButton btnEliminar) {
		this.btnEliminar = btnEliminar;
	}
	
	
	
	
}
