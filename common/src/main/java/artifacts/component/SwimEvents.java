package artifacts.component;

import artifacts.platform.PlatformServices;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.material.FluidState;

public class SwimEvents {

    public static void register() {
        TickEvent.PLAYER_PRE.register(SwimEvents::onPlayerTick);
    }

    private static void onPlayerTick(Player player) {
        SwimData swimData = PlatformServices.platformHelper.getSwimData(player);
        if (swimData != null) {
            if (player.isInWater() || player.isInLava() || player.fallDistance > 6) {
                if (!swimData.isWet()) {
                    swimData.setWet(true);
                }
            } else if (player.onGround() || player.getAbilities().flying) {
                swimData.setWet(false);
            }
        }
    }

    public static EventResult onPlayerSwim(Player player) {
        SwimData swimData = PlatformServices.platformHelper.getSwimData(player);
        if (swimData != null) {
            if (swimData.isSwimming()) {
                return EventResult.interruptTrue();
            } else if (AbilityHelper.hasAbilityActive(ModAbilities.SINKING.get(), player)) {
                return EventResult.interruptFalse();
            }
        }
        return EventResult.pass();
    }

    public static boolean onFluidCollision(LivingEntity player, FluidState fluidState) {
        if (canCollideWithFluid(player)) {
            SwimData swimData = PlatformServices.platformHelper.getSwimData(player);
            if (swimData != null && !swimData.isWet() && !swimData.isSwimming()) {
                if (fluidState.is(FluidTags.LAVA) && !player.fireImmune() && !EnchantmentHelper.hasFrostWalker(player)) {
                    player.hurt(player.damageSources().hotFloor(), 1);
                }
                return true;
            }
        }
        return false;
    }

    private static boolean canCollideWithFluid(LivingEntity entity) {
        return AbilityHelper.hasAbilityActive(ModAbilities.SPRINT_ON_WATER.get(), entity)
                && entity.isSprinting()
                && !entity.isUsingItem()
                && !entity.isCrouching();
    }
}
