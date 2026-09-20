package io.github.electricindigo.research.puzzle.causality;

import java.util.ArrayList;
import java.util.List;

public final class CausalitySolver
{
    private CausalitySolver(){}

    public static int countSolutions(List<CausalityEvent> events, List<CausalityClue> clues, int stopAfter)
    {
        int[] count = {0};
        permute(new ArrayList<>(events), 0, clues, count, stopAfter);
        return count[0];
    }

    public static boolean hasUniqueSolution(List<CausalityEvent> events, List<CausalityClue> clues)
    {
        return countSolutions(events, clues, 1) == 1;
    }

    private static void permute(List<CausalityEvent> current, int k, List<CausalityClue> clues, int[] count, int stopAfter)
    {
        if (count[0] > stopAfter)
        {
            return;
        }
        if (k == current.size())
        {
            boolean valid = clues.stream().allMatch(causalityClue -> causalityClue.isSatisfied(current));
            if (valid)
            {
                count[0]++;
            }
            return;
        }
        for (int i = k; i < current.size(); i++)
        {
            swap(current, k, i);
            permute(current, k + 1, clues, count, stopAfter);
            swap(current, k, i);
        }
    }

    private static void swap(List<CausalityEvent> list, int i, int j)
    {
        CausalityEvent tmp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, tmp);
    }
}
