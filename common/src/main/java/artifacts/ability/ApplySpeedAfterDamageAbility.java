package artifacts.ability;

import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.event.EventResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public record ApplySpeedAfterDamageAbility(IntegerValue speedLevel, IntegerValue speedDuration, IntegerValue cooldown) implements ArtifactAbility {

    public static final MapCodec<ApplySpeedAfterDamageAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IntegerValue.mobEffectLevelCodec().optionalFieldOf("level", IntegerValue.ONE).forGetter(ApplySpeedAfterDamageAbility::speedLevel),
            IntegerValue.durationSecondsCodec().fieldOf("duration").forGetter(ApplySpeedAfterDamageAbility::speedDuration),
            IntegerValue.durationSecondsCodec().fieldOf("cooldown").forGetter(ApplySpeedAfterDamageAbility::cooldown)
    ).apply(instance, ApplySpeedAfterDamageAbility::new));

    public static final StreamCodec<ByteBuf, ApplySpeedAfterDamageAbility> STREAM_CODEC = StreamCodec.composite(
            IntegerValue.streamCodec(),
            ApplySpeedAfterDamageAbility::speedLevel,
            IntegerValue.streamCodec(),
            ApplySpeedAfterDamageAbility::speedDuration,
            IntegerValue.streamCodec(),
            ApplySpeedAfterDamageAbility::cooldown,
            ApplySpeedAfterDamageAbility::new
    );

    @SuppressWarnings("unused")
    public static EventResult onLivingHurt(LivingEntity entity, DamageSource damageSource, float amount) {
        if (!entity.level().isClientSide() && amount >= 1) {
            if (AbilityHelper.hasAbilityActive(ModAbilities.APPLY_SPEED_AFTER_DAMAGE.get(), entity, true)) {
                int duration = AbilityHelper.maxInt(ModAbilities.APPLY_SPEED_AFTER_DAMAGE.get(), entity, ability -> ability.speedDuration().get(), true);
                int level = AbilityHelper.maxInt(ModAbilities.APPLY_SPEED_AFTER_DAMAGE.get(), entity, ability -> ability.speedLevel().get(), true);

                if (duration > 0 && level > 0) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, level - 1, false, false));
                    AbilityHelper.applyCooldowns(ModAbilities.APPLY_SPEED_AFTER_DAMAGE.get(), entity, ability -> ability.cooldown().get());
                }
            }
        }
        return EventResult.pass();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.APPLY_SPEED_AFTER_DAMAGE.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return speedDuration().get() > 0 && speedLevel().get() > 0;
    }
}
