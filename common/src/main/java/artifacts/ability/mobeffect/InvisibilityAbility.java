package artifacts.ability.mobeffect;

import artifacts.ability.value.BooleanValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffects;

import java.util.Objects;

public class InvisibilityAbility extends MobEffectAbility {

    public static final MapCodec<InvisibilityAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BooleanValue.enabledField(ModGameRules.SCARF_OF_INVISIBILITY_ENABLED).forGetter(InvisibilityAbility::enabled)
    ).apply(instance, InvisibilityAbility::new));

    public static final StreamCodec<ByteBuf, InvisibilityAbility> STREAM_CODEC = StreamCodec.composite(
            BooleanValue.defaultStreamCodec(ModGameRules.SCARF_OF_INVISIBILITY_ENABLED),
            InvisibilityAbility::enabled,
            InvisibilityAbility::new
    );

    private final BooleanValue enabled;

    public InvisibilityAbility(BooleanValue enabled) {
        super(MobEffects.INVISIBILITY, () -> enabled.get() ? 1 : 0);
        this.enabled = enabled;
    }

    public BooleanValue enabled() {
        return enabled;
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.INVISIBILITY.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InvisibilityAbility that = (InvisibilityAbility) o;
        return enabled().equals(that.enabled());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), enabled());
    }
}
