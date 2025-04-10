package app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import app.dto.ArticuloDiscusionDTO;
import app.dto.RevisionArticuloRevisionDTO;
import app.enums.Rol;
import app.model.GestionarDiscusionesCoordinadorModel;
import app.view.GestionarDiscusionesCoordinadorView;
import giis.demo.util.SwingUtil;

/**
 * Controller para la gestión de discusiones a cargo del coordinador.
 * 
 * <p>
 * Esta clase gestiona la interacción entre la vista y el modelo en el contexto de la 
 * gestión de discusiones de artículos, permitiendo al coordinador:
 * <ul>
 *   <li>Visualizar los artículos aptos para discusión.</li>
 *   <li>Consultar las revisiones asociadas a cada artículo.</li>
 *   <li>Poner un artículo en discusión, notificando a los revisores correspondientes.</li>
 * </ul>
 * </p>
 * 
 * @see GestionarDiscusionesCoordinadorModel
 * @see GestionarDiscusionesCoordinadorView
 * @see ArticuloDiscusionDTO
 * @see RevisionArticuloRevisionDTO
 */
public class GestionarDiscusionesCoordinadorController {

    // Atributos de la clase
    private GestionarDiscusionesCoordinadorModel model;
    private GestionarDiscusionesCoordinadorView view;
    private String email;
    private DefaultListModel<ArticuloDiscusionDTO> listModel;
    private List<ArticuloDiscusionDTO> articulosDTO;
    private List<RevisionArticuloRevisionDTO> revisionesDTO;
    private Map<Integer, List<RevisionArticuloRevisionDTO>> revisionesArticulos = new HashMap<>();
    private static final Rol ROL = Rol.COORDINADOR;

    /**
     * Constructor del controller.
     * <p>
     * Inicializa el controlador con el modelo, la vista y el correo del coordinador.
     * Llama al backend para cargar los artículos aptos para discusión y sus revisiones asociadas,
     * y posteriormente inicializa la vista.
     * </p>
     *
     * @param m     Modelo que maneja la lógica de negocio de las discusiones.
     * @param v     Vista que presenta la información de los artículos y revisiones.
     * @param email Correo electrónico del coordinador.
     */
    public GestionarDiscusionesCoordinadorController(GestionarDiscusionesCoordinadorModel m,
            GestionarDiscusionesCoordinadorView v, String email) {
        this.model = m;
        this.view = v;
        this.email = email;

        // Cargar artículos aptos para discusión.
        if (!obtenerArticulos()) {
            return;
        }

        // Cargar revisiones asociadas a los artículos.
        if (!obtenerRevisiones()) {
            return;
        }

        // Inicializar la vista.
        this.initView();
    }

