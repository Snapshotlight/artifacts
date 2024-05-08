package artifacts.ability;

import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;

public record ApplyHasteAfterEatingAbility(IntegerValue durationPerFoodPoint, IntegerValue hasteLevel) implements ArtifactAbility {

    public static final MapCodec<ApplyHasteAfterEatingAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IntegerValue.field("duration", ModGameRules.ONION_RING_HASTE_DURATION_PER_FOOD_POINT).forGetter(ApplyHasteAfterEatingAbility::durationPerFoodPoint),
            IntegerValue.field("level", ModGameRules.ONION_RING_HASTE_LEVEL).forGetter(ApplyHasteAfterEatingAbility::hasteLevel)
    ).apply(instance, ApplyHasteAfterEatingAbility::new));

    public static final StreamCodec<ByteBuf, ApplyHasteAfterEatingAbility> STREAM_CODEC = StreamCodec.composite(
            IntegerValue.defaultStreamCodec(ModGameRules.ONION_RING_HASTE_DURATION_PER_FOOD_POINT),
            ApplyHasteAfterEatingAbility::durationPerFoodPoint,
            IntegerValue.defaultStreamCodec(ModGameRules.ONION_RING_HASTE_LEVEL),
            ApplyHasteAfterEatingAbility::hasteLevel,
            ApplyHasteAfterEatingAbility::new
    );

    public static void applyHasteEffect(LivingEntity entity, FoodProperties properties) {
        if (properties.nutrition() > 0 && !properties.canAlwaysEat()) {
            AbilityHelper.forEach(ModAbilities.APPLY_HASTE_AFTER_EATING.get(), entity, ability -> {
                int duration = ability.durationPerFoodPoint().get() * properties.nutrition();
                entity.addEffect((new MobEffectInstance(MobEffects.DIG_SPEED, duration, ability.hasteLevel().get() - 1, false, false, true)));
            });
        }
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.APPLY_HASTE_AFTER_EATING.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return durationPerFoodPoint().get() > 0 && hasteLevel().get() > 0;
    }
}
