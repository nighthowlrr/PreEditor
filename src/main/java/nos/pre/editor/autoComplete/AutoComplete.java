package nos.pre.editor.autoComplete;

import nos.pre.editor.UI.Editor.editingPane.PreTextPane;
import nos.pre.editor.UI.Fonts;
import nos.pre.editor.autoComplete.completions.BaseCompletion;
import nos.pre.editor.autoComplete.completions.CompletionList;
import nos.pre.editor.defaultValues.KeyboardShortcuts;
import nos.pre.editor.defaultValues.UIColors;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;

public class AutoComplete {
    private final int visibleRowCount = 5; // TODO: Implement scrolling

    private final PreTextPane preTextPane;
    private final CompletionList completions;

    private final JPopupMenu popupMenu = new JPopupMenu();
    private final JList<BaseCompletion> autoCompleteList = new JList<>();

    private String currentSubWord;
    private int currentInsertPosition;

    public AutoComplete(PreTextPane preTextPane, CompletionList completions) {
        this.preTextPane = preTextPane;
        this.completions = completions;

        initUI();

        this.preTextPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (popupMenu.isVisible()) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        insertSelection();

                        final int position = preTextPane.getCaretPosition();
                        SwingUtilities.invokeLater(() -> {
                            try {
                                preTextPane.getDocument().remove(position - 1, 1); // Remove newLine from enter key
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
                            moveUp();
                            e.consume();
                            break;
                        case KeyEvent.VK_DOWN:
                            moveDown();
                            e.consume();
                            break;
                        case KeyEvent.VK_LEFT:
                        case KeyEvent.VK_RIGHT:
                            e.consume();
                            // To stop the cursor from moving left or right.
                            break;
                        case KeyEvent.VK_HOME:
                            jumpToTop();
                            e.consume();
                            break;
                        case KeyEvent.VK_END:
                            jumpToBottom();
                            e.consume();
                            break;
                    }

                    // TODO: Calling e.consume(); outside of switch statement causes autocomplete to not insert
                    //  last character of completion...??
                }

                if (Character.isLetterOrDigit(e.getKeyChar())) {
                    showAutoCompleteMenuLater();
                } else if ((Character.isWhitespace(e.getKeyChar()) && e.getKeyChar() != KeyEvent.VK_ENTER) // "\n" also counts as whitespace
                        || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    hideAutoCompleteMenu();
                }
            }
        });

        String autoCompleteKey = "AutoComplete";

        this.preTextPane.getActionMap().put(autoCompleteKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAutoCompleteMenuLater();
            }
        });
        this.preTextPane.getInputMap().put(KeyboardShortcuts.EDITINGPANE_AUTOCOMPLETE, autoCompleteKey);
    }

    private void initUI() {
        popupMenu.setOpaque(false);
        popupMenu.setBorder(BorderFactory.createEmptyBorder());
        popupMenu.setVisible(false); // just in case

        autoCompleteList.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        autoCompleteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        autoCompleteList.setVisibleRowCount(this.visibleRowCount);
        autoCompleteList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                // TODO: Proper list cell renderer component

                final BaseCompletion completion = (BaseCompletion) value;

                String text = completion.getAutoCompleteMenuText();

                JComponent component = (JComponent) super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
                if (isSelected) {
                    component.setBackground(UIColors.AUTOCOMPLETE_MENU_SELECTED_BG);
                } else component.setBackground(UIColors.AUTOCOMPLETE_MENU_BG);
                component.setForeground(UIColors.AUTOCOMPLETE_MENU_FG);
                component.setFont(Fonts.SourceCodePro_Regular.deriveFont(14F)); // TODO: UIFonts defaultValues Class
                component.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

                return component;
            }
        });

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

        int cursorPosition = this.preTextPane.getCaretPosition();
        Point location;
        try {
            location = this.preTextPane.modelToView2D(cursorPosition).getBounds().getLocation();
        } catch (BadLocationException e) {
            e.printStackTrace();
            return;
        }

        String editingPaneText = this.preTextPane.getText();
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
        if (subWord.length() < 1) { // How long subWord should be before autoComplete is shown.
            return;
        }

        createAutoCompleteMenu(subWord, cursorPosition, location);
        SwingUtilities.invokeLater(this.preTextPane::requestFocusInWindow);
    }

    private void showAutoCompleteMenuLater() {
        SwingUtilities.invokeLater(this::showAutoCompleteMenu);
    }

    private void createAutoCompleteMenu(String subWord, int insertPosition, Point menuLocation) {
        hideAutoCompleteMenu();
        popupMenu.removeAll();

        BaseCompletion[] matchingCompletions = CompletionList.getCompletionsAsArray(this.completions.getMatchingCompletions(subWord));
        if (matchingCompletions.length == 0) {
            return;
        }

        autoCompleteList.setListData(matchingCompletions);
        autoCompleteList.setSelectedIndex(0);

        popupMenu.add(autoCompleteList);

        popupMenu.show(this.preTextPane, menuLocation.x,
                this.preTextPane.getBaseline(0, 0) + menuLocation.y);

        this.currentSubWord = subWord;
        this.currentInsertPosition = insertPosition;
    }


    private void insertSelection() {
        if (this.popupMenu.isVisible()) {
            if (this.autoCompleteList.getSelectedValue() != null) {
                try {
                    BaseCompletion selectedCompletion = autoCompleteList.getSelectedValue();
                    selectedCompletion.insertCompletion(this.preTextPane, this.currentInsertPosition, this.currentSubWord.length());

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

    private void jumpToTop() {
        autoCompleteList.setSelectedIndex(0);
    }

    private void jumpToBottom() {
        autoCompleteList.setSelectedIndex(autoCompleteList.getModel().getSize() - 1);
    }

    private void hideAutoCompleteMenu() {
        popupMenu.setVisible(false);
    }
}
