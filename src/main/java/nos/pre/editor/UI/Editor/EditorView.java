package nos.pre.editor.UI.Editor;

import nos.pre.editor.UI.Colors;
import templateUI.JTextLineNumber;
import templateUI.jScrollPane;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.Objects;

public class EditorView extends JPanel {
    private final jScrollPane editorScrollPane = new jScrollPane();
    private final JPanel textContentPanel = new JPanel(new BorderLayout(), true);
    private final PreEditingPane editingPane = new PreEditingPane();
    private final JTextLineNumber editorLineNumber = new JTextLineNumber(editingPane);

    private final JPanel bottomPanel = new JPanel(new BorderLayout(), true);
    private final JLabel caretLocationLabel = new JLabel("1:1");

    public EditorView() {
        super(new BorderLayout(), true);

        this.addTextContent();
        this.addBottomPanel();
        this.addFilter();
    }

    private void addTextContent() {
        editingPane.getCaretLocationOnJLabel(caretLocationLabel);
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

        caretLocationLabel.setPreferredSize(new Dimension(100, 0)); // TODO: Adaptive size
        caretLocationLabel.setForeground(Color.LIGHT_GRAY);
        caretLocationLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        caretLocationLabel.setBorder(BorderFactory.createLineBorder(bottomPanel.getBackground(), 2));
        caretLocationLabel.setHorizontalAlignment(JLabel.CENTER);

        bottomPanel.add(caretLocationLabel, BorderLayout.EAST);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addFilter() {
        AbstractDocument abstractDocument;
        StyledDocument styledDocument = editingPane.getStyledDocument();
        if (styledDocument instanceof AbstractDocument) {
            abstractDocument = (AbstractDocument) styledDocument;
            abstractDocument.setDocumentFilter(new DocumentFilter() {
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if (text.endsWith("{")) {
                        super.replace(fb, offset, length, text, attrs);
                        fb.insertString(fb.getDocument().getLength(), "}", attrs);
                    } else if (text.endsWith("(")) {
                        super.replace(fb, offset, length, text, attrs);
                        fb.insertString(fb.getDocument().getLength(), ")", attrs);
                    } else if (text.endsWith("[")) {
                        super.replace(fb, offset, length, text, attrs);
                        fb.insertString(fb.getDocument().getLength(), "]", attrs);
                    } else if (text.endsWith("\"")) {
                        if (Objects.equals(abstractDocument.getText(offset + 1, 1), "\"")) {
                            editingPane.setCaretPosition(abstractDocument.createPosition(offset + 1).getOffset());
                        } else {
                            super.replace(fb, offset, length, text, attrs);
                            fb.insertString(fb.getDocument().getLength(), "\"", attrs);
                        }
                    } else if (text.endsWith("'")) {
                        super.replace(fb, offset, length, text, attrs);
                        fb.insertString(fb.getDocument().getLength(), "'", attrs);
                    } else {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            });
        }
    }
}
