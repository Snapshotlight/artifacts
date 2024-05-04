package artifacts.ability;

import artifacts.Artifacts;
import artifacts.platform.PlatformServices;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModTags;
import artifacts.util.AbilityHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public class UpgradeToolTierAbility implements ArtifactAbility {

    public static boolean canHarvestWithTier(LivingEntity entity, BlockState state) {
        if (state.is(ModTags.MINEABLE_WITH_DIGGING_CLAWS)) {
            Optional<Tier> tier = getTier(AbilityHelper.maxInt(
                    ModAbilities.UPGRADE_TOOL_TIER.get(), entity,
                    UpgradeToolTierAbility::getToolTier, false
            ));
            return tier.isPresent() && PlatformServices.platformHelper.isCorrectTierForDrops(tier.get(), state);
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

    public int getToolTier() {
        return ModGameRules.DIGGING_CLAWS_TOOL_TIER.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.UPGRADE_TOOL_TIER.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return getTier(getToolTier()).isPresent();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        getTier(getToolTier()).ifPresent(tier ->  {
            ResourceLocation id = ModAbilities.REGISTRY.getId(getType());
            tooltip.add(
                    Component.translatable(
                            "%s.tooltip.ability.%s".formatted(id.getNamespace(), id.getPath()),
                            Component.translatable("%s.tooltip.tool_tier.%s".formatted(Artifacts.MOD_ID, tier.getLevel() + 1))
                    )
            );
        });
    }
}
