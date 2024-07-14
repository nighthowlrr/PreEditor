package nos.pre.editor.coderead.java.codeobjects;

import com.sun.source.tree.VariableTree;
import nos.pre.editor.coderead.java.JavaModifiers;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JavaVariable {
    private final ArrayList<JavaAnnotation> variableAnnotations;

    private final JavaModifiers.AccessModifiers accessModifier;
    private final boolean isStatic;
    private final boolean isFinal;
    private final boolean isTransient;

    private transient final String variableType;
    private final String variableName;

    private final String variableInitValue;

    @Contract(pure = true)
    public JavaVariable(ArrayList<JavaAnnotation> variableAnnotations, JavaModifiers.AccessModifiers accessModifier,
                        boolean isStatic, boolean isFinal, boolean isTransient,
                        String variableType, String variableName, @Nullable String variableInitValue) {
        this.variableAnnotations = variableAnnotations;

        this.accessModifier = accessModifier;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.isTransient = isTransient;

        this.variableType = variableType;
        this.variableName = variableName;

        this.variableInitValue = variableInitValue;
    }

    // Getters ===
    public ArrayList<JavaAnnotation> getVariableAnnotations() {
        return variableAnnotations;
    }

    public JavaModifiers.AccessModifiers getAccessModifier() {
        return accessModifier;
    }
    public boolean isStatic() {
        return isStatic;
    }
    public boolean isFinal() {
        return isFinal;
    }
    public boolean isTransient() {
        return isTransient;
    }

    public String getVariableType() {
        return variableType;
    }
    public String getVariableName() {
        return variableName;
    }

    public String getVariableInitValue() {
        return variableInitValue;
    }
    // ===

    @Override
    public String toString() {
        StringBuilder variableString = new StringBuilder();

        for (JavaAnnotation annotation : this.variableAnnotations) {
            variableString.append(annotation.toString()).append("\n");
        }

        variableString.append(this.accessModifier.getAccessModifierText());

        if (isStatic) {
            variableString.append(" static");
        }
        if (isFinal) {
            variableString.append(" final");
        }
        if (isTransient) {
            variableString.append(" transient");
        }

        variableString.append(" ").append(this.variableType);
        variableString.append(" ").append(this.variableName);

        if (this.variableInitValue != null) {
            variableString.append(" = ").append(this.variableInitValue);
        }

        variableString.append(";");

        return variableString.toString();
    }

    /**
     * Creates and returns a new <code>JavaVariable</code> object using the <code>VariableTree</code> object passed.
     *
     * @param variableTree The <code>VariableTree</code> object to use to create a <code>JavaVariable</code> object.
     * @return Returns the new <code>JavaVariable</code> object.
     */
    @Contract("_ -> new")
    public static @NotNull JavaVariable createJavaVariableObject(@NotNull VariableTree variableTree) {
        ArrayList<JavaAnnotation> variableAnnotations = JavaAnnotation.getAnnotations(variableTree);

        Set<Modifier> modifiersTree = variableTree.getModifiers().getFlags();
        List<String> modifiersList = modifiersTree.stream().map(Modifier::toString).toList();

        JavaModifiers.AccessModifiers accessModifier;

        switch (modifiersList.getFirst().toLowerCase()) {
            case "public" -> accessModifier = JavaModifiers.AccessModifiers.PUBLIC;
            case "protected" -> accessModifier = JavaModifiers.AccessModifiers.PROTECTED;
            case "private" -> accessModifier = JavaModifiers.AccessModifiers.PRIVATE;
            default -> accessModifier = JavaModifiers.AccessModifiers.PACKAGE_PROTECTED;
        }

        boolean isStatic = false;
        boolean isFinal = false;
        boolean isTransient = false;

        if (modifiersList.contains("static")) {
            isStatic = true;
        }
        if (modifiersList.contains("final")) {
            isFinal = true;
        }
        if (modifiersList.contains("transient")) {
            isTransient = true;
        }

        String variableType = variableTree.getType().toString();
        String variableName = variableTree.getName().toString();
        String variableInitValue = variableTree.getInitializer() != null ? variableTree.getInitializer().toString() : null;

        // TODO: accessModifier
        return new JavaVariable(variableAnnotations, accessModifier, isStatic, isFinal, isTransient,
                variableType, variableName, variableInitValue);
    }
}
