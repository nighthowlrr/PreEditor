package nos.pre.editor.UI.Editor.editingPane;

import nos.pre.editor.UI.Colors;
import nos.pre.editor.UI.Fonts;
import nos.pre.editor.functions.UndoRedoFunction;
import nos.pre.editor.languages.java.JavaSyntaxDocument;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.text.*;
import java.io.File;
import java.util.Scanner;

public class EditingPane extends JTextPane {
    private final File openedFile;
    public File getOpenedFile() {
        return openedFile;
    }

    private final UndoRedoFunction undoRedoFunction;

    public EditingPane(File openedFile) {
        this.openedFile = openedFile;

        this.setDoubleBuffered(true);
        this.setBackground(Colors.editorBackground);
        this.setForeground(Colors.editorForeground);
        this.setFont(Fonts.SourceCodePro_Regular.deriveFont(14F));
        this.setCaretColor(Colors.editorCaretColor);
        this.setSelectionColor(Colors.editorSelectionColor);
        // TODO: Make Caret bigger for visibility

        LinePainter linePainter = new LinePainter(this, Colors.editorCurrentLineHighlightColor); // To highlight the current line

        setLanguageDocument();
        undoRedoFunction = new UndoRedoFunction(this);

        this.setComponentPopupMenu(new EditingPaneMenu(this));
    }

    private void setLanguageDocument() {
        String fileName = this.openedFile.getName();
        String fileExtension = this.openedFile.getName().substring(fileName.lastIndexOf(".") + 1);

        switch (fileExtension) {
            // TODO: Make a global class and check from that class
            case "java":
            // TODO: case "class": decompile class files
                this.setStyledDocument(new JavaSyntaxDocument());
                break;
            default:
                this.setStyledDocument(new DefaultStyledDocument());
                break;
        }
    }

    public void openFile() throws Exception {
        this.setText("");

        // Read the file
        Scanner scanner = new Scanner(this.openedFile);
        while (scanner.hasNextLine()) {
            // Append the text line-by-line
            this.getStyledDocument().insertString(this.getStyledDocument().getLength(), scanner.nextLine() + "\n", null);
        }
        // Remove the last "\n" character
        this.getStyledDocument().remove(this.getStyledDocument().getLength() - 1, 1);
        scanner.close();

        // Set Caret to the beginning of the text
        this.setCaretPosition(0);
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

    /**
     * If there are any edits that can be undone, then undoes the appropriate edits.
     */
    public void undo() {
        this.undoRedoFunction.undo();
    }

    /**
     * If there are any edits that can be redone, then redoes the appropriate edits.
     */
    public void redo() {
        this.undoRedoFunction.redo();
    }
}
