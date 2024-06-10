package nos.pre.editor.UI.Editor.editingPane;

import nos.pre.editor.UI.Fonts;
import nos.pre.editor.defaultValues.KeyboardShortcuts;
import nos.pre.editor.defaultValues.UIColors;
import nos.pre.editor.files.FileSaveListener;
import nos.pre.editor.functions.UndoRedoFunction;
import nos.pre.editor.languages.java.JavaSyntaxDocument;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class EditingPane extends JTextPane {
    // Functionality enabled booleans ===
    private boolean undoRedoEnabled; // True by default (set in constructor)
    // TODO: ViewingModes: ReadOnly, EditingMode (May need to change saving code (e.g. boolean in EditingPaneMenu))

    public boolean isUndoRedoEnabled() {
        return undoRedoEnabled;
    }
    public void setUndoRedoEnabled(boolean undoRedoEnabled) {
        this.undoRedoEnabled = undoRedoEnabled;
        this.ensureAddUndoRedoFunction();
        this.editingPaneMenu.updateMenuItems();
    }

    // Functionality objects ===
    private UndoRedoFunction undoRedoFunction;

    private final EditingPaneMenu editingPaneMenu;

    // LISTENERS
    private final ArrayList<FileSaveListener> fileSaveListenersList = new ArrayList<>();
    public void addFileSaveListener(FileSaveListener fileSaveListener) {
        fileSaveListenersList.add(fileSaveListener);
    }
    public void removeFileSaveListener(FileSaveListener fileSaveListener) {
        fileSaveListenersList.remove(fileSaveListener);
    }

    private final File openedFile;
    public File getOpenedFile() {
        return openedFile;
    }

    private boolean isFileSaved;
    public boolean isFileSaved() {
        return isFileSaved;
    }


    public EditingPane(File openedFile) {
        this.openedFile = openedFile;

        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setFont(Fonts.SourceCodePro_Regular.deriveFont(14F));
        this.setBackground(UIColors.EDITINGPANE_BG);
        this.setForeground(UIColors.EDITINGPANE_FG);
        this.setCaretColor(UIColors.EDITINGPANE_CARET_COLOR);
        this.setSelectionColor(UIColors.EDITINGPANE_SELECTION_COLOR);
        // TODO: Make Caret bigger for visibility

        editingPaneMenu = new EditingPaneMenu(this);
        this.setComponentPopupMenu(this.editingPaneMenu);

        LinePainter linePainter = new LinePainter(this, UIColors.EDITINGPANE_CURRENT_LINE_HIGHLIGHT); // To highlight the current line

        setLanguageDocument();
        addUnsavedChangeListener();

        setUndoRedoEnabled(false); // TODO: Temporarily false (until undo/redo is fixed) (see ensureAddUndoRedoFunction();)
        addSaveKeyboardShortcut();
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

    private void addUnsavedChangeListener() {
        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeProcedure();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                changeProcedure();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                changeProcedure();
            }

            void changeProcedure() {
                isFileSaved = false;
                runFileUnSavedListeners();
            }
        });
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

        // OpenedFile has not been changed yet, so isFileSaved = true,
        // and FileSaveListener.fileSaved() is triggered for all fileSaveListeners
        isFileSaved = true;
        runFileSavedListeners();
    }

    /**
     * If <code>openedFile</code> is not saved, then saves the file, and runs <code>FileSaveListener.fileSaved()</code>
     * method for all FileSaveListeners
     */
    public void saveFile() {
        if (! isFileSaved) {
            try {
                PrintWriter writer = new PrintWriter(this.openedFile);
                writer.write(this.getText());
                writer.close();

                isFileSaved = true;
                runFileSavedListeners();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void addSaveKeyboardShortcut() {
        String saveKey = "Save";

        this.getActionMap().put(saveKey, new AbstractAction(saveKey) {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyboardShortcuts.EDITINGPANE_SAVE), saveKey);
    }

    private void ensureAddUndoRedoFunction() {
        if (undoRedoEnabled) {
            undoRedoFunction = new UndoRedoFunction(this); // TODO: Undo/Redo Functionality suddenly not working.
        }
    }

    /**
     * Get the line the caret is currently on.
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

    /**
     * If <code>fileSaveListenersList</code> is not empty, then run <code>FileSaveListener.fileSaved()</code> method of
     * all FileSaveListeners in the list.
     * @see #runFileUnSavedListeners()
     */
    private void runFileSavedListeners() {
        if (! fileSaveListenersList.isEmpty()) {
            for (FileSaveListener fileSaveListener : fileSaveListenersList) {
                fileSaveListener.fileSaved();
            }
        }
    }

    /**
     * If <code>fileSaveListenersList</code> is not empty, then run <code>FileSaveListener.fileUnsaved()</code> method of
     * all FileSaveListeners in the list.
     * @see #runFileSavedListeners()
     */
    private void runFileUnSavedListeners() {
        if (! fileSaveListenersList.isEmpty()) {
            for (FileSaveListener fileSaveListener : fileSaveListenersList) {
                fileSaveListener.fileUnsaved();
            }
        }
    }
}
