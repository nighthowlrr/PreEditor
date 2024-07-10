package nos.pre.editor.autoComplete.completions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class CompletionList extends ArrayList<BaseCompletion> {

    public CompletionList getMatchingCompletions(String subWordToMatch) {
        CompletionList matchingCompletions = new CompletionList();

        for (BaseCompletion completion : this) {
            if (completion.isCompletionMatching(subWordToMatch)) {
                matchingCompletions.add(completion);
            }
        }

        CompletionList.sortCompletions(matchingCompletions);

        return matchingCompletions;
    }

    public static void sortCompletions(@NotNull CompletionList completions) {
        // Comparing By CompletionText for KeywordCompletion, or InputText for TemplateCompletion. For alphabetically sorting
        completions.sort(new Comparator<>() {
            @Override
            public int compare(BaseCompletion o1, BaseCompletion o2) {
                return o1.compareTo(o2);
            }
        });

        Collections.reverse(completions);
        // Reversed because when collections is reversed for descending order of relevance, completions are also
        // alphabetically reversed. So reversing after alphabetically sorting will un-reverse in later
        // reversing for descending order of relevance

        // Comparing by Relevance, for sorting by relevance.
        completions.sort(new Comparator<>() {
            @Override
            public int compare(BaseCompletion o1, BaseCompletion o2) {
                return Integer.compare(o1.getRelevance(), o2.getRelevance());
            }
        });
        Collections.reverse(completions); // Reverse for descending order of relevance
    }

    public static BaseCompletion[] getCompletionsAsArray(CompletionList listToArray) {
        return listToArray.toArray(new BaseCompletion[listToArray.size()]);
    }

    public static @NotNull CompletionList combineAndSortCompletions(CompletionList @NotNull ... completionLists) {
        if (completionLists.length > 1) {
            CompletionList masterCompletionList = new CompletionList();

            Arrays.stream(completionLists).forEach(masterCompletionList::addAll);
            CompletionList.sortCompletions(masterCompletionList);

            return masterCompletionList;
        } else if (completionLists.length == 1) {
            return completionLists[0];
        } else {
            throw new IllegalArgumentException("CompletionList.combineAndSortCompletions(): no CompletionLists were provided");
        }
    }
}
