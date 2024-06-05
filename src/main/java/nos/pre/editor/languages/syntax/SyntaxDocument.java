package nos.pre.editor.languages.syntax;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.DocumentEvent;
import javax.swing.text.*;
import java.util.Map;

public class SyntaxDocument extends DefaultStyledDocument {
    public enum KeywordType {
        PREPROCESSOR_COMMAND,
        DATA_TYPE,
        OBJECT_LITERAL,
        GENERAL_CONSTRUCT,
    }

    private final Map<String, KeywordType> languageKeywords;
    private final SyntaxColorInfo syntaxColorInfo;
    private final LanguageDelimiterInfo delimiterInfo;

    private final Element rootElement = this.getDefaultRootElement();
    private boolean multiLineComment;

    public SyntaxDocument(Map<String, KeywordType> keywords, SyntaxColorInfo syntaxColorInfo, LanguageDelimiterInfo delimiterInfo) {
        this.languageKeywords = keywords;
        this.syntaxColorInfo = syntaxColorInfo;
        this.delimiterInfo = delimiterInfo;

        this.putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
    }

    /**
     * Override to apply syntax highlighting after the document has been updated
     */
    @Override
    public void insertString(int offset, @NotNull String str, AttributeSet a) throws BadLocationException {
        // if (str.equals("{")) str = addMatchingBrace(offset);
        super.insertString(offset, str, a);
        processChangedLines(offset, str.length());
    }

    /**
     * Override to apply syntax highlighting after the document has been updated
     */
    @Override
    public void remove(int offset, int length) throws BadLocationException {
        super.remove(offset, length);
        processChangedLines(offset, 0);
    }

    /**
     * Determine how many lines have been changed,
     * then apply highlighting to each line
     */
    private void processChangedLines(int offset, int length) throws BadLocationException {
        String content = this.getText(0, this.getLength());

        // The lines affected by the latest document update
        int startLine = rootElement.getElementIndex(offset);
        int endLine = rootElement.getElementIndex(offset + length);

        // Make sure all comment lines prior to the start line are commented
        // and determine if the start line is still in a multi-line comment
        setMultiLineComment(commentLinesBefore(content, startLine));

        // Do the actual highlighting
        for (int i = startLine; i <= endLine; i++)
            applyHighlighting(content, i);

        // Resolve highlighting to the next end multi line delimiter
        if (isMultiLineComment())
            commentLinesAfter(content, endLine);
        else
            highlightLinesAfter(content, endLine);
    }

    /**
     * Highlight lines when a multi-line comment is still 'open'
     * (i.e., matching end delimiter has not yet been encountered)
     */
    private boolean commentLinesBefore(String content, int line) {
        int offset = rootElement.getElement(line).getStartOffset();
        // Start of comment not found, nothing to do
        int startDelimiter = lastIndexOf(content, getMultiLineCommentStartDelimiter(), offset - 2);
        if (startDelimiter < 0)
            return false;

        // Matching start/end of comment found, nothing to do
        int endDelimiter = indexOf(content, getMultiLineCommentEndDelimiter(), startDelimiter);
        if (endDelimiter < offset & endDelimiter != -1)
            return false;

        // End of comment isn't found, highlight the lines
        this.setCharacterAttributes(startDelimiter, offset - startDelimiter + 1,
                this.syntaxColorInfo.getStyle_multiLineComment(), false);
        return true;
    }

    /**
     * Highlight comment lines to matching end delimiter
     */
    private void commentLinesAfter(String content, int line) {
        int offset = rootElement.getElement(line).getEndOffset();

        // End of comment isn't found, nothing to do
        int endDelimiter = indexOf(content, getMultiLineCommentEndDelimiter(), offset);
        if (endDelimiter < 0)
            return;

        // Matching start/end of comment found, comment the lines
        int startDelimiter = lastIndexOf(content, getMultiLineCommentStartDelimiter(), endDelimiter);
        if (startDelimiter < 0 || startDelimiter <= offset) {
            this.setCharacterAttributes(offset, endDelimiter - offset + 1,
                    this.syntaxColorInfo.getStyle_multiLineComment(), false);
        }
    }

