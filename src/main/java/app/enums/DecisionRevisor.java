package app.enums;

import java.awt.Color;

/**
 * Enum que representa las decisiones del revisor con su respectiva etiqueta, valor numérico y color asociado.
 * <p>
 * Las decisiones posibles son:
 * <ul>
 *     <li>{@code ACEPTAR_FUERTE}: "Aceptar Fuerte" con valor 2 y color verde.</li>
 *     <li>{@code ACEPTAR_DEBIL}: "Aceptar Débil" con valor 1 y color verde.</li>
 *     <li>{@code RECHAZAR_DEBIL}: "Rechazar Débil" con valor -1 y color rojo oscuro.</li>
 *     <li>{@code RECHAZAR_FUERTE}: "Rechazar Fuerte" con valor -2 y color rojo oscuro.</li>
 * </ul>
 * </p>
 */
public enum DecisionRevisor {
	
	ACEPTAR_FUERTE("Aceptar Fuerte", 2, new Color(0, 128, 0)),
    ACEPTAR_DEBIL("Aceptar Débil", 1, new Color(0, 128, 0)),
    RECHAZAR_DEBIL("Rechazar Débil", -1, new Color(178, 34, 34)),
    RECHAZAR_FUERTE("Rechazar Fuerte", -2, new Color(178, 34, 34));

	private final String label;
    private final int value;
    private final Color color;

    /**
     * Constructor del enum {@code DecisionRevisor}.
     *
     * @param label  Etiqueta legible de la decisión.
     * @param value  Valor numérico asociado a la decisión.
     * @param color  Color asociado a la decisión.
     */
    DecisionRevisor(String label, int value, Color color) {
        this.label = label;
        this.value = value;
        this.color = color;
    }
   
    /**
     * Obtiene el valor numérico asociado a la decisión.
     *
     * @return Valor numérico de la decisión.
     */
    public int getValue() {
        return value;
    }

    /**
     * Obtiene la etiqueta legible de la decisión.
     *
     * @return Etiqueta legible de la decisión.
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * Obtiene el color asociado a la decisión.
     *
     * @return Color asociado a la decisión.
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Busca y retorna la instancia de {@code DecisionRevisor} correspondiente a la etiqueta proporcionada.
     *
     * @param label Etiqueta de la decisión a buscar.
     * @return La instancia de {@code DecisionRevisor} correspondiente o {@code null} si no se encuentra.
     */
    public static DecisionRevisor fromLabel(String label) {
        for (DecisionRevisor d : values()) {
            if (d.getLabel().equalsIgnoreCase(label)) {
                return d;
            }
        }
        return null;
    }
    
    /**
     * Obtiene la etiqueta asociada a un valor numérico de la decisión.
     *
     * @param value Valor numérico de la decisión.
     * @return Etiqueta correspondiente al valor proporcionado o {@code null} si no se encuentra.
     */
    public static String getLabelByValue(int value) {
        for (DecisionRevisor d : values()) {
            if (d.getValue() == value) {
                return d.getLabel();
            }
        }
        return null;
    }
    
    /**
     * Retorna la instancia de DecisionRevisor que coincide con el valor numérico proporcionado.
     * 
     * @param value Valor numérico de la decisión.
     * @return Instancia de DecisionRevisor correspondiente o null si no coincide.
     */
    public static DecisionRevisor fromValue(int value) {
        for (DecisionRevisor d : values()) {
            if (d.getValue() == value) {
                return d;
            }
        }
        return null;
    }


    /**
     * Devuelve la representación en cadena de la decisión.
     *
     * @return La etiqueta legible de la decisión.
     */
    @Override
    public String toString() {
        return label;
    }
}
