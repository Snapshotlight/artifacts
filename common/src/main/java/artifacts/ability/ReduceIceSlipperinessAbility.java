package artifacts.ability;

import artifacts.ability.value.DoubleValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModTags;
import artifacts.util.AbilityHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;

public record ReduceIceSlipperinessAbility(DoubleValue slipperinessReduction) implements ArtifactAbility {

    public static final MapCodec<ReduceIceSlipperinessAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DoubleValue.field(ModGameRules.SNOWSHOES_SLIPPERINESS_REDUCTION).forGetter(ReduceIceSlipperinessAbility::slipperinessReduction)
    ).apply(instance, ReduceIceSlipperinessAbility::new));

    public static final StreamCodec<ByteBuf, ReduceIceSlipperinessAbility> STREAM_CODEC = StreamCodec.composite(
            DoubleValue.defaultStreamCodec(ModGameRules.SNOWSHOES_SLIPPERINESS_REDUCTION),
            ReduceIceSlipperinessAbility::slipperinessReduction,
            ReduceIceSlipperinessAbility::new
    );

    public static ArtifactAbility createDefaultInstance() {
        return ArtifactAbility.createDefaultInstance(CODEC);
    }

    public static float getModifiedFriction(float friction, LivingEntity entity, Block block) {
        if (ModTags.isInTag(block, BlockTags.ICE)
                && friction > 0.6F
        ) {
            double slipperinessReduction = AbilityHelper.maxDouble(
                    ModAbilities.REDUCE_ICE_SLIPPERINESS.get(),
                    entity,
                    ability -> ability.slipperinessReduction().get(),
                    false
            );
            return Mth.lerp(((float) slipperinessReduction), friction, 0.6F);
        }
        return friction;
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.REDUCE_ICE_SLIPPERINESS.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return !slipperinessReduction().fuzzyEquals(0);
    }
}
