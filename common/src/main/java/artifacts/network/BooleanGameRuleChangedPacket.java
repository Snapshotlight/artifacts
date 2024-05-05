package artifacts.network;

import artifacts.Artifacts;
import artifacts.registry.ModGameRules;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record BooleanGameRuleChangedPacket(String key, boolean value) implements CustomPacketPayload {

    public static final Type<BooleanGameRuleChangedPacket> TYPE = new Type<>(Artifacts.id("boolean_game_rule_changed"));

    public static final StreamCodec<FriendlyByteBuf, BooleanGameRuleChangedPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            BooleanGameRuleChangedPacket::key,
            ByteBufCodecs.BOOL,
            BooleanGameRuleChangedPacket::value,
            BooleanGameRuleChangedPacket::new
    );

    public void apply(NetworkManager.PacketContext context) {
        context.queue(() -> ModGameRules.updateValue(key, value));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
