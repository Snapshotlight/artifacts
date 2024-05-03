package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.UseAnim;

import java.util.List;

public class ReduceEatingDurationAbility implements ArtifactAbility {

    private final ModGameRules.DoubleValue durationMultiplier;
    private final UseAnim anim;

    public ReduceEatingDurationAbility(ModGameRules.DoubleValue durationMultiplier, UseAnim anim) {
        this.durationMultiplier = durationMultiplier;
        this.anim = anim;
    }

    public static int getDrinkingHatUseDuration(LivingEntity entity, UseAnim anim, int original) {
        if (anim != UseAnim.EAT && anim != UseAnim.DRINK) {
            return original;
        }
        double newDuration = original * AbilityHelper.minDouble(
                ModAbilities.REDUCE_EATING_DURATION, entity, 1,
                ability -> ability.getAnim() == anim ? ability.getDurationMultiplier() : 1,
                false);
        return (int) newDuration;
    }

    public double getDurationMultiplier() {
        return durationMultiplier.get();
    }

    public UseAnim getAnim() {
        return anim;
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.REDUCE_EATING_DURATION;
    }

    @Override
    public boolean isNonCosmetic() {
        return getDurationMultiplier() + 1e-10 < 1;
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        tooltip.add(tooltipLine(getAnim().name().toLowerCase()));
    }
}
