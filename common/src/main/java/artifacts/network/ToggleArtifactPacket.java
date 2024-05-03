package artifacts.network;

import artifacts.ability.ArtifactAbility;
import artifacts.registry.ModAbilities;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Supplier;

public class ToggleArtifactPacket {

    private final ArtifactAbility.Type<?> ability;

    public ToggleArtifactPacket(FriendlyByteBuf buffer) {
        // TODO use registry for abilities
        ResourceLocation id = buffer.readResourceLocation();
        this.ability = ModAbilities.REGISTRY.get(id);
    }

    public ToggleArtifactPacket(ArtifactAbility.Type<?> ability) {
        this.ability = ability;
    }

    void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(ModAbilities.REGISTRY.getId(ability));
    }

    void apply(Supplier<NetworkManager.PacketContext> context) {
        if (context.get().getPlayer() instanceof ServerPlayer player) {
            // TODO ability toggles
            // context.get().queue(() -> WearableArtifactItem.toggleItem(ability, player));
        }
    }
}
