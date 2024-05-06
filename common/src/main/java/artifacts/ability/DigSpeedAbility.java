package artifacts.ability;

import artifacts.ability.value.DoubleValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public record DigSpeedAbility(DoubleValue speedBonus) implements ArtifactAbility {

    public static final MapCodec<DigSpeedAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DoubleValue.field(ModGameRules.DIGGING_CLAWS_DIG_SPEED_BONUS).forGetter(DigSpeedAbility::speedBonus)
    ).apply(instance, DigSpeedAbility::new));

    public static final StreamCodec<ByteBuf, DigSpeedAbility> STREAM_CODEC = StreamCodec.composite(
            DoubleValue.defaultStreamCodec(ModGameRules.DIGGING_CLAWS_DIG_SPEED_BONUS),
            DigSpeedAbility::speedBonus,
            DigSpeedAbility::new
    );

    public static float getSpeedBonus(Player player, BlockState state) {
        if (player.hasCorrectToolForDrops(state)) {
            return (float) AbilityHelper.sumDouble(ModAbilities.DIG_SPEED.get(), player, ability -> ability.speedBonus().get(), false);
        }
        return 0;
    }

    public static ArtifactAbility createDefaultInstance() {
        return ArtifactAbility.createDefaultInstance(CODEC);
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.DIG_SPEED.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return !speedBonus().fuzzyEquals(0);
    }
}
