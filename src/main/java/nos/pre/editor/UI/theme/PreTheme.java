package nos.pre.editor.UI.theme;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import util.FileUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HexFormat;

public class PreTheme {
    private enum PreThemeJSONKeys {
        themeName("themeName"),
        isThemeDark("isThemeDark"),

        themeInfo("themeInfo"),
        uiInfo("uiInfo"),
        ;

        private final String key;

        @Contract(pure = true)
        PreThemeJSONKeys(String key) {
            this.key = key;
        }

        @Contract(pure = true)
        public String getKey() {
            return this.key;
        }
    }

    public enum PreThemeColorKeys {
        // All UI Color values ---
        globalAccent("globalAccentColor"),
        globalForegroundLvl1("globalForegroundLvl1Color"),
        globalForegroundLvl2("globalForegroundLvl2Color"),
        globalForegroundLvl3("globalForegroundLvl3Color"),

        welcomeFrameMainBG("WelcomeFrameMainBackgroundColor"),
        welcomeFrameSecondaryBG("WelcomeFrameSecondaryBackgroundColor"),

        settingsFrameMainBG("SettingsFrameMainBackgroundColor"),
        settingsFrameMainFG("SettingsFrameMainForegroundColor"),

        editorFrameDivider("EditorFrameDividerColor"),

        editorTabbedPaneBG("EditorTabbedPaneBackgroundColor"),
        editorTabbedPaneFG("EditorTabbedPaneForegroundColor"),
        editorTabbedPaneOpenTabIndicator("EditorTabbedPaneOpenTabIndicatorColor"),
        editorTabbedPaneOpenTabBG("EditorTabbedPaneOpenTabBackgroundColor"),

        preTextPaneBG("PretextPaneBackgroundColor"),
        preTextPaneFG("PretextPaneForegroundColor"),
        preTextPaneCaret("PretextPaneCaretColor"),
        preTextPaneSelectionFocused("PretextPaneSelectionColorFocused"),
        preTextPaneSelectionUnfocused("PretextPaneSelectionColorUnfocused"),
        preTextPaneCurrentLineHighlight("PretextPaneCurrentLineHighlight"),
        preTextPaneMatchingBracketHighlight("PretextPaneMatchingBracketHighlight"),

        autocompleteMenuBG("AutocompleteMenuBackgroundColor"),
        autocompleteMenuSelectedBG("AutocompleteMenuSelectedItemColorColor"),
        autocompleteMenuFG("AutocompleteMenuForegroundColor"),
        autocompleteMenuBottomTextBarBG("AutocompleteMenuBottomTipBarBackgroundColor"),
        autocompleteMenuBottomTextBarFG("AutocompleteMenuBottomTipBarBackgroundColor"),

        findReplaceUIBG("FindReplaceUIBackgroundColor"),
        findReplaceUISelectedButtonBG("FindReplaceUISelectedButtonBackgroundColor"),
        findReplaceUILabelsFG("FindReplaceUILabelsForegroundColor"),
        findReplaceUIInputFG("FindReplaceUIInputForegroundColor"),
        findReplaceUIBorder("FindReplaceUIBorderColor"),
        findReplaceHighlight("FindReplaceHighlight"),

        editorLineNumbersFG("EditorLineNumbersForegroundColor"),
        editorLineNumbersCurrentLineFG("EditorLineNumbersCurrentLineForegroundColor"),
        editorLineNumbersSeparator("EditorLineNumbersSeparatorColor"),

        editorStatusBarBG("EditorStatusBarBackgroundColor"),
        editorStatusBarBorder("EditorStatusBarBorderColor"),
        editorStatusBarMainFG("EditorStatusBarMainForegroundColor"),
        editorStatusBarSaveStatusUnsavedFG("EditorStatusBarSaveStatusUnsavedForegroundColor"),

        toolWindowBarBG("ToolWindowBarBackgroundColor"),
        toolWindowToolButtonBG("ToolWindowBarToolButtonBackgroundColor"),
        toolWindowToolButtonFG("ToolWindowBarToolButtonForegroundColor"),

        toolWindowHolderBG("ToolWindowHolderBackgroundColor"),

        projectToolWindowBG("ProjectToolWindowBackgroundColor"),
        projectToolWindowFileSelectedFocused("ProjectToolWindowSelectedFileColorFocused"),
        projectToolWindowFileSelectedUnfocused("ProjectToolWindowSelectedFileColorUnfocused")
        ;

        private final String colorKey;

        @Contract(pure = true)
        PreThemeColorKeys(String colorKey) {
            this.colorKey = colorKey;
        }

        @Contract(pure = true)
        public String getColorKey() {
            return this.colorKey;
        }
    }

    private final File themeFile;
    private final JsonNode uiColorInfoNode;

    public PreTheme(File themeFile) {
        if (!FileUtil.getFileExtension(themeFile).equalsIgnoreCase("json")) {
            throw new IllegalArgumentException("PreTheme.init: \"themeFile\" must be a JSON file.");
        }
        this.themeFile = themeFile;
        this.uiColorInfoNode = this.getUIColorInfoNode();
    }

    private JsonNode getUIColorInfoNode() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode mainNode = objectMapper.readTree(this.themeFile);
            JsonNode themeInfoNode = mainNode.get(PreThemeJSONKeys.themeInfo.getKey());

            return themeInfoNode.get(PreThemeJSONKeys.uiInfo.getKey());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Color getColor(@NotNull PreTheme.PreThemeColorKeys colorKey) {
        JsonNode colorValueNode = this.uiColorInfoNode.get(colorKey.getColorKey());
        if (colorValueNode != null) {
            String colorValue = colorValueNode.textValue();
            boolean hasAlpha = colorValue.length() == 8;

            return new Color(HexFormat.fromHexDigits(colorValue), hasAlpha);
        } else {
            // TODO: Return color from default PreTheme. If current theme is dark, use default-dark theme, otherwise use default-light theme.
            return null;
        }
    }
}
