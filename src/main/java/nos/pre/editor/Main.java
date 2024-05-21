package nos.pre.editor;

import nos.pre.editor.UI.Welcome.WelcomeFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WelcomeFrame();
        });
    }
}
