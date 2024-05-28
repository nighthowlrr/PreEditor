package nos.pre.editor.processmanagers;

import nos.pre.editor.UI.EditorWindow.NewEditorFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class EditorProcess { // TODO: Convert to record class if possible
    private final File projectPath;
    public File getProjectPath() {
        return projectPath;
    }

    private NewEditorFrame editorFrame = null;
    public @Nullable NewEditorFrame getEditorFrame() {
        return editorFrame;
    }

    public void createEditorFrame() {
        this.editorFrame = new NewEditorFrame(this);
    }

    public EditorProcess(@NotNull File projectPath) {
        if (projectPath.isDirectory()) {
            this.projectPath = projectPath;
        } else throw new RuntimeException(this + " | EditorProcess: ProjectPath must point to a directory, not a file.");
    }
}
