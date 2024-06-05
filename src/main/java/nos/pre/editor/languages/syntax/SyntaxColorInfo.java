package nos.pre.editor.languages.syntax;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class SyntaxColorInfo {
    private final MutableAttributeSet style_normal = new SimpleAttributeSet();
    private final MutableAttributeSet style_preprocessorCommand = new SimpleAttributeSet();
    private final MutableAttributeSet style_dataType = new SimpleAttributeSet();
    private final MutableAttributeSet style_objectLiteral = new SimpleAttributeSet();
    private final MutableAttributeSet style_generalConstructs = new SimpleAttributeSet();
    private final MutableAttributeSet style_number = new SimpleAttributeSet();
    private final MutableAttributeSet style_singleLineComment = new SimpleAttributeSet();
    private final MutableAttributeSet style_multiLineComment = new SimpleAttributeSet();
    private final MutableAttributeSet style_quote = new SimpleAttributeSet();

    public SyntaxColorInfo() {}

    public MutableAttributeSet getStyle_normal() {
        return style_normal;
    }
    public MutableAttributeSet getStyle_preprocessorCommand() {
        return style_preprocessorCommand;
    }
    public MutableAttributeSet getStyle_dataType() {
        return style_dataType;
    }
    public MutableAttributeSet getStyle_objectLiteral() {
        return style_objectLiteral;
    }
    public MutableAttributeSet getStyle_generalConstructs() {
        return style_generalConstructs;
    }
    public MutableAttributeSet getStyle_number() {
        return style_number;
    }
    public MutableAttributeSet getStyle_singleLineComment() {
        return style_singleLineComment;
    }
    public MutableAttributeSet getStyle_multiLineComment() {
        return style_multiLineComment;
    }
    public MutableAttributeSet getStyle_quote() {
        return style_quote;
    }

    public void setSyntaxNormalAttributes(Color color, boolean setItalic, boolean setBold) {
        StyleConstants.setForeground(this.style_normal, color);
        StyleConstants.setItalic(this.style_normal, setItalic);
        StyleConstants.setBold(this.style_normal, setBold);
    }
    public void setSyntaxPreprocessorCommandAttributes(Color color, boolean setItalic, boolean setBold) {
        StyleConstants.setForeground(this.style_preprocessorCommand, color);
        StyleConstants.setItalic(this.style_preprocessorCommand, setItalic);
        StyleConstants.setBold(this.style_preprocessorCommand, setBold);
    }
    public void setSyntaxDataTypeAttributes(Color color, boolean setItalic, boolean setBold) {
        StyleConstants.setForeground(this.style_dataType, color);
        StyleConstants.setItalic(this.style_dataType, setItalic);
        StyleConstants.setBold(this.style_dataType, setBold);
    }
    public void setSyntaxObjectLiteralAttributes(Color color, boolean setItalic, boolean setBold) {
        StyleConstants.setForeground(this.style_objectLiteral, color);
        StyleConstants.setItalic(this.style_objectLiteral, setItalic);
        StyleConstants.setBold(this.style_objectLiteral, setBold);
    }
    public void setSyntaxGeneralConstructsAttributes(Color color, boolean setItalic, boolean setBold) {
        StyleConstants.setForeground(this.style_generalConstructs, color);
        StyleConstants.setItalic(this.style_generalConstructs, setItalic);
        StyleConstants.setBold(this.style_generalConstructs, setBold);
    }
    public void setSyntaxNumberAttributes(Color color, boolean setItalic, boolean setBold) {
        StyleConstants.setForeground(this.style_number, color);
        StyleConstants.setItalic(this.style_number, setItalic);
        StyleConstants.setBold(this.style_number, setBold);
    }
    public void setSyntaxSingleLineAttributes(Color color, boolean setItalic, boolean setBold) {
        StyleConstants.setForeground(this.style_singleLineComment, color);
        StyleConstants.setItalic(this.style_singleLineComment, setItalic);
        StyleConstants.setBold(this.style_singleLineComment, setBold);
    }
    public void setSyntaxMultiLineCommentAttributes(Color color, boolean setItalic, boolean setBold) {
        StyleConstants.setForeground(this.style_multiLineComment, color);
        StyleConstants.setItalic(this.style_multiLineComment, setItalic);
        StyleConstants.setBold(this.style_multiLineComment, setBold);
    }
    public void setSyntaxQuoteAttributes(Color color, boolean setItalic, boolean setBold) {
        StyleConstants.setForeground(this.style_quote, color);
        StyleConstants.setItalic(this.style_quote, setItalic);
        StyleConstants.setBold(this.style_quote, setBold);
    }

    public void setForAllSyntax(Color color, boolean setItalic, boolean setBold) {
        setSyntaxNormalAttributes(color, setItalic, setBold);
        setSyntaxPreprocessorCommandAttributes(color, setItalic, setBold);
        setSyntaxDataTypeAttributes(color, setItalic, setBold);
        setSyntaxObjectLiteralAttributes(color, setItalic, setBold);
        setSyntaxGeneralConstructsAttributes(color, setItalic, setBold);
        setSyntaxNumberAttributes(color, setItalic, setBold);
        setSyntaxSingleLineAttributes(color, setItalic, setBold);
        setSyntaxMultiLineCommentAttributes(color, setItalic, setBold);
        setSyntaxQuoteAttributes(color, setItalic, setBold);
    }
}
