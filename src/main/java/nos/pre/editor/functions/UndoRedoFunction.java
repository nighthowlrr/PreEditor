package nos.pre.editor.functions;

import nos.pre.editor.pretextpane.PreTextPane;
import nos.pre.editor.defaultValues.KeyboardShortcuts;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;

public class UndoRedoFunction {
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

        // Add keyboard shortcuts for undo and redo actions

        // Undo action
        KeyboardShortcuts.addKeyboardShortcut("Undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }
        }, KeyboardShortcuts.PRETEXTPANE_UNDO, this.preTextPane);

        // Redo action
        KeyboardShortcuts.addKeyboardShortcut("Redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
            }
        }, KeyboardShortcuts.PRETEXTPANE_REDO, this.preTextPane);
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
