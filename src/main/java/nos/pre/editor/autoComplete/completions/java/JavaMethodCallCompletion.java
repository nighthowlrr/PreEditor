package nos.pre.editor.autoComplete.completions.java;

import nos.pre.editor.autoComplete.completions.BaseCompletion;
import nos.pre.editor.coderead.codeobjects.java.JavaMethod;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class JavaMethodCallCompletion extends BaseCompletion {
    private final JavaMethod javaMethod;

    public JavaMethodCallCompletion(@NotNull JavaMethod javaMethod) {
        super(javaMethod.getMethodName(), javaMethod.getReturnType(), DEFAULT_RELEVANCE,
                new ImageIcon(JavaMethodCallCompletion.class.getResource("/icons/autoComplete/java/javaMethodIcon.png")));
        this.javaMethod = javaMethod;
    }

    @Override
    public String getAutoCompleteMenuText() {
        StringBuilder menuText = new StringBuilder();

        menuText.append(this.javaMethod.getMethodName());

        if (! this.javaMethod.getParameters().isEmpty()) {
            menuText.append("(");
            for (JavaMethod.JavaParameter parameter : this.javaMethod.getParameters()) {
                menuText.append(parameter.toString()).append(", ");
            }
            menuText.delete(menuText.length() - 2, menuText.length());
            menuText.append(")");
        }

        menuText.append(" - ").append(this.getSummary());

        return menuText.toString();
    }

    @Override
    public boolean isCompletionMatching(String subWordToMatch) {
        return this.getCompletionText().startsWith(subWordToMatch);
    }
}
