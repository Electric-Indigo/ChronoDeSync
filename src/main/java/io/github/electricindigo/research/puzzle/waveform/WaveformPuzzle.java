package io.github.electricindigo.research.puzzle.waveform;

import java.util.List;

public record WaveformPuzzle(List<WaveParams> targetWaves, double tolerance, int difficulty)
{
    public WaveformResult evaluate(List<WaveParams> submitted)
    {
        double error = WaveMath.rmsError(targetWaves, submitted);
            if (error <= tolerance)
            {
                return new WaveformResult(true, error, 0);
            }

            double overshoot = error - tolerance;
            int riftPoints = (int) Math.ceil(overshoot * 5.0);
            riftPoints = Math.min(riftPoints, 10);

            return new WaveformResult(false, error, riftPoints);
    }
}
