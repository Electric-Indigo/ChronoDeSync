package io.github.electricindigo.network;

import io.github.electricindigo.ChronoDynamics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record OpenWaveformPuzzlePayload(long seed, int difficulty) implements CustomPacketPayload
{
    public static final Type<OpenWaveformPuzzlePayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(ChronoDynamics.MODID, "open_waveform_puzzle"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenWaveformPuzzlePayload> STREAM_CODEC =
            StreamCodec.composite(
                    net.minecraft.network.codec.ByteBufCodecs.VAR_LONG, OpenWaveformPuzzlePayload::seed,
                    net.minecraft.network.codec.ByteBufCodecs.VAR_INT, OpenWaveformPuzzlePayload::difficulty,
                    OpenWaveformPuzzlePayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
