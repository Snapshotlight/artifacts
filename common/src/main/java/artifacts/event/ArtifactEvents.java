package artifacts.event;

import artifacts.Artifacts;
import artifacts.ability.*;
import artifacts.ability.retaliation.RetaliationAbility;
import artifacts.mixin.accessors.MobAccessor;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModTags;
import artifacts.util.AbilityHelper;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class ArtifactEvents {

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

    private static EventResult onPendantLivingHurt(LivingEntity entity, DamageSource damageSource, float amount) {
        activateRetaliationAbility(ModAbilities.SET_ATTACKERS_ON_FIRE, entity, damageSource, amount);
        activateRetaliationAbility(ModAbilities.THORNS, entity, damageSource, amount);

        return EventResult.pass();
    }

    private static void activateRetaliationAbility(ArtifactAbility.Type<? extends RetaliationAbility> type, LivingEntity entity, DamageSource damageSource, float amount) {
        AbilityHelper.forEach(type, entity, (ability, stack) -> ability.onLivingHurt(entity, stack, damageSource, amount), true);
    }

    private static EventResult onEntityJoinWorld(Entity entity, Level level) {
        if (entity instanceof PathfinderMob creeper && creeper.getType().is(ModTags.CREEPERS)) {
            Predicate<LivingEntity> predicate = target -> AbilityHelper.hasAbility(ModAbilities.SCARE_CREEPERS, target);
            ((MobAccessor) creeper).getGoalSelector().addGoal(3,
                    new AvoidEntityGoal<>(creeper, Player.class, predicate, 6, 1, 1.3, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test)
            );
        }
        return EventResult.pass();
    }

    public static void onPlaySoundAtEntity(LivingEntity entity, float volume, float pitch) {
        if (Artifacts.CONFIG.common.modifyHurtSounds) {
            AbilityHelper.forEach(ModAbilities.MODIFY_HURT_SOUND, entity, ability -> entity.playSound(ability.getSoundEvent(), volume, pitch));
        }
    }

    private static EventResult onLightningHurt(LivingEntity entity, DamageSource damageSource, float amount) {
        if (!entity.level().isClientSide()
                && amount > 0
                && damageSource.is(DamageTypeTags.IS_LIGHTNING)
                && AbilityHelper.hasAbility(ModAbilities.LIGHTNING_IMMUNITY, entity)
        ) {
            return EventResult.interruptFalse();
        }
        return EventResult.pass();
    }
}
