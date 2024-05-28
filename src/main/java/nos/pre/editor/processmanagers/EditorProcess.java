package nos.pre.editor.processmanagers;

import nos.pre.editor.UI.EditorWindow.EditorFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class EditorProcess { // TODO: Convert to record class if possible
    private final File projectPath;
    public File getProjectPath() {
        return projectPath;
    }

    private EditorFrame editorFrame = null;
    public @Nullable EditorFrame getEditorFrame() {
        return editorFrame;
    }

    public void createEditorFrame() {
        this.editorFrame = new EditorFrame(this);
    }

    public EditorProcess(@NotNull File projectPath) {
        if (projectPath.isDirectory()) {
            this.projectPath = projectPath;
        } else throw new RuntimeException(this + " | EditorProcess: ProjectPath must point to a directory, not a file.");
    }
}
