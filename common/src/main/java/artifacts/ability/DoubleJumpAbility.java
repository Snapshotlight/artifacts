package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModSoundEvents;
import artifacts.util.AbilityHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class DoubleJumpAbility implements ArtifactAbility {

    public static void jump(Player player) {
        player.fallDistance = 0;

        double upwardsMotion = 0.5;
        if (player.hasEffect(MobEffects.JUMP)) {
            // noinspection ConstantConditions
            upwardsMotion += 0.1 * (player.getEffect(MobEffects.JUMP).getAmplifier() + 1);
        }
        if (player.isSprinting()) {
            upwardsMotion *= 1 + AbilityHelper.maxDouble(
                    ModAbilities.DOUBLE_JUMP.get(), player,
                    DoubleJumpAbility::getSprintJumpVerticalVelocity, false
            );
        }

        Vec3 motion = player.getDeltaMovement();
        double motionMultiplier = 0;
        if (player.isSprinting()) {
            motionMultiplier = AbilityHelper.maxDouble(
                    ModAbilities.DOUBLE_JUMP.get(), player,
                    DoubleJumpAbility::getSprintJumpHorizontalVelocity, false
            );
        }
        float direction = (float) (player.getYRot() * Math.PI / 180);
        player.setDeltaMovement(player.getDeltaMovement().add(
                -Mth.sin(direction) * motionMultiplier,
                upwardsMotion - motion.y,
                Mth.cos(direction) * motionMultiplier)
        );

        player.hasImpulse = true;

        player.awardStat(Stats.JUMP);
        if (player.isSprinting()) {
            player.causeFoodExhaustion(0.2F);
        } else {
            player.causeFoodExhaustion(0.05F);
        }

        if (AbilityHelper.hasAbility(ModAbilities.FART.get(), player)) {
            player.playSound(ModSoundEvents.FART.get(), 1, 0.9F + player.getRandom().nextFloat() * 0.2F);
        } else {
            player.playSound(SoundEvents.WOOL_FALL, 1, 0.9F + player.getRandom().nextFloat() * 0.2F);
        }
    }

    public static float getReducedFallDistance(LivingEntity entity, float distance) {
        if (AbilityHelper.hasAbility(ModAbilities.DOUBLE_JUMP.get(), entity)) {
            return Math.max(0, distance - 3);
        }
        return distance;
    }

    public double getSprintJumpHorizontalVelocity() {
        return ModGameRules.CLOUD_IN_A_BOTTLE_SPRINT_JUMP_HORIZONTAL_VELOCITY.get();
    }

    public double getSprintJumpVerticalVelocity() {
        return ModGameRules.CLOUD_IN_A_BOTTLE_SPRINT_JUMP_VERTICAL_VELOCITY.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.DOUBLE_JUMP.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return ModGameRules.CLOUD_IN_A_BOTTLE_ENABLED.get();
    }
}
