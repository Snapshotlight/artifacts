package artifacts.ability.value;

import artifacts.registry.ModGameRules;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Supplier;

public interface DoubleValue extends Supplier<Double> {

    static MapCodec<DoubleValue> field(String fieldName, ModGameRules.DoubleGameRule gameRule) {
        return codec(gameRule.integerGameRule().max(), gameRule.integerGameRule().multiplier(), gameRule.factor()).optionalFieldOf(fieldName, gameRule);
    }

    static Codec<DoubleValue> codec(int max, int multiplier, double factor) {
        return ExtraCodecs.intRange(0, max)
                .xmap(i -> i * multiplier, i -> i / multiplier)
                .xmap(Integer::doubleValue, Double::intValue)
                .xmap(d -> d / factor, d -> d * factor)
                .xmap(DoubleValue.Constant::new, Supplier::get);
    }

    static StreamCodec<ByteBuf, DoubleValue> defaultStreamCodec(ModGameRules.DoubleGameRule gameRule) {
        return ByteBufCodecs.BOOL.dispatch(
                value -> value == gameRule,
                b -> b ? StreamCodec.unit(gameRule) : streamCodec()
        );
    }

    static StreamCodec<ByteBuf, DoubleValue> streamCodec() {
        return ByteBufCodecs.DOUBLE.map(DoubleValue.Constant::new, Supplier::get);
    }

    default boolean fuzzyEquals(double i) {
        return Math.abs(get() - i) < 1e-10;
    }

    record Constant(Double get) implements DoubleValue {

    }
}
