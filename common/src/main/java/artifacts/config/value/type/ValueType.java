package artifacts.config.value.type;

import artifacts.config.ItemConfigs;
import artifacts.config.value.Value;
import artifacts.util.ModCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ValueType<T, C> {

    public abstract boolean isCorrect(T value);

    public abstract String makeError(T value);

    public abstract String getAllowedValuesComment();

    public abstract T read(C c);

    public final Codec<Value<T>> codec() {
        return ModCodecs.xorAlternative(
                configCodec().flatXmap(
                        DataResult::success,
                        value -> value instanceof Value.ConfigValue<T> configValue && ItemConfigs.getValues(this).containsKey(configValue.getId())
                                ? DataResult.success(configValue)
                                : DataResult.error(() -> "Not a valid config value: %s".formatted(value))
                ),
                constantCodec()
        );
    }

    protected abstract Codec<T> valueCodec();

    public final Codec<Value<T>> constantCodec() {
        return valueCodec().comapFlatMap(
                value -> isCorrect(value) ? DataResult.success(value) : DataResult.error(() -> makeError(value)),
                Function.identity()
        ).xmap(Value.Constant::new, Supplier::get);
    }

    public final Codec<Value.ConfigValue<T>> configCodec() {
        return StringRepresentable.fromValues(this::getConfigValues);
    }

    public final StreamCodec<ByteBuf, Value<T>> streamCodec() {
        return ByteBufCodecs.BOOL.dispatch(
                value -> value instanceof Value.ConfigValue<?>,
                b -> b ? configStreamCodec() : valueStreamCodec().map(Value.Constant::new, Supplier::get)
        );
    }

    public abstract StreamCodec<ByteBuf, T> valueStreamCodec();

    public final StreamCodec<ByteBuf, Value.ConfigValue<T>> configStreamCodec() {
        Value.ConfigValue<T>[] values = getConfigValues();
        return ByteBufCodecs.idMapper(i -> values[i], Util.createIndexLookup(Arrays.asList(values)));
    }

    @SuppressWarnings("unchecked")
    private Value.ConfigValue<T>[] getConfigValues() {
        List<Value.ConfigValue<T>> values = List.copyOf(ItemConfigs.getValues(this).values());
        // yikes
        Value.ConfigValue<T>[] result = (Value.ConfigValue<T>[]) java.lang.reflect.Array.newInstance(values.get(0).getClass(), values.size());
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }
        return result;
    }
}
