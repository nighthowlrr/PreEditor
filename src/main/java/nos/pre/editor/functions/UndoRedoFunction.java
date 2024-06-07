package nos.pre.editor.functions;

import nos.pre.editor.UI.Editor.editingPane.EditingPane;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;

public class UndoRedoFunction {
    private static final String undoKey = "Undo";
    private static final String redoKey = "Redo";

    private final EditingPane editingPane;

    private final UndoManager undoManager = new UndoManager();

    public UndoRedoFunction(EditingPane editingPane) {
        this.editingPane = editingPane;
        undoManager.setLimit(100); // TODO: User can change limit from settings
        addUndoRedo();
    }

    private void addUndoRedo() {
        Document document = this.editingPane.getDocument();

        // Add UndoableEdit when edits are made to the document
        document.addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));

        // Undo action
        this.editingPane.getActionMap().put(undoKey, new AbstractAction(undoKey) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }
        });

        // Redo action
        this.editingPane.getActionMap().put(redoKey, new AbstractAction(redoKey) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
            }
        });

        // Add keyboard shortcuts for undo and redo
        this.editingPane.getInputMap().put(KeyStroke.getKeyStroke("control Z"), undoKey);
        this.editingPane.getInputMap().put(KeyStroke.getKeyStroke("control shift Z"), redoKey);
        // TODO: Changeable keyboard shortcuts
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