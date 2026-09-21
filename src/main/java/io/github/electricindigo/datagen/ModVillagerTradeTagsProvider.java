package io.github.electricindigo.datagen;

import io.github.electricindigo.ChronoDesync;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.VillagerTradeTags;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.concurrent.CompletableFuture;

public class ModVillagerTradeTagsProvider extends TagsProvider<VillagerTrade>
{

    public ModVillagerTradeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.VILLAGER_TRADE, lookupProvider, ChronoDesync.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(VillagerTradeTags.LIBRARIAN_LEVEL_2)
                .add(ModVillagerTrades.LIBRARIAN_EFD);
    }
}
