package nos.pre.editor.UI.Editor;

import nos.pre.editor.UI.Colors;
import templateUI.SwingComponents.jTextLineNumber;
import templateUI.SwingComponents.jScrollPane;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class EditorView extends JPanel {
    public EditorView() {
        super(new BorderLayout(), true);

        this.addUIComponents();
    }

    private final JPanel editingPaneHolder = new JPanel(new BorderLayout(), true);
    private final PreEditingPane editingPane = new PreEditingPane();
    private final jTextLineNumber editorLineNumber = new jTextLineNumber(editingPane);
    private final jScrollPane editorScrollPane = new jScrollPane(editingPaneHolder);

    private final JPanel statusBar = new JPanel(new BorderLayout(), true);
    private final JLabel caretLocationLabel = new JLabel("1:1");

    private void addUIComponents() {
        // EDITOR PANE & LINE NUMBER ===
        editingPane.addCaretListener(e -> caretLocationLabel.setText(editingPane.getCaretLocationString(e)));
        editingPaneHolder.add(editingPane, BorderLayout.CENTER);

        editorLineNumber.setCurrentLineForeground(Color.WHITE);
        editorLineNumber.setLineForeground(Color.DARK_GRAY);
        editorLineNumber.setFont(editingPane.getFont());
        editingPaneHolder.add(editorLineNumber, BorderLayout.WEST);

        editorScrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(editorScrollPane, BorderLayout.CENTER);

        // STATUS BAR ===
        statusBar.setPreferredSize(new Dimension(0, 20));
        statusBar.setFocusable(false);
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
     * Reads a <code>File</code> and appends its text to <code>EditingPane</code>
     * @param file The file to read and append the text of.
     */
    public void openFile(File file) {
        try {
            // TODO: Open file in new tab or switch to tab with the file already open.
            // Reset EditingPane
            editingPane.setText("");

            // Read the file
            Scanner scanner = new Scanner(file);
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
        } catch (FileNotFoundException | BadLocationException e) {
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
