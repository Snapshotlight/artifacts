package artifacts.ability.retaliation;

import artifacts.ability.ArtifactAbility;
import artifacts.util.AbilityHelper;
import artifacts.util.DamageSourceHelper;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public abstract class RetaliationAbility implements ArtifactAbility {

    private final Supplier<Double> strikeChance;
    private final Supplier<Integer> cooldown;

    public RetaliationAbility(Supplier<Double> strikeChance, Supplier<Integer> cooldown) {
        this.strikeChance = strikeChance;
        this.cooldown = cooldown;
    }

    public double getStrikeChance() {
        return strikeChance.get();
    }

    public int getCooldown() {
        return cooldown.get();
    }

    public void onLivingHurt(LivingEntity entity, ItemStack stack, DamageSource damageSource, float amount) {
        LivingEntity attacker = DamageSourceHelper.getAttacker(damageSource);
        if (amount >= 1 && attacker != null
                        && AbilityHelper.hasAbility(getType(), entity)
                        && entity.getRandom().nextDouble() < getStrikeChance()
        ) {
            applyEffect(entity, attacker);
            if (entity instanceof Player player && getCooldown() > 0) {
                player.getCooldowns().addCooldown(stack.getItem(), getCooldown());
            }
        }
    }

    protected abstract void applyEffect(LivingEntity target, LivingEntity attacker);

    @Override
    public boolean isNonCosmetic() {
        return getStrikeChance() > 0;
    }
}
