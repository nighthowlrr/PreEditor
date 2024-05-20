package nos.pre.editor.UI.toolWindows;

import nos.pre.editor.UI.Colors;
import templateUI.SwingComponents.jScrollPane;
import templateUI.jFileTree;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ProjectToolWindow extends ToolWindowBase {
    private final jFileTree fileTree = new jFileTree();
    private final jScrollPane fileTreeScroll = new jScrollPane(fileTree);

    public ProjectToolWindow(ToolWindowLocation toolWindowLocation) {
        super(toolWindowLocation);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(250, 250));
        this.setBackground(Colors.projectToolBackground);

        this.addUIComponents();
    }

    private void addUIComponents() {
        fileTree.setBackground(this.getBackground());

        fileTreeScroll.setBorder(BorderFactory.createLineBorder(this.getBackground(), 5));
        fileTreeScroll.setBackground(this.getBackground());

        this.add(fileTreeScroll, BorderLayout.CENTER);
    }

    public void setProjectPath(File projectPath) {
        fileTree.setStartingPath(projectPath);
    }
}
