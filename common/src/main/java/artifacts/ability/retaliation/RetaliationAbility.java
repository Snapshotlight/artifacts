package artifacts.ability.retaliation;

import artifacts.ability.ArtifactAbility;
import artifacts.ability.value.DoubleValue;
import artifacts.ability.value.IntegerValue;
import artifacts.util.AbilityHelper;
import artifacts.util.DamageSourceHelper;
import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class RetaliationAbility implements ArtifactAbility {

    private final DoubleValue strikeChance;
    private final IntegerValue cooldown;

    protected static <T extends RetaliationAbility> Products.P2<RecordCodecBuilder.Mu<T>, DoubleValue, IntegerValue> codecStart(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(
                DoubleValue.percentage().fieldOf("strike_chance").forGetter(RetaliationAbility::strikeChance),
                IntegerValue.durationSecondsCodec().optionalFieldOf("cooldown", IntegerValue.ZERO).forGetter(RetaliationAbility::cooldown)
        );
    }

    public RetaliationAbility(DoubleValue strikeChance, IntegerValue cooldown) {
        this.strikeChance = strikeChance;
        this.cooldown = cooldown;
    }

    public DoubleValue strikeChance() {
        return strikeChance;
    }

    public IntegerValue cooldown() {
        return cooldown;
    }

    public void onLivingHurt(LivingEntity entity, ItemStack stack, DamageSource damageSource, float amount) {
        LivingEntity attacker = DamageSourceHelper.getAttacker(damageSource);
        if (amount >= 1 && attacker != null
                        && AbilityHelper.hasAbilityActive(getType(), entity)
                        && entity.getRandom().nextDouble() < strikeChance().get()
        ) {
            applyEffect(entity, attacker);
            if (entity instanceof Player player && cooldown().get() > 0) {
                player.getCooldowns().addCooldown(stack.getItem(), cooldown().get());
            }
        }
    }

    protected abstract void applyEffect(LivingEntity target, LivingEntity attacker);

    @Override
    public boolean isNonCosmetic() {
        return !strikeChance().fuzzyEquals(0);
    }
}
