package artifacts.item.wearable.hands;

import artifacts.ability.SimpleAbility;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class PickaxeHeaterItem extends WearableArtifactItem {

    public PickaxeHeaterItem() {
        super(SimpleAbility.smeltOres());
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_IRON;
    }

    public static ObjectArrayList<ItemStack> getModifiedBlockDrops(ObjectArrayList<ItemStack> items, LootContext context, TagKey<Block> ores, TagKey<Item> rawOres) {
        if (context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof LivingEntity entity
                && AbilityHelper.hasAbility(ModAbilities.SMELT_ORES, entity)
                && context.hasParam(LootContextParams.ORIGIN)
                && context.hasParam(LootContextParams.BLOCK_STATE)
                && context.getParam(LootContextParams.BLOCK_STATE).is(ores)
        ) {
            ObjectArrayList<ItemStack> result = new ObjectArrayList<>(items.size());
            Container container = new SimpleContainer(3);
            float experience = 0;
            for (ItemStack item : items) {
                ItemStack resultItem = item;
                if (item.is(rawOres)) {
                    container.setItem(0, item);
                    Optional<RecipeHolder<SmeltingRecipe>> recipe = context.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, container, context.getLevel());
                    if (recipe.isPresent()) {
                        ItemStack smeltingResult = recipe.get().value().getResultItem(context.getLevel().registryAccess());
                        if (!smeltingResult.isEmpty()) {
                            resultItem = smeltingResult.copyWithCount(smeltingResult.getCount() * item.getCount());
                            experience += recipe.get().value().getExperience();
                        }
                    }
                }
                result.add(resultItem);
            }
            awardExperience(context.getLevel(), context.getParam(LootContextParams.ORIGIN), experience);
            return result;
        }

        return items;
    }

    private static void awardExperience(ServerLevel level, Vec3 position, float experience) {
        int amount = Mth.floor(experience);
        if (Math.random() < Mth.frac(experience)) {
            amount++;
        }
        ExperienceOrb.award(level, position, amount);
    }
}
