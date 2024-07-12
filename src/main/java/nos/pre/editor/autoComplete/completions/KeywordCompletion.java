package nos.pre.editor.autoComplete.completions;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class KeywordCompletion extends BaseCompletion {

    public KeywordCompletion(String keyword) {
        super(keyword, null, BaseCompletion.DEFAULT_RELEVANCE,
                new ImageIcon(KeywordCompletion.class.getResource("/icons/autoComplete/keywordIcon.png")));
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
