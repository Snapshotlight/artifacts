package artifacts.mixin.item;

import artifacts.Artifacts;
import artifacts.ability.ArtifactAbility;
import artifacts.ability.AttributeModifierAbility;
import artifacts.item.WearableArtifactItem;
import artifacts.platform.PlatformServices;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModDataComponents;
import artifacts.util.AbilityHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringUtil;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
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
        if (!Artifacts.CONFIG.client.showTooltips.get()) {
            return;
        }

        // noinspection ConstantConditions
        ItemStack stack = (ItemStack) (Object) this;

        if (stack.getItem() instanceof WearableArtifactItem) {
            List<MutableComponent> tooltip = new ArrayList<>();
            if (AbilityHelper.isCosmetic(stack)) {
                tooltip.add(Component.translatable("%s.tooltip.cosmetic".formatted(Artifacts.MOD_ID)).withStyle(ChatFormatting.ITALIC));
            } else {
                PlatformServices.platformHelper.addCosmeticToggleTooltip(tooltip, stack);
            }
            tooltip.forEach(line -> tooltipList.add(line.withStyle(ChatFormatting.GRAY)));
        }

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
        // noinspection ConstantConditions
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
                consumer.accept(Component.translatable("item.modifiers.body").withStyle(ChatFormatting.GRAY));
            }
            addAbilityAttributeTooltips(self, consumer);
        }
        addWhenHurtTooltips(consumer, self);
        addPerFoodPointEatenTooltip(consumer, self);
    }

    @Unique
    private static void addAbilityAttributeTooltips(ItemStack stack, Consumer<Component> tooltip) {
        AbilityHelper.getAbilities(ModAbilities.ATTRIBUTE_MODIFIER.get(), stack).forEach(ability -> addAbilityAttributeTooltip(tooltip, ability));
        AbilityHelper.getAbilities(ModAbilities.MOB_EFFECT.get(), stack).forEach(ability -> addMobEffectTooltip(tooltip, ability.getMobEffect().value(), ability.getDuration(), ability.getLevel().get(), ability.isInfinite()));
        AbilityHelper.getAbilities(ModAbilities.LIMITED_WATER_BREATHING.get(), stack).forEach(ability -> addMobEffectTooltip(tooltip, ability.getMobEffect().value(), ability.getDuration(), ability.getLevel().get(), ability.isInfinite()));
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
    private static void addWhenHurtTooltips(Consumer<Component> tooltip, ItemStack stack) {
        MutableBoolean flag = new MutableBoolean(false);
        List<TagKey<DamageType>> list = new ArrayList<>();
        AbilityHelper.getAbilities(ModAbilities.APPLY_MOB_EFFECT_AFTER_DAMAGE.get(), stack).forEach(ability -> {
            if (ability.tag().isEmpty()) {
                flag.setTrue();
            } else if (!list.contains(ability.tag().get())) {
                list.add(ability.tag().get());
            }
        });

        if (flag.booleanValue()) {
            tooltip.accept(CommonComponents.EMPTY);
            tooltip.accept(Component.translatable("artifacts.tooltip.when_hurt").withStyle(ChatFormatting.GRAY));
            addWhenHurtTooltip(tooltip, stack, null);
        }
        for (TagKey<DamageType> tag : list) {
            tooltip.accept(CommonComponents.EMPTY);
            tooltip.accept(Component.translatable("artifacts.tooltip.when_hurt.%s".formatted(
                    tag.location()
                            .toString()
                            .replace("minecraft:", "")
                            .replace(':', '.')
            )).withStyle(ChatFormatting.GRAY));
            addWhenHurtTooltip(tooltip, stack, tag);
        }
    }

    @Unique
    private static void addWhenHurtTooltip(Consumer<Component> tooltip, ItemStack stack, @Nullable TagKey<DamageType> tag) {
        AbilityHelper.getAbilities(ModAbilities.APPLY_MOB_EFFECT_AFTER_DAMAGE.get(), stack)
                .forEach(ability -> {
                    if (ability.tag().isEmpty() && tag == null || ability.tag().isPresent() && ability.tag().get().equals(tag)) {
                        addMobEffectTooltip(tooltip, ability.mobEffect().value(), ability.duration().get(), ability.level().get(), false);
                    }
                });
        AbilityHelper.getAbilities(ModAbilities.APPLY_COOLDOWN_AFTER_DAMAGE.get(), stack)
                .forEach(ability -> {
                    if (ability.tag().isEmpty() && tag == null || ability.tag().isPresent() && ability.tag().get().equals(tag)) {
                        tooltip.accept(Component.translatable("artifacts.tooltip.cooldown", formatDurationSeconds(ability.cooldown().get())).withStyle(ChatFormatting.GOLD));
                    }
                });
    }

    @Unique
    private static void addPerFoodPointEatenTooltip(Consumer<Component> tooltip, ItemStack stack) {
        if (AbilityHelper.hasAbility(ModAbilities.APPLY_MOB_EFFECT_AFTER_EATING.get(), stack)) {
            tooltip.accept(CommonComponents.EMPTY);
            tooltip.accept(Component.translatable("artifacts.tooltip.per_food_point_restored").withStyle(ChatFormatting.GRAY));
            AbilityHelper.getAbilities(ModAbilities.APPLY_MOB_EFFECT_AFTER_EATING.get(), stack).forEach(ability ->
                    addMobEffectTooltip(tooltip, ability.mobEffect().value(), ability.durationPerFoodPoint().get(), ability.level().get(), false)
            );
        }
    }

    @Unique
    private static void addMobEffectTooltip(Consumer<Component> tooltip, MobEffect mobEffect, int duration, int level, boolean isInfinite) {
        MutableComponent mutableComponent;
        mutableComponent = Component.translatable(mobEffect.getDescriptionId());
        if (level > 1) {
            mutableComponent = Component.translatable("potion.withAmplifier", mutableComponent, Component.translatable("potion.potency." + (level - 1)));
        }
        if (!isInfinite) {
            mutableComponent = Component.translatable("potion.withDuration", mutableComponent, formatDurationSeconds(duration));
        }
        tooltip.accept(Component.translatable("artifacts.tooltip.plus_mob_effect", mutableComponent).withStyle(mobEffect.getCategory().getTooltipFormatting()));
    }

    @Unique
    private static MutableComponent formatDurationSeconds(int seconds) {
        return Component.literal(StringUtil.formatTickDuration(seconds * 20, 20));
    }
}
