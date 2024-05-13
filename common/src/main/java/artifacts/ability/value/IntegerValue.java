package artifacts.ability.value;

import artifacts.registry.ModGameRules;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Supplier;

public interface IntegerValue extends Supplier<Integer> {

    IntegerValue ZERO = new IntegerValue.Constant(0);
    IntegerValue ONE = new IntegerValue.Constant(1);

    static MapCodec<IntegerValue> field(String fieldName, ModGameRules.IntegerGameRule gameRule) {
        return codec(gameRule.max(), gameRule.multiplier()).optionalFieldOf(fieldName, gameRule);
    }

    static Codec<IntegerValue> codec(int max, int multiplier) {
        return ExtraCodecs.intRange(0, max)
                .xmap(i -> i * multiplier, i -> i / multiplier)
                .xmap(IntegerValue.Constant::new, Supplier::get);
    }

    static StreamCodec<ByteBuf, IntegerValue> defaultStreamCodec(ModGameRules.IntegerGameRule gameRule) {
        return ByteBufCodecs.BOOL.dispatch(
                value -> value == gameRule,
                b -> b ? StreamCodec.unit(gameRule) : streamCodec()
        );
    }

    static StreamCodec<ByteBuf, IntegerValue> streamCodec() {
        return ByteBufCodecs.INT.map(IntegerValue.Constant::new, Supplier::get);
    }

    record Constant(Integer get) implements IntegerValue {

    }
}
