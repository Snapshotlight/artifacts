package artifacts.item;

import artifacts.ability.ArtifactAbility;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class WearableArtifactItem extends ArtifactItem {

    private final List<ArtifactAbility> abilities = new ArrayList<>();

    private final Supplier<SoundEvent> equipSound;
    private final float equipSoundPitch;

    public WearableArtifactItem(ArtifactAbility... abilities) {
        this(new Item.Properties(), abilities);
    }

    public WearableArtifactItem(SoundEvent equipSound, ArtifactAbility... abilities) {
        this(new Item.Properties(), equipSound, abilities);
    }

    public WearableArtifactItem(Item.Properties properties, ArtifactAbility... abilities) {
        this(properties, SoundEvents.ARMOR_EQUIP_GENERIC, abilities);
    }

    public WearableArtifactItem(Item.Properties properties, SoundEvent equipSound, ArtifactAbility... abilities) {
        this(properties, () -> equipSound, 1F, abilities);
    }

    public WearableArtifactItem(Item.Properties properties, Supplier<SoundEvent> equipSound, float equipSoundPitch, ArtifactAbility... abilities) {
        super(properties);
        this.abilities.addAll(Set.of(abilities));
        this.equipSound = equipSound;
        this.equipSoundPitch = equipSoundPitch;
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
            ability.onEquip(entity, ability.isActive(entity));
        }
    }

    public void onUnequip(LivingEntity entity, ItemStack stack) {
        if (entity.level().isClientSide()) {
            return;
        }
        for (ArtifactAbility ability : getAbilities(stack)) {
            ability.onUnequip(entity, ability.isActive(entity));
        }
    }

    public void wornTick(LivingEntity entity, ItemStack stack) {
        if (entity.level().isClientSide() || entity.isSpectator()) {
            return;
        }
        for (ArtifactAbility ability : getAbilities(stack)) {
            boolean isActive = ability.isActive(entity);
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
        return equipSound.get();
    }

    public float getEquipSoundPitch() {
        return equipSoundPitch;
    }

    @Override
    protected void addEffectsTooltip(ItemStack stack, List<MutableComponent> tooltip, @Nullable Player player) {
        for (ArtifactAbility ability : getAbilities(stack)) {
            ability.addTooltipIfNonCosmetic(tooltip, player);
        }
    }
}
