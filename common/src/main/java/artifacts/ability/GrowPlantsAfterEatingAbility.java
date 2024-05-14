package artifacts.ability;

import artifacts.ability.value.BooleanValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModTags;
import artifacts.util.AbilityHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

// TODO migrate to simpleAbility
public record GrowPlantsAfterEatingAbility(BooleanValue enabled) implements ArtifactAbility {

    public static final MapCodec<GrowPlantsAfterEatingAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BooleanValue.enabledField().forGetter(GrowPlantsAfterEatingAbility::enabled)
    ).apply(instance, GrowPlantsAfterEatingAbility::new));

    public static final StreamCodec<ByteBuf, GrowPlantsAfterEatingAbility> STREAM_CODEC = StreamCodec.composite(
            BooleanValue.streamCodec(),
            GrowPlantsAfterEatingAbility::enabled,
            GrowPlantsAfterEatingAbility::new
    );

    @Override
    public Type<?> getType() {
        return ModAbilities.GROW_PLANTS_AFTER_EATING.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return enabled().get();
    }

    public static void applyBoneMeal(LivingEntity entity, FoodProperties properties) {
        if (!entity.level().isClientSide()
                && AbilityHelper.hasAbilityActive(ModAbilities.GROW_PLANTS_AFTER_EATING.get(), entity)
                && properties.nutrition() > 0
                && !properties.canAlwaysEat()
                && entity.onGround()
                && entity.getBlockStateOn().is(ModTags.ROOTED_BOOTS_GRASS)
        ) {
            BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), entity.level(), entity.getOnPos());
        }
    }
}
