package artifacts.item.wearable.head;

import artifacts.ability.ReduceEatingDurationAbility;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModGameRules;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

import java.util.List;

public class DrinkingHatItem extends WearableArtifactItem {

    private final boolean hasSpecialTooltip;

    public DrinkingHatItem(ModGameRules.DoubleValue drinkingDurationMultiplier, ModGameRules.DoubleValue eatingDurationMultiplier, boolean hasSpecialTooltip) {
        super(
                new ReduceEatingDurationAbility(drinkingDurationMultiplier, UseAnim.DRINK),
                new ReduceEatingDurationAbility(eatingDurationMultiplier, UseAnim.EAT)
        );
        this.hasSpecialTooltip = hasSpecialTooltip;
    }

    @Override
    protected void addTooltip(ItemStack stack, List<MutableComponent> tooltip) {
        if (hasSpecialTooltip) {
            tooltip.add(tooltipLine("special").withStyle(ChatFormatting.ITALIC));
            addEffectsTooltip(stack, tooltip);
        } else {
            super.addTooltip(stack, tooltip);
        }
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.BOTTLE_FILL;
    }
}
