package nos.pre.editor.coderead.java.codeobjects;

import com.sun.source.tree.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JavaAnnotation {
    private final String annotationType;
    private final ArrayList<String> annotationArguments;

    @Contract(pure = true)
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


    public static @NotNull ArrayList<JavaAnnotation> getAnnotations(@NotNull MethodTree methodTree) {
        return JavaAnnotation.getAnnotations(methodTree.getModifiers());
    }
    public static @NotNull ArrayList<JavaAnnotation> getAnnotations(@NotNull VariableTree variableTree) {
        return JavaAnnotation.getAnnotations(variableTree.getModifiers());
    }
    public static @NotNull ArrayList<JavaAnnotation> getAnnotations(@NotNull ModifiersTree modifiersTree) {
        ArrayList<JavaAnnotation> javaAnnotations = new ArrayList<>();

        List<? extends AnnotationTree> annotationTrees = modifiersTree.getAnnotations();

        for (AnnotationTree annotationTree : annotationTrees) {
            List<? extends ExpressionTree> arguments = annotationTree.getArguments();

            ArrayList<String> annotationArguments = arguments.stream()
                    .map(Object::toString)
                    .collect(Collectors.toCollection(ArrayList::new));

            javaAnnotations.add(new JavaAnnotation(annotationTree.getAnnotationType().toString(), annotationArguments));
        }

        return javaAnnotations;
    }
}
