package artifacts.config.value.type;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.List;

public class StringRepresentableValueType<T extends StringRepresentable> extends ValueType<T, String> {

    private final T[] values;

    public StringRepresentableValueType(T[] values) {
        this.values = values;
    }

    public List<T> getValues() {
        return Arrays.asList(values);
    }

    @Override
    public boolean isCorrect(StringRepresentable value) {
        return true;
    }

    @Override
    public String makeError(StringRepresentable value) {
        return "";
    }

    @Override
    public String getAllowedValuesComment() {
        StringBuilder builder = new StringBuilder("Allowed Values: ");
        builder.append(values[0].getSerializedName());
        for (int i = 1; i < values.length; i++) {
            builder.append(", ").append(values[i].getSerializedName());
        }
        return builder.toString();
    }

    @Override
    public T read(String configValue) {
        for (T value : values) {
            if (value.getSerializedName().equalsIgnoreCase(configValue)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown value '%s'".formatted(configValue));
    }

    @Override
    protected Codec<T> valueCodec() {
        return StringRepresentable.fromValues(() -> values);
    }

    @Override
    public StreamCodec<ByteBuf, T> valueStreamCodec() {
        return ByteBufCodecs.idMapper(i -> values[i], Util.createIndexLookup(Arrays.asList(values)));
    }
}
