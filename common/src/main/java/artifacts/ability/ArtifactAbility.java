package artifacts.ability;

import artifacts.Artifacts;
import artifacts.client.ToggleKeyHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public interface ArtifactAbility {

    Type<?> getType();

    boolean isNonCosmetic();

    default boolean isEnabled() {
        return isNonCosmetic();
    }

    default void addTooltipIfNonCosmetic(List<MutableComponent> tooltip) {
        if (isNonCosmetic()) {
            addAbilityTooltip(tooltip);
            addToggleKeyTooltip(tooltip);
        }
    }

    default void addAbilityTooltip(List<MutableComponent> tooltip) {
        tooltip.add(Component.translatable("%s.tooltip.ability.%s".formatted(getType().id.getNamespace(), getType().id.getPath())));
    }

    default void addToggleKeyTooltip(List<MutableComponent> tooltip) {
        KeyMapping key = ToggleKeyHandler.getToggleKey(this.getType());
        if (key != null && (!key.isUnbound() || !isEnabled())) {
            tooltip.add(Component.translatable("%s.tooltip.toggle_keymapping".formatted(Artifacts.MOD_ID), key.getTranslatedKeyMessage()));
        }
    }

    default MutableComponent tooltipLine(String abilityName, Object... args) {
        return Component.translatable("%s.tooltip.ability.%s.%s".formatted(getType().id.getNamespace(), getType().id.getPath(), abilityName), args);
    }

    default void wornTick(LivingEntity entity, boolean isOnCooldown, boolean isActive) {

    }

    default void onEquip(LivingEntity entity, boolean isActive) {

    }

    default void onUnequip(LivingEntity entity, boolean wasActive) {

    }

    @SuppressWarnings("unused")
    record Type<T extends ArtifactAbility>(ResourceLocation id) {

    }
}
