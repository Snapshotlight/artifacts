package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import artifacts.util.DamageSourceHelper;
import dev.architectury.event.EventResult;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class KnockbackAbility implements ArtifactAbility {

    public static EventResult onLivingHurt(LivingEntity entity, DamageSource damageSource, float amount) {
        LivingEntity attacker = DamageSourceHelper.getAttacker(damageSource);
        if (attacker != null) {
            double knockbackBonus = AbilityHelper.sumDouble(ModAbilities.KNOCKBACK, attacker, KnockbackAbility::getStrength, false);
            entity.knockback(knockbackBonus, Mth.sin((float) (attacker.getYRot() * (Math.PI / 180))), -Mth.cos((float) (attacker.getYRot() * (Math.PI / 180))));
        }
        return EventResult.pass();
    }

    public double getStrength() {
        return ModGameRules.POCKET_PISTON_KNOCKBACK_STRENGTH.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.KNOCKBACK;
    }

    @Override
    public boolean isNonCosmetic() {
        return getStrength() > 0;
    }
}
