package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import artifacts.util.DamageSourceHelper;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class AbsorbDamageAbility implements ArtifactAbility {

    public static void onLivingDamage(LivingEntity entity, DamageSource damageSource, float amount) {
        LivingEntity attacker = DamageSourceHelper.getAttacker(damageSource);
        if (attacker != null && DamageSourceHelper.isMeleeAttack(damageSource)) {
            AbilityHelper.forEach(ModAbilities.ABSORB_DAMAGE.get(), entity, ability -> {
                int maxHealthAbsorbed = ability.getMaxHealingPerHit();
                double absorptionRatio = ability.getAbsorptionRatio();
                double absorptionProbability = ability.getAbsorptionChance();

                float damageDealt = Math.min(amount, entity.getHealth());
                float damageAbsorbed = Math.min(maxHealthAbsorbed, (float) absorptionRatio * damageDealt);

                if (damageAbsorbed > 0 && entity.getRandom().nextFloat() < absorptionProbability) {
                    attacker.heal(damageAbsorbed);
                }
            });
        }
    }

    public double getAbsorptionRatio() {
        return ModGameRules.VAMPIRIC_GLOVE_ABSORPTION_RATIO.get();
    }

    public int getMaxHealingPerHit() {
        return ModGameRules.VAMPIRIC_GLOVE_MAX_HEALING_PER_HIT.get();
    }

    public double getAbsorptionChance() {
        return ModGameRules.VAMPIRIC_GLOVE_ABSORPTION_CHANCE.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.ABSORB_DAMAGE.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return getAbsorptionChance() > 0 && getAbsorptionRatio() > 0 && getMaxHealingPerHit() > 0;
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        if (getAbsorptionChance() + 1e-10 >= 1 ) {
            tooltip.add(tooltipLine("constant"));
        } else {
            tooltip.add(tooltipLine("chance"));
        }
    }
}
