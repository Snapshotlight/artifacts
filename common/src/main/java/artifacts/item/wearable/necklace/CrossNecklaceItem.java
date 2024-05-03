package artifacts.item.wearable.necklace;

import artifacts.ability.BonusInvincibilityTicksAbility;
import artifacts.ability.MakePiglinsNeutralAbility;
import artifacts.item.wearable.WearableArtifactItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class CrossNecklaceItem extends WearableArtifactItem {

    public CrossNecklaceItem() {
        super(new MakePiglinsNeutralAbility(), new BonusInvincibilityTicksAbility());
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_DIAMOND;
    }
}
