package nos.pre.editor.coderead;

import com.sun.source.tree.*;
import com.sun.source.util.JavacTask;
import nos.pre.editor.coderead.codeobjects.java.JavaAnnotation;
import nos.pre.editor.coderead.codeobjects.java.JavaMethod;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Modifier;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaCodeRead {
    private final File javaFile;

    @Contract(pure = true)
    public JavaCodeRead(File javaFile) {
        this.javaFile = javaFile;
    }

    // GET CLASS MEMBER TREES ===

    private List<? extends Tree> getClassMembersList() {
        try {
            JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(null, null, StandardCharsets.UTF_8);

            Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjectsFromFiles(List.of(this.javaFile));
            JavacTask javacTask = (JavacTask) javaCompiler.getTask(null, fileManager, null, null, null, javaFileObjects);
            Iterable<? extends CompilationUnitTree> javaFileObjectTrees = javacTask.parse();

            ClassTree classTree = (ClassTree) javaFileObjectTrees.iterator().next().getTypeDecls().getFirst();
            List<? extends Tree> classMemberList = classTree.getMembers();

            return classMemberList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private List<MethodTree> getClassMethods() {
        return this.getClassMembersList().stream()
                .filter(MethodTree.class::isInstance)
                .map(MethodTree.class::cast)
                .toList();
    }

    // CLASS METHODS ==

    public void listFirstMethod() {
        List<MethodTree> classMethods = this.getClassMethods();

        MethodTree firstMethod = classMethods.get(1);
        JavaMethod javaMethod = createJavaMethodObject(firstMethod);

        System.out.println("JavaCodeRead.listFirstMethod: \n" + javaMethod);
    }

    @Contract("_ -> new")
    private @NotNull JavaMethod createJavaMethodObject(@NotNull MethodTree methodTree) {
        // Annotations
        ArrayList<JavaAnnotation> methodAnnotations = getAnnotations(methodTree);

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
        ArrayList<JavaMethod.JavaParameter> javaParameters = new ArrayList<>();

        List<? extends VariableTree> methodParameters = methodTree.getParameters();
        for (VariableTree parameter : methodParameters) {
            javaParameters.add(createJavaParameterObject(parameter));
        }

        // TODO: accessModifier
        return new JavaMethod(methodAnnotations, null, isAbstract, isStatic, isTransient,
                returnType, methodName, javaParameters, exceptions,
                methodTree.getBody());
    }

    @Contract("_ -> new")
    private @NotNull JavaMethod.JavaParameter createJavaParameterObject(@NotNull VariableTree parameter) {
        ArrayList<JavaAnnotation> annotations = getAnnotations(parameter);

        return new JavaMethod.JavaParameter(annotations, parameter.getType().toString(), parameter.getName().toString());
    }

    // GET ANNOTATIONS ===

    private @NotNull ArrayList<JavaAnnotation> getAnnotations(@NotNull MethodTree methodTree) {
        return getAnnotations(methodTree.getModifiers());
    }
    private @NotNull ArrayList<JavaAnnotation> getAnnotations(@NotNull VariableTree variableTree) {
        return getAnnotations(variableTree.getModifiers());
    }
    private @NotNull ArrayList<JavaAnnotation> getAnnotations(@NotNull ModifiersTree modifiersTree) {
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
