package artifacts.ability;

import artifacts.ability.value.BooleanValue;
import artifacts.ability.value.DoubleValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModAttributes;
import artifacts.registry.ModSoundEvents;
import artifacts.util.AbilityHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public record DoubleJumpAbility(BooleanValue enabled, DoubleValue sprintHorizontalVelocity, DoubleValue sprintVerticalVelocity) implements ArtifactAbility {

    public static final MapCodec<DoubleJumpAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BooleanValue.enabledField().forGetter(DoubleJumpAbility::enabled),
            DoubleValue.codec(100 * 100, 100).optionalFieldOf("sprint_jump_horizontal_velocity", DoubleValue.ZERO).forGetter(DoubleJumpAbility::sprintHorizontalVelocity),
            DoubleValue.codec(100 * 100, 100).optionalFieldOf("sprint_jump_vertical_velocity", DoubleValue.ZERO).forGetter(DoubleJumpAbility::sprintVerticalVelocity)
    ).apply(instance, DoubleJumpAbility::new));

    public static final StreamCodec<ByteBuf, DoubleJumpAbility> STREAM_CODEC = StreamCodec.composite(
            BooleanValue.streamCodec(),
            DoubleJumpAbility::enabled,
            DoubleValue.streamCodec(),
            DoubleJumpAbility::sprintHorizontalVelocity,
            DoubleValue.streamCodec(),
            DoubleJumpAbility::sprintVerticalVelocity,
            DoubleJumpAbility::new
    );


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
                    ability -> ability.sprintVerticalVelocity().get(), false
            );
        }

        Vec3 motion = player.getDeltaMovement();
        double motionMultiplier = 0;
        if (player.isSprinting()) {
            motionMultiplier = AbilityHelper.maxDouble(
                    ModAbilities.DOUBLE_JUMP.get(), player,
                    ability -> ability.sprintHorizontalVelocity().get(), false
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

        if (player.level().isClientSide()) {
            double chance = player.getAttributeValue(ModAttributes.FLATULENCE);
            if (player.getRandom().nextFloat() < chance) {
                player.level().playSound(null, player, ModSoundEvents.FART.get(), SoundSource.PLAYERS, 1, 0.9F + player.getRandom().nextFloat() * 0.2F);
            } else {
                player.level().playSound(null, player, SoundEvents.WOOL_FALL, SoundSource.PLAYERS, 1, 0.9F + player.getRandom().nextFloat() * 0.2F);
            }
        }
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.DOUBLE_JUMP.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return enabled().get();
    }
}
