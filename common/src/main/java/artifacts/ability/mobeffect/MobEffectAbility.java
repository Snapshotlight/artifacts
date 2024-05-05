package artifacts.ability.mobeffect;

import artifacts.ability.ArtifactAbility;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public abstract class MobEffectAbility implements ArtifactAbility {

    private final Holder<MobEffect> mobEffect;
    private final int duration;
    protected final Supplier<Integer> amplifier;

    public MobEffectAbility(Holder<MobEffect> mobEffect) {
        this(mobEffect, () -> 1);
    }

    public MobEffectAbility(Holder<MobEffect> mobEffect, Supplier<Integer> amplifier) {
        this(mobEffect, amplifier, 40);
    }

    private MobEffectAbility(Holder<MobEffect> mobEffect, Supplier<Integer> amplifier, int duration) {
        this.mobEffect = mobEffect;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    private int getAmplifier() {
        return this.amplifier.get() - 1;
    }

    protected int getDuration(LivingEntity entity) {
        return duration;
    }

    @Nullable
    protected LivingEntity getTarget(LivingEntity entity) {
        return entity;
    }

    protected boolean shouldShowIcon() {
        return false;
    }

    protected boolean shouldShowParticles() {
        return false;
    }

    protected int getUpdateInterval() {
        return 1;
    }

    protected boolean shouldApplyMobEffect(LivingEntity entity) {
        return true;
    }

    @Override
    public boolean isNonCosmetic() {
        return amplifier.get() > 0;
    }

    @Override
    public void wornTick(LivingEntity entity, boolean isOnCooldown, boolean isActive) {
        if (!entity.level().isClientSide() && isActive && shouldApplyMobEffect(entity)) {
            LivingEntity target = getTarget(entity);
            if (target != null && entity.tickCount % getUpdateInterval() == 0) {
                target.addEffect(new MobEffectInstance(mobEffect, getDuration(target) - 1, getAmplifier(), false, shouldShowParticles(), shouldShowIcon()));
            }
        }
    }

    @Override
    public void onUnequip(LivingEntity entity, boolean wasActive) {
        if (!entity.level().isClientSide() && getTarget(entity) == entity && wasActive) {
            MobEffectInstance effectInstance = entity.getEffect(mobEffect);
            if (effectInstance != null && effectInstance.getAmplifier() == getAmplifier() && !effectInstance.isVisible() && effectInstance.getDuration() < getDuration(entity)) {
                entity.removeEffect(mobEffect);
            }
        }
    }
}
