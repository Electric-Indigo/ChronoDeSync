package io.github.electricindigo.block.researchdesk;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class TabButton extends AbstractButton
{
    private final Identifier texture;
    private final int u;
    private final int v;
    private final int textureWidth;
    private final int textureHeight;
    private final Runnable onClick;

    public TabButton(int x, int y, int width, int height, Identifier texture, int u, int v, int textureWidth, int textureHeight, Runnable onClick) {
        super(x, y, width, height, CommonComponents.EMPTY);
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.onClick = onClick;
    }

    @Override
    public void onPress(InputWithModifiers inputWithModifiers)
    {
        onClick.run();
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor guiGraphicsExtractor, int i, int i1, float partialTick)
    {
        guiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, texture, getX(), getY(), (float) u, (float) v, width, height, textureWidth, textureHeight);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
    {
        defaultButtonNarrationText(narrationElementOutput);
    }
}
