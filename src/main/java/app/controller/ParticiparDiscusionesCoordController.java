package app.controller;

import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import app.dto.AccederDiscusionDTO;
import app.dto.AnotacionesDTO;
import app.dto.RevisionArticuloRevisionDTO;
import app.enums.DecisionRevisor;
import app.enums.Rol;
import app.model.ParticiparDiscusionesCoordModel;
import app.util.UserUtil;
import app.view.ParticiparDiscusionesCoordView;
import giis.demo.util.SwingUtil;

/**
 * Controller para la participación del coordinador en discusiones.
 * <p>
 * Permite al coordinador:
 * <ul>
 *   <li>Ver los artículos en discusión con discusión abierta.</li>
 *   <li>Seleccionar un artículo para ver sus anotaciones.</li>
 *   <li>Traer los revisores asociados al artículo y visualizar sus decisiones en los ComboBoxes.</li>
 *   <li>Activar el botón "Modificar" (para cambiar la decisión de un revisor) solo cuando el deadline de la discusión haya finalizado.</li>
 *   <li>Agregar anotaciones a la discusión.</li>
 * </ul>
 * </p>
 *
 * @see ParticiparDiscusionesCoordModel
 * @see ParticiparDiscusionesCoordView
 */
public class ParticiparDiscusionesCoordController {

    // Atributos de la clase
    private ParticiparDiscusionesCoordModel model;
    private ParticiparDiscusionesCoordView view;
    private String email;
    private String fecha; // Fecha actual en formato "yyyy-MM-dd"
    private DefaultListModel<AccederDiscusionDTO> listModel;
    private List<AccederDiscusionDTO> articulosDTO;
    private List<AnotacionesDTO> anotacionesDTO;
    private Map<Integer, List<AnotacionesDTO>> anotacionesArticulos = new HashMap<>();
    private List<RevisionArticuloRevisionDTO> currentRevisores; // revisores del artículo seleccionado
    private static final Rol ROL = Rol.COORDINADOR;

    /**
     * Constructor del controller.
     * <p>
     * Inicializa el controlador con el modelo, la vista y el correo del coordinador.
     * Llama al backend para cargar los artículos en discusión (con discusión abierta) y sus anotaciones,
     * y posteriormente inicializa la vista.
     * </p>
     *
     * @param m     Modelo que maneja la lógica de negocio de las discusiones.
     * @param v     Vista que presenta la información de los artículos y anotaciones.
     * @param email Correo electrónico del coordinador.
     */
    public ParticiparDiscusionesCoordController(ParticiparDiscusionesCoordModel m, ParticiparDiscusionesCoordView v,
            String email) {
        this.model = m;
        this.view = v;
        this.email = email;
        this.fecha = UserUtil.getFechaActual();

        // Detener la inicialización si el email es inválido.
        if (!UserUtil.checkEmail(email, ROL.getNombre(), model.getDbUtil())) {
            return;
        }

        // Obtener información de los artículos en discusión (con discusión abierta).
        if (!obtenerArticulos()) {
            return;
        }
        // Obtener las anotaciones asociadas a los artículos.
        if (!obtenerAnotaciones()) {
            return;
        }
        initView();
    }

