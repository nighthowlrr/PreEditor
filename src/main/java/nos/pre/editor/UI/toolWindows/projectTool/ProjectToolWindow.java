package nos.pre.editor.UI.toolWindows.projectTool;

import nos.pre.editor.UI.Colors;
import nos.pre.editor.UI.Editor.EditorTabbedPane;
import nos.pre.editor.UI.toolWindows.ToolWindow;
import templateUI.SwingComponents.jScrollPane;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ProjectToolWindow extends ToolWindow {
    public ProjectToolWindow(ToolWindowLocation toolWindowLocation) { // TODO: Final ProjectPath
        super("Project", toolWindowLocation);
        this.setLayout(new BorderLayout());
        this.setBackground(Colors.projectToolBackground);

        this.addUIComponents();
    }


    // UI ===
    private final JFileTree fileTree = new JFileTree();
    private final jScrollPane fileTreeScroll = new jScrollPane(fileTree);

    private void addUIComponents() {
        this.add(this.getToolWindowNameLabel(), BorderLayout.PAGE_START);

        fileTree.setBackground(this.getBackground());

        fileTreeScroll.setBorder(BorderFactory.createLineBorder(this.getBackground(), 5));
        fileTreeScroll.setBackground(this.getBackground());

        this.add(fileTreeScroll, BorderLayout.CENTER);
    }

    public void setProjectPath(File projectPath) {
        fileTree.setStartingPath(projectPath);
        fileTree.revalidate();
        fileTree.repaint();
    }

    public void linkToEditorTabbedPane(EditorTabbedPane editorTabbedPane) {
        fileTree.addTreeSelectionListener(e -> {
            File selectedFile = new File(JFileTree.getTreePathAsFilePathString(e.getPath()));

            if (selectedFile.isFile()) {
                editorTabbedPane.openEditorTab(selectedFile);
                // TODO: Request focus to editor textPane
            }
        });
    }
}
