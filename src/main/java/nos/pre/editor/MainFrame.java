package nos.pre.editor;

import templateUI.JTextLineNumber;
import templateUI.jScrollPane;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;

public class MainFrame extends JFrame {

    private final JPanel mainContentPanel = new JPanel(new BorderLayout(), true);
    public MainFrame() {
        super("PreEditor");
        setSize(1000, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(mainContentPanel);

        addUIComponents();

        setVisible(true);
    }

    private void addUIComponents() {
        mainContentPanel.add(new EditorView(), BorderLayout.CENTER);
    }

    private static class EditorView extends JPanel {
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
            textPane.setFont(new Font("League Spartan", Font.PLAIN, 22));
            textPane.addCaretListener(new CaretListener() {
                @Override
                public void caretUpdate(CaretEvent e) {
                    caretLocationLabel.setText(String.valueOf(textPane.getCaretPosition()));
                }
            });
            textContentPanel.add(textPane, BorderLayout.CENTER);

            textLineNumber.setCurrentLineForeground(Color.WHITE);
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
}
