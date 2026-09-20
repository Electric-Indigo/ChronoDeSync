package io.github.electricindigo.research.puzzle.causality;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class CausalityGenerator
{
    private CausalityGenerator() {}

    public static CausalityPuzzle generate(long seed, int difficulty)
    {
        int eventCount = 3 + difficulty;
        Random random = new Random(seed);

        List<CausalityEvent> events = new ArrayList<>();
        for (int i = 0; i < eventCount; i++)
        {
            events.add(new CausalityEvent("event_" + i));
        }

        List<CausalityEvent> solution = new ArrayList<>(events);
        Collections.shuffle(solution, random);

        List<CausalityClue> canidates = buildCanidateClues(solution, random);
        Collections.shuffle(canidates, random);

        List<CausalityClue> chosen = new ArrayList<>();
        for (CausalityClue clue : canidates)
        {
            chosen.add(clue);
            if (CausalitySolver.hasUniqueSolution(events, chosen))
            {
                return new CausalityPuzzle(events, chosen, solution);
            }
        }
        return generate(seed + 1, difficulty);
    }

    private static List<CausalityClue> buildCanidateClues(List<CausalityEvent> solution, Random random)
    {
        List<CausalityClue> canidates = new ArrayList<>();

        for (int i = 0; i < solution.size(); i++)
        {
            for (int j = i + 1; j < solution.size(); j++)
            {
                canidates.add(new CausalityClue.Before(solution.get(i), solution.get(j)));
            }
        }

        for (int i = 0; i < solution.size() - 1; i++) {
            for (int j = i + 2; j < solution.size(); j++) {
                canidates.add(new CausalityClue.NotAdjacent(solution.get(i), solution.get(j)));
            }
        }
        return canidates;
    }
}
