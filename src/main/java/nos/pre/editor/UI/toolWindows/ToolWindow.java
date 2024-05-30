package nos.pre.editor.UI.toolWindows;

import nos.pre.editor.UI.Fonts;
import org.jetbrains.annotations.Contract;

import javax.swing.*;
import java.awt.*;
import java.beans.ConstructorProperties;

public class ToolWindow extends JPanel {
    public enum ToolWindowLocation {
        LEFT_TOP("left", 0),
        LEFT_BOTTOM("left", 1),
        BOTTOM_LEFT("bottom", 0),
        BOTTOM_RIGHT("bottom", 1),
        RIGHT_TOP("right", 0),
        RIGHT_BOTTOM("right", 1);

        private final String side;
        private final int layoutIndex;

        @Contract(pure = true)
        public int getLayoutIndex() {
            return layoutIndex;
        }

        @Contract(pure = true)
        public String getSide() {
            return side;
        }

        @Contract(pure = true)
        @ConstructorProperties({"side", "layoutIndex"})
        ToolWindowLocation(String side, int layoutIndex) {
            this.side = side;
            this.layoutIndex = layoutIndex;
        }
    }

    // PROPERTIES ---
    private final String toolWindowName;
    private ToolWindowLocation toolWindowLocation;

    // GETTERS & SETTERS
    public String getToolWindowName() {
        return toolWindowName;
    }

    public ToolWindowLocation getToolWindowLocation() {
        return this.toolWindowLocation;
    }
    public void setToolWindowLocation(ToolWindowLocation location) {
        this.toolWindowLocation = location;
    }

    /**
     * Base class for PreEditor Tool windows
     * @param toolWindowName The name of the tool window
     * @param location The location of the tool window
     */
    public ToolWindow(String toolWindowName, ToolWindowLocation location) {
        super(true);
        this.toolWindowName = toolWindowName;

        setToolWindowLocation(location);
    }

    public JLabel getToolWindowNameLabel() {
        JLabel nameLabel = new JLabel(this.toolWindowName);
        nameLabel.setOpaque(true);
        nameLabel.setBackground(this.getBackground());
        nameLabel.setPreferredSize(new Dimension(1, 30));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setHorizontalAlignment(JLabel.LEFT);
        nameLabel.setFont(Fonts.LeagueSpartan.deriveFont(Font.PLAIN, 15));
        nameLabel.setBorder(BorderFactory.createLineBorder(nameLabel.getBackground(), 8));

        return nameLabel;
    }
}
