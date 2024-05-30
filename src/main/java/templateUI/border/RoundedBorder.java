package templateUI.border;

import nos.pre.editor.UI.GraphicsUtilities;

import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;


public class RoundedBorder extends LineBorder {
    private final int radius;
    
    /**
     * Constructor for creating a Rounded border
     *
     * @param c The color of the border
     * @param thickness The thickness of the border
     * @param radius The corner radius of the border
     */
    public RoundedBorder(Color c, int thickness, int radius) {
        super(c, thickness, true);
        this.radius = radius;
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (this.thickness > 0) {
            Graphics2D g2d = GraphicsUtilities.getGraphics2DWithHints(g);
    
            Color oldColor = g2d.getColor();
            g2d.setColor(this.lineColor);
    
            int offs = this.thickness;
            int size = offs + offs;
    
            Shape outer = new RoundRectangle2D.Float(x, y, width, height, 0, 0);
            Shape inner = new RoundRectangle2D.Float(x + offs, y + offs, width - size, height - size, radius, radius);
            
            Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
            path.append(inner, false);
            path.append(outer, false);
    
            g2d.fill(path);
            g2d.setColor(oldColor);
            
            g2d.dispose();
        }
    }
}
