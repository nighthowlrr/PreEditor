package nos.pre.editor.autoComplete.completions;

import org.jetbrains.annotations.NotNull;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class BaseCompletion implements Comparable<BaseCompletion> {
    public static final int DEFAULT_RELEVANCE = 0;
    private static final String DEFAULT_SUMMARY = null;

    private final String completionText;
    private final String summary;
    private final int relevance;

    public BaseCompletion(String completionText) {
        this(completionText, DEFAULT_SUMMARY, DEFAULT_RELEVANCE);
    }
    public BaseCompletion(String completionText, String summary) {
        this(completionText, summary, DEFAULT_RELEVANCE);
    }
    public BaseCompletion(@NotNull String completionText, @Nullable String summary, int relevance) {
        this.completionText = completionText;
        this.summary = summary;
        this.relevance = relevance;
    }

    public void insertCompletion(@NotNull Document document, int insertPosition, int subWordLength) throws BadLocationException {
        document.insertString(insertPosition, getCompletionText().substring(subWordLength), null);
    }

    @Override
    public int compareTo(@NotNull BaseCompletion c2) {
        if (c2 == this) {
            return 0;
        } else {
            // return c2 != null ? this.toString().compareToIgnoreCase(c2.toString()) : -1;
            return this.toString().compareToIgnoreCase(c2.toString());
        }
    }

    @Override
    public String toString() {
        if (this.summary != null) {
            return "{" + this.getClass().getSimpleName() + ": completionText: " + this.completionText +
                    ", summary: " + this.summary + ", relevance: " + this.relevance + "}";
        } else {
            return "{" + this.getClass().getSimpleName() + ": completionText: " + this.completionText +
                    ", relevance: " + this.relevance + "}";
        }
    }

    public String getCompletionText() {
        return completionText;
    }

    public String getSummary() {
        return summary;
    }

    public int getRelevance() {
        return relevance;
    }
}
