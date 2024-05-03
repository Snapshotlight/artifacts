package artifacts.ability.mobeffect;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import net.minecraft.world.effect.MobEffects;

public class JumpBoostAbility extends MobEffectAbility {

    public JumpBoostAbility() {
        super(MobEffects.JUMP, ModGameRules.BUNNY_HOPPERS_JUMP_BOOST_LEVEL);
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.JUMP_BOOST;
    }
}
