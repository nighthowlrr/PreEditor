package nos.pre.editor.defaultValues;

import java.awt.*;

public class UIColors { // TODO: Changeable & Default Themes. Make a theme reader and add theme support.
    // BG = background & FG = foreground

    // GLOBAL_COLOR
    public static final Color GLOBAL_COLOR_ACCENT = new Color(0x2767BA);

    public static final Color GLOBAL_FG_LVL1 = new Color(0xFFFFFF);
    public static final Color GLOBAL_FG_LVL2 = new Color(0xC1C1C1);
    public static final Color GLOBAL_FG_LVL3 = new Color(0x818181);


    // WELCOME_FRAME
    public static final Color WELCOME_FRAME_MAIN_BG = new Color(0x1E1F22);
    public static final Color WELCOME_FRAME_SIDE_PANEL_BG = new Color(0x2B2D30);


    // EDITOR_FRAME
    public static final Color EDITOR_FRAME_DIVIDER_COLOR = new Color(0x1E1F22);

    // EDITOR_TABBED_PANE
    public static final Color EDITOR_TABBED_PANE_BG = new Color(0x2E2F30);
    public static final Color EDITOR_TABBED_PANE_FG = new Color(0xFFFFFF);
    public static final Color EDITOR_TABBED_PANE_OPENTAB_INDICATOR = UIColors.GLOBAL_COLOR_ACCENT;
    public static final Color EDITOR_TABBED_PANE_OPENTAB_BG = new Color(0x322767BA, true); // 32-Alpha, 2767BA-GLOBAL_ACCENT

    // EDITINGPANE
    public static final Color EDITINGPANE_BG = new Color(0x1E1F22);
    public static final Color EDITINGPANE_FG = new Color(0xFFFFFF);
    public static final Color EDITINGPANE_CARET_COLOR = new Color(0x2767BA);
    public static final Color EDITINGPANE_SELECTION_COLOR = new Color(0xA6D2FF);
    public static final Color EDITINGPANE_CURRENT_LINE_HIGHLIGHT = new Color(255, 255, 255, 20);

    // EDITOR_LINE_NUMBERS
    public static final Color EDITOR_LINE_NUMBERS_CURRENTLINE_FG = new Color(0xFFFFFF);
    public static final Color EDITOR_LINE_NUMBERS_FG = new Color(0x5C5C5C);
    public static final Color EDITOR_LINE_NUMBERS_SEPARATOR = new Color(0x2E3032);

    // EDITOR_STATUS_BAR
    public static final Color EDITOR_STATUS_BAR_BG = UIColors.EDITINGPANE_BG;
    public static final Color EDITOR_STATUS_BAR_BORDER = new Color(0x2E3032);
    public static final Color EDITOR_STATUS_BAR_CARET_LOCATION_TEXT = new Color(0xC0C0C0);
    public static final Color EDITOR_STATUS_BAR_SAVESTATUS_SAVED_TEXT = new Color(0xC0C0C0);
    public static final Color EDITOR_STATUS_BAR_SAVESTATUS_UNSAVED_TEXT = new Color(0xED5757);


    // TOOL_WINDOW_BAR
    public static final Color TOOL_WINDOW_BAR_BG = new Color(0x2B2D30);
    public static final Color TOOL_WINDOW_BAR_TOOLBUTTON_BG = UIColors.TOOL_WINDOW_BAR_BG.brighter();
    // TODO: Brighter temporarily. Make same color, only brighter color for selected button (associated toolwindow of button is opened)
    public static final Color TOOL_WINDOW_BAR_TOOLBUTTON_FG = new Color(0xFFFFFF);

    // TOOL_WINDOW_HOLDER
    public static final Color TOOL_WINDOW_HOLDER_BG = new Color(0x2B2D30);

    // PROJECT_TOOL_WINDOW
    public static final Color PROJECT_TOOL_WINDOW_BG = new Color(0x2B2D30);
    public static final Color PROJECT_TOOL_WINDOW_FILESELECTED_FOCUSED = new Color(0x802767BA, true); // 80-Alpha, 2767BA-GLOBAL_ACCENT
    public static final Color PROJECT_TOOL_WINDOW_FILESELECTED_UNFOCUSED = new Color(0x4DFFFFFF, true);

    // nice purple color = 0x6647D3
}