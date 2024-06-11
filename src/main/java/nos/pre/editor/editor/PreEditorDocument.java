package nos.pre.editor.editor;

import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import java.util.Objects;

public abstract class PreEditorDocument extends DefaultStyledDocument {
    public static final String TABS_POLICY_USE_TABS = "tabsPolicy_useTabs";
    public static final String TABS_POLICY_USE_SPACES = "tabsPolicy_useSpaces";

    private int tabSize;
    private String tabPolicy;

    public PreEditorDocument() {
        setDefaultValues();

        this.putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
    }

    private void setDefaultValues() {
        setTabSize(4);
        setTabPolicy(TABS_POLICY_USE_SPACES);
        // TODO: get defaults from settings
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        // if (str.equals("{")) str = addMatchingBrace(offset);

        // Managing tabs
        if (Objects.equals(tabPolicy, TABS_POLICY_USE_SPACES)) {
            str = str.replaceAll("\t", " ".repeat(tabSize));
        } // TODO: else if (Objects.equals(tabPolicy, TABS_POLICY_USE_TABS))

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
    public String getTabPolicy() {
        return tabPolicy;
    }

    public void setTabPolicy(String tabPolicy) {
        if (Objects.equals(tabPolicy, TABS_POLICY_USE_TABS) || Objects.equals(tabPolicy, TABS_POLICY_USE_SPACES)) {
            this.tabPolicy = tabPolicy;
        } else throw new IllegalArgumentException("PreEditorDocument.setTabPolicy: tabPolicy must be either " +
                "\"TABS_POLICY_USE_TABS\" or \"TABS_POLICY_USE_SPACES\".");
    }

    public int getTabSize() {
        return tabSize;
    }

    public void setTabSize(int tabSize) {
        this.tabSize = tabSize;
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
