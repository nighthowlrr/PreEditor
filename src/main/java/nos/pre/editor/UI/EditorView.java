package nos.pre.editor.UI;

import templateUI.JTextLineNumber;
import templateUI.jScrollPane;

import javax.swing.*;
import java.awt.*;

public class EditorView extends JPanel {
    private final jScrollPane editorScrollPane = new jScrollPane();
    private final JPanel textContentPanel = new JPanel(new BorderLayout(), true);
    private final JTextPane editingPane = new JTextPane();
    private final JTextLineNumber editorLineNumber = new JTextLineNumber(editingPane);

    private final JPanel bottomPanel = new JPanel(new BorderLayout(), true);
    private final JLabel caretLocationLabel = new JLabel(String.valueOf(editingPane.getCaretPosition()));

    public EditorView() {
        super(new BorderLayout(), true);

        this.addTextContent();
        this.addBottomPanel();
    }

    private void addTextContent() {
        editingPane.setBackground(Colors.editorBackground);
        editingPane.setForeground(Colors.editorForeground);
        editingPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        editingPane.setCaretColor(Colors.editorCaretColor);
        editingPane.setSelectionColor(Colors.editorSelectionColor);
        editingPane.addCaretListener(_ -> caretLocationLabel.setText(String.valueOf(editingPane.getCaretPosition()))); // caretUpdate - e
        textContentPanel.add(editingPane, BorderLayout.CENTER);

        editorLineNumber.setCurrentLineForeground(Color.WHITE);
        editorLineNumber.setLineForeground(Color.DARK_GRAY);
        editorLineNumber.setFont(editingPane.getFont());
        textContentPanel.add(editorLineNumber, BorderLayout.WEST);

        editorScrollPane.setViewportView(textContentPanel);
        editorScrollPane.setBorder(BorderFactory.createLineBorder(Colors.editorBorderColor, 2));
        this.add(editorScrollPane, BorderLayout.CENTER);
    }

    private void addBottomPanel() {
        bottomPanel.setPreferredSize(new Dimension(0, 20));
        bottomPanel.setBackground(new Color(0x2b2d30));

        caretLocationLabel.setBorder(BorderFactory.createLineBorder(bottomPanel.getBackground(), 2));
        caretLocationLabel.setForeground(Color.LIGHT_GRAY);

        bottomPanel.add(caretLocationLabel);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }
}
