package nos.pre.editor.UI.Editor;

import nos.pre.editor.UI.Fonts;
import nos.pre.editor.UI.Welcome.WelcomeFrame;
import nos.pre.editor.UI.toolWindows.projectTool.ProjectToolWindow;
import nos.pre.editor.UI.toolWindows.ToolWindow;
import nos.pre.editor.UI.toolWindows.ToolWindowHolder;
import nos.pre.editor.defaultValues.UIColors;
import nos.pre.editor.processManagers.EditorProcess;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class EditorFrame extends JFrame {
    // Fixed UI Properties ===
    public final Dimension toolWindowBarSize = new Dimension(40, 0);
    // ---

    private final EditorProcess editorProcess;

    private final boolean confirmBeforeExit = false; // TODO: Remove in final. For testing purposes only

    public EditorFrame(EditorProcess editorProcess) {
        super("PreEditor");
        this.editorProcess = editorProcess;

        setSize(1600, 900); // 16:9
        setMinimumSize(new Dimension(960, 540)); // 16:9
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
        } else setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(this.parentPanel);

        this.addUIComponents();
        this.addToolWindows();

        setVisible(true);
    }

    // UI ===
    // ParentPanel ===
    private final JPanel parentPanel = new JPanel(new BorderLayout(), true);
    private final JToolBar leftToolWindowBar = new JToolBar(JToolBar.VERTICAL); // Toolbar containing buttons to toggle tool windows
    private final JToolBar rightToolWindowBar = new JToolBar(JToolBar.VERTICAL); // Toolbar containing buttons to toggle tool windows

    // ViewsPanel ===
    private final JPanel viewsPanel = new JPanel(new BorderLayout(), true);
    private final EditorTabbedPane editorTabbedPane = new EditorTabbedPane();

    private final ToolWindowHolder leftToolWindowHolder = new ToolWindowHolder(ToolWindowHolder.ToolHolderLocation.LEFT);
    private final ToolWindowHolder bottomToolWindowHolder = new ToolWindowHolder(ToolWindowHolder.ToolHolderLocation.BOTTOM);
    private final ToolWindowHolder rightToolWindowHolder = new ToolWindowHolder(ToolWindowHolder.ToolHolderLocation.RIGHT);

    private void addUIComponents() {
        // ParentPanel ===
        leftToolWindowBar.setPreferredSize(this.toolWindowBarSize);
        leftToolWindowBar.setFloatable(false);
        leftToolWindowBar.setBackground(UIColors.TOOL_WINDOW_BAR_BG);

        rightToolWindowBar.setPreferredSize(this.toolWindowBarSize);
        rightToolWindowBar.setFloatable(false);
        rightToolWindowBar.setBackground(UIColors.TOOL_WINDOW_BAR_BG);

        parentPanel.add(leftToolWindowBar, BorderLayout.WEST);
        parentPanel.add(rightToolWindowBar, BorderLayout.EAST);

        // ViewsPanel ===
        viewsPanel.add(editorTabbedPane);

        leftToolWindowHolder.setBorder(BorderFactory.createLineBorder(UIColors.EDITOR_FRAME_DIVIDER_COLOR, 1));
        bottomToolWindowHolder.setBorder(BorderFactory.createLineBorder(UIColors.EDITOR_FRAME_DIVIDER_COLOR, 1));
        rightToolWindowHolder.setBorder(BorderFactory.createLineBorder(UIColors.EDITOR_FRAME_DIVIDER_COLOR, 1));

        this.addToolWindowHolder(leftToolWindowHolder);
        //this.addToolWindowHolder(bottomToolWindowHolder);
        //this.addToolWindowHolder(rightToolWindowHolder);

        parentPanel.add(viewsPanel, BorderLayout.CENTER);
    }

    // TOOL WINDOWS ===
    private final ProjectToolWindow projectToolWindow = new ProjectToolWindow(ToolWindow.ToolWindowLocation.LEFT_TOP);

    /**
     * Adds all tool windows
     */
    private void addToolWindows() {
        projectToolWindow.setProjectPath(this.editorProcess.getProjectPath());
        projectToolWindow.linkToEditorTabbedPane(this.editorTabbedPane);
        this.addToolWindow(projectToolWindow);
    }

    // TOOL WINDOWS UTILITY FUNCTIONS ===
    /**
     * Add the given <code>ToolWindowHolder</code> to the appropriate side of the <code>ViewsPanel</code>
     * according to its <code>ToolHolderLocation</code>, and then repaints <code>ViewsPanel</code>.
     * @param toolWindowHolder The <code>ToolWindowHolder</code> to add to the <code>ViewsPanel</code>.
     */
    private void addToolWindowHolder(@NotNull ToolWindowHolder toolWindowHolder) {
        switch (toolWindowHolder.getToolholderLocation()) {
            case LEFT -> viewsPanel.add(toolWindowHolder, BorderLayout.WEST);
            case BOTTOM -> viewsPanel.add(toolWindowHolder, BorderLayout.SOUTH);
            case RIGHT -> viewsPanel.add(toolWindowHolder, BorderLayout.EAST);
        }
        viewsPanel.revalidate();
        viewsPanel.repaint();
    }

    /**
     * Toggles visibility of the specified <code>ToolWindowHolder</code> and repaints <code>ViewsPanel</code>.
     * @param toolWindowHolder The <code>ToolWindowHolder</code> to toggle the visibility of.
     * @see #showHideToolWindowHolder(ToolWindowHolder, boolean)
     */
    private void showHideToolWindowHolder(@NotNull ToolWindowHolder toolWindowHolder) {
        toolWindowHolder.setVisible(! toolWindowHolder.isVisible());
        viewsPanel.revalidate();
        viewsPanel.repaint();
    }
    /**
     * Shows or hides the specified <code>ToolWindowHolder</code> and repaints <code>ViewsPanel</code>.
     * @param toolWindowHolder The <code>ToolWindowHolder</code> to toggle the visibility of.
     * @param show <code>Boolean</code> specifying whether to show or hide the <code>ToolWindowHolder</code>
     * @see #showHideToolWindowHolder(ToolWindowHolder)
     */
    private void showHideToolWindowHolder(@NotNull ToolWindowHolder toolWindowHolder, boolean show) {
        toolWindowHolder.setVisible(show);
        viewsPanel.revalidate();
        viewsPanel.repaint();
    }

    /**
     * Adds a <code>ToolWindow</code> to the appropriate <code>ToolWindowHolder</code> according to its <code>ToolWindowLocation</code>.
     * Also calls <code>this.addButtonForToolWindow(</code>the specified <code>ToolWindow)</code>
     * to add a toggle button for the <code>ToolWindow</code>
     * @param toolWindow The <code>ToolWindow</code> to add.
     * @see #removeToolWindow(ToolWindow.ToolWindowLocation)
     */
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
            default:
                throw new IllegalStateException("Unexpected value: " + location.getSide().toLowerCase());
        }

        this.addButtonForToolWindow(toolWindow);
    }
    /**
     * Removes the <code>ToolWindow</code> from the specified location.
     * @param location The <code>ToolWindowLocation</code> to remove the <code>ToolWindow</code> from.
     * @see #addToolWindow(ToolWindow)
     */
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
            default:
                throw new IllegalStateException("Unexpected value: " + location.getSide().toLowerCase());
        }

        // TODO: Remove the toggle button for the ToolWindow
    }

    /**
     * Gets the <code>ToolWindowHolder</code> the specified <code>ToolWindow</code> resides in.
     * @param toolWindow The <code>ToolWindow</code> to get the <code>ToolWindowHolder</code> of.
     * @return The <code>ToolWindowHolder</code> the specified <code>ToolWindow</code> resides in.
     */
    private ToolWindowHolder getHolderForToolWindow(@NotNull ToolWindow toolWindow) {
        return switch (toolWindow.getToolWindowLocation()) {
            case LEFT_TOP, LEFT_BOTTOM -> leftToolWindowHolder;
            case BOTTOM_LEFT, BOTTOM_RIGHT -> bottomToolWindowHolder;
            case RIGHT_BOTTOM, RIGHT_TOP -> rightToolWindowHolder;
        };
    }

    /**
     * Toggles the visibility of the specified <code>ToolWindow</code>.
     * <p>
     * If the <code>ToolWindow</code> is to be hidden, and there is no other <code>ToolWindow</code> visible or present
     * in the <code>ToolWindowHolder</code> the specified <code>ToolWindow</code> resides in, that <code>ToolWindowHolder</code> is also hidden.
     * </p>
     * @param toolWindow The <code>ToolWindow</code> to toggle the visibility of.
     */
    private void showHideToolWindow(@NotNull ToolWindow toolWindow) {
        ToolWindowHolder thisToolWindowHolder = getHolderForToolWindow(toolWindow);

        if (toolWindow.isVisible()) { // If the toolWindow is visible...
            thisToolWindowHolder.showHideToolWindow(toolWindow, false); // ... , hide the toolWindow...

            if ((! thisToolWindowHolder.anyToolWindowsVisible()) && thisToolWindowHolder.isVisible()) {
                // Now if the toolWindowHolder is visible and there are no toolWindows visible...
                this.showHideToolWindowHolder(thisToolWindowHolder, false); // Hide the toolWindowHolder.
            }
        } else { // else if the toolWindow is hidden...
            thisToolWindowHolder.showHideToolWindow(toolWindow, true); // show the toolWindow...
            if (! thisToolWindowHolder.isVisible()) { // and if the toolWindowHolder is hidden...
                showHideToolWindowHolder(thisToolWindowHolder, true); // show the toolWindowHolder
            }
        }

        // TODO: More than two toolWindows per toolWindowHolder, Remember? How you gonna add them all at once? u dumdum.
        //  add and remove instead of hiding and showing
    }

    /**
     * Adds a toggle button for the specified <code>ToolWindow</code> in the appropriate <code>ToolWindowBar</code>.
     * Text of the button is the name of the <code>ToolWindow</code>
     * @param toolWindow The <code>ToolWindow</code> to add the toggle button for.
     */
    private void addButtonForToolWindow(@NotNull ToolWindow toolWindow) {
        JButton toolWindowToggleButton = new JButton(toolWindow.getToolWindowName());

        Dimension buttonSize = new Dimension(this.toolWindowBarSize.width, 30);
        toolWindowToggleButton.setMinimumSize(buttonSize);
        toolWindowToggleButton.setPreferredSize(buttonSize);
        toolWindowToggleButton.setMaximumSize(buttonSize);

        toolWindowToggleButton.setDoubleBuffered(true);
        toolWindowToggleButton.setFocusable(false);

        toolWindowToggleButton.setBackground(UIColors.TOOL_WINDOW_BAR_TOOLBUTTON_BG);
        toolWindowToggleButton.setForeground(UIColors.TOOL_WINDOW_BAR_TOOLBUTTON_FG);
        toolWindowToggleButton.setFont(Fonts.LeagueSpartan.deriveFont(12F));
        toolWindowToggleButton.setHorizontalTextPosition(JButton.CENTER);
        toolWindowToggleButton.setVerticalTextPosition(JButton.CENTER);
        toolWindowToggleButton.setBorder(BorderFactory.createEmptyBorder());

        toolWindowToggleButton.addActionListener(e -> showHideToolWindow(toolWindow));

        switch (toolWindow.getToolWindowLocation()) {
            case LEFT_TOP:
            case LEFT_BOTTOM:
            case BOTTOM_LEFT:
                leftToolWindowBar.add(toolWindowToggleButton);
                break;
            case RIGHT_TOP:
            case RIGHT_BOTTOM:
            case BOTTOM_RIGHT:
                rightToolWindowBar.add(toolWindowToggleButton);
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
