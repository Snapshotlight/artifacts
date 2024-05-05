package artifacts.ability.value;

import artifacts.registry.ModGameRules;
import com.google.common.base.CaseFormat;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;

public interface BooleanValue extends Supplier<Boolean> {

    static MapCodec<BooleanValue> codec(ModGameRules.BooleanGameRule gameRule) {
        String fieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, gameRule.getKey().getId().split("\\.")[2]);
        return Codec.BOOL.<BooleanValue>flatXmap(bool -> DataResult.success(new Constant(bool)), value -> value == gameRule
                        ? DataResult.error(() -> "Cannot convert game rule to constant")
                        : DataResult.success(value.get()))
                .optionalFieldOf(fieldName, gameRule);
    }

    record Constant(Boolean get) implements BooleanValue {

    }
}
