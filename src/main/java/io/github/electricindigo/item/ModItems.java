package io.github.electricindigo.item;

import io.github.electricindigo.ChronoDesync;
import io.github.electricindigo.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems
{
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ChronoDesync.MODID);


    public static final DeferredItem<Item> EFD_ITEM = ITEMS.registerSimpleItem("e_for_d",
            properties -> properties.stacksTo(1));

    public static final DeferredItem<Item> COMPUTER_UPGRADE = ITEMS.registerSimpleItem("computer_upgrade",
            properties -> properties.stacksTo(1));

    public static final DeferredItem<BlockItem> RESEARCH_DESK_ITEM = ITEMS.registerSimpleBlockItem(ModBlocks.RESEARCH_DESK);


    public static void register(IEventBus modEventBus)
    {
        ITEMS.register(modEventBus);
    }
}
