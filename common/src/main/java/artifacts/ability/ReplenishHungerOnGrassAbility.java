package artifacts.ability;

import artifacts.ability.value.BooleanValue;
import artifacts.ability.value.IntegerValue;
import artifacts.network.PlaySoundAtPlayerPacket;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModTags;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

public record ReplenishHungerOnGrassAbility(BooleanValue enabled, IntegerValue replenishingDuration) implements ArtifactAbility {

    public static final MapCodec<ReplenishHungerOnGrassAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BooleanValue.enabledField().forGetter(ReplenishHungerOnGrassAbility::enabled),
            IntegerValue.durationSecondsCodec().fieldOf("duration").forGetter(ReplenishHungerOnGrassAbility::replenishingDuration)
    ).apply(instance, ReplenishHungerOnGrassAbility::new));

    public static final StreamCodec<ByteBuf, ReplenishHungerOnGrassAbility> STREAM_CODEC = StreamCodec.composite(
            BooleanValue.streamCodec(),
            ReplenishHungerOnGrassAbility::enabled,
            IntegerValue.streamCodec(),
            ReplenishHungerOnGrassAbility::replenishingDuration,
            ReplenishHungerOnGrassAbility::new
    );

    @Override
    public Type<?> getType() {
        return ModAbilities.REPLENISH_HUNGER_ON_GRASS.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return enabled().get();
    }

    @Override
    public void wornTick(LivingEntity entity, boolean isOnCooldown, boolean isActive) {
        if (isActive && entity instanceof ServerPlayer player
                && player.onGround()
                && player.getFoodData().needsFood()
                && entity.tickCount % Math.max(20, replenishingDuration().get()) == 0
                && entity.getBlockStateOn().is(ModTags.ROOTED_BOOTS_GRASS)
        ) {
            player.getFoodData().eat(1, 0.5F);
            PlaySoundAtPlayerPacket.sendSound(player, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.GENERIC_EAT), 0.5F, 0.8F + entity.getRandom().nextFloat() * 0.4F);
        }
    }
}
