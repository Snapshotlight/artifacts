package artifacts.ability;

import artifacts.ability.value.DoubleValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModAttributes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record AttributeModifierAbility(Holder<Attribute> attribute, DoubleValue amount, AttributeModifier.Operation operation, UUID modifierId, String name, boolean ignoreCooldown) implements ArtifactAbility {

    private static final Set<Holder<Attribute>> CUSTOM_TOOLTIP_ATTRIBUTES;

    static {
        CUSTOM_TOOLTIP_ATTRIBUTES = new HashSet<>();
        CUSTOM_TOOLTIP_ATTRIBUTES.addAll(ModAttributes.PLAYER_ATTRIBUTES);
        CUSTOM_TOOLTIP_ATTRIBUTES.addAll(ModAttributes.GENERIC_ATTRIBUTES);
        CUSTOM_TOOLTIP_ATTRIBUTES.addAll(List.of(
                Attributes.ATTACK_DAMAGE,
                Attributes.ATTACK_KNOCKBACK,
                Attributes.ATTACK_SPEED,
                Attributes.BLOCK_BREAK_SPEED,
                Attributes.JUMP_STRENGTH,
                Attributes.KNOCKBACK_RESISTANCE,
                Attributes.MAX_HEALTH,
                Attributes.SAFE_FALL_DISTANCE,
                Attributes.FALL_DAMAGE_MULTIPLIER,
                Attributes.SCALE
        ));
        CUSTOM_TOOLTIP_ATTRIBUTES.remove(ModAttributes.MAX_ATTACK_DAMAGE_ABSORBED);
    }

    public static final MapCodec<AttributeModifierAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("attribute").forGetter(AttributeModifierAbility::attribute),
            DoubleValue.codec(Integer.MIN_VALUE, Integer.MAX_VALUE, 100).fieldOf("amount").forGetter(AttributeModifierAbility::amount),
            AttributeModifier.Operation.CODEC.optionalFieldOf("operation", AttributeModifier.Operation.ADD_VALUE).forGetter(AttributeModifierAbility::operation),
            Codec.STRING.fieldOf("id").forGetter(AttributeModifierAbility::name),
            Codec.BOOL.optionalFieldOf("ignore_cooldown", true).forGetter(AttributeModifierAbility::ignoreCooldown)
    ).apply(instance, AttributeModifierAbility::create));

    public static final StreamCodec<ByteBuf, AttributeModifierAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.idMapper(BuiltInRegistries.ATTRIBUTE.asHolderIdMap()),
            AttributeModifierAbility::attribute,
            DoubleValue.streamCodec(),
            AttributeModifierAbility::amount,
            AttributeModifier.Operation.STREAM_CODEC,
            AttributeModifierAbility::operation,
            ByteBufCodecs.STRING_UTF8,
            AttributeModifierAbility::name,
            ByteBufCodecs.BOOL,
            AttributeModifierAbility::ignoreCooldown,
            AttributeModifierAbility::create
    );

    public static AttributeModifierAbility create(Holder<Attribute> attribute, DoubleValue amount, AttributeModifier.Operation operation, String name) {
        return create(attribute, amount, operation, name, true);
    }

    public static AttributeModifierAbility create(Holder<Attribute> attribute, DoubleValue amount, AttributeModifier.Operation operation, String name, boolean ignoreCooldowns) {
        return new AttributeModifierAbility(attribute, amount, operation, UUID.nameUUIDFromBytes(name.getBytes()), name, ignoreCooldowns);
    }

    public AttributeModifier createModifier() {
        return new AttributeModifier(modifierId(), name(), amount().get(), operation());
    }

    private void onAttributeUpdated(LivingEntity entity) {
        if (attribute() == Attributes.MAX_HEALTH && entity.getHealth() > entity.getMaxHealth()) {
            entity.setHealth(entity.getMaxHealth());
        }
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.ATTRIBUTE_MODIFIER.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return !amount().fuzzyEquals(0);
    }

    @Override
    public void onUnequip(LivingEntity entity, boolean wasActive) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute());
        if (attributeInstance != null && wasActive) {
            attributeInstance.removeModifier(modifierId());
            onAttributeUpdated(entity);
        }
    }

    @Override
    public void wornTick(LivingEntity entity, boolean isOnCooldown, boolean isActive) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute());
        if (attributeInstance == null) {
            return;
        }
        AttributeModifier existingModifier = attributeInstance.getModifier(modifierId());
        if (!ignoreCooldown() && isOnCooldown) {
            if (isActive) {
                onUnequip(entity, true);
            }
        } else {
            if (existingModifier == null || !amount().fuzzyEquals(existingModifier.amount())) {
                attributeInstance.removeModifier(modifierId());
                attributeInstance.addPermanentModifier(createModifier());
                onAttributeUpdated(entity);
            }
        }
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        for (Holder<Attribute> attribute : CUSTOM_TOOLTIP_ATTRIBUTES) {
            if (attribute.isBound() && attribute.value() == attribute().value()) {
                if ((attribute.equals(Attributes.SCALE) || attribute.equals(Attributes.FALL_DAMAGE_MULTIPLIER)) ^ amount.get() > 0) {
                    //noinspection ConstantConditions
                    tooltip.add(tooltipLine(BuiltInRegistries.ATTRIBUTE.getKey(attribute.value()).getPath()));
                }
                return;
            }
        }
    }
}
