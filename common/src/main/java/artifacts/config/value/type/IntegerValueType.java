package artifacts.config.value.type;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class IntegerValueType extends NumberValueType<Integer> {

    public IntegerValueType(Integer min, Integer max, Codec<Integer> valueCodec, StreamCodec<ByteBuf, Integer> valueStreamCodec) {
        super(min, max, valueCodec, valueStreamCodec);
    }
}
