# UI-Hierarchy - WelcomeFrame

Hierarchy of UI elements in **WelcomeFrame** and in **WelcomeFrameViews**:
- ProjectsListView
- BasicSettingsView

---

- WelcomeFrame (JFrame)
  - mainContentPanel (JPanel) (`WelcomeFrame.contentPane`)
    - sidePanel (JPanel) (_WEST in mainContentPanel_)
      - titleLabel (JLabel)
      - versionLabel (JLabel)
      - mainViewButtons (ButtonGroup)
        - projectsButton (jToggleButton)
        - settingsButton (jToggleButton)
    - centerPanel (JPanel) (_CENTER in mainContentPanel_)
      - mainViewScrollPane (jScrollPane) (_CENTER in centerPanel_)
        - **Switches between different JPanels:** 
          - projectsListView (ProjectsListView - JPanel)
          - basicSettingsView (BasicSettingsView - JPanel)
      - actionPanel (JPanel) (_SOUTH in centerPanel_)
        - newButton (JButton)
        - openButton (JButton)

Class: `nos.pre.editor.UI.Welcome.WelcomeFrame.java`

---

- ProjectsListView (JPanel)
  - (empty)

Class: `nos.pre.editor.UI.Welcome.Views.ProjectsListView`

---

- BasicSettingsView (JPanel)
  - (empty)

Class: `nos.pre.editor.UI.Welcome.Views.BasicSettingsView`
