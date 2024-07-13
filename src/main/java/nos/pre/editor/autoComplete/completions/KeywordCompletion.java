package nos.pre.editor.autoComplete.completions;

import nos.pre.editor.pretextpane.PreTextPane;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.BadLocationException;

public class KeywordCompletion extends BaseCompletion {

    public KeywordCompletion(String keyword) {
        super(keyword, BaseCompletion.NULL_SUMMARY, BaseCompletion.DEFAULT_RELEVANCE,
                new ImageIcon(KeywordCompletion.class.getResource("/icons/autoComplete/keywordIcon.png")));
    }

    @Override
    public void insertCompletion(@NotNull PreTextPane preTextPane, int insertPosition, int subWordLength) throws BadLocationException {
        preTextPane.getDocument().insertString(insertPosition, this.getCompletionText().substring(subWordLength), null);
    }

    @Override
    public String getAutoCompleteMenuText() {
        return this.getCompletionText();
    }

    @Override
    public boolean isCompletionMatching(String subWordToMatch) {
        return this.getCompletionText().startsWith(subWordToMatch);
    }

    @Override
    public int compareTo(@NotNull BaseCompletion c2) {
        if (c2 instanceof KeywordCompletion keywordCompletion2) {
            return this.getCompletionText().compareTo(keywordCompletion2.getCompletionText());
        } else return 0;
    }
}
