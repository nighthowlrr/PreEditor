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
        syntaxColorInfo.setSyntaxPreprocessorCommandAttributes(new Color(0xC88461), false, false);
        syntaxColorInfo.setSyntaxDataTypeAttributes(new Color(0xC88461), false, true);
        syntaxColorInfo.setSyntaxObjectLiteralAttributes(new Color(0xC88461), false, false);
        syntaxColorInfo.setSyntaxGeneralConstructsAttributes(new Color(0xC88461), false, false);

        syntaxColorInfo.setSyntaxNumberAttributes(new Color(0x59B7BF), false, true);

        syntaxColorInfo.setSyntaxSingleLineAttributes(new Color(0x989898), true, false);
        syntaxColorInfo.setSyntaxMultiLineCommentAttributes(new Color(0x989898), true, false);
        syntaxColorInfo.setSyntaxQuoteAttributes(new Color(0x53B584), false, false);

        return syntaxColorInfo;
    }

    @Contract(" -> new")
    private static @NotNull LanguageDelimiterInfo getJavaDelimiterInfo() {
        return new LanguageDelimiterInfo(";:{}()[]+-/%<=>!&|^~*", "\"'", "//",
                "/*", "*/");
    }
}
