package artifacts.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class ModCodecs {

    public static Codec<Double> doubleRange(double min, double max) {
        return Codec.DOUBLE.validate(d -> {
            if (d >= min - 1e10-5 && d <= max + 1e10-5) {
                return DataResult.success(d);
            } else {
                return DataResult.error(() -> "Value must be within range [" + min + ";" + max + "]: " + d);
            }
        });
    }

    public static Codec<Double> doubleNonNegative() {
        return Codec.DOUBLE.validate(d -> {
            if (d >= 0) {
                return DataResult.success(d);
            } else {
                return DataResult.error(() -> "Value must be non-negative: " + d);
            }
        });
    }

    public static <T> StreamCodec<ByteBuf, TagKey<T>> tagKeyStreamCodec(ResourceKey<? extends Registry<T>> registry) {
        return ResourceLocation.STREAM_CODEC.map(
                id -> TagKey.create(registry, id),
                TagKey::location
        );
    }

    public static <T> Codec<T> xorAlternative(final Codec<T> codec, final Codec<T> alternative) {
        return new XorAlternativeCodec<>(codec, alternative);
    }

    // see NeoForgeExtraCodecs
    private record XorAlternativeCodec<T>(Codec<T> codec, Codec<T> alternative) implements Codec<T> {
        @Override
        public <T1> DataResult<Pair<T, T1>> decode(final DynamicOps<T1> ops, final T1 input) {
            final DataResult<Pair<T, T1>> result = codec.decode(ops, input);
            if (result.error().isEmpty()) {
                return result;
            } else {
                return alternative.decode(ops, input);
            }
        }

        @Override
        public <T1> DataResult<T1> encode(final T input, final DynamicOps<T1> ops, final T1 prefix) {
            final DataResult<T1> result = codec.encode(input, ops, prefix);
            if (result.error().isEmpty()) {
                return result;
            } else {
                return alternative.encode(input, ops, prefix);
            }
        }

        @Override
        public String toString() {
            return "Alternative[" + codec + ", " + alternative + "]";
        }
    }
}
