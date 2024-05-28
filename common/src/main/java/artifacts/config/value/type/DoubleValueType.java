package artifacts.config.value.type;

import artifacts.config.AbstractConfigManager;
import artifacts.config.value.Value;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;

public class DoubleValueType extends NumberValueType<Double> {

    public DoubleValueType(Double min, Double max, Codec<Double> valueCodec, StreamCodec<ByteBuf, Double> valueStreamCodec) {
        super(min, max, valueCodec, valueStreamCodec);
    }

    @Override
    public FieldBuilder<?, ?, ?> createConfigEntry(AbstractConfigManager config, ConfigEntryBuilder entryBuilder, Component title, Value.ConfigValue<Double> value) {
        return entryBuilder.startDoubleField(title, config.get(value.type(), value.getId()))
                .setDefaultValue(value.getDefaultValue())
                .setSaveConsumer(v -> config.set(value.type(), value.getId(), v))
                .setMin(getMin())
                .setMax(getMax());
    }
}
