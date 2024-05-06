package artifacts.ability;

import artifacts.registry.ModAbilities;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record MakePiglinsNeutralAbility() implements ArtifactAbility {

    public static final MakePiglinsNeutralAbility INSTANCE = new MakePiglinsNeutralAbility();

    public static final MapCodec<MakePiglinsNeutralAbility> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, MakePiglinsNeutralAbility> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<?> getType() {
        return ModAbilities.MAKE_PIGLINS_NEUTRAL.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
