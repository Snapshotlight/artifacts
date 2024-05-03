package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModTags;
import artifacts.util.AbilityHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;

public class ReduceIceSlipperinessAbility implements ArtifactAbility {

    public static float getModifiedFriction(float friction, LivingEntity entity, Block block) {
        if (ModTags.isInTag(block, BlockTags.ICE)
                && friction > 0.6F
        ) {
            double slipperinessReduction = AbilityHelper.maxDouble(
                    ModAbilities.REDUCE_ICE_SLIPPERINESS.get(),
                    entity,
                    ReduceIceSlipperinessAbility::getSlipperinessReduction,
                    false
            );
            return Mth.lerp(((float) slipperinessReduction), friction, 0.6F);
        }
        return friction;
    }

    public double getSlipperinessReduction() {
        return ModGameRules.SNOWSHOES_SLIPPERINESS_REDUCTION.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.REDUCE_ICE_SLIPPERINESS.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return getSlipperinessReduction() > 0;
    }
}
