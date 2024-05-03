package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import dev.architectury.event.EventResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class ApplySpeedAfterDamageAbility implements ArtifactAbility {

    public static EventResult onLivingHurt(LivingEntity entity, DamageSource damageSource, float amount) {
        if (!entity.level().isClientSide() && amount >= 1) {
            if (AbilityHelper.hasAbility(ModAbilities.APPLY_SPEED_AFTER_DAMAGE, entity, true)) {
                int duration = AbilityHelper.maxInt(ModAbilities.APPLY_SPEED_AFTER_DAMAGE, entity, ApplySpeedAfterDamageAbility::getSpeedDuration, true);
                int level = AbilityHelper.maxInt(ModAbilities.APPLY_SPEED_AFTER_DAMAGE, entity, ApplySpeedAfterDamageAbility::getSpeedLevel, true);

                if (duration > 0 && level > 0) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, level - 1, false, false));
                    AbilityHelper.applyCooldowns(ModAbilities.APPLY_SPEED_AFTER_DAMAGE, entity, ApplySpeedAfterDamageAbility::getCooldown);
                }
            }
        }
        return EventResult.pass();
    }

    public int getSpeedLevel() {
        return ModGameRules.PANIC_NECKLACE_SPEED_LEVEL.get();
    }

    public int getSpeedDuration() {
        return ModGameRules.PANIC_NECKLACE_SPEED_DURATION.get();
    }

    public int getCooldown() {
        return ModGameRules.PANIC_NECKLACE_COOLDOWN.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.APPLY_SPEED_AFTER_DAMAGE;
    }

    @Override
    public boolean isNonCosmetic() {
        return getSpeedDuration() > 0 && getSpeedLevel() > 0;
    }
}
