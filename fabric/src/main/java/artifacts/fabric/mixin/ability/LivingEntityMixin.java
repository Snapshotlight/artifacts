package artifacts.fabric.mixin.ability;

import artifacts.event.ArtifactEvents;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        // noinspection ConstantConditions
        LivingEntity entity = (LivingEntity) (Object) this;
        ArtifactEvents.livingUpdate(entity);
    }
}
