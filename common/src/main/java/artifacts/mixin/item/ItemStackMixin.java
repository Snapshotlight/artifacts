package artifacts.mixin.item;

import artifacts.Artifacts;
import artifacts.ability.ArtifactAbility;
import artifacts.ability.AttributeModifierAbility;
import artifacts.ability.mobeffect.MobEffectAbility;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModDataComponents;
import artifacts.util.AbilityHelper;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
    }

    @Inject(method = "addAttributeTooltips", at = @At("TAIL"))
    private void addAttributeTooltips(Consumer<Component> consumer, @Nullable Player player, CallbackInfo info) {
        ItemStack self = (ItemStack) (Object) this;
        ItemAttributeModifiers itemAttributeModifiers = self.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        boolean hasSlotTooltip = false;
        if (itemAttributeModifiers.showInTooltip()) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                MutableBoolean b = new MutableBoolean(false);
                self.forEachModifier(slot, (holder, attributeModifier) -> b.setTrue());
                if (b.booleanValue()) {
                    hasSlotTooltip = true;
                    addAbilityAttributeTooltips(self, consumer);
                }
            }
        }
        if (!hasSlotTooltip) {
            if (AbilityHelper.hasAbility(ModAbilities.ATTRIBUTE_MODIFIER.get(), self)
                    || AbilityHelper.hasAbility(ModAbilities.MOB_EFFECT.get(), self)
                    || AbilityHelper.hasAbility(ModAbilities.LIMITED_WATER_BREATHING.get(), self)
            ) {
                consumer.accept(CommonComponents.EMPTY);
                consumer.accept(Component.translatable("artifacts.tooltip.when_equipped").withStyle(ChatFormatting.GRAY));
            }
            addAbilityAttributeTooltips(self, consumer);
        }
    }

    @Unique
    private static void addAbilityAttributeTooltips(ItemStack stack, Consumer<Component> tooltip) {
        AbilityHelper.getAbilities(ModAbilities.ATTRIBUTE_MODIFIER.get(), stack).forEach(ability -> addAbilityAttributeTooltip(tooltip, ability));
        AbilityHelper.getAbilities(ModAbilities.MOB_EFFECT.get(), stack).forEach(ability -> addAbilityMobEffectTooltip(tooltip, ability));
        AbilityHelper.getAbilities(ModAbilities.LIMITED_WATER_BREATHING.get(), stack).forEach(ability -> addAbilityMobEffectTooltip(tooltip, ability));
    }

    @Unique
    private static void addAbilityAttributeTooltip(Consumer<Component> tooltip, AttributeModifierAbility ability) {
        double amount = ability.amount().get();

        if (ability.operation() != AttributeModifier.Operation.ADD_VALUE) {
            amount *= 100;
        } else if (ability.attribute().equals(Attributes.KNOCKBACK_RESISTANCE)) {
            amount *= 10;
        }

        if (amount > 0) {
            tooltip.accept(Component.translatable(
                    "attribute.modifier.plus." + ability.operation().id(),
                    ATTRIBUTE_MODIFIER_FORMAT.format(amount),
                    Component.translatable(ability.attribute().value().getDescriptionId())
            ).withStyle(ChatFormatting.BLUE));
        } else if (amount < 0) {
            amount *= -1;
            tooltip.accept(Component.translatable(
                    "attribute.modifier.take." + ability.operation().id(),
                    ATTRIBUTE_MODIFIER_FORMAT.format(amount),
                    Component.translatable(ability.attribute().value().getDescriptionId())
            ).withStyle(ChatFormatting.RED));
        }
    }

    @Unique
    private static void addAbilityMobEffectTooltip(Consumer<Component> tooltip, MobEffectAbility ability) {
        List<Pair<Holder<Attribute>, AttributeModifier>> list = Lists.newArrayList();

        MutableComponent mutableComponent;

        mutableComponent = Component.translatable(ability.getMobEffect().value().getDescriptionId());
        ability.getMobEffect().value().createModifiers(ability.getAmplifier(), (arg, arg2) -> list.add(new Pair<>(arg, arg2)));
        if (ability.getLevel().get() > 0) {
            mutableComponent = Component.translatable("potion.withAmplifier", mutableComponent, Component.translatable("potion.potency." + ability.getAmplifier()));
        }

        if (!ability.isInfinite()) {
            mutableComponent = Component.translatable("potion.withDuration", mutableComponent, formatDuration(ability));
        }

        tooltip.accept(Component.translatable("artifacts.tooltip.plus_mob_effect", mutableComponent).withStyle(ability.getMobEffect().value().getCategory().getTooltipFormatting()));

        if (!list.isEmpty()) {
            for (Pair<Holder<Attribute>, AttributeModifier> pair : list) {
                AttributeModifier attributeModifier = pair.getSecond();
                double d = attributeModifier.amount();
                double e;
                if (attributeModifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE && attributeModifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    e = attributeModifier.amount();
                } else {
                    e = attributeModifier.amount() * 100.0;
                }

                if (d > 0.0) {
                    tooltip.accept(Component.translatable("attribute.modifier.plus." + attributeModifier.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(e), Component.translatable(pair.getFirst().value().getDescriptionId())).withStyle(ChatFormatting.BLUE));
                } else if (d < 0.0) {
                    e *= -1.0;
                    tooltip.accept(Component.translatable("attribute.modifier.take." + attributeModifier.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(e), Component.translatable(pair.getFirst().value().getDescriptionId())).withStyle(ChatFormatting.RED));
                }
            }
        }
    }

    @Unique
    private static MutableComponent formatDuration(MobEffectAbility ability) {
        if (ability.isInfinite()) {
            return Component.translatable("effect.duration.infinite");
        } else {
            float tickRate = 20;
            if (Minecraft.getInstance() != null && Minecraft.getInstance().level != null) {
                tickRate = Minecraft.getInstance().level.tickRateManager().tickrate();
            }
            return Component.literal(StringUtil.formatTickDuration(ability.getDuration(), tickRate));
        }
    }
}
