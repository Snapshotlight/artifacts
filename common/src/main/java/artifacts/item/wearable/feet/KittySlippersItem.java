package artifacts.item.wearable.feet;

import artifacts.ability.HurtSoundAbility;
import artifacts.ability.SimpleAbility;
import artifacts.item.wearable.WearableArtifactItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class KittySlippersItem extends WearableArtifactItem {

    public KittySlippersItem() {
        super(SimpleAbility.scareCreepers(), new HurtSoundAbility(SoundEvents.CAT_HURT));
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.CAT_AMBIENT;
    }
}
