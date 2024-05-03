package artifacts.item.wearable.feet;

import artifacts.ability.GrowPlantsAfterEatingAbility;
import artifacts.ability.ReplenishHungerOnGrassAbility;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModTags;
import artifacts.util.AbilityHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RootedBootsItem extends WearableArtifactItem {

    public RootedBootsItem() {
        super(new ReplenishHungerOnGrassAbility(), new GrowPlantsAfterEatingAbility());
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
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
