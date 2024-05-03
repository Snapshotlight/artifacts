package artifacts.neoforge.curio;

import artifacts.ability.IncreaseEnchantmentLevelAbility;
import artifacts.item.WearableArtifactItem;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import artifacts.util.DamageSourceHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class WearableArtifactCurio implements ICurioItem {

    private final WearableArtifactItem item;

    public WearableArtifactCurio(WearableArtifactItem item) {
        this.item = item;
    }

    @Override
    public final void curioTick(SlotContext slotContext, ItemStack stack) {
        item.wornTick(slotContext.entity(), stack);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack originalStack, ItemStack stack) {
        item.onEquip(slotContext.entity(), stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        item.onUnequip(slotContext.entity(), stack);
    }

    @Override
    public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        if (DamageSourceHelper.shouldDestroyWornItemsOnDeath(slotContext.entity())) {
            return ICurio.DropRule.DESTROY;
        }
        return ICurioItem.super.getDropRule(slotContext, source, lootingLevel, recentlyHit, stack);
    }

    @Override
    public final ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(item.getEquipSound(), 1, item.getEquipSoundPitch());
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return item.getFoodProperties(stack, slotContext.entity()) == null;
    }

    @Override
    public int getFortuneLevel(SlotContext slotContext, @Nullable LootContext lootContext, ItemStack stack) {
        return AbilityHelper.getAbilities(ModAbilities.INCREASE_ENCHANTMENT_LEVEL.get(), stack)
                .filter(ability -> ability.getEnchantment() == Enchantments.BLOCK_FORTUNE)
                .mapToInt(IncreaseEnchantmentLevelAbility::getAmount)
                .sum();
    }

    @Override
    public int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting, ItemStack stack) {
        return AbilityHelper.getAbilities(ModAbilities.INCREASE_ENCHANTMENT_LEVEL.get(), stack)
                .filter(ability -> ability.getEnchantment() == Enchantments.MOB_LOOTING)
                .mapToInt(IncreaseEnchantmentLevelAbility::getAmount)
                .sum();
    }

    @Override
    public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        return AbilityHelper.hasAbility(ModAbilities.MAKE_PIGLINS_NEUTRAL.get(), stack);
    }

    @Override
    public boolean canWalkOnPowderedSnow(SlotContext slotContext, ItemStack stack) {
        return AbilityHelper.hasAbility(ModAbilities.WALK_ON_POWDER_SNOW.get(), stack);
    }
}
