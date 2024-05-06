package artifacts.ability.mobeffect;

import artifacts.ability.ArtifactAbility;
import artifacts.ability.value.IntegerValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffects;

public class JumpBoostAbility extends MobEffectAbility {

    public static final MapCodec<JumpBoostAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IntegerValue.field(ModGameRules.BUNNY_HOPPERS_JUMP_BOOST_LEVEL).forGetter(MobEffectAbility::level)
    ).apply(instance, JumpBoostAbility::new));

    public static final StreamCodec<ByteBuf, JumpBoostAbility> STREAM_CODEC = StreamCodec.composite(
            IntegerValue.defaultStreamCodec(ModGameRules.BUNNY_HOPPERS_JUMP_BOOST_LEVEL),
            JumpBoostAbility::level,
            JumpBoostAbility::new
    );

    public JumpBoostAbility(IntegerValue jumpBoostLevel) {
        super(MobEffects.JUMP, jumpBoostLevel);
    }

    public static ArtifactAbility createDefaultInstance() {
        return ArtifactAbility.createDefaultInstance(CODEC);
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.JUMP_BOOST.get();
    }
}
