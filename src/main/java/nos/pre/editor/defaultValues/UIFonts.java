package nos.pre.editor.defaultValues;

import nos.pre.editor.UI.Fonts;

import java.awt.*;

public class UIFonts {

    // WELCOME_FRAME
    public static final Font WELCOME_FRAME_APP_TITLE_LABEL_FONT = Fonts.URW_Gothic.deriveFont(Font.BOLD, 24F);
    public static final Font WELCOME_FRAME_APP_VERSION_LABEL_FONT = Fonts.URW_Gothic.deriveFont(18F);
    public static final Font WELCOME_FRAME_VIEW_TOGGLE_BUTTON_FONT = Fonts.URW_Gothic.deriveFont(16F);


    // EDITOR_TABBED_PANE
    public static final Font EDITOR_TABBED_PANE_FONT = Fonts.LeagueSpartan.deriveFont(Font.BOLD, 14F);

    // PRETEXTPANE
    public static final Font PRETEXTPANE_FONT = Fonts.SourceCodePro_Regular.deriveFont(14F);

    // PRETEXTPANE_MENU
    public static final Font PRETEXTPANE_MENU_FONT = Fonts.LeagueSpartan.deriveFont(Font.BOLD, 14F);

    // EDITOR_FIND_REPLACE_UI
    public static final Font FIND_REPLACE_UI_LABEL_FONT = Fonts.LeagueSpartan.deriveFont(14F);

    // EDITOR_STATUS_BAR
    public static final Font EDITOR_STATUS_BAR_INDICATORS_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);

    // TOOL_WINDOW
    public static final Font BASE_TOOL_WINDOW_NAME_LABEL_FONT = Fonts.LeagueSpartan.deriveFont(15F);

    // TOOL_WINDOW_BAR
    public static final Font TOOL_WINDOW_BAR_TOOLBUTTON_FONT = Fonts.LeagueSpartan.deriveFont(12F);
}
