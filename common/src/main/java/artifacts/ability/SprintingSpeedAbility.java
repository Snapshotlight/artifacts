package artifacts.ability;

import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class SprintingSpeedAbility implements ArtifactAbility {

    private static AttributeModifier getSpeedBonus() {
        double speedMultiplier = ModGameRules.RUNNING_SHOES_SPEED_BONUS.get();
        return new AttributeModifier(UUID.fromString("ac7ab816-2b08-46b6-879d-e5dea34ff305"), "artifacts:running_shoes_movement_speed", speedMultiplier, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.SPRINTING_SPEED.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return ModGameRules.RUNNING_SHOES_SPEED_BONUS.get() > 0;
    }

    @Override
    public void wornTick(LivingEntity entity, boolean isOnCooldown, boolean isActive) {
        AttributeInstance movementSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeModifier modifier = getSpeedBonus();
        if (entity.isSprinting() && isActive) {
            if (movementSpeed != null && !movementSpeed.hasModifier(modifier) && entity instanceof Player) {
                movementSpeed.addTransientModifier(modifier);
            }
        } else {
            if (movementSpeed != null && movementSpeed.hasModifier(modifier)) {
                movementSpeed.removeModifier(modifier.getId());
            }
        }
    }
}
