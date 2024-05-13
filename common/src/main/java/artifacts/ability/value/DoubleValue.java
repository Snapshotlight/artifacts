package artifacts.ability.value;

import artifacts.registry.ModGameRules;
import artifacts.util.ModCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

import java.util.function.Supplier;

public interface DoubleValue extends Supplier<Double> {

    Codec<ModGameRules.DoubleGameRule> GAMERULE_CODEC = new StringRepresentable.StringRepresentableCodec<>(
            ModGameRules.DOUBLE_VALUES_LIST.toArray(ModGameRules.DoubleGameRule[]::new),
            ModGameRules.DOUBLE_VALUES::get,
            ModGameRules.DOUBLE_VALUES_LIST::indexOf
    );

    static Codec<DoubleValue> codec() {
        return ModCodecs.xorAlternative(GAMERULE_CODEC.flatXmap(
                DataResult::success,
                value -> value instanceof ModGameRules.DoubleGameRule gameRule
                        ? DataResult.success(gameRule)
                        : DataResult.error(() -> "Not a game rule")
        ), DoubleValue.constantCodec());
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    static StreamCodec<ByteBuf, DoubleValue> streamCodec() {
        return ByteBufCodecs.BOOL.dispatch(
                ModGameRules.INTEGER_VALUES_LIST::contains,
                b -> b ? ByteBufCodecs.idMapper(ModGameRules.DOUBLE_VALUES_LIST::get, ModGameRules.DOUBLE_VALUES_LIST::indexOf) : constantStreamCodec()
        );
    }

    static MapCodec<DoubleValue> field(String fieldName, ModGameRules.DoubleGameRule gameRule) {
        return constantCodec(gameRule.integerGameRule().max(), gameRule.integerGameRule().multiplier(), gameRule.factor()).optionalFieldOf(fieldName, gameRule);
    }

    static Codec<DoubleValue> constantCodec(int max, int multiplier, double factor) {
        return ExtraCodecs.intRange(0, max)
                .xmap(i -> i * multiplier, i -> i / multiplier)
                .xmap(Integer::doubleValue, Double::intValue)
                .xmap(d -> d / factor, d -> d * factor)
                .xmap(DoubleValue.Constant::new, Supplier::get);
    }

    static Codec<DoubleValue> constantCodec() {
        return Codec.DOUBLE.xmap(DoubleValue.Constant::new, Supplier::get);
    }

    static StreamCodec<ByteBuf, DoubleValue> defaultStreamCodec(ModGameRules.DoubleGameRule gameRule) {
        return ByteBufCodecs.BOOL.dispatch(
                value -> value == gameRule,
                b -> b ? StreamCodec.unit(gameRule) : constantStreamCodec()
        );
    }

    static StreamCodec<ByteBuf, DoubleValue> constantStreamCodec() {
        return ByteBufCodecs.DOUBLE.map(DoubleValue.Constant::new, Supplier::get);
    }

    default boolean fuzzyEquals(double i) {
        return Math.abs(get() - i) < 1e-10;
    }

    record Constant(Double get) implements DoubleValue {

    }
}
