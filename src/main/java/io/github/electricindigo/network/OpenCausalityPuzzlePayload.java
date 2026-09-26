package io.github.electricindigo.network;

import io.github.electricindigo.ChronoDynamics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record OpenCausalityPuzzlePayload(long seed, int difficulty) implements CustomPacketPayload
{
    public static final Type<OpenCausalityPuzzlePayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(ChronoDynamics.MODID, "open_causality_puzzle"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenCausalityPuzzlePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_LONG, OpenCausalityPuzzlePayload::seed,
                    ByteBufCodecs.VAR_INT, OpenCausalityPuzzlePayload::difficulty,
                    OpenCausalityPuzzlePayload::new
            );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
