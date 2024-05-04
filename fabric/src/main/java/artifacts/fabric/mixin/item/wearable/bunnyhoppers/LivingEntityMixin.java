package artifacts.fabric.mixin.item.wearable.bunnyhoppers;

import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "causeFallDamage", cancellable = true, at = @At("HEAD"))
    private void cancelFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> info) {
        if (AbilityHelper.hasAbilityActive(ModAbilities.CANCEL_FALL_DAMAGE.get(), (LivingEntity) (Object) this)) {
            info.setReturnValue(false); // early return intended
        }
    }
}
