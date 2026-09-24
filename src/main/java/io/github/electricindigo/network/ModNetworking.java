package io.github.electricindigo.network;

import io.github.electricindigo.ChronoDesync;
import io.github.electricindigo.client.PuzzleClientHandler;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModNetworking
{
    private ModNetworking(){}

    public static void register(IEventBus modEventBus)
    {
        modEventBus.addListener(ModNetworking::onRegisterPayloadHandlers);
    }

    private static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event)
    {
        PayloadRegistrar registrar = event.registrar(ChronoDesync.MODID).versioned("1.0.0");

        registrar.playToClient(
                OpenCausalityPuzzlePayload.TYPE,
                OpenCausalityPuzzlePayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(()->
                        PuzzleClientHandler.openCausalityPuzzle(payload.seed(), payload.difficulty())));

        registrar.playToClient(
                OpenWaveformPuzzlePayload.TYPE,
                OpenWaveformPuzzlePayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(()->
                        PuzzleClientHandler.openWaveformPuzzle(payload.seed(), payload.difficulty())));
    }
}
