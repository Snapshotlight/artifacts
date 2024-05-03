package artifacts.mixin.item.wearable.villagerhat;

import artifacts.ability.VillagerReputation;
import artifacts.registry.ModAbilities;
import artifacts.util.AbilityHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Villager.class)
public abstract class VillagerMixin {

    @ModifyExpressionValue(method = "updateSpecialPrices", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;getPlayerReputation(Lnet/minecraft/world/entity/player/Player;)I"))
    private int increaseReputation(int original, Player player) {
        return original + AbilityHelper.sumInt(ModAbilities.VILLAGER_REPUTATION.get(), player, VillagerReputation::getReputationBonus, false);
    }
}
