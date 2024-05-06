package artifacts.ability.mobeffect;

import artifacts.ability.ArtifactAbility;
import artifacts.ability.value.BooleanValue;
import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
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
            IntegerValue.field(ModGameRules.SNORKEL_WATER_BREATHING_DURATION).forGetter(LimitedWaterBreathingAbility::maxDuration),
            BooleanValue.field(ModGameRules.SNORKEL_IS_INFINITE).forGetter(LimitedWaterBreathingAbility::isInfinite)
    ).apply(instance, LimitedWaterBreathingAbility::new));

    public static final StreamCodec<ByteBuf, LimitedWaterBreathingAbility> STREAM_CODEC = StreamCodec.composite(
            IntegerValue.defaultStreamCodec(ModGameRules.SNORKEL_WATER_BREATHING_DURATION),
            LimitedWaterBreathingAbility::maxDuration,
            BooleanValue.defaultStreamCodec(ModGameRules.SNORKEL_IS_INFINITE),
            LimitedWaterBreathingAbility::isInfinite,
            LimitedWaterBreathingAbility::new
    );

    private final IntegerValue duration;
    private final BooleanValue isInfinite;

    public LimitedWaterBreathingAbility(IntegerValue duration, BooleanValue isInfinite) {
        super(MobEffects.WATER_BREATHING);
        this.duration = duration;
        this.isInfinite = isInfinite;
    }

    public static ArtifactAbility createDefaultInstance() {
        return ArtifactAbility.createDefaultInstance(CODEC);
    }

    private IntegerValue maxDuration() {
        return duration;
    }

    private BooleanValue isInfinite() {
        return isInfinite;
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.LIMITED_WATER_BREATHING.get();
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        if (isInfinite().get()) {
            tooltip.add(tooltipLine("infinite"));
        } else {
            tooltip.add(tooltipLine("limited"));
        }
    }

    @Override
    protected int getDuration(LivingEntity entity) {
        int duration = maxDuration().get();
        if (!isInfinite().get()
                && entity instanceof Player
                && entity.getItemBySlot(EquipmentSlot.HEAD).is(Items.TURTLE_HELMET)
                && !entity.isEyeInFluid(FluidTags.WATER)
        ) {
            duration += 200;
        }
        return duration + 19;
    }

    @Override
    protected boolean shouldShowIcon() {
        return !isInfinite().get();
    }

    @Override
    public boolean shouldApplyMobEffect(LivingEntity entity) {
        return isInfinite().get() || !entity.isEyeInFluid(FluidTags.WATER);
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
