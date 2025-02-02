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

        this.add(new TemplateCompletion("else-if",
                "else if (", ") {}",
                "Add an else-if branch"));

        this.add(new TemplateCompletion("try-catch",
                "try {", "}\ncatch () {}",
                "Create a try catch block"));

        this.add(new TemplateCompletion("psf",
                "public static final ", "",
                "public static final"));
        this.add(new TemplateCompletion("prsf",
                "private static final ", "",
                "private static final"));

        // TODO: Proper indentation and bracket location

        this.add(new TemplateCompletion("St", "String", "", "String"));
        this.add(new TemplateCompletion("thr", "throw new", "", "throw new"));
    }
}
