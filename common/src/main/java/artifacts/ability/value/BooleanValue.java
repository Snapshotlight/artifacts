package artifacts.ability.value;

import artifacts.registry.ModGameRules;
import artifacts.util.ModCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.function.Supplier;

public interface BooleanValue extends Supplier<Boolean> {

    BooleanValue TRUE = new Constant(true);

    Codec<ModGameRules.BooleanGameRule> GAMERULE_CODEC = new StringRepresentable.StringRepresentableCodec<>(
            ModGameRules.BOOLEAN_VALUES_LIST.toArray(ModGameRules.BooleanGameRule[]::new),
            ModGameRules.BOOLEAN_VALUES::get,
            ModGameRules.BOOLEAN_VALUES_LIST::indexOf
    );

    static Codec<BooleanValue> codec() {
        return ModCodecs.xorAlternative(GAMERULE_CODEC.flatXmap(
                DataResult::success,
                value -> value instanceof ModGameRules.BooleanGameRule gameRule
                        ? DataResult.success(gameRule)
                        : DataResult.error(() -> "Not a game rule")
        ), BooleanValue.constantCodec());
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    static StreamCodec<ByteBuf, BooleanValue> streamCodec() {
        return ByteBufCodecs.BOOL.dispatch(
                ModGameRules.BOOLEAN_VALUES_LIST::contains,
                b -> b ? ByteBufCodecs.idMapper(ModGameRules.BOOLEAN_VALUES_LIST::get, ModGameRules.BOOLEAN_VALUES_LIST::indexOf) : constantStreamCodec()
        );
    }

    static MapCodec<BooleanValue> enabledField(ModGameRules.BooleanGameRule gameRule) {
        return field("enabled", gameRule);
    }

    static MapCodec<BooleanValue> field(String fieldName, ModGameRules.BooleanGameRule gameRule) {
        return Codec.BOOL.<BooleanValue>flatXmap(bool -> DataResult.success(new Constant(bool)), value -> value == gameRule
                        ? DataResult.error(() -> "Cannot convert game rule to constant")
                        : DataResult.success(value.get()))
                .optionalFieldOf(fieldName, gameRule);
    }

    static Codec<BooleanValue> constantCodec() {
        return Codec.BOOL.xmap(Constant::new, Supplier::get);
    }

    static StreamCodec<ByteBuf, BooleanValue> defaultStreamCodec(ModGameRules.BooleanGameRule gameRule) {
        return ByteBufCodecs.BOOL.dispatch(
                value -> value == gameRule,
                b -> b ? StreamCodec.unit(gameRule) : constantStreamCodec()
        );
    }

    static StreamCodec<ByteBuf, BooleanValue> constantStreamCodec() {
        return ByteBufCodecs.BOOL.map(BooleanValue.Constant::new, Supplier::get);
    }

    record Constant(Boolean get) implements BooleanValue {

    }
}
