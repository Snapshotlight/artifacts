package artifacts.neoforge;

import artifacts.Artifacts;
import artifacts.config.ModConfig;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.neoforge.curio.WearableArtifactCurio;
import artifacts.neoforge.event.ArtifactEventsNeoForge;
import artifacts.neoforge.event.SwimEventsNeoForge;
import artifacts.neoforge.registry.ModAttachmentTypes;
import artifacts.neoforge.registry.ModLootModifiers;
import artifacts.registry.ModItems;
import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import top.theillusivec4.curios.api.CuriosApi;

@Mod(Artifacts.MOD_ID)
public class ArtifactsNeoForge {

    public ArtifactsNeoForge(IEventBus modBus) {
        Artifacts.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            new ArtifactsNeoForgeClient(modBus);
        }

        ModLootModifiers.LOOT_MODIFIERS.register(modBus);
        ModAttachmentTypes.ATTACHMENT_TYPES.register(modBus);

        modBus.addListener(this::registerCapabilities);

        registerConfig();
        ArtifactEventsNeoForge.register();
        SwimEventsNeoForge.register();
    }

    private void registerConfig() {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get()
                )
        );
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        ModItems.ITEMS.forEach(entry -> {
            if (entry.get() instanceof WearableArtifactItem item) {
                CuriosApi.registerCurio(item, new WearableArtifactCurio(item));
            }
        });
    }
}
