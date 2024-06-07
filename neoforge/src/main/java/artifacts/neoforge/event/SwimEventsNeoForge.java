package artifacts.neoforge.event;

import artifacts.component.SwimEvents;
import be.florens.expandability.api.EventResult;
import be.florens.expandability.api.forge.LivingFluidCollisionEvent;
import be.florens.expandability.api.forge.PlayerSwimEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;

public class SwimEventsNeoForge {

    public static void register() {
        if (ModList.get().isLoaded("expandability")) {
            NeoForge.EVENT_BUS.addListener(SwimEventsNeoForge::onPlayerSwim);
            NeoForge.EVENT_BUS.addListener(SwimEventsNeoForge::onAquaDashersFluidCollision);
        }
    }

    public static void onPlayerSwim(PlayerSwimEvent event) {
        if (event.getResult() == EventResult.PASS) {
            event.setResult(SwimEvents.onPlayerSwim(event.getEntity()));
        }
    }

    private static void onAquaDashersFluidCollision(LivingFluidCollisionEvent event) {
        if (SwimEvents.onFluidCollision(event.getEntity(), event.getFluidState())) {
            event.setColliding(true);
        }
    }
}
