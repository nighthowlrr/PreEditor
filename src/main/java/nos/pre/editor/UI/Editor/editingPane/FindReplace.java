package nos.pre.editor.UI.Editor.editingPane;

import nos.pre.editor.UI.Fonts;
import nos.pre.editor.defaultValues.UIColors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import templateUI.SwingComponents.jToggleButton;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class FindReplace {
    private final EditingPane editingPane;

    private boolean matchCase = false;

    private int currentSearchPos = 0;

    private final FindReplaceUIPanel findReplaceUIPanel;

    public FindReplace(EditingPane editingPane) {
        this.editingPane = editingPane;

        this.findReplaceUIPanel = new FindReplaceUIPanel();
    }

    public JPanel getUI() {
        return findReplaceUIPanel;
    }

    private void selectNextOccurrenceOfText(String textToFind) {
        if (! textToFind.isEmpty()) {

            if (! matchCase) {
                textToFind = textToFind.toLowerCase();
            }

            Document document = this.editingPane.getDocument();
            int textToFindLength = textToFind.length();

            boolean found = false;

            try {
                // Reset the search position if it is at the end of the document
                if (this.currentSearchPos + textToFindLength > document.getLength()) {
                    this.currentSearchPos = 0;
                }

                while (this.currentSearchPos + textToFindLength <= document.getLength()) {
                    String match = document.getText(this.currentSearchPos, textToFindLength);

                    if (doesTextMatch(match, textToFind)) {
                        found = true;
                        break;
                    } else this.currentSearchPos  ++;
                }

                // If a match has been found...
                if (found) {
                    // Scroll to where the text is.
                    Rectangle2D viewRect = this.editingPane.modelToView2D(this.currentSearchPos);
                    this.editingPane.scrollRectToVisible((Rectangle) viewRect);

                    // Select the text found.
                    this.editingPane.setSelection(this.currentSearchPos, this.currentSearchPos + textToFindLength);

                    // Move the search position beyond the current match
                    this.currentSearchPos += textToFindLength;
                }

                // TODO: When last occurrence is selected and "next occurrence" button is pressed, first time the button
                //  is pressed, it does nothing, second time it jumps to first occurrence.

            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    private void selectPreviousOccurrenceOfText(String textToFind) {
        if (! textToFind.isEmpty()) {

            if (! matchCase) {
                textToFind = textToFind.toLowerCase();
            }

            Document document = this.editingPane.getDocument();
            int textToFindLength = textToFind.length();

            boolean found = false;

            try {
                // Reset the search position if it is at the start of the document
                if (this.currentSearchPos <= 0) {
                    this.currentSearchPos = document.getLength() - textToFindLength;
                }

                while (this.currentSearchPos >= 0) {
                    String match = document.getText(this.currentSearchPos, textToFindLength);

                    if (doesTextMatch(match, textToFind)) {
                        found = true;
                        break;
                    } else this.currentSearchPos --;
                }

                // If a match has been found...
                if (found) {
                    // Scroll to where the text is.
                    Rectangle2D viewRect = this.editingPane.modelToView2D(this.currentSearchPos);
                    this.editingPane.scrollRectToVisible((Rectangle) viewRect);

                    // Select the text found.
                    this.editingPane.setSelection(this.currentSearchPos , this.currentSearchPos + textToFindLength);

                    // Move the search position above the current match
                    this.currentSearchPos -= textToFindLength;
                }

            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    private void setMatchCase(boolean matchCase) {
        if (this.matchCase != matchCase) {
            this.matchCase = matchCase;

            this.findReplaceUIPanel.setMatchCaseToggleButtonState(matchCase);

            selectNextOccurrenceOfText(this.findReplaceUIPanel.getFindTextFieldInput());
            // TODO: Updated selected text occurrences
        }
    }

    /**
     * Returns <code>true</code> if <code>textToMatch</code> does match <code>textToMatchTo</code>.
     * Case is taken into account if <code>FindReplace.matchCase</code> is <code>true</code>, otherwise case is ignored.
     * @param textToMatch The String to match
     * @param textToMatchTo The String to match <code>textToMatch</code> with
     * @return <code>True</code> if both Strings match.
     */
    private boolean doesTextMatch(String textToMatch, String textToMatchTo) {
        return this.matchCase ? textToMatch.equals(textToMatchTo) : textToMatch.equalsIgnoreCase(textToMatchTo);
    }


    private class FindReplaceUIPanel extends JPanel { // UI in separate class for organization
        private final int uiBarHeight = 30;
        private final int uiButtonWidth = 40;

        private final JPanel findTextFieldContainer = new JPanel(new BorderLayout(), true);
        private final JLabel findTextFieldLabel = new JLabel("Find: ");
        private final JTextField findTextField = new JTextField();

        private final JPanel findTextButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        private final jToggleButton matchCaseToggleButton = new jToggleButton("Cc", matchCase);
        private final JButton nextOccurrenceButton = new JButton("Next");
        private final JButton previousOccurrenceButton = new JButton("Prev");
        // TODO: Icons for buttons.

        private final JPanel replaceTextFieldContainer = new JPanel(new BorderLayout(), true);
        private final JLabel replaceTextFieldLabel = new JLabel("Replace: ");
        private final JTextField replaceTextField = new JTextField();

        public FindReplaceUIPanel() {
            super(new BorderLayout(), true);
            this.setBackground(UIColors.FIND_REPLACE_UI_BG);
            this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIColors.FIND_REPLACE_UI_BORDER));

            this.setUI();
            this.addFunctionality();
        }

        private void setUI() {
            // Find TextField ===
            findTextFieldLabel.setPreferredSize(new Dimension(60, 0));
            findTextFieldLabel.setForeground(UIColors.FIND_REPLACE_UI_LABELS_FG);
            findTextFieldLabel.setFont(Fonts.LeagueSpartan.deriveFont(Font.PLAIN, 14));

            findTextField.setBackground(UIColors.FIND_REPLACE_UI_BG);
            findTextField.setForeground(UIColors.FIND_REPLACE_UI_INPUT_FG);
            findTextField.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, UIColors.FIND_REPLACE_UI_BORDER));
            findTextField.setFont(Fonts.SourceCodePro_Regular.deriveFont(Font.PLAIN, 14));
            findTextField.setCaretColor(UIColors.EDITINGPANE_CARET_COLOR);

            findTextFieldContainer.setFocusable(false);
            findTextFieldContainer.setOpaque(false);
            findTextFieldContainer.setPreferredSize(new Dimension(0, uiBarHeight));

            matchCaseToggleButton.setPreferredSize(new Dimension(uiButtonWidth, uiBarHeight));
            matchCaseToggleButton.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, UIColors.FIND_REPLACE_UI_BORDER));
            matchCaseToggleButton.setBackground(UIColors.FIND_REPLACE_UI_BG);
            matchCaseToggleButton.setForeground(UIColors.FIND_REPLACE_UI_LABELS_FG);
            matchCaseToggleButton.setFocusable(false);
            matchCaseToggleButton.setSelectedColor(UIColors.FIND_REPLACE_UI_SELECTED_BUTTON_BG);
            matchCaseToggleButton.setToolTipText("Match Case");

            setUpFindUIButton(nextOccurrenceButton, "Select Next Occurrence");

            setUpFindUIButton(previousOccurrenceButton, "Select Previous Occurrence");

            findTextButtonPanel.setOpaque(false);
            findTextButtonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, UIColors.FIND_REPLACE_UI_BORDER));

            findTextButtonPanel.add(matchCaseToggleButton);
            findTextButtonPanel.add(nextOccurrenceButton);
            findTextButtonPanel.add(previousOccurrenceButton);

            findTextFieldContainer.add(findTextFieldLabel, BorderLayout.WEST);
            findTextFieldContainer.add(findTextField, BorderLayout.CENTER);
            findTextFieldContainer.add(findTextButtonPanel, BorderLayout.EAST);

            // Replace TextField ===
            replaceTextFieldLabel.setPreferredSize(new Dimension(60, 0));
            replaceTextFieldLabel.setForeground(UIColors.FIND_REPLACE_UI_LABELS_FG);
            replaceTextFieldLabel.setFont(Fonts.LeagueSpartan.deriveFont(Font.PLAIN, 14));

            replaceTextField.setBackground(UIColors.FIND_REPLACE_UI_BG);
            replaceTextField.setForeground(UIColors.FIND_REPLACE_UI_INPUT_FG);
            replaceTextField.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, UIColors.FIND_REPLACE_UI_BORDER));
            replaceTextField.setFont(Fonts.SourceCodePro_Regular.deriveFont(Font.PLAIN, 14));
            replaceTextField.setCaretColor(UIColors.EDITINGPANE_CARET_COLOR);

            replaceTextFieldContainer.setFocusable(false);
            replaceTextFieldContainer.setOpaque(false);
            replaceTextFieldContainer.setPreferredSize(new Dimension(0, uiBarHeight));

            replaceTextFieldContainer.add(replaceTextFieldLabel, BorderLayout.WEST);
            replaceTextFieldContainer.add(replaceTextField, BorderLayout.CENTER);

            // ====
            this.add(findTextFieldContainer, BorderLayout.NORTH);
            this.add(replaceTextFieldContainer, BorderLayout.SOUTH);
        }

        private void setUpFindUIButton(@NotNull JButton button, @Nullable String toolTipText) {
            button.setPreferredSize(new Dimension(uiButtonWidth, uiBarHeight));
            button.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIColors.FIND_REPLACE_UI_BORDER));
            button.setBackground(UIColors.FIND_REPLACE_UI_BG);
            button.setForeground(UIColors.FIND_REPLACE_UI_LABELS_FG);
            button.setFocusable(false);
            if (toolTipText != null) button.setToolTipText(toolTipText);
        }

        private void addFunctionality() {
            findTextField.addActionListener(e -> selectNextOccurrenceOfText(findTextField.getText()));

            matchCaseToggleButton.addActionListener(e -> setMatchCase(matchCaseToggleButton.isSelected()));
            nextOccurrenceButton.addActionListener(e -> selectNextOccurrenceOfText(findTextField.getText()));
            previousOccurrenceButton.addActionListener(e -> selectPreviousOccurrenceOfText(findTextField.getText()));
        }


        public String getFindTextFieldInput() {
            return replaceTextField.getText();
        }
        public void setMatchCaseToggleButtonState(boolean matchCaseEnabled) {
            matchCaseToggleButton.setSelected(matchCaseEnabled);
        }
    }
}
