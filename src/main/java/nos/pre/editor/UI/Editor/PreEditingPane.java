package nos.pre.editor.UI.Editor;

import nos.pre.editor.UI.Colors;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.text.*;
import java.awt.*;

public class PreEditingPane extends JTextPane {
    public PreEditingPane() {
        this.setDoubleBuffered(true);
        this.setBackground(Colors.editorBackground);
        this.setForeground(Colors.editorForeground);
        this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        this.setCaretColor(Colors.editorCaretColor);
        this.setSelectionColor(Colors.editorSelectionColor);

        LinePainter linePainter = new LinePainter(this, Colors.editorCurrentLineHighlightColor);
        this.addFilter();
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
    public String getCaretLocationString(@NotNull CaretEvent e) {
        int dot = e.getDot();
        int line = getCaretLinePosition(dot);
        int posInLine = dot - getLineStartOffset(line);

        return (line + 1) + ":" + (posInLine + 1);
    }

    private void addFilter() {
        AbstractDocument abstractDocument;
        StyledDocument styledDocument = this.getStyledDocument();
        if (styledDocument instanceof AbstractDocument) {
            abstractDocument = (AbstractDocument) styledDocument;
            abstractDocument.setDocumentFilter(new DocumentFilter() {
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if (text.endsWith("{")) {
                        super.replace(fb, offset, length, text, attrs);
                        fb.insertString(offset + 1, "}", attrs);
                    } else if (text.endsWith("(")) {
                        super.replace(fb, offset, length, text, attrs);
                        fb.insertString(offset + 1, ")", attrs);
                    } else if (text.endsWith("[")) {
                        super.replace(fb, offset, length, text, attrs);
                        fb.insertString(offset + 1, "]", attrs);
                    } else if (text.equals("\"")) {
                        super.replace(fb, offset, length, text, attrs);
                        fb.insertString(offset + 1, "\"", attrs);
                    } else if (text.endsWith("'")) {
                        super.replace(fb, offset, length, text, attrs);
                        fb.insertString(offset + 1, "'", attrs);
                    } else {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
                // TODO: Don't auto-close when matching close symbol available
            });
        }
    }
}
