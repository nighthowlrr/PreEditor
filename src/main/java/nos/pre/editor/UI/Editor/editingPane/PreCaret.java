package nos.pre.editor.UI.Editor.editingPane;

import nos.pre.editor.defaultValues.UIColors;

import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.event.FocusEvent;

/**
 * Custom Caret class.
 * Managing:
 * <li>Text Selection Color (both when text component is focused and unfocused)</li>
 *
 * (Color of caret itself is changed by the text component caret is bound to.)
 */
public class PreCaret extends DefaultCaret {
    private final Highlighter.HighlightPainter focusedHighlightPainter;
    private final Highlighter.HighlightPainter unfocusedHighlightPainter;

    public PreCaret() {
        focusedHighlightPainter = new DefaultHighlighter.DefaultHighlightPainter(UIColors.EDITINGPANE_SELECTION_COLOR);
        unfocusedHighlightPainter = new DefaultHighlighter.DefaultHighlightPainter(UIColors.EDITINGPANE_SELECTION_COLOR_UNFOCUSED);

        setBlinkRate(500);
        // TODO: Make Caret bigger for visibility
    }

    @Override
    protected Highlighter.HighlightPainter getSelectionPainter() {
        return getComponent().hasFocus() ? focusedHighlightPainter : unfocusedHighlightPainter;
    }

    @Override
    public void focusGained(FocusEvent e) {
        // setSelectionVisible(false); // super.focusGained(e) sets selectionVisible to true.
        super.focusGained(e);
    }

    @Override
    public void focusLost(FocusEvent e) {
        super.focusLost(e);
        setSelectionVisible(true); // to show selections even when text component is unfocused.
    }
}
