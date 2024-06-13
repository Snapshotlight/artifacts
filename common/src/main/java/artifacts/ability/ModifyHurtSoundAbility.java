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

public record ModifyHurtSoundAbility(Holder<SoundEvent> soundEvent) implements TooltiplessAbility {

    public static final MapCodec<ModifyHurtSoundAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("sound").forGetter(ModifyHurtSoundAbility::soundEvent)
    ).apply(instance, ModifyHurtSoundAbility::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ModifyHurtSoundAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.SOUND_EVENT),
            ModifyHurtSoundAbility::soundEvent,
            ModifyHurtSoundAbility::new
    );

    @Override
    public Type<?> getType() {
        return ModAbilities.MODIFY_HURT_SOUND.value();
    }

    @Override
    public boolean isNonCosmetic() {
        return true;
    }
}
