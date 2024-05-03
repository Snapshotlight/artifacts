package artifacts.neoforge.event;

import artifacts.component.SwimEvents;
import be.florens.expandability.api.forge.LivingFluidCollisionEvent;
import be.florens.expandability.api.forge.PlayerSwimEvent;
import dev.architectury.event.EventResult;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

public class SwimEventsNeoForge {

    public static void register() {
        NeoForge.EVENT_BUS.addListener(SwimEventsNeoForge::onPlayerSwim);
        NeoForge.EVENT_BUS.addListener(SwimEventsNeoForge::onAquaDashersFluidCollision);
    }

    public static void onPlayerSwim(PlayerSwimEvent event) {
        if (event.getResult() == Event.Result.DEFAULT) {
            EventResult result = SwimEvents.onPlayerSwim(event.getEntity());
            if (!result.interruptsFurtherEvaluation()) {
                event.setResult(Event.Result.DEFAULT);
            } else if (result.isTrue()) {
                event.setResult(Event.Result.ALLOW);
            } else {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    private static void onAquaDashersFluidCollision(LivingFluidCollisionEvent event) {
        if (SwimEvents.onFluidCollision(event.getEntity(), event.getFluidState())) {
            event.setResult(Event.Result.ALLOW);
        }
    }
}
