package app.controller;

import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import app.dto.ArticuloRevisionDTO;
import app.dto.AutorDTO;
import app.dto.RevisorDTO;
import app.enums.Rol;
import app.model.AsignarRevisoresModel;
import app.util.UserUtil;
import app.view.AsignarRevisoresView;
import giis.demo.util.SwingUtil;

public class AsignarRevisoresController {
    private AsignarRevisoresModel model;
    private AsignarRevisoresView view;
    private List<ArticuloRevisionDTO> articulos;
    private List<RevisorDTO> revisores;
    private boolean controllerIniciado;
    private static final Rol ROL = Rol.COORDINADOR;

    // Constructor
    public AsignarRevisoresController(AsignarRevisoresModel model, AsignarRevisoresView view, String email) {
        this.model = model;
        this.view = view;

        if (!UserUtil.checkEmail(email, ROL.getNombre(), model.getDbUtil())) {
            return;
        }

        if (!obtenerArticulosSinRevisores(true)) {
            return;
        }

        this.initView();
        if (!controllerIniciado) {
            this.initController();
        }
    }

    public void initView() {
        view.getFrame().setVisible(true);
    }

    /// Método para inicializar el controlador
    public void initController() {
        if (controllerIniciado) return;
        controllerIniciado = true;

        System.out.println("initController ejecutado");

        //Listener eventos de los botones
        view.getBtnAsignarRevisor().addActionListener(e -> SwingUtil.exceptionWrapper(() -> asignarRevisor()));
        view.getBtnEliminar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> eliminarRevisor()));

        view.getComboBoxAticulosAsignadosoNo().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                actualizarCombobox();
            } else {
                limpiarCampos();
            }
        });

        view.getComboArticulosNoAsignados().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                actualizarDetallesArticulo();
            } else {
                limpiarCampos();
            }
        });

        view.getComboBoxExperto().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                SwingUtil.exceptionWrapper(() -> {
                    int idx = view.getComboArticulosNoAsignados().getSelectedIndex();
                    if (idx != -1) {
                        int idArticulo = articulos.get(idx).getId();
                        obtenerRevisoresDisponibles(idArticulo);
                    }
                });
            }
        });
    }

    //metodo para actualizar el combobox de articulos
    private void actualizarCombobox() {
        int selectedIndex = view.getComboBoxAticulosAsignadosoNo().getSelectedIndex();
        view.getComboArticulosNoAsignados().removeAllItems();

        if (selectedIndex == 0) {
            obtenerArticulosSinRevisores(true);
        } else if (selectedIndex == 1) {
            obtenerArticulosSinRevisores(false);
        }
    }

    // Método para actualizar los detalles del artículo seleccionado
    private void actualizarDetallesArticulo() {
        int selectedIndex = view.getComboArticulosNoAsignados().getSelectedIndex();

        if (selectedIndex != -1) {
            ArticuloRevisionDTO articuloSeleccionado = articulos.get(selectedIndex);

            // Actualizar tabla de revisores disponibles con posible filtro
            obtenerRevisoresDisponibles(articuloSeleccionado.getId());

            // Actualizar tabla de autores
            DefaultTableModel modelAutores = (DefaultTableModel) view.getTableAutores().getModel();
            modelAutores.setRowCount(0);
            for (AutorDTO autor : articuloSeleccionado.getAutores()) {
                modelAutores.addRow(new Object[]{
                    autor.getEmail(),
                    autor.getNombre(),
                    autor.getOrganizacion(),
                    autor.getGrupoInvestigacion()
                });
            }

            view.getTxtPalabraClave().setText(articuloSeleccionado.getPalabrasClave());
            view.getTxtResumen().setText(articuloSeleccionado.getResumen());
            actualizarRevisoresSeleccionados(articuloSeleccionado.getId());
        }
    }
    // Método para obtener los artículos sin revisores
    private boolean obtenerArticulosSinRevisores(boolean verRevisiones) {
        if (!verRevisiones) {
            articulos = model.obtenerArticulosSinRevisores();
        } else {
            articulos = model.obtenerArticulosConRevisores();
        }

        view.getComboArticulosNoAsignados().removeAllItems();
        for (ArticuloRevisionDTO articulo : articulos) {
            String textoCombo = "ID: " + articulo.getId() + " - " + articulo.getTitulo() + " (" +
                articulo.getPalabrasClave() + ")";
            view.getComboArticulosNoAsignados().addItem(textoCombo);
        }

        actualizarDetallesArticulo();
        return true;
    }

    // Método para obtener los revisores disponibles para un artículo específico
    private void obtenerRevisoresDisponibles(int idArticulo) {
        // Obtener todos los revisores disponibles
        revisores = model.obtenerRevisoresDisponibles(idArticulo);

        // Obtener artículo actual para extraer keywords
        int idxArt = view.getComboArticulosNoAsignados().getSelectedIndex();
        ArticuloRevisionDTO articulo = articulos.get(idxArt);
        List<String> trackKeywords = Arrays.stream(
            articulo.getPalabrasClaveTrack().split(","))
            .map(String::trim)
            .collect(Collectors.toList());

        // Determinar filtro
        int filtro = view.getComboBoxExperto().getSelectedIndex();
        boolean filtrarExpertos = (filtro == 2);

        DefaultTableModel tableModel = (DefaultTableModel) view.getTableRevisoresDisponibles().getModel();
        tableModel.setRowCount(0);

        for (RevisorDTO revisor : revisores) {
            String preferencia = model.obtenerPreferenciaRevisor(revisor.getEmail(), idArticulo);
            if ("Conflicto".equalsIgnoreCase(preferencia)) {
                continue;
            }

            List<String> revisorKeywords = Arrays.stream(
                revisor.getPalabrasClave().split(","))
                .map(String::trim)
                .collect(Collectors.toList());

            long comunes = revisorKeywords.stream()
                .filter(trackKeywords::contains)
                .count();

            if (filtrarExpertos && comunes == 0) {
                continue;
            }

            tableModel.addRow(new Object[]{
                revisor.getEmail(),
                revisor.getNombre(),
                revisor.getOrganizacion(),
                revisor.getGrupoInvestigacion(),
                preferencia,
                comunes
            });
        }
    }

    // Método para asignar un revisor a un artículo
    private void asignarRevisor() {
        int selectedIndex = view.getComboArticulosNoAsignados().getSelectedIndex();
        if (selectedIndex == -1) {
            SwingUtil.showMessage("Seleccione un artículo para asignar un revisor.", "INFORMACIÓN",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idArticulo = articulos.get(selectedIndex).getId();
        List<RevisorDTO> revisoresAsignados = model.obtenerRevisoresDeArticulo(idArticulo);
        if (revisoresAsignados.size() >= 3) {
            SwingUtil.showMessage("No se pueden asignar más de 3 revisores a un artículo.", "Límite alcanzado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = view.getTableRevisoresDisponibles().getSelectedRow();
        if (selectedRow == -1) {
            SwingUtil.showMessage("Seleccione un revisor disponible.", "INFORMACIÓN",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String emailRevisor = (String) view.getTableRevisoresDisponibles().getValueAt(selectedRow, 0);
        model.asignarRevisor(idArticulo, emailRevisor);

        actualizarRevisoresSeleccionados(idArticulo);
        obtenerRevisoresDisponibles(idArticulo);

        SwingUtil.showMessage("Revisor asignado con éxito.", "INFORMACIÓN",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // Método para eliminar un revisor asignado a un artículo
    private void eliminarRevisor() {
        int selectedIndex = view.getComboArticulosNoAsignados().getSelectedIndex();
        if (selectedIndex == -1) {
            SwingUtil.showMessage("Seleccione un artículo para eliminar un revisor.", "INFORMACIÓN",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idArticulo = articulos.get(selectedIndex).getId();
        int selectedRow = view.getTableRevisoresSeleccionados().getSelectedRow();
        if (selectedRow == -1) {
            SwingUtil.showMessage("Seleccione un revisor asignado.", "INFORMACIÓN",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String emailRevisor = (String) view.getTableRevisoresSeleccionados().getValueAt(selectedRow, 0);
        model.eliminarRevisor(idArticulo, emailRevisor);

        actualizarRevisoresSeleccionados(idArticulo);
        obtenerRevisoresDisponibles(idArticulo);

        SwingUtil.showMessage("Revisor eliminado con éxito.", "INFORMACIÓN",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Método para actualizar la tabla de revisores seleccionados
    private void actualizarRevisoresSeleccionados(int idArticulo) {
        List<RevisorDTO> revisoresSeleccionados = model.obtenerRevisoresDeArticulo(idArticulo);
        DefaultTableModel tableModel = (DefaultTableModel) view.getTableRevisoresSeleccionados().getModel();
        tableModel.setRowCount(0);
        for (RevisorDTO revisor : revisoresSeleccionados) {
            tableModel.addRow(new Object[]{
                revisor.getEmail(),
                revisor.getNombre(),
                revisor.getOrganizacion(),
                revisor.getGrupoInvestigacion()
            });
        }
    }
    
    // Método para limpiar los campos de texto y tablas
    public void limpiarCampos() {
        view.getTxtPalabraClave().setText("");
        view.getTxtResumen().setText("");
        ((DefaultTableModel) view.getTableAutores().getModel()).setRowCount(0);
        ((DefaultTableModel) view.getTableRevisoresDisponibles().getModel()).setRowCount(0);
        ((DefaultTableModel) view.getTableRevisoresSeleccionados().getModel()).setRowCount(0);
    }
}
