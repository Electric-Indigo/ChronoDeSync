package io.github.electricindigo.datagen;

import io.github.electricindigo.ChronoDynamics;
import io.github.electricindigo.block.ModBlocks;
import io.github.electricindigo.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider
{

    public ModLanguageProvider(PackOutput output) {
        super(output, ChronoDynamics.MODID, "en_us");
    }

    @Override
    protected void addTranslations()
    {
        addItem(ModItems.EFD_ITEM, "Electronics for Dummies");
        addItem(ModItems.COMPUTER_UPGRADE, "Computer Upgrade");

        addBlock(ModBlocks.RESEARCH_DESK, "Research Desk");

        add("creativetab.chronodynamics.chronodynamics_tab", "Chrono Dynamics");
    }
}
