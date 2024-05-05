package artifacts.network;

import artifacts.Artifacts;
import artifacts.registry.ModGameRules;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record IntegerGameRuleChangedPacket(String key, int value) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<IntegerGameRuleChangedPacket> TYPE = new CustomPacketPayload.Type<>(Artifacts.id("integer_game_rule_changed"));

    public static final StreamCodec<FriendlyByteBuf, IntegerGameRuleChangedPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            IntegerGameRuleChangedPacket::key,
            ByteBufCodecs.INT,
            IntegerGameRuleChangedPacket::value,
            IntegerGameRuleChangedPacket::new
    );

    public void apply(NetworkManager.PacketContext context) {
        context.queue(() -> ModGameRules.updateValue(key, value));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
