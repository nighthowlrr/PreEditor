package nos.pre.editor.UI;

import templateUI.JTextLineNumber;
import templateUI.jScrollPane;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final JPanel mainContentPanel = new JPanel(new BorderLayout(), true);
    public MainFrame() {
        super("PreEditor");
        setSize(1000, 700);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(mainContentPanel);

        addUIComponents();

        setVisible(true);
    }

    private void addUIComponents() {
        mainContentPanel.add(new EditorView(), BorderLayout.CENTER);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        super.paint(g2d);
    }
}
