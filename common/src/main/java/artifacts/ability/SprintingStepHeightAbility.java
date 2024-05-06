package artifacts.ability;

import artifacts.ability.value.BooleanValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public record SprintingStepHeightAbility(BooleanValue enabled) implements ArtifactAbility {

    public static final MapCodec<SprintingStepHeightAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BooleanValue.field(ModGameRules.RUNNING_SHOES_DO_INCREASE_STEP_HEIGHT, "enabled").forGetter(SprintingStepHeightAbility::enabled)
    ).apply(instance, SprintingStepHeightAbility::new));

    public static final StreamCodec<ByteBuf, SprintingStepHeightAbility> STREAM_CODEC = StreamCodec.composite(
            BooleanValue.defaultStreamCodec(ModGameRules.RUNNING_SHOES_DO_INCREASE_STEP_HEIGHT),
            SprintingStepHeightAbility::enabled,
            SprintingStepHeightAbility::new
    );

    private static final AttributeModifier STEP_HEIGHT_BONUS = new AttributeModifier(UUID.fromString("4a312f09-78e0-4f3a-95c2-07ed63212472"), "artifacts:running_shoes_step_height", 0.5, AttributeModifier.Operation.ADD_VALUE);

    public static ArtifactAbility createDefaultInstance() {
        return ArtifactAbility.createDefaultInstance(CODEC);
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.SPRINTING_STEP_HEIGHT.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return enabled().get();
    }

    @Override
    public void wornTick(LivingEntity entity, boolean isOnCooldown, boolean isActive) {
        AttributeInstance stepHeight = entity.getAttribute(Attributes.STEP_HEIGHT);
        if (entity.isSprinting() && isActive) {
            if (stepHeight != null && !stepHeight.hasModifier(STEP_HEIGHT_BONUS) && entity instanceof Player) {
                stepHeight.addTransientModifier(STEP_HEIGHT_BONUS);
            }
        } else {
            if (stepHeight != null && stepHeight.hasModifier(STEP_HEIGHT_BONUS)) {
                stepHeight.removeModifier(STEP_HEIGHT_BONUS.id());
            }
        }
    }
}
