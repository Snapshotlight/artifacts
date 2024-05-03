package artifacts.item.wearable.belt;

import artifacts.ability.SwimInAirAbility;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModSoundEvents;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class HeliumFlamingoItem extends WearableArtifactItem {

    public HeliumFlamingoItem() {
        super(new SwimInAirAbility());
    }

    @NotNull
    @Override
    public SoundEvent getEquipSound() {
        return ModSoundEvents.POP.get();
    }
}
