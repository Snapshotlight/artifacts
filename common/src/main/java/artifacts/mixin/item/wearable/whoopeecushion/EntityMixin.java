package artifacts.mixin.item.wearable.whoopeecushion;

import artifacts.ability.FartAbility;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModSoundEvents;
import artifacts.util.AbilityHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract boolean isShiftKeyDown();

    @Inject(method = "setShiftKeyDown", at = @At("HEAD"))
    private void setShiftKeyDown(boolean isDown, CallbackInfo ci) {
        if (isDown && !isShiftKeyDown() && ((Entity) (Object) this) instanceof LivingEntity entity && !entity.level().isClientSide()) {
            double chance = AbilityHelper.maxDouble(ModAbilities.FART.get(), entity, ability -> ability.fartChance().get(), false);
            if (entity.getRandom().nextFloat() < chance) {
                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSoundEvents.FART.get(), SoundSource.PLAYERS, 1, 0.9F + entity.getRandom().nextFloat() * 0.2F);
            }
        }
    }
}
