package artifacts.ability;

import artifacts.component.SwimData;
import artifacts.platform.PlatformServices;
import artifacts.registry.*;
import artifacts.util.AbilityHelper;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class SwimInAirAbility implements ArtifactAbility {

    public static void onHeliumFlamingoTick(Player player) {
        SwimData swimData = PlatformServices.platformHelper.getSwimData(player);
        if (swimData == null) {
            return;
        }
        int maxFlightTime = getFlightDuration(player);
        boolean shouldSink = AbilityHelper.hasAbility(ModAbilities.SINKING.get(), player);
        boolean canFly = maxFlightTime > 0;
        if (swimData.isSwimming()) {
            if (swimData.getSwimTime() > maxFlightTime
                    || player.isInWater() && !player.isSwimming() && !shouldSink
                    || (!player.isInWater() || shouldSink) && player.onGround()
            ) {
                swimData.setSwimming(player, false);
                if (!player.onGround() && !player.isInWater()) {
                    player.playSound(ModSoundEvents.POP.get(), 0.5F, 0.75F);
                }
            }

            if (canFly && !PlatformServices.platformHelper.isEyeInWater(player)) {
                if (!player.getAbilities().invulnerable) {
                    swimData.setSwimTime(swimData.getSwimTime() + 1);
                }
            }
        } else if (swimData.getSwimTime() < 0) {
            int rechargeTime = getRechargeDuration(player);
            swimData.setSwimTime(
                    swimData.getSwimTime() < -rechargeTime ? -rechargeTime : swimData.getSwimTime() + 1
            );
        }
    }

    public static int getFlightDuration(LivingEntity entity) {
        return AbilityHelper.maxInt(ModAbilities.SWIM_IN_AIR.get(), entity, SwimInAirAbility::getFlightDuration, false);
    }

    public static int getRechargeDuration(LivingEntity entity) {
        return Math.max(20, AbilityHelper.maxInt(ModAbilities.SWIM_IN_AIR.get(), entity, SwimInAirAbility::getRechargeDuration, false));
    }

    public int getFlightDuration() {
        return ModGameRules.HELIUM_FLAMINGO_FLIGHT_DURATION.get();
    }

    public int getRechargeDuration() {
        return ModGameRules.HELIUM_FLAMINGO_RECHARGE_DURATION.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.SWIM_IN_AIR.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return getFlightDuration() > 0;
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        tooltip.add(tooltipLine("swimming"));
    }

    @Override
    public void addToggleKeyTooltip(List<MutableComponent> tooltip) {
        tooltip.add(tooltipLine("keymapping", ModKeyMappings.getHeliumFlamingoKey().getTranslatedKeyMessage()));
    }
}
