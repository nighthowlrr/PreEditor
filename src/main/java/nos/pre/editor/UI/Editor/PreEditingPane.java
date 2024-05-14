package nos.pre.editor.UI.Editor;

import nos.pre.editor.UI.Colors;

import javax.swing.*;
import java.awt.*;

public class PreEditingPane extends JTextPane {
    public PreEditingPane() {
        this.setDoubleBuffered(true);
        this.setBackground(Colors.editorBackground);
        this.setForeground(Colors.editorForeground);
        this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        this.setCaretColor(Colors.editorCaretColor);
        this.setSelectionColor(Colors.editorSelectionColor);
    }
}
