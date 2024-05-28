package artifacts.config;

import artifacts.config.value.Value;
import artifacts.config.value.ValueTypes;
import artifacts.config.value.type.ValueType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ConfigManager extends AbstractConfigManager {

    private final Map<String, Value.ConfigValue<?>> values = new HashMap<>();
    private final Map<String, List<String>> tooltips = new HashMap<>();

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

    protected Value.ConfigValue<Boolean> defineBool(String key, boolean defaultValue, String... tooltips) {
        return define(key, ValueTypes.BOOLEAN, defaultValue, tooltips);
    }

    protected Value.ConfigValue<Integer> defineNonNegativeInt(String key, int defaultValue, String... tooltips) {
        return define(key, ValueTypes.NON_NEGATIVE_INT, defaultValue, tooltips);
    }

    protected Value.ConfigValue<Integer> defineInt(String key, int defaultValue, String... tooltips) {
        return define(key, ValueTypes.INT, defaultValue, tooltips);
    }

    protected Value.ConfigValue<Double> defineFraction(String key, double defaultValue, String... tooltips) {
        return define(key, ValueTypes.FRACTION, defaultValue, tooltips);
    }

    protected <T> Value.ConfigValue<T> define(String key, ValueType<T, ?> type, T defaultValue, String... tooltips) {
        Value.ConfigValue<T> value = new Value.ConfigValue<>(type, key, defaultValue);
        spec.define(key, defaultValue);
        values.put(key, value);
        this.tooltips.put(key, List.of(tooltips));
        return value;
    }
}
