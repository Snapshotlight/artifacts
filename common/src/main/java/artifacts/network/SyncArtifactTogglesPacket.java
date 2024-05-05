package artifacts.network;

import artifacts.Artifacts;
import artifacts.ability.ArtifactAbility;
import artifacts.component.AbilityToggles;
import artifacts.platform.PlatformServices;
import artifacts.registry.ModAbilities;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public record SyncArtifactTogglesPacket(List<ArtifactAbility.Type<?>> toggles) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncArtifactTogglesPacket> TYPE = new CustomPacketPayload.Type<>(Artifacts.id("sync_artifact_toggles"));

    public static final StreamCodec<FriendlyByteBuf, SyncArtifactTogglesPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.<ByteBuf, ArtifactAbility.Type<?>>list().apply(
                    ResourceLocation.STREAM_CODEC.map(ModAbilities.REGISTRY::get, ModAbilities.REGISTRY::getId)
            ),
            SyncArtifactTogglesPacket::toggles,
            SyncArtifactTogglesPacket::new
    );

    void apply(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            AbilityToggles abilityToggles = PlatformServices.platformHelper.getAbilityToggles(player);
            if (abilityToggles != null) {
                abilityToggles.applyToggles(toggles, context.getPlayer());
            }
        }
    }

    @Override
    public CustomPacketPayload.Type<SyncArtifactTogglesPacket> type() {
        return TYPE;
    }
}
