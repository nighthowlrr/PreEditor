package nos.pre.editor.autoComplete.completions;

import nos.pre.editor.pretextpane.PreTextPane;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.BadLocationException;

public class TemplateCompletion extends BaseCompletion {
    private final String inputText;
    private final String completionTextBeforeCaret;
    private final String completionTextAfterCaret;

    public TemplateCompletion(String inputText, String completionTextBeforeCaret, String completionTextAfterCaret,
                              String summary) {
        this(inputText, completionTextBeforeCaret, completionTextAfterCaret, summary, BaseCompletion.DEFAULT_RELEVANCE);
    }

    public TemplateCompletion(String inputText, String completionTextBeforeCaret, String completionTextAfterCaret,
                              String summary, int relevance) {
        super(completionTextBeforeCaret + completionTextAfterCaret, summary, relevance);
        this.inputText = inputText;
        this.completionTextBeforeCaret = completionTextBeforeCaret;
        this.completionTextAfterCaret = completionTextAfterCaret;
    }

    @Override
    public void insertCompletion(@NotNull PreTextPane preTextPane, int insertPosition, int subWordLength) throws BadLocationException {
        // remove subWord typed
        preTextPane.getDocument().remove(insertPosition - subWordLength, subWordLength);

        // get new insert Position, since subWord is removed.
        int newInsertPosition = insertPosition - subWordLength;

        // Insert completion Text before and after caret
        preTextPane.getDocument().insertString(newInsertPosition, completionTextBeforeCaret, null);
        final int newCaretPosition = preTextPane.getCaretPosition();
        preTextPane.getDocument().insertString(newCaretPosition, completionTextAfterCaret, null);
        preTextPane.setCaretPosition(newCaretPosition);
    }

    @Override
    public boolean isCompletionMatching(String subWordToMatch) {
        return this.getInputText().startsWith(subWordToMatch);
    }

    @Override
    public String getAutoCompleteMenuText() {
        return this.inputText + "  -  " + this.getSummary();
    }

    @Override
    public String toString() {
        return "{" + this.getClass().getSimpleName() + ": inputText: " + this.inputText +
                ", completionTextBeforeCaret: " + this.completionTextBeforeCaret +
                ", completionTextAfterCaret: " + this.completionTextAfterCaret + ", summary: " + this.getSummary() +
                ", relevance: " + this.getRelevance() + "}";
    }

    @Override
    public int compareTo(@NotNull BaseCompletion c2) {
        if (c2 instanceof TemplateCompletion templateCompletion) {
            return this.getInputText().compareTo(templateCompletion.getInputText());
        } else return 0;
    }

    public String getInputText() {
        return inputText;
    }

    public String getCompletionTextBeforeCaret() {
        return completionTextBeforeCaret;
    }

    public String getCompletionTextAfterCaret() {
        return completionTextAfterCaret;
    }
}
