package nos.pre.editor.functions;

import org.jetbrains.annotations.NotNull;

import javax.swing.event.CaretEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.ArrayList;

public class BracketMatching {
    private final String allBrackets = "()[]{}";
    
    private final Document document;
    private final Highlighter textHighlighter;
    private final Highlighter.HighlightPainter matchingHighlightpainter;

    private final ArrayList<BracketHighlightInfo> bracketHighlights = new ArrayList<>();

    public BracketMatching(Document document, Highlighter highlighter, Color highlightColor) {
        this.document = document;
        this.textHighlighter = highlighter;
        this.matchingHighlightpainter = new DefaultHighlighter.DefaultHighlightPainter(highlightColor);
    }


    public void updateBracketMatching(@NotNull CaretEvent e) {
        this.resetHighlights();

        try {
            int firstBracketOffset = checkForBracket(e.getDot());
            int matchingBracketOffset = findMatchingBracket(firstBracketOffset);

            if (matchingBracketOffset != -1) {
                this.addBracketHighlights(firstBracketOffset, matchingBracketOffset);
            }
        } catch (BadLocationException ex) {
            // TODO: Proper exception handling. (Continuous errors until cursor is on a bracket)
            System.out.println("BracketMatching.updateBracketMatching: " + ex + ". {not a problem :)}");
        }
    }

    private void addBracketHighlights(int startPos, int endPos) {
        try {
            Object firstBracketHighlightTag = this.textHighlighter.addHighlight(startPos, startPos + 1,
                    this.matchingHighlightpainter);
            Object matchingBracketHighlightInfo = this.textHighlighter.addHighlight(endPos, endPos + 1,
                    this.matchingHighlightpainter);

            this.bracketHighlights.add(new BracketHighlightInfo(firstBracketHighlightTag, startPos, startPos + 1));
            this.bracketHighlights.add(new BracketHighlightInfo(matchingBracketHighlightInfo, endPos, endPos + 1));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void resetHighlights() {
        if (! this.bracketHighlights.isEmpty()) {
            for (BracketHighlightInfo highlightInfo : this.bracketHighlights) {
                this.textHighlighter.removeHighlight(highlightInfo.highlightTag);
            }

            this.bracketHighlights.clear();
        }
    }

    /**
     * Returns the offset of the bracket matching the one at the specified <code>firstBracketOffset</code> of the document,
     * or -1 if the bracket is unmatched, or if char at <code>firstBracketOffset</code> is not a bracket.
     * @param firstBracketOffset The location of the first bracket in the document.
     * @return The offset of the matching bracket, or -1 if bracket is unmatched.
     * @throws BadLocationException If out-of-bounds access was attempted on the document text
     */
    private int findMatchingBracket(int firstBracketOffset) throws BadLocationException {
        //@throws IllegalArgumentException If char at <code>firstBracketOffset</code> is not a bracket.

        if (this.document.getLength() == 0) {
            return -1;
        }

        int newFirstBracketOffset = checkForBracket(firstBracketOffset);
        if (newFirstBracketOffset != - 1) {
            firstBracketOffset = newFirstBracketOffset;
        } else {
            //throw new IllegalArgumentException("CodeRead.findMatchingBracket(): char at location \"firstBracketOffset\" is not a bracket.");
            return - 1;
        }

        char firstBracket = this.document.getText(firstBracketOffset, 1).charAt(0);

        char matchingBracket;
        boolean scanForward;

        switch (firstBracket) {
            case '(' -> {
                matchingBracket = ')';
                scanForward = true;
            }
            case ')' -> {
                matchingBracket = '(';
                scanForward = false;
            }
            case '[' -> {
                matchingBracket = ']';
                scanForward = true;
            }
            case ']' -> {
                matchingBracket = '[';
                scanForward = false;
            }
            case '{' -> {
                matchingBracket = '}';
                scanForward = true;
            }
            case '}' -> {
                matchingBracket = '{';
                scanForward = false;
            }
            default -> {
                //throw new IllegalArgumentException("CodeRead.findMatchingBracket(): char at location \"firstBracketOffset\" is not a bracket.");
                return -1;
            }
        }

        if (scanForward) {
            int closingBracketCount = 0;

            String text = this.document.getText(firstBracketOffset, this.document.getLength() - firstBracketOffset);

            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == firstBracket) {
                    closingBracketCount++;
                } else if (text.charAt(i) == matchingBracket) {
                    if (--closingBracketCount == 0) {
                        return firstBracketOffset + i;
                    }
                }
            }
        } else {
            int openingBracketCount = 0;

            String text = this.document.getText(0, firstBracketOffset);

            for (int i = text.length() - 1; i >= 0; i--) {
                if (text.charAt(i) == firstBracket) {
                    openingBracketCount++;
                } else if (text.charAt(i) == matchingBracket) {
                    if (openingBracketCount > 0) {
                        --openingBracketCount;
                    } else if (openingBracketCount == 0) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    /**
     * If char at <code>offset</code> is a bracket, returns <code>offset</code>, else if char before <code>offset</code>
     * is a bracket, returns <code>offset - 1</code>. Otherwise, returns -1.
     * @param offset The location to search for brackets.
     * @return
     * @throws BadLocationException
     */
    private int checkForBracket(int offset) throws BadLocationException {
        if (this.allBrackets.contains(this.document.getText(offset, 1))) {
            return offset;
        } else if (this.allBrackets.contains(this.document.getText(offset - 1, 1))) {
            return offset - 1;
        } else {
            //throw new IllegalArgumentException("CodeRead.checkForBracket(): char at location \"firstBracketOffset\" is not a bracket.");
            return -1;
        }
    }

    private record BracketHighlightInfo(Object highlightTag, int startPos, int endPos) {}
}
