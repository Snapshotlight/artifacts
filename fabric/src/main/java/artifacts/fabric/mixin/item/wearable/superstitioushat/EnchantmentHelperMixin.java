package artifacts.fabric.mixin.item.wearable.superstitioushat;

import artifacts.util.AbilityHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @ModifyReturnValue(method = "getMobLooting", at = @At("RETURN"))
    private static int increaseLooting(int original, LivingEntity entity) {
        return original + AbilityHelper.getEnchantmentSum(Enchantments.MOB_LOOTING, entity);
    }
}
