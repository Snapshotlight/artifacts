package artifacts.item.wearable.hands;

import artifacts.ability.DigSpeedAbility;
import artifacts.ability.UpgradeToolTierAbility;
import artifacts.item.wearable.WearableArtifactItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class DiggingClawsItem extends WearableArtifactItem {

    public DiggingClawsItem() {
        super(new DigSpeedAbility(), new UpgradeToolTierAbility());
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_NETHERITE;
    }
}
