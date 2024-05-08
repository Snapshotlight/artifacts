package artifacts.mixin.item;

import artifacts.Artifacts;
import artifacts.ability.ArtifactAbility;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModDataComponents;
import artifacts.util.AbilityHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.item.component.ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "getTooltipLines", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V"))
    private void getTooltipLines(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, List<Component> tooltipList) {
        if (!Artifacts.CONFIG.client.showTooltips) {
            return;
        }

        ItemStack stack = (ItemStack) (Object) this;
        if (stack.has(ModDataComponents.ABILITIES.get())) {
            List<MutableComponent> tooltip = new ArrayList<>();
            if (!AbilityHelper.isCosmetic(stack)) {
                for (ArtifactAbility ability : AbilityHelper.getAbilities(stack)) {
                    ability.addTooltipIfNonCosmetic(tooltip);
                }
            }
            tooltip.forEach(line -> tooltipList.add(line.withStyle(ChatFormatting.GRAY)));
        }

        if (AbilityHelper.hasAbility(ModAbilities.ATTRIBUTE_MODIFIER.get(), stack)) {
            tooltipList.add(Component.empty());
            tooltipList.add(Component.translatable("artifacts.tooltip.attribute_modifiers").withStyle(ChatFormatting.GRAY));

            AbilityHelper.getAbilities(ModAbilities.ATTRIBUTE_MODIFIER.get(), stack).forEach(ability -> {
                double amount = ability.amount().get();

                if (ability.operation() != AttributeModifier.Operation.ADD_VALUE) {
                    amount *= 100;
                } else if (ability.attribute().equals(Attributes.KNOCKBACK_RESISTANCE)) {
                    amount *= 10;
                }

                if (amount > 0) {
                    tooltipList.add(Component.translatable(
                            "attribute.modifier.plus." + ability.operation().id(),
                            ATTRIBUTE_MODIFIER_FORMAT.format(amount),
                            Component.translatable(ability.attribute().value().getDescriptionId())
                    ).withStyle(ChatFormatting.BLUE));
                } else if (amount < 0) {
                    amount *= -1;
                    tooltipList.add(Component.translatable(
                            "attribute.modifier.take." + ability.operation().id(),
                            ATTRIBUTE_MODIFIER_FORMAT.format(amount),
                            Component.translatable(ability.attribute().value().getDescriptionId())
                    ).withStyle(ChatFormatting.RED));
                }
            });
        }
    }
}
