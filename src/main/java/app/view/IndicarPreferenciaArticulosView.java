package app.view;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;


public class IndicarPreferenciaArticulosView  {

	private JFrame frame;
	private JPanel contentPane;
	private JComboBox<String> comboBoxArticulo;
	private JComboBox<String> comboBoxPreferencia;
	private JComboBox<String> comboBoxExperto;

	
	public IndicarPreferenciaArticulosView() {
		initialize();
	}
	/**
	 * Create the frame.
	 * @return 
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setTitle("Indicar Preferencia");
		frame.setBounds(100, 100, 450, 300);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		frame.setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[::30px,grow][][::30px,grow][::30px,grow][grow]"));
		
		JLabel lblNewLabel = new JLabel("Artículos");
		contentPane.add(lblNewLabel, "cell 0 0");
		
		comboBoxExperto = new JComboBox<String>();
		comboBoxExperto.setModel(new DefaultComboBoxModel<String>(new String[] {
			    "-- Selecciona filtro --",
			    "Todos los artículos",
			    "En los que soy experto"
			}));
		contentPane.add(comboBoxExperto, "cell 0 1,growx");
		
		comboBoxArticulo = new JComboBox<String>();
		contentPane.add(comboBoxArticulo, "cell 0 2,growx");
		
		JLabel lblNewLabel_1 = new JLabel("Preferencia:");
		contentPane.add(lblNewLabel_1, "cell 0 3,alignx left");
		
		comboBoxPreferencia = new JComboBox<String>();
		comboBoxPreferencia.setModel(new DefaultComboBoxModel<String>(new String[] {
			    "-- Selecciona preferencia --",
			    "Lo quiero revisar",
			    "No quiero revisar",
			    "Puedo revisar",
			    "Conflicto"
			}));
		contentPane.add(comboBoxPreferencia, "cell 0 4,growx");
		
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
	public JComboBox<String> getComboBoxArticulo() {
		return comboBoxArticulo;
	}
	public void setComboBoxArticulo(JComboBox<String> comboBoxArticulo) {
		this.comboBoxArticulo = comboBoxArticulo;
	}
	public JComboBox<String> getComboBoxPreferencia() {
		return comboBoxPreferencia;
	}
	public void setComboBoxPreferencia(JComboBox<String> comboBoxPreferencia) {
		this.comboBoxPreferencia = comboBoxPreferencia;
	}
	public JComboBox<String> getComboBoxExperto() {
	    return comboBoxExperto;
	}

	
	

}
