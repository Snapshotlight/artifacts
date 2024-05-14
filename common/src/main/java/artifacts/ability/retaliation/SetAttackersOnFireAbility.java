package artifacts.ability.retaliation;

import artifacts.ability.value.BooleanValue;
import artifacts.ability.value.DoubleValue;
import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class SetAttackersOnFireAbility extends RetaliationAbility {

    public static final MapCodec<SetAttackersOnFireAbility> CODEC = RecordCodecBuilder.mapCodec(
            instance -> codecStart(instance)
                    .and(IntegerValue.durationSecondsCodec().fieldOf("duration").forGetter(SetAttackersOnFireAbility::fireDuration))
                    .and(BooleanValue.codec().optionalFieldOf("grants_fire_resistance", BooleanValue.TRUE).forGetter(SetAttackersOnFireAbility::grantsFireResistance))
                    .apply(instance, SetAttackersOnFireAbility::new)
    );

    public static final StreamCodec<ByteBuf, SetAttackersOnFireAbility> STREAM_CODEC = StreamCodec.composite(
            DoubleValue.streamCodec(),
            SetAttackersOnFireAbility::strikeChance,
            IntegerValue.streamCodec(),
            SetAttackersOnFireAbility::cooldown,
            IntegerValue.streamCodec(),
            SetAttackersOnFireAbility::fireDuration,
            BooleanValue.streamCodec(),
            SetAttackersOnFireAbility::grantsFireResistance,
            SetAttackersOnFireAbility::new
    );

    private final IntegerValue fireDuration;
    private final BooleanValue grantsFireResistance;

    public SetAttackersOnFireAbility(DoubleValue strikeChance, IntegerValue cooldown, IntegerValue fireDuration, BooleanValue grantsFireResistance) {
        super(strikeChance, cooldown);
        this.fireDuration = fireDuration;
        this.grantsFireResistance = grantsFireResistance;
    }

    public IntegerValue fireDuration() {
        return fireDuration;
    }

    public BooleanValue grantsFireResistance() {
        return grantsFireResistance;
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.SET_ATTACKERS_ON_FIRE.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return super.isNonCosmetic() && fireDuration().get() > 0;
    }

    @Override
    protected void applyEffect(LivingEntity target, LivingEntity attacker) {
        if (!attacker.fireImmune() && attacker.attackable() && fireDuration().get() > 0) {
            if (grantsFireResistance().get()) {
                target.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fireDuration().get(), 0, false, false, true));
            }
            attacker.igniteForTicks(fireDuration().get());
        }
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        tooltip.add(tooltipLine("strike_chance"));
        if (grantsFireResistance().get()) {
            tooltip.add(tooltipLine("fire_resistance"));
        }
    }
}
