package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class RemoveBadEffectsAbility implements ArtifactAbility {

    private int getMaxEffectDuration() {
        return ModGameRules.ANTIDOTE_VESSEL_MAX_EFFECT_DURATION.get();
    }
    @Override
    public Type<?> getType() {
        return ModAbilities.REMOVE_BAD_EFFECTS;
    }

    @Override
    public boolean isNonCosmetic() {
        return ModGameRules.ANTIDOTE_VESSEL_ENABLED.get();
    }

    @Override
    public void wornTick(LivingEntity entity, boolean isOnCooldown, boolean isActive) {
        if (!isActive) {
            return;
        }
        Map<MobEffect, MobEffectInstance> effects = new HashMap<>();

        int maxEffectDuration = getMaxEffectDuration();
        entity.getActiveEffectsMap().forEach((effect, instance) -> {
            if (ModTags.isInTag(effect, ModTags.ANTIDOTE_VESSEL_CANCELLABLE) && instance.getDuration() > maxEffectDuration) {
                effects.put(effect, instance);
            }
        });

        effects.forEach((effect, instance) -> {
            entity.removeEffectNoUpdate(effect);
            if (maxEffectDuration > 0) {
                entity.addEffect(new MobEffectInstance(effect, maxEffectDuration, instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon()));
            }
        });
    }
}
