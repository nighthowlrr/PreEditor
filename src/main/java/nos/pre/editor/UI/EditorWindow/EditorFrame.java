package nos.pre.editor.UI.EditorWindow;

import nos.pre.editor.UI.Colors;
import nos.pre.editor.UI.Editor.EditorView;
import nos.pre.editor.UI.Welcome.WelcomeFrame;
import nos.pre.editor.UI.toolWindows.ProjectToolWindow;
import nos.pre.editor.UI.toolWindows.ToolWindow;
import nos.pre.editor.UI.toolWindows.ToolWindowHolder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

public class EditorFrame extends JFrame {
    private final File projectPath;

    private final boolean confirmBeforeExit = false; // TODO: Remove in final. For testing purposes only

    private final JPanel mainContentPanel = new JPanel(new BorderLayout(), true);

    private final ProjectToolWindow projectToolWindow = new ProjectToolWindow(ToolWindow.ToolWindowLocation.LEFT_TOP);

    public EditorFrame(File projectPath) {
        super("PreEditor - NewProjectFrame");

        setSize(1600, 900);
        setLocationRelativeTo(null);

        if (confirmBeforeExit) {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowListener() {
                @Override public void windowOpened(WindowEvent e) {}
                @Override public void windowClosing(WindowEvent e) {
                    showExitDialog();
                }
                @Override public void windowClosed(WindowEvent e) {}
                @Override public void windowIconified(WindowEvent e) {}
                @Override public void windowDeiconified(WindowEvent e) {}
                @Override public void windowActivated(WindowEvent e) {}
                @Override public void windowDeactivated(WindowEvent e) {}
            });
        } else {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }

        setContentPane(this.mainContentPanel);
        addEditorView();
        addToolWindowHolders();

        this.projectPath = projectPath;
        this.addToolWindow(this.projectToolWindow);
        this.projectToolWindow.setProjectPath(this.projectPath);

        setVisible(true);
    }

    // UI ===
    private final EditorView editorView = new EditorView();

    private final ToolWindowHolder leftToolWindowHolder = new ToolWindowHolder(ToolWindowHolder.ToolHolderLocation.LEFT);
    private final ToolWindowHolder bottomToolWindowHolder = new ToolWindowHolder(ToolWindowHolder.ToolHolderLocation.BOTTOM);
    private final ToolWindowHolder rightToolWindowHolder = new ToolWindowHolder(ToolWindowHolder.ToolHolderLocation.RIGHT);

    private void addEditorView() {
        editorView.setBorder(BorderFactory.createLineBorder(Colors.editorFrameDividingBorderColor, 1));

        // TODO: Proper
        projectToolWindow.linkToEditorPane(editorView);

        this.add(editorView, BorderLayout.CENTER);
    }

    private void addToolWindowHolders() {
        leftToolWindowHolder.setBorder(BorderFactory.createLineBorder(Colors.editorFrameDividingBorderColor, 1));
        bottomToolWindowHolder.setBorder(BorderFactory.createLineBorder(Colors.editorFrameDividingBorderColor, 1));
        rightToolWindowHolder.setBorder(BorderFactory.createLineBorder(Colors.editorFrameDividingBorderColor, 1));

        this.add(leftToolWindowHolder, BorderLayout.WEST);
        this.add(bottomToolWindowHolder, BorderLayout.SOUTH);
        this.add(rightToolWindowHolder, BorderLayout.EAST);
    }

    public void addToolWindow(@NotNull ToolWindow toolWindow) {
        ToolWindow.ToolWindowLocation location = toolWindow.getToolWindowLocation();

        // LayoutIndex is handled by the ToolWindowHolder.addToolWindow(ToolWindow toolWindow) method.

        switch (location.getSide().toLowerCase()) {
            case "left":
                try {
                    leftToolWindowHolder.addToolWindow(toolWindow);
                } catch (Exception e) { e.printStackTrace(); }
                break;
            case "bottom":
                try {
                    bottomToolWindowHolder.addToolWindow(toolWindow);
                } catch (Exception e) { e.printStackTrace(); }
                break;
            case "right":
                try {
                    rightToolWindowHolder.addToolWindow(toolWindow);
                } catch (Exception e) { e.printStackTrace(); }
                break;
        }
    }

    public void removeToolWindow(@NotNull ToolWindow.ToolWindowLocation location) {
        switch (location.getSide().toLowerCase()) {
            case "left":
                try {
                    leftToolWindowHolder.removeToolWindow(location);
                } catch (Exception e) { e.printStackTrace(); }
                break;
            case "bottom":
                try {
                    bottomToolWindowHolder.removeToolWindow(location);
                } catch (Exception e) { e.printStackTrace(); }
                break;
            case "right":
                try {
                    rightToolWindowHolder.removeToolWindow(location);
                } catch (Exception e) { e.printStackTrace(); }
                break;
        }
    }

    // WINDOW FUNCTIONS ===
    private void showExitDialog() {
        // TODO: Custom UI for dialog
        int exitOption = JOptionPane.showInternalOptionDialog(null,
                "Are you sure you want to exit?", "Confirm Exit",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Exit", "Close Project", "Cancel"}, "Exit");
        if (exitOption == JOptionPane.YES_OPTION) {
            this.dispose();
            System.exit(0);
        } else if (exitOption == JOptionPane.NO_OPTION) {
            this.dispose();
            new WelcomeFrame();
        }
    }
}
