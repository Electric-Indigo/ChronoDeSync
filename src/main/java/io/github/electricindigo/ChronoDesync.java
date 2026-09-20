package io.github.electricindigo;

import io.github.electricindigo.command.DebugPuzzleCommand;
import io.github.electricindigo.command.DebugWaveformCommand;
import io.github.electricindigo.network.ModNetworking;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(ChronoDesync.MODID)
public class ChronoDesync
{
    public static final String MODID = "chronodesync";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ChronoDesync(IEventBus modEventBus, ModContainer modContainer)
    {
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
        ModNetworking.register(modEventBus);
    }

    private void onRegisterCommands(RegisterCommandsEvent event)
    {
        DebugPuzzleCommand.register(event.getDispatcher());
        DebugWaveformCommand.register(event.getDispatcher());
    }
}
