package nos.pre.editor.UI.toolWindows.projectTool;

import nos.pre.editor.UI.Editor.EditorTabbedPane;
import nos.pre.editor.UI.toolWindows.ToolWindow;
import nos.pre.editor.defaultValues.UIColors;
import org.jetbrains.annotations.NotNull;
import templateUI.SwingComponents.jScrollPane;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class ProjectToolWindow extends ToolWindow {
    private final File projectPath;

    public ProjectToolWindow(ToolWindowLocation toolWindowLocation, File projectPath) { // TODO: Final ProjectPath
        super("Project", toolWindowLocation);
        this.projectPath = projectPath;

        this.setLayout(new BorderLayout());
        this.setBackground(UIColors.PROJECT_TOOL_WINDOW_BG);

        this.addUIComponents();
    }


    // UI ===
    private JFileTree fileTree;
    private jScrollPane fileTreeScroll;

    private void addUIComponents() {
        this.add(this.getToolWindowNameLabel(), BorderLayout.PAGE_START);

        fileTree = new JFileTree(this.projectPath);
        fileTree.setBackground(this.getBackground());
        //  TODO: Set color for selected node (focused and unfocused)

        fileTreeScroll = new jScrollPane(fileTree);
        fileTreeScroll.setBorder(BorderFactory.createLineBorder(this.getBackground(), 5));
        fileTreeScroll.setBackground(this.getBackground());

        this.add(fileTreeScroll, BorderLayout.CENTER);
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

    private @NotNull DefaultMutableTreeNode addFileNodes(DefaultMutableTreeNode parentNode, @NotNull File directory) {
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(directory.getPath());
        if (parentNode != null) {
            parentNode.add(treeNode);
        }

        ArrayList<String> childNames = new ArrayList<>();
        if (directory.list() != null) {
            Collections.addAll(childNames, directory.list());
        }
        childNames.sort(String.CASE_INSENSITIVE_ORDER);

        ArrayList<File> folders = new ArrayList<>();
        ArrayList<File> files = new ArrayList<>();

        // Sort all files and folders
        for (String currentChildName : childNames) {
            String currentChildPath = directory.getPath() + File.separator + currentChildName;

            File file = new File(currentChildPath);
            if (file.isDirectory()) {
                folders.add(file);
            } else {
                files.add(file);
            }
        }

        // First add all folders
        for (File file : folders) {
            addFileNodes(treeNode, file);
        }

        // Add all files afterwards
        for (File file : files) {
            treeNode.add(new DefaultMutableTreeNode(file.getName()));
        }

        return treeNode;
    }
}
