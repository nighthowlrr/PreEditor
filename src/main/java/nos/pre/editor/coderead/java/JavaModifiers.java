package nos.pre.editor.coderead.java;

import org.jetbrains.annotations.Contract;

public class JavaModifiers {
    public enum AccessModifiers {
        PACKAGE_PROTECTED(""),
        PRIVATE("private"),
        PROTECTED("protected"),
        PUBLIC("public");

        private final String accessModifierText;

        @Contract(pure = true)
        AccessModifiers(String accessModifierText) {
            this.accessModifierText = accessModifierText;
        }

        @Contract(pure = true)
        public String getAccessModifierText() {
            return accessModifierText;
        }
    }
}
