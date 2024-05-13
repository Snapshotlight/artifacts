package artifacts.attribute;

import artifacts.item.UmbrellaItem;
import artifacts.registry.ModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

public class DynamicAttributeModifier {

    private static final List<DynamicAttributeModifier> MODIFIERS = List.of(
            new DynamicAttributeModifier(
                    "artifacts:mount_speed",
                    Attributes.MOVEMENT_SPEED,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    entity -> entity.getControllingPassenger() != null,
                    entity -> Objects.requireNonNull(entity.getControllingPassenger()).getAttributeValue(ModAttributes.MOUNT_SPEED)
            ),
            new DynamicAttributeModifier(
                    "artifacts:sprinting_speed",
                    Attributes.MOVEMENT_SPEED,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                    LivingEntity::isSprinting,
                    entity -> entity.getAttributeValue(ModAttributes.SPRINTING_SPEED) - 1
            ),
            new DynamicAttributeModifier(
                    "artifacts:sprinting_step_height",
                    Attributes.STEP_HEIGHT,
                    AttributeModifier.Operation.ADD_VALUE,
                    LivingEntity::isSprinting,
                    entity -> entity.getAttributeValue(ModAttributes.SPRINTING_STEP_HEIGHT)
            ),
            new DynamicAttributeModifier(
                    "artifacts:umbrella_gravity",
                    Attributes.GRAVITY,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                    UmbrellaItem::shouldGlide,
                    entity -> -0.875D
            )
    );

    private final Holder<Attribute> attribute;
    private final AttributeModifier.Operation operation;
    private final UUID modifierId;
    private final String name;
    private final Predicate<LivingEntity> shouldApply;
    private final Function<LivingEntity, Double> amount;

    public DynamicAttributeModifier(String name, Holder<Attribute> attribute, AttributeModifier.Operation operation, Predicate<LivingEntity> shouldApply, Function<LivingEntity, Double> amount) {
        this.attribute = attribute;
        this.operation = operation;
        this.modifierId = UUID.nameUUIDFromBytes(name.getBytes());
        this.name = name;
        this.shouldApply = shouldApply;
        this.amount = amount;
    }

    public static void tickModifiers(LivingEntity entity) {
        for (DynamicAttributeModifier modifier : MODIFIERS) {
            modifier.tick(entity);
        }
    }

    private void tick(LivingEntity entity) {
        AttributeInstance movementSpeed = entity.getAttribute(attribute);
        if (movementSpeed == null) {
            return;
        }
        if (shouldApply.test(entity)) {
            double amount = this.amount.apply(entity);
            AttributeModifier modifier = movementSpeed.getModifier(modifierId);
            if (modifier == null || modifier.amount() != amount) {
                movementSpeed.addOrUpdateTransientModifier(new AttributeModifier(modifierId, name, amount, operation));
            }
        } else {
            movementSpeed.removeModifier(modifierId);
        }
    }
}
