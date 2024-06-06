package artifacts.ability.mobeffect;

import artifacts.ability.ArtifactAbility;
import artifacts.config.value.Value;
import artifacts.config.value.ValueTypes;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;

import java.util.List;

public record ApplyMobEffectAfterEatingAbility(Holder<MobEffect> mobEffect, Value<Integer> durationPerFoodPoint, Value<Integer> level) implements ArtifactAbility {

    public static final MapCodec<ApplyMobEffectAfterEatingAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("mob_effect").forGetter(ApplyMobEffectAfterEatingAbility::mobEffect),
            ValueTypes.DURATION.codec().fieldOf("duration").forGetter(ApplyMobEffectAfterEatingAbility::durationPerFoodPoint),
            ValueTypes.mobEffectLevelField().forGetter(ApplyMobEffectAfterEatingAbility::level)
    ).apply(instance, ApplyMobEffectAfterEatingAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ApplyMobEffectAfterEatingAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT),
            ApplyMobEffectAfterEatingAbility::mobEffect,
            ValueTypes.DURATION.streamCodec(),
            ApplyMobEffectAfterEatingAbility::durationPerFoodPoint,
            ValueTypes.MOB_EFFECT_LEVEL.streamCodec(),
            ApplyMobEffectAfterEatingAbility::level,
            ApplyMobEffectAfterEatingAbility::new
    );

    public static void applyEffects(LivingEntity entity, FoodProperties properties) {
        int foodPointsMissing = entity instanceof Player player ? 20 - player.getFoodData().getFoodLevel() : 20;
        int foodPointsRestored = Math.min(properties.nutrition(), foodPointsMissing);
        applyEffects(entity, foodPointsRestored);
    }

    public static void applyEffects(LivingEntity entity, int foodPointsRestored) {
        if (foodPointsRestored > 0) {
            AbilityHelper.forEach(ModAbilities.APPLY_MOB_EFFECT_AFTER_EATING.get(), entity, ability -> {
                int duration = ability.durationPerFoodPoint().get() * 20 * foodPointsRestored;
                entity.addEffect((new MobEffectInstance(ability.mobEffect(), duration, ability.level().get() - 1, false, false, true)));
            });
        }
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.APPLY_MOB_EFFECT_AFTER_EATING.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return durationPerFoodPoint().get() > 0 && level().get() > 0;
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        if (mobEffect.equals(MobEffects.DIG_SPEED)) {
            tooltip.add(tooltipLine("haste"));
        }
    }
}
