package artifacts.ability.value;

import artifacts.registry.ModGameRules;
import com.google.common.base.CaseFormat;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Supplier;

public interface IntegerValue extends Supplier<Integer> {

    static Pair<MapCodec<IntegerValue>, StreamCodec<ByteBuf, IntegerValue>> codecs(ModGameRules.IntegerGameRule gameRule) {
        return Pair.of(field(gameRule, getFieldName(gameRule)), defaultStreamCodec(gameRule));
    }

    static Pair<MapCodec<IntegerValue>, StreamCodec<ByteBuf, IntegerValue>> codecs(ModGameRules.IntegerGameRule gameRule, String fieldName) {
        return Pair.of(field(gameRule, fieldName), defaultStreamCodec(gameRule));
    }

    static MapCodec<IntegerValue> field(ModGameRules.IntegerGameRule gameRule) {
        return field(gameRule, getFieldName(gameRule));
    }

    static MapCodec<IntegerValue> field(ModGameRules.IntegerGameRule gameRule, String fieldName) {
        return codec(gameRule.max(), gameRule.multiplier()).optionalFieldOf(fieldName, gameRule);
    }

    static String getFieldName(ModGameRules.IntegerGameRule gameRule) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, ModGameRules.INTEGER_VALUES.inverse().get(gameRule).split("\\.")[2]);
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