    /**
     * Highlight lines to start or end delimiter
     */
    private void highlightLinesAfter(String content, int line) {
        int offset = rootElement.getElement(line).getEndOffset();

        // Start/End delimiter not found, nothing to do
        int startDelimiter = indexOf(content, getMultiLineCommentStartDelimiter(), offset);
        int endDelimiter = indexOf(content, getMultiLineCommentEndDelimiter(), offset);

        if (startDelimiter < 0)
            startDelimiter = content.length();
        if (endDelimiter < 0)
            endDelimiter = content.length();

        int delimiter = Math.min(startDelimiter, endDelimiter);
        if (delimiter < offset)
            return;

        // Start/End delimiter found, reapply highlighting
        int endLine = rootElement.getElementIndex(delimiter);

        for (int i = line + 1; i < endLine; i++) {
            Element branch = rootElement.getElement(i);
            Element leaf = this.getCharacterElement(branch.getStartOffset());
            AttributeSet as = leaf.getAttributes();

            if (as.isEqual(this.syntaxColorInfo.getStyle_multiLineComment()))
                applyHighlighting(content, i);
        }
    }

    /**
     * Parse the line to determine the appropriate highlighting
     */
    private void applyHighlighting(@NotNull String content, int line) {
        int startOffset = rootElement.getElement(line).getStartOffset();
        int endOffset = rootElement.getElement(line).getEndOffset() - 1;

        int lineLength = endOffset - startOffset;
        int contentLength = content.length();

        if (endOffset >= contentLength)
            endOffset = contentLength - 1;

        // check for multi line comments
        // (always set the comment attribute for the entire line)
        if (endingMultiLineComment(content, startOffset, endOffset) || isMultiLineComment()
                || startingMultiLineComment(content, startOffset, endOffset)) {
            this.setCharacterAttributes(startOffset, endOffset - startOffset + 1,
                    this.syntaxColorInfo.getStyle_multiLineComment(), false);
            return;
        }
        // set normal attributes for the line
        this.setCharacterAttributes(startOffset, lineLength, this.syntaxColorInfo.getStyle_normal(), true);

        // check for single line comment
        int index = content.indexOf(getSingleLineCommentDelimiter(), startOffset);
        if ((index > -1) && (index < endOffset)) {
            this.setCharacterAttributes(index, endOffset - index + 1, this.syntaxColorInfo.getStyle_singleLineComment(), false);
            endOffset = index - 1;
        }

        // check for tokens
        checkForTokens(content, startOffset, endOffset);
    }

    /**
     * Does this line contain the start delimiter?
     */
    private boolean startingMultiLineComment(String content, int startOffset, int endOffset) {
        int index = indexOf(content, getMultiLineCommentStartDelimiter(), startOffset);
        if ((index < 0) || (index > endOffset))
            return false;
        else {
            setMultiLineComment(true);
            return true;
        }
    }

    /**
     * Does this line contain the end delimiter?
     */
    private boolean endingMultiLineComment(String content, int startOffset, int endOffset) {
        int index = indexOf(content, getMultiLineCommentEndDelimiter(), startOffset);
        if ((index < 0) || (index > endOffset))
            return false;
        else {
            setMultiLineComment(false);
            return true;
        }
    }

    /**
     * We have found a start delimiter and are still searching for the end delimiter
     */
    @Contract(pure = true)
    private boolean isMultiLineComment() {
        return multiLineComment;
    }
    @Contract(mutates = "this")
    private void setMultiLineComment(boolean value) {
        multiLineComment = value;
    }

    /**
     * Parse the line for tokens to highlight
     */
    private void checkForTokens(String content, int startOffset, int endOffset) {
        while (startOffset <= endOffset) {
            // skip the delimiters to find the start of a new token
            while (isDelimiter(content.substring(startOffset, startOffset + 1))) {
                if (startOffset < endOffset)
                    startOffset++;
                else
                    return;
            }

            // Extract and process the entire token
            if (isQuoteDelimiter(content.substring(startOffset, startOffset + 1)))
                startOffset = getQuoteToken(content, startOffset, endOffset);
            else
                startOffset = getOtherToken(content, startOffset, endOffset);
        }
    }

    /**
     * Parse the line to get the quotes and highlight it
     */
    private int getQuoteToken(@NotNull String content, int startOffset, int endOffset) {
        String quoteDelimiter = content.substring(startOffset, startOffset + 1);
        String escapeString = getEscapeString(quoteDelimiter);
        int index;
        int endOfQuote = startOffset;

        // skip over the escape quotes in this quote
        index = content.indexOf(escapeString, endOfQuote + 1);
        while ((index > -1) && (index < endOffset)) {
            endOfQuote = index + 1;
            index = content.indexOf(escapeString, endOfQuote);
        }
        // now find the matching delimiter
        index = content.indexOf(quoteDelimiter, endOfQuote + 1);
        if ((index < 0) || (index > endOffset))
            endOfQuote = endOffset;
        else
            endOfQuote = index;

        this.setCharacterAttributes(startOffset, endOfQuote - startOffset + 1, this.syntaxColorInfo.getStyle_quote(), false);
        return endOfQuote + 1;
    }

