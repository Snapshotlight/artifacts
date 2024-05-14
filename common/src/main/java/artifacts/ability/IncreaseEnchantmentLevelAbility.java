package artifacts.ability;

import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

public record IncreaseEnchantmentLevelAbility(Enchantment enchantment, IntegerValue amount) implements ArtifactAbility {

    public static final MapCodec<IncreaseEnchantmentLevelAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENCHANTMENT.byNameCodec().fieldOf("enchantment").forGetter(IncreaseEnchantmentLevelAbility::enchantment),
            IntegerValue.codec(100).optionalFieldOf("level", IntegerValue.ONE).forGetter(IncreaseEnchantmentLevelAbility::amount)
    ).apply(instance, IncreaseEnchantmentLevelAbility::new));

    public static final StreamCodec<ByteBuf, IncreaseEnchantmentLevelAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.idMapper(BuiltInRegistries.ENCHANTMENT),
            IncreaseEnchantmentLevelAbility::enchantment,
            IntegerValue.streamCodec(),
            IncreaseEnchantmentLevelAbility::amount,
            IncreaseEnchantmentLevelAbility::new
    );

    public int getAmount() {
        return amount.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.INCREASE_ENCHANTMENT_LEVEL.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return amount().get() > 0;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        String enchantmentName = BuiltInRegistries.ENCHANTMENT.getKey(enchantment()).getPath();
        if (getAmount() == 1) {
            tooltip.add(tooltipLine("%s.single_level".formatted(enchantmentName)));
        } else {
            tooltip.add(tooltipLine("%s.multiple_levels".formatted(enchantmentName), getAmount()));
        }
    }
}
