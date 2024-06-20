package nos.pre.editor.languages.java;

import nos.pre.editor.autoComplete.completions.CompletionList;
import nos.pre.editor.autoComplete.completions.TemplateCompletion;

public class JavaCompletions extends CompletionList {
    public JavaCompletions() {
        this.addAll(JavaKeywords.getKeywordCompletions());

        this.add(new TemplateCompletion("fori",
                "for (int i = 0; i < ", "; i++) {}",
                "Create iteration \"for\" loop"));
        // TODO: Check if variable i is used, if yes, then use j ...

        this.add(new TemplateCompletion("sout", "System.out.println(", ");",
                "Print a string to System.out"));

        this.add(new TemplateCompletion("main",
                "public static void main(String[] args) {", "}",
                "main() method declaration"));
        this.add(new TemplateCompletion("psvm",
                "public static void main(String[] args) {", "}",
                "main() method declaration"));
    }
}
