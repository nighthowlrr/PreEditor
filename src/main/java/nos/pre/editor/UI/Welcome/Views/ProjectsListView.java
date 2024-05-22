package nos.pre.editor.UI.Welcome.Views;

import nos.pre.editor.UI.Colors;

import javax.swing.*;
import java.awt.*;

public class ProjectsListView extends JPanel {
    public ProjectsListView() {
        super(new FlowLayout(), true);
        setBackground(Colors.welcomeMainBackground);

        this.addUIComponents();
    }

    private final JLabel projectLabel = new JLabel("Projects");

    private void addUIComponents() {
        projectLabel.setPreferredSize(new Dimension(200, 40));
        projectLabel.setOpaque(true);
        projectLabel.setBackground(Color.BLACK);
        projectLabel.setForeground(Color.WHITE);
        projectLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(projectLabel);
    }
}
