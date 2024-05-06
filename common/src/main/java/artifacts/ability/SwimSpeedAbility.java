package artifacts.ability;

import artifacts.ability.value.DoubleValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record SwimSpeedAbility(DoubleValue swimSpeedBonus) implements ArtifactAbility {

    public static final MapCodec<SwimSpeedAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DoubleValue.field(ModGameRules.FLIPPERS_SWIM_SPEED_BONUS).forGetter(SwimSpeedAbility::swimSpeedBonus)
    ).apply(instance, SwimSpeedAbility::new));

    public static final StreamCodec<ByteBuf, SwimSpeedAbility> STREAM_CODEC = StreamCodec.composite(
            DoubleValue.defaultStreamCodec(ModGameRules.FLIPPERS_SWIM_SPEED_BONUS),
            SwimSpeedAbility::swimSpeedBonus,
            SwimSpeedAbility::new
    );

    public static ArtifactAbility createDefaultInstance() {
        return ArtifactAbility.createDefaultInstance(CODEC);
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.SWIM_SPEED.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return !swimSpeedBonus().fuzzyEquals(0);
    }
}
