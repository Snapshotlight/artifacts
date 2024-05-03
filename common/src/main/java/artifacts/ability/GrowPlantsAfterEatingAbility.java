package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModTags;
import artifacts.util.AbilityHelper;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class GrowPlantsAfterEatingAbility implements ArtifactAbility {

    @Override
    public Type<?> getType() {
        return ModAbilities.GROW_PLANTS_AFTER_EATING;
    }

    @Override
    public boolean isNonCosmetic() {
        return ModGameRules.ROOTED_BOOTS_DO_GROW_PLANTS_AFTER_EATING.get() && ModGameRules.ROOTED_BOOTS_ENABLED.get();
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        // TODO
    }

    public static void applyBoneMeal(LivingEntity entity, FoodProperties properties) {
        if (!entity.level().isClientSide()
                && AbilityHelper.hasAbility(ModAbilities.GROW_PLANTS_AFTER_EATING, entity)
                && properties.getNutrition() > 0
                && !properties.canAlwaysEat()
                && entity.onGround()
                && entity.getBlockStateOn().is(ModTags.ROOTED_BOOTS_GRASS)
        ) {
            BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), entity.level(), entity.getOnPos());
        }
    }
}
