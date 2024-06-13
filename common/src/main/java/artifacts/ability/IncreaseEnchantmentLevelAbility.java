package artifacts.ability;

import artifacts.config.value.Value;
import artifacts.config.value.ValueTypes;
import artifacts.registry.ModAbilities;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public record IncreaseEnchantmentLevelAbility(Holder<Enchantment> enchantment, Value<Integer> amount) implements ArtifactAbility {

    public static final List<Enchantment> ALLOWED_ENCHANTMENTS = List.of(
            Enchantments.FORTUNE,
            Enchantments.LOOTING,
            Enchantments.LURE,
            Enchantments.LUCK_OF_THE_SEA
    );

    public static final MapCodec<IncreaseEnchantmentLevelAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENCHANTMENT.holderByNameCodec()
                    .validate(enchantment -> ALLOWED_ENCHANTMENTS.contains(enchantment.value())
                            ? DataResult.success(enchantment)
                            : DataResult.error(() -> "Unsupported enchantment: %s".formatted(enchantment.unwrapKey().orElseThrow().location())))
                    .fieldOf("enchantment").forGetter(IncreaseEnchantmentLevelAbility::enchantment),
            ValueTypes.ENCHANTMENT_LEVEL.codec().optionalFieldOf("level", Value.of(1)).forGetter(IncreaseEnchantmentLevelAbility::amount)
    ).apply(instance, IncreaseEnchantmentLevelAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, IncreaseEnchantmentLevelAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.ENCHANTMENT),
            IncreaseEnchantmentLevelAbility::enchantment,
            ValueTypes.ENCHANTMENT_LEVEL.streamCodec(),
            IncreaseEnchantmentLevelAbility::amount,
            IncreaseEnchantmentLevelAbility::new
    );

    public int getAmount() {
        return amount.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.INCREASE_ENCHANTMENT_LEVEL.value();
    }

    @Override
    public boolean isNonCosmetic() {
        return amount().get() > 0;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        String enchantmentName = BuiltInRegistries.ENCHANTMENT.getKey(enchantment().value()).getPath();
        if (getAmount() == 1) {
            tooltip.add(tooltipLine("%s.single_level".formatted(enchantmentName)));
        } else {
            tooltip.add(tooltipLine("%s.multiple_levels".formatted(enchantmentName), getAmount()));
        }
    }
}
