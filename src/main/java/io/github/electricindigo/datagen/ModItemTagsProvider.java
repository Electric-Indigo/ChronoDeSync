package io.github.electricindigo.datagen;

import io.github.electricindigo.ChronoDynamics;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider
{

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ChronoDynamics.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {

    }
}
