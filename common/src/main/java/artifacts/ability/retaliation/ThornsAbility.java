package artifacts.ability.retaliation;

import artifacts.ability.value.DoubleValue;
import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;

public class ThornsAbility extends RetaliationAbility {

    public static final MapCodec<ThornsAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            codecStart(instance, ModGameRules.THORN_PENDANT_STRIKE_CHANCE, ModGameRules.THORN_PENDANT_COOLDOWN)
                    .and(IntegerValue.field("min_damage", ModGameRules.THORN_PENDANT_MIN_DAMAGE).forGetter(ThornsAbility::minDamage))
                    .and(IntegerValue.field("max_damage", ModGameRules.THORN_PENDANT_MAX_DAMAGE).forGetter(ThornsAbility::maxDamage))
                    .apply(instance, ThornsAbility::new)
    );

    public static final StreamCodec<ByteBuf, ThornsAbility> STREAM_CODEC = StreamCodec.composite(
            DoubleValue.defaultStreamCodec(ModGameRules.THORN_PENDANT_STRIKE_CHANCE),
            ThornsAbility::strikeChance,
            IntegerValue.defaultStreamCodec(ModGameRules.THORN_PENDANT_COOLDOWN),
            ThornsAbility::cooldown,
            IntegerValue.defaultStreamCodec(ModGameRules.THORN_PENDANT_MIN_DAMAGE),
            ThornsAbility::minDamage,
            IntegerValue.defaultStreamCodec(ModGameRules.THORN_PENDANT_MAX_DAMAGE),
            ThornsAbility::maxDamage,
            ThornsAbility::new
    );

    private final IntegerValue minDamage;
    private final IntegerValue maxDamage;

    public ThornsAbility(DoubleValue strikeChance, IntegerValue cooldown, IntegerValue minDamage, IntegerValue maxDamage) {
        super(strikeChance, cooldown);
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
    }

    public IntegerValue minDamage() {
        return minDamage;
    }

    public IntegerValue maxDamage() {
        return maxDamage;
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.THORNS.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return super.isNonCosmetic() && maxDamage().get() > 0;
    }

    @Override
    protected void applyEffect(LivingEntity target, LivingEntity attacker) {
        if (attacker.attackable()) {
            int minDamage = minDamage().get();
            int maxDamage = maxDamage().get();
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
