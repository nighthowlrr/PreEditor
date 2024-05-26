package nos.pre.editor.UI;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Objects;

public class Fonts {
    // Font Registering
    public static final Font LeagueSpartan;
    public static final Font URW_Gothic;

    static {
        try {
            LeagueSpartan = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Fonts.class.getResourceAsStream("/fonts/LeagueSpartan_regular.ttf")));
            URW_Gothic = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Fonts.class.getResourceAsStream("/fonts/urw_gothic_l_demi.ttf")));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException("Font Input error.", e);
        }
    }

    // Fonts methods
    public static FontMetrics getMetricsOfFont(Font font) {
        Canvas canvas = new Canvas();
        return canvas.getFontMetrics(font);
    }

    public static @NotNull Rectangle2D getStringBounds(String s, @NotNull Font f) {
        FontRenderContext renderContext = new FontRenderContext(f.getTransform(), true, true);
        TextLayout textLayout = new TextLayout(s, f, renderContext);
        return textLayout.getBounds();
    }
}