    /**
     * Configura los listeners de la vista.
     * <p>
     * Se configuran los siguientes listeners:
     * <ul>
     *   <li>Cerrar ventana ("Cerrar").</li>
     *   <li>Cerrar discusión ("Cerrar Discusión").</li>
     *   <li>Seleccionar un artículo para mostrar sus revisiones.</li>
     *   <li>Cambiar el filtro (JComboBox).</li>
     *   <li>Poner en discusión ("Poner en Discusión").</li>
     * </ul>
     * </p>
     */
    public void initController() {
        // Listener para cerrar la ventana.
        view.getBtnCerrar().addActionListener(e -> view.getFrame().dispose());
        
        // Listener para el botón "Enviar recordatorio".
        view.getBtnRecordatorio().addActionListener(e -> {
            SwingUtil.showMessage("Se ha enviado recordatorio a los revisores", "Información", JOptionPane.INFORMATION_MESSAGE);
        });

        // Listener para el botón "Cerrar Discusión".
        view.getBtnCerrarDiscusion().addActionListener(e -> {
            // Obtener el artículo seleccionado.
            ArticuloDiscusionDTO articulo = view.getListArticulos().getSelectedValue();
            if (articulo == null) {
                SwingUtil.showMessage("No se ha seleccionado ningún artículo", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Marcar discusión como cerrada.
            if (model.cerrarDiscusion(articulo.getIdArticulo())) {
                SwingUtil.showMessage("Discusión cerrada correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                
                // Actualizar listado según el filtro activo.
                String filtroActual = (String) view.getComboFiltro().getSelectedItem();
                List<ArticuloDiscusionDTO> listaActualizada = new ArrayList<>();
                switch (filtroActual) {
                    case "Cerradas":
                        listaActualizada = model.getArticulosCerrados();
                        break;
                    case "Aptas para discusión":
                        listaActualizada = model.getArticulosAptosDiscusion();
                        break;
                    case "Abiertas":
                        listaActualizada = model.getDiscusionAbierta();
                        break;
                    case "Abiertas firmes":
                        listaActualizada = model.getDiscusionAbiertaFirmes();
                        break;
                    case "Abiertas c/ deadline pasado":
                        listaActualizada = model.getDiscusionAbiertaDeadlinePasado();
                        break;
                    case "Abiertas sin anotaciones":
                        listaActualizada = model.getArticulosAbiertasSinAnotaciones();
                        break;
                    default:
                        break;
                }
                
                // Crear un nuevo modelo y asignarlo al JList.
                DefaultListModel<ArticuloDiscusionDTO> nuevoModelo = new DefaultListModel<>();
                for (ArticuloDiscusionDTO a : listaActualizada) {
                    nuevoModelo.addElement(a);
                }
                view.getListArticulos().setModel(nuevoModelo);
                
                // Limpiar revisiones y valoración.
                view.clearRevisiones();
                view.getLblValoracionGlobal().setText("");
            } else {
                SwingUtil.showMessage("Error al cerrar la discusión", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Listener para la selección de un artículo en el JList.
        view.getListArticulos().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            
            // Obtener el artículo seleccionado.
            ArticuloDiscusionDTO articuloSeleccionado = view.getListArticulos().getSelectedValue();
            if (articuloSeleccionado == null || articuloSeleccionado.getIdArticulo() == 0) {
                return;
            }
            
            // Limpiar el panel de revisiones.
            view.getPanelRevisiones().removeAll();
            
            // Mostrar la valoración del artículo.
            view.getLblValoracionGlobal().setText(Integer.toString(articuloSeleccionado.getValoracionGlobal()));
            
            // Obtener y mostrar las revisiones asociadas.
            revisionesDTO = revisionesArticulos.get(articuloSeleccionado.getIdArticulo());
            if (revisionesDTO != null) {
                for (RevisionArticuloRevisionDTO r : revisionesDTO) {
                    String decision = app.enums.DecisionRevisor.getLabelByValue(r.getDecisionRevisor());
                    view.addRevisionCard(r.getNombre(), r.getNivelExperto(), decision, r.getComentariosParaCoordinador());
                }
            } else {
                view.getPanelRevisiones().removeAll();
                view.getPanelRevisiones().revalidate();
                view.getPanelRevisiones().repaint();
            }
        });

        // Listener para el filtro del JComboBox.
        view.getComboFiltro().addActionListener(e -> {
            String selected = (String) view.getComboFiltro().getSelectedItem();
            
            // Limpiar revisiones y valoración.
            view.clearRevisiones();
            view.getLblValoracionGlobal().setText("");
            
            // Actualizar botones según el filtro.
            updateButtonsState(selected);
            
            // Actualizar el listado según el filtro.
            if ("Cerradas".equals(selected)) {
                List<ArticuloDiscusionDTO> articulosCerrados = model.getArticulosCerrados();
                DefaultListModel<ArticuloDiscusionDTO> closedModel = new DefaultListModel<>();
                if (articulosCerrados.isEmpty()) {
                    view.getListArticulos().setModel(closedModel);
                    view.clearRevisiones();	          
                } else {
                    for (ArticuloDiscusionDTO articulo : articulosCerrados) {
                        closedModel.addElement(articulo);
                    }
                    view.getListArticulos().setModel(closedModel);
                }
            } else if ("Aptas para discusión".equals(selected)) {
                view.getListArticulos().setModel(listModel);
            } else if ("Abiertas firmes".equals(selected)) {
                List<ArticuloDiscusionDTO> articulosFirmes = model.getDiscusionAbiertaFirmes();
                DefaultListModel<ArticuloDiscusionDTO> firmesModel = new DefaultListModel<>();
                if (articulosFirmes.isEmpty()) {
                    view.getListArticulos().setModel(firmesModel);
                    view.clearRevisiones();
                } else {
                    for (ArticuloDiscusionDTO articulo : articulosFirmes) {
                        firmesModel.addElement(articulo);
                    }
                    view.getListArticulos().setModel(firmesModel);
                }
            } else if ("Abiertas".equals(selected)) {
                List<ArticuloDiscusionDTO> articulosAbiertos = model.getDiscusionAbierta();
                DefaultListModel<ArticuloDiscusionDTO> abiertasModel = new DefaultListModel<>();
                if (articulosAbiertos.isEmpty()) {
                    view.getListArticulos().setModel(abiertasModel);
                    view.clearRevisiones();
                } else {
                    for (ArticuloDiscusionDTO articulo : articulosAbiertos) {
                        abiertasModel.addElement(articulo);
                    }
                    view.getListArticulos().setModel(abiertasModel);
                }
            } else if ("Abiertas c/ deadline pasado".equals(selected)) {
                List<ArticuloDiscusionDTO> articulosDeadlinePasado = model.getDiscusionAbiertaDeadlinePasado();
                DefaultListModel<ArticuloDiscusionDTO> deadlineModel = new DefaultListModel<>();
                if (articulosDeadlinePasado.isEmpty()) {
                    view.getListArticulos().setModel(deadlineModel);
                    view.clearRevisiones();
                } else {
                    for (ArticuloDiscusionDTO articulo : articulosDeadlinePasado) {
                        deadlineModel.addElement(articulo);
                    }
                    view.getListArticulos().setModel(deadlineModel);
                }
            } else if ("Abiertas sin anotaciones".equals(selected)) {
                // Se asume que en el modelo implementaste getArticulosAbiertasSinAnotaciones()
                List<ArticuloDiscusionDTO> articulosSinAnotaciones = model.getArticulosAbiertasSinAnotaciones();
                DefaultListModel<ArticuloDiscusionDTO> sinAnotacionesModel = new DefaultListModel<>();
                if (articulosSinAnotaciones.isEmpty()) {
                    view.getListArticulos().setModel(sinAnotacionesModel);
                    view.clearRevisiones();
                } else {
                    for (ArticuloDiscusionDTO articulo : articulosSinAnotaciones) {
                        sinAnotacionesModel.addElement(articulo);
                    }
                    view.getListArticulos().setModel(sinAnotacionesModel);
                }
            }
            
        });

        // Listener para el botón "Poner en Discusión".
        view.getBtnPonerEnDiscusion().addActionListener(e -> {
            // Obtener el artículo seleccionado.
            ArticuloDiscusionDTO articulo = view.getListArticulos().getSelectedValue();
            if (articulo == null) {
                SwingUtil.showMessage("No se ha seleccionado ningún artículo", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Llamar al backend para poner en discusión el artículo seleccionado.
            if (model.ponerEnDiscusion(articulo.getIdArticulo())) {
                SwingUtil.showMessage("Artículo puesto en discusión, se ha notificado a los revisores", "Información", JOptionPane.INFORMATION_MESSAGE);
                
                // Eliminar el artículo del modelo.
                listModel.removeElement(articulo);
                
                // Limpiar valoraciones y revisiones.
                view.getLblValoracionGlobal().setText("");
                view.getPanelRevisiones().removeAll();
            } else {
                SwingUtil.showMessage("No se ha podido poner en discusión el artículo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Inicializa la vista, haciéndola visible y estableciendo el modelo inicial del JList.
     */
    public void initView() {
    	view.getFrame().setVisible(true);
        view.getListArticulos().setModel(listModel);

        // Llama al método para actualizar el estado de los botones
        String filtroSeleccionado = (String) view.getComboFiltro().getSelectedItem();
        updateButtonsState(filtroSeleccionado);
    }

    /**
     * Obtiene las revisiones asociadas a los artículos aptos para discusión y las agrupa en un mapa
     * donde la clave es el id del artículo.
     *
     * @return true si se obtuvieron las revisiones correctamente; false en caso contrario.
     */
    private boolean obtenerRevisiones() {
        List<RevisionArticuloRevisionDTO> revisiones = model.getRevisionesArticulos();
        for (RevisionArticuloRevisionDTO r : revisiones) {
            if (revisionesArticulos.containsKey(r.getIdArticulo())) {
                revisionesArticulos.get(r.getIdArticulo()).add(r);
            } else {
                List<RevisionArticuloRevisionDTO> lista = new ArrayList<>();
                lista.add(r);
                revisionesArticulos.put(r.getIdArticulo(), lista);
            }
        }
        return true;
    }

    /**
     * Obtiene los artículos aptos para discusión y actualiza el modelo del JList.
     *
     * @return true si se obtuvieron los artículos correctamente; false en caso contrario.
     */
    private boolean obtenerArticulos() {
        List<ArticuloDiscusionDTO> articulos = model.getArticulosAptosDiscusion();
        articulosDTO = new ArrayList<>();
        for (ArticuloDiscusionDTO e : articulos) {
            ArticuloDiscusionDTO dto = new ArticuloDiscusionDTO(e.getIdArticulo(), e.getTitulo(), e.getValoracionGlobal());
            articulosDTO.add(dto);
        }
        listModel = new DefaultListModel<>();
        for (ArticuloDiscusionDTO dto : articulosDTO) {
            listModel.addElement(dto);
        }
        return true;
    }

    /**
     * Actualiza el estado de los botones de la vista en función del filtro activo.
     *
     * @param filtro El filtro activo.
     */
    private void updateButtonsState(String filtro) {
        view.getBtnPonerEnDiscusion().setEnabled(false);
        view.getBtnCerrar().setEnabled(false);
        view.getBtnAceptarArticulo().setEnabled(false);
        view.getBtnRechazarArticulo().setEnabled(false);
        view.getBtnRecordatorio().setEnabled(false);
        view.getBtnCerrarDiscusion().setEnabled(false);
        switch (filtro) {
            case "Cerradas":
                view.getBtnAceptarArticulo().setEnabled(true);
                view.getBtnRechazarArticulo().setEnabled(true);
                view.getBtnCerrar().setEnabled(true);
                break;
            case "Aptas para discusión":
                view.getBtnPonerEnDiscusion().setEnabled(true);
                view.getBtnCerrar().setEnabled(true);
                break;
            case "Abiertas":
                view.getBtnCerrar().setEnabled(true);
                break;
            case "Abiertas firmes":
                view.getBtnCerrarDiscusion().setEnabled(true);
                view.getBtnCerrar().setEnabled(true);
                break;
            case "Abiertas c/ deadline pasado":
                view.getBtnCerrarDiscusion().setEnabled(true);
                view.getBtnCerrar().setEnabled(true);
                break;
            case "Abiertas sin anotaciones":
                view.getBtnRecordatorio().setEnabled(true);
                view.getBtnCerrar().setEnabled(true);
                break;
            default:
                break;
        }
    }
}
