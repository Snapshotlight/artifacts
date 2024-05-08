package artifacts.fabric.trinket;

import artifacts.item.WearableArtifactItem;
import artifacts.util.DamageSourceHelper;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketEnums;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record WearableArtifactTrinket(WearableArtifactItem item) implements Trinket {

    @Override
    public TrinketEnums.DropRule getDropRule(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (DamageSourceHelper.shouldDestroyWornItemsOnDeath(entity)) {
            return TrinketEnums.DropRule.DESTROY;
        }
        return Trinket.super.getDropRule(stack, slot, entity);
    }
}
