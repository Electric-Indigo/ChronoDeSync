package io.github.electricindigo.datagen;

import io.github.electricindigo.ChronoDesync;
import io.github.electricindigo.block.ModBlocks;
import io.github.electricindigo.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider
{

    public ModLanguageProvider(PackOutput output) {
        super(output, ChronoDesync.MODID, "en_us");
    }

    @Override
    protected void addTranslations()
    {
        addItem(ModItems.EFD_ITEM, "Electronics for Dummies");
        addItem(ModItems.COMPUTER_UPGRADE, "Computer Upgrade");

        addBlock(ModBlocks.RESEARCH_DESK, "Research Desk");

        add("creativetab.chronodesync.chronodesync_tab", "ChronoDesync");
    }
}
