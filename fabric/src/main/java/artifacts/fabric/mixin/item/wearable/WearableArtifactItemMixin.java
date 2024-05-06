package artifacts.fabric.mixin.item.wearable;

import artifacts.Artifacts;
import artifacts.fabric.ArtifactsFabric;
import artifacts.fabric.client.CosmeticsHelper;
import artifacts.item.ArtifactItem;
import artifacts.item.WearableArtifactItem;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(WearableArtifactItem.class)
public abstract class WearableArtifactItemMixin extends ArtifactItem {

    @Shadow
    public abstract SoundEvent getEquipSound();

    @Shadow
    public abstract float getEquipSoundPitch();

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        if (!stack.has(DataComponents.FOOD) && TrinketItem.equipItem(user, stack)) {
            user.playSound(getEquipSound(), 1, getEquipSoundPitch());

            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        return super.use(level, user, hand);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack slotStack, ItemStack holdingStack, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (!isCosmetic(slotStack) && clickAction == ClickAction.SECONDARY && holdingStack.isEmpty()) {
            CosmeticsHelper.toggleCosmetics(slotStack);
            return true;
        }

        return super.overrideOtherStackedOnMe(slotStack, holdingStack, slot, clickAction, player, slotAccess);
    }

    @Override
    protected void addTooltip(ItemStack stack, List<MutableComponent> tooltip, @Nullable Player player) {
        if (!isCosmetic(stack)) { // Don't render cosmetics tooltip if item is cosmetic-only
            if (CosmeticsHelper.areCosmeticsToggledOffByPlayer(stack)) {
                tooltip.add(
                        Component.translatable("%s.tooltip.cosmetics_disabled".formatted(Artifacts.MOD_ID))
                                .withStyle(ChatFormatting.ITALIC)
                );
            } else if (ArtifactsFabric.getClientConfig().alwaysShowCosmeticsToggleTooltip()) {
                tooltip.add(
                        Component.translatable("%s.tooltip.cosmetics_enabled".formatted(Artifacts.MOD_ID))
                                .withStyle(ChatFormatting.ITALIC)
                );
            }
        }
        super.addTooltip(stack, tooltip, player);
    }
}
