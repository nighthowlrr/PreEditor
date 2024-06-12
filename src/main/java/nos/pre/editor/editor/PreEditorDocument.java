package nos.pre.editor.editor;

import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import java.util.Objects;

public abstract class PreEditorDocument extends DefaultStyledDocument {
    public static final String INDENT_STYLE_USE_TABS = "tabsPolicy_useTabs";
    public static final String INDENT_STYLE_USE_SPACES = "tabsPolicy_useSpaces";

    private int indentSize;
    private String indentStyle;

    public PreEditorDocument() {
        setDefaultValues();

        this.putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
    }

    private void setDefaultValues() {
        setIndentSize(4);
        setIndentStyle(INDENT_STYLE_USE_SPACES);
        // TODO: get defaults from settings
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        // if (str.equals("{")) str = addMatchingBrace(offset);

        // Managing tabs
        if (Objects.equals(indentStyle, INDENT_STYLE_USE_SPACES)) {
            str = str.replaceAll("\t", " ".repeat(indentSize));
        } // TODO: else if (Objects.equals(indentStyle, INDENT_STYLE_USE_TABS))

        super.insertString(offs, str, a);

        // Syntax-Highlighting method
        processChangedLines(offs, str.length());
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        super.remove(offs, len);

        processChangedLines(offs, 0);
    }

    @Override
    protected void fireInsertUpdate(DocumentEvent e) {
        super.fireInsertUpdate(e);

        processChangedLines(e.getOffset(), e.getLength());
    }

    @Override
    protected void fireRemoveUpdate(DocumentEvent e) {
        super.fireRemoveUpdate(e);

        processChangedLines(e.getOffset(), e.getLength());
    }

    // TODO: Add auto-bracket closing here

    /**
     * Method to contain code for processing all lines changed, then do something. Made for Syntax Highlighting.
     * <p>Called after:
     * <ul>
     *     <li>{@code insertString(int offset, String str, AttributeSet a)}.
     *     <br/>{@code int offset} is passed for offset. {@code str.length} is passed for length.
     *
     *     <li>{@code remove(int offset, int length)}.
     *     <br/>{@code int offset} is passed for offset. {@code 0} is passed for length.
     *
     *     <li>{@code fireInsertUpdate(DocumentEvent e)}.
     *     <br/>{@code e.getOffset()} is passed for offset. {@code e.getLength()} is passed for length.
     *
     *     <li>{@code fireRemoveUpdate(DocumentEvent e)}.
     *     <br/>{@code e.getOffset()} is passed for offset. {@code e.getLength()} is passed for length.
     * </ul>
     */
    protected abstract void processChangedLines(int offset, int length);

    /*
    protected String addMatchingBrace(int offset) throws BadLocationException {
        StringBuilder whiteSpace = new StringBuilder();

        int line = rootElement.getElementIndex(offset);
        int i = rootElement.getElement(line).getStartOffset();
        while (true) {
            String temp = this.getText(i, 1);
            if (temp.equals(" ") || temp.equals("\t")) {
                whiteSpace.append(temp);
                i++;
            } else
                break;
        }
        return "{\n" + whiteSpace + whiteSpace + "\n" + whiteSpace + "}";
    }
    */


    // GETTERS & SETTERS ===
    public String getIndentStyle() {
        return indentStyle;
    }

    public void setIndentStyle(String indentStyle) {
        if (Objects.equals(indentStyle, INDENT_STYLE_USE_TABS) || Objects.equals(indentStyle, INDENT_STYLE_USE_SPACES)) {
            this.indentStyle = indentStyle;
        } else throw new IllegalArgumentException("PreEditorDocument.setIndentStyle: indentStyle must be either " +
                "\"INDENT_STYLE_USE_TABS\" or \"INDENT_STYLE_USE_SPACES\".");
    }

    public int getIndentSize() {
        return indentSize;
    }

    public void setIndentSize(int indentSize) {
        this.indentSize = indentSize;
    }

    // CONVENIENCE METHODS ===
    protected String getAllContent() {
        try {
            return this.getText(0, this.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
            return null;
        }
    }
}
