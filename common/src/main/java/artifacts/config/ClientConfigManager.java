package artifacts.config;

import java.util.function.Supplier;

public class ClientConfigManager extends ConfigManager {

    public final Supplier<Boolean> showFirstPersonGloves = defineBool("showFirstPersonGloves", true,
            "Whether models for gloves are shown in first person");
    public final Supplier<Boolean> showTooltips = defineBool("showTooltips", true,
            "Whether artifacts have tooltips explaining their effects");
    public final Supplier<Boolean> useModdedMimicTextures = defineBool("useModdedMimicTextures", true,
            "Whether mimics can use textures from Lootr or Quark");
    public final Supplier<Boolean> enableCooldownOverlay = defineBool("enableCooldownOverlay", false,
            "Display artifacts on cooldown next to the hotbar");
    public final Supplier<Integer> cooldownOverlayOffset = defineInt("cooldownOverlayOffset", 10,
            "Location of the artifact cooldown gui element",
            "Distance from the hotbar measured in pixels",
            "Negative values place the element left of the hotbar");

    protected ClientConfigManager() {
        super("client");
    }
}
