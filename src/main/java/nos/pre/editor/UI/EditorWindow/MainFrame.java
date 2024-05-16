package nos.pre.editor.UI.EditorWindow;

import nos.pre.editor.UI.Editor.EditorView;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    // TODO: Launch from and exit to welcome frame

    private final JPanel mainContentPanel = new JPanel(new BorderLayout(), true);
    public MainFrame() {
        super("PreEditor");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(mainContentPanel);

        addUIComponents();

        setVisible(true);
    }

    private void addUIComponents() {
        mainContentPanel.add(new EditorView(), BorderLayout.CENTER);
    }
}
