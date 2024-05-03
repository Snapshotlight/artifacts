package artifacts.item.wearable.head;

import artifacts.ability.mobeffect.MountSpeedAbility;
import artifacts.item.wearable.WearableArtifactItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class CowboyHatItem extends WearableArtifactItem {

    public CowboyHatItem() {
        super(new MountSpeedAbility());
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }
}
