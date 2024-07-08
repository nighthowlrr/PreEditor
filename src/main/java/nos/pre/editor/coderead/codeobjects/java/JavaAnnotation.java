package nos.pre.editor.coderead.codeobjects.java;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class JavaAnnotation {
    private final String annotationType;
    private final ArrayList<String> annotationArguments;

    public JavaAnnotation(@NotNull String annotationType, @NotNull ArrayList<String> annotationArguments) {
        this.annotationType = annotationType;
        this.annotationArguments = annotationArguments;
    }

    public ArrayList<String> getAnnotationArguments() {
        return annotationArguments;
    }

    public String getAnnotationType() {
        return annotationType;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("@").append(this.annotationType);

        if (! annotationArguments.isEmpty()) {
            stringBuilder.append("(");
            for (String argument : annotationArguments) {
                stringBuilder.append(argument).append(", ");
            }
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            stringBuilder.append(")");
        }

        return stringBuilder.toString();
    }
}
