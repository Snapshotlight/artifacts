package artifacts.network;

import artifacts.Artifacts;
import artifacts.config.ItemConfigs;
import artifacts.config.value.Value;
import artifacts.config.value.type.ValueType;
import com.mojang.datafixers.util.Pair;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record UpdateItemConfigPacket(Value.ConfigValue<?> value) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<UpdateItemConfigPacket> TYPE = new CustomPacketPayload.Type<>(Artifacts.id("update_item_configs"));

    // oof
    public static final StreamCodec<ByteBuf, UpdateItemConfigPacket> CODEC = StreamCodec.<ByteBuf, Pair<String, ValueType<?, ?>>, String, ValueType<?, ?>>composite(
            ByteBufCodecs.STRING_UTF8,
            Pair::getFirst,
            ByteBufCodecs.idMapper(
                ItemConfigs.getValueTypes().stream().toList()::get,
                ItemConfigs.getValueTypes().stream().toList()::indexOf
            ),
            Pair::getSecond,
            Pair::of
    ).<Value.ConfigValue<?>>dispatch(
            value -> Pair.of(value.getId(), value.type()),
            pair -> pair.getSecond().valueStreamCodec().map(
                    v -> new Value.ConfigValue<>(pair.getSecond(), pair.getFirst(), cast(v)),
                    c -> cast(c.get())
            )
    ).map(UpdateItemConfigPacket::new, packet -> packet.value);

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object object) {
        return (T) object;
    }

    void apply(NetworkManager.PacketContext context) {
        apply(value.getId(), value);
    }

    private <T> void apply(String key, Value.ConfigValue<T> value) {
        ItemConfigs.getValues(value.type()).get(key).set(value.get());
    }

    @Override
    public CustomPacketPayload.Type<UpdateItemConfigPacket> type() {
        return TYPE;
    }
}
