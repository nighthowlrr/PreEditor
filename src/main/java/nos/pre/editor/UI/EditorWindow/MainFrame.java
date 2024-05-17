package nos.pre.editor.UI.EditorWindow;

import nos.pre.editor.UI.Editor.EditorView;
import nos.pre.editor.UI.Welcome.WelcomeFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainFrame extends JFrame {
    // TODO: Launch from and exit to welcome frame

    private final JPanel mainContentPanel = new JPanel(new BorderLayout(), true);
    public MainFrame() {
        super("PreEditor");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListener() {
            @Override public void windowOpened(WindowEvent e) {}
            @Override public void windowClosing(WindowEvent e) {
                showExitDialog();
            }
            @Override public void windowClosed(WindowEvent e) {}
            @Override public void windowIconified(WindowEvent e) {}
            @Override public void windowDeiconified(WindowEvent e) {}
            @Override public void windowActivated(WindowEvent e) {}
            @Override public void windowDeactivated(WindowEvent e) {}
        });

        setContentPane(mainContentPanel);

        addUIComponents();

        setVisible(true);
    }

    private void addUIComponents() {
        mainContentPanel.add(new EditorView(), BorderLayout.CENTER);
    }

    private void showExitDialog() {
        // TODO: Custom UI for dialog
        int exitOption = JOptionPane.showInternalOptionDialog(null,
                "Are you sure you want to exit?", "Confirm Exit",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Exit", "Close Project", "Cancel"}, "Exit");
        if (exitOption == JOptionPane.YES_OPTION) {
            this.dispose();
            System.exit(0);
        } else if (exitOption == JOptionPane.NO_OPTION) {
            this.dispose();
            new WelcomeFrame();
        } else if (exitOption == JOptionPane.CANCEL_OPTION) {}
    }
}
