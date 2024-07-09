package nos.pre.editor.coderead;

import com.sun.source.tree.*;
import com.sun.source.util.JavacTask;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
    private List<MethodTree> getClassMethods() {
        return this.getClassMembersList().stream()
                .filter(MethodTree.class::isInstance)
                .map(MethodTree.class::cast)
                .toList();
    }
}
