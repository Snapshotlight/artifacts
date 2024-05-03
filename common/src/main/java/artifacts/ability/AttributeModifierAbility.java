package artifacts.ability;

import artifacts.registry.ModAbilities;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class AttributeModifierAbility implements ArtifactAbility {

    private final Attribute attribute;
    private final Supplier<Double> amount;
    private final AttributeModifier.Operation operation;
    private final UUID modifierId;
    private final String modifierName;

    public AttributeModifierAbility(Attribute attribute, Supplier<Double> amount, ResourceLocation id) {
        this(attribute, amount, AttributeModifier.Operation.ADDITION, id);
    }

    public AttributeModifierAbility(Attribute attribute, Supplier<Double> amount, AttributeModifier.Operation operation, ResourceLocation id) {
        this.attribute = attribute;
        this.amount = amount;
        this.operation = operation;
        this.modifierId = UUID.nameUUIDFromBytes(id.toString().getBytes());
        this.modifierName = id.toString();
    }

    protected AttributeModifier createModifier() {
        return new AttributeModifier(modifierId, modifierName, getAmount(), getOperation());
    }

    public Attribute getAttribute() {
        return attribute;
    }

    protected UUID getModifierId() {
        return modifierId;
    }

    public double getAmount() {
        return amount.get();
    }

    public AttributeModifier.Operation getOperation() {
        return operation;
    }

    protected void onAttributeUpdated(LivingEntity entity) {
        if (getAttribute() == Attributes.MAX_HEALTH && entity.getHealth() > entity.getMaxHealth()) {
            entity.setHealth(entity.getMaxHealth());
        }
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.ATTRIBUTE_MODIFIER.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return getAmount() != 0;
    }

    @Override
    public void onEquip(LivingEntity entity, boolean isActive) {
        AttributeInstance attributeInstance = entity.getAttribute(getAttribute());
        if (attributeInstance != null) {
            attributeInstance.removeModifier(getModifierId());
            AttributeModifier attributeModifier = createModifier();
            if (isActive) {
                attributeInstance.addTransientModifier(attributeModifier);
            }
            onAttributeUpdated(entity);
        }
    }

    @Override
    public void onUnequip(LivingEntity entity, boolean wasActive) {
        AttributeInstance attributeInstance = entity.getAttribute(getAttribute());
        if (attributeInstance != null && wasActive) {
            attributeInstance.removeModifier(getModifierId());
            onAttributeUpdated(entity);
        }
    }

    @Override
    public void wornTick(LivingEntity entity, boolean isOnCooldown, boolean isActive) {
        AttributeInstance attributeInstance = entity.getAttribute(getAttribute());
        if (attributeInstance != null) {
            AttributeModifier existingModifier = attributeInstance.getModifier(getModifierId());
            if (existingModifier == null || existingModifier.getAmount() != getAmount()) {
                attributeInstance.removeModifier(getModifierId());
                if (isActive) {
                    attributeInstance.addTransientModifier(createModifier());
                }
                onAttributeUpdated(entity);
            }
        }
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {

    }
}
