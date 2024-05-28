package artifacts.config.value.type;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class DoubleValueType extends NumberValueType<Double> {

    public DoubleValueType(Double min, Double max, Codec<Double> valueCodec, StreamCodec<ByteBuf, Double> valueStreamCodec) {
        super(min, max, valueCodec, valueStreamCodec);
    }
}
