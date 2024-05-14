package artifacts.ability.value;

import artifacts.registry.ModGameRules;
import artifacts.util.ModCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

import java.util.function.Supplier;

// TODO remove game rule implements
public interface IntegerValue extends Supplier<Integer> {

    IntegerValue ZERO = new Constant(0);
    IntegerValue ONE = new Constant(1);

    Codec<ModGameRules.IntegerGameRule> GAMERULE_CODEC = new StringRepresentable.StringRepresentableCodec<>(
            ModGameRules.INTEGER_GAME_RULES.toArray(ModGameRules.IntegerGameRule[]::new),
            ModGameRules.INTEGER_VALUES::get,
            ModGameRules.INTEGER_GAME_RULES::indexOf
    );

    static Codec<IntegerValue> durationSecondsCodec() {
        return codec(20 * 60 * 60, 20);
    }

    static Codec<IntegerValue> mobEffectLevelCodec() {
        return codec(128);
    }

    static Codec<IntegerValue> codec() {
        return codec(Integer.MAX_VALUE);
    }

    static Codec<IntegerValue> codec(int max) {
        return codec(max, 1);
    }

    static Codec<IntegerValue> codec(int max, int multiplier) {
        return ModCodecs.xorAlternative(GameRuleValue.CODEC.flatXmap(
                DataResult::success,
                value -> value instanceof GameRuleValue gameRule
                        ? DataResult.success(gameRule)
                        : DataResult.error(() -> "Not a game rule")
        ), Constant.codec(max, multiplier));
    }

    static StreamCodec<ByteBuf, IntegerValue> streamCodec() {
        return ByteBufCodecs.BOOL.dispatch(
                value -> value instanceof GameRuleValue,
                // TODO why does this work? isn't a cast needed here?
                b -> b ? GameRuleValue.STREAM_CODEC : Constant.STREAM_CODEC
        );
    }

    record Constant(Integer get) implements IntegerValue {

        public static StreamCodec<ByteBuf, IntegerValue> STREAM_CODEC = ByteBufCodecs.INT.map(Constant::new, Supplier::get);

        public static Codec<IntegerValue> codec(int max, int multiplier) {
            return ExtraCodecs.intRange(0, max)
                    .xmap(i -> i * multiplier, i -> i / multiplier)
                    .xmap(Constant::new, Supplier::get);
        }
    }

    record GameRuleValue(ModGameRules.IntegerGameRule gameRule, int max, int multiplier) implements IntegerValue {

        public static final Codec<GameRuleValue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                GAMERULE_CODEC.fieldOf("gamerule").forGetter(GameRuleValue::gameRule),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(GameRuleValue::max),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("multiplier", 1).forGetter(GameRuleValue::multiplier)
        ).apply(instance, GameRuleValue::new));

        public static final StreamCodec<ByteBuf, GameRuleValue> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.idMapper(ModGameRules.INTEGER_GAME_RULES::get, ModGameRules.INTEGER_GAME_RULES::indexOf),
                GameRuleValue::gameRule,
                ByteBufCodecs.INT,
                GameRuleValue::max,
                ByteBufCodecs.INT,
                GameRuleValue::multiplier,
                GameRuleValue::new
        );

        @Override
        public Integer get() {
            return Math.min(max, Math.max(0, gameRule.get())) * multiplier;
        }
    }
}
