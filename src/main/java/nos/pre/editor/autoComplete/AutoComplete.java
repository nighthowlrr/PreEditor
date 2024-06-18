package nos.pre.editor.autoComplete;

import nos.pre.editor.UI.Editor.editingPane.EditingPane;
import nos.pre.editor.autoComplete.completions.BaseCompletion;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AutoComplete {
    private final int visibleRowCount = 5; // TODO: Implement scrolling

    private final EditingPane editingPane;
    private final ArrayList<BaseCompletion> completions;

    private final JPopupMenu popupMenu = new JPopupMenu();
    private final JList autoCompleteList = new JList();

    private String currentSubWord;
    private int currentInsertPosition;

    public AutoComplete(EditingPane editingPane, ArrayList<BaseCompletion> completions) {
        this.editingPane = editingPane;
        this.completions = completions;

        initUI();
        this.editingPane.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    if (popupMenu.isVisible()) {
                        insertSelection();

                        final int position = editingPane.getCaretPosition();
                        SwingUtilities.invokeLater(() -> {
                            try {
                                editingPane.getDocument().remove(position - 1, 1);// Remove newLine from enter key
                                // TODO: Manually removing newLine from pressing enter key ads lag, and new line is
                                //  visibly inserted before removed, making it look like a graphic glitch
                            } catch (BadLocationException e1) {
                                e1.printStackTrace();
                            }
                        });
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (popupMenu.isVisible()) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                        case KeyEvent.VK_DOWN:
                        case KeyEvent.VK_LEFT:
                        case KeyEvent.VK_RIGHT:
                            e.consume();
                            // To stop the cursor from moving as well. For unknown reasons, doesn't work in keyReleased, when it should
                            break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP && popupMenu.isVisible()) {
                    moveUp();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && popupMenu.isVisible()) {
                    moveDown();
                } else if (Character.isLetterOrDigit(e.getKeyChar())) {
                    showAutoCompleteMenuLater();
                } else if (Character.isWhitespace(e.getKeyChar()) || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    hideAutoCompleteMenu();
                }

                // TODO: for unknown reason, using this code in "keyPressed" makes enter key action stop working,
                //  but fixes cursor moving from up and down key...
            }
        });
    }

    private void initUI() {
        popupMenu.setOpaque(false);
        popupMenu.setBorder(BorderFactory.createEmptyBorder());
        popupMenu.setVisible(false); // just in case

        autoCompleteList.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        autoCompleteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        autoCompleteList.setVisibleRowCount(this.visibleRowCount);

        autoCompleteList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    insertSelection();
                }
            }
        });
    }

    private void showAutoCompleteMenu() {
        hideAutoCompleteMenu();

        int cursorPosition = this.editingPane.getCaretPosition();
        Point location;
        try {
            location = this.editingPane.modelToView2D(cursorPosition).getBounds().getLocation();
        } catch (BadLocationException e) {
            e.printStackTrace();
            return;
        }

        String editingPaneText = this.editingPane.getText();
        int start = Math.max(0, cursorPosition - 1);
        while (start > 0) {
            if (!Character.isWhitespace(editingPaneText.charAt(start))) {
                start --;
            } else {
                start ++;
                break;
            }
        }

        if (start > cursorPosition) {
            return;
        }

        String subWord = editingPaneText.substring(start, cursorPosition);
        if (subWord.length() < 2) {
            return;
        }

        createAutoCompleteMenu(subWord, cursorPosition, location);
        SwingUtilities.invokeLater(this.editingPane::requestFocusInWindow);
    }

    private void showAutoCompleteMenuLater() {
        SwingUtilities.invokeLater(this::showAutoCompleteMenu);
    }

    private void createAutoCompleteMenu(String subWord, int insertPosition, Point menuLocation) {
        popupMenu.setVisible(false);
        popupMenu.removeAll();

        String[] data = new String[10];
        for (int i = 0; i < data.length; i++) {
            data[i] = subWord + i;
        }

        autoCompleteList.setListData(data);
        autoCompleteList.setSelectedIndex(0);

        popupMenu.add(autoCompleteList);

        popupMenu.show(this.editingPane, menuLocation.x,
                this.editingPane.getBaseline(0, 0) + menuLocation.y);

        this.currentSubWord = subWord;
        this.currentInsertPosition = insertPosition;
    }


    private void insertSelection() {
        if (this.popupMenu.isVisible()) {
            if (this.autoCompleteList.getSelectedValue() != null) {
                try {
                    String selectedSuggestion = ((String) autoCompleteList.getSelectedValue()).substring(this.currentSubWord.length());
                    this.editingPane.getDocument().insertString(this.currentInsertPosition, selectedSuggestion, null);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }

                hideAutoCompleteMenu();
            }
        }
    }

    private void moveUp() {
        int index;
        if (autoCompleteList.getSelectedIndex() == 0) {
            index = autoCompleteList.getModel().getSize() - 1; // If first option already selected, move to last option
        } else {
            index = Math.max(autoCompleteList.getSelectedIndex() - 1, 0);
        }
        autoCompleteList.setSelectedIndex(index);
    }

    private void moveDown() {
        int index;
        if (autoCompleteList.getSelectedIndex() == autoCompleteList.getModel().getSize() - 1) {
            index = 0; // If last option already selected, move to first option.
        } else {
            index = Math.min(autoCompleteList.getSelectedIndex() + 1, autoCompleteList.getModel().getSize() - 1);
        }
        autoCompleteList.setSelectedIndex(index);
    }

    private void hideAutoCompleteMenu() {
        popupMenu.setVisible(false);
    }
}
