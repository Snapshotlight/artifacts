package artifacts.ability;

import artifacts.ability.value.DoubleValue;
import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import artifacts.util.DamageSourceHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public record AbsorbDamageAbility(DoubleValue absorptionRatio, IntegerValue maxHealingPerHit, DoubleValue absorptionChance) implements ArtifactAbility {

    public static final MapCodec<AbsorbDamageAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DoubleValue.codec(ModGameRules.VAMPIRIC_GLOVE_ABSORPTION_RATIO).forGetter(AbsorbDamageAbility::absorptionRatio),
            IntegerValue.codec(ModGameRules.VAMPIRIC_GLOVE_MAX_HEALING_PER_HIT).forGetter(AbsorbDamageAbility::maxHealingPerHit),
            DoubleValue.codec(ModGameRules.VAMPIRIC_GLOVE_ABSORPTION_CHANCE).forGetter(AbsorbDamageAbility::absorptionChance)
    ).apply(instance, AbsorbDamageAbility::new));

    public static void onLivingDamage(LivingEntity entity, DamageSource damageSource, float amount) {
        LivingEntity attacker = DamageSourceHelper.getAttacker(damageSource);
        if (attacker != null && DamageSourceHelper.isMeleeAttack(damageSource)) {
            AbilityHelper.forEach(ModAbilities.ABSORB_DAMAGE.get(), entity, ability -> {
                int maxHealthAbsorbed = ability.maxHealingPerHit().get();
                double absorptionRatio = ability.absorptionRatio().get();
                double absorptionProbability = ability.absorptionChance().get();

                float damageDealt = Math.min(amount, entity.getHealth());
                float damageAbsorbed = Math.min(maxHealthAbsorbed, (float) absorptionRatio * damageDealt);

                if (damageAbsorbed > 0 && entity.getRandom().nextFloat() < absorptionProbability) {
                    attacker.heal(damageAbsorbed);
                }
            });
        }
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.ABSORB_DAMAGE.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return absorptionChance().get() > 0 && absorptionRatio().get() > 0 && maxHealingPerHit().get() > 0;
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        if (absorptionChance().get() + 1e-10 >= 1 ) {
            tooltip.add(tooltipLine("constant"));
        } else {
            tooltip.add(tooltipLine("chance"));
        }
    }
}
