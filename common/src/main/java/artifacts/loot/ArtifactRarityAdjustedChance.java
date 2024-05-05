package artifacts.loot;

import artifacts.Artifacts;
import artifacts.registry.ModLootConditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record ArtifactRarityAdjustedChance(float defaultProbability) implements LootItemCondition {

    public static final MapCodec<ArtifactRarityAdjustedChance> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(Codec.FLOAT.fieldOf("default_probability").forGetter(ArtifactRarityAdjustedChance::defaultProbability))
                    .apply(instance, ArtifactRarityAdjustedChance::new)
    );

    @Override
    public LootItemConditionType getType() {
        return ModLootConditions.ARTIFACT_RARITY_ADJUSTED_CHANCE.get();
    }

    @Override
    public boolean test(LootContext context) {
        if (Artifacts.CONFIG.common.getArtifactRarity() > 9999) {
            return false;
        }
        float r = (float) Artifacts.CONFIG.common.getArtifactRarity();
        float p = defaultProbability;
        float adjustedProbability = p / (p + r - r * p);
        return context.getRandom().nextFloat() < adjustedProbability;
    }

    public static LootItemCondition.Builder adjustedChance(float probability) {
        return () -> new ArtifactRarityAdjustedChance(probability);
    }
}
