package artifacts.config;

import artifacts.config.value.Value;
import artifacts.config.value.ValueTypes;
import artifacts.config.value.type.EnumValueType;
import artifacts.config.value.type.NumberValueType;
import artifacts.config.value.type.ValueType;
import net.minecraft.util.StringRepresentable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SameParameterValue")
public abstract class ConfigManager extends AbstractConfigManager {

    private final Map<String, Value.ConfigValue<?>> values = new HashMap<>();
    private final Map<String, List<String>> tooltips = new HashMap<>();

    private final Map<ValueType<?, ?>, ValueMap<?>> typeToValues = new HashMap<>();

    protected ConfigManager(String fileName) {
        super(fileName);
    }

    @Override
    public Map<String, Value.ConfigValue<?>> getValues() {
        return values;
    }

    @Override
    public List<String> getTooltips(String key) {
        return tooltips.get(key);
    }

    protected Value.ConfigValue<Boolean> defineBool(String key, String... tooltips) {
        return defineBool(key, true, tooltips);
    }

    protected Value.ConfigValue<Boolean> defineBool(String key, boolean defaultValue, String... tooltips) {
        Value.ConfigValue<Boolean> value = createValue(key, ValueTypes.BOOLEAN, defaultValue, tooltips);
        spec.define(key, defaultValue);
        return value;
    }

    protected Value.ConfigValue<Integer> defineNonNegativeInt(String key, int defaultValue, String... tooltips) {
        return defineNumber(key, ValueTypes.NON_NEGATIVE_INT, defaultValue, tooltips);
    }

    protected Value.ConfigValue<Integer> defineInt(String key, int defaultValue, String... tooltips) {
        return defineNumber(key, ValueTypes.INT, defaultValue, tooltips);
    }

    protected Value.ConfigValue<Double> defineAttributeModifier(String key, double defaultValue, String... tooltips) {
        return defineNumber(key, ValueTypes.ATTRIBUTE_MODIFIER_AMOUNT, defaultValue, tooltips);
    }

    protected Value.ConfigValue<Double> defineNonNegativeDouble(String key, double defaultValue, String... tooltips) {
        return defineNumber(key, ValueTypes.NON_NEGATIVE_DOUBLE, defaultValue, tooltips);
    }


    protected Value.ConfigValue<Double> defineFraction(String key, double defaultValue, String... tooltips) {
        return defineNumber(key, ValueTypes.FRACTION, defaultValue, tooltips);
    }

    protected Value.ConfigValue<Integer> defineDuration(String key, int defaultValue, String... tooltips) {
        return defineNumber(key, ValueTypes.DURATION, defaultValue, tooltips);
    }

    protected Value.ConfigValue<Integer> defineEnchantmentLevel(String key, int defaultValue, String... tooltips) {
        return defineNumber(key, ValueTypes.ENCHANTMENT_LEVEL, defaultValue, tooltips);
    }

    protected Value.ConfigValue<Integer> defineMobEffectLevel(String key, int defaultValue, String... tooltips) {
        return defineNumber(key, ValueTypes.MOB_EFFECT_LEVEL, defaultValue, tooltips);
    }

    private <T extends Number & Comparable<T>> Value.ConfigValue<T> defineNumber(String key, NumberValueType<T> type, T defaultValue, String... tooltips) {
        Value.ConfigValue<T> value = createValue(key, type, defaultValue, tooltips);
        spec.defineInRange(key, defaultValue, type.getMin(), type.getMax());
        return value;
    }

    protected <T extends Enum<T> & StringRepresentable> Value.ConfigValue<T> defineEnum(String key, EnumValueType<T> type, T defaultValue, String... tooltips) {
        Value.ConfigValue<T> value = createValue(key, type, defaultValue, tooltips);
        List<String> allowedValues = new ArrayList<>();
        allowedValues.addAll(type.getValues().stream().map(StringRepresentable::getSerializedName).toList());
        allowedValues.addAll(type.getValues().stream().map(StringRepresentable::getSerializedName).map(String::toUpperCase).toList());
        spec.defineInList(key, value.getDefaultValue().getSerializedName(), allowedValues);
        return value;
    }

    protected <T> Value.ConfigValue<T> createValue(String key, ValueType<T, ?> type, T defaultValue, String... tooltips) {
        Value.ConfigValue<T> value = new Value.ConfigValue<>(type, key, defaultValue);
        values.put(key, value);
        this.tooltips.put(key, List.of(tooltips));
        if (!this.typeToValues.containsKey(type)) {
            typeToValues.put(type, new ValueMap<>());
        }
        //noinspection unchecked
        ((ValueMap<T>) typeToValues.get(type)).getMap().put(key, value);
        return value;
    }

    @SuppressWarnings("unchecked")
    public <T> Map<String, Value.ConfigValue<T>> getValues(ValueType<T, ?> type) {
        ValueMap<T> valueMap = (ValueMap<T>) typeToValues.get(type);
        return valueMap.getMap();
    }

    private static class ValueMap<T> {
        private final Map<String, Value.ConfigValue<T>> map = new HashMap<>();

        public Map<String, Value.ConfigValue<T>> getMap() {
            return map;
        }
    }
}
