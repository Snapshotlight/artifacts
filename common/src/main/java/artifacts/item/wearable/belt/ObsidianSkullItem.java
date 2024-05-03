package artifacts.item.wearable.belt;

import artifacts.ability.ApplyFireResistanceAfterFireDamageAbility;
import artifacts.item.wearable.WearableArtifactItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class ObsidianSkullItem extends WearableArtifactItem {

    public ObsidianSkullItem() {
        super(new ApplyFireResistanceAfterFireDamageAbility());
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_IRON;
    }
}
