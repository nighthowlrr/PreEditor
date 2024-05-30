package templateUI.SwingComponents;

import nos.pre.editor.UI.GraphicsUtilities;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class jScrollPane extends JScrollPane {
    private Color scrollTrackColor = null;
    public Color getScrollTrackColor() {
        return scrollTrackColor;
    }
    public void setScrollTrackColor(Color scrollTrackColor) {
        this.scrollTrackColor = scrollTrackColor;
    }

    private Color scrollThumbColor = null;
    public Color getScrollThumbColor() {
        return scrollThumbColor;
    }
    public void setScrollThumbColor(Color scrollThumbColor) {
        this.scrollThumbColor = scrollThumbColor;
    }

    public jScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        this.setFocusable(false);
        this.setOpaque(false);

        this.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        this.getHorizontalScrollBar().setUI(new CustomScrollBarUI());

        this.setScrollUnitIncrement(10);
    }

    public jScrollPane(Component view) {
        this(view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
    public jScrollPane(int vsbPolicy, int hsbPolicy) {
        this(null, vsbPolicy, hsbPolicy);
    }
    public jScrollPane() {
        this(null, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }


    private class CustomScrollBarUI extends BasicScrollBarUI {
        private final int normalTransparency = 50;
        private final int rolloverTransparency = 75;

        @Contract("_ -> new")
        @Override
        protected @NotNull JButton createDecreaseButton (int orientation) {
            return new JButton() {
                @Override
                public Dimension getPreferredSize () {
                    return new Dimension();
                }
            };
        }

        @Contract("_ -> new")
        @Override
        protected @NotNull JButton createIncreaseButton (int orientation) {
            return new JButton() {
                @Override
                public Dimension getPreferredSize () {
                    return new Dimension();
                }
            };
        }

        @Override
        protected void paintThumb (@NotNull Graphics g, JComponent c, @NotNull Rectangle thumbBounds) {
            int alpha = isThumbRollover() ? rolloverTransparency : normalTransparency;
            Color baseColor = scrollThumbColor != null ? scrollThumbColor : new Color(0x000000);
            Color color = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha);

            Graphics2D g2d = GraphicsUtilities.getGraphics2DWithHints(g);

            g2d.setColor(color);
            g2d.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);

            g2d.dispose();
        }

        @Override
        protected void setThumbBounds(int x, int y, int width, int height) {
            super.setThumbBounds(x, y, width, height);
            scrollbar.repaint();
        }

        @Override
        protected void paintTrack(@NotNull Graphics g, JComponent c, @NotNull Rectangle trackBounds) {
            Graphics2D g2d = GraphicsUtilities.getGraphics2DWithHints(g);

            g2d.setColor(scrollTrackColor != null ? scrollTrackColor : this.trackColor);

            g2d.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);

            if(trackHighlight == DECREASE_HIGHLIGHT) {
                paintDecreaseHighlight(g2d);
            } else if(trackHighlight == INCREASE_HIGHLIGHT) {
                paintIncreaseHighlight(g2d);
            }
        }
    }

    public void setScrollUnitIncrement(int increment) {
        this.getVerticalScrollBar().setUnitIncrement(increment);
        this.getHorizontalScrollBar().setUnitIncrement(increment);
    }
}
