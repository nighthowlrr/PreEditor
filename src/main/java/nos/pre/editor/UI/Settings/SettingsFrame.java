package nos.pre.editor.UI.Settings;

import nos.pre.editor.defaultValues.UIColors;
import nos.pre.editor.defaultValues.UIFonts;
import templateUI.SwingComponents.jTabbedPane;

import javax.swing.*;
import java.awt.*;

public class SettingsFrame extends JFrame {
    private final JPanel mainContentPanel = new JPanel(new BorderLayout(), true);

    public SettingsFrame() {
        super("PreEditor Settings");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.mainContentPanel.setBackground(UIColors.SETTINGS_FRAME_MAIN_BG);
        setContentPane(this.mainContentPanel);

        addUIComponents();

        setVisible(true);
    }


    private final jTabbedPane tabbedPane = new jTabbedPane(false);

    private void addUIComponents() {
        tabbedPane.setBackground(UIColors.SETTINGS_FRAME_MAIN_BG);
        tabbedPane.setForeground(UIColors.SETTINGS_FRAME_MAIN_FG);
        tabbedPane.setFont(UIFonts.SETTINGS_FRAME_MAIN_FONT);
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);

        this.add(tabbedPane, BorderLayout.CENTER);
    }
}
