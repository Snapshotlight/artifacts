package artifacts.ability.mobeffect;

import artifacts.ability.value.DoubleValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffects;

import java.util.List;
import java.util.Objects;

public class NightVisionAbility extends MobEffectAbility {

    public static final MapCodec<NightVisionAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DoubleValue.field("strength", ModGameRules.NIGHT_VISION_GOGGLES_STRENGTH).forGetter(NightVisionAbility::strength)
    ).apply(instance, NightVisionAbility::new));

    public static final StreamCodec<ByteBuf, NightVisionAbility> STREAM_CODEC = StreamCodec.composite(
            DoubleValue.defaultStreamCodec(ModGameRules.NIGHT_VISION_GOGGLES_STRENGTH),
            NightVisionAbility::strength,
            NightVisionAbility::new
    );

    private final DoubleValue strength;

    public NightVisionAbility(DoubleValue strength) {
        super(MobEffects.NIGHT_VISION);
        this.strength = strength;
    }

    public DoubleValue strength() {
        return strength;
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.NIGHT_VISION.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return !strength().fuzzyEquals(0);
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        if (ModGameRules.NIGHT_VISION_GOGGLES_STRENGTH.get() > 0.5) {
            tooltip.add(tooltipLine("full"));
        } else {
            tooltip.add(tooltipLine("partial"));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NightVisionAbility that = (NightVisionAbility) o;
        return strength.equals(that.strength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), strength);
    }
}
