package nos.pre.editor.languages.java;

import nos.pre.editor.autoComplete.completions.BaseCompletion;

import java.util.ArrayList;

public class JavaCompletions extends ArrayList<BaseCompletion> {
    public JavaCompletions() {
        this.addAll(JavaKeywords.getKeywordCompletions());
    }
}
