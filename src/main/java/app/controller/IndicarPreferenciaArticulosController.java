package app.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import app.dto.ArticuloRevisionDTO;
import app.dto.PreferenciaDTO;
import app.enums.Rol;
import app.model.IndicarPreferenciaArticulosModel;
import app.util.UserUtil;
import app.view.IndicarPreferenciaArticulosView;

public class IndicarPreferenciaArticulosController {
	private IndicarPreferenciaArticulosModel model;
	private IndicarPreferenciaArticulosView view;
	private List<ArticuloRevisionDTO> articulos;
	private List<ArticuloRevisionDTO> articulosVisibles;
	private PreferenciaDTO preferencia;
	private String email;
	private static final Rol ROL = Rol.REVISOR;
	
	public IndicarPreferenciaArticulosController(IndicarPreferenciaArticulosModel model, IndicarPreferenciaArticulosView view, String email) {
		this.model = model;
		this.view = view;
		this.email = email;
	
		if (!UserUtil.checkEmail(email, ROL.getNombre(), model.getDbUtil())) {
			// Detener la inicialización si el email es inválido.
			return;
		}
		
		// Obtener los artículos del track del revisor.
		if(!obtenerArticulosTrackRevisor(email)) {
			// Detener la inicialización si no se pudieron obtener los artículos.
			return;
		}
		// después de obtenerArticulosTrackRevisor(email)
		this.articulosVisibles = new ArrayList<>(articulos);

		
		this.initView();
		this.initController();
		
	
	}
	// Método para inicializar la vista
	public void initView() {
		view.getFrame().setVisible(true);
	}
	
	public void initController() {
		
		
		// Evento cuando se selecciona un artículo en el ComboBox
		view.getComboBoxArticulo().addItemListener(e -> {
			if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
				actualizarCombobox();
			}
		});
		
		view.getComboBoxPreferencia().addActionListener(e -> {
		    if (view.getComboBoxPreferencia().getSelectedIndex() <= 0) return; // evita guardar la opción por defecto

		    int index = view.getComboBoxArticulo().getSelectedIndex();
		    if (index < 0) return;

		    ArticuloRevisionDTO articulo = articulos.get(index);
		    String seleccion = (String) view.getComboBoxPreferencia().getSelectedItem();

		    model.guardarOActualizarPreferencia(articulo.getId(), email, seleccion);
		});
		
		
		// filtro experto
		view.getComboBoxExperto().addActionListener(e -> {
		    String sel = (String) view.getComboBoxExperto().getSelectedItem();
		    if ("En los que soy experto".equals(sel)) {
		        filtrarPorExperiencia();
		    } else {
		        // "-- Selecciona filtro --" y "Todos los artículos"
		        cargarComboArticulos(articulos);
		    }
		});

	}
	
	

	//Metodo para obtener los articulos del track del revisor
	private boolean obtenerArticulosTrackRevisor(String email) {
		articulos = model.obtenerArticulosTrackRevisor(email);
		articulosVisibles = new ArrayList<>(articulos);
	
		// Limpiar ComboBox antes de llenarlo
		view.getComboBoxArticulo().removeAllItems();
	
	    // Cargar artículos con ID y palabras clave en el ComboBox
		cargarComboArticulos(articulosVisibles);
	
	    return true;
		
	}
	
	// Método para actualizar el ComboBox 
	private void actualizarCombobox() {
	    int index = view.getComboBoxArticulo().getSelectedIndex();
	    if (index < 0 || index >= articulos.size()) return;

	    ArticuloRevisionDTO articulo = articulos.get(index);
	    preferencia = model.obtenerPreferenciaDTO(articulo.getId(), email);

	    if (preferencia != null && preferencia.getPreferencia() != null) {
	        for (int i = 0; i < view.getComboBoxPreferencia().getItemCount(); i++) {
	            String item = view.getComboBoxPreferencia().getItemAt(i).trim();
	            if (item.equalsIgnoreCase(preferencia.getPreferencia().trim())) {
	                view.getComboBoxPreferencia().setSelectedIndex(i);
	                return;
	            }
	        }
	    }

	    // Si no hay preferencia previa → dejar en la primera opción
	    view.getComboBoxPreferencia().setSelectedIndex(0);
	}
	
	
	private void cargarComboArticulos(List<ArticuloRevisionDTO> lista) {
	    articulosVisibles = lista;
	    view.getComboBoxArticulo().removeAllItems();
	    for (ArticuloRevisionDTO art : lista) {
	    	String kw = art.getPalabrasClaveTrack();
	    	kw = (kw == null ? "" : kw);
	    	String texto = "ID: " + art.getId() +
	    	               " - " + art.getTitulo() +
	    	               " (" + kw + ")";

	        view.getComboBoxArticulo().addItem(texto);
	    }
	}


	private void filtrarPorExperiencia() {
	    List<String> userKw = model.obtenerPalabrasClaveUsuario(email);
	    if (userKw.isEmpty()) {
	        cargarComboArticulos(Collections.emptyList());
	        return;
	    }
	    List<ArticuloRevisionDTO> expertos = articulos.stream()
	        .filter(art -> {
	            String text = art.getPalabrasClaveTrack();
	            // <-- nueva comprobación:
	            if (text == null || text.trim().isEmpty()) return false;
	            for (String clave : text.split("\\s*,\\s*")) {
	                if (userKw.contains(clave.trim())) return true;
	            }
	            return false;
	        })
	        .collect(Collectors.toList());
	    cargarComboArticulos(expertos);
	}


	
}

	
