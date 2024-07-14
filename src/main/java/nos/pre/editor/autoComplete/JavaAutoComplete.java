package nos.pre.editor.autoComplete;

import nos.pre.editor.autoComplete.completions.CompletionList;
import nos.pre.editor.coderead.java.JavaCodeRead;
import nos.pre.editor.languages.java.JavaCompletions;
import nos.pre.editor.pretextpane.PreTextPane;
import org.jetbrains.annotations.NotNull;

public class JavaAutoComplete extends AutoComplete {
    public JavaAutoComplete(PreTextPane preTextPane, JavaCodeRead codeRead) {
        super(preTextPane, new JavaCompletions(), codeRead);
    }

    @Override
    protected @NotNull CompletionList getAllMatchingCompletions(String subWord) {
        JavaCodeRead javaCodeRead = (JavaCodeRead) this.getCodeRead();

        CompletionList matchingBasicCompletions = this.getBasicCompletions().getMatchingCompletions(subWord);
        CompletionList matchingMethodCompletions = javaCodeRead.getClassMethodCompletions().getMatchingCompletions(subWord);
        CompletionList matchingVariableCompletions = javaCodeRead.getClassVariableCompletions().getMatchingCompletions(subWord);

        return CompletionList.combineAndSortCompletions(matchingBasicCompletions, matchingMethodCompletions, matchingVariableCompletions);
    }
}
