package nos.pre.editor.UI.Editor;

import nos.pre.editor.UI.Editor.editingPane.PreTextPane;
import nos.pre.editor.UI.Editor.editingPane.FindReplace;
import nos.pre.editor.defaultValues.UIColors;
import nos.pre.editor.editor.PreEditorDocument;
import nos.pre.editor.files.FileSaveListener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import templateUI.SwingComponents.TextLineNumber;
import templateUI.SwingComponents.jScrollPane;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class EditorView extends JPanel {
    private final File openedFile;
    public File getOpenedFile() {
        return this.openedFile;
    }

    public EditorView(File openFile) {
        super(new BorderLayout(), true);

        this.openedFile = openFile;

        this.addUIComponents();
        this.openFile();
    }

    private JPanel editingPaneHolder;
    private PreTextPane preTextPane;
    private TextLineNumber editorLineNumber;
    private jScrollPane editorScrollPane;

    private JPanel statusBar;
    private JLabel saveStatusLabel;
    private JLabel tabPolicyLabel;
    private JLabel caretLocationLabel;

    private void initUI() {
        editingPaneHolder  = new JPanel(new BorderLayout(), true);
        preTextPane = new PreTextPane(this.openedFile);
        editorLineNumber = new TextLineNumber(preTextPane);
        editorScrollPane = new jScrollPane(editingPaneHolder);

        statusBar = new JPanel(true);
        saveStatusLabel = new JLabel();
        tabPolicyLabel = new JLabel();
        caretLocationLabel = new JLabel();
    }

    private void addUIComponents() {
        this.initUI();

        // EDITOR PANE & LINE NUMBER ===
        editingPaneHolder.add(preTextPane, BorderLayout.CENTER);

        editorLineNumber.setCurrentLineForeground(UIColors.EDITOR_LINE_NUMBERS_CURRENTLINE_FG);
        editorLineNumber.setLineForeground(UIColors.EDITOR_LINE_NUMBERS_FG);
        editorLineNumber.setSeparatorColor(UIColors.EDITOR_LINE_NUMBERS_SEPARATOR);
        editorScrollPane.setRowHeaderView(editorLineNumber);

        editorScrollPane.setWheelScrollingEnabled(true);
        editorScrollPane.setBackground(preTextPane.getBackground());
        editorScrollPane.setScrollThumbColor(new Color(0xFFFFFF));
        editorScrollPane.setScrollTrackColor(preTextPane.getBackground());
        editorScrollPane.setBorder(BorderFactory.createEmptyBorder());
        setEditorScrollPaneCorners();
        this.add(editorScrollPane, BorderLayout.CENTER);

        // TODO: When scrolling horizontally, find/replace ui panel also scrolls then glitches back to normal position
        editorScrollPane.setColumnHeaderView(new FindReplace(preTextPane).getUI());

        // STATUS BAR ===
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        statusBar.setPreferredSize(new Dimension(0, 20));
        statusBar.setFocusable(false);
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIColors.EDITOR_STATUS_BAR_BORDER));
        statusBar.setBackground(UIColors.EDITOR_STATUS_BAR_BG);

        preTextPane.addFileSaveListener(new FileSaveListener() {
            @Override
            public void fileSaved() {
                saveStatusLabel.setText("Saved");
                saveStatusLabel.setForeground(UIColors.EDITOR_STATUS_BAR_SAVESTATUS_SAVED_TEXT);
            }
            @Override
            public void fileUnsaved() {
                saveStatusLabel.setText("Unsaved");
                saveStatusLabel.setForeground(UIColors.EDITOR_STATUS_BAR_SAVESTATUS_UNSAVED_TEXT);
            }
        });
        saveStatusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        int tabSize = preTextPane.getIndentSize();
        String tabPolicy = "";
        if (Objects.equals(preTextPane.getIndentStyle(), PreEditorDocument.INDENT_STYLE_USE_SPACES)) {
            tabPolicy = "spaces";
        } else if (Objects.equals(preTextPane.getIndentStyle(), PreEditorDocument.INDENT_STYLE_USE_TABS)) {
            tabPolicy = "tab"; // TODO: refinement: proper text (reference intellij)
        }
        tabPolicyLabel.setText(tabSize + " " + tabPolicy);
        tabPolicyLabel.setForeground(UIColors.EDITOR_STATUS_BAR_TAB_POLICY_TEXT);
        tabPolicyLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        preTextPane.addCaretListener(e -> {
            int dot = e.getDot();
            int line = preTextPane.getCaretLinePosition(dot);
            int posInLine = preTextPane.getCaretPositionInLine(line, dot);

            caretLocationLabel.setText((line + 1) + ":" + (posInLine + 1));
        });
        caretLocationLabel.setForeground(UIColors.EDITOR_STATUS_BAR_CARET_LOCATION_TEXT);
        caretLocationLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));
        statusBar.add(saveStatusLabel);
        statusBar.add(Box.createRigidArea(new Dimension(20, 0)));

        statusBar.add(Box.createHorizontalGlue());

        statusBar.add(tabPolicyLabel);
        statusBar.add(Box.createRigidArea(new Dimension(20, 0)));
        statusBar.add(caretLocationLabel);
        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));

        this.add(statusBar, BorderLayout.SOUTH);
    }

    private void openFile() {
        // TODO: If file type is not supported (file cannot be read and opened), then do not open a new tab
        if (preTextPane.openFile()) {
            saveStatusLabel.setText("Saved");
            caretLocationLabel.setText("1:1");
        } else {
            JOptionPane.showMessageDialog(null, "Unable to open file. An error occurred",
                    "Error", JOptionPane.ERROR_MESSAGE, null);
            // TODO: More detailed error messages
        }
    }

    @Override
    public void requestFocus() {
        preTextPane.requestFocus();
    }


    @Contract(" -> new")
    private @NotNull JPanel createNewCorner() {
        return new JPanel(null, true) {
            @Override
            public Color getBackground() {
                return UIColors.PRETEXTPANE_BG; // TODO: UIColors
            }
        };
    }
    private void setEditorScrollPaneCorners() {
        editorScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, createNewCorner());
        editorScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, createNewCorner());
        editorScrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER, createNewCorner());
        editorScrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER, createNewCorner());
    }
}
