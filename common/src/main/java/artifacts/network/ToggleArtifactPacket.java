package artifacts.network;

import artifacts.ability.ArtifactAbility;
import artifacts.component.AbilityToggles;
import artifacts.platform.PlatformServices;
import artifacts.registry.ModAbilities;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ToggleArtifactPacket {

    private final Set<ArtifactAbility.Type<?>> toggles;

    public ToggleArtifactPacket(FriendlyByteBuf buffer) {
        toggles = new HashSet<>();
        int count = buffer.readInt();
        for (int i = 0; i < count; i++) {
            toggles.add(ModAbilities.REGISTRY.get(buffer.readResourceLocation()));
        }
    }

    public ToggleArtifactPacket(Collection<ArtifactAbility.Type<?>> toggles) {
        this.toggles = new HashSet<>(toggles);
    }

    void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(toggles.size());
        for (ArtifactAbility.Type<?> ability : toggles) {
            buffer.writeResourceLocation(ModAbilities.REGISTRY.getId(ability));
        }
    }

    void apply(Supplier<NetworkManager.PacketContext> context) {
        Player player = context.get().getPlayer();
        if (player != null) {
            AbilityToggles abilityToggles = PlatformServices.platformHelper.getAbilityToggles(player);
            if (abilityToggles != null) {
                abilityToggles.applyToggles(toggles, context.get().getPlayer());
            }
        }
    }
}
