package io.github.electricindigo;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(ChronoDesync.MODID)
public class ChronoDesync {
    public static final String MODID = "chronodesync";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ChronoDesync(IEventBus modEventBus, ModContainer modContainer) {
    }
}
