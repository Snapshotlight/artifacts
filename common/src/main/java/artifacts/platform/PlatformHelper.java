package artifacts.platform;

import artifacts.ability.ArtifactAbility;
import artifacts.client.item.renderer.ArtifactRenderer;
import artifacts.component.AbilityToggles;
import artifacts.component.SwimData;
import artifacts.item.WearableArtifactItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface PlatformHelper {

    default boolean isEquippedBy(@Nullable LivingEntity entity, Item item) {
        return isEquippedBy(entity, stack -> stack.is(item));
    }

    boolean isEquippedBy(@Nullable LivingEntity entity, Predicate<ItemStack> predicate);

    default Stream<ItemStack> findAllEquippedBy(LivingEntity entity, Item item) {
        return findAllEquippedBy(entity, stack -> stack.is(item));
    }

    Stream<ItemStack> findAllEquippedBy(LivingEntity entity, Predicate<ItemStack> predicate);

    <T> T reduceItems(LivingEntity entity, T init, BiFunction<ItemStack, T, T> f);

    boolean tryEquipInFirstSlot(LivingEntity entity, ItemStack item);

    Attribute getStepHeightAttribute();

    boolean isCorrectTierForDrops(Tier tier, BlockState state);

    @Nullable
    AbilityToggles getAbilityToggles(LivingEntity entity);

    @Nullable
    SwimData getSwimData(LivingEntity entity);

    ArtifactAbility getFlippersSwimAbility();

    boolean isEyeInWater(Player player);

    boolean isVisibleOnHand(LivingEntity entity, InteractionHand hand, WearableArtifactItem item);

    boolean areBootsHidden(LivingEntity entity);

    void registerArtifactRenderer(WearableArtifactItem item, Supplier<ArtifactRenderer> rendererSupplier);

    @Nullable
    ArtifactRenderer getArtifactRenderer(Item item);
}
