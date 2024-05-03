package artifacts.item.wearable.hands;

import artifacts.Artifacts;
import artifacts.ability.AttributeModifierAbility;
import artifacts.ability.CustomTooltipAbility;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModGameRules;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FeralClawsItem extends WearableArtifactItem {

    public FeralClawsItem() {
        super(CustomTooltipAbility.attributeTooltip("attack_speed"), new AttributeModifierAbility(
                Attributes.ATTACK_SPEED,
                ModGameRules.FERAL_CLAWS_ATTACK_SPEED_BONUS,
                Artifacts.id("feral_claws/attack_speed_bonus")
        ));
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_NETHERITE;
    }
}
