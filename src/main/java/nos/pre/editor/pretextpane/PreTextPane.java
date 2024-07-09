package nos.pre.editor.pretextpane;

import nos.pre.editor.autoComplete.AutoComplete;
import nos.pre.editor.defaultValues.KeyboardShortcuts;
import nos.pre.editor.defaultValues.UIColors;
import nos.pre.editor.defaultValues.UIFonts;
import nos.pre.editor.editor.DefaultPreEditorDocument;
import nos.pre.editor.editor.PreEditorDocument;
import nos.pre.editor.files.FileIO;
import nos.pre.editor.files.FileSaveListener;
import nos.pre.editor.functions.UndoRedoFunction;
import nos.pre.editor.languages.java.JavaCompletions;
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
    private String savedFileText;

    // Functionality enabled booleans ===
    private boolean undoRedoEnabled; // True by default (set in constructor)
    // TODO: ViewingModes: ReadOnly, EditingMode (May need to change saving code (e.g. boolean in EditingPaneMenu))

    public boolean isUndoRedoEnabled() {
        return undoRedoEnabled;
    }
    public void setUndoRedoEnabled(boolean undoRedoEnabled) {
        this.undoRedoEnabled = undoRedoEnabled;
        this.ensureAddUndoRedoFunction();
        this.preTextPaneMenu.updateMenuItems();
    }

    // Functionality objects ===
    private UndoRedoFunction undoRedoFunction;
    private AutoComplete autoComplete;

    private final PreTextPaneMenu preTextPaneMenu;

    // LISTENERS
    private final ArrayList<FileSaveListener> fileSaveListenersList = new ArrayList<>();
    public void addFileSaveListener(FileSaveListener fileSaveListener) {
        fileSaveListenersList.add(fileSaveListener);
    }
    public void removeFileSaveListener(FileSaveListener fileSaveListener) {
        fileSaveListenersList.remove(fileSaveListener);
    }

    private final File openedFile;

    private boolean isFileSaved;


    public PreTextPane(File openedFile) {
        this.openedFile = openedFile;

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

        addLanguageFeatures();

        addSaveFunctionality();
        setUndoRedoEnabled(false); // TODO: Temporarily false (until undo/redo is fixed) (see ensureAddUndoRedoFunction();)
    }

    private void addLanguageFeatures() {
        String fileName = this.openedFile.getName();
        String fileExtension = this.openedFile.getName().substring(fileName.lastIndexOf(".") + 1);

        PreEditorDocument preEditorDocument;

        // TODO: Make a global class and check from that class

        switch (fileExtension) {
            case "java":
            // TODO: case "class": decompile class files
                preEditorDocument = new JavaSyntaxPreEditorDocument();

                this.autoComplete = new AutoComplete(this, new JavaCompletions());
                break;
            default:
                preEditorDocument = new DefaultPreEditorDocument();
                break;
        }

        // Setting the document
        this.setStyledDocument(preEditorDocument);
    }

    // FILE I/O FUNCTIONS ===

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

    /**
     * If <code>openedFile</code> is not saved, then saves the file, and runs <code>FileSaveListener.fileSaved()</code>
     * method for all FileSaveListeners
     */
    public void saveFile() {
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
     */
    public int getCaretPositionInLine(int line, int offset) {
        Element map = this.getDocument().getDefaultRootElement();
        Element lineElem = map.getElement(line);
        return offset - lineElem.getStartOffset();
    }

    // UNDO/REDO FUNCTIONS ===

    /**
     * If <code>undoRedoEnabled</code> is <code>True</code>, creates new <code>UndoRedoFunction</code> object.
     * Otherwise, sets <code>undoRedoFunction</code> to <code>null</code>.
     */
    private void ensureAddUndoRedoFunction() {
        if (undoRedoEnabled) {
            undoRedoFunction = new UndoRedoFunction(this); // TODO: Undo/Redo Functionality suddenly not working.
        } else {
            undoRedoFunction = null;
        }
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

    // GETTERS & SETTERS ===

    public File getOpenedFile() {
        return openedFile;
    }

    public boolean isFileSaved() {
        return isFileSaved;
    }

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
