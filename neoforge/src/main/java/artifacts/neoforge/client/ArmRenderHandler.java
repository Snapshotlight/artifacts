package artifacts.neoforge.client;

import artifacts.Artifacts;
import artifacts.client.item.renderer.GloveArtifactRenderer;
import artifacts.item.WearableArtifactItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.client.event.RenderArmEvent;
import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public abstract class ArmRenderHandler {

    public static void setup() {
        NeoForge.EVENT_BUS.addListener(EventPriority.LOW, ArmRenderHandler::onRenderArm);
    }

    public static void onRenderArm(RenderArmEvent event) {
        if (!Artifacts.CONFIG.client.showFirstPersonGloves || event.isCanceled()) {
            return;
        }

        InteractionHand hand = event.getArm() == event.getPlayer().getMainArm() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

        CuriosApi.getCuriosInventory(event.getPlayer()).ifPresent(handler -> {
            ICurioStacksHandler stacksHandler = handler.getCurios().get("hands");
            if (stacksHandler != null) {
                IDynamicStackHandler stacks = stacksHandler.getStacks();
                IDynamicStackHandler cosmeticStacks = stacksHandler.getCosmeticStacks();

                for (int slot = hand == InteractionHand.MAIN_HAND ? 0 : 1; slot < stacks.getSlots(); slot += 2) {
                    ItemStack stack = cosmeticStacks.getStackInSlot(slot);
                    if (stack.isEmpty() && stacksHandler.getRenders().get(slot)) {
                        stack = stacks.getStackInSlot(slot);
                    }

                    if (stack.getItem() instanceof WearableArtifactItem) {
                        GloveArtifactRenderer renderer = GloveArtifactRenderer.getGloveRenderer(stack);
                        if (renderer != null) {
                            renderer.renderFirstPersonArm(event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), event.getPlayer(), event.getArm(), stack.hasFoil());
                        }
                    }
                }
            }
        });
    }
}
