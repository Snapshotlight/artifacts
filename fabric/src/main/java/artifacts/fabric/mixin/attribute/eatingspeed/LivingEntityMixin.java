package artifacts.fabric.mixin.attribute.eatingspeed;

import artifacts.event.ArtifactEvents;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract ItemStack getUseItem();

    @SuppressWarnings("ConstantConditions")
    @ModifyExpressionValue(method = "startUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseDuration()I"))
    private int decreaseDrinkingDuration(int original, InteractionHand hand) {
        LivingEntity entity = (LivingEntity) (Object) this;
        return ArtifactEvents.modifyUseDuration(original, entity.getItemInHand(hand), entity);
    }
}
