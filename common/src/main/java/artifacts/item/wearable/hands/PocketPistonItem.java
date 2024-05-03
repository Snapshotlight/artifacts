package artifacts.item.wearable.hands;

import artifacts.ability.KnockbackAbility;
import artifacts.item.wearable.WearableArtifactItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class PocketPistonItem extends WearableArtifactItem {

    public PocketPistonItem() {
        super(new KnockbackAbility());
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.PISTON_EXTEND;
    }
}
