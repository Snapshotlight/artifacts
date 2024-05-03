package artifacts.item.wearable;

import artifacts.ability.ArtifactAbility;
import artifacts.item.ArtifactItem;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class WearableArtifactItem extends ArtifactItem {

    private final List<ArtifactAbility> abilities = new ArrayList<>();

    public WearableArtifactItem(Properties properties, ArtifactAbility... abilities) {
        super(properties);
        this.abilities.addAll(Set.of(abilities));
    }

    public WearableArtifactItem(ArtifactAbility... abilities) {
        this(new Properties(), abilities);
    }

    public static Collection<ArtifactAbility> getAbilities(ItemStack stack) {
        if (stack.getItem() instanceof WearableArtifactItem item) {
            return item.abilities;
        }
        return List.of();
    }

    public void onEquip(LivingEntity entity, ItemStack stack) {
        if (entity.level().isClientSide()) {
            return;
        }
        for (ArtifactAbility ability : getAbilities(stack)) {
            ability.onEquip(entity, ability.isEnabled());
        }
    }

    public void onUnequip(LivingEntity entity, ItemStack stack) {
        if (entity.level().isClientSide()) {
            return;
        }
        for (ArtifactAbility ability : getAbilities(stack)) {
            ability.onUnequip(entity, ability.isEnabled());
        }
    }

    public void wornTick(LivingEntity entity, ItemStack stack) {
        if (entity.level().isClientSide() || entity.isSpectator()) {
            return;
        }
        for (ArtifactAbility ability : getAbilities(stack)) {
            boolean isActive = ability.isEnabled();
            boolean isOnCooldown = entity instanceof Player player && player.getCooldowns().isOnCooldown(this);
            ability.wornTick(entity, isOnCooldown, isActive);
        }
    }

    @Override
    public final boolean isCosmetic(ItemStack stack) {
        for (ArtifactAbility ability : getAbilities(stack)) {
            if (ability.isNonCosmetic()) {
                return false;
            }
        }
        return true;
    }

    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_GENERIC;
    }

    @Override
    protected void addEffectsTooltip(ItemStack stack, List<MutableComponent> tooltip) {
        for (ArtifactAbility ability : getAbilities(stack)) {
            ability.addTooltipIfNonCosmetic(tooltip);
        }
    }
}
