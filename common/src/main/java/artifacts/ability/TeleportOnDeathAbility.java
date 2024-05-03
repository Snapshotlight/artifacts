package artifacts.ability;

import artifacts.platform.PlatformServices;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TeleportOnDeathAbility implements ArtifactAbility {

    public static ItemStack findTotem(LivingEntity entity) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack handItem = entity.getItemInHand(hand);
            if (AbilityHelper.hasAbility(ModAbilities.TELEPORT_ON_DEATH.get(), handItem)
                    && !(entity instanceof Player player && player.getCooldowns().isOnCooldown(handItem.getItem()))
            ) {
                return handItem;
            }
        }

        return PlatformServices.platformHelper
                .findAllEquippedBy(entity, stack -> AbilityHelper.hasAbility(ModAbilities.TELEPORT_ON_DEATH.get(), stack)
                        && !(entity instanceof Player player && player.getCooldowns().isOnCooldown(stack.getItem())))
                .findFirst()
                .orElse(ItemStack.EMPTY);
    }

    public static void teleport(LivingEntity entity, ServerLevel level) {
        double oldX = entity.getX();
        double oldY = entity.getY();
        double oldZ = entity.getZ();

        for (int i = 0; i < 32; ++i) {
            double newX = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 32;
            double newY = Mth.clamp(entity.getY() + entity.getRandom().nextInt(16) - 8, level.getMinBuildHeight(), level.getMinBuildHeight() + level.getLogicalHeight() - 1);
            double newZ = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 32;

            Vec3 oldPos = entity.position();
            if (oldPos.distanceToSqr(newX, newY, newZ) < 16 * 16) {
                continue;
            }

            if (entity.isPassenger()) {
                entity.stopRiding();
            }

            if (entity.randomTeleport(newX, newY, newZ, true)) {
                entity.level().gameEvent(GameEvent.TELEPORT, oldPos, GameEvent.Context.of(entity));
                entity.level().playSound(null, oldX, oldY, oldZ, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1, 1);
                entity.level().playSound(null, newX, newY, newZ, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1, 1);
                break;
            }
        }
    }

    public double getTeleportationChance() {
        return ModGameRules.CHORUS_TOTEM_TELEPORTATION_CHANCE.get();
    }

    public int getHealthRestored() {
        return ModGameRules.CHORUS_TOTEM_HEALTH_RESTORED.get();
    }

    public boolean isConsumedOnUse() {
        return ModGameRules.CHORUS_TOTEM_DO_CONSUME_ON_USE.get();
    }

    public int getCooldown() {
        return ModGameRules.CHORUS_TOTEM_COOLDOWN.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.TELEPORT_ON_DEATH.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return getTeleportationChance() > 0;
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        if (getTeleportationChance() + 1e-10 >= 1) {
            tooltip.add(tooltipLine("constant"));
        } else {
            tooltip.add(tooltipLine("chance"));
        }
        if (!isConsumedOnUse()) {
            tooltip.add(tooltipLine("not_consumed"));
        }
    }
}
