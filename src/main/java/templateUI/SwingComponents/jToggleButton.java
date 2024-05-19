package templateUI.SwingComponents;

import javax.swing.*;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import java.awt.*;

public class jToggleButton extends JToggleButton {
    private Color selectedColor = null;

    public Color getSelectedColor() {
        return selectedColor;
    }
    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }


    public jToggleButton(String text, boolean selected) {
        super(text, selected);
        this.setUI(new CustomToggleButtonUI());
    }

    private class CustomToggleButtonUI extends BasicToggleButtonUI {
        public CustomToggleButtonUI() {}

        @Override
        protected void paintButtonPressed(Graphics g, AbstractButton b) {
            if (selectedColor != null) {
                g.setColor(selectedColor);
                g.fillRect(0, 0, b.getWidth(), b.getHeight());
            } else {
                super.paintButtonPressed(g, b);
            }
        }
    }
}
