package artifacts.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class EverlastingFoodItem extends ArtifactItem {

    private final Supplier<Integer> eatingCooldown;
    private final Supplier<Boolean> isEnabled;

    public EverlastingFoodItem(FoodProperties food, Supplier<Integer> eatingCooldown, Supplier<Boolean> isEnabled) {
        super(new Properties().food(food));
        this.eatingCooldown = eatingCooldown;
        this.isEnabled = isEnabled;
    }

    @Override
    public boolean isCosmetic(ItemStack stack) {
        return !isEnabled.get();
    }

    @Override
    protected String getTooltipItemName() {
        return "everlasting_food";
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if (stack.has(DataComponents.FOOD)) {
            entity.eat(world, stack.copy());
            addCooldown(entity, eatingCooldown.get());
        }

        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!isEnabled.get()) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }
}


