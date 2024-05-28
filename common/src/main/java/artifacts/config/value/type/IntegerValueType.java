package artifacts.config.value.type;

import artifacts.config.value.Value;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;

public class IntegerValueType extends NumberValueType<Integer> {

    public IntegerValueType(Integer min, Integer max, Codec<Integer> valueCodec, StreamCodec<ByteBuf, Integer> valueStreamCodec) {
        super(min, max, valueCodec, valueStreamCodec);
    }

    @Override
    public FieldBuilder<?, ?, ?> createConfigEntry(ConfigEntryBuilder entryBuilder, Component title, Value.ConfigValue<Integer> value) {
        return entryBuilder.startIntField(title, value.get())
                .setDefaultValue(value.getDefaultValue())
                .setSaveConsumer(value::set)
                .setMin(getMin())
                .setMax(getMax());
    }
}
