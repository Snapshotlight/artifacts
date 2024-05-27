package artifacts.config.value;

import artifacts.config.value.type.ValueType;
import net.minecraft.util.StringRepresentable;

import java.util.function.Supplier;

public interface Value<T> extends Supplier<T> {

    record Constant<T>(T get) implements Value<T> {

        public static final Constant<Boolean> FALSE = new Constant<>(false);
        public static final Constant<Boolean> TRUE = new Constant<>(true);

        public static final Constant<Integer> ZERO = new Constant<>(0);
        public static final Constant<Integer> ONE = new Constant<>(1);

        public static final Constant<Double> ZERO_D = new Constant<>(0D);
        public static final Constant<Double> ONE_D = new Constant<>(1D);

    }

    final class ConfigValue<T> implements Value<T>, StringRepresentable {

        private final ValueType<T, ?> type;
        private final String id;
        private final T defaultValue;

        private T value;

        public ConfigValue(ValueType<T, ?> type, String id, T defaultValue) {
            this.type = type;
            this.id = id;
            this.defaultValue = this.value = defaultValue;
        }

        public String getId() {
            return id;
        }

        public String getSerializedName() {
            return getId();
        }

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
        }

        public T getDefaultValue() {
            return defaultValue;
        }

        public ValueType<T, ?> type() {
            return type;
        }
    }
}
