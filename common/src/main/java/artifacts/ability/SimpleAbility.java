package artifacts.ability;

import artifacts.config.value.Value;
import artifacts.config.value.ValueTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public record SimpleAbility(Supplier<Type<SimpleAbility>> type, Value<Boolean> enabled) implements ArtifactAbility {

    public static Type<SimpleAbility> createType() {
        AtomicReference<Type<SimpleAbility>> type = new AtomicReference<>();
        type.set(new Type<>(codec(type::get), streamCodec(type::get)));
        return type.get();
    }

    private static MapCodec<SimpleAbility> codec(Supplier<Type<SimpleAbility>> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ValueTypes.enabledField().forGetter(SimpleAbility::enabled)
        ).apply(instance, value -> new SimpleAbility(type, value)));
    }

    private static StreamCodec<ByteBuf, SimpleAbility> streamCodec(Supplier<Type<SimpleAbility>> type) {
        return StreamCodec.composite(
                ValueTypes.BOOLEAN.streamCodec(),
                SimpleAbility::enabled,
                value -> new SimpleAbility(type, value)
        );
    }

    @Override
    public Type<?> getType() {
        return type.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return enabled.get();
    }
}
