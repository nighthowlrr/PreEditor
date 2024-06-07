package nos.pre.editor.UI.Editor;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class EditingPaneMenu extends JPopupMenu {
    private final EditingPane editingPane;

    private final boolean isDesktopSupported;
    private final Desktop thisDesktop;

    public EditingPaneMenu(EditingPane editingPane) {
        this.editingPane = editingPane;

        this.isDesktopSupported = Desktop.isDesktopSupported();
        if (isDesktopSupported) {
            this.thisDesktop = Desktop.getDesktop();
        } else this.thisDesktop = null;

        this.addMenuItems();
        this.addListeners();
    }

    private final JMenuItem undoItem = new JMenuItem("Undo");
    private final JMenuItem redoItem = new JMenuItem("Redo");

    private final JMenuItem cutItem = new JMenuItem("Cut");
    private final JMenuItem copyItem = new JMenuItem("Copy");
    private final JMenuItem pasteItem = new JMenuItem("Paste");

    private final JMenu openInMenu = new JMenu("Open In");
    private final JMenuItem openInExplorerItem = new JMenuItem("File Explorer");
    private final JMenuItem openInAppItem = new JMenuItem("Associated Application");
    private final JMenuItem openInTerminalItem = new JMenuItem("Terminal (Not yet implemented)");

    private void addMenuItems() {
        undoItem.setEnabled(false);
        this.add(undoItem);

        redoItem.setEnabled(false);
        this.add(redoItem);

        this.addSeparator();

        cutItem.setEnabled(false);
        cutItem.addActionListener(e -> this.editingPane.cut());
        this.add(cutItem);

        copyItem.setEnabled(false);
        copyItem.addActionListener(e -> this.editingPane.copy());
        this.add(copyItem);

        pasteItem.addActionListener(e -> this.editingPane.paste());
        this.add(pasteItem);

        this.addSeparator();

        openInExplorerItem.setEnabled(isDesktopSupported);
        openInExplorerItem.addActionListener(e -> {
            try {
                thisDesktop.browse(this.editingPane.getOpenedFile().getParentFile().toURI());
                // TODO: Also select the file in file explorer instead of just opening the file's parent folder.
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        openInMenu.add(openInExplorerItem);

        openInAppItem.setEnabled(isDesktopSupported);
        openInAppItem.addActionListener(e -> {
            try {
                thisDesktop.browse(this.editingPane.getOpenedFile().toURI());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        openInMenu.add(openInAppItem);

        openInTerminalItem.setEnabled(false);
        // TODO: Open the in-app terminal tool window
        openInMenu.add(openInTerminalItem);

        this.add(openInMenu);
    }

    private void addListeners() {
        this.editingPane.addCaretListener(e -> {
            boolean dotEqualsMark = e.getDot() == e.getMark();

            cutItem.setEnabled(! dotEqualsMark);
            copyItem.setEnabled(! dotEqualsMark);
        });
    }
}
