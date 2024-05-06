package artifacts.ability.mobeffect;

import artifacts.ability.ArtifactAbility;
import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class MountSpeedAbility extends MobEffectAbility {

    public static final MapCodec<MountSpeedAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IntegerValue.field("level", ModGameRules.COWBOY_HAT_SPEED_LEVEL).forGetter(MountSpeedAbility::level)
    ).apply(instance, MountSpeedAbility::new));

    public static final StreamCodec<ByteBuf, MountSpeedAbility> STREAM_CODEC = StreamCodec.composite(
            IntegerValue.defaultStreamCodec(ModGameRules.COWBOY_HAT_SPEED_LEVEL),
            MountSpeedAbility::level,
            MountSpeedAbility::new
    );

    public MountSpeedAbility(IntegerValue speedLevel) {
        super(MobEffects.MOVEMENT_SPEED, speedLevel);
    }

    public static ArtifactAbility createDefaultInstance() {
        return ArtifactAbility.createDefaultInstance(CODEC);
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
