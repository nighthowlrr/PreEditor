package nos.pre.editor.UI.EditorWindow;

import nos.pre.editor.UI.Colors;
import nos.pre.editor.UI.Editor.EditorView;
import nos.pre.editor.UI.Welcome.WelcomeFrame;
import nos.pre.editor.UI.toolWindows.ProjectToolWindow;
import nos.pre.editor.UI.toolWindows.ToolWindow;
import nos.pre.editor.UI.toolWindows.ToolWindowHolder;
import nos.pre.editor.processmanagers.EditorProcess;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class EditorFrame extends JFrame {
    private final EditorProcess editorProcess;

    private final boolean confirmBeforeExit = false; // TODO: Remove in final. For testing purposes only

    public EditorFrame(EditorProcess editorProcess) {
        super();
        this.editorProcess = editorProcess;

        setTitle("PreEditor");
        setSize(1600, 900);
        setLocationRelativeTo(null);

        if (this.confirmBeforeExit) {
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

        setContentPane(this.parentPanel);

        this.addUIComponents();
        this.addToolWindows();

        setVisible(true);
    }


    // UI ===
    // ParentPanel ===
    private final JPanel parentPanel = new JPanel(new BorderLayout(), true);
    private final JToolBar leftToolWindowBar = new JToolBar(JToolBar.VERTICAL);
    private final JToolBar rightToolWindowBar = new JToolBar(JToolBar.VERTICAL);

    private final JButton leftShowHideButton = new JButton("S/H L");
    private final JButton bottomShowHideButton = new JButton("S/H B");
    private final JButton rightShowHideButton = new JButton("S/H R");

    // ViewsPanel ===
    private final JPanel viewsPanel = new JPanel(new BorderLayout(), true);
    private final EditorView editorView = new EditorView();

    private final ToolWindowHolder leftToolWindowHolder = new ToolWindowHolder(ToolWindowHolder.ToolHolderLocation.LEFT);
    private final ToolWindowHolder bottomToolWindowHolder = new ToolWindowHolder(ToolWindowHolder.ToolHolderLocation.BOTTOM);
    private final ToolWindowHolder rightToolWindowHolder = new ToolWindowHolder(ToolWindowHolder.ToolHolderLocation.RIGHT);

    private void addUIComponents() {
        // ParentPanel ===
        leftToolWindowBar.setPreferredSize(new Dimension(40, 40));
        leftToolWindowBar.setFloatable(false);
        leftToolWindowBar.setBackground(Colors.toolWindowBarBackground);
        leftToolWindowBar.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0,
                Colors.editorFrameDividingBorderColor));

        leftShowHideButton.addActionListener(_ -> this.showHideToolWindowHolder(leftToolWindowHolder));
        leftToolWindowBar.add(leftShowHideButton);

        bottomShowHideButton.addActionListener(_ -> this.showHideToolWindowHolder(bottomToolWindowHolder));
        leftToolWindowBar.add(bottomShowHideButton);

        rightToolWindowBar.setPreferredSize(new Dimension(40, 40));
        rightToolWindowBar.setFloatable(false);
        rightToolWindowBar.setBackground(Colors.toolWindowBarBackground);
        rightToolWindowBar.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0,
                Colors.editorFrameDividingBorderColor));

        rightShowHideButton.addActionListener(_ -> this.showHideToolWindowHolder(rightToolWindowHolder));
        rightToolWindowBar.add(rightShowHideButton);

        parentPanel.add(leftToolWindowBar, BorderLayout.WEST);
        parentPanel.add(rightToolWindowBar, BorderLayout.EAST);

        // ViewsPanel ===
        viewsPanel.add(editorView);

        leftToolWindowHolder.setBorder(BorderFactory.createLineBorder(Colors.editorFrameDividingBorderColor, 1));
        bottomToolWindowHolder.setBorder(BorderFactory.createLineBorder(Colors.editorFrameDividingBorderColor, 1));
        rightToolWindowHolder.setBorder(BorderFactory.createLineBorder(Colors.editorFrameDividingBorderColor, 1));

        this.addToolWindowHolder(leftToolWindowHolder);
        this.addToolWindowHolder(bottomToolWindowHolder);
        this.addToolWindowHolder(rightToolWindowHolder);

        parentPanel.add(viewsPanel, BorderLayout.CENTER);
    }

    private void addToolWindows() {
        ProjectToolWindow projectToolWindow = new ProjectToolWindow(ToolWindow.ToolWindowLocation.LEFT_TOP);
        projectToolWindow.setProjectPath(this.editorProcess.getProjectPath());
        this.addToolWindow(projectToolWindow);

        projectToolWindow.linkToEditorPane(this.editorView);
    }

    // TOOL WINDOWS UTILITY FUNCTIONS===
    private void addToolWindowHolder(@NotNull ToolWindowHolder toolWindowHolder) {
        switch (toolWindowHolder.getToolholderLocation()) {
            case LEFT -> viewsPanel.add(toolWindowHolder, BorderLayout.WEST);
            case BOTTOM -> viewsPanel.add(toolWindowHolder, BorderLayout.SOUTH);
            case RIGHT -> viewsPanel.add(toolWindowHolder, BorderLayout.EAST);
        }
        viewsPanel.revalidate();
        viewsPanel.repaint();
    }

    private void showHideToolWindowHolder(@NotNull ToolWindowHolder toolWindowHolder) {
        toolWindowHolder.setVisible(! toolWindowHolder.isVisible());
        viewsPanel.revalidate();
        viewsPanel.repaint();
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
        int exitOption = JOptionPane.showInternalOptionDialog(this,
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
