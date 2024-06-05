package nos.pre.editor.UI.Editor;

import nos.pre.editor.UI.Colors;
import templateUI.SwingComponents.TextLineNumber;
import templateUI.SwingComponents.jScrollPane;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class EditorView extends JPanel {
    private final File openedFile;
    public File getOpenedFile() {
        return this.openedFile;
    }

    public EditorView(File openFile) {
        super(new BorderLayout(), true);

        this.openedFile = openFile;

        this.addUIComponents();
        this.openFile();
    }

    private final JPanel editingPaneHolder = new JPanel(new BorderLayout(), true);
    private final EditingPane editingPane = new EditingPane();
    private final TextLineNumber editorLineNumber = new TextLineNumber(editingPane);
    private final jScrollPane editorScrollPane = new jScrollPane(editingPaneHolder);

    private final JPanel statusBar = new JPanel(new BorderLayout(), true);
    private final JLabel caretLocationLabel = new JLabel("1:1");

    private void addUIComponents() {
        // EDITOR PANE & LINE NUMBER ===
        editingPane.addCaretListener(e -> caretLocationLabel.setText(editingPane.getCaretLocationString(e)));
        editingPaneHolder.add(editingPane, BorderLayout.CENTER);

        editorLineNumber.setCurrentLineForeground(Color.WHITE); // TODO: colors
        editorLineNumber.setLineForeground(Color.GRAY.darker()); // TODO: colors
        editorLineNumber.setSeparatorColor(Colors.editorInternalBorderColor);
        editorScrollPane.setRowHeaderView(editorLineNumber);

        editorScrollPane.setWheelScrollingEnabled(true);
        editorScrollPane.setBackground(editingPane.getBackground());
        editorScrollPane.setScrollThumbColor(new Color(0xFFFFFF));
        editorScrollPane.setScrollTrackColor(editingPane.getBackground());
        editorScrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(editorScrollPane, BorderLayout.CENTER);

        // STATUS BAR ===
        statusBar.setPreferredSize(new Dimension(0, 20));
        statusBar.setFocusable(false);
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Colors.editorInternalBorderColor));
        statusBar.setBackground(Colors.editorStatusBarBackground);

        caretLocationLabel.setPreferredSize(new Dimension(100, 0)); // TODO: Adaptive size
        caretLocationLabel.setForeground(Color.LIGHT_GRAY);
        caretLocationLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        caretLocationLabel.setBorder(BorderFactory.createLineBorder(statusBar.getBackground(), 2));
        caretLocationLabel.setHorizontalAlignment(JLabel.CENTER);

        statusBar.add(caretLocationLabel, BorderLayout.EAST);
        this.add(statusBar, BorderLayout.SOUTH);
    }

    /**
     * Reads <code>this.openedFile</code> and appends its text to <code>EditingPane</code>
     */
    private void openFile() {
        // TODO: If file type is not supported (file cannot be read and opened), then do not open a new tab

        try {
            // Reset EditingPane
            editingPane.setText("");

            // Read the file
            Scanner scanner = new Scanner(this.openedFile);
            while (scanner.hasNextLine()) {
                // Append the text line-by-line
                editingPane.getStyledDocument().insertString(editingPane.getStyledDocument().getLength(),
                        scanner.nextLine() + "\n", null);
            }
            // Remove the last "\n" character
            editingPane.getStyledDocument().remove(editingPane.getStyledDocument().getLength() - 1, 1);
            scanner.close();

            // Set Caret to the beginning of the text
            editingPane.setCaretPosition(0);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to open file. An error occurred",
                    "Error", JOptionPane.ERROR_MESSAGE, null);
            // TODO: More detailed error messages
        }
    }

    @Override
    public void requestFocus() {
        //super.requestFocus();
        editingPane.requestFocus();
    }
}
