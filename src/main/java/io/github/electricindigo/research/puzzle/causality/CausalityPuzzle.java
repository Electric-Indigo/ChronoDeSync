package io.github.electricindigo.research.puzzle.causality;

import java.util.List;

public record CausalityPuzzle(List<CausalityEvent> events, List<CausalityClue> clues, List<CausalityEvent> solution)
{
    public boolean isCorrect(List<CausalityEvent> submitted)
    {
        return submitted.equals(solution);
    }

    public boolean satisfiesAllClues(List<CausalityEvent> order)
    {
        return clues.stream().allMatch(clue -> clue.isSatisfied(order));
    }
}
