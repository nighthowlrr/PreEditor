package nos.pre.editor.UI.toolWindows;

import nos.pre.editor.UI.Colors;
import nos.pre.editor.UI.Editor.EditorView;
import org.jetbrains.annotations.NotNull;
import templateUI.SwingComponents.jScrollPane;
import templateUI.jFileTree;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;

public class ProjectToolWindow extends ToolWindow {
    private final jFileTree fileTree = new jFileTree();
    private final jScrollPane fileTreeScroll = new jScrollPane(fileTree);

    public ProjectToolWindow(ToolWindowLocation toolWindowLocation) {
        super(toolWindowLocation);
        this.setLayout(new BorderLayout());
        this.setBackground(Colors.projectToolBackground);

        this.addUIComponents();
    }

    private void addUIComponents() {
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

    public void linkToEditorPane(EditorView editorView) {
        fileTree.addTreeSelectionListener(e -> {
            File selectedFile = new File(getTreePathAsFilePathString(e.getPath()));

            if (selectedFile.isFile()) {
                editorView.openFile(selectedFile);
                editorView.requestFocus();
            }
        });
    }


    private @NotNull String getTreePathAsFilePathString(@NotNull TreePath path) {
        Object[] pathComponents = path.getPath(); // Get the path components as an array
        StringBuilder sb = new StringBuilder();

        // Iterate over the path components and append them to the string builder
        for (Object component : pathComponents) {
            sb.append(component.toString()).append("\\");
        }

        return sb.toString();
    }
}
