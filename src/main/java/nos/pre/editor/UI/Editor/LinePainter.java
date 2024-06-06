package nos.pre.editor.UI.Editor;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class LinePainter extends DefaultHighlighter.DefaultHighlightPainter implements CaretListener, MouseListener, MouseMotionListener {
    private final JTextPane editingPane;
    private final DefaultHighlighter highlighter;
    private Object lastHighlight;

    public LinePainter(JTextPane textPane, Color color) {
        super(color);
        this.editingPane = textPane;

        this.highlighter = (DefaultHighlighter) this.editingPane.getHighlighter();
        highlighter.setDrawsLayeredHighlights(true);

        this.editingPane.addCaretListener(this);
        this.editingPane.addMouseListener(this);
        this.editingPane.addMouseMotionListener(this);

        addHighlight(0);
    }

    /**
     * This method is overridden to get the desired behavior.
     * The offs1 parameter is ignored and the entire line is highlighted
     * @param g the graphics context
     * @param offs0 the starting model offset >= 0
     * @param offs1 the ending model offset >= offs1
     * @param bounds the bounding box of the view, which is not necessarily the region to paint.
     * @param c the editor
     * @param view View painting for
     * @return The region which drawing occurred in
     */
    @Override
    public Shape paintLayer(@NotNull Graphics g, int offs0, int offs1, Shape bounds, @NotNull JTextComponent c, View view) {
        try {
            // Only use the first offset to get the line to highlight
            Rectangle r = c.modelToView(offs0);
            r.x = 0;
            r.width = c.getSize().width;

            g.setColor(this.getColor());
            g.fillRect(r.x, r.y, r.width, r.height);
            return r;
        } catch (BadLocationException e) {
            return null;
        }
    }

    /**
     * Remove/add the highlight to make sure it gets repainted
     */
    private void resetHighlight() {
        SwingUtilities.invokeLater(() -> {
            highlighter.removeHighlight(lastHighlight);

            Element root = editingPane.getDocument().getDefaultRootElement();
            int line = root.getElementIndex(editingPane.getCaretPosition());
            Element lineElement = root.getElement(line);
            int start = lineElement.getStartOffset();
            addHighlight(start);
        });
    }

    private void addHighlight(int offset) {
        try {
            lastHighlight = highlighter.addHighlight( offset, offset + 1, this );
        } catch (BadLocationException e) {}
    }

    // Implement CaretListener
    @Override
    public void caretUpdate(CaretEvent e) {
        resetHighlight();
    }

    // Implement MouseListener
    @Override
    public void mousePressed(MouseEvent e) {
        resetHighlight();
    }
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    //  Implement MouseMotionListener
    @Override
    public void mouseDragged(MouseEvent e) {
        resetHighlight();
    }
    @Override public void mouseMoved(MouseEvent e) {}
}
