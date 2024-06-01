package nos.pre.editor.UI;

import java.awt.*;

public class Colors {
    // TODO: Make a theme reader and add theme support

    // GLOBAL_COLORS
    public static final Color accentColor = new Color(0x2767BA);

    // WELCOME_SCREEN
    public static final Color welcomeMainBackground = new Color(0x1E1F22);
    public static final Color welcomeSideBackground = new Color(0x2B2D30);
    public static final Color welcomeForegroundLvl1 = Color.WHITE;
    public static final Color welcomeForegroundLvl2 = new Color(0xC1C1C1);
    public static final Color welcomeForegroundLvl3 = new Color(0x818181);

    // EDITOR_FRAME
    public static final Color editorFrameDividingBorderColor = new Color(0x1E1F22);

    // EDITOR_TABBED_PANE
    public static final Color editorTabbedPaneBackground = new Color(0x2E2F30);
    public static final Color editorTabbedPaneSelectedTabIndicator = Colors.accentColor;
    public static final int editorTabbedPaneSelectedTabBGAlpha = 50;

    // EDITOR_VIEW
    public static final Color editorBackground = new Color(0x1E1F22);
    public static final Color editorForeground = Color.WHITE;
    public static final Color editorCaretColor = new Color(0x097EBF);
    public static final Color editorSelectionColor = new Color(0xA6D2FF);
    public static final Color editorCurrentLineHighlightColor = new Color(0x14FFFFFF, true);
    public static final Color editorStatusBarBackground = new Color(0x1E1F22);
    public static final Color editorInternalBorderColor = new Color(0x2E3032);

    // TOOL_WINDOW_BAR
    public static final Color toolWindowBarBackground = new Color(0x2B2D30);

    // EMPTY_TOOL_WINDOW_HOLDER
    public static final Color toolWindowHolderBackground = new Color(0x2B2D30);

    // PROJECT_TOOL_WINDOW
    public static final Color projectToolBackground = new Color(0x2B2D30);
    // TODO: selected and non-selected colors

    // public static final Color editorSelectionColorLight = new Color(0x214283);
    // nice purple color = 0x6647D3
}
