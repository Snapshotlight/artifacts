package artifacts.ability.value;

import artifacts.registry.ModGameRules;
import com.google.common.base.CaseFormat;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Supplier;

public interface DoubleValue extends Supplier<Double> {

    static MapCodec<DoubleValue> codec(ModGameRules.DoubleGameRule gameRule) {
        int max = gameRule.integerGameRule().max();
        int multiplier = gameRule.integerGameRule().multiplier();
        String fieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, gameRule.integerGameRule().getKey().getId().split("\\.")[2]);
        return ExtraCodecs.intRange(0, max)
                .xmap(i -> i * multiplier, i -> i / multiplier)
                .xmap(Integer::doubleValue, Double::intValue)
                .xmap(d -> d / gameRule.factor(), d -> d * gameRule.factor())
                .<DoubleValue>xmap(DoubleValue.Constant::new, Supplier::get)
                .optionalFieldOf(fieldName, gameRule);
    }

    record Constant(Double get) implements DoubleValue {

    }
}
