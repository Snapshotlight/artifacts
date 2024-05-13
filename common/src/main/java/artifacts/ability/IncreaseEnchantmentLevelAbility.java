package artifacts.ability;

import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.ModCodecs;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public record IncreaseEnchantmentLevelAbility(Enchantment enchantment, IntegerValue amount) implements ArtifactAbility {

    private static final List<ModGameRules.IntegerGameRule> LEVEL_GAME_RULES = List.of(
            ModGameRules.LUCKY_SCARF_FORTUNE_BONUS,
            ModGameRules.SUPERSTITIOUS_HAT_LOOTING_LEVEL_BONUS,
            ModGameRules.ANGLERS_HAT_LURE_LEVEL_BONUS,
            ModGameRules.ANGLERS_HAT_LUCK_OF_THE_SEA_LEVEL_BONUS
    );

    private static final StringRepresentable.StringRepresentableCodec<ModGameRules.IntegerGameRule> LEVEL_CODEC = new StringRepresentable.StringRepresentableCodec<>(
            LEVEL_GAME_RULES.toArray(ModGameRules.IntegerGameRule[]::new),
            ModGameRules.INTEGER_VALUES::get,
            LEVEL_GAME_RULES::indexOf
    );

    public static final MapCodec<IncreaseEnchantmentLevelAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENCHANTMENT.byNameCodec().fieldOf("enchantment").forGetter(IncreaseEnchantmentLevelAbility::enchantment),
            ModCodecs.xorAlternative(LEVEL_CODEC.stable().flatXmap(
                    DataResult::success,
                    value -> value instanceof ModGameRules.IntegerGameRule gameRule
                            ? DataResult.success(gameRule)
                            : DataResult.error(() -> "Not a game rule")
            ), IntegerValue.constantCodec(100, 1)).fieldOf("level").forGetter(IncreaseEnchantmentLevelAbility::amount)
    ).apply(instance, IncreaseEnchantmentLevelAbility::new));

    @SuppressWarnings("SuspiciousMethodCalls")
    public static final StreamCodec<ByteBuf, IncreaseEnchantmentLevelAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.idMapper(BuiltInRegistries.ENCHANTMENT),
            IncreaseEnchantmentLevelAbility::enchantment,
            ByteBufCodecs.BOOL.dispatch(
                    LEVEL_GAME_RULES::contains,
                    b -> b ? ByteBufCodecs.idMapper(LEVEL_GAME_RULES::get, LEVEL_GAME_RULES::indexOf) : IntegerValue.constantStreamCodec()
            ),
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

    public static IncreaseEnchantmentLevelAbility fortune() {
        return new IncreaseEnchantmentLevelAbility(Enchantments.FORTUNE, ModGameRules.LUCKY_SCARF_FORTUNE_BONUS);
    }

    public static IncreaseEnchantmentLevelAbility looting() {
        return new IncreaseEnchantmentLevelAbility(Enchantments.LOOTING, ModGameRules.SUPERSTITIOUS_HAT_LOOTING_LEVEL_BONUS);
    }

    public static IncreaseEnchantmentLevelAbility lure() {
        return new IncreaseEnchantmentLevelAbility(Enchantments.LURE, ModGameRules.ANGLERS_HAT_LURE_LEVEL_BONUS);
    }

    public static IncreaseEnchantmentLevelAbility luckOfTheSea() {
        return new IncreaseEnchantmentLevelAbility(Enchantments.LUCK_OF_THE_SEA, ModGameRules.ANGLERS_HAT_LUCK_OF_THE_SEA_LEVEL_BONUS);
    }
}
