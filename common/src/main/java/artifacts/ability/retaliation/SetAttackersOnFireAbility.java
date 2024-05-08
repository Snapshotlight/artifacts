package artifacts.ability.retaliation;

import artifacts.ability.value.BooleanValue;
import artifacts.ability.value.DoubleValue;
import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
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
            instance -> codecStart(instance, ModGameRules.FLAME_PENDANT_STRIKE_CHANCE, ModGameRules.FLAME_PENDANT_COOLDOWN)
                    .and(IntegerValue.field("duration", ModGameRules.FLAME_PENDANT_FIRE_DURATION).forGetter(SetAttackersOnFireAbility::fireDuration))
                    .and(BooleanValue.field("grants_fire_resistance", ModGameRules.FLAME_PENDANT_DO_GRANT_FIRE_RESISTANCE).forGetter(SetAttackersOnFireAbility::grantsFireResistance))
                    .apply(instance, SetAttackersOnFireAbility::new)
    );

    public static final StreamCodec<ByteBuf, SetAttackersOnFireAbility> STREAM_CODEC = StreamCodec.composite(
            DoubleValue.defaultStreamCodec(ModGameRules.FLAME_PENDANT_STRIKE_CHANCE),
            SetAttackersOnFireAbility::strikeChance,
            IntegerValue.defaultStreamCodec(ModGameRules.FLAME_PENDANT_COOLDOWN),
            SetAttackersOnFireAbility::cooldown,
            IntegerValue.defaultStreamCodec(ModGameRules.FLAME_PENDANT_FIRE_DURATION),
            SetAttackersOnFireAbility::fireDuration,
            BooleanValue.defaultStreamCodec(ModGameRules.FLAME_PENDANT_DO_GRANT_FIRE_RESISTANCE),
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
        if (ModGameRules.FLAME_PENDANT_DO_GRANT_FIRE_RESISTANCE.get()) {
            tooltip.add(tooltipLine("fire_resistance"));
        }
    }
}
