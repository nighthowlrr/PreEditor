package nos.pre.editor.UI.toolWindows;

import nos.pre.editor.UI.Colors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import templateUI.ComponentResizer;

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

    private final Dimension minimumHolderSize = new Dimension(200, 200);
    private final Dimension defaultHolderSize = new Dimension(300, 300);
    private final Dimension maximumHolderSize = new Dimension(500, 500);

    private final ToolHolderLocation toolHolderLocation;
    @NotNull public ToolWindowHolder.ToolHolderLocation getToolholderLocation() {
        return this.toolHolderLocation;
    }

    /**
     * Constructor for JSplitPane made for holding and managing <code>ToolWindow</code>.
     * @param location The <code>ToolHolderLocation</code> object specifying the location of the <code>ToolWindowHolder</code>.
     */
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
        
        ComponentResizer resizer = new ComponentResizer();

        switch (this.toolHolderLocation) {
            case LEFT:
                resizer.setDragInsets(new Insets(0, 0, 0, 10));
                break;
            case BOTTOM:
                resizer.setDragInsets(new Insets(10, 0, 0, 0));
                break;
            case RIGHT:
                resizer.setDragInsets(new Insets(0, 10, 0, 0));
                break;
        }

        resizer.setSnapSize(new Dimension(1, 1));
        resizer.registerComponent(this);
        resizer.setMinimumSize(this.minimumHolderSize);
        resizer.setMaximumSize(this.maximumHolderSize);
    }

    /**
     *
     * @param toolWindow
     * @throws Exception
     */
    public void addToolWindow(@NotNull ToolWindow toolWindow) throws Exception {
        ToolWindow.ToolWindowLocation toolWindowLocation = toolWindow.getToolWindowLocation();
        if (Objects.equals(toolWindowLocation.getSide(), this.toolHolderLocation.getSide())) {
            if (toolWindowLocation.getLayoutIndex() == 0) {
                this.setLeftComponent(toolWindow);
            } else if (toolWindowLocation.getLayoutIndex() == 1) {
                this.setRightComponent(toolWindow);
            }
            this.setDividerToMiddle();
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

    /**
     * Toggles visibility of the specified <code>ToolWindow</code> and repaints this <code>ToolWindowHolder</code>.
     * @param toolWindow The <code>ToolWindow</code> to toggle the visibility of.
     */
    public void showHideToolWindow(@NotNull ToolWindow toolWindow) {
        toolWindow.setVisible(! toolWindow.isVisible());
        this.revalidate();
        this.repaint();
        this.setDividerToMiddle();
    }
    /**
     * Shows or hides the specified <code>ToolWindow</code> and repaints this <code>ToolWindowHolder</code>.
     * @param toolWindow The <code>ToolWindow</code> to toggle the visibility of.
     * @param show <code>Boolean</code> specifying whether to show or hide the <code>ToolWindow</code>
     */
    public void showHideToolWindow(@NotNull ToolWindow toolWindow, boolean show) {
        toolWindow.setVisible(show);
        this.revalidate();
        this.repaint();
        this.setDividerToMiddle();
    }

    /**
     * @return <code>Boolean</code> specifying if there are any toolWindows visible.
     */
    public boolean anyToolWindowsVisible() {
        if ((! this.getLeftComponent().isVisible()) && this.getRightComponent() == null) {
            return false;
        } else if (this.getLeftComponent() == null && (! this.getRightComponent().isVisible())) {
            return false;
        } else if (!this.getLeftComponent().isVisible() && !this.getRightComponent().isVisible()) {
            return false;
        } else return true;
    }

    private void setDividerToMiddle() {
        SwingUtilities.invokeLater(() -> this.setDividerLocation(0.5d));
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        this.setDividerToMiddle();
    }
}
