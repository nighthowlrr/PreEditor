package nos.pre.editor.autoComplete.completions;

public class KeywordCompletion extends BaseCompletion {

    public KeywordCompletion(String keyword) {
        super(keyword);
    }

    @Override
    public boolean isCompletionMatching(String subWordToMatch) {
        return this.getCompletionText().startsWith(subWordToMatch);
    }
}
