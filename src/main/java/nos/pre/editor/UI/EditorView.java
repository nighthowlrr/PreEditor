package nos.pre.editor.UI;

import templateUI.JTextLineNumber;
import templateUI.jScrollPane;

import javax.swing.*;
import java.awt.*;

public class EditorView extends JPanel {
    private final jScrollPane scrollPane = new jScrollPane();
    private final JPanel textContentPanel = new JPanel(new BorderLayout(), true);
    private final JTextPane textPane = new JTextPane();
    private final JTextLineNumber textLineNumber = new JTextLineNumber(textPane);

    private final JPanel bottomPanel = new JPanel(new BorderLayout(), true);
    private final JLabel caretLocationLabel = new JLabel(String.valueOf(textPane.getCaretPosition()));

    public EditorView() {
        super(new BorderLayout(), true);

        this.addTextContent();
        this.addBottomPanel();
    }

    private void addTextContent() {
        textPane.setBackground(new Color(0x1e1f22));
        textPane.setForeground(Color.WHITE);
        textPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        textPane.addCaretListener(_ -> caretLocationLabel.setText(String.valueOf(textPane.getCaretPosition()))); // caretUpdate - e
        textContentPanel.add(textPane, BorderLayout.CENTER);

        textLineNumber.setCurrentLineForeground(Color.WHITE);
        textLineNumber.setLineForeground(Color.DARK_GRAY);
        textLineNumber.setFont(textPane.getFont());
        textContentPanel.add(textLineNumber, BorderLayout.WEST);

        scrollPane.setViewportView(textContentPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0x2b2d30), 2));
        this.add(scrollPane, BorderLayout.CENTER);
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
