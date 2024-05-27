package artifacts.ability.mobeffect;

import artifacts.config.value.Value;
import artifacts.config.value.ValueTypes;
import artifacts.registry.ModAbilities;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
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
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("mob_effect").forGetter(GenericMobEffectAbility::getMobEffect),
            ValueTypes.MOB_EFFECT_LEVEL.codec().optionalFieldOf("level", Value.Constant.ONE).forGetter(GenericMobEffectAbility::getLevel),
            ValueTypes.BOOLEAN.codec().optionalFieldOf("enabled", Value.Constant.TRUE).forGetter(ability -> ability.enabled)
    ).apply(instance, GenericMobEffectAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GenericMobEffectAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT),
            GenericMobEffectAbility::getMobEffect,
            ValueTypes.MOB_EFFECT_LEVEL.streamCodec(),
            GenericMobEffectAbility::getLevel,
            ValueTypes.BOOLEAN.streamCodec(),
            ability -> ability.enabled,
            GenericMobEffectAbility::new
    );

    private final Value<Boolean> enabled;

    public GenericMobEffectAbility(Holder<MobEffect> mobEffect, Value<Integer> level, Value<Boolean> enabled) {
        super(mobEffect, level);
        this.enabled = enabled;
    }

    @Override
    public Value<Integer> getLevel() {
        return enabled.get() ? super.getLevel() : Value.Constant.ZERO;
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
