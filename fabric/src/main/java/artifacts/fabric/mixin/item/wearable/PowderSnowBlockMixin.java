package artifacts.fabric.mixin.item.wearable;

import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin {

    @ModifyReturnValue(method = "canEntityWalkOnPowderSnow", at = @At("RETURN"))
    private static boolean canEntityWalkOnPowderSnow(boolean original, Entity entity) {
        return original || (entity instanceof LivingEntity livingEntity && AbilityHelper.hasAbility(ModAbilities.WALK_ON_POWDER_SNOW, livingEntity));
    }
}
