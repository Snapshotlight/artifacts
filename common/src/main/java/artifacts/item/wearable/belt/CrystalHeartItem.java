package artifacts.item.wearable.belt;

import artifacts.Artifacts;
import artifacts.ability.AttributeModifierAbility;
import artifacts.ability.CustomTooltipAbility;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.registry.ModGameRules;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class CrystalHeartItem extends WearableArtifactItem {

    public CrystalHeartItem() {
        super(CustomTooltipAbility.attributeTooltip("max_health"), new AttributeModifierAbility(
                Attributes.MAX_HEALTH,
                () -> (double) ModGameRules.CRYSTAL_HEART_HEALTH_BONUS.get(),
                Artifacts.id("crystal_heart/health_bonus")
        ));
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_DIAMOND;
    }
}
