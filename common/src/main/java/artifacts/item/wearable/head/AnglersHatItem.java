package artifacts.item.wearable.head;

import artifacts.ability.IncreaseEnchantmentLevelAbility;
import artifacts.item.wearable.WearableArtifactItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class AnglersHatItem extends WearableArtifactItem {

    public AnglersHatItem() {
        super(IncreaseEnchantmentLevelAbility.luckOfTheSea(), IncreaseEnchantmentLevelAbility.lure());
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }
}
