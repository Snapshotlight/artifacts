package artifacts.item.wearable.necklace;

import artifacts.ability.ApplySpeedAfterDamageAbility;
import artifacts.item.wearable.WearableArtifactItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class PanicNecklaceItem extends WearableArtifactItem {

    public PanicNecklaceItem() {
        super(new ApplySpeedAfterDamageAbility());
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_DIAMOND;
    }
}
