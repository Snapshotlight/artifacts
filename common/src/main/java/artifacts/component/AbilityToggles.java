package artifacts.component;

import artifacts.ability.ArtifactAbility;
import artifacts.network.NetworkHandler;
import artifacts.network.ToggleArtifactPacket;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.*;

public class AbilityToggles {

    public static Codec<AbilityToggles> CODEC = ResourceLocation.CODEC.listOf().xmap(
            list -> {
                Set<ArtifactAbility.Type<?>> toggles = new HashSet<>();
                for (ResourceLocation id : list) {
                    toggles.add(ModAbilities.REGISTRY.get(id));
                }
                return new AbilityToggles(toggles);
            },
            abilityToggles -> {
                List<ResourceLocation> list = new ArrayList<>();
                for (ArtifactAbility.Type<?> toggle : abilityToggles.toggles) {
                    ResourceLocation id = ModAbilities.REGISTRY.getId(toggle);
                    list.add(id);
                }
                return list;
            }
    );

    protected final Set<ArtifactAbility.Type<?>> toggles = new HashSet<>();

    public AbilityToggles() {
        this(Set.of());
    }

    public AbilityToggles(Collection<ArtifactAbility.Type<?>> toggles) {
        this.toggles.addAll(toggles);
    }

    public boolean isToggledOn(ArtifactAbility.Type<?> type) {
        return !toggles.contains(type);
    }

    public void toggle(ArtifactAbility.Type<?> type, LivingEntity entity) {
        if (toggles.contains(type)) {
            toggles.remove(type);
            if (!entity.level().isClientSide()) {
                AbilityHelper.forEach(type, entity, ability -> ability.onEquip(entity, ability.isEnabled()));
            }
        } else {
            toggles.add(type);
            if (!entity.level().isClientSide()) {
                AbilityHelper.forEach(type, entity, ability -> ability.onUnequip(entity, ability.isEnabled()));
            }
        }
    }

    public void applyToggles(Set<ArtifactAbility.Type<?>> toggles, LivingEntity entity) {
        for (ArtifactAbility.Type<?> type : new HashSet<>(Sets.symmetricDifference(this.toggles, toggles))) {
            toggle(type, entity);
        }
    }

    public void sendToServer() {
        NetworkHandler.CHANNEL.sendToServer(new ToggleArtifactPacket(toggles));
    }

    public void sendToClient(ServerPlayer player) {
        NetworkHandler.CHANNEL.sendToPlayer(player, new ToggleArtifactPacket(toggles));
    }
}
