package nos.pre.editor.UI.Welcome;

import nos.pre.editor.UI.Colors;

import javax.swing.*;
import java.awt.*;

public class BasicSettingsView extends JPanel {
    public BasicSettingsView() {
        super(new FlowLayout(), true);
        setBackground(Colors.welcomeMainBackground);

        this.addUIComponents();
    }

    private final JLabel settingsLabel = new JLabel("Settings");

    private void addUIComponents() {
        settingsLabel.setPreferredSize(new Dimension(200, 40));
        settingsLabel.setOpaque(true);
        settingsLabel.setBackground(Color.BLACK);
        settingsLabel.setForeground(Color.WHITE);
        settingsLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(settingsLabel);
    }
}
