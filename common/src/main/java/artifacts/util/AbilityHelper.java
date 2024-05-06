package artifacts.util;

import artifacts.ability.ArtifactAbility;
import artifacts.component.AbilityToggles;
import artifacts.item.WearableArtifactItem;
import artifacts.platform.PlatformServices;
import artifacts.registry.ModAbilities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class AbilityHelper {

    public static <A extends ArtifactAbility, T> T reduce(ArtifactAbility.Type<A> type, LivingEntity entity, boolean skipItemsOnCooldown, T init, BiFunction<A, T, T> f) {
        return PlatformServices.platformHelper.reduceItems(entity, init, (stack, init_) -> {
            for (ArtifactAbility ability : WearableArtifactItem.getAbilities(stack)) {
                if (ability.getType() == type && (!skipItemsOnCooldown || !isOnCooldown(entity, stack))) {
                    //noinspection unchecked
                    init_ = f.apply(((A) ability), init_);
                }
            }
            return init_;
        });
    }

    private static boolean isOnCooldown(LivingEntity entity, ItemStack stack) {
        return !(entity instanceof Player player) || player.getCooldowns().isOnCooldown(stack.getItem());
    }

    public static boolean hasAbility(ArtifactAbility.Type<?> type, ItemStack stack) {
        for (ArtifactAbility ability : WearableArtifactItem.getAbilities(stack)) {
            if (ability.getType() == type && ability.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isToggledOn(ArtifactAbility.Type<?> type, LivingEntity entity) {
        AbilityToggles abilityToggles = PlatformServices.platformHelper.getAbilityToggles(entity);
        if (abilityToggles != null) {
            return abilityToggles.isToggledOn(type);
        }
        return true;
    }

    public static boolean hasAbilityActive(ArtifactAbility.Type<?> type, @Nullable LivingEntity entity) {
        return hasAbilityActive(type, entity, false);
    }

    public static boolean hasAbilityActive(ArtifactAbility.Type<?> type, @Nullable LivingEntity entity, boolean skipItemsOnCooldown) {
        if (entity == null || !isToggledOn(type, entity)) {
            return false;
        }
        return reduce(type, entity, skipItemsOnCooldown, false, (ability, b) -> b || ability.isEnabled());
    }

    @SuppressWarnings("unchecked")
    public static <T extends ArtifactAbility> Stream<T> getAbilities(ArtifactAbility.Type<T> type, ItemStack stack) {
        return WearableArtifactItem.getAbilities(stack)
                .stream()
                .filter(ability -> ability.getType() == type)
                .map(ability -> (T) ability);
    }

    public static int getEnchantmentSum(Enchantment enchantment, LivingEntity entity) {
        return sumInt(ModAbilities.INCREASE_ENCHANTMENT_LEVEL.get(), entity, ability ->
                ability.enchantment() == enchantment ? ability.getAmount() : 0, false
        );
    }

    public static <A extends ArtifactAbility> int sumInt(ArtifactAbility.Type<A> type, LivingEntity entity, Function<A, Integer> f, boolean skipItemsOnCooldown) {
        return reduce(type, entity, skipItemsOnCooldown, 0, (ability, i) -> i + f.apply(ability));
    }

    public static <A extends ArtifactAbility> double sumDouble(ArtifactAbility.Type<A> type, LivingEntity entity, Function<A, Double> f, boolean skipItemsOnCooldown) {
        return reduce(type, entity, skipItemsOnCooldown, 0D, (ability, i) -> i + f.apply(ability));
    }

    public static <A extends ArtifactAbility> double maxDouble(ArtifactAbility.Type<A> type, LivingEntity entity, Function<A, Double> f, boolean skipItemsOnCooldown) {
        return reduce(type, entity, skipItemsOnCooldown, 0D, (ability, d) -> Math.max(d, f.apply(ability)));
    }

    public static <A extends ArtifactAbility> double minDouble(ArtifactAbility.Type<A> type, LivingEntity entity, double init, Function<A, Double> f, boolean skipItemsOnCooldown) {
        return reduce(type, entity, skipItemsOnCooldown, init, (ability, d) -> Math.min(d, f.apply(ability)));
    }

    public static <A extends ArtifactAbility> int maxInt(ArtifactAbility.Type<A> type, LivingEntity entity, Function<A, Integer> f, boolean skipItemsOnCooldown) {
        return reduce(type, entity, skipItemsOnCooldown, 0, (ability, d) -> Math.max(d, f.apply(ability)));
    }

    public static void addCooldown(ArtifactAbility.Type<?> type, LivingEntity entity, int ticks) {
        if (ticks > 0 && !entity.level().isClientSide() && entity instanceof Player player) {
            PlatformServices.platformHelper.findAllEquippedBy(entity, stack -> hasAbility(type, stack))
                    .forEach(stack -> player.getCooldowns().addCooldown(stack.getItem(), ticks));
        }
    }

    public static <A extends ArtifactAbility> void applyCooldowns(ArtifactAbility.Type<A> type, LivingEntity entity, Function<A, Integer> cooldown) {
        if (entity instanceof Player player && !player.level().isClientSide()) {
            forEach(type, entity, (ability, stack) -> {
                int c = cooldown.apply(ability);
                if (c > 0) {
                    player.getCooldowns().addCooldown(stack.getItem(), c);
                }
            }, true);
        }
    }

    public static <A extends ArtifactAbility> void forEach(ArtifactAbility.Type<A> type, LivingEntity entity, Consumer<A> consumer) {
        forEach(type, entity, (ability, stack) -> consumer.accept(ability), false);
    }

    public static <A extends ArtifactAbility> void forEach(ArtifactAbility.Type<A> type, LivingEntity entity, BiConsumer<A, ItemStack> consumer, boolean skipItemsOnCooldown) {
        PlatformServices.platformHelper.findAllEquippedBy(entity, stack -> hasAbility(type, stack))
                .forEach(stack -> getAbilities(type, stack)
                        .filter(ArtifactAbility::isEnabled)
                        .filter(ability -> !skipItemsOnCooldown || !(entity instanceof Player player) || !player.getCooldowns().isOnCooldown(stack.getItem()))
                        .forEach(ability -> consumer.accept(ability, stack))
                );
    }
}
