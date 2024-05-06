package artifacts.mixin.item.wearable.crossnecklace;

import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "hurt", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;invulnerableTime:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void hurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = ((LivingEntity) (Object) this);
        int bonusTicks = AbilityHelper.maxInt(ModAbilities.BONUS_INVINCIBILITY_TICKS.get(), entity, ability -> ability.bonusTicks().get(), true);
        if (bonusTicks > 0) {
            entity.invulnerableTime += bonusTicks;
            AbilityHelper.applyCooldowns(ModAbilities.BONUS_INVINCIBILITY_TICKS.get(), entity, ability -> ability.cooldown().get());
        }
    }

    @Inject(method = "handleDamageEvent", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;invulnerableTime:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void handleDamageEvent(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity entity = ((LivingEntity) (Object) this);
        int bonusTicks = AbilityHelper.maxInt(ModAbilities.BONUS_INVINCIBILITY_TICKS.get(), entity, ability -> ability.bonusTicks().get(), true);
        if (bonusTicks > 0) {
            entity.invulnerableTime += bonusTicks;
        }
    }
}
