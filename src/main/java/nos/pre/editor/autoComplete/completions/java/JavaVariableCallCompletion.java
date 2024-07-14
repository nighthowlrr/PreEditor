package nos.pre.editor.autoComplete.completions.java;

import nos.pre.editor.autoComplete.completions.BaseCompletion;
import nos.pre.editor.coderead.codeobjects.java.JavaVariable;
import nos.pre.editor.pretextpane.PreTextPane;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.BadLocationException;

public class JavaVariableCallCompletion extends BaseCompletion {
    private final JavaVariable javaVariable;

    public JavaVariableCallCompletion(@NotNull JavaVariable javaVariable) {
        super(javaVariable.getVariableName(), javaVariable.getVariableType()); // TODO: Icon
        this.javaVariable = javaVariable;
    }

    @Override
    public void insertCompletion(@NotNull PreTextPane preTextPane, int insertPosition, int subWordLength) throws BadLocationException {
        preTextPane.getDocument().insertString(insertPosition, this.getCompletionText().substring(subWordLength), null);
    }

    @Override
    public String getAutoCompleteMenuText() {
        return this.javaVariable.getVariableName() + " - " + this.javaVariable.getVariableType();
    }

    @Override
    public boolean isCompletionMatching(String subWordToMatch) {
        return this.getCompletionText().startsWith(subWordToMatch);
    }
}
