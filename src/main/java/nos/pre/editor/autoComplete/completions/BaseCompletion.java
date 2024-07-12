package nos.pre.editor.autoComplete.completions;

import nos.pre.editor.pretextpane.PreTextPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.BadLocationException;

public abstract class BaseCompletion implements Comparable<BaseCompletion> {
    // Default values
    public static final int DEFAULT_RELEVANCE = 0;
    private static final String NULL_SUMMARY = null;
    private static final ImageIcon NULL_ICON = null;

    // --

    private final String completionText;
    private final String summary;
    private final int relevance;

    private final ImageIcon icon;

    public BaseCompletion(String completionText) {
        this(completionText, NULL_SUMMARY, DEFAULT_RELEVANCE);
    }
    public BaseCompletion(String completionText, String summary) {
        this(completionText, summary, DEFAULT_RELEVANCE);
    }
    public BaseCompletion(@NotNull String completionText, @Nullable String summary, int relevance) {
        this(completionText, summary, relevance, NULL_ICON);
    }
    public BaseCompletion(@NotNull String completionText, @Nullable String summary, int relevance, @Nullable ImageIcon icon) {
        this.completionText = completionText;
        this.summary = summary;
        this.relevance = relevance;
        this.icon = icon;
    }

    public void insertCompletion(@NotNull PreTextPane preTextPane, int insertPosition, int subWordLength) throws BadLocationException {
        preTextPane.getDocument().insertString(insertPosition, this.completionText.substring(subWordLength), null);
    }

    public String getAutoCompleteMenuText() {
        if (this.summary != null) {
            return this.completionText + "  -  " + this.summary;
        } else return this.completionText;
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

    public ImageIcon getIcon() {
        return icon;
    }

    public abstract boolean isCompletionMatching(String subWordToMatch);
}
