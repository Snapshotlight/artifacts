package artifacts.ability;

import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public record ApplyFireResistanceAfterFireDamageAbility(IntegerValue fireResistanceDuration, IntegerValue cooldown) implements ArtifactAbility {

    public static final MapCodec<ApplyFireResistanceAfterFireDamageAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IntegerValue.durationSecondsCodec().fieldOf("duration").forGetter(ApplyFireResistanceAfterFireDamageAbility::fireResistanceDuration),
            IntegerValue.durationSecondsCodec().fieldOf("cooldown").forGetter(ApplyFireResistanceAfterFireDamageAbility::cooldown)
    ).apply(instance, ApplyFireResistanceAfterFireDamageAbility::new));

    public static final StreamCodec<ByteBuf, ApplyFireResistanceAfterFireDamageAbility> STREAM_CODEC = StreamCodec.composite(
            IntegerValue.streamCodec(),
            ApplyFireResistanceAfterFireDamageAbility::fireResistanceDuration,
            IntegerValue.streamCodec(),
            ApplyFireResistanceAfterFireDamageAbility::cooldown,
            ApplyFireResistanceAfterFireDamageAbility::new
    );

    public static void onLivingDamage(LivingEntity entity, DamageSource damageSource, float amount) {
        if (!entity.level().isClientSide
                && amount >= 1
                && damageSource.is(DamageTypeTags.IS_FIRE)
                && entity instanceof Player player
        ) {
            AbilityHelper.forEach(ModAbilities.APPLY_FIRE_RESISTANCE_AFTER_FIRE_DAMAGE.get(), entity, (ability, stack) -> {
                entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, ability.fireResistanceDuration().get(), 0, false, false, true));
                if (ability.cooldown().get() > 0) {
                    player.getCooldowns().addCooldown(stack.getItem(), ability.cooldown().get());
                }
            }, true);
        }
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.APPLY_FIRE_RESISTANCE_AFTER_FIRE_DAMAGE.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return fireResistanceDuration().get() > 0;
    }
}
