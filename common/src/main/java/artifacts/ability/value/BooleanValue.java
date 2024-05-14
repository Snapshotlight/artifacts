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
    BooleanValue FALSE = new Constant(false);

    Codec<ModGameRules.BooleanGameRule> GAMERULE_CODEC = new StringRepresentable.StringRepresentableCodec<>(
            ModGameRules.BOOLEAN_GAME_RULES.toArray(ModGameRules.BooleanGameRule[]::new),
            ModGameRules.BOOLEAN_VALUES::get,
            ModGameRules.BOOLEAN_GAME_RULES::indexOf
    );

    static MapCodec<BooleanValue> enabledField() {
        return codec().optionalFieldOf("enabled", TRUE);
    }

    static Codec<BooleanValue> codec() {
        return ModCodecs.xorAlternative(GAMERULE_CODEC.flatXmap(
                DataResult::success,
                value -> value instanceof ModGameRules.BooleanGameRule gameRule
                        ? DataResult.success(gameRule)
                        : DataResult.error(() -> "Not a game rule")
        ), Constant.CODEC);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    static StreamCodec<ByteBuf, BooleanValue> streamCodec() {
        return ByteBufCodecs.BOOL.dispatch(
                ModGameRules.BOOLEAN_GAME_RULES::contains,
                b -> b ? ByteBufCodecs.idMapper(ModGameRules.BOOLEAN_GAME_RULES::get, ModGameRules.BOOLEAN_GAME_RULES::indexOf) : Constant.STREAM_CODEC
        );
    }

    record Constant(Boolean get) implements BooleanValue {

        public static Codec<BooleanValue> CODEC = Codec.BOOL.xmap(Constant::new, Supplier::get);
        public static StreamCodec<ByteBuf, BooleanValue> STREAM_CODEC = ByteBufCodecs.BOOL.map(BooleanValue.Constant::new, Supplier::get);

    }
}
