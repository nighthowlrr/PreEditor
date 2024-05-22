package nos.pre.editor.UI.toolWindows;

import nos.pre.editor.UI.Colors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.beans.ConstructorProperties;
import java.util.Objects;

public class ToolWindowHolder extends JSplitPane {
    public enum ToolHolderLocation {
        LEFT ("left"),
        BOTTOM ("bottom"),
        RIGHT ("right");

        private final String side;
        @Contract(pure = true)
        public String getSide() {
            return side;
        }

        @Contract(pure = true)
        @ConstructorProperties({"side"})
        ToolHolderLocation(String side) {
            this.side = side;
        }
    }

    private final Dimension defaultHolderSize = new Dimension(200, 200);

    private final ToolHolderLocation toolHolderLocation;
    @NotNull public ToolWindowHolder.ToolHolderLocation getToolholderLocation() {
        return this.toolHolderLocation;
    }

    public ToolWindowHolder(ToolHolderLocation location) {
        this.toolHolderLocation = location;

        this.setPreferredSize(this.defaultHolderSize);
        this.setBackground(Colors.toolWindowHolderBackground);

        if (this.toolHolderLocation == ToolHolderLocation.LEFT || this.toolHolderLocation == ToolHolderLocation.RIGHT) {
            this.setOrientation(JSplitPane.VERTICAL_SPLIT);
        } else if (this.toolHolderLocation == ToolHolderLocation.BOTTOM) {
            this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        }
        this.setContinuousLayout(true);
        this.setOneTouchExpandable(false);
        this.setResizeWeight(0.55);
        this.setDividerSize(2);

        this.setLeftComponent(null);
        this.setRightComponent(null);

        this.setVisible(true);
        //SwingUtilities.invokeLater(() -> this.setDividerLocation(0.5d));
    }

    public void addToolWindow(@NotNull ToolWindow toolWindow) throws Exception {
        ToolWindow.ToolWindowLocation toolWindowLocation = toolWindow.getToolWindowLocation();
        if (Objects.equals(toolWindowLocation.getSide(), this.toolHolderLocation.getSide())) {
            if (toolWindowLocation.getLayoutIndex() == 0) {
                this.setLeftComponent(toolWindow);
            } else if (toolWindowLocation.getLayoutIndex() == 1) {
                this.setRightComponent(toolWindow);
            }
        } else throw new Exception("ToolWindow location does not match ToolWindowHolder location");
    }

    public void removeToolWindow(ToolWindow.@NotNull ToolWindowLocation location) throws Exception {
        if (Objects.equals(location.getSide(), this.toolHolderLocation.getSide())) {
            if (location.getLayoutIndex() == 0) {
                this.setLeftComponent(null);
            } else if (location.getLayoutIndex() == 1) {
                this.setRightComponent(null);
            }
        } else throw new Exception("ToolWindow location does not match ToolWindowHolder location");
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        SwingUtilities.invokeLater(() -> this.setDividerLocation(0.5d));
    }
}
