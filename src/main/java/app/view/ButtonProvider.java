package app.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class ButtonProvider {
    private String text;
    private ActionListener listener;

    public ButtonProvider(String text, ActionListener listener) {
        this.text = text;
        this.listener = listener;
    }

    public String getText() {
        return text;
    }

    public ActionListener getListener() {
        return listener;
    }
    
    // Método auxiliar para crear el botón con propiedades comunes
    public JButton createButton() {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        button.addActionListener(listener);
        return button;
    }
}
