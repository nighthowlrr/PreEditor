package nos.pre.editor.languages.java;

import nos.pre.editor.autocomplete.completions.BaseCompletion;
import nos.pre.editor.autocomplete.completions.KeywordCompletion;
import nos.pre.editor.languages.syntax.KeywordType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum JavaKeywords {
    PACKAGE("package", KeywordType.PREPROCESSOR_COMMAND),
    IMPORT("import", KeywordType.PREPROCESSOR_COMMAND),

    CLASS("class", KeywordType.DATA_TYPE),
    INTERFACE("interface", KeywordType.DATA_TYPE),
    ENUM("enum", KeywordType.DATA_TYPE),
    RECORD("record", KeywordType.DATA_TYPE),
    BYTE("byte", KeywordType.DATA_TYPE),
    SHORT("short", KeywordType.DATA_TYPE),
    INT("int", KeywordType.DATA_TYPE),
    LONG("long", KeywordType.DATA_TYPE),
    FLOAT("float", KeywordType.DATA_TYPE),
    DOUBLE("double", KeywordType.DATA_TYPE),
    BOOLEAN("boolean", KeywordType.DATA_TYPE),
    CHAR("char", KeywordType.DATA_TYPE),
    VOID("void", KeywordType.DATA_TYPE),

    TRUE("true", KeywordType.OBJECT_LITERAL),
    FALSE("false", KeywordType.OBJECT_LITERAL),
    NULL("null", KeywordType.OBJECT_LITERAL),
    SUPER("super", KeywordType.OBJECT_LITERAL),
    THIS("this", KeywordType.OBJECT_LITERAL),

    ABSTRACT("abstract", KeywordType.GENERAL_CONSTRUCT),
    ASSERT("assert", KeywordType.GENERAL_CONSTRUCT),
    BREAK("break", KeywordType.GENERAL_CONSTRUCT),
    CASE("case", KeywordType.GENERAL_CONSTRUCT),
    CATCH("catch", KeywordType.GENERAL_CONSTRUCT),
    CONTINUE("continue", KeywordType.GENERAL_CONSTRUCT),
    DO("do", KeywordType.GENERAL_CONSTRUCT),
    ELSE("else", KeywordType.GENERAL_CONSTRUCT),
    EXTENDS("extends", KeywordType.GENERAL_CONSTRUCT),
    FINAL("final", KeywordType.GENERAL_CONSTRUCT),
    FINALLY("finally", KeywordType.GENERAL_CONSTRUCT),
    FOR("for", KeywordType.GENERAL_CONSTRUCT),
    IF("if", KeywordType.GENERAL_CONSTRUCT),
    IMPLEMENTS("implements", KeywordType.GENERAL_CONSTRUCT),
    INSTANCEOF("instanceof", KeywordType.GENERAL_CONSTRUCT),
    NATIVE("native", KeywordType.GENERAL_CONSTRUCT),
    NEW("new", KeywordType.GENERAL_CONSTRUCT),
    PRIVATE("private", KeywordType.GENERAL_CONSTRUCT),
    PROTECTED("protected", KeywordType.GENERAL_CONSTRUCT),
    PUBLIC("public", KeywordType.GENERAL_CONSTRUCT),
    RETURN("return", KeywordType.GENERAL_CONSTRUCT),
    STATIC("static", KeywordType.GENERAL_CONSTRUCT),
    SWITCH("switch", KeywordType.GENERAL_CONSTRUCT),
    SYNCHRONIZED("synchronized", KeywordType.GENERAL_CONSTRUCT),
    THROW("throw", KeywordType.GENERAL_CONSTRUCT),
    THROWS("throws", KeywordType.GENERAL_CONSTRUCT),
    TRANSIENT("transient", KeywordType.GENERAL_CONSTRUCT),
    TRY("try", KeywordType.GENERAL_CONSTRUCT),
    VAR("var", KeywordType.GENERAL_CONSTRUCT),
    VOLATILE("volatile", KeywordType.GENERAL_CONSTRUCT),
    WHILE("while", KeywordType.GENERAL_CONSTRUCT),
    ;

    private final String keyword;
    private final KeywordType keywordType;

    JavaKeywords(String keyword, KeywordType keywordType) {
        this.keyword = keyword;
        this.keywordType = keywordType;
    }

    public KeywordType getKeywordType() {
        return keywordType;
    }
    public String getKeyword() {
        return keyword;
    }

    public static HashMap<String, KeywordType> getKeywordMap() {
        return EnumSet.allOf(JavaKeywords.class).stream()
                .collect(Collectors.toMap(JavaKeywords::getKeyword, JavaKeywords::getKeywordType,
                        (o1, o2) -> o1, HashMap::new));
    }

    public static List<String> getListOfKeywords() {
        return Stream.of(JavaKeywords.values()).map(JavaKeywords::getKeyword).toList();
    }

    }
}
