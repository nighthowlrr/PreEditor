package nos.pre.editor.languages.java;

import nos.pre.editor.autoComplete.completions.CompletionList;

import java.util.ArrayList;

public class JavaCompletions extends ArrayList<BaseCompletion> {
public class JavaCompletions extends CompletionList {
    public JavaCompletions() {
        this.addAll(JavaKeywords.getKeywordCompletions());
    }
}
