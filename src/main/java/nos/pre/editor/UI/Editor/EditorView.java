package nos.pre.editor.UI.Editor;

import nos.pre.editor.UI.Colors;
import nos.pre.editor.UI.toolWindows.ProjectToolWindow;
import nos.pre.editor.UI.toolWindows.ToolWindowBase;
import nos.pre.editor.UI.toolWindows.ToolWindowLocation;
import org.jetbrains.annotations.NotNull;
import templateUI.SwingComponents.jTextLineNumber;
import templateUI.SwingComponents.jScrollPane;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class EditorView extends JPanel {

    private final JPanel editorPanel = new JPanel(new BorderLayout(), true);

    private final PreEditingPane editingPane = new PreEditingPane();
    private final jTextLineNumber editorLineNumber = new jTextLineNumber(editingPane);
    private final jScrollPane editorScrollPane = new jScrollPane(editingPane);

    private final JPanel statusBar = new JPanel(new BorderLayout(), true);
    private final JLabel caretLocationLabel = new JLabel("1:1");

    // TOOL_WINDOWS
    private ProjectToolWindow projectToolWindow = new ProjectToolWindow(ToolWindowLocation.LEFT_TOP);

    public EditorView() {
        super(new BorderLayout(), true);

        this.addUIComponents();
        this.addToolWindows();
    }

    public void openFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                editingPane.getStyledDocument().insertString(editingPane.getStyledDocument().getLength(), scanner.nextLine() + "\n", null);
            }
            editingPane.getStyledDocument().remove(editingPane.getStyledDocument().getLength() - 1, 1);
            scanner.close();
        } catch (FileNotFoundException | BadLocationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to open file. An error occurred", "Error", JOptionPane.ERROR_MESSAGE, null);
            // TODO: More detailed error messages
        }
    }

    public void openProject(File projectPath) {
        projectToolWindow.setProjectPath(projectPath);
    }

    private void addUIComponents() {
        // EDITOR CONTENT ===
        editingPane.addCaretListener(e -> caretLocationLabel.setText(editingPane.getCaretLocationString(e)));

        editorScrollPane.setBorder(BorderFactory.createLineBorder(Colors.editorBorderColor, 2));
        editorPanel.add(editorScrollPane, BorderLayout.CENTER);

        editorLineNumber.setCurrentLineForeground(Color.WHITE);
        editorLineNumber.setLineForeground(Color.DARK_GRAY);
        editorLineNumber.setFont(editingPane.getFont());
        editorPanel.add(editorLineNumber, BorderLayout.WEST);

        // STATUS BAR ===
        statusBar.setPreferredSize(new Dimension(0, 20));
        statusBar.setBackground(new Color(0x2b2d30));

        caretLocationLabel.setPreferredSize(new Dimension(100, 0)); // TODO: Adaptive size
        caretLocationLabel.setForeground(Color.LIGHT_GRAY);
        caretLocationLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        caretLocationLabel.setBorder(BorderFactory.createLineBorder(statusBar.getBackground(), 2));
        caretLocationLabel.setHorizontalAlignment(JLabel.CENTER);

        statusBar.add(caretLocationLabel, BorderLayout.EAST);
        editorPanel.add(statusBar, BorderLayout.SOUTH);

        // ADD editorPanel TO EditorView
        this.add(editorPanel, BorderLayout.CENTER);
    }

    private void addToolWindows () {
        // TODO: Proper tool windows toggling from sidebars

        // add tool windows
        this.addToolWindow(this.projectToolWindow);
    }

    private void addToolWindow(@NotNull ToolWindowBase toolWindow) {
        if (toolWindow.getToolWindowLocation() == ToolWindowLocation.LEFT_TOP ||
                toolWindow.getToolWindowLocation() == ToolWindowLocation.LEFT_BOTTOM) {
            this.add(toolWindow, BorderLayout.WEST);
        } else if (toolWindow.getToolWindowLocation() == ToolWindowLocation.RIGHT_TOP ||
                toolWindow.getToolWindowLocation() == ToolWindowLocation.RIGHT_BOTTOM) {
            this.add(toolWindow, BorderLayout.EAST);
        } else if (toolWindow.getToolWindowLocation() == ToolWindowLocation.BOTTOM_LEFT ||
                toolWindow.getToolWindowLocation() == ToolWindowLocation.BOTTOM_RIGHT) {
            this.add(toolWindow, BorderLayout.SOUTH);
        }
        // TODO: Proper locations
    }
}
