package nos.pre.editor.functions;

import nos.pre.editor.UI.Editor.editingPane.PreTextPane;
import nos.pre.editor.defaultValues.KeyboardShortcuts;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;

public class UndoRedoFunction {
    private static final String undoKey = "Undo";
    private static final String redoKey = "Redo";

    private final PreTextPane preTextPane;

    private final UndoManager undoManager = new UndoManager();

    public UndoRedoFunction(PreTextPane preTextPane) {
        this.preTextPane = preTextPane;
        undoManager.setLimit(100); // TODO: User can change limit from settings
        addUndoRedo();
    }

    private void addUndoRedo() {
        Document document = this.preTextPane.getDocument();

        // Add UndoableEdit when edits are made to the document
        document.addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));

        // Undo action
        this.preTextPane.getActionMap().put(undoKey, new AbstractAction(undoKey) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }
        });

        // Redo action
        this.preTextPane.getActionMap().put(redoKey, new AbstractAction(redoKey) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
            }
        });

        // Add keyboard shortcuts for undo and redo
        this.preTextPane.getInputMap().put(KeyboardShortcuts.EDITINGPANE_UNDO, undoKey);
        this.preTextPane.getInputMap().put(KeyboardShortcuts.EDITINGPANE_REDO, redoKey);
    }

    /**
     * If there are any edits that can be undone, then undoes the appropriate edits.
     */
    public void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    /**
     * If there are any edits that can be redone, then redoes the appropriate edits.
     */
    public void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }
}
