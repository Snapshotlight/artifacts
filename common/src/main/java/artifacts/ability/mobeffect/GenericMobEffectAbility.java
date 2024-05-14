package artifacts.ability.mobeffect;

import artifacts.ability.value.BooleanValue;
import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GenericMobEffectAbility extends MobEffectAbility {

    private static final Set<Holder<MobEffect>> CUSTOM_TOOLTIP_MOB_EFFECTS = Set.of(
            MobEffects.INVISIBILITY
    );

    public static final MapCodec<GenericMobEffectAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("id").forGetter(GenericMobEffectAbility::getMobEffect),
            IntegerValue.constantCodec(127, 1).optionalFieldOf("level", IntegerValue.ONE).forGetter(GenericMobEffectAbility::getLevel),
            BooleanValue.constantCodec().optionalFieldOf("enabled", BooleanValue.TRUE).forGetter(ability -> ability.enabled)
    ).apply(instance, GenericMobEffectAbility::new));

    public static final StreamCodec<ByteBuf, GenericMobEffectAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.idMapper(BuiltInRegistries.MOB_EFFECT.asHolderIdMap()),
            GenericMobEffectAbility::getMobEffect,
            IntegerValue.constantStreamCodec(),
            GenericMobEffectAbility::getLevel,
            BooleanValue.constantStreamCodec(),
            ability -> ability.enabled,
            GenericMobEffectAbility::new
    );

    private final BooleanValue enabled;

    public GenericMobEffectAbility(Holder<MobEffect> mobEffect, IntegerValue level, BooleanValue enabled) {
        super(mobEffect, level);
        this.enabled = enabled;
    }

    @Override
    public IntegerValue getLevel() {
        return enabled.get() ? super.getLevel() : IntegerValue.ZERO;
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.MOB_EFFECT.get();
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        for (Holder<MobEffect> mobEffect : CUSTOM_TOOLTIP_MOB_EFFECTS) {
            if (mobEffect.isBound() && mobEffect.value() == getMobEffect().value()) {
                //noinspection ConstantConditions
                tooltip.add(tooltipLine(BuiltInRegistries.MOB_EFFECT.getKey(mobEffect.value()).getPath()));
                return;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GenericMobEffectAbility that = (GenericMobEffectAbility) o;
        return enabled.equals(that.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isEnabled());
    }
}
