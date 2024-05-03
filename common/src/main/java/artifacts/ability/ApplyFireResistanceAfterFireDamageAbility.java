package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ApplyFireResistanceAfterFireDamageAbility implements ArtifactAbility {

    public static void onLivingDamage(LivingEntity entity, DamageSource damageSource, float amount) {
        if (!entity.level().isClientSide
                && amount >= 1
                && damageSource.is(DamageTypeTags.IS_FIRE)
                && entity instanceof Player player
        ) {
            AbilityHelper.forEach(ModAbilities.APPLY_FIRE_RESISTANCE_AFTER_FIRE_DAMAGE, entity, (ability, stack) -> {
                entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, ability.getFireResistanceDuration(), 0, false, false, true));
                if (ability.getCooldown() > 0) {
                    player.getCooldowns().addCooldown(stack.getItem(), ability.getCooldown());
                }
            }, true);
        }
    }

    public int getFireResistanceDuration() {
        return ModGameRules.OBSIDIAN_SKULL_FIRE_RESISTANCE_DURATION.get();
    }

    public int getCooldown() {
        return ModGameRules.OBSIDIAN_SKULL_FIRE_RESISTANCE_COOLDOWN.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.APPLY_FIRE_RESISTANCE_AFTER_FIRE_DAMAGE;
    }

    @Override
    public boolean isNonCosmetic() {
        return getFireResistanceDuration() > 0;
    }
}
