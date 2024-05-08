package artifacts.ability;

import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record BonusInvincibilityTicksAbility(IntegerValue bonusTicks, IntegerValue cooldown) implements ArtifactAbility {

    public static final MapCodec<BonusInvincibilityTicksAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IntegerValue.field("amount", ModGameRules.CROSS_NECKLACE_BONUS_INVINCIBILITY_TICKS).forGetter(BonusInvincibilityTicksAbility::bonusTicks),
            IntegerValue.field("cooldown", ModGameRules.CROSS_NECKLACE_COOLDOWN).forGetter(BonusInvincibilityTicksAbility::cooldown)
    ).apply(instance, BonusInvincibilityTicksAbility::new));

    public static final StreamCodec<ByteBuf, BonusInvincibilityTicksAbility> STREAM_CODEC = StreamCodec.composite(
            IntegerValue.defaultStreamCodec(ModGameRules.CROSS_NECKLACE_BONUS_INVINCIBILITY_TICKS),
            BonusInvincibilityTicksAbility::bonusTicks,
            IntegerValue.defaultStreamCodec(ModGameRules.CROSS_NECKLACE_COOLDOWN),
            BonusInvincibilityTicksAbility::cooldown,
            BonusInvincibilityTicksAbility::new
    );

    @Override
    public Type<?> getType() {
        return ModAbilities.BONUS_INVINCIBILITY_TICKS.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return bonusTicks().get() > 0;
    }
}
