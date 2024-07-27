package nos.pre.editor.UI.editor;

import nos.pre.editor.defaultValues.UIColors;
import nos.pre.editor.defaultValues.UIFonts;
import org.jetbrains.annotations.NotNull;
import templateUI.SwingComponents.jTabbedPane;

import java.io.File;
import java.util.Objects;

public class EditorTabbedPane extends jTabbedPane {
    public EditorTabbedPane() {
        super(true);

        this.setOpaque(true);
        this.setBackground(UIColors.EDITOR_TABBED_PANE_BG);
        this.setForeground(UIColors.EDITOR_TABBED_PANE_FG);
        this.setFont(UIFonts.EDITOR_TABBED_PANE_FONT);

        // TODO: Save file automatically before closing. Do this at the very end.
    }

    public void openEditorTab(@NotNull File file) {
        // TODO: If file type is not supported (file cannot be read and opened), then do not open a new tab

        if (file.isFile()) {
            String fileNameTabTitle = file.getName();

            // Check if tab with same title is already open
            boolean isTabOpen = false;

            for (int i = 0; i < this.getTabCount(); i++) {
                if (Objects.equals(this.getTitleAt(i), fileNameTabTitle)) {
                    if (this.getComponentAt(i) instanceof EditorView sameNameEditorView) {
                        // If the path of file passed is the same
                        // as the path of file opened in the editor with the same tab name
                        if (sameNameEditorView.getOpenedFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                            isTabOpen = true;
                            this.setSelectedIndex(i); // Select already opened tab with the same title.
                            // TODO: Request focus to editor textPane
                        } else { // Open new tab with the name of the parent directory of file with the same name but different path included.
                            String sameFileNameTabTitle = file.getParentFile().getName() + "/" + fileNameTabTitle;

                            this.addTab(sameFileNameTabTitle, new EditorView(file));
                            isTabOpen = true;
                        }
                        break;
                    }
                }
            }

            if (!isTabOpen) {
                this.addTab(fileNameTabTitle, new EditorView(file));
            }
        } else throw new IllegalArgumentException("EditorTabbedPane.openFile(): File must be a normal file.");
    }

    private void focusPreTextPaneInTab(int index) {
        if (this.getComponentAt(index) instanceof EditorView editorView) {
            editorView.focusPreTextPane();
        }
    }

    @Override
    public void setSelectedIndex(int index) {
        super.setSelectedIndex(index);
        focusPreTextPaneInTab(index);
    }
}
