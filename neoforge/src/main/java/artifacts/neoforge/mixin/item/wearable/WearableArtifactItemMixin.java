package artifacts.neoforge.mixin.item.wearable;

import artifacts.Artifacts;
import artifacts.item.ArtifactItem;
import artifacts.item.WearableArtifactItem;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

@Mixin(WearableArtifactItem.class)
public abstract class WearableArtifactItemMixin extends ArtifactItem {

    // mimic Curios' attribute tooltip
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltipList, TooltipFlag flags) {
        super.appendHoverText(stack, world, tooltipList, flags);
        Set<String> curioTags = CuriosApi.getItemStackSlots(stack).keySet();
        List<String> slots = new ArrayList<>(curioTags);

        if (!Artifacts.CONFIG.client.showTooltips || slots.isEmpty() || !AbilityHelper.hasAbility(ModAbilities.ATTRIBUTE_MODIFIER, stack)) {
            return;
        }

        tooltipList.add(Component.empty());

        String identifier = slots.contains("curio") ? "curio" : slots.get(0);
        tooltipList.add(Component.translatable("curios.modifiers." + identifier));

        AbilityHelper.getAbilities(ModAbilities.ATTRIBUTE_MODIFIER, stack).forEach(ability -> {
            double amount = ability.getAmount();

            if (ability.getOperation() == AttributeModifier.Operation.ADDITION) {
                if (ability.getAttribute().equals(Attributes.KNOCKBACK_RESISTANCE)) {
                    amount *= 10;
                }
            } else {
                amount *= 100;
            }

            tooltipList.add((Component.translatable(
                    "attribute.modifier.plus." + ability.getOperation().toValue(),
                    ATTRIBUTE_MODIFIER_FORMAT.format(amount),
                    Component.translatable(ability.getAttribute().getDescriptionId())))
                    .withStyle(ChatFormatting.BLUE));
        });
    }
}
