package artifacts.config;

import artifacts.config.value.Value;
import artifacts.config.value.type.EnumValueType;
import artifacts.config.value.type.NumberValueType;
import artifacts.config.value.type.ValueType;
import com.electronwill.nightconfig.core.ConfigSpec;
import net.minecraft.util.StringRepresentable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemConfigsManager extends ConfigManager {

    protected static ItemConfigsManager INSTANCE;

    protected ItemConfigsManager() {
        super("items");
    }

    public static void setup() {
        INSTANCE = new ItemConfigsManager();
    }

    @Override
    protected Map<String, Value.ConfigValue<?>> getValueMap() {
        Map<String, Value.ConfigValue<?>> result = new HashMap<>();
        for (ValueType<?, ?> type : ItemConfigs.getValueTypes()) {
            result.putAll(ItemConfigs.getValues(type));
        }
        return result;
    }

    @Override
    protected List<String> getTooltips(String key) {
        return ItemConfigs.getTooltips(key);
    }

    @Override
    protected ConfigSpec createConfigSpec() {
        ConfigSpec spec = new ConfigSpec();
        for (ValueType<?, ?> type : ItemConfigs.getValueTypes()) {
            for (String key : ItemConfigs.getValues(type).keySet()) {
                addToSpec(spec, key, type);
            }
        }
        return spec;
    }

    protected <T> void addToSpec(ConfigSpec spec, String key, ValueType<T, ?> type) {
        if (type instanceof NumberValueType<?> numberValueType) {
            defineNumber(spec, key, numberValueType);
        } else if (type instanceof EnumValueType<?> enumValueType) {
            defineStringRepresentable(spec, key, enumValueType);
        } else {
            defineValue(spec, key, type);
        }
    }

    private <T extends Number & Comparable<T>> void defineNumber(ConfigSpec spec, String key, NumberValueType<T> type) {
        Value.ConfigValue<T> value = ItemConfigs.getValues(type).get(key);
        spec.defineInRange(key, value.getDefaultValue(), type.getMin(), type.getMax());
    }

    private <T extends Enum<T> & StringRepresentable> void defineStringRepresentable(ConfigSpec spec, String key, EnumValueType<T> type) {
        Value.ConfigValue<T> value = ItemConfigs.getValues(type).get(key);
        List<String> allowedValues = new ArrayList<>();
        allowedValues.addAll(type.getValues().stream().map(StringRepresentable::getSerializedName).toList());
        allowedValues.addAll(type.getValues().stream().map(StringRepresentable::getSerializedName).map(String::toUpperCase).toList());
        spec.defineInList(key, value.getDefaultValue().getSerializedName(), allowedValues);
    }

    private <T> void defineValue(ConfigSpec spec, String key, ValueType<T, ?> type) {
        Value.ConfigValue<T> value = ItemConfigs.getValues(type).get(key);
        spec.define(key, value.getDefaultValue());
    }
}
