package nos.pre.editor.autoComplete.completions.java;

import nos.pre.editor.autoComplete.completions.BaseCompletion;
import nos.pre.editor.coderead.java.codeobjects.JavaMethod;
import nos.pre.editor.pretextpane.PreTextPane;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.BadLocationException;

public class JavaMethodCallCompletion extends BaseCompletion {
    private final JavaMethod javaMethod;

    public JavaMethodCallCompletion(@NotNull JavaMethod javaMethod) {
        super(javaMethod.getMethodName(), javaMethod.getReturnType(), DEFAULT_RELEVANCE,
                new ImageIcon(JavaMethodCallCompletion.class.getResource("/icons/autoComplete/java/javaMethodIcon.png")));
        this.javaMethod = javaMethod;
    }

    @Override
    public void insertCompletion(@NotNull PreTextPane preTextPane, int insertPosition, int subWordLength) throws BadLocationException {
        preTextPane.getDocument().insertString(insertPosition,
                this.getCompletionText().substring(subWordLength) + "()", null);
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
