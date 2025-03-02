package app.enums;

import java.awt.Color;

/**
 * Enum que representa la decisión final de un artículo y su color asociado.
 */
public enum DecisionFinal {
    ACEPTADO("Aceptado", new Color(0, 128, 0)),
    RECHAZADO("Rechazado", new Color(178, 34, 34));

    private final String label;
    private final Color color;

    /**
     * Constructor del enum.
     *
     * @param label Etiqueta legible de la decisión.
     * @param color Color asociado a la decisión.
     */
    DecisionFinal(String label, Color color) {
        this.label = label;
        this.color = color;
    }

    /**
     * Obtiene la etiqueta legible de la decisión.
     *
     * @return La etiqueta.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Obtiene el color asociado a la decisión.
     *
     * @return El color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Busca y retorna la instancia de DecisionFinal correspondiente a la etiqueta dada.
     *
     * @param label Etiqueta de la decisión.
     * @return La instancia de DecisionFinal si se encuentra; null en caso contrario.
     */
    public static DecisionFinal fromLabel(String label) {
        for (DecisionFinal df : values()) {
            if (df.getLabel().equalsIgnoreCase(label)) {
                return df;
            }
        }
        return null;
    }
}
