package io.github.electricindigo.research.puzzle.waveform;

import java.util.List;

public final class WaveMath
{
    private static final int SAMPLE_COUNT = 200;
    private static final double SAMPLE_RANGE = 4 * Math.PI;

    private WaveMath(){}

    public static double sampleSum(List<WaveParams> waves, double t)
    {
        double total = 0.0;
        for (WaveParams wave : waves)
        {
            total += wave.sample(t);
        }
        return total;
    }

    public static double rmsError(List<WaveParams> target, List<WaveParams> submitted)
    {
        double sumSquaredError = 0.0;

        for (int i = 0; i < SAMPLE_COUNT; i++)
        {
            double t = (SAMPLE_RANGE * i) / (SAMPLE_COUNT - 1);
            double diff = sampleSum(target, t) - sampleSum(submitted, t);
            sumSquaredError += diff * diff;
        }

        return Math.sqrt(sumSquaredError / SAMPLE_COUNT);
    }
}
