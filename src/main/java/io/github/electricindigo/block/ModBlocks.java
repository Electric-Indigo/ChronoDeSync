package io.github.electricindigo.block;

import io.github.electricindigo.ChronoDesync;
import io.github.electricindigo.block.researchdesk.ResearchDeskBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks
{
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ChronoDesync.MODID);

    public static final DeferredBlock<Block> RESEARCH_DESK = BLOCKS.registerBlock(
            "research_desk",
            ResearchDeskBlock::new,
            props -> props.noOcclusion().strength(2.5f).requiresCorrectToolForDrops()
    );

    public static void register(IEventBus modEventBus)
    {
        BLOCKS.register(modEventBus);
    }
}
