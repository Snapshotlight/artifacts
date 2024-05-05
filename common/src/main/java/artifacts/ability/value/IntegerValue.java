package artifacts.ability.value;

import artifacts.registry.ModGameRules;
import com.google.common.base.CaseFormat;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Supplier;

public interface IntegerValue extends Supplier<Integer> {

    static MapCodec<IntegerValue> codec(ModGameRules.IntegerGameRule gameRule) {
        int max = gameRule.max();
        int multiplier = gameRule.multiplier();
        String fieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, gameRule.getKey().getId().split("\\.")[2]);
        return ExtraCodecs.intRange(0, max).xmap(i -> i * multiplier, i -> i / multiplier)
                .<IntegerValue>xmap(IntegerValue.Constant::new, Supplier::get)
                .optionalFieldOf(fieldName, gameRule);
    }

    record Constant(Integer get) implements IntegerValue {

    }
}
