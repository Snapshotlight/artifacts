package artifacts.item.wearable.belt;

import artifacts.ability.DoubleJumpAbility;
import artifacts.item.wearable.WearableArtifactItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class CloudInABottleItem extends WearableArtifactItem {

    public CloudInABottleItem() {
        super(new DoubleJumpAbility());
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.BOTTLE_FILL_DRAGONBREATH;
    }
}
