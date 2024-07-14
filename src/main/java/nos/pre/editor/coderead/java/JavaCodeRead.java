package nos.pre.editor.coderead.java;

import com.sun.source.tree.*;
import com.sun.source.util.JavacTask;
import nos.pre.editor.autoComplete.completions.CompletionList;
import nos.pre.editor.autoComplete.completions.java.JavaMethodCallCompletion;
import nos.pre.editor.autoComplete.completions.java.JavaVariableCallCompletion;
import nos.pre.editor.coderead.CodeRead;
import nos.pre.editor.coderead.java.codeobjects.JavaMethod;
import nos.pre.editor.coderead.java.codeobjects.JavaVariable;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JavaCodeRead extends CodeRead {
    public JavaCodeRead(File javaFile) {
        super(javaFile);
    }

    // GET CLASS MEMBER TREES ===
    private List<? extends Tree> getClassMembersList() {
        try {
            JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(null, null, StandardCharsets.UTF_8);

            Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjectsFromFiles(List.of(this.getCodeFile()));
            JavacTask javacTask = (JavacTask) javaCompiler.getTask(null, fileManager, null, null, null, javaFileObjects);
            Iterable<? extends CompilationUnitTree> javaFileObjectTrees = javacTask.parse();

            ClassTree classTree = (ClassTree) javaFileObjectTrees.iterator().next().getTypeDecls().getFirst();
            List<? extends Tree> classMemberList = classTree.getMembers();

            return classMemberList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Java Variables ===
    private List<VariableTree> getClassVariables() {
        return this.getClassMembersList().stream()
                .filter(VariableTree.class::isInstance)
                .map(VariableTree.class::cast)
                .toList();
    }
    private ArrayList<JavaVariable> getClassJavaVariables() {
        return this.getClassVariables()
                .stream()
                .map(JavaVariable::createJavaVariableObject)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    public CompletionList getClassVariableCompletions() {
        return this.getClassJavaVariables()
                .stream()
                .map(JavaVariableCallCompletion::new)
                .collect(Collectors.toCollection(CompletionList::new));
    }

    // Java Methods ===
    private List<MethodTree> getClassMethods() {
        return this.getClassMembersList().stream()
                .filter(MethodTree.class::isInstance)
                .map(MethodTree.class::cast)
                .toList();
    }
    private ArrayList<JavaMethod> getClassJavaMethods() {
        return this.getClassMethods()
                .stream()
                .map(JavaMethod::createJavaMethodObject)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    public CompletionList getClassMethodCompletions() {
        ArrayList<JavaMethod> classJavaMethods = getClassJavaMethods();
        CompletionList classMethodCompletions = new CompletionList();

        classJavaMethods.stream().map(JavaMethodCallCompletion::new).forEach(classMethodCompletions::add);

        return classMethodCompletions;
    }
}
