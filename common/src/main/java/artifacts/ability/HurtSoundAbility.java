package artifacts.ability;

import artifacts.registry.ModAbilities;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;

public record HurtSoundAbility(Holder<SoundEvent> soundEvent) implements TooltiplessAbility {

    public static final MapCodec<HurtSoundAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("sound").forGetter(HurtSoundAbility::soundEvent)
    ).apply(instance, HurtSoundAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, HurtSoundAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.SOUND_EVENT),
            HurtSoundAbility::soundEvent,
            HurtSoundAbility::new
    );

    @Override
    public Type<?> getType() {
        return ModAbilities.MODIFY_HURT_SOUND.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return true;
    }
}
