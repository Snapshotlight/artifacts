package artifacts.fabric.event;

import artifacts.component.SwimEvents;
import be.florens.expandability.api.EventResult;
import be.florens.expandability.api.fabric.LivingFluidCollisionCallback;
import be.florens.expandability.api.fabric.PlayerSwimCallback;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;

public class SwimEventsFabric {

    public static void register() {
        PlayerSwimCallback.EVENT.register(SwimEventsFabric::onPlayerSwim);
        LivingFluidCollisionCallback.EVENT.register(SwimEventsFabric::onAquaDashersFluidCollision);
    }

    private static EventResult onPlayerSwim(Player player) {
        return SwimEvents.onPlayerSwim(player);
    }

    private static boolean onAquaDashersFluidCollision(LivingEntity entity, FluidState fluidState) {
        return SwimEvents.onFluidCollision(entity, fluidState);
    }
}
