package artifacts.ability.retaliation;

import artifacts.config.value.Value;
import artifacts.config.value.ValueTypes;
import artifacts.registry.ModAbilities;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class StrikeAttackersWithLightningAbility extends RetaliationAbility {

    public static final MapCodec<StrikeAttackersWithLightningAbility> CODEC = RecordCodecBuilder.mapCodec(
            instance -> codecStart(instance).apply(instance, StrikeAttackersWithLightningAbility::new)
    );

    public static final StreamCodec<ByteBuf, StrikeAttackersWithLightningAbility> STREAM_CODEC = StreamCodec.composite(
            ValueTypes.FRACTION.streamCodec(),
            StrikeAttackersWithLightningAbility::strikeChance,
            ValueTypes.DURATION.streamCodec(),
            StrikeAttackersWithLightningAbility::cooldown,
            StrikeAttackersWithLightningAbility::new
    );

    public StrikeAttackersWithLightningAbility(Value<Double> strikeChance, Value<Integer> cooldown) {
        super(strikeChance, cooldown);
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.STRIKE_ATTACKERS_WITH_LIGHTNING.get();
    }

    @Override
    protected void applyEffect(LivingEntity target, LivingEntity attacker) {
        if (attacker.level().canSeeSky(BlockPos.containing(attacker.position()))) {
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(attacker.level());
            if (lightningBolt != null) {
                lightningBolt.moveTo(Vec3.atBottomCenterOf(attacker.blockPosition()));
                lightningBolt.setCause(attacker instanceof ServerPlayer player ? player : null);
                attacker.level().addFreshEntity(lightningBolt);
            }
        }
    }
}
