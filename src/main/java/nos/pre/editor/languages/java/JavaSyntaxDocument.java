package nos.pre.editor.languages.java;

import nos.pre.editor.languages.syntax.LanguageDelimiterInfo;
import nos.pre.editor.languages.syntax.SyntaxColorInfo;
import nos.pre.editor.languages.syntax.SyntaxDocument;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class JavaSyntaxDocument extends SyntaxDocument {
    public JavaSyntaxDocument() {
        super(JavaKeywords.getKeywordMap(), getJavaSyntaxColorInfo(), getJavaDelimiterInfo());
    }

    private static @NotNull SyntaxColorInfo getJavaSyntaxColorInfo() {
        SyntaxColorInfo syntaxColorInfo = new SyntaxColorInfo();

        syntaxColorInfo.setSyntaxNormalAttributes(Color.WHITE, false, false);
        syntaxColorInfo.setSyntaxPreprocessorCommandAttributes(new Color(0xAA623D), false, false);
        syntaxColorInfo.setSyntaxDataTypeAttributes(new Color(0xAA623D), false, true);
        syntaxColorInfo.setSyntaxObjectLiteralAttributes(new Color(0xAA623D), false, false);
        syntaxColorInfo.setSyntaxGeneralConstructsAttributes(new Color(0xAA623D), false, false);

        syntaxColorInfo.setSyntaxNumberAttributes(new Color(0x59BF9C), false, false);

        syntaxColorInfo.setSyntaxSingleLineAttributes(new Color(0x6E6E6E), true, false);
        syntaxColorInfo.setSyntaxMultiLineCommentAttributes(new Color(0x6E6E6E), true, false);
        syntaxColorInfo.setSyntaxQuoteAttributes(new Color(0x44946F), false, false);

        return syntaxColorInfo;
    }

    @Contract(" -> new")
    private static @NotNull LanguageDelimiterInfo getJavaDelimiterInfo() {
        return new LanguageDelimiterInfo(";:{}()[]+-/%<=>!&|^~*", "\"'", "//",
                "/*", "*/");
    }
}
