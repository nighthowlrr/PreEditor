package nos.pre.editor;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final JPanel contentPanel = new JPanel(new BorderLayout(), true);
    public MainFrame() {
        super("PreEditor");
        setSize(1000, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        contentPanel.setOpaque(true);
        contentPanel.setBackground(Color.GRAY);
        setContentPane(contentPanel);

        addUIComponents();

        setVisible(true);
    }

    private void addUIComponents() {
        JPanel textPanel = new JPanel(new BorderLayout(), true);

        JTextPane textPane = new JTextPane();
        textPane.setFont(new Font("League Spartan", Font.PLAIN, 22));
        textPanel.add(textPane, BorderLayout.CENTER);

        JTextLineNumber textLineNumber = new JTextLineNumber(textPane);
        textPanel.add(textLineNumber, BorderLayout.WEST);

        JScrollPane scrollPane = new JScrollPane(textPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 10));
        contentPanel.add(scrollPane, BorderLayout.CENTER);
    }
}
