package templateUI;

import nos.pre.editor.UI.GraphicsUtilities;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class jScrollPane extends JScrollPane {
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


    private static class CustomScrollBarUI extends BasicScrollBarUI {
        private static final int normalTransparency = 50;
        private static final int rolloverTransparency = 75;

        @Override
        protected JButton createDecreaseButton (int orientation) {
            return new JButton() {
                @Override
                public Dimension getPreferredSize () {
                    return new Dimension();
                }
            };
        }

        @Override
        protected JButton createIncreaseButton (int orientation) {
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
            Color baseColor = new Color(0, 0, 0);
            Color color = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha);

            //int thumbSize = 13;

            //int width = (scrollbar.getOrientation() == JScrollBar.VERTICAL ? thumbSize : thumbBounds.width);
            //width = Math.max(width, thumbSize);

            //int height = (scrollbar.getOrientation() == JScrollBar.VERTICAL ? thumbBounds.height : thumbSize);
            //height = Math.max(height, thumbSize);

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
    }

    public void setScrollUnitIncrement(int increment) {
        this.getVerticalScrollBar().setUnitIncrement(increment);
        this.getHorizontalScrollBar().setUnitIncrement(increment);
    }

}
