package templateUI;

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


            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

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

}
