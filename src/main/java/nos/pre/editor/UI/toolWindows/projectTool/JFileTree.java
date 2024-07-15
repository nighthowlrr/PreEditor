package nos.pre.editor.UI.toolWindows.projectTool;

import nos.pre.editor.defaultValues.UIColors;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import javax.swing.tree.*;
import java.awt.*;
import java.io.File;
import java.util.*;

public class JFileTree extends JTree {
    private final File startingPath;
    public File getStartingPath() {
        return this.startingPath;
    }

    public JFileTree(File projectPath) {
        this.startingPath = projectPath;

        this.setModel(new DefaultTreeModel(addFileNodes(null, this.startingPath)));
        this.setCellRenderer(new FileTreeCellRenderer());
        this.setEditable(false);
    }

    private @NotNull DefaultMutableTreeNode addFileNodes(DefaultMutableTreeNode parentNode, @NotNull File directory) {
        DefaultMutableTreeNode treeNode;
        if (parentNode != null) { // If this treeNode will be parent node...
            treeNode = new DefaultMutableTreeNode(directory.getName());
            parentNode.add(treeNode);
        } else {
            treeNode = new DefaultMutableTreeNode(directory.getPath());
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

    /**
     * Takes <code>TreePath</code> from jFileTree
     * and converts it into a String of the path of the file the <code>TreeNode</code> represented.
     * @param path The <code>TreePath</code> to convert into a String.
     * @return The String path of the file.
     */
    public static @NotNull String getTreePathAsFilePathString(@NotNull TreePath path) {
        Object[] pathComponents = path.getPath(); // Get the path components as an array
        StringBuilder sb = new StringBuilder();

        // Iterate over the path components and append them to the string builder
        for (Object component : pathComponents) {
            sb.append(component.toString()).append("\\");
        }

        return sb.toString();
    }

    private static class FileTreeCellRenderer extends DefaultTreeCellRenderer {
        public FileTreeCellRenderer() {
            this.setBackgroundSelectionColor(UIColors.PROJECT_TOOL_WINDOW_FILESELECTED_FOCUSED);
            this.setBackgroundNonSelectionColor(new Color(0x0FFFFFF, true));

            this.setTextSelectionColor(Color.WHITE);
            this.setTextNonSelectionColor(Color.WHITE);

            this.setBorderSelectionColor(new Color(0x0FFFFFF, true));
        }
    }
}
