package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;
import java.util.function.Supplier;

public class IncreaseEnchantmentLevelAbility implements ArtifactAbility {

    private final Enchantment enchantment;
    private final Supplier<Integer> amount;

    private IncreaseEnchantmentLevelAbility(Enchantment enchantment, Supplier<Integer> amount) {
        this.enchantment = enchantment;
        this.amount = amount;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public int getAmount() {
        return amount.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.INCREASE_ENCHANTMENT_LEVEL.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return amount.get() > 0;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        String enchantmentName = BuiltInRegistries.ENCHANTMENT.getKey(enchantment).getPath();
        if (getAmount() == 1) {
            tooltip.add(tooltipLine("%s.single_level".formatted(enchantmentName)));
        } else {
            tooltip.add(tooltipLine("%s.multiple_levels".formatted(enchantmentName), getAmount()));
        }
    }

    public static IncreaseEnchantmentLevelAbility fortune() {
        return new IncreaseEnchantmentLevelAbility(Enchantments.BLOCK_FORTUNE, ModGameRules.LUCKY_SCARF_FORTUNE_BONUS);
    }

    public static IncreaseEnchantmentLevelAbility looting() {
        return new IncreaseEnchantmentLevelAbility(Enchantments.MOB_LOOTING, ModGameRules.SUPERSTITIOUS_HAT_LOOTING_LEVEL_BONUS);
    }

    public static IncreaseEnchantmentLevelAbility lure() {
        return new IncreaseEnchantmentLevelAbility(Enchantments.FISHING_SPEED, ModGameRules.ANGLERS_HAT_LURE_LEVEL_BONUS);
    }

    public static IncreaseEnchantmentLevelAbility luckOfTheSea() {
        return new IncreaseEnchantmentLevelAbility(Enchantments.FISHING_LUCK, ModGameRules.ANGLERS_HAT_LUCK_OF_THE_SEA_LEVEL_BONUS);
    }
}
