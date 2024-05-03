package artifacts.ability;

import artifacts.registry.ModAbilities;
import net.minecraft.sounds.SoundEvent;

public class HurtSoundAbility implements ArtifactAbility {

    private final SoundEvent soundEvent;

    public HurtSoundAbility(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.MODIFY_HURT_SOUND;
    }

    @Override
    public boolean isNonCosmetic() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
