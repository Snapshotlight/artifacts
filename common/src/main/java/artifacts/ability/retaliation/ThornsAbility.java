package artifacts.ability.retaliation;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import net.minecraft.world.entity.LivingEntity;

public class ThornsAbility extends RetaliationAbility {

    public ThornsAbility() {
        super(ModGameRules.THORN_PENDANT_STRIKE_CHANCE, ModGameRules.THORN_PENDANT_COOLDOWN);
    }

    public int getMinDamage() {
        return ModGameRules.THORN_PENDANT_MIN_DAMAGE.get();
    }

    public int getMaxDamage() {
        return ModGameRules.THORN_PENDANT_MAX_DAMAGE.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.THORNS;
    }

    @Override
    public boolean isNonCosmetic() {
        return super.isNonCosmetic() && getMaxDamage() > 0;
    }

    @Override
    protected void applyEffect(LivingEntity target, LivingEntity attacker) {
        if (attacker.attackable()) {
            int minDamage = getMinDamage();
            int maxDamage = getMaxDamage();
            if (maxDamage < minDamage) {
                minDamage = maxDamage;
            }
            int damage = minDamage + target.getRandom().nextInt(maxDamage - minDamage + 1);
            if (damage > 0) {
                attacker.hurt(target.damageSources().thorns(target), damage);
            }
        }
    }
}
