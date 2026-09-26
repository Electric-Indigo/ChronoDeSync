package io.github.electricindigo.network;

import io.github.electricindigo.ChronoDynamics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record UnlockResearchPayload(String nodeId) implements CustomPacketPayload
{
    public static final Type<UnlockResearchPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(ChronoDynamics.MODID, "unlock_research"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UnlockResearchPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, UnlockResearchPayload::nodeId,
                    UnlockResearchPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
