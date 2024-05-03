package artifacts.neoforge.event;

import artifacts.ability.*;
import artifacts.item.UmbrellaItem;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModTags;
import artifacts.util.AbilityHelper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.UUID;

public class ArtifactEventsNeoForge {

    private static final AttributeModifier UMBRELLA_SLOW_FALLING = new AttributeModifier(
            UUID.fromString("a7a25453-2065-4a96-bc83-df600e13f390"),
            "artifacts:umbrella_slow_falling",
            -0.875,
            AttributeModifier.Operation.MULTIPLY_TOTAL
    );

    public static void register() {
        NeoForge.EVENT_BUS.addListener(EventPriority.LOW, ArtifactEventsNeoForge::onLivingDamage);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGH, ArtifactEventsNeoForge::onLivingFall);
        NeoForge.EVENT_BUS.addListener(ArtifactEventsNeoForge::onLivingUpdate);
        NeoForge.EVENT_BUS.addListener(ArtifactEventsNeoForge::onDrinkingHatItemUse);
        NeoForge.EVENT_BUS.addListener(ArtifactEventsNeoForge::onGoldenHookExperienceDrop);
        NeoForge.EVENT_BUS.addListener(ArtifactEventsNeoForge::onKittySlippersChangeTarget);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOW, ArtifactEventsNeoForge::onDiggingClawsBreakSpeed);
        NeoForge.EVENT_BUS.addListener(ArtifactEventsNeoForge::onDiggingClawsHarvestCheck);
        NeoForge.EVENT_BUS.addListener(ArtifactEventsNeoForge::onFoodEaten);
    }

    private static void onLivingDamage(LivingDamageEvent event) {
        AbsorbDamageAbility.onLivingDamage(event.getEntity(), event.getSource(), event.getAmount());
        ApplyFireResistanceAfterFireDamageAbility.onLivingDamage(event.getEntity(), event.getSource(), event.getAmount());
    }

    private static void onLivingFall(LivingFallEvent event) {
        if (AbilityHelper.hasAbility(ModAbilities.CANCEL_FALL_DAMAGE, event.getEntity())) {
            event.setDamageMultiplier(0);
        }
        onCloudInABottleFall(event);
    }

    private static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        onKittySlippersLivingUpdate(event);
        onUmbrellaLivingUpdate(event);
    }

    private static void onCloudInABottleFall(LivingFallEvent event) {
        event.setDistance(DoubleJumpAbility.getReducedFallDistance(event.getEntity(), event.getDistance()));
    }

    private static void onDrinkingHatItemUse(LivingEntityUseItemEvent.Start event) {
        event.setDuration(ReduceEatingDurationAbility.getDrinkingHatUseDuration(event.getEntity(), event.getItem().getUseAnimation(), event.getDuration()));
    }

    private static void onGoldenHookExperienceDrop(LivingExperienceDropEvent event) {
        int originalXp = event.getOriginalExperience();
        int droppedXp = event.getDroppedExperience();
        int modifiedXp = droppedXp + ExperienceBonusAbility.getExperienceBonus(originalXp, event.getEntity(), event.getAttackingPlayer());
        event.setDroppedExperience(modifiedXp);
    }

    private static void onKittySlippersChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity target = event.getNewTarget();
        if (AbilityHelper.hasAbility(ModAbilities.SCARE_CREEPERS, target)
                && event.getEntity() instanceof Mob creeper
                && creeper.getType().is(ModTags.CREEPERS)
        ) {
            event.setCanceled(true);
        }
    }

    private static void onKittySlippersLivingUpdate(LivingEvent.LivingTickEvent event) {
        if (AbilityHelper.hasAbility(ModAbilities.SCARE_CREEPERS, event.getEntity().getLastHurtByMob())
                && event.getEntity().getType().is(ModTags.CREEPERS)
        ) {
            event.getEntity().setLastHurtByMob(null);
        }
    }

    private static void onDiggingClawsBreakSpeed(PlayerEvent.BreakSpeed event) {
        float speedBonus = DigSpeedAbility.getSpeedBonus(event.getEntity(), event.getState());
        if (speedBonus > 0) {
            event.setNewSpeed(event.getNewSpeed() + speedBonus);
        }
    }

    private static void onDiggingClawsHarvestCheck(PlayerEvent.HarvestCheck event) {
        event.setCanHarvest(event.canHarvest() || UpgradeToolTierAbility.canHarvestWithTier(event.getEntity(), event.getTargetBlock()));
    }

    private static void onUmbrellaLivingUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        AttributeInstance gravity = entity.getAttribute(NeoForgeMod.ENTITY_GRAVITY.value());
        if (gravity != null) {
            boolean isInWater = entity.isInWater() && !AbilityHelper.hasAbility(ModAbilities.SINKING, entity);
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
                gravity.removeModifier(UMBRELLA_SLOW_FALLING.getId());
            }
        }
    }

    private static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        FoodProperties properties = event.getItem().getFoodProperties(event.getEntity());
        if (properties != null) {
            ApplyHasteAfterEatingAbility.applyHasteEffect(event.getEntity(), properties);
            GrowPlantsAfterEatingAbility.applyBoneMeal(event.getEntity(), properties);
        }
    }
}
