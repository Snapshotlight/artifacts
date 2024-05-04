package artifacts.ability;

import artifacts.Artifacts;
import artifacts.client.ToggleKeyHandler;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ArtifactAbility {

    Type<?> getType();

    boolean isNonCosmetic();

    default boolean isEnabled() {
        return isNonCosmetic();
    }

    default boolean isActive(LivingEntity entity) {
        return isEnabled() && AbilityHelper.isToggledOn(getType(), entity);
    }

    default void addTooltipIfNonCosmetic(List<MutableComponent> tooltip, @Nullable Player player) {
        if (isNonCosmetic()) {
            addAbilityTooltip(tooltip);
            addToggleKeyTooltip(tooltip, player);
        }
    }

    @SuppressWarnings("ConstantConditions")
    default void addAbilityTooltip(List<MutableComponent> tooltip) {
        ResourceLocation id = ModAbilities.REGISTRY.getId(getType());
        tooltip.add(Component.translatable("%s.tooltip.ability.%s".formatted(id.getNamespace(), id.getPath())));
    }

    default void addToggleKeyTooltip(List<MutableComponent> tooltip, @Nullable Player player) {
        KeyMapping key = ToggleKeyHandler.getToggleKey(this.getType());
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

    default void onEquip(LivingEntity entity, boolean isActive) {

    }

    default void onUnequip(LivingEntity entity, boolean wasActive) {

    }

    @SuppressWarnings("unused")
    class Type<T extends ArtifactAbility> {

    }
}
