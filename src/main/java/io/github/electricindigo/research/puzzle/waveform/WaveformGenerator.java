package io.github.electricindigo.research.puzzle.waveform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class WaveformGenerator
{
    private WaveformGenerator(){}

    public static WaveformPuzzle generate(long seed, int difficulty)
    {
        Random random = new Random(seed);
        int layerCount = difficulty;

        List<WaveParams> waves = new ArrayList<>();
        for (int i = 0; i < layerCount; i++)
        {
            double amplitude = 0.5 + random.nextDouble() * 1.5;
            double frequency = 0.5 + random.nextDouble() * 2.5;
            double phase = random.nextDouble() * 2 * Math.PI;
            waves.add(new WaveParams(amplitude, frequency, phase));
        }
        double tolerance = 0.5 - (difficulty * 0.1);

        return new WaveformPuzzle(waves, tolerance, difficulty);
    }
}
