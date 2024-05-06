package artifacts.ability;

import artifacts.ability.value.DoubleValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import artifacts.util.ModCodecs;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AttributeModifierAbility implements ArtifactAbility {

    private static final List<ModGameRules.DoubleGameRule> AMOUNT_GAME_RULES = List.of(
            ModGameRules.CRYSTAL_HEART_HEALTH_BONUS,
            ModGameRules.FERAL_CLAWS_ATTACK_SPEED_BONUS,
            ModGameRules.PLASTIC_DRINKING_HAT_DRINKING_DURATION_MULTIPLIER,
            ModGameRules.FLIPPERS_SWIM_SPEED_BONUS,
            ModGameRules.POWER_GLOVE_ATTACK_DAMAGE_BONUS
    );

    private static final StringRepresentable.StringRepresentableCodec<ModGameRules.DoubleGameRule> AMOUNT_CODEC = new StringRepresentable.StringRepresentableCodec<>(
            AMOUNT_GAME_RULES.toArray(ModGameRules.DoubleGameRule[]::new),
            ModGameRules.DOUBLE_VALUES::get,
            AMOUNT_GAME_RULES::indexOf
    );

    public static final MapCodec<AttributeModifierAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("attribute").forGetter(AttributeModifierAbility::getAttribute),
            ModCodecs.xorAlternative(AMOUNT_CODEC.stable().flatXmap(
                    DataResult::success,
                    value -> value instanceof ModGameRules.DoubleGameRule gameRule
                            ? DataResult.success(gameRule)
                            : DataResult.error(() -> "Not a game rule")
            ), DoubleValue.codec(100, 1, 1)).fieldOf("amount").forGetter(AttributeModifierAbility::amount),
            ResourceLocation.CODEC.fieldOf("id").forGetter(ability -> new ResourceLocation(ability.modifierName))
    ).apply(instance, AttributeModifierAbility::new));

    @SuppressWarnings("SuspiciousMethodCalls")
    public static final StreamCodec<ByteBuf, AttributeModifierAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.idMapper(BuiltInRegistries.ATTRIBUTE.asHolderIdMap()),
            AttributeModifierAbility::getAttribute,
            ByteBufCodecs.BOOL.dispatch(
                    AMOUNT_GAME_RULES::contains,
                    b -> b ? ByteBufCodecs.idMapper(AMOUNT_GAME_RULES::get, AMOUNT_GAME_RULES::indexOf) : DoubleValue.streamCodec()
            ),
            AttributeModifierAbility::amount,
            ResourceLocation.STREAM_CODEC,
            ability -> new ResourceLocation(ability.modifierName),
            AttributeModifierAbility::new
    );

    private final Holder<Attribute> attribute;
    private final DoubleValue amount;
    private final AttributeModifier.Operation operation;
    private final UUID modifierId;
    private final String modifierName;

    public AttributeModifierAbility(Holder<Attribute> attribute, DoubleValue amount, ResourceLocation id) {
        this(attribute, amount, AttributeModifier.Operation.ADD_VALUE, id);
    }

    public AttributeModifierAbility(Holder<Attribute> attribute, DoubleValue amount, AttributeModifier.Operation operation, ResourceLocation id) {
        this.attribute = attribute;
        this.amount = amount;
        this.operation = operation;
        this.modifierId = UUID.nameUUIDFromBytes(id.toString().getBytes());
        this.modifierName = id.toString();
    }

    protected AttributeModifier createModifier() {
        return new AttributeModifier(modifierId, modifierName, amount().get(), getOperation());
    }

    public Holder<Attribute> getAttribute() {
        return attribute;
    }

    protected UUID getModifierId() {
        return modifierId;
    }

    public DoubleValue amount() {
        return amount;
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
        return !amount().fuzzyEquals(0);
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
            if (existingModifier == null || !amount().fuzzyEquals(existingModifier.amount())) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeModifierAbility that = (AttributeModifierAbility) o;
        return getAttribute().equals(that.getAttribute()) && amount.equals(that.amount) && getOperation() == that.getOperation() && modifierName.equals(that.modifierName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAttribute(), amount, getOperation(), modifierName);
    }
}
