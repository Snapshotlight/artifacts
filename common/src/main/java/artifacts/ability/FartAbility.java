package artifacts.ability;

import artifacts.ability.value.DoubleValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record FartAbility(DoubleValue fartChance) implements ArtifactAbility {

    public static final MapCodec<FartAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DoubleValue.field("chance", ModGameRules.WHOOPEE_CUSHION_FART_CHANCE).forGetter(FartAbility::fartChance)
    ).apply(instance, FartAbility::new));

    public static final StreamCodec<ByteBuf, FartAbility> STREAM_CODEC = StreamCodec.composite(
            DoubleValue.defaultStreamCodec(ModGameRules.WHOOPEE_CUSHION_FART_CHANCE),
            FartAbility::fartChance,
            FartAbility::new
    );

    @Override
    public Type<?> getType() {
        return ModAbilities.FART.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return true;
    }
}
