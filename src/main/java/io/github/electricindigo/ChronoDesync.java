package io.github.electricindigo;

import io.github.electricindigo.block.ModBlocks;
import io.github.electricindigo.block.researchdesk.ResearchDeskScreen;
import io.github.electricindigo.command.DebugPuzzleCommand;
import io.github.electricindigo.command.DebugWaveformCommand;
import io.github.electricindigo.datagen.*;
import io.github.electricindigo.item.ModItems;
import io.github.electricindigo.network.ModNetworking;
import io.github.electricindigo.registry.ModBlockEntities;
import io.github.electricindigo.registry.ModMenuTypes;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

import java.util.List;
import java.util.Set;

@Mod(ChronoDesync.MODID)
public class ChronoDesync
{
    public static final String MODID = "chronodesync";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ChronoDesync(IEventBus modEventBus, ModContainer modContainer)
    {
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
        modEventBus.addListener(this::onGatherDataClient);
        modEventBus.addListener(this::onRegisterMenuScreens);
        ModNetworking.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
    }

    public void onRegisterMenuScreens(RegisterMenuScreensEvent event)
    {
        event.register(ModMenuTypes.RESEARCH_DESK_MENU.get(), ResearchDeskScreen::new);
    }

    private void onRegisterCommands(RegisterCommandsEvent event)
    {
        DebugPuzzleCommand.register(event.getDispatcher());
        DebugWaveformCommand.register(event.getDispatcher());
    }

    private void onGatherDataClient(GatherDataEvent.Client event)
    {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        generator.addProvider(true, new ModLanguageProvider(output));
        generator.addProvider(true, new ModModelProvider(output));

        event.createBlockAndItemTags(ModBlockTagsProvider::new, ((output1, lookupProvider, contentsGetter) -> new ModItemTagsProvider(output1, lookupProvider)));

        event.createWorldRegistryObjects(new RegistrySetBuilder().add(Registries.VILLAGER_TRADE, ModVillagerTrades::bootstrap),
                Set.of(MODID)
        );
        event.createProvider((output2, lookupProvider) -> new ModVillagerTradeTagsProvider(output2, event.getWorldLookupProvider()));

        event.createReloadableRegistryObjects(new RegistrySetBuilder().add(Registries.LOOT_TABLE, new LootTableProvider(
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootSubProvider::new, LootContextParamSets.BLOCK))
        )));
    }
}
