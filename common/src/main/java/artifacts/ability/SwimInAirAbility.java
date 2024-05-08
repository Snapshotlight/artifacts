package artifacts.ability;

import artifacts.ability.value.IntegerValue;
import artifacts.component.SwimData;
import artifacts.platform.PlatformServices;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModKeyMappings;
import artifacts.registry.ModSoundEvents;
import artifacts.util.AbilityHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public record SwimInAirAbility(IntegerValue flightDuration, IntegerValue rechargeDuration) implements ArtifactAbility {

    public static final MapCodec<SwimInAirAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IntegerValue.field("flight_duration", ModGameRules.HELIUM_FLAMINGO_FLIGHT_DURATION).forGetter(SwimInAirAbility::flightDuration),
            IntegerValue.field("recharge_duration", ModGameRules.HELIUM_FLAMINGO_RECHARGE_DURATION).forGetter(SwimInAirAbility::rechargeDuration)
    ).apply(instance, SwimInAirAbility::new));

    public static final StreamCodec<ByteBuf, SwimInAirAbility> STREAM_CODEC = StreamCodec.composite(
            IntegerValue.defaultStreamCodec(ModGameRules.HELIUM_FLAMINGO_FLIGHT_DURATION),
            SwimInAirAbility::flightDuration,
            IntegerValue.defaultStreamCodec(ModGameRules.HELIUM_FLAMINGO_RECHARGE_DURATION),
            SwimInAirAbility::rechargeDuration,
            SwimInAirAbility::new
    );

    public static void onHeliumFlamingoTick(Player player) {
        SwimData swimData = PlatformServices.platformHelper.getSwimData(player);
        if (swimData == null) {
            return;
        }
        int maxFlightTime = getFlightDuration(player);
        boolean shouldSink = AbilityHelper.hasAbilityActive(ModAbilities.SINKING.get(), player);
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
        return AbilityHelper.maxInt(ModAbilities.SWIM_IN_AIR.get(), entity, ability -> ability.flightDuration().get(), false);
    }

    public static int getRechargeDuration(LivingEntity entity) {
        return Math.max(20, AbilityHelper.maxInt(ModAbilities.SWIM_IN_AIR.get(), entity, ability -> ability.rechargeDuration().get(), false));
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.SWIM_IN_AIR.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return flightDuration().get() > 0;
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