    private int getOtherToken(String content, int startOffset, int endOffset) {
        int endOfToken = startOffset + 1;
        while (endOfToken <= endOffset) {
            if (isDelimiter(content.substring(endOfToken, endOfToken + 1)))
                break;

            endOfToken++;
        }
        String token = content.substring(startOffset, endOfToken);
        if (isKeyword(token)) {
            switch (getKeywordType(token)) {
                case PREPROCESSOR_COMMAND -> this.setCharacterAttributes(startOffset, endOfToken - startOffset,
                        this.syntaxColorInfo.getStyle_preprocessorCommand(), false);
                case DATA_TYPE -> this.setCharacterAttributes(startOffset, endOfToken - startOffset,
                        this.syntaxColorInfo.getStyle_dataType(), false);
                case OBJECT_LITERAL -> this.setCharacterAttributes(startOffset, endOfToken - startOffset,
                        this.syntaxColorInfo.getStyle_objectLiteral(), false);
                case GENERAL_CONSTRUCT -> this.setCharacterAttributes(startOffset, endOfToken - startOffset,
                        this.syntaxColorInfo.getStyle_generalConstructs(), false);
            }

            //this.setCharacterAttributes(startOffset, endOfToken - startOffset, keyword, false);
        } else if (isNumber(token)) {
            this.setCharacterAttributes(startOffset, endOfToken - startOffset, this.syntaxColorInfo.getStyle_number(), false);
        }
        return endOfToken + 1;
    }

    /**
     * This updates the colored text and prepares for undo event
     */
    protected void fireInsertUpdate(DocumentEvent evt) {
        super.fireInsertUpdate(evt);

        try {
            processChangedLines(evt.getOffset(), evt.getLength());
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This updates the colored text and does the undo operation
     */
    protected void fireRemoveUpdate(DocumentEvent evt) {
        super.fireRemoveUpdate(evt);

        try {
            processChangedLines(evt.getOffset(), evt.getLength());
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Assume the needle will the found at the start/end of the line
     */
    private int indexOf(@NotNull String content, String needle, int offset) {
        int index;
        while ((index = content.indexOf(needle, offset)) != -1) {
            String text = getLine(content, index).trim();

            if (text.startsWith(needle) || text.endsWith(needle))
                break;
            else
                offset = index + 1;
        }
        return index;
    }

    /**
     * Assume the needle will the found at the start/end of the line
     */
    private int lastIndexOf(@NotNull String content, String needle, int offset) {
        int index;
        while ((index = content.lastIndexOf(needle, offset)) != -1) {
            String text = getLine(content, index).trim();

            if (text.startsWith(needle) || text.endsWith(needle))
                break;
            else
                offset = index - 1;
        }
        return index;
    }

    private @NotNull String getLine(@NotNull String content, int offset) {
        int line = rootElement.getElementIndex(offset);
        Element lineElement = rootElement.getElement(line);
        int start = lineElement.getStartOffset();
        int end = lineElement.getEndOffset();

        return content.substring(start, end - 1);
    }

    protected boolean isDelimiter(@NotNull String character) {
        return Character.isWhitespace(character.charAt(0)) || this.delimiterInfo.delimiters().contains(character);
    }
    protected boolean isQuoteDelimiter(String character) {
        return this.delimiterInfo.quoteDelimiters().contains(character);
    }

    protected String getSingleLineCommentDelimiter() {
        return this.delimiterInfo.singleLineCommentDelimiter();
    }
    protected String getMultiLineCommentStartDelimiter() {
        return this.delimiterInfo.multiLineCommentStartDelimiter();
    }
    protected String getMultiLineCommentEndDelimiter() {
        return this.delimiterInfo.multiLineCommentEndDelimiter();
    }

    protected boolean isKeyword(@NotNull String token) {
        return languageKeywords.containsKey(token);
    }
    protected KeywordType getKeywordType(@NotNull String token) {
        return languageKeywords.get(token);
    }

    protected boolean isNumber(@NotNull String token) {
        try {
            Double.parseDouble(token);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    protected String getEscapeString(String quoteDelimiter) {
        return "\\" + quoteDelimiter;
    }

    /*
    protected String addMatchingBrace(int offset) throws BadLocationException {
        StringBuilder whiteSpace = new StringBuilder();

        int line = rootElement.getElementIndex(offset);
        int i = rootElement.getElement(line).getStartOffset();
        while (true) {
            String temp = this.getText(i, 1);
            if (temp.equals(" ") || temp.equals("\t")) {
                whiteSpace.append(temp);
                i++;
            } else
                break;
        }
        return "{\n" + whiteSpace + whiteSpace + "\n" + whiteSpace + "}";
    }
    */
}
