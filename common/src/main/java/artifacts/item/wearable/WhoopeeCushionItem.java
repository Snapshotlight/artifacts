package artifacts.item.wearable;

import artifacts.ability.FartAbility;
import artifacts.registry.ModSoundEvents;
import net.minecraft.sounds.SoundEvent;

public class WhoopeeCushionItem extends WearableArtifactItem {

    public WhoopeeCushionItem() {
        super(new FartAbility());
    }

    @Override
    public SoundEvent getEquipSound() {
        return ModSoundEvents.FART.get();
    }
}
