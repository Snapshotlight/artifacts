package artifacts.ability.mobeffect;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class MountSpeedAbility extends MobEffectAbility {

    public MountSpeedAbility() {
        super(MobEffects.MOVEMENT_SPEED, ModGameRules.COWBOY_HAT_SPEED_LEVEL);
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.MOUNT_SPEED.get();
    }

    @Override
    protected LivingEntity getTarget(LivingEntity entity) {
        if (entity.getControlledVehicle() instanceof LivingEntity target) {
            return target;
        }
        return null;
    }

    @Override
    protected int getUpdateInterval() {
        return 10;
    }

    @Override
    protected boolean shouldShowParticles() {
        return true;
    }
}
