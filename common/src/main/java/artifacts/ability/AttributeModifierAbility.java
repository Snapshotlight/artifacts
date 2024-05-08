package artifacts.ability;

import artifacts.ability.value.DoubleValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModAttributes;
import artifacts.registry.ModGameRules;
import artifacts.util.ModCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record AttributeModifierAbility(Holder<Attribute> attribute, DoubleValue amount, AttributeModifier.Operation operation, UUID modifierId, String name) implements ArtifactAbility {

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
                Attributes.SAFE_FALL_DISTANCE
        ));
        CUSTOM_TOOLTIP_ATTRIBUTES.remove(ModAttributes.MAX_ATTACK_DAMAGE_ABSORBED);
    }

    private static final StringRepresentable.StringRepresentableCodec<ModGameRules.DoubleGameRule> AMOUNT_CODEC = new StringRepresentable.StringRepresentableCodec<>(
            ModGameRules.DOUBLE_VALUES_LIST.toArray(ModGameRules.DoubleGameRule[]::new),
            ModGameRules.DOUBLE_VALUES::get,
            ModGameRules.DOUBLE_VALUES_LIST::indexOf
    );

    public static final MapCodec<AttributeModifierAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("attribute").forGetter(AttributeModifierAbility::attribute),
            ModCodecs.xorAlternative(AMOUNT_CODEC.stable().flatXmap(
                    DataResult::success,
                    value -> value instanceof ModGameRules.DoubleGameRule gameRule
                            ? DataResult.success(gameRule)
                            : DataResult.error(() -> "Not a game rule")
            ), DoubleValue.codec()).fieldOf("amount").forGetter(AttributeModifierAbility::amount),
            AttributeModifier.Operation.CODEC.optionalFieldOf("operation", AttributeModifier.Operation.ADD_VALUE).forGetter(AttributeModifierAbility::operation),
            Codec.STRING.fieldOf("id").forGetter(AttributeModifierAbility::name)
    ).apply(instance, AttributeModifierAbility::create));

    @SuppressWarnings("SuspiciousMethodCalls")
    public static final StreamCodec<ByteBuf, AttributeModifierAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.idMapper(BuiltInRegistries.ATTRIBUTE.asHolderIdMap()),
            AttributeModifierAbility::attribute,
            ByteBufCodecs.BOOL.dispatch(
                    ModGameRules.DOUBLE_VALUES_LIST::contains,
                    b -> b ? ByteBufCodecs.idMapper(ModGameRules.DOUBLE_VALUES_LIST::get, ModGameRules.DOUBLE_VALUES_LIST::indexOf) : DoubleValue.streamCodec()
            ),
            AttributeModifierAbility::amount,
            AttributeModifier.Operation.STREAM_CODEC,
            AttributeModifierAbility::operation,
            ByteBufCodecs.STRING_UTF8,
            AttributeModifierAbility::name,
            AttributeModifierAbility::create
    );

    public static AttributeModifierAbility create(Holder<Attribute> attribute, DoubleValue amount, AttributeModifier.Operation operation, String name) {
        return new AttributeModifierAbility(attribute, amount, operation, UUID.nameUUIDFromBytes(name.getBytes()), name);
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
        if (attributeInstance != null) {
            AttributeModifier existingModifier = attributeInstance.getModifier(modifierId());
            if (existingModifier == null || !amount().fuzzyEquals(existingModifier.amount())) {
                attributeInstance.removeModifier(modifierId());
                if (isActive) {
                    attributeInstance.addPermanentModifier(createModifier());
                }
                onAttributeUpdated(entity);
            }
        }
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        if (attribute.value() == ModAttributes.SWIM_SPEED.value()) {
            tooltip.add(tooltipLine("artifacts.generic.swim_speed"));
            return;
        }
        for (Holder<Attribute> attribute : CUSTOM_TOOLTIP_ATTRIBUTES) {
            if (attribute.isBound() && attribute.value() == attribute().value()) {
                //noinspection ConstantConditions
                tooltip.add(tooltipLine(BuiltInRegistries.ATTRIBUTE.getKey(attribute.value()).getPath()));
                return;
            }
        }
    }
}
