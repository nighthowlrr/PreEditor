package nos.pre.editor.languages;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record LanguageDelimiterInfo(String delimiters, String quoteDelimiters, String singleLineCommentDelimiter,
                                    String multiLineCommentStartDelimiter, String multiLineCommentEndDelimiter) {

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return this + "| delimiters: " + delimiters + " , quoteDelimiters: " + quoteDelimiters
                + " , commentDelimiter: " + singleLineCommentDelimiter
                + " , MultiLineCommentDelimiters: " + multiLineCommentStartDelimiter + " " + multiLineCommentEndDelimiter + " .";
    }
}
