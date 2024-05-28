package nos.pre.editor.UI.Welcome;

import nos.pre.editor.About;
import nos.pre.editor.UI.Colors;
import nos.pre.editor.UI.EditorWindow.EditorFrame;
import nos.pre.editor.UI.Fonts;
import nos.pre.editor.UI.Welcome.Views.BasicSettingsView;
import nos.pre.editor.UI.Welcome.Views.ProjectsListView;
import templateUI.SwingComponents.jScrollPane;
import templateUI.SwingComponents.jToggleButton;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collections;

public class WelcomeFrame extends JFrame {
    private final JPanel mainContentPanel = new JPanel(new BorderLayout(), true);

    public WelcomeFrame() {
        super("Welcome to PreEditor");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(this.mainContentPanel);

        addUIComponents();

        setVisible(true);
    }

    private final JPanel sidePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0), true);
    private final JLabel titleLabel = new JLabel(About.appName);
    private final JLabel versionLabel = new JLabel(About.version);

    private final ButtonGroup mainViewButtons = new ButtonGroup();
    private final jToggleButton projectsButton = new jToggleButton("   Projects   ", true);
    private final jToggleButton settingsButton = new jToggleButton("   Settings   ", false);

    private final JPanel centerPanel = new JPanel(new BorderLayout(), true);

    private final ProjectsListView projectsListView = new ProjectsListView();
    private final BasicSettingsView basicSettingsView = new BasicSettingsView();

    private final jScrollPane scrollPane = new jScrollPane(projectsListView);

    private final JPanel actionPanel = new JPanel(new FlowLayout(), true);
    private final JButton newButton = new JButton("New Project");
    private final JButton openButton = new JButton("Open Project");

    private void addUIComponents() {
        sidePanel.setPreferredSize(new Dimension(200, 0));
        sidePanel.setBackground(Colors.welcomeSideBackground);
        this.mainContentPanel.add(sidePanel, BorderLayout.WEST);

        titleLabel.setFont(Fonts.URW_Gothic.deriveFont(Font.BOLD, 24));
        titleLabel.setForeground(Colors.welcomeForegroundLvl1);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setVerticalAlignment(JLabel.BOTTOM);
        titleLabel.setPreferredSize(new Dimension(sidePanel.getPreferredSize().width, ( Fonts.getMetricsOfFont(titleLabel.getFont()).getHeight() ) + 10));
        sidePanel.add(titleLabel);

        versionLabel.setFont(Fonts.URW_Gothic.deriveFont(Font.PLAIN, 18));
        versionLabel.setForeground(Colors.welcomeForegroundLvl3);
        versionLabel.setHorizontalAlignment(JLabel.CENTER);
        versionLabel.setVerticalAlignment(JLabel.TOP);
        versionLabel.setPreferredSize(new Dimension(sidePanel.getPreferredSize().width, Fonts.getMetricsOfFont(versionLabel.getFont()).getHeight()));
        sidePanel.add(versionLabel);

        mainViewButtons.add(projectsButton);
        mainViewButtons.add(settingsButton);

        for (AbstractButton button : Collections.list(mainViewButtons.getElements())) {
            assert button instanceof jToggleButton;
            jToggleButton toggleButton = (jToggleButton) button;

            toggleButton.setOpaque(true);
            toggleButton.setFocusable(false);
            toggleButton.setBorder(BorderFactory.createEmptyBorder());
            toggleButton.setBackground(sidePanel.getBackground());
            toggleButton.setForeground(Colors.welcomeForegroundLvl2);
            toggleButton.setSelectedColor(new Color(0x2E3955));
            toggleButton.setFont(Fonts.URW_Gothic.deriveFont(Font.PLAIN, 16));
            toggleButton.setHorizontalAlignment(JButton.LEFT);
            toggleButton.setPreferredSize(new Dimension(sidePanel.getPreferredSize().width, 40));

            sidePanel.add(toggleButton);
        }

        projectsButton.addActionListener(_ -> {
            if (scrollPane.getViewport().getView() != projectsListView) {
                scrollPane.getViewport().setView(projectsListView);
            }
        });
        settingsButton.addActionListener(_ -> {
            if (scrollPane.getViewport().getView() != basicSettingsView) {
                scrollPane.getViewport().setView(basicSettingsView);
            }
        });

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.setBackground(Colors.welcomeMainBackground);
        this.mainContentPanel.add(centerPanel, BorderLayout.CENTER);

        newButton.setFocusable(false);
        newButton.setContentAreaFilled(false);
        newButton.setOpaque(true);
        // TODO: Proper new project functionality
        // TODO: SHow keyboard shortcuts on buttons for non-focusable buttons
        newButton.setPreferredSize(new Dimension(120, 30));

        openButton.setFocusable(false);
        openButton.setContentAreaFilled(false);
        openButton.setOpaque(true);
        openButton.setPreferredSize(new Dimension(120, 30));

        actionPanel.setOpaque(false);
        actionPanel.add(newButton);
        actionPanel.add(openButton);
        centerPanel.add(actionPanel, BorderLayout.SOUTH);
    }
}
