package nos.pre.editor.UI.toolWindows;

import javax.swing.*;

public class ToolWindowBase extends JPanel {
    // PROPERTIES ---
    private ToolWindowLocation toolWindowLocation;

    // GETTERS & SETTERS
    public ToolWindowLocation getToolWindowLocation() {
        return this.toolWindowLocation;
    }
    public void setToolWindowLocation(ToolWindowLocation location) {
        this.toolWindowLocation = location;
    }

    /**
     * Base class for PreEditor Tool windows
     * @param location The location of the tool window
     */
    public ToolWindowBase(ToolWindowLocation location) {
        super(true);

        setToolWindowLocation(location);
    }
}
