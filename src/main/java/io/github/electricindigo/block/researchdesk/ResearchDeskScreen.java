package io.github.electricindigo.block.researchdesk;

import io.github.electricindigo.ChronoDesync;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.EnumMap;
import java.util.Map;

public class ResearchDeskScreen extends AbstractContainerScreen<ResearchDeskMenu>
{
    private static final Identifier TAB_ICONS = Identifier.fromNamespaceAndPath(ChronoDesync.MODID, "textures/gui/tab_icons.png");

    private enum Tab
    {
        DESK(Identifier.fromNamespaceAndPath(ChronoDesync.MODID, "textures/gui/desk_gui.png"), 0, 0),
        COMPUTER(Identifier.fromNamespaceAndPath(ChronoDesync.MODID, "textures/gui/computer_gui.png"), 22, 0),
        TEST(Identifier.fromNamespaceAndPath(ChronoDesync.MODID, "textures/gui/test_gui.png"), 44, 0);

        final Identifier background;
        final int iconU;
        final int iconV;

        Tab(Identifier background, int iconU, int iconV)
        {
            this.background = background;
            this.iconU = iconU;
            this.iconV = iconV;
        }
    }

    private Tab activeTab = Tab.DESK;
    private final Map<Tab, TabButton> tabButtons = new EnumMap<>(Tab.class);

    public ResearchDeskScreen(ResearchDeskMenu menu, Inventory inventory, Component title)
    {
        super(menu, inventory, title, 230, 219);
    }

    @Override
    protected void init()
    {
        System.out.println("init called");
        super.init();
        tabButtons.clear();

        int index = 0;
        for (Tab tab : Tab.values())
        {
            TabButton button = new TabButton(leftPos + 230, topPos + 8 + index * 24, 22, 22,
                    TAB_ICONS, tab.iconU, tab.iconV, 256, 256,
                    () -> setTab(tab));
            addRenderableWidget(button);
            tabButtons.put(tab, button);
            index++;
        }

        updateTabButtonVisibility();
    }

    private void setTab(Tab tab)
    {
        this.activeTab = tab;
        updateTabButtonVisibility();
    }

    private void updateTabButtonVisibility()
    {
        tabButtons.forEach((tab, button) -> button.visible = tab != activeTab);
    }


    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a)
    {
        graphics.blit(RenderPipelines.GUI_TEXTURED, activeTab.background, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }
}
