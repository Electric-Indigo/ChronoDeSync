package io.github.electricindigo.research.puzzle.waveform;

public record WaveParams(double amplitude, double frequency, double phase)
{
    public double sample(double t)
    {
        return amplitude * Math.sin(frequency * t + phase);
    }
}
