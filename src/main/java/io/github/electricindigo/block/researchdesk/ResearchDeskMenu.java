package io.github.electricindigo.block.researchdesk;

import io.github.electricindigo.registry.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class ResearchDeskMenu extends AbstractContainerMenu
{
    public ResearchDeskMenu(int containerId, Inventory playerInventory)
    {
        super(ModMenuTypes.RESEARCH_DESK_MENU.get(), containerId);
        this.addStandardInventorySlots(playerInventory, 36, 137);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i)
    {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
