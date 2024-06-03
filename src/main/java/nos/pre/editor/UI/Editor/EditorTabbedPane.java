package nos.pre.editor.UI.Editor;

import nos.pre.editor.UI.Colors;
import nos.pre.editor.UI.Fonts;
import nos.pre.editor.UI.GraphicsUtilities;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class EditorTabbedPane extends JTabbedPane {
    public EditorTabbedPane() {
        this.setUI(new CustomTabbedPaneUI());

        this.setDoubleBuffered(true);
        this.setOpaque(true);
        this.setBackground(Colors.editorTabbedPaneBackground);
        this.setForeground(Color.WHITE);
        this.setFont(Fonts.LeagueSpartan.deriveFont(Font.BOLD, 14));
        this.setBorder(BorderFactory.createLineBorder(Colors.editorFrameDividingBorderColor, 1));
    }

    public void openEditorTab(@NotNull File file) {
        if (file.isFile()) {
            String fileNameTabTitle = file.getName();

            // Check if tab with same title is already open
            boolean isTabOpen = false;

            for (int i = 0; i < this.getTabCount(); i++) {
                if (Objects.equals(this.getTitleAt(i), fileNameTabTitle)) {
                    if (this.getComponentAt(i) instanceof EditorView sameNameEditorView) {
                        if (! sameNameEditorView.getOpenedFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                            String sameFileNameTabTitle = file.getParentFile().getName() + "/" + fileNameTabTitle;

                            addEditorTab(file, sameFileNameTabTitle);
                            isTabOpen = true;
                        }
                        break;
                    }
                }
            }

            if (!isTabOpen) {
                addEditorTab(file, fileNameTabTitle);
            }
        } else throw new IllegalArgumentException("EditorTabbedPane.openFile(): File must be a normal file.");
    }

    private void addEditorTab(File file, String tabTitle) {
        this.addTab(tabTitle, new EditorView(file));
        this.setTabComponentAt(this.getTabCount() - 1, new TabTitleComponent(this, tabTitle));

        // Select last tab (Assuming that new tab is always created at last index)
        this.setSelectedIndex(this.getTabCount() - 1);

        // TODO: Request focus to editor textPane
    }

    // TODO: Method to Request focus to editor textPane of current tab

    private static class TabTitleComponent extends JPanel {
        private final JTabbedPane tabbedPane;
        private final String tabTitle;

        public TabTitleComponent(JTabbedPane tabbedPane, String title) {
            super();
            if (tabbedPane != null) {
                this.tabbedPane = tabbedPane;
            } else throw new NullPointerException("EditorTabbedPane.TabTitleComponent(): tabbedPane is null");
            this.tabTitle = title;

            this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            this.setDoubleBuffered(true);
            this.setOpaque(false);

            this.addUI();
        }

        private void addUI() {
            JLabel titleLabel = new JLabel(this.tabTitle);
            titleLabel.setOpaque(false);
            titleLabel.setForeground(this.tabbedPane.getForeground());
            titleLabel.setFont(this.tabbedPane.getFont());
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            this.add(titleLabel);

            JButton closeButton = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    Graphics2D g2d = GraphicsUtilities.getGraphics2DWithHints(g);

                    //shift the image for pressed buttons
                    if (getModel().isPressed()) {
                        g2d.translate(1, 1);
                    }
                    g2d.setStroke(new BasicStroke(1));
                    g2d.setColor(tabbedPane.getForeground().darker());
                    int delta = 6;
                    g2d.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
                    g2d.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
                    g2d.dispose();
                }

                @Override
                public void updateUI() {} // Don't update the UI for this button
            };
            closeButton.setPreferredSize(new Dimension(19, 19));
            closeButton.setToolTipText("Close " + this.tabTitle);
            closeButton.setUI(new BasicButtonUI());
            closeButton.setContentAreaFilled(false);
            closeButton.setFocusable(false);
            closeButton.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            closeButton.setBorderPainted(false);
            closeButton.addActionListener(_ -> this.tabbedPane.removeTabAt(this.tabbedPane.indexOfTab(this.tabTitle)));
            this.add(closeButton);
        }
    }

    private static class CustomTabbedPaneUI extends BasicTabbedPaneUI {
        private Color tabIndicatorColor;
        private int tabIndicatorBGAlpha;

        @Contract(pure = true)
        public Color getTabIndicatorColor() {
            return tabIndicatorColor;
        }
        @Contract(mutates = "this")
        public void setTabIndicatorColor(Color tabIndicatorColor) {
            this.tabIndicatorColor = tabIndicatorColor;
        }

        @Contract(pure = true)
        public int getTabIndicatorBGTransparency() {
            return tabIndicatorBGAlpha;
        }
        @Contract(mutates = "this")
        public void setTabIndicatorBGTransparency(int tabIndicatorBGTransparency) {
            this.tabIndicatorBGAlpha = tabIndicatorBGTransparency;
        }

        public CustomTabbedPaneUI() {
            this.setDefaults();
        }

        @Contract(mutates = "this")
        private void setDefaults() {
            this.tabIndicatorColor = Colors.editorTabbedPaneSelectedTabIndicator;
            this.tabIndicatorBGAlpha = Colors.editorTabbedPaneSelectedTabBGAlpha;
            // TODO: Interface "Default-able..." for UI classes for library and L&F later
        }


        @Override
        public void paint(Graphics g, JComponent c) {
            super.paint(g, c);
        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                                          boolean isSelected) {
            if (isSelected) {
                g.setColor(new Color(this.tabIndicatorColor.getRed(), this.tabIndicatorColor.getGreen(),
                                this.tabIndicatorColor.getBlue(), this.tabIndicatorBGAlpha));
                g.fillRect(x, y, w, h);
            }
        }

        @Override
        protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                                      boolean isSelected) {}

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {}

        @Override
        protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex,
                                           Rectangle iconRect, Rectangle textRect, boolean isSelected) {
            Point startLocation = new Point();
            Point endLocation = new Point();
            startLocation.x = rects[tabIndex].x + 1;
            startLocation.y = rects[tabIndex].y + rects[tabIndex].height - 2;

            endLocation.x = rects[tabIndex].x + rects[tabIndex].width - 2;
            endLocation.y = startLocation.y;

            if (isSelected) {
                Graphics2D g2d = GraphicsUtilities.getGraphics2DWithHints(g);

                g2d.setStroke(new BasicStroke(2.0F));
                g2d.setColor(this.tabIndicatorColor);

                g2d.drawLine(startLocation.x, startLocation.y, endLocation.x, endLocation.y);
            }
        }
    }
}
