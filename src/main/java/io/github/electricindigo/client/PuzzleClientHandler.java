package io.github.electricindigo.client;

import net.minecraft.client.Minecraft;

public final class PuzzleClientHandler
{
    private PuzzleClientHandler(){}

    public static void openCausalityPuzzle(long seed, int difficulty)
    {
        Minecraft.getInstance().gui.setScreen(new CausalitySortScreen(seed, difficulty));
    }

    public static void openWaveformPuzzle(long seed, int difficulty)
    {
        Minecraft.getInstance().gui.setScreen(new WaveformSyncScreen(seed, difficulty));
    }
}
