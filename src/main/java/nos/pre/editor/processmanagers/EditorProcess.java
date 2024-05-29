package nos.pre.editor.processmanagers;

import nos.pre.editor.UI.EditorWindow.EditorFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class EditorProcess { // TODO: Convert to record class if possible
    private final File projectPath;

    /**
     * @return <code>File</code> object pointing to the project path set for this Editor Process
     */
    public File getProjectPath() {
        return projectPath;
    }

    private EditorFrame editorFrame = null;

    /**
     * @return the EditorFrame for this Editor Process.
     */
    public @Nullable EditorFrame getEditorFrame() {
        return editorFrame;
    }

    /**
     * Creates an <code>EditorFrame</code> for this Editor Process and makes it visible.
     */
    public void createEditorFrame() {
        this.editorFrame = new EditorFrame(this);
    }

    /**
     * Constructor for Editor Process
     * @param projectPath <code>File</code> object pointing to the project path to be used by this Editor Process.
     */
    public EditorProcess(@NotNull File projectPath) {
        if (projectPath.isDirectory()) {
            this.projectPath = projectPath;
        } else throw new RuntimeException(this + " | EditorProcess: ProjectPath must point to a directory, not a file.");
    }
}
