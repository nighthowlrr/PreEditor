package nos.pre.editor.UI.toolWindows.projectTool;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.*;

public class JFileTree extends JTree {
    private File startingPath;

    public JFileTree() {
        this.setEditable(false);
    }

    public void setStartingPath(File startingPath) {
        this.startingPath = startingPath;
        FileSystemModel fileSystemModel = new FileSystemModel(startingPath);
        this.setModel(fileSystemModel);
    }
    public File getStartingPath() {
        return this.startingPath;
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


    private static class FileSystemModel implements TreeModel {
        private final File rootPath;

        private final List<TreeModelListener> listeners = new ArrayList<>();

        public FileSystemModel(File startingPath) {
            this.rootPath = startingPath;
        }

        /**
         * Returns the path for the root file
         * @return The path for the root file.
         */
        @Override
        public Object getRoot() {
            return this.rootPath;
        }

        @Override
        public Object getChild(Object parent, int index) {
            File parentFile = (File) parent;

            String[] children = parentFile.list();

            if (children != null) {
                return new TreeFile(parentFile, children[index]);
            } else return null;
        }

        @Override
        public int getChildCount(Object parent) {
            File parentFile = (File) parent;

            if (parentFile.isDirectory()) {
                String[] children = parentFile.list();
                if (children != null) {
                    return children.length;
                }
            }
            return 0;
        }

        @Override
        public boolean isLeaf(Object node) {
            File file = (File) node;
            return file.isFile();
        }

        @Override
        public void valueForPathChanged(@NotNull TreePath path, Object newValue) {
            File oldFile = (File) path.getLastPathComponent();
            String oldFileParentPath = oldFile.getParent();

            String newFileName = (String) newValue;
            File targetFile = new File(oldFileParentPath, newFileName);
            boolean renamed = oldFile.renameTo(targetFile);

            if (renamed) {
                File parent = new File(oldFileParentPath);
                int[] changedChildrenIndices = {
                        getIndexOfChild(parent, targetFile)
                };
                Object[] changedChildren = {
                        targetFile
                };
                fireTreeNodesChanged(path.getParentPath(), changedChildrenIndices, changedChildren);
            }
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            File parentFile = (File) parent;
            File childFile = (File) child;

            String[] children = parentFile.list();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    if (Objects.equals(childFile, new File(children[i]))) {
                        return i;
                    }
                }
            }
            return -1;
        }

        @Override
        public void addTreeModelListener(TreeModelListener l) {
            listeners.add(l);
        }
        @Override
        public void removeTreeModelListener(TreeModelListener l) {
            listeners.remove(l);
        }

        private void fireTreeNodesChanged(TreePath parentPath, int[] indices, Object[] children) {
            TreeModelEvent event = new TreeModelEvent(this, parentPath, indices, children);
            Iterator<TreeModelListener> iterator = listeners.iterator();
            TreeModelListener listener;
            while (iterator.hasNext()) {
                listener = iterator.next();
                listener.treeNodesChanged(event);
            }
        }

        private static class TreeFile extends File {
            public TreeFile(File parent, String child) {
                super(parent, child);
            }

            @Contract(pure = true)
            public @NotNull String toString() {
                return getName();
                // Otherwise, each node would show the full path of the file.
            }
        }
    }
}
