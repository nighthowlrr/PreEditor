package nos.pre.editor.pretextpane;

import nos.pre.editor.defaultValues.UIFonts;
import nos.pre.editor.files.FileSaveListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class PreTextPaneMenu extends JPopupMenu {
    private final PreTextPane preTextPane;

    private final boolean isDesktopSupported;
    private final Desktop thisDesktop;

    public PreTextPaneMenu(PreTextPane preTextPane) {
        this.preTextPane = preTextPane;

        this.isDesktopSupported = Desktop.isDesktopSupported();
        if (isDesktopSupported) {
            this.thisDesktop = Desktop.getDesktop();
        } else this.thisDesktop = null;

        this.setupMenuItems();
        this.addListeners();

        this.addAllMenuItems();
    }

    private final ArrayList<JMenuItem> menuItems = new ArrayList<>();

    private final JMenuItem autocompleteItem = new JMenuItem("Show AutoComplete suggestions");

    private final JMenuItem undoItem = new JMenuItem("Undo");
    private final JMenuItem redoItem = new JMenuItem("Redo");

    private final JMenuItem saveItem = new JMenuItem("Save");
    private final JMenuItem reloadFromDiskItem = new JMenuItem("Reload file from disk");

    private final JMenuItem cutItem = new JMenuItem("Cut");
    private final JMenuItem copyItem = new JMenuItem("Copy");
    private final JMenuItem pasteItem = new JMenuItem("Paste");

    private final JMenu openInMenu = new JMenu("Open In");
    private final JMenuItem openInExplorerItem = new JMenuItem("File Explorer");
    private final JMenuItem openInAppItem = new JMenuItem("Associated Application");
    private final JMenuItem openInTerminalItem = new JMenuItem("Terminal (Not yet implemented)");

    private final JMenuItem googleSearchItem = new JMenuItem("Search with google");

    private void setupMenuItems() {
        autocompleteItem.addActionListener(e -> this.preTextPane.showAutoCompleteMenuLater());
        menuItems.add(autocompleteItem);

        if (this.preTextPane.isUndoRedoEnabled()) { // Only add Undo/Redo options if Undo/Redo is enabled
            undoItem.setEnabled(true);
            undoItem.addActionListener(e -> this.preTextPane.undo());
            menuItems.add(undoItem);

            redoItem.setEnabled(true);
            redoItem.addActionListener(e -> this.preTextPane.redo());
            menuItems.add(redoItem);
        }

        saveItem.setEnabled(false);
        saveItem.addActionListener(e -> this.preTextPane.saveFile());
        menuItems.add(saveItem);

        reloadFromDiskItem.addActionListener(e -> this.preTextPane.reloadFileFromDisk());
        menuItems.add(reloadFromDiskItem);

        cutItem.setEnabled(false);
        cutItem.addActionListener(e -> this.preTextPane.cut());
        menuItems.add(cutItem);

        copyItem.setEnabled(false);
        copyItem.addActionListener(e -> this.preTextPane.copy());
        menuItems.add(copyItem);

        pasteItem.addActionListener(e -> this.preTextPane.paste()); // TODO: Using paste item from menu pastes text two times...
        menuItems.add(pasteItem);

        openInExplorerItem.setEnabled(isDesktopSupported);
        openInExplorerItem.addActionListener(e -> {
            try {
                thisDesktop.browse(this.preTextPane.getOpenedFile().getParentFile().toURI());
                // TODO: Also select the file in file explorer instead of just opening the file's parent folder.
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        menuItems.add(openInExplorerItem);

        openInAppItem.setEnabled(isDesktopSupported);
        openInAppItem.addActionListener(e -> {
            try {
                thisDesktop.browse(this.preTextPane.getOpenedFile().toURI());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        menuItems.add(openInAppItem);

        openInTerminalItem.setEnabled(false);
        // TODO: Open the in-app terminal tool window
        menuItems.add(openInTerminalItem);

        menuItems.add(openInMenu);

        if (isDesktopSupported) {
            googleSearchItem.setEnabled(false);
            googleSearchItem.addActionListener(e -> {
                try {
                    thisDesktop.browse(new URI("https://www.google.com/search?q=" + this.preTextPane.getSelectedText()));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            });
            menuItems.add(googleSearchItem);
        }

        // Modify all menu items
        for (JMenuItem menuItem : menuItems) {
            menuItem.setFont(UIFonts.PRETEXTPANE_MENU_FONT);
        }
    }

    private void addAllMenuItems() {
        this.add(autocompleteItem);
        this.addSeparator();
        if (this.preTextPane.isUndoRedoEnabled()) {
            this.add(undoItem);
            this.add(redoItem);
            this.addSeparator();
        }
        this.add(saveItem);
        this.add(reloadFromDiskItem);
        this.addSeparator();
        this.add(cutItem);
        this.add(copyItem);
        this.add(pasteItem);
        this.addSeparator();

        openInMenu.add(openInExplorerItem);
        openInMenu.add(openInAppItem);
        openInMenu.add(openInTerminalItem);
        this.add(openInMenu);

        this.addSeparator();
        if (isDesktopSupported) {
            this.add(googleSearchItem);
        }
    }

    private void addListeners() {
        this.preTextPane.addCaretListener(e -> {
            boolean dotEqualsMark = e.getDot() == e.getMark();

            cutItem.setEnabled(! dotEqualsMark);
            copyItem.setEnabled(! dotEqualsMark);

            googleSearchItem.setEnabled(! dotEqualsMark);
        });

        this.preTextPane.addFileSaveListener(new FileSaveListener() {
            @Override
            public void fileSaved() {
                saveItem.setEnabled(false);
            }
            @Override
            public void fileUnsaved() {
                saveItem.setEnabled(true);
            }
        });
    }

    public void updateMenuItems() {
        this.removeAll();
        this.addAllMenuItems();
    }
}
