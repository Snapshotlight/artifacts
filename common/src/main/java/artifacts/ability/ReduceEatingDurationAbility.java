package artifacts.ability;

import artifacts.ability.value.DoubleValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.AbilityHelper;
import artifacts.util.ModCodecs;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.UseAnim;

import java.util.List;

public record ReduceEatingDurationAbility(DoubleValue durationMultiplier, Action action) implements ArtifactAbility {

    private static final List<ModGameRules.DoubleGameRule> DURATION_GAME_RULES = List.of(
            ModGameRules.NOVELTY_DRINKING_HAT_DRINKING_DURATION_MULTIPLIER,
            ModGameRules.NOVELTY_DRINKING_HAT_EATING_DURATION_MULTIPLIER,
            ModGameRules.PLASTIC_DRINKING_HAT_DRINKING_DURATION_MULTIPLIER,
            ModGameRules.PLASTIC_DRINKING_HAT_EATING_DURATION_MULTIPLIER
    );

    private static final StringRepresentable.StringRepresentableCodec<ModGameRules.DoubleGameRule> DURATION_CODEC = new StringRepresentable.StringRepresentableCodec<>(
            DURATION_GAME_RULES.toArray(ModGameRules.DoubleGameRule[]::new),
            ModGameRules.DOUBLE_VALUES::get,
            DURATION_GAME_RULES::indexOf
    );

    private static final StringRepresentable.StringRepresentableCodec<Action> ACTION_CODEC = new StringRepresentable.StringRepresentableCodec<>(
            Action.values(),
            Action::byName,
            Action::ordinal
    );

    public static final MapCodec<ReduceEatingDurationAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ModCodecs.xorAlternative(DURATION_CODEC.stable().flatXmap(
                    DataResult::success,
                    value -> value instanceof ModGameRules.DoubleGameRule gameRule
                            ? DataResult.success(gameRule)
                            : DataResult.error(() -> "Not a game rule")
            ), DoubleValue.codec(100, 1, 1)).fieldOf("level").forGetter(ReduceEatingDurationAbility::durationMultiplier),
            ACTION_CODEC.fieldOf("action").forGetter(ReduceEatingDurationAbility::action)
    ).apply(instance, ReduceEatingDurationAbility::new));

    @SuppressWarnings("SuspiciousMethodCalls")
    public static final StreamCodec<ByteBuf, ReduceEatingDurationAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL.dispatch(
                    DURATION_GAME_RULES::contains,
                    b -> b ? ByteBufCodecs.idMapper(DURATION_GAME_RULES::get, DURATION_GAME_RULES::indexOf) : DoubleValue.streamCodec()
            ),
            ReduceEatingDurationAbility::durationMultiplier,
            ByteBufCodecs.idMapper(i -> Action.values()[i], Action::ordinal),
            ReduceEatingDurationAbility::action,
            ReduceEatingDurationAbility::new
    );

    public static int getDrinkingHatUseDuration(LivingEntity entity, UseAnim anim, int original) {
        if (anim != UseAnim.EAT && anim != UseAnim.DRINK) {
            return original;
        }
        double newDuration = original * AbilityHelper.minDouble(
                ModAbilities.REDUCE_EATING_DURATION.get(), entity, 1,
                ability -> ability.action().getAnim() == anim ? ability.durationMultiplier().get() : 1,
                false);
        return (int) newDuration;
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.REDUCE_EATING_DURATION.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return !durationMultiplier().fuzzyEquals(1);
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        tooltip.add(tooltipLine(action().getSerializedName().toLowerCase()));
    }

    public enum Action implements StringRepresentable {
        EAT(UseAnim.EAT, "eat"),
        DRINK(UseAnim.DRINK, "drink");

        private final UseAnim anim;
        private final String name;

        Action(UseAnim anim, String name) {
            this.anim = anim;
            this.name = name;
        }

        public UseAnim getAnim() {
            return anim;
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        public static Action byName(String name) {
            if (name.equals(EAT.name)) {
                return EAT;
            } else if (name.equals(DRINK.name)) {
                return DRINK;
            }
            throw new IllegalArgumentException();
        }
    }
}
