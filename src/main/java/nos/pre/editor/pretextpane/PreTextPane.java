package nos.pre.editor.pretextpane;

import nos.pre.editor.autoComplete.AutoComplete;
import nos.pre.editor.autoComplete.JavaAutoComplete;
import nos.pre.editor.coderead.CodeRead;
import nos.pre.editor.coderead.JavaCodeRead;
import nos.pre.editor.defaultValues.KeyboardShortcuts;
import nos.pre.editor.defaultValues.UIColors;
import nos.pre.editor.defaultValues.UIFonts;
import nos.pre.editor.editor.DefaultPreEditorDocument;
import nos.pre.editor.editor.PreEditorDocument;
import nos.pre.editor.files.FileIO;
import nos.pre.editor.files.FileSaveListener;
import nos.pre.editor.functions.UndoRedoFunction;
import nos.pre.editor.languages.java.JavaSyntaxPreEditorDocument;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class PreTextPane extends JTextPane {
    // File ===
    private final File openedFile;
    public File getOpenedFile() {
        return openedFile;
    }

    private boolean isFileSaved;
    private String savedFileText;

    public boolean isFileSaved() {
        return isFileSaved;
    }

    // Functionality booleans ===
    private boolean undoRedoEnabled; // True by default (set in constructor)
    // TODO: ViewingModes: ReadOnly, EditingMode

    public boolean isUndoRedoEnabled() {
        return undoRedoEnabled;
    }
    public void setUndoRedoEnabled(boolean undoRedoEnabled) {
        this.undoRedoEnabled = undoRedoEnabled;
        if (undoRedoEnabled) {
            undoRedoFunction = new UndoRedoFunction(this); // TODO: Undo/Redo Functionality suddenly not working.
        } else {
            undoRedoFunction = null;
        }
        this.preTextPaneMenu.updateMenuItems();
    }

    // Functionality objects ===
    private UndoRedoFunction undoRedoFunction;
    private AutoComplete autoComplete;
    private CodeRead codeRead;

    private final PreTextPaneMenu preTextPaneMenu;

    // Listeners ===
    private final ArrayList<FileSaveListener> fileSaveListenersList = new ArrayList<>();
    public void addFileSaveListener(FileSaveListener fileSaveListener) {
        fileSaveListenersList.add(fileSaveListener);
    }
    public void removeFileSaveListener(FileSaveListener fileSaveListener) {
        fileSaveListenersList.remove(fileSaveListener);
    }

    /**
     * Constructor for PreTextPane
     * @param openedFile File to open
     */
    public PreTextPane(File openedFile) {
        this.openedFile = openedFile;

        // Setting up TextComponent

        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setFont(UIFonts.PRETEXTPANE_FONT);
        this.setBackground(UIColors.PRETEXTPANE_BG);
        this.setForeground(UIColors.PRETEXTPANE_FG);

        PreCaret preCaret = new PreCaret();
        preCaret.registerEditingPane(this);

        preTextPaneMenu = new PreTextPaneMenu(this);
        this.setComponentPopupMenu(this.preTextPaneMenu);

        // LinePainter linePainter = new LinePainter(this, UIColors.PRETEXTPANE_CURRENT_LINE_HIGHLIGHT); // To highlight the current line
        // TODO: linePainter temporarily disabled. See line highlight task in Todoist Project.

        // Adding Functionality

        addLanguageFeatures();
        addSaveFunctionality();
        // Save document listener added after setting document (addLanguageFeatures()) so that listener is added to language document.

        setUndoRedoEnabled(false); // TODO: Temporarily false (until undo/redo is fixed)
    }

    private void addLanguageFeatures() {
        String fileName = this.openedFile.getName();
        String fileExtension = this.openedFile.getName().substring(fileName.lastIndexOf(".") + 1);

        switch (fileExtension) {
            // TODO: case "class": decompile class files
            case "java":
                this.setStyledDocument(new JavaSyntaxPreEditorDocument());
                this.codeRead = new JavaCodeRead(this.openedFile);
                this.autoComplete = new JavaAutoComplete(this, (JavaCodeRead) this.codeRead);
                break;
            default:
                this.setStyledDocument(new DefaultPreEditorDocument());
                this.autoComplete = null;
                this.codeRead = null;
                break;
        }
    }

    // FILE I/O METHODS ===
    public boolean openFile() {
        this.setText("");

        if (FileIO.openFileToDocument(this.openedFile, this.getStyledDocument())) {
            // Set Caret to the beginning of the text
            this.setCaretPosition(0);

            // OpenedFile has not been changed yet, so isFileSaved = true,
            // and FileSaveListener.fileSaved() is triggered for all fileSaveListeners
            isFileSaved = true;
            try {
                savedFileText = this.getDocument().getText(0, this.getDocument().getLength() - 1);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            runFileSavedListeners();

            return true;
        } else return false;
    }

    /**
     * If <code>openedFile</code> is not saved, then saves the file, and runs <code>FileSaveListener.fileSaved()</code>
     * method for all FileSaveListeners
     */
    public void saveFile() {
        if (! isFileSaved) {
            if (FileIO.saveFile(this.openedFile, this.getText())) {
                isFileSaved = true;
                try {
                    savedFileText = this.getDocument().getText(0, this.getDocument().getLength() - 1);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                runFileSavedListeners();
            }
        }
    }

    private void addSaveFunctionality() {
        // Unsaved Changes Listener
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

        // Save Keyboard shortcut
        KeyboardShortcuts.addKeyboardShortcut("Save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        }, KeyboardShortcuts.PRETEXTPANE_SAVE, this);
    }

    public void reloadFileFromDisk() {
        String fileText = FileIO.openFile(this.openedFile);

        if (! Objects.equals(this.savedFileText, fileText)) {
            if (this.isFileSaved) { //
                int caretPos = this.getCaretPosition();

                this.openFile(); // Opening file code already written.

                if (caretPos > this.getDocument().getLength()) {
                    caretPos = this.getDocument().getLength();
                }

                this.setCaretPosition(caretPos);

            } else {
                // TODO: Implement Show difference feature
                int option = JOptionPane.showOptionDialog(null,
                        "Changes have been made to \"" + this.openedFile.getAbsolutePath() + "\" in memory and on disk.",
                        "File changes conflict", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                        new String[] {"Load changes from disk", "Keep memory changes", "Show difference (not implemented)"},
                        "Load changes from disk");

                if (option == JOptionPane.YES_OPTION) { // Load changes from disk
                    int caretPos = this.getCaretPosition();

                    this.openFile(); // Opening file code already written.

                    if (caretPos > this.getDocument().getLength()) {
                        caretPos = this.getDocument().getLength();
                    }

                    this.setCaretPosition(caretPos);

                } else if (option == JOptionPane.NO_OPTION) { // Keep memory changes
                    System.out.println("PreTextPane.reloadFileFromDisk: Keep memory changes (no option)");
                } else if (option == JOptionPane.CANCEL_OPTION) { // Show difference
                    System.out.println("PreTextPane.reloadFileFromDisk: show difference (cancel option)");
                }
            }
        }
    }

    // CARET LOCATION METHODS ===
    /**
     * Get the line the caret is currently on.
     * @param offset Location of caret with reference to all text.
     * @return The line on which caret is currently on.
     *
     * @see #getCaretPositionInLine(int, int)
     */
    public int getCaretLinePosition(int offset) {
        Document document = this.getDocument();
        Element map = document.getDefaultRootElement();
        return map.getElementIndex(offset);
    }

    /**
     * Get char position of caret in the specified line.
     * @param line The line in which caret is located.
     * @param offset Location of caret with reference to all text.
     * @return The char position of caret in the specified line.
     *
     * @see #getCaretLinePosition(int)
     */
    public int getCaretPositionInLine(int line, int offset) {
        Element map = this.getDocument().getDefaultRootElement();
        Element lineElem = map.getElement(line);
        return offset - lineElem.getStartOffset();
    }

    // UNDO/REDO METHODS ===
    /**
     * If there are any edits that can be undone, then undoes the appropriate edits.
     * @see #redo()
     * @see #isUndoRedoEnabled()
     * @see #setUndoRedoEnabled(boolean)
     */
    public void undo() {
        if (this.undoRedoFunction != null) {
            this.undoRedoFunction.undo();
        }
    }

    /**
     * If there are any edits that can be redone, then redoes the appropriate edits.
     * @see #undo()
     * @see #isUndoRedoEnabled()
     * @see #setUndoRedoEnabled(boolean)
     */
    public void redo() {
        if (this.undoRedoFunction != null) {
            this.undoRedoFunction.redo();
        }
    }

    // CUSTOM LISTENER METHODS ===
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

    // INDENT METHODS ===
    public String getIndentStyle() {
        if (this.getStyledDocument() instanceof PreEditorDocument) {
            return ((PreEditorDocument) this.getStyledDocument()).getIndentStyle();
        } else {
            throw new IllegalStateException("EditingPane.getIndentStyle(): this.StyledDocument not instanceof PreEditorDocument");
        }
    }

    public int getIndentSize() {
        if (this.getStyledDocument() instanceof PreEditorDocument) {
            return ((PreEditorDocument) this.getStyledDocument()).getIndentSize();
        } else {
            throw new IllegalStateException("EditingPane.getIndentSize(): this.StyledDocument not instanceof PreEditorDocument");
        }
    }

    // UTIL METHODS ===
    public void setSelection(int start, int end) {
        this.setSelectionStart(start);
        this.setSelectionEnd(end);
    }

    public void showAutoCompleteMenuLater() {
        if (this.autoComplete != null) {
            this.autoComplete.showAutoCompleteMenuLater();
        }
    }
}
