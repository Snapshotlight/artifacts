package artifacts.ability;

import artifacts.network.PlaySoundAtPlayerPacket;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.registry.ModTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

public class ReplenishHungerOnGrassAbility implements ArtifactAbility {

    private int getHungerReplenishingDuration() {
        return ModGameRules.ROOTED_BOOTS_HUNGER_REPLENISHING_DURATION.get();
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.REPLENISH_HUNGER_ON_GRASS;
    }

    @Override
    public boolean isNonCosmetic() {
        return ModGameRules.ROOTED_BOOTS_ENABLED.get();
    }

    @Override
    public void wornTick(LivingEntity entity, boolean isOnCooldown, boolean isActive) {
        if (isActive && entity instanceof ServerPlayer player
                && player.onGround()
                && player.getFoodData().needsFood()
                && entity.tickCount % Math.max(20, getHungerReplenishingDuration()) == 0
                && entity.getBlockStateOn().is(ModTags.ROOTED_BOOTS_GRASS)
        ) {
            player.getFoodData().eat(1, 0.5F);
            PlaySoundAtPlayerPacket.sendSound(player, SoundEvents.GENERIC_EAT, 0.5F, 0.8F + entity.getRandom().nextFloat() * 0.4F);
        }
    }
}
