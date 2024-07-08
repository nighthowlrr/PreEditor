package nos.pre.editor.coderead.codeobjects.java;

import com.sun.source.tree.BlockTree;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class JavaMethod {
    private final ArrayList<JavaAnnotation> annotations;

    private final JavaModifiers.AccessModifiers accessModifier;

    private final boolean isStatic;
    private final boolean isAbstract;
    private final boolean isTransient;

    private final String returnType;
    private final String methodName;

    private final ArrayList<JavaParameter> parameters;

    private final ArrayList<String> thrownExceptions;

    public JavaMethod(ArrayList<JavaAnnotation> annotations, JavaModifiers.AccessModifiers accessModifier,
                      boolean isAbstract, boolean isStatic, boolean isTransient,
                      String returnType, String methodName,
                      ArrayList<JavaParameter> parameters, ArrayList<String> thrownExceptions,
                      BlockTree methodBody) { // getting BlockTree for future use.
        this.annotations = annotations;

        this.accessModifier = accessModifier;

        this.isAbstract = isAbstract;
        this.isStatic = isStatic;
        this.isTransient = isTransient;

        this.returnType = returnType;

        this.methodName = methodName;

        this.parameters = parameters;
        this.thrownExceptions = thrownExceptions;
    }

    @Override
    public String toString() {
        StringBuilder methodString = new StringBuilder();

        for (JavaAnnotation annotation : this.annotations) {
            methodString.append(annotation.toString()).append("\n");
        }

        methodString.append(this.accessModifier);

        if (isAbstract) {
            methodString.append(" abstract");
        }
        if (isStatic) {
            methodString.append(" static");
        }
        if (isTransient) {
            methodString.append(" transient");
        }

        methodString.append(" ").append(this.returnType);
        methodString.append(" ").append(this.methodName);

        methodString.append("(");
        if (! this.parameters.isEmpty()) {
            for (JavaParameter parameter : this.parameters) {
                methodString.append(parameter).append(", ");
            }
            methodString.delete(methodString.length() - 2, methodString.length());
        }
        methodString.append(")");

        if (! this.thrownExceptions.isEmpty()) {
            methodString.append(" ").append("throws ");
            for (String e : thrownExceptions) {
                methodString.append(e).append(", ");
            }
            methodString.delete(methodString.length() - 2, methodString.length());
        }

        return methodString.toString();
    }

    public record JavaParameter(ArrayList<JavaAnnotation> annotations, @NotNull String parameterType, @NotNull String parameterName) {
        @Override
        public @NotNull String toString() {
            StringBuilder string = new StringBuilder();

            if (! this.annotations.isEmpty()) {
                for (JavaAnnotation annotation : this.annotations) {
                    string.append(annotation).append(" ");
                }
            }

            string.append(this.parameterType).append(" ").append(this.parameterName);

            return string.toString();
        }
    }
}
