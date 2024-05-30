package artifacts.ability.mobeffect;

import artifacts.ability.ArtifactAbility;
import artifacts.config.value.Value;
import artifacts.config.value.ValueTypes;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import artifacts.util.DamageSourceHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.event.EventResult;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Set;

public record AttacksInflictMobEffectAbility(Holder<MobEffect> mobEffect, Value<Integer> level, Value<Integer> duration, Value<Integer> cooldown) implements ArtifactAbility {

    private static final Set<Holder<MobEffect>> CUSTOM_TOOLTIP_MOB_EFFECTS = Set.of(
            MobEffects.WITHER,
            MobEffects.POISON
    );

    public static final MapCodec<AttacksInflictMobEffectAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("mob_effect").forGetter(AttacksInflictMobEffectAbility::mobEffect),
            ValueTypes.MOB_EFFECT_LEVEL.codec().optionalFieldOf("level", Value.Constant.ONE).forGetter(AttacksInflictMobEffectAbility::level),
            ValueTypes.DURATION.codec().fieldOf("duration").forGetter(AttacksInflictMobEffectAbility::duration),
            ValueTypes.DURATION.codec().optionalFieldOf("cooldown", Value.Constant.ZERO).forGetter(AttacksInflictMobEffectAbility::cooldown)
    ).apply(instance, AttacksInflictMobEffectAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AttacksInflictMobEffectAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT),
            AttacksInflictMobEffectAbility::mobEffect,
            ValueTypes.MOB_EFFECT_LEVEL.streamCodec(),
            AttacksInflictMobEffectAbility::level,
            ValueTypes.DURATION.streamCodec(),
            AttacksInflictMobEffectAbility::duration,
            ValueTypes.DURATION.streamCodec(),
            AttacksInflictMobEffectAbility::cooldown,
            AttacksInflictMobEffectAbility::new
    );

    public static EventResult onLivingHurt(LivingEntity entity, DamageSource damageSource, float amount) {
        LivingEntity attacker = DamageSourceHelper.getAttacker(damageSource);
        if (attacker != null && DamageSourceHelper.isMeleeAttack(damageSource)) {
            AbilityHelper.forEach(ModAbilities.ATTACKS_INFLICT_MOB_EFFECT.get(), attacker, (ability, stack) -> {
                entity.addEffect(new MobEffectInstance(ability.mobEffect(), ability.duration().get() * 20, ability.level().get() - 1), attacker);
                if (attacker instanceof Player player) {
                    player.getCooldowns().addCooldown(stack.getItem(), ability.cooldown().get() * 20);
                }
            }, true);
        }
        return EventResult.pass();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.ATTACKS_INFLICT_MOB_EFFECT.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return duration().get() > 0 && level().get() > 0;
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        for (Holder<MobEffect> mobEffect : CUSTOM_TOOLTIP_MOB_EFFECTS) {
            if (mobEffect.isBound() && mobEffect.value() == mobEffect().value()) {
                //noinspection ConstantConditions
                tooltip.add(tooltipLine(BuiltInRegistries.MOB_EFFECT.getKey(mobEffect.value()).getPath()));
                return;
            }
        }
    }
}
