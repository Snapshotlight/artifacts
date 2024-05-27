package artifacts.ability.mobeffect;

import artifacts.config.value.Value;
import artifacts.config.value.ValueTypes;
import artifacts.registry.ModAbilities;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Objects;

public class LimitedWaterBreathingAbility extends MobEffectAbility {

    public static final MapCodec<LimitedWaterBreathingAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ValueTypes.DURATION.codec().fieldOf("duration").forGetter(LimitedWaterBreathingAbility::maxDuration),
            ValueTypes.BOOLEAN.codec().optionalFieldOf("infinite", Value.Constant.FALSE).forGetter(ability -> ability.isInfinite)
    ).apply(instance, LimitedWaterBreathingAbility::new));

    public static final StreamCodec<ByteBuf, LimitedWaterBreathingAbility> STREAM_CODEC = StreamCodec.composite(
            ValueTypes.DURATION.streamCodec(),
            LimitedWaterBreathingAbility::maxDuration,
            ValueTypes.BOOLEAN.streamCodec(),
            ability -> ability.isInfinite,
            LimitedWaterBreathingAbility::new
    );

    private final Value<Integer> duration;
    private final Value<Boolean> isInfinite;

    public LimitedWaterBreathingAbility(Value<Integer> duration, Value<Boolean> isInfinite) {
        super(MobEffects.WATER_BREATHING);
        this.duration = duration;
        this.isInfinite = isInfinite;
    }

    private Value<Integer> maxDuration() {
        return duration;
    }

    public boolean isInfinite() {
        return isInfinite.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.LIMITED_WATER_BREATHING.get();
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        if (isInfinite()) {
            tooltip.add(tooltipLine("infinite"));
        } else {
            tooltip.add(tooltipLine("limited"));
        }
    }

    @Override
    public int getDuration() {
        return maxDuration().get() * 20;
    }

    protected int getAdditionalDuration(LivingEntity target) {
        if (!isInfinite()
                && target instanceof Player
                && target.getItemBySlot(EquipmentSlot.HEAD).is(Items.TURTLE_HELMET)
                && !target.isEyeInFluid(FluidTags.WATER)
        ) {
            return  200;
        }
        return 0;
    }

    @Override
    protected boolean shouldShowIcon() {
        return !isInfinite();
    }

    @Override
    public boolean shouldApplyMobEffect(LivingEntity entity) {
        return isInfinite() || !entity.isEyeInFluid(FluidTags.WATER);
    }

    @Override
    public boolean isNonCosmetic() {
        return maxDuration().get() > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LimitedWaterBreathingAbility that = (LimitedWaterBreathingAbility) o;
        return duration.equals(that.duration) && isInfinite.equals(that.isInfinite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), duration, isInfinite);
    }
}
