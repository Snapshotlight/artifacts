package artifacts.ability.mobeffect;

import artifacts.ability.ArtifactAbility;
import artifacts.ability.value.IntegerValue;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class MobEffectAbility implements ArtifactAbility {

    private final Holder<MobEffect> mobEffect;
    protected final IntegerValue level;

    protected MobEffectAbility(Holder<MobEffect> mobEffect) {
        this(mobEffect, new IntegerValue.Constant(1));
    }

    protected MobEffectAbility(Holder<MobEffect> mobEffect, IntegerValue level) {
        this.mobEffect = mobEffect;
        this.level = level;
    }

    public IntegerValue level() {
        return level;
    }

    private int getAmplifier() {
        return this.level().get() - 1;
    }

    protected int getDuration(LivingEntity entity) {
        return 40;
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
        return level.get() > 0;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MobEffectAbility that = (MobEffectAbility) o;
        return mobEffect.equals(that.mobEffect) && level.equals(that.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mobEffect, level);
    }
}
