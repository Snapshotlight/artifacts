package artifacts.network;

import artifacts.Artifacts;
import artifacts.ability.DoubleJumpAbility;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record DoubleJumpPacket() implements CustomPacketPayload {

    public static final Type<DoubleJumpPacket> TYPE = new Type<>(Artifacts.id("double_jump"));

    public static final StreamCodec<FriendlyByteBuf, DoubleJumpPacket> CODEC = StreamCodec.unit(new DoubleJumpPacket());

    void apply(NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof ServerPlayer player && AbilityHelper.hasAbilityActive(ModAbilities.DOUBLE_JUMP.get(), player)) {
            context.queue(() -> {
                DoubleJumpAbility.jump(player);

                for (int i = 0; i < 20; ++i) {
                    double motionX = player.getRandom().nextGaussian() * 0.02;
                    double motionY = player.getRandom().nextGaussian() * 0.02 + 0.20;
                    double motionZ = player.getRandom().nextGaussian() * 0.02;
                    ParticleOptions particleType = player.isInWater() ? ParticleTypes.BUBBLE : ParticleTypes.POOF;
                    player.serverLevel().sendParticles(particleType, player.getX(), player.getY(), player.getZ(), 1, motionX, motionY, motionZ, 0.15);
                }
            });
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
