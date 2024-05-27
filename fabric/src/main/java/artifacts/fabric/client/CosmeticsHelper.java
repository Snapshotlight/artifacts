package artifacts.fabric.client;

import artifacts.fabric.registry.ModDataComponentsFabric;
import artifacts.item.ArtifactItem;
import net.minecraft.world.item.ItemStack;

public class CosmeticsHelper {

    public static boolean areCosmeticsToggledOffByPlayer(ItemStack stack) {
        Boolean enabled = stack.get(ModDataComponentsFabric.COSMETICS_ENABLED.get());
        return enabled != null && enabled && !isCosmeticOnly(stack);
    }

    public static void toggleCosmetics(ItemStack stack) {
        if (!isCosmeticOnly(stack)) {
            stack.set(ModDataComponentsFabric.COSMETICS_ENABLED.get(), !areCosmeticsToggledOffByPlayer(stack));
        }
    }

    private static boolean isCosmeticOnly(ItemStack stack) {
        return stack.getItem() instanceof ArtifactItem item && item.isCosmetic();
    }
}
