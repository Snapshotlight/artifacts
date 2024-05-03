package artifacts.item.wearable.belt;

import artifacts.ability.MakePiglinsNeutralAbility;
import artifacts.ability.RemoveBadEffectsAbility;
import artifacts.item.wearable.WearableArtifactItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class AntidoteVesselItem extends WearableArtifactItem {

    public AntidoteVesselItem() {
        super(new MakePiglinsNeutralAbility(), new RemoveBadEffectsAbility());
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.BOTTLE_FILL;
    }
}
