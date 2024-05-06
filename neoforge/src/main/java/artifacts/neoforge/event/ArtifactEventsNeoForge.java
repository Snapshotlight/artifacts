package artifacts.neoforge.event;

import artifacts.ability.*;
import artifacts.component.AbilityToggles;
import artifacts.event.ArtifactEvents;
import artifacts.platform.PlatformServices;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModAttributes;
import artifacts.registry.ModTags;
import artifacts.util.AbilityHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.UseAnim;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class ArtifactEventsNeoForge {

    public static void register() {
        NeoForge.EVENT_BUS.addListener(EventPriority.LOW, ArtifactEventsNeoForge::onLivingDamage);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGH, ArtifactEventsNeoForge::onLivingFall);
        NeoForge.EVENT_BUS.addListener(ArtifactEventsNeoForge::onLivingUpdate);
        NeoForge.EVENT_BUS.addListener(ArtifactEventsNeoForge::onDrinkingHatItemUse);
        NeoForge.EVENT_BUS.addListener(ArtifactEventsNeoForge::onGoldenHookExperienceDrop);
        NeoForge.EVENT_BUS.addListener(ArtifactEventsNeoForge::onKittySlippersChangeTarget);
        NeoForge.EVENT_BUS.addListener(ArtifactEventsNeoForge::onDiggingClawsHarvestCheck);
        NeoForge.EVENT_BUS.addListener(ArtifactEventsNeoForge::onFoodEaten);
        NeoForge.EVENT_BUS.addListener(ArtifactEventsNeoForge::onPlayerTick);
    }

    private static void onPlayerTick(PlayerTickEvent.Post event) {
        AbilityToggles abilityToggles = PlatformServices.platformHelper.getAbilityToggles(event.getEntity());
        if (event.getEntity() instanceof ServerPlayer serverPlayer && abilityToggles != null) {
            abilityToggles.sendToClient(serverPlayer);
        }
    }

    private static void onLivingDamage(LivingDamageEvent event) {
        AbsorbDamageAbility.onLivingDamage(event.getEntity(), event.getSource(), event.getAmount());
        ApplyFireResistanceAfterFireDamageAbility.onLivingDamage(event.getEntity(), event.getSource(), event.getAmount());
    }

    private static void onLivingFall(LivingFallEvent event) {
        if (AbilityHelper.hasAbilityActive(ModAbilities.CANCEL_FALL_DAMAGE.get(), event.getEntity())) {
            event.setDamageMultiplier(0);
        }
    }

    private static void onLivingUpdate(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof LivingEntity entity) {
            onKittySlippersLivingUpdate(entity);
            ArtifactEvents.livingUpdate(entity);
        }
    }

    private static void onDrinkingHatItemUse(LivingEntityUseItemEvent.Start event) {
        event.setDuration(ArtifactEvents.modifyUseDuration(event.getDuration(), event.getItem(), event.getEntity()));
    }

    private static void onGoldenHookExperienceDrop(LivingExperienceDropEvent event) {
        int originalXp = event.getOriginalExperience();
        int droppedXp = event.getDroppedExperience();
        int modifiedXp = droppedXp + ExperienceBonusAbility.getExperienceBonus(originalXp, event.getEntity(), event.getAttackingPlayer());
        event.setDroppedExperience(modifiedXp);
    }

    private static void onKittySlippersChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity target = event.getNewTarget();
        if (AbilityHelper.hasAbilityActive(ModAbilities.SCARE_CREEPERS.get(), target)
                && event.getEntity() instanceof Mob creeper
                && creeper.getType().is(ModTags.CREEPERS)
        ) {
            event.setCanceled(true);
        }
    }

    private static void onKittySlippersLivingUpdate(LivingEntity entity) {
        if (AbilityHelper.hasAbilityActive(ModAbilities.SCARE_CREEPERS.get(), entity.getLastHurtByMob())
                && entity.getType().is(ModTags.CREEPERS)
        ) {
            entity.setLastHurtByMob(null);
        }
    }

    private static void onDiggingClawsHarvestCheck(PlayerEvent.HarvestCheck event) {
        event.setCanHarvest(event.canHarvest() || UpgradeToolTierAbility.canHarvestWithTier(event.getEntity(), event.getTargetBlock()));
    }

    private static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        FoodProperties properties = event.getItem().getFoodProperties(event.getEntity());
        if (properties != null) {
            ApplyHasteAfterEatingAbility.applyHasteEffect(event.getEntity(), properties);
            GrowPlantsAfterEatingAbility.applyBoneMeal(event.getEntity(), properties);
        }
    }
}
