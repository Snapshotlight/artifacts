package artifacts.ability.value;

import artifacts.registry.ModGameRules;
import com.google.common.base.CaseFormat;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public interface BooleanValue extends Supplier<Boolean> {

    static Pair<MapCodec<BooleanValue>, StreamCodec<ByteBuf, BooleanValue>> codecs(ModGameRules.BooleanGameRule gameRule) {
        return Pair.of(field(gameRule, getFieldName(gameRule)), defaultStreamCodec(gameRule));
    }

    static Pair<MapCodec<BooleanValue>, StreamCodec<ByteBuf, BooleanValue>> codecs(ModGameRules.BooleanGameRule gameRule, String fieldName) {
        return Pair.of(field(gameRule, fieldName), defaultStreamCodec(gameRule));
    }

    static MapCodec<BooleanValue> field(ModGameRules.BooleanGameRule gameRule) {
        return field(gameRule, getFieldName(gameRule));
    }

    static MapCodec<BooleanValue> field(ModGameRules.BooleanGameRule gameRule, String fieldName) {
        return Codec.BOOL.<BooleanValue>flatXmap(bool -> DataResult.success(new Constant(bool)), value -> value == gameRule
                        ? DataResult.error(() -> "Cannot convert game rule to constant")
                        : DataResult.success(value.get()))
                .optionalFieldOf(fieldName, gameRule);
    }

    static String getFieldName(ModGameRules.BooleanGameRule gameRule) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, ModGameRules.BOOLEAN_VALUES.inverse().get(gameRule).split("\\.")[2]);
    }

    static StreamCodec<ByteBuf, BooleanValue> defaultStreamCodec(ModGameRules.BooleanGameRule gameRule) {
        return ByteBufCodecs.BOOL.dispatch(
                value -> value == gameRule,
                b -> b ? StreamCodec.unit(gameRule) : streamCodec()
        );
    }

    static StreamCodec<ByteBuf, BooleanValue> streamCodec() {
        return ByteBufCodecs.BOOL.map(BooleanValue.Constant::new, Supplier::get);
    }

    record Constant(Boolean get) implements BooleanValue {

    }
}
