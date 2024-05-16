package nos.pre.editor.UI;

import java.awt.*;
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
}
