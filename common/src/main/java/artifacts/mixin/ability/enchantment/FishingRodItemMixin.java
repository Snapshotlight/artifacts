package artifacts.mixin.ability.enchantment;

import artifacts.util.AbilityHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingRodItem.class)
public abstract class FishingRodItemMixin {

    @ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getFishingLuckBonus(Lnet/minecraft/world/item/ItemStack;)I"))
    private int getFishingLuckBonus(int original, Level level, Player player, InteractionHand interactionHand) {
        return original + AbilityHelper.getEnchantmentSum(Enchantments.LUCK_OF_THE_SEA, player);
    }

    @ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getFishingSpeedBonus(Lnet/minecraft/world/item/ItemStack;)I"))
    private int getFishingSpeedBonus(int original, Level level, Player player, InteractionHand interactionHand) {
        // Lure >5 breaks fishing, don't return more than 5 unless original was more than 5
        if (original >= 5) {
            return original;
        }
        return Math.min(5, original + AbilityHelper.getEnchantmentSum(Enchantments.LURE, player));
    }
}
