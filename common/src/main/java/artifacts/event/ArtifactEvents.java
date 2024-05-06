package artifacts.event;

import artifacts.Artifacts;
import artifacts.ability.*;
import artifacts.ability.retaliation.RetaliationAbility;
import artifacts.item.UmbrellaItem;
import artifacts.mixin.accessors.MobAccessor;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModAttributes;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModTags;
import artifacts.util.AbilityHelper;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class ArtifactEvents {

    private static final AttributeModifier UMBRELLA_SLOW_FALLING = new AttributeModifier(
            UUID.fromString("a7a25453-2065-4a96-bc83-df600e13f390"),
            "artifacts:umbrella_slow_falling",
            -0.875,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
    );

    public static void register() {
        PlayerEvent.DROP_ITEM.register(AttractItemsAbility::onItemToss);
        EntityEvent.LIVING_HURT.register(ApplySpeedAfterDamageAbility::onLivingHurt);
        EntityEvent.LIVING_HURT.register(FireAspectAbility::onLivingHurt);
        EntityEvent.LIVING_HURT.register(KnockbackAbility::onLivingHurt);
        EntityEvent.LIVING_HURT.register(ArtifactEvents::onPendantLivingHurt);
        EntityEvent.LIVING_HURT.register(ArtifactEvents::onLightningHurt);
        EntityEvent.ADD.register(ArtifactEvents::onEntityJoinWorld);
        TickEvent.PLAYER_PRE.register(SwimInAirAbility::onHeliumFlamingoTick);
    }

    public static void livingUpdate(LivingEntity entity) {
        onUmbrellaLivingUpdate(entity);
    }

    private static void onUmbrellaLivingUpdate(LivingEntity entity) {
        AttributeInstance gravity = entity.getAttribute(Attributes.GRAVITY);
        if (gravity != null) {
            boolean isInWater = entity.isInWater() && !AbilityHelper.hasAbilityActive(ModAbilities.SINKING.get(), entity);
            if (ModGameRules.UMBRELLA_IS_GLIDER.get()
                    && !entity.onGround() && !isInWater
                    && entity.getDeltaMovement().y < 0
                    && !entity.hasEffect(MobEffects.SLOW_FALLING)
                    && UmbrellaItem.isHoldingUmbrellaUpright(entity)
            ) {
                if (!gravity.hasModifier(UMBRELLA_SLOW_FALLING)) {
                    gravity.addTransientModifier(UMBRELLA_SLOW_FALLING);
                }
                entity.fallDistance = 0;
            } else if (gravity.hasModifier(UMBRELLA_SLOW_FALLING)) {
                gravity.removeModifier(UMBRELLA_SLOW_FALLING.id());
            }
        }
    }

    private static EventResult onPendantLivingHurt(LivingEntity entity, DamageSource damageSource, float amount) {
        activateRetaliationAbility(ModAbilities.SET_ATTACKERS_ON_FIRE.get(), entity, damageSource, amount);
        activateRetaliationAbility(ModAbilities.THORNS.get(), entity, damageSource, amount);

        return EventResult.pass();
    }

    private static void activateRetaliationAbility(ArtifactAbility.Type<? extends RetaliationAbility> type, LivingEntity entity, DamageSource damageSource, float amount) {
        AbilityHelper.forEach(type, entity, (ability, stack) -> ability.onLivingHurt(entity, stack, damageSource, amount), true);
    }

    private static EventResult onEntityJoinWorld(Entity entity, Level level) {
        if (entity instanceof PathfinderMob creeper && creeper.getType().is(ModTags.CREEPERS)) {
            Predicate<LivingEntity> predicate = target -> AbilityHelper.hasAbilityActive(ModAbilities.SCARE_CREEPERS.get(), target);
            ((MobAccessor) creeper).getGoalSelector().addGoal(3,
                    new AvoidEntityGoal<>(creeper, Player.class, predicate, 6, 1, 1.3, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test)
            );
        }
        return EventResult.pass();
    }

    public static void onPlaySoundAtEntity(LivingEntity entity, float volume, float pitch) {
        if (Artifacts.CONFIG.common.modifyHurtSounds) {
            AbilityHelper.forEach(ModAbilities.MODIFY_HURT_SOUND.get(), entity, ability -> entity.playSound(ability.soundEvent(), volume, pitch));
        }
    }

    private static EventResult onLightningHurt(LivingEntity entity, DamageSource damageSource, float amount) {
        if (!entity.level().isClientSide()
                && amount > 0
                && damageSource.is(DamageTypeTags.IS_LIGHTNING)
                && AbilityHelper.hasAbilityActive(ModAbilities.LIGHTNING_IMMUNITY.get(), entity)
        ) {
            return EventResult.interruptFalse();
        }
        return EventResult.pass();
    }

    public static ObjectArrayList<ItemStack> getPickaxeHeaterModifiedBlockDrops(ObjectArrayList<ItemStack> items, LootContext context, TagKey<Block> ores, TagKey<Item> rawOres) {
        if (context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof LivingEntity entity
                && AbilityHelper.hasAbilityActive(ModAbilities.SMELT_ORES.get(), entity)
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

    public static int modifyUseDuration(int originalDuration, ItemStack item, LivingEntity entity) {
        if (originalDuration <= 0) {
            return originalDuration;
        }
        if (item.getUseAnimation() == UseAnim.EAT) {
            return (int) Math.max(1, Math.round(originalDuration / entity.getAttributeValue(ModAttributes.EATING_SPEED)));
        } else if (item.getUseAnimation() == UseAnim.DRINK) {
            return (int) Math.max(1, Math.round(originalDuration / entity.getAttributeValue(ModAttributes.DRINKING_SPEED)));
        }
        return originalDuration;
    }
}
