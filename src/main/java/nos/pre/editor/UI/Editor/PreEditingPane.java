package nos.pre.editor.UI.Editor;

import nos.pre.editor.UI.Colors;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.Element;
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

    private int getCaretLinePosition(int offset) {
        Document document = this.getDocument();
        Element map = document.getDefaultRootElement();
        return map.getElementIndex(offset);
    }
    private int getLineStartOffset(int line) {
        Element map = this.getDocument().getDefaultRootElement();
        Element lineElem = map.getElement(line);
        return lineElem.getStartOffset();
    }
    public void getCaretLocationOnJLabel(JLabel label) {
        this.addCaretListener(e -> {
            int dot = e.getDot();
            int line = getCaretLinePosition(dot);
            int posInLine = dot - getLineStartOffset(line);

            label.setText((line + 1) + ":" + (posInLine + 1));
        });
    }
}