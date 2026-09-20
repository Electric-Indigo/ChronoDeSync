package io.github.electricindigo.research.puzzle.causality;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CausalityGeneratorTest
{
    @Test
    void generatedPuzzlesHaveExactlyOneSolution()
    {
        for (int difficulty = 1; difficulty <= 3; difficulty++)
        {
            for (long seed = 0; seed < 500; seed++)
            {
                CausalityPuzzle puzzle = CausalityGenerator.generate(seed, difficulty);
                int solutions = CausalitySolver.countSolutions(puzzle.events(), puzzle.clues(), 2);
                assertEquals(1, solutions,
                        "difficulty=" + difficulty + " seed=" + seed + " did not have a unique solution");
            }
        }
    }

    @Test
    void sameSeedProducesSamePuzzle()
    {
        CausalityPuzzle a = CausalityGenerator.generate(42, 2);
        CausalityPuzzle b = CausalityGenerator.generate(42, 2);

        assertEquals(a.solution(), b.solution());
        assertEquals(a.clues(), b.clues());
    }

    @Test
    void correctOrderIsAccepted()
    {
        CausalityPuzzle puzzle = CausalityGenerator.generate(7, 1);

        assertTrue(puzzle.isCorrect(puzzle.solution()));
    }

    @Test
    void wrongOrderIsRejected()
    {
        CausalityPuzzle puzzle = CausalityGenerator.generate(7, 1);

        List<CausalityEvent> scrambled = new ArrayList<>(puzzle.solution());
        Collections.reverse(scrambled);

        if (!scrambled.equals(puzzle.solution()))
        {
            assertFalse(puzzle.isCorrect(scrambled));
        }
    }

    @Test
    void eventCountScalesWithDifficulty()
    {
        assertEquals(4, CausalityGenerator.generate(1, 1).events().size());
        assertEquals(5, CausalityGenerator.generate(1, 2).events().size());
        assertEquals(6, CausalityGenerator.generate(1, 3).events().size());
    }
}
