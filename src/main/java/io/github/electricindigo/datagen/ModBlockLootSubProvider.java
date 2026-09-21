package io.github.electricindigo.datagen;

import io.github.electricindigo.block.ModBlocks;
import io.github.electricindigo.block.researchdesk.DeskPart;
import io.github.electricindigo.block.researchdesk.ResearchDeskBlock;

import net.minecraft.data.loot.BlockLootSubProvider;

import net.minecraft.data.loot.LootTableSubProvider;

import net.minecraft.world.flag.FeatureFlags;

import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootSubProvider extends BlockLootSubProvider
{

    public ModBlockLootSubProvider(LootTableSubProvider.Context context)
    {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, context);
    }

    @Override
    protected Iterable<Block> getKnownBlocks()
    {
        return ModBlocks.BLOCKS.getEntries()
                .stream()
                .map(e -> (Block) e.value())
                .toList();
    }

    @Override
    protected void generate()
    {
        this.add(ModBlocks.RESEARCH_DESK.get(),
                createSinglePropConditionTable(ModBlocks.RESEARCH_DESK.get(), ResearchDeskBlock.PART, DeskPart.PRIMARY)
               );
    }
}
