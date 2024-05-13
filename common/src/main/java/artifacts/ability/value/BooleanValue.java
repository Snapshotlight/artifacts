package artifacts.ability.value;

import artifacts.registry.ModGameRules;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public interface BooleanValue extends Supplier<Boolean> {

    BooleanValue TRUE = new Constant(true);

    static MapCodec<BooleanValue> enabledField(ModGameRules.BooleanGameRule gameRule) {
        return field("enabled", gameRule);
    }

    static MapCodec<BooleanValue> field(String fieldName, ModGameRules.BooleanGameRule gameRule) {
        return Codec.BOOL.<BooleanValue>flatXmap(bool -> DataResult.success(new Constant(bool)), value -> value == gameRule
                        ? DataResult.error(() -> "Cannot convert game rule to constant")
                        : DataResult.success(value.get()))
                .optionalFieldOf(fieldName, gameRule);
    }

    static Codec<BooleanValue> codec() {
        return Codec.BOOL.xmap(Constant::new, Supplier::get);
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
