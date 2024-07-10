package nos.pre.editor.coderead.codeobjects.java;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    private final BlockTree methodBody;

    @Contract(pure = true)
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

        this.methodBody = methodBody;
    }

    // Getters & Setters ===
    public ArrayList<JavaAnnotation> getAnnotations() {
        return new ArrayList<>(this.annotations);
    }

    public JavaModifiers.AccessModifiers getAccessModifier() {
        return this.accessModifier;
    }

    public boolean isAbstract() {
        return this.isAbstract;
    }
    public boolean isStatic() {
        return this.isStatic;
    }
    public boolean isTransient() {
        return this.isTransient;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getReturnType() {
        return this.returnType;
    }

    public ArrayList<JavaMethod.JavaParameter> getParameters() {
        return new ArrayList<>(this.parameters);
    }
    public ArrayList<String> getThrownExceptions() {
        return new ArrayList<>(this.thrownExceptions);
    }

    public BlockTree getMethodBody() {
        return this.methodBody;
    }
    // ===

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


        public static @NotNull JavaParameter createJavaParameterObject(@NotNull VariableTree parameter) {
            ArrayList<JavaAnnotation> annotations = JavaAnnotation.getAnnotations(parameter);

            return new JavaMethod.JavaParameter(annotations, parameter.getType().toString(), parameter.getName().toString());
        }
    }

    // Static MethodTree methods ===

    @Contract("_ -> new")
    public static @NotNull JavaMethod createJavaMethodObject(@NotNull MethodTree methodTree) {
        // Annotations
        ArrayList<JavaAnnotation> methodAnnotations = JavaAnnotation.getAnnotations(methodTree);

        // Modifiers
        Set<Modifier> modifiersTree = methodTree.getModifiers().getFlags();
        List<String> modifiersList = modifiersTree.stream().map(Modifier::toString).toList();

        boolean isAbstract = false;
        boolean isStatic = false;
        boolean isTransient = false;

        if (modifiersList.contains("abstract")) {
            isAbstract = true;
        }
        if (modifiersList.contains("static")) {
            isStatic = true;
        }
        if (modifiersList.contains("transient")) {
            isTransient = true;
        }

        // Info
        String methodName = methodTree.getName().toString();
        String returnType = methodTree.getReturnType().toString();

        // Exceptions
        ArrayList<String> exceptions = methodTree.getThrows()
                .stream()
                .map(Object::toString)
                .collect(Collectors.toCollection(ArrayList::new));

        // Parameters
        ArrayList<JavaMethod.JavaParameter> javaParameters = methodTree.getParameters()
                .stream()
                .map(JavaParameter::createJavaParameterObject)
                .collect(Collectors.toCollection(ArrayList::new));

        // TODO: accessModifier
        return new JavaMethod(methodAnnotations, null, isAbstract, isStatic, isTransient,
                returnType, methodName, javaParameters, exceptions,
                methodTree.getBody());
    }

}