    /**
     * Inicializa los listeners y comportamientos de la vista.
     * <p>
     * Configura los listeners para los botones (agregar nota, modificar decisión), para
     * la selección de un artículo en la lista y para enviar la anotación al pulsar Enter.
     * </p>
     */
    @SuppressWarnings("serial")
    public void initController() {
        // Botón de agregar nota.
        view.getBtnAgregarNota().addActionListener(e -> agregarNota());

        // Listener para la selección de un artículo.
        view.getListArticulos().addListSelectionListener(e -> {
            AccederDiscusionDTO articuloSeleccionado = view.getListArticulos().getSelectedValue();
            if (articuloSeleccionado == null || articuloSeleccionado.getIdArticulo() == 0) {
                return;
            }
            // Limpiar anotaciones.
            view.clearAnnotations();

            // Mostrar la decisión global (si fuera necesaria) y cargar anotaciones.
            // Se asume que el DTO ya tiene la información necesaria.
            anotacionesDTO = anotacionesArticulos.get(articuloSeleccionado.getIdArticulo());
            mostrarAnotaciones();

            // Obtener los revisores asociados al artículo.
            currentRevisores = model.getRevisoresArticulo(articuloSeleccionado.getIdArticulo());
            DefaultComboBoxModel<String> revisorModel = new DefaultComboBoxModel<>();
            if (currentRevisores != null && !currentRevisores.isEmpty()) {
                for (RevisionArticuloRevisionDTO rev : currentRevisores) {
                    String revisorDisplay = rev.getNombre() + " (" + rev.getEmail() + ")";
                    revisorModel.addElement(revisorDisplay);
                }
            }
            view.getComboRevisor().setModel(revisorModel);

            // Agregar listener al comboRevisor para actualizar el comboDecision según el revisor seleccionado.
            view.getComboRevisor().addActionListener((ActionEvent ev) -> {
                int idx = view.getComboRevisor().getSelectedIndex();
                if (idx >= 0 && currentRevisores != null && idx < currentRevisores.size()) {
                    RevisionArticuloRevisionDTO rev = currentRevisores.get(idx);
                    DecisionRevisor decision = DecisionRevisor.fromValue(rev.getDecisionRevisor());
                    view.getComboDecision().setSelectedItem(decision);
                }
            });

            // Habilitar el botón "Modificar" solo si el deadline de la discusión ha finalizado.
            // Se asume que el artículo (AccederDiscusionDTO) tiene el campo deadlineDiscusion.
            if (compareDates(articuloSeleccionado.getDeadlineDiscusion(), fecha) < 0) {
                view.getBtnModificarDecision().setEnabled(true);
            } else {
                view.getBtnModificarDecision().setEnabled(false);
            }
        });

        // Asignar acción de "enviar anotación" al pulsar Enter.
        view.getTextNuevaAnotacion().getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "sendNote");
        view.getTextNuevaAnotacion().getActionMap().put("sendNote", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarNota();
            }
        });
        // Permitir Shift+Enter para salto de línea.
        view.getTextNuevaAnotacion().getInputMap().put(KeyStroke.getKeyStroke("shift ENTER"),
                view.getTextNuevaAnotacion().getInputMap().get(KeyStroke.getKeyStroke("ENTER")));

        // Listener para el botón "Modificar" de decisión.
        view.getBtnModificarDecision().addActionListener(e -> modificarDecisionAction());
    }

    /**
     * Inicializa la vista haciéndola visible y asigna el modelo inicial al JList.
     */
    public void initView() {
        view.getFrame().setVisible(true);
        view.getListArticulos().setModel(listModel);
    }

    /**
     * Agrega una nueva anotación al artículo seleccionado.
     */
    private void agregarNota() {
        AccederDiscusionDTO articuloSeleccionado = view.getListArticulos().getSelectedValue();
        if (articuloSeleccionado == null || articuloSeleccionado.getIdArticulo() == 0) {
            SwingUtil.showMessage("Selecciona un artículo para agregar una anotación", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        String comentario = view.getTextNuevaAnotacion().getText();
        if (comentario.isEmpty()) {
            SwingUtil.showMessage("El comentario no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            model.agregarAnotacion(articuloSeleccionado.getIdArticulo(), email, comentario, fecha, UserUtil.getHoraActual());
            view.getTextNuevaAnotacion().setText("");
            anotacionesArticulos.clear();
            obtenerAnotaciones();
            anotacionesDTO = anotacionesArticulos.get(articuloSeleccionado.getIdArticulo());
            view.clearAnnotations();
            mostrarAnotaciones();
        } catch (Exception ex) {
            SwingUtil.showMessage("No se ha podido agregar la anotación\n" + ex, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra las anotaciones en la vista para el artículo seleccionado.
     */
    private void mostrarAnotaciones() {
        if (anotacionesDTO != null) {
            for (AnotacionesDTO a : anotacionesDTO) {
                view.addAnnotationCard(a.getEmailUsuario(), a.getFecha(), a.getHora(), a.getComentario());
            }
        } else {
            view.clearAnnotations();
        }
    }

    /**
     * Acción para modificar la decisión de un revisor.
     * <p>
     * Verifica que se haya seleccionado un artículo y un revisor, y que se haya elegido
     * una nueva decisión. Si todo es correcto, actualiza la decisión en la base de datos.
     * </p>
     */
    private void modificarDecisionAction() {
        AccederDiscusionDTO articuloSeleccionado = view.getListArticulos().getSelectedValue();
        if (articuloSeleccionado == null) {
            SwingUtil.showMessage("Selecciona un artículo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idx = view.getComboRevisor().getSelectedIndex();
        if (idx < 0 || currentRevisores == null || idx >= currentRevisores.size()) {
            SwingUtil.showMessage("Selecciona un revisor", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        RevisionArticuloRevisionDTO rev = currentRevisores.get(idx);
        DecisionRevisor nuevaDecision = (DecisionRevisor) view.getComboDecision().getSelectedItem();
        if (nuevaDecision == null) {
            SwingUtil.showMessage("Selecciona una nueva decisión", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        model.actualizarDecisionRevisor(articuloSeleccionado.getIdArticulo(), rev.getEmail(), nuevaDecision.getValue());
        rev.setDecisionRevisor(nuevaDecision.getValue());
        SwingUtil.showMessage("Decisión modificada correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Compara dos fechas en formato "yyyy-MM-dd".
     *
     * @param d1 Primera fecha.
     * @param d2 Segunda fecha.
     * @return Negativo si d1 es anterior a d2; 0 si son iguales; positivo si d1 es posterior a d2.
     */
    private int compareDates(String d1, String d2) {
        LocalDate date1 = LocalDate.parse(d1, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate date2 = LocalDate.parse(d2, DateTimeFormatter.ISO_LOCAL_DATE);
        return date1.compareTo(date2);
    }

    /**
     * Obtiene los artículos en discusión para el coordinador.
     * <p>
     * Se llama al modelo para obtener los artículos en discusión (con discusión abierta),
     * convierte los datos a DTOs y los agrega a un DefaultListModel para la vista.
     * </p>
     *
     * @return true si se obtuvieron los artículos; false en caso contrario.
     */
    private boolean obtenerArticulos() {
        List<AccederDiscusionDTO> articulos = model.getArticulos(email, fecha);
        if (articulos.isEmpty()) {
            SwingUtil.showMessage("No tienes ningún artículo que discutir", "Información", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        articulosDTO = new ArrayList<>();
        listModel = new DefaultListModel<>();
        for (AccederDiscusionDTO e : articulos) {
            // Se asume que AccederDiscusionDTO se ha actualizado para incluir deadlineDiscusion.
            AccederDiscusionDTO dto = new AccederDiscusionDTO(e.getIdArticulo(), e.getTitulo(), e.getDecisionRevisor(),
                    e.getMantenerseFirme(), e.getDeadlineDiscusion());
            articulosDTO.add(dto);
            listModel.addElement(dto);
        }
        return true;
    }

    /**
     * Obtiene las anotaciones asociadas a los artículos en discusión.
     * <p>
     * Se llama al modelo para obtener las anotaciones y se organizan en un mapa
     * con el id del artículo como clave.
     * </p>
     *
     * @return true si se obtuvieron las anotaciones; false en caso contrario.
     */
    private boolean obtenerAnotaciones() {
        List<AnotacionesDTO> anotaciones = model.getAnotaciones(email, fecha);
        if (anotaciones.isEmpty()) {
            SwingUtil.showMessage("No se han encontrado revisiones", "Información", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        for (AnotacionesDTO a : anotaciones) {
            if (anotacionesArticulos.containsKey(a.getIdArticulo())) {
                anotacionesArticulos.get(a.getIdArticulo()).add(a);
            } else {
                List<AnotacionesDTO> lista = new ArrayList<>();
                lista.add(a);
                anotacionesArticulos.put(a.getIdArticulo(), lista);
            }
        }
        return true;
    }

    /**
     * Actualiza el estado de los botones en función del filtro activo.
     *
     * @param filtro El filtro activo.
     */
    private void updateButtonsState(String filtro) {
        view.getBtnAgregarNota().setEnabled(true);
        view.getBtnModificarDecision().setEnabled(false);
        switch (filtro) {
            case "Cerradas":
                // Se habilitan botones específicos (según se requiera).
                break;
            case "Aptas para discusión":
                break;
            case "Abiertas":
                break;
            case "Abiertas firmes":
                break;
            case "Abiertas c/ deadline pasado":
                break;
            case "Abiertas sin anotaciones":
                break;
            default:
                break;
        }
    }
}
