package io.github.electricindigo.client;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.DoubleConsumer;

public class LabeledSlider extends AbstractSliderButton
{
    private final double min;
    private final double max;
    private final String label;
    private final DoubleConsumer onChange;

    public LabeledSlider(int x, int y, int width, int height, String label, double min, double max, double initial, DoubleConsumer onChange)
    {
        super(x, y, width, height, Component.literal(""), normalize(initial, min, max));
        this.min = min;
        this.max = max;
        this.label = label;
        this.onChange = onChange;
        updateMessage();
    }

    private static double normalize(double actual, double min, double max)
    {
        return (actual - min) / (max - min);
    }

    public double actualValue()
    {
        return min + value * (max - min);
    }

    @Override
    protected void updateMessage()
    {
        setMessage(Component.literal(String.format("%s: %.2f", label, actualValue())));
    }

    @Override
    protected void applyValue()
    {
        if (onChange != null)
        {
            onChange.accept(actualValue());
        }
    }
}
