package artifacts.ability;

import artifacts.registry.ModAbilities;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;

public record HurtSoundAbility(SoundEvent soundEvent) implements ArtifactAbility {

    public static final MapCodec<HurtSoundAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("sound").forGetter(HurtSoundAbility::soundEvent)
    ).apply(instance, HurtSoundAbility::new));

    public static final StreamCodec<ByteBuf, HurtSoundAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.idMapper(BuiltInRegistries.SOUND_EVENT),
            HurtSoundAbility::soundEvent,
            HurtSoundAbility::new
    );

    @Override
    public Type<?> getType() {
        return ModAbilities.MODIFY_HURT_SOUND.get();
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
