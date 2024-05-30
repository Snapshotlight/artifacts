package artifacts.util;

import com.google.common.base.Suppliers;
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

import java.util.function.Supplier;

public class ModCodecs {

    public static <T> StreamCodec<ByteBuf, TagKey<T>> tagKeyStreamCodec(ResourceKey<? extends Registry<T>> registry) {
        return ResourceLocation.STREAM_CODEC.map(
                id -> TagKey.create(registry, id),
                TagKey::location
        );
    }

    public static <T> Codec<T> xorAlternative(final Codec<T> codec, final Codec<T> alternative) {
        return new XorAlternativeCodec<>(codec, alternative);
    }

    public static <T> Codec<T> lazyCodec(Supplier<Codec<T>> codec) {
        return new LazyCodec<>(codec);
    }

    public static <B, T> StreamCodec<B, T> lazyStreamCodec(Supplier<StreamCodec<B, T>> codec) {
        return new LazyStreamCodec<>(codec);
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

    private record LazyCodec<V>(Supplier<Codec<V>> codec) implements Codec<V> {

        private LazyCodec(Supplier<Codec<V>> codec) {
            this.codec = Suppliers.memoize(codec::get);
        }

        @Override
        public <T1> DataResult<Pair<V, T1>> decode(DynamicOps<T1> ops, T1 input) {
            return codec.get().decode(ops, input);
        }

        @Override
        public <T1> DataResult<T1> encode(V input, DynamicOps<T1> ops, T1 prefix) {
            return codec.get().encode(input, ops, prefix);
        }
    }

    private record LazyStreamCodec<B, V>(Supplier<StreamCodec<B, V>> codec) implements StreamCodec<B, V> {

        private LazyStreamCodec(Supplier<StreamCodec<B, V>> codec) {
            this.codec = Suppliers.memoize(codec::get);
        }

        @Override
        public V decode(B buffer) {
            return codec.get().decode(buffer);
        }

        @Override
        public void encode(B buffer, V value) {
            codec.get().encode(buffer, value);
        }
    }
}
