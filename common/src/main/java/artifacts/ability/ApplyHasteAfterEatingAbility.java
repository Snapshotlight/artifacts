package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;

public class ApplyHasteAfterEatingAbility implements ArtifactAbility {

    public static void applyHasteEffect(LivingEntity entity, FoodProperties properties) {
        if (properties.getNutrition() > 0 && !properties.canAlwaysEat()) {
            AbilityHelper.forEach(ModAbilities.APPLY_HASTE_AFTER_EATING.get(), entity, ability -> {
                int duration = ability.getDurationPerFoodPoint() * properties.getNutrition();
                entity.addEffect((new MobEffectInstance(MobEffects.DIG_SPEED, duration, ability.getHasteLevel() - 1, false, false, true)));
            });
        }
    }

    public int getDurationPerFoodPoint() {
        return ModGameRules.ONION_RING_HASTE_DURATION_PER_FOOD_POINT.get();
    }

    public int getHasteLevel() {
        return ModGameRules.ONION_RING_HASTE_LEVEL.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.APPLY_HASTE_AFTER_EATING.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return getDurationPerFoodPoint() > 0 && getHasteLevel() > 0;
    }
}
