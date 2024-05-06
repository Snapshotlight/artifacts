package artifacts.ability.mobeffect;

import artifacts.ability.ArtifactAbility;
import artifacts.ability.value.BooleanValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.ModCodecs;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffects;

public class InvisibilityAbility extends MobEffectAbility {

    public static final MapCodec<InvisibilityAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BooleanValue.field(ModGameRules.SCARF_OF_INVISIBILITY_ENABLED).forGetter(InvisibilityAbility::enabled)
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

    public static ArtifactAbility createDefaultInstance() {
        return ArtifactAbility.createDefaultInstance(CODEC);
    }

    public BooleanValue enabled() {
        return enabled;
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.INVISIBILITY.get();
    }
}
