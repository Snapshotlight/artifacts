package artifacts.ability.value;

import artifacts.registry.ModGameRules;
import artifacts.util.ModCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public interface DoubleValue extends Supplier<Double> {

    DoubleValue ZERO = new Constant(0D);
    DoubleValue ONE = new Constant(1D);

    static Codec<DoubleValue> percentage() {
        return codec(100, 100);
    }

    static Codec<DoubleValue> codec() {
        return codec(1);
    }

    static Codec<DoubleValue> codec(int factor) {
        return codec(Integer.MAX_VALUE, factor);
    }

    static Codec<DoubleValue> codec(int max, double factor) {
        return ModCodecs.xorAlternative(IntegerValue.GAMERULE_CODEC.flatXmap(
                gameRule -> DataResult.success(new GameRuleValue(gameRule, max, factor)),
                value -> value instanceof GameRuleValue gameRule
                        ? DataResult.success(gameRule.gameRule())
                        : DataResult.error(() -> "Not a game rule")
        ), Constant.codec(max, factor));
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    static StreamCodec<ByteBuf, DoubleValue> streamCodec() {
        return ByteBufCodecs.BOOL.dispatch(
                ModGameRules.INTEGER_GAME_RULES::contains,
                b -> b ? GameRuleValue.STREAM_CODEC : Constant.STREAM_CODEC
        );
    }

    default boolean fuzzyEquals(double i) {
        return Math.abs(get() - i) < 1e-10;
    }

    record Constant(Double get) implements DoubleValue {

        static StreamCodec<ByteBuf, DoubleValue> STREAM_CODEC =  ByteBufCodecs.DOUBLE.map(Constant::new, Supplier::get);

        static Codec<DoubleValue> codec(int max, double factor) {
            if (max == Integer.MAX_VALUE) {
                return ModCodecs.doubleNonNegative().xmap(Constant::new, Supplier::get);
            }
            return ModCodecs.doubleRange(max / factor).xmap(Constant::new, Supplier::get);
        }
    }

    record GameRuleValue(ModGameRules.IntegerGameRule gameRule, int max, double factor) implements DoubleValue {

        public static Codec<GameRuleValue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                IntegerValue.GAMERULE_CODEC.fieldOf("gamerule").forGetter(GameRuleValue::gameRule),
                Codec.INT.fieldOf("max").forGetter(GameRuleValue::max),
                Codec.DOUBLE.fieldOf("factor").forGetter(GameRuleValue::factor)
        ).apply(instance, GameRuleValue::new));

        public static StreamCodec<ByteBuf, GameRuleValue> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.idMapper(ModGameRules.INTEGER_GAME_RULES::get, ModGameRules.INTEGER_GAME_RULES::indexOf),
                GameRuleValue::gameRule,
                ByteBufCodecs.INT,
                GameRuleValue::max,
                ByteBufCodecs.DOUBLE,
                GameRuleValue::factor,
                GameRuleValue::new
        );

        @Override
        public Double get() {
            return Math.min(max, Math.max(0, gameRule.get())) / factor;
        }

        public static GameRuleValue of(ModGameRules.IntegerGameRule gameRule, int max, double factor) {
            return new GameRuleValue(gameRule, max, factor);
        }
    }
}
