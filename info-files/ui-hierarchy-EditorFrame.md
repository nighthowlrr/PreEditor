# UI-Hierarchy - EditorFrame

Hierarchy of UI elements in **EditorFrame**.

---

- EditorFrame (JFrame)
  - parentPanel (JPanel) (`EditorFrame.contentPane`)
    - leftToolWindowBar (JToolBar) (_WEST in parentPanel_)
    - rightToolWindowBar (JToolBar) (_EAST in parentPanel_)
    - viewsPanel (JPanel) (_CENTER in parentPanel_)
      - editorTabbedPane (EditorTabbedPane - JTabbedPane) (_CENTER in viewsPanel_)
      - leftToolWindowHolder (ToolWindowHolder - JSplitPane) (_ToolHolderLocation.LEFT_) (check `EditorFrame.addToolWindowHolder()`)
      - bottomToolWindowHolder (ToolWindowHolder - JSplitPane) (_ToolHolderLocation.BOTTOM_) (check `EditorFrame.addToolWindowHolder()`)
      - rightToolWindowHolder (ToolWindowHolder - JSplitPane) (_ToolHolderLocation.RIGHT_) (check `EditorFrame.addToolWindowHolder()`)

Class: `nos.pre.editor.UI.Editor.EditorFrame`

---

- EditorTabbedPane (JTabbedPane)
  - Each tab's component is a new instance of `EditorView`

Class: `nos.pre.editor.UI.Editor.EditorTabbedPane`

---

- EditorView (JPanel)
  - editorScrollPane (jScrollPane) (_CENTER in EditorView_)
    - editingPaneHolder (JPanel) (_main view in editorScrollPane_)
      - preTextPane (EditingPane - JTextPane) (_CENTER in editingPaneHolder_)
    - editorLineNumber (TextLineNumber - Custom Component) (`editorScrollPane.rowHeaderView`)
  - statusBar (JPanel) (_SOUTH in EditorView_)
    - saveStatusLabel (JLabel)
    - `Box.createHorizontalGlue()`
    - caretLocationLabel (JLabel)

Class: `nos.pre.editor.UI.Editor.EditorView`
