package artifacts.ability.mobeffect;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import net.minecraft.world.effect.MobEffects;

public class InvisibilityAbility extends MobEffectAbility {

    public InvisibilityAbility() {
        super(MobEffects.INVISIBILITY, () -> ModGameRules.SCARF_OF_INVISIBILITY_ENABLED.get() ? 1 : 0);
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.INVISIBILITY;
    }
}
