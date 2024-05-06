package artifacts.mixin.attribute;

import artifacts.registry.ModAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class PlayerMixin {

    @ModifyReturnValue(method = "createAttributes", at = @At(value = "RETURN"))
    private static AttributeSupplier.Builder createAttributes(AttributeSupplier.Builder original) {
        return original.add(ModAttributes.VILLAGER_REPUTATION, 0);
    }
}
