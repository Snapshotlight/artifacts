package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import artifacts.util.DamageSourceHelper;
import dev.architectury.event.EventResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class FireAspectAbility implements ArtifactAbility {

    public static EventResult onLivingHurt(LivingEntity entity, DamageSource damageSource, float amount) {
        LivingEntity attacker = DamageSourceHelper.getAttacker(damageSource);
        if (attacker != null && DamageSourceHelper.isMeleeAttack(damageSource) && !entity.fireImmune()) {
            int durationTicks = AbilityHelper.maxInt(ModAbilities.FIRE_ASPECT.get(), attacker, FireAspectAbility::fireDuration, false);
            entity.setSecondsOnFire(durationTicks / 20);
        }
        return EventResult.pass();
    }

    public int fireDuration() {
        return ModGameRules.FIRE_GAUNTLET_FIRE_DURATION.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.FIRE_ASPECT.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return fireDuration() > 0;
    }
}
