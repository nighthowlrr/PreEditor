package nos.pre.editor.languages.java;

import nos.pre.editor.languages.syntax.LanguageDelimiterInfo;
import nos.pre.editor.languages.syntax.SyntaxColorInfo;
import nos.pre.editor.languages.syntax.SyntaxDocument;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class JavaSyntaxDocument extends SyntaxDocument {
    public JavaSyntaxDocument() {
        super(getJavaKeywordMap(), getJavaSyntaxColorInfo(), getJavaDelimiterInfo());
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

    private static @NotNull Map<String, SyntaxDocument.KeywordType> getJavaKeywordMap() {
        Map<String, SyntaxDocument.KeywordType> javaKeywords = new HashMap<>();

        javaKeywords.put("package", SyntaxDocument.KeywordType.PREPROCESSOR_COMMAND);
        javaKeywords.put("import", SyntaxDocument.KeywordType.PREPROCESSOR_COMMAND);

        javaKeywords.put("class", SyntaxDocument.KeywordType.DATA_TYPE);
        javaKeywords.put("interface", SyntaxDocument.KeywordType.DATA_TYPE);
        javaKeywords.put("enum", SyntaxDocument.KeywordType.DATA_TYPE);
        javaKeywords.put("record", SyntaxDocument.KeywordType.DATA_TYPE);
        javaKeywords.put("byte", SyntaxDocument.KeywordType.DATA_TYPE);
        javaKeywords.put("short", SyntaxDocument.KeywordType.DATA_TYPE);
        javaKeywords.put("int", SyntaxDocument.KeywordType.DATA_TYPE);
        javaKeywords.put("long", SyntaxDocument.KeywordType.DATA_TYPE);
        javaKeywords.put("float", SyntaxDocument.KeywordType.DATA_TYPE);
        javaKeywords.put("double", SyntaxDocument.KeywordType.DATA_TYPE);
        javaKeywords.put("boolean", SyntaxDocument.KeywordType.DATA_TYPE);
        javaKeywords.put("char", SyntaxDocument.KeywordType.DATA_TYPE);
        javaKeywords.put("void", SyntaxDocument.KeywordType.DATA_TYPE);

        javaKeywords.put("true", SyntaxDocument.KeywordType.OBJECT_LITERAL);
        javaKeywords.put("false", SyntaxDocument.KeywordType.OBJECT_LITERAL);
        javaKeywords.put("null", SyntaxDocument.KeywordType.OBJECT_LITERAL);
        javaKeywords.put("super", SyntaxDocument.KeywordType.OBJECT_LITERAL);
        javaKeywords.put("this", SyntaxDocument.KeywordType.OBJECT_LITERAL);

        javaKeywords.put("abstract", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("assert", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("break", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("case", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("catch", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("continue", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("do", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("else", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("extends", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("final", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("finally", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("for", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("if", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("implements", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("instanceof", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("native", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("new", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("private", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("protected", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("public", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("return", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("static", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("switch", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("synchronized", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("throw", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("throws", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("transient", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("try", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("var", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("volatile", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);
        javaKeywords.put("while", SyntaxDocument.KeywordType.GENERAL_CONSTRUCT);

        return javaKeywords;
    }

    @Contract(" -> new")
    private static @NotNull LanguageDelimiterInfo getJavaDelimiterInfo() {
        return new LanguageDelimiterInfo(";:{}()[]+-/%<=>!&|^~*", "\"'", "//",
                "/*", "*/");
    }
}
