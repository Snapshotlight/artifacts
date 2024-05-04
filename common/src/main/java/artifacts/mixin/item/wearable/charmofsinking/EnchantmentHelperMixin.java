package artifacts.mixin.item.wearable.charmofsinking;

import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @ModifyReturnValue(method = "hasAquaAffinity", at = @At("RETURN"))
    private static boolean dontSlowMiningUnderwater(boolean original, LivingEntity entity) {
        return original || AbilityHelper.hasAbilityActive(ModAbilities.SINKING.get(), entity);
    }
}
