package artifacts.config.screen;

import artifacts.config.AbstractConfigManager;
import artifacts.config.value.Value;
import artifacts.config.value.type.*;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

@SuppressWarnings("unchecked")
public class ConfigEntries {

    protected static <T> FieldBuilder<?, ?, ?> createConfigEntry(AbstractConfigManager config, Value.ConfigValue<T> value, ConfigEntryBuilder builder, Component title) {
        return switch (value.type()) {
            case BooleanValueType b -> createBoolConfigEntry(config, builder, title, ((Value.ConfigValue<Boolean>) value));
            case IntegerValueType i -> createIntConfigEntry(config, builder, title, ((Value.ConfigValue<Integer>) value));
            case DoubleValueType d -> createDoubleConfigEntry(config, builder, title, ((Value.ConfigValue<Double>) value));
            case EnumValueType<?> e -> createEnumConfigEntry(config, builder, title, cast(value));
            default -> throw new IllegalArgumentException();
        };
    }

    private static FieldBuilder<?, ?, ?> createBoolConfigEntry(AbstractConfigManager config, ConfigEntryBuilder entryBuilder, Component title, Value.ConfigValue<Boolean> value) {
        return entryBuilder.startBooleanToggle(title, config.get(value.type(), value.getId()))
                .setDefaultValue(value.getDefaultValue())
                .setSaveConsumer(v -> config.set(value.type(), value.getId(), v));
    }

    private static FieldBuilder<?, ?, ?> createIntConfigEntry(AbstractConfigManager config, ConfigEntryBuilder entryBuilder, Component title, Value.ConfigValue<Integer> value) {
        NumberValueType<Integer> type = cast(value.type());
        return entryBuilder.startIntField(title, config.get(value.type(), value.getId()))
                .setDefaultValue(value.getDefaultValue())
                .setSaveConsumer(v -> config.set(value.type(), value.getId(), v))
                .setMin(type.getMin())
                .setMax(type.getMax());
    }

    private static FieldBuilder<?, ?, ?> createDoubleConfigEntry(AbstractConfigManager config, ConfigEntryBuilder entryBuilder, Component title, Value.ConfigValue<Double> value) {
        NumberValueType<Double> type = cast(value.type());
        return entryBuilder.startDoubleField(title, config.get(value.type(), value.getId()))
                .setDefaultValue(value.getDefaultValue())
                .setSaveConsumer(v -> config.set(value.type(), value.getId(), v))
                .setMin(type.getMin())
                .setMax(type.getMax());
    }

    private static <T extends Enum<T> & StringRepresentable> FieldBuilder<?, ?, ?> createEnumConfigEntry(AbstractConfigManager config, ConfigEntryBuilder entryBuilder, Component title, Value.ConfigValue<T> value) {
        EnumValueType<T> type = cast(value.type());
        return entryBuilder.startEnumSelector(title, cast(value.getDefaultValue().getClass()), config.get(value.type(), value.getId()))
                .setEnumNameProvider(e -> type.getAsComponent((T) e))
                .setDefaultValue(value.getDefaultValue())
                .setSaveConsumer(v -> config.set(value.type(), value.getId(), v));
    }

    private static <T> T cast(Object object) {
        return (T) object;
    }
}
