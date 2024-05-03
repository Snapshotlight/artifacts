package artifacts.ability.retaliation;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class SetAttackersOnFireAbility extends RetaliationAbility {

    public SetAttackersOnFireAbility() {
        super(ModGameRules.FLAME_PENDANT_STRIKE_CHANCE, ModGameRules.FLAME_PENDANT_COOLDOWN);
    }

    public int getFireDuration() {
        return ModGameRules.FLAME_PENDANT_FIRE_DURATION.get();
    }

    public boolean grantsFireResistance() {
        return ModGameRules.FLAME_PENDANT_DO_GRANT_FIRE_RESISTANCE.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.SET_ATTACKERS_ON_FIRE;
    }

    @Override
    public boolean isNonCosmetic() {
        return super.isNonCosmetic() && getFireDuration() > 0;
    }

    @Override
    protected void applyEffect(LivingEntity target, LivingEntity attacker) {
        if (!attacker.fireImmune() && attacker.attackable() && getFireDuration() > 0) {
            if (grantsFireResistance()) {
                target.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, getFireDuration(), 0, false, false, true));
            }
            attacker.setSecondsOnFire(getFireDuration() / 20);
        }
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        tooltip.add(tooltipLine("strike_chance"));
        if (ModGameRules.FLAME_PENDANT_DO_GRANT_FIRE_RESISTANCE.get()) {
            tooltip.add(tooltipLine("fire_resistance"));
        }
    }
}
