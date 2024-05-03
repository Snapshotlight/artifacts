package artifacts.fabric.mixin.item.wearable.cloudinabottle;

import artifacts.ability.DoubleJumpAbility;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyVariable(method = "causeFallDamage", ordinal = 0, at = @At("HEAD"), argsOnly = true)
    private float reduceFallDistance(float fallDistance) {
        //noinspection ConstantConditions
        return DoubleJumpAbility.getReducedFallDistance((LivingEntity) (Object) this, fallDistance);
    }
}
