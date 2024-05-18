package nos.pre.editor.UI.Editor;

import nos.pre.editor.UI.Colors;
import templateUI.JTextLineNumber;
import templateUI.jScrollPane;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class EditorView extends JPanel {

    private final JPanel editorPanel = new JPanel(new BorderLayout(), true);

    private final PreEditingPane editingPane = new PreEditingPane();
    private final JTextLineNumber editorLineNumber = new JTextLineNumber(editingPane);
    private final jScrollPane editorScrollPane = new jScrollPane(editingPane);

    private final JPanel statusBar = new JPanel(new BorderLayout(), true);
    private final JLabel caretLocationLabel = new JLabel("1:1");

    public EditorView() {
        super(new BorderLayout(), true);

        this.addUIComponents();
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

        // ADD TO EDITOR_VIEW
        this.add(editorPanel, BorderLayout.CENTER);
    }
}
