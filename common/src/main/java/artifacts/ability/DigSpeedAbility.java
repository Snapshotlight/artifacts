package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class DigSpeedAbility implements ArtifactAbility {

    public static float getSpeedBonus(Player player, BlockState state) {
        if (player.hasCorrectToolForDrops(state)) {
            return (float) AbilityHelper.sumDouble(ModAbilities.DIG_SPEED.get(), player, DigSpeedAbility::getSpeedBonus, false);
        }
        return 0;
    }

    public double getSpeedBonus() {
        return ModGameRules.DIGGING_CLAWS_DIG_SPEED_BONUS.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.DIG_SPEED.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return getSpeedBonus() > 0;
    }
}
