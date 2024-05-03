package artifacts.ability.mobeffect;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class NightVisionAbility extends MobEffectAbility {

    public NightVisionAbility() {
        super(MobEffects.NIGHT_VISION);
    }

    public double getStrength() {
        return ModGameRules.NIGHT_VISION_GOGGLES_STRENGTH.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.NIGHT_VISION;
    }

    @Override
    public boolean isNonCosmetic() {
        return getStrength() > 0;
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        if (ModGameRules.NIGHT_VISION_GOGGLES_STRENGTH.get() > 0.5) {
            tooltip.add(tooltipLine("full"));
        } else {
            tooltip.add(tooltipLine("partial"));
        }

    }
}
