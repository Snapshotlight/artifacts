package artifacts.ability;

import artifacts.Artifacts;
import artifacts.ArtifactsClient;
import artifacts.client.ToggleKeyHandler;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.function.Function;

public interface ArtifactAbility {

    @SuppressWarnings("ConstantConditions")
    Codec<ArtifactAbility> CODEC = ResourceLocation.CODEC
            .comapFlatMap(id -> ModAbilities.REGISTRY.contains(id)
                    ? DataResult.success(id)
                    : DataResult.error(() -> "Unknown ability type '%s'".formatted(id)),
                    Function.identity()
            ).dispatch("type",
                    ability -> ModAbilities.REGISTRY.getId(ability.getType()),
                    id -> ModAbilities.REGISTRY.get(id).codec()
            );

    @SuppressWarnings("ConstantConditions")
    StreamCodec<RegistryFriendlyByteBuf, ArtifactAbility> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(ResourceLocation.CODEC)
            .dispatch(
                    ability -> ModAbilities.REGISTRY.getId(ability.getType()),
                    id -> ModAbilities.REGISTRY.get(id).streamCodec()
            );

    Type<?> getType();

    boolean isNonCosmetic();

    default boolean isEnabled() {
        return isNonCosmetic();
    }

    default boolean isActive(LivingEntity entity) {
        return isEnabled() && AbilityHelper.isToggledOn(getType(), entity);
    }

    default void addTooltipIfNonCosmetic(List<MutableComponent> tooltip) {
        if (isNonCosmetic()) {
            addAbilityTooltip(tooltip);
            addToggleKeyTooltip(tooltip);
        }
    }

    @SuppressWarnings("ConstantConditions")
    default void addAbilityTooltip(List<MutableComponent> tooltip) {
        ResourceLocation id = ModAbilities.REGISTRY.getId(getType());
        tooltip.add(Component.translatable("%s.tooltip.ability.%s".formatted(id.getNamespace(), id.getPath())));
    }

    default void addToggleKeyTooltip(List<MutableComponent> tooltip) {
        KeyMapping key = ToggleKeyHandler.getToggleKey(this.getType());
        Player player = null;
        if (Minecraft.getInstance() != null) {
            player = ArtifactsClient.getLocalPlayer();
        }
        if (key != null && (!key.isUnbound() || !AbilityHelper.isToggledOn(getType(), player))) {
            tooltip.add(Component.translatable("%s.tooltip.toggle_keymapping".formatted(Artifacts.MOD_ID), key.getTranslatedKeyMessage()));
        }
    }

    @SuppressWarnings("ConstantConditions")
    default MutableComponent tooltipLine(String abilityName, Object... args) {
        ResourceLocation id = ModAbilities.REGISTRY.getId(getType());
        return Component.translatable("%s.tooltip.ability.%s.%s".formatted(id.getNamespace(), id.getPath(), abilityName), args);
    }

    default void wornTick(LivingEntity entity, boolean isOnCooldown, boolean isActive) {

    }

    default void onUnequip(LivingEntity entity, boolean wasActive) {

    }

    record Type<T extends ArtifactAbility>(MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {

    }
}
