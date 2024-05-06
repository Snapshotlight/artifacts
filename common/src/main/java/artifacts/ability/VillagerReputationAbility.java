package artifacts.ability;

import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record VillagerReputationAbility(IntegerValue reputationBonus) implements ArtifactAbility {

    public static final MapCodec<VillagerReputationAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IntegerValue.field(ModGameRules.VILLAGER_HAT_REPUTATION_BONUS).forGetter(VillagerReputationAbility::reputationBonus)
    ).apply(instance, VillagerReputationAbility::new));

    public static final StreamCodec<ByteBuf, VillagerReputationAbility> STREAM_CODEC = StreamCodec.composite(
            IntegerValue.defaultStreamCodec(ModGameRules.VILLAGER_HAT_REPUTATION_BONUS),
            VillagerReputationAbility::reputationBonus,
            VillagerReputationAbility::new
    );

    public static ArtifactAbility createDefaultInstance() {
        return ArtifactAbility.createDefaultInstance(CODEC);
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.VILLAGER_REPUTATION.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return reputationBonus().get() > 0;
    }
}
