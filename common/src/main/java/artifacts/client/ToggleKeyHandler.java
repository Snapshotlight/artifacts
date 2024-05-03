package artifacts.client;

import artifacts.ability.ArtifactAbility;
import artifacts.network.NetworkHandler;
import artifacts.network.ToggleArtifactPacket;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModKeyMappings;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.KeyMapping;

import java.util.HashMap;
import java.util.Map;

public class ToggleKeyHandler {

    private static final Map<ArtifactAbility.Type<?>, KeyMapping> TOGGLE_KEY_MAPPINGS = new HashMap<>();

    public static void register() {
        addToggleInputHandler(ModAbilities.NIGHT_VISION.get(), ModKeyMappings.TOGGLE_NIGHT_VISION_GOGGLES);
        addToggleInputHandler(ModAbilities.ATTRACT_ITEMS.get(), ModKeyMappings.TOGGLE_UNIVERSAL_ATTRACTOR);
    }

    public static KeyMapping getToggleKey(ArtifactAbility.Type<?> ability) {
        return TOGGLE_KEY_MAPPINGS.get(ability);
    }

    private static void addToggleInputHandler(ArtifactAbility.Type<?> ability, KeyMapping toggleKey) {
        TOGGLE_KEY_MAPPINGS.put(ability, toggleKey);
        ToggleInputHandler handler = new ToggleInputHandler(ability);
        ClientTickEvent.CLIENT_PRE.register(instance -> handler.onClientTick());
    }

    private static class ToggleInputHandler {

        private boolean wasToggleKeyDown;
        private final ArtifactAbility.Type<?> ability;

        public ToggleInputHandler(ArtifactAbility.Type<?> ability) {
            this.ability = ability;
        }

        public void onClientTick() {
            boolean isToggleKeyDown = getToggleKey(ability).isDown();
            if (isToggleKeyDown && !wasToggleKeyDown) {
                NetworkHandler.CHANNEL.sendToServer(new ToggleArtifactPacket(ability));
            }
            wasToggleKeyDown = isToggleKeyDown;
        }
    }
}
