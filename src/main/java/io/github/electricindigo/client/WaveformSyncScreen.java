package io.github.electricindigo.client;

import io.github.electricindigo.research.puzzle.waveform.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class WaveformSyncScreen extends Screen
{
    private static final int LIST_X = 30;
    private static final int GRAPH_X = LIST_X + 3 * (100 + 10);
    private static final int GRAPH_Y = 20;
    private static final int GRAPH_WIDTH = 110;
    private static final int GRAPH_HEIGHT = 60;
    private static final int SUBMIT_WIDTH = 100;
    private static final int SUBMIT_HEIGHT = 20;
    private static final int SUBMIT_GAP = 50;
    private static final double GRAPH_SAMPLE_RANGE = 4 * Math.PI;
    private static final double GRAPH_AMPLITUDE_SCALE = 7.5;

    private final WaveformPuzzle puzzle;
    private final List<MutableWave> layers;

    private final List<Integer> layerRowY = new ArrayList<>();

    private WaveformResult lastResult = null;
    private String resultMessage = null;

    public WaveformSyncScreen(long seed, int difficulty)
    {
        super(Component.literal("Waveform Sync"));
        this.puzzle = WaveformGenerator.generate(seed, difficulty);

        this.layers = new ArrayList<>();
        for (int i = 0; i < puzzle.targetWaves().size(); i++)
        {
            MutableWave w = new MutableWave();
            w.amplitude = 1.0;
            w.frequency = 1.0;
            w.phase = 0.0;
            layers.add(w);
        }
    }

    @Override
    protected void init() {
        layerRowY.clear();

        int sliderWidth = 100;
        int sliderHeight = 20;
        int colGap = 10;
        int rowGap = 6;
        int miniGraphHeight = 40;
        int blockGap = 10;
        int startY = 30;

        int submitX = GRAPH_X + GRAPH_WIDTH / 2 - SUBMIT_WIDTH / 2;
        int submitY = GRAPH_Y + GRAPH_HEIGHT + SUBMIT_GAP;

        addRenderableWidget(Button.builder(Component.literal("Submit"), b -> onSubmit())
                .bounds(submitX, submitY, SUBMIT_WIDTH, SUBMIT_HEIGHT)
                .build());

        int y = startY;
        for (int i = 0; i < layers.size(); i++)
        {
            int layerIndex = i;
            int rowY = y;
            layerRowY.add(rowY);
            MutableWave w = layers.get(layerIndex);

            addRenderableWidget(new LabeledSlider(LIST_X, rowY, sliderWidth, sliderHeight,
                    "L" + (layerIndex + 1) + " Amp", 0.0, 3.0, w.amplitude,
                    v -> layers.get(layerIndex).amplitude = v));

            addRenderableWidget(new LabeledSlider(LIST_X + sliderWidth + colGap, rowY, sliderWidth, sliderHeight,
                    "L" + (layerIndex + 1) + " Freq", 0.0, 4.0, w.frequency,
                    v -> layers.get(layerIndex).frequency = v));

            addRenderableWidget(new LabeledSlider(LIST_X + 2 * (sliderWidth + colGap), rowY, sliderWidth, sliderHeight,
                    "L" + (layerIndex + 1) + " Phase", 0.0, Math.PI * 2, w.phase,
                    v -> layers.get(layerIndex).phase = v));

            y = rowY + sliderHeight + rowGap + miniGraphHeight + blockGap;
        }
    }

    private void onSubmit()
    {
        lastResult = puzzle.evaluate(currentSubmittedWaves());
        resultMessage = lastResult.clean()
                ? String.format("Clean sync! error=%.4f", lastResult.error())
                : String.format("Not synced. error=%.4f rift=%d", lastResult.error(), lastResult.riftPoints());

        Minecraft.getInstance().gui.hud.getChat().addClientSystemMessage(Component.literal(resultMessage));
        this.onClose();
    }

    private List<WaveParams> currentSubmittedWaves()
    {
        List<WaveParams> result = new ArrayList<>();
        for (MutableWave w : layers)
        {
            result.add(new WaveParams(w.amplitude, w.frequency, w.phase));
        }
        return result;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a)
    {
        int miniGraphWidth = 3 * (100 + 10) - 10;
        int miniGraphHeight = 40;

        for (int i = 0; i < layers.size(); i++)
        {
            int rowY = layerRowY.get(i);
            int graphY = rowY + 20 + 6;
            MutableWave w = layers.get(i);
            WaveParams submittedLayer = new WaveParams(w.amplitude, w.frequency, w.phase);
            WaveParams targetLayer = puzzle.targetWaves().get(i);
            drawMiniGraph(graphics, LIST_X, graphY, miniGraphWidth, miniGraphHeight, targetLayer, submittedLayer);
        }
        drawGraph(graphics);

        double liveError = WaveMath.rmsError(puzzle.targetWaves(), currentSubmittedWaves());
        graphics.text(font, String.format("Live error: %.4f", liveError), GRAPH_X, GRAPH_Y + GRAPH_HEIGHT + 10, 0xFFAAAAAA);

        if (resultMessage != null)
        {
            graphics.text(font, resultMessage, LIST_X, 15, 0xFFFFFFFF);
        }
        super.extractBackground(graphics, mouseX, mouseY, a);
    }

    private void drawGraph(GuiGraphicsExtractor graphics)
    {
        graphics.fill(GRAPH_X - 1, GRAPH_Y -1, GRAPH_X + GRAPH_WIDTH + 1, GRAPH_Y + GRAPH_HEIGHT + 1, 0xFF3F3F3F);
        graphics.fill(GRAPH_X, GRAPH_Y, GRAPH_X + GRAPH_WIDTH, GRAPH_Y + GRAPH_HEIGHT, 0xC0101010);

        int zeroY = valueToPixelY(0);
        graphics.fill(GRAPH_X, zeroY, GRAPH_X + GRAPH_WIDTH, zeroY + 1, 0xFF444444);

        List<WaveParams> submitted = currentSubmittedWaves();

        int prevTargetY = -1;
        int prevSubmittedY = -1;
        int lineThickness = 2;

        for (int px = 0; px < GRAPH_WIDTH; px++)
        {
            double t = (px / (double) (GRAPH_WIDTH -1)) * GRAPH_SAMPLE_RANGE;

            double targetVal = WaveMath.sampleSum(puzzle.targetWaves(), t);
            double submittedVal = WaveMath.sampleSum(submitted, t);

            int targetY = valueToPixelY(targetVal);
            int submittedY = valueToPixelY(submittedVal);

            if (prevTargetY != -1)
            {
                int top = Math.min(prevTargetY, targetY);
                int bottom = Math.max(prevTargetY, targetY) + lineThickness;
                graphics.fill(GRAPH_X + px - 1, top, GRAPH_X + px + 1, bottom, 0x8055AAFF);
            }
            if (prevSubmittedY != -1)
            {
                int top = Math.min(prevSubmittedY, submittedY);
                int bottom = Math.max(prevSubmittedY, submittedY) + lineThickness;
                graphics.fill(GRAPH_X + px - 1, top, GRAPH_X + px + 1, bottom, 0xD9FFAA00);
            }

            prevTargetY = targetY;
            prevSubmittedY = submittedY;
        }
        graphics.text(font, "Target", GRAPH_X, GRAPH_Y - 10, 0xFF55AAFF);
        graphics.text(font, "Yours", GRAPH_X + 60, GRAPH_Y - 10, 0xFFFFAA00);
    }

    private void drawMiniGraph(GuiGraphicsExtractor graphics, int x, int y, int width, int height, WaveParams target, WaveParams submitted)
    {
        graphics.fill(x - 1, y - 1, x + width + 1, y + height + 1, 0xFF3F3F3F);
        graphics.fill(x, y, x + width, y + height, 0xC0101010);

        double scale = 3.5;
        int zeroY = y + height - (int) (((0 + scale) / (2 * scale)) * height);
        graphics.fill(x, zeroY, x + width, zeroY + 1, 0xFF444444);

        int prevTargetY = -1;
        int prevSubmittedY = -1;
        int lineThickness = 2;

        for (int px = 0; px < width; px++)
        {
            double t = (px / (double) (width - 1)) * GRAPH_SAMPLE_RANGE;

            double targetVal = target.sample(t);
            double submittedVal = submitted.sample(t);

            int targetY = y + height - (int) (((clamp(targetVal, scale) + scale) / (2 * scale)) * height);
            int submittedY = y + height - (int) (((clamp(submittedVal, scale) + scale) / (2 * scale)) * height);

            if (prevTargetY != -1)
            {
                int top = Math.min(prevTargetY, targetY);
                int bottom = Math.max(prevTargetY, targetY) + lineThickness;
                graphics.fill(x + px - 1, top, x + px + 1, bottom, 0x8055AAFF);
            }

            if (prevSubmittedY != -1)
            {
                int top = Math.min(prevSubmittedY, submittedY);
                int bottom = Math.max(prevSubmittedY, submittedY) + lineThickness;
                graphics.fill(x + px - 1, top, x + px + 1, bottom, 0xD9FFAA00);
            }

            prevTargetY = targetY;
            prevSubmittedY = submittedY;
        }
    }

    private static double clamp(double v, double scale)
    {
        return Math.max(-scale, Math.min(scale, v));
    }

    private int valueToPixelY(double value)
    {
        double clamped = Math.max(-GRAPH_AMPLITUDE_SCALE, Math.min(GRAPH_AMPLITUDE_SCALE, value));
        double normalized = (clamped + GRAPH_AMPLITUDE_SCALE) / (2 * GRAPH_AMPLITUDE_SCALE);
        return GRAPH_Y + GRAPH_HEIGHT - (int) (normalized * GRAPH_HEIGHT);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static class MutableWave
    {
        double amplitude;
        double frequency;
        double phase;
    }
}
