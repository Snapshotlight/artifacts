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

import java.util.function.Supplier;

public interface IntegerValue extends Supplier<Integer> {

    IntegerValue ZERO = new IntegerValue.Constant(0);
    IntegerValue ONE = new IntegerValue.Constant(1);

    static Codec<IntegerValue> codec() {
        return ModCodecs.xorAlternative(ModGameRules.INTEGER_CODEC.flatXmap(
                DataResult::success,
                value -> value instanceof ModGameRules.IntegerGameRule gameRule
                        ? DataResult.success(gameRule)
                        : DataResult.error(() -> "Not a game rule")
        ), IntegerValue.constantCodec());
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    static StreamCodec<ByteBuf, IntegerValue> streamCodec() {
        return ByteBufCodecs.BOOL.dispatch(
                ModGameRules.INTEGER_VALUES_LIST::contains,
                b -> b ? ByteBufCodecs.idMapper(ModGameRules.INTEGER_VALUES_LIST::get, ModGameRules.INTEGER_VALUES_LIST::indexOf) : IntegerValue.constantStreamCodec()
        );
    }

    // TODO use gameRuleOrConstant
    static MapCodec<IntegerValue> field(String fieldName, ModGameRules.IntegerGameRule gameRule) {
        return constantCodec(gameRule.max(), gameRule.multiplier()).optionalFieldOf(fieldName, gameRule);
    }

    static Codec<IntegerValue> constantCodec(int max, int multiplier) {
        return ExtraCodecs.intRange(0, max)
                .xmap(i -> i * multiplier, i -> i / multiplier)
                .xmap(IntegerValue.Constant::new, Supplier::get);
    }

    static Codec<IntegerValue> constantCodec() {
        return Codec.INT.xmap(IntegerValue.Constant::new, Supplier::get);
    }

    static StreamCodec<ByteBuf, IntegerValue> defaultStreamCodec(ModGameRules.IntegerGameRule gameRule) {
        return ByteBufCodecs.BOOL.dispatch(
                value -> value == gameRule,
                b -> b ? StreamCodec.unit(gameRule) : constantStreamCodec()
        );
    }

    static StreamCodec<ByteBuf, IntegerValue> constantStreamCodec() {
        return ByteBufCodecs.INT.map(IntegerValue.Constant::new, Supplier::get);
    }

    record Constant(Integer get) implements IntegerValue {

    }
}
