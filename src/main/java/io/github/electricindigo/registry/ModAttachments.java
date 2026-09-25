package io.github.electricindigo.registry;

import io.github.electricindigo.ChronoDesync;
import io.github.electricindigo.research.ResearchData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments
{
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ChronoDesync.MODID);

    public static final Supplier<AttachmentType<ResearchData>> RESEARCH =
            ATTACHMENT_TYPES.register("research", () -> AttachmentType.builder(() -> ResearchData.EMPTY)
                    .serialize(ResearchData.MAP_CODEC)
                    .copyOnDeath()
                    .sync((holder, to) -> holder == to, ResearchData.STREAM_CODEC)
                    .build());

    public static void register(IEventBus modEventBus)
    {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
