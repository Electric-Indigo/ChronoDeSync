package io.github.electricindigo.research.puzzle.waveform;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WaveformGeneratorTest
{
    @Test
    void perfectSubmissionIsAlwaysClean() {
        for (int difficulty = 1; difficulty <= 3; difficulty++) {
            for (long seed = 0; seed < 100; seed++) {
                WaveformPuzzle puzzle = WaveformGenerator.generate(seed, difficulty);
                WaveformResult result = puzzle.evaluate(puzzle.targetWaves());

                assertTrue(result.clean(),
                        "difficulty=" + difficulty + " seed=" + seed + " exact match wasn't clean");
                assertEquals(0, result.riftPoints());
                assertEquals(0.0, result.error(), 0.0001);
            }
        }
    }

    @Test
    void wildlyWrongSubmissionScoresPositiveRiftPoints() {
        WaveformPuzzle puzzle = WaveformGenerator.generate(1, 2);

        List<WaveParams> way_off = List.of(
                new WaveParams(10.0, 10.0, 0.0),
                new WaveParams(10.0, 10.0, 0.0)
        );

        WaveformResult result = puzzle.evaluate(way_off);

        assertFalse(result.clean());
        assertTrue(result.riftPoints() > 0);
    }

    @Test
    void sameSeedProducesSamePuzzle() {
        WaveformPuzzle a = WaveformGenerator.generate(42, 2);
        WaveformPuzzle b = WaveformGenerator.generate(42, 2);

        assertEquals(a.targetWaves(), b.targetWaves());
        assertEquals(a.tolerance(), b.tolerance());
    }

    @Test
    void layerCountScalesWithDifficulty() {
        assertEquals(1, WaveformGenerator.generate(1, 1).targetWaves().size());
        assertEquals(2, WaveformGenerator.generate(1, 2).targetWaves().size());
        assertEquals(3, WaveformGenerator.generate(1, 3).targetWaves().size());
    }

    @Test
    void toleranceTightensWithDifficulty() {
        double t1 = WaveformGenerator.generate(1, 1).tolerance();
        double t2 = WaveformGenerator.generate(1, 2).tolerance();
        double t3 = WaveformGenerator.generate(1, 3).tolerance();

        assertTrue(t1 > t2);
        assertTrue(t2 > t3);
    }

    @Test
    void riftPointsAreCappedAtTen() {
        WaveformPuzzle puzzle = WaveformGenerator.generate(1, 1);

        List<WaveParams> extreme = List.of(new WaveParams(1000.0, 1000.0, 0.0));
        WaveformResult result = puzzle.evaluate(extreme);

        assertEquals(10, result.riftPoints());
    }
}
