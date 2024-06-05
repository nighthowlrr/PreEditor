package nos.pre.editor.UI.Editor;

import nos.pre.editor.UI.Colors;
import nos.pre.editor.UI.Fonts;
import nos.pre.editor.languages.java.JavaSyntaxDocument;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.text.*;

public class EditingPane extends JTextPane {
    public EditingPane() {
        this.setDoubleBuffered(true);
        this.setBackground(Colors.editorBackground);
        this.setForeground(Colors.editorForeground);
        this.setFont(Fonts.SourceCodePro_Regular.deriveFont(14F));
        this.setCaretColor(Colors.editorCaretColor);
        this.setSelectionColor(Colors.editorSelectionColor);

        // To highlight the current line
        LinePainter linePainter = new LinePainter(this, Colors.editorCurrentLineHighlightColor);

        this.setStyledDocument(new JavaSyntaxDocument()); // TODO: Select LanguageDocument based on file opened.

        // TODO: Make Caret bigger for visibility
    }

    /**
     * Get the line the caret is currently on.
     * @param offset
     * @return The line on which caret is currently on.
     */
    private int getCaretLinePosition(int offset) {
        Document document = this.getDocument();
        Element map = document.getDefaultRootElement();
        return map.getElementIndex(offset);
    }

    /**
     * Get char position of caret in the specified line.
     * @param line The line in which caret is located.
     * @return The char position of caret in the specified line.
     */
    private int getLineStartOffset(int line) {
        Element map = this.getDocument().getDefaultRootElement();
        Element lineElem = map.getElement(line);
        return lineElem.getStartOffset();
    }

    /**
     * Returns a string in the format "line:position in line" for position of caret.
     * @param e The caretEvent to listen to
     * @return A string in the format "line:position in line" for position of caret.
     */
    public String getCaretLocationString(@NotNull CaretEvent e) {
        int dot = e.getDot();
        int line = getCaretLinePosition(dot);
        int posInLine = dot - getLineStartOffset(line);

        return (line + 1) + ":" + (posInLine + 1);
    }

    /* TODO: Use code in SyntaxDocument
     * Add a <code>DocumentFilter</code> to automatically close parentheses and quotation marks when users type.

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
    */
}
