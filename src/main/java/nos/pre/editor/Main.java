package nos.pre.editor;

import nos.pre.editor.processManagers.EditorProcess;

import javax.swing.SwingUtilities;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditorProcess(new File("E:/JavaProjects/PreEditor")).createEditorFrame());
    }

    // TODO: Call to 'printStackTrace()' should be replaced with more robust logging.
}
