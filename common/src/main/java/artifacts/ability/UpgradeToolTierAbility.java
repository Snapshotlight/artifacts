package artifacts.ability;

import artifacts.Artifacts;
import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModTags;
import artifacts.util.AbilityHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public record UpgradeToolTierAbility(IntegerValue tierLevel) implements ArtifactAbility {

    public static final MapCodec<UpgradeToolTierAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IntegerValue.codec(5).fieldOf("tier").forGetter(UpgradeToolTierAbility::tierLevel)
    ).apply(instance, UpgradeToolTierAbility::new));

    public static final StreamCodec<ByteBuf, UpgradeToolTierAbility> STREAM_CODEC = StreamCodec.composite(
            IntegerValue.streamCodec(),
            UpgradeToolTierAbility::tierLevel,
            UpgradeToolTierAbility::new
    );

    public static boolean canHarvestWithTier(LivingEntity entity, BlockState state) {
        if (state.is(ModTags.MINEABLE_WITH_DIGGING_CLAWS)) {
            Optional<Tier> tier = getTier(AbilityHelper.maxInt(
                    ModAbilities.UPGRADE_TOOL_TIER.get(), entity,
                    ability -> ability.tierLevel().get(), false
            ));
            return tier.isPresent() && isCorrectTierForDrops(tier.get(), state);
        }
        return false;
    }

    public static Optional<Tier> getTier(int toolTier) {
        return switch (toolTier) {
            case 0 -> Optional.empty();
            case 1 -> Optional.of(Tiers.WOOD);
            case 2 -> Optional.of(Tiers.STONE);
            case 3 -> Optional.of(Tiers.IRON);
            case 4 -> Optional.of(Tiers.DIAMOND);
            default -> Optional.of(Tiers.NETHERITE);
        };
    }

    public static int getTierLevel(Tier tier) {
        if (!(tier instanceof Tiers tiers)) {
            return 0;
        }
        return switch (tiers) {
            case WOOD -> 1;
            case STONE -> 2;
            case IRON -> 3;
            case DIAMOND -> 4;
            case NETHERITE -> 5;
            default -> 0;
        };
    }

    public static boolean isCorrectTierForDrops(Tier tier, BlockState state) {
        int i = UpgradeToolTierAbility.getTierLevel(tier);
        if (state.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return i >= 4;
        } else if (state.is(BlockTags.NEEDS_IRON_TOOL)) {
            return i >= 3;
        } else if (state.is(BlockTags.NEEDS_STONE_TOOL)) {
            return i >= 2;
        }
        return true;
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.UPGRADE_TOOL_TIER.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return getTier(tierLevel().get()).isPresent();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        getTier(tierLevel().get()).ifPresent(tier ->  {
            ResourceLocation id = ModAbilities.REGISTRY.getId(getType());
            tooltip.add(
                    Component.translatable(
                            "%s.tooltip.ability.%s".formatted(id.getNamespace(), id.getPath()),
                            Component.translatable("%s.tooltip.tool_tier.%s".formatted(Artifacts.MOD_ID, getTierLevel(tier)))
                    )
            );
        });
    }
}
