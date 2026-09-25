package io.github.electricindigo.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record ResearchData(Set<String> unlocked)
{
    public static final ResearchData EMPTY = new ResearchData(Set.of());

    public static final MapCodec<ResearchData> MAP_CODEC = Codec.STRING.listOf().fieldOf("unlocked")
            .xmap(list -> new ResearchData(Set.copyOf(list)), data -> List.copyOf(data.unlocked()));

    public static final StreamCodec<ByteBuf, ResearchData> STREAM_CODEC =
            ByteBufCodecs.collection(HashSet::new, ByteBufCodecs.STRING_UTF8)
                    .map(set -> new ResearchData(Set.copyOf(set)), data -> new HashSet<>(data.unlocked()));

    public boolean has(String id)
    {
        return unlocked.contains(id);
    }

    public ResearchData with(String id)
    {
        Set<String> copy = new HashSet<>(unlocked);
        copy.add(id);
        return new ResearchData(Set.copyOf(copy));
    }
}
