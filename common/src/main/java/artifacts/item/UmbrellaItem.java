package artifacts.item;

import artifacts.config.ItemConfigs;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.List;

public class UmbrellaItem extends ArtifactItem {

    public UmbrellaItem() {
        super(new Properties());
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public boolean isCosmetic() {
        return !ItemConfigs.UMBRELLA_IS_GLIDER.get() && !ItemConfigs.UMBRELLA_IS_SHIELD.get();
    }

    @Override
    protected void addEffectsTooltip(List<MutableComponent> tooltip) {
        if (ItemConfigs.UMBRELLA_IS_GLIDER.get()) {
            tooltip.add(tooltipLine("glider"));
        }
        if (ItemConfigs.UMBRELLA_IS_SHIELD.get()) {
            tooltip.add(tooltipLine("shield"));
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!ItemConfigs.UMBRELLA_IS_SHIELD.get()) {
            return super.use(level, player, hand);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    public static void onLivingUpdate(LivingEntity entity) {
        if (UmbrellaItem.shouldGlide(entity)) {
            entity.fallDistance = 0;
        }
    }

    public static boolean shouldGlide(LivingEntity entity) {
        boolean isInWater = entity.isInWater() && !AbilityHelper.hasAbilityActive(ModAbilities.SINKING.get(), entity);
        return ItemConfigs.UMBRELLA_IS_GLIDER.get()
                && !entity.onGround() && !isInWater
                && entity.getDeltaMovement().y < 0
                && !entity.hasEffect(MobEffects.SLOW_FALLING)
                && UmbrellaItem.isHoldingUmbrellaUpright(entity);
    }

    public static boolean isHoldingUmbrellaUpright(LivingEntity entity, InteractionHand hand) {
        return entity.getItemInHand(hand).getItem() instanceof UmbrellaItem && (!entity.isUsingItem() || entity.getUsedItemHand() != hand);
    }

    public static boolean isHoldingUmbrellaUpright(LivingEntity entity) {
        return isHoldingUmbrellaUpright(entity, InteractionHand.MAIN_HAND) || isHoldingUmbrellaUpright(entity, InteractionHand.OFF_HAND);
    }
}
