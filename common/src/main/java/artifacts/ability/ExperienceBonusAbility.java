package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ExperienceBonusAbility implements ArtifactAbility {

    public static int getExperienceBonus(int originalXP, LivingEntity entity, Player attacker) {
        if (attacker == null || entity instanceof Player) {
            return 0;
        }

        double multiplier = AbilityHelper.maxDouble(ModAbilities.EXPERIENCE_BONUS.get(), attacker, ExperienceBonusAbility::getExperienceBonus, false);
        int experienceBonus = (int) (originalXP * multiplier);
        return Math.max(0, experienceBonus);
    }

    public double getExperienceBonus() {
        return ModGameRules.GOLDEN_HOOK_EXPERIENCE_BONUS.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.EXPERIENCE_BONUS.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return getExperienceBonus() > 0;
    }
}
