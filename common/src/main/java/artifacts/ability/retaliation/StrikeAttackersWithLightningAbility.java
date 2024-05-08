package artifacts.ability.retaliation;

import artifacts.ability.value.DoubleValue;
import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
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
            instance -> codecStart(instance, ModGameRules.SHOCK_PENDANT_STRIKE_CHANCE, ModGameRules.SHOCK_PENDANT_COOLDOWN)
                    .apply(instance, StrikeAttackersWithLightningAbility::new)
    );

    public static final StreamCodec<ByteBuf, StrikeAttackersWithLightningAbility> STREAM_CODEC = StreamCodec.composite(
            DoubleValue.defaultStreamCodec(ModGameRules.SHOCK_PENDANT_STRIKE_CHANCE),
            StrikeAttackersWithLightningAbility::strikeChance,
            IntegerValue.defaultStreamCodec(ModGameRules.SHOCK_PENDANT_COOLDOWN),
            StrikeAttackersWithLightningAbility::cooldown,
            StrikeAttackersWithLightningAbility::new
    );

    public StrikeAttackersWithLightningAbility(DoubleValue strikeChance, IntegerValue cooldown) {
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
