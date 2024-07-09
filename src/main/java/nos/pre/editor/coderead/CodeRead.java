package nos.pre.editor.coderead;

import org.jetbrains.annotations.Contract;

import java.io.File;

public class CodeRead {
    private final File codeFile;
    public File getCodeFile() {
        return codeFile;
    }

    @Contract(pure = true)
    public CodeRead(File codeFile) {
        this.codeFile = codeFile;
    }
}
