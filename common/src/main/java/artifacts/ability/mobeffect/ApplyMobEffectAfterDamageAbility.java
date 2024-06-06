package artifacts.ability.mobeffect;

import artifacts.ability.ArtifactAbility;
import artifacts.config.value.Value;
import artifacts.config.value.ValueTypes;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import artifacts.util.ModCodecs;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Optional;

public record ApplyMobEffectAfterDamageAbility(Holder<MobEffect> mobEffect, Value<Integer> level, Value<Integer> duration, Optional<TagKey<DamageType>> tag) implements ArtifactAbility {

    public static final MapCodec<ApplyMobEffectAfterDamageAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("mob_effect").forGetter(ApplyMobEffectAfterDamageAbility::mobEffect),
            ValueTypes.mobEffectLevelField().forGetter(ApplyMobEffectAfterDamageAbility::level),
            ValueTypes.DURATION.codec().fieldOf("duration").forGetter(ApplyMobEffectAfterDamageAbility::duration),
            TagKey.codec(Registries.DAMAGE_TYPE).optionalFieldOf("tag").forGetter(ApplyMobEffectAfterDamageAbility::tag)
    ).apply(instance, ApplyMobEffectAfterDamageAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ApplyMobEffectAfterDamageAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT),
            ApplyMobEffectAfterDamageAbility::mobEffect,
            ValueTypes.MOB_EFFECT_LEVEL.streamCodec(),
            ApplyMobEffectAfterDamageAbility::level,
            ValueTypes.DURATION.streamCodec(),
            ApplyMobEffectAfterDamageAbility::duration,
            ByteBufCodecs.optional(ModCodecs.tagKeyStreamCodec(Registries.DAMAGE_TYPE)),
            ApplyMobEffectAfterDamageAbility::tag,
            ApplyMobEffectAfterDamageAbility::new
    );

    public static void onLivingDamaged(LivingEntity entity, DamageSource damageSource, float amount) {
        if (!entity.level().isClientSide()
                && amount >= 1
        ) {
            AbilityHelper.forEach(ModAbilities.APPLY_MOB_EFFECT_AFTER_DAMAGE.get(), entity, (ability, stack) -> {
                if (ability.tag().isEmpty() || damageSource.is(ability.tag().get())) {
                    entity.addEffect(new MobEffectInstance(ability.mobEffect(), ability.duration().get() * 20, ability.level().get() - 1, false, false, true));
                }
            }, true);
        }
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.APPLY_MOB_EFFECT_AFTER_DAMAGE.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return level().get() > 0 && duration().get() > 0;
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        if (mobEffect().equals(MobEffects.FIRE_RESISTANCE) && tag.isPresent() && tag.get().equals(DamageTypeTags.IS_FIRE)) {
            tooltip.add(tooltipLine("fire_resistance"));
        } else if (mobEffect().equals(MobEffects.MOVEMENT_SPEED)) {
            tooltip.add(tooltipLine("speed"));
        }
    }
}
