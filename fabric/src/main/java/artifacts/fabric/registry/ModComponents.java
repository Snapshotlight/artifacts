package artifacts.fabric.registry;

import artifacts.Artifacts;
import artifacts.fabric.component.AbilityTogglesComponent;
import artifacts.fabric.component.SwimDataComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;

public class ModComponents implements EntityComponentInitializer {

    public static final ComponentKey<SwimDataComponent> SWIM_DATA = ComponentRegistryV3.INSTANCE.getOrCreate(Artifacts.id("swim_data"), SwimDataComponent.class);
    public static final ComponentKey<AbilityTogglesComponent> ABILITY_TOGGLES = ComponentRegistryV3.INSTANCE.getOrCreate(Artifacts.id("ability_toggles"), AbilityTogglesComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(SWIM_DATA, provider -> new SwimDataComponent(), RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(ABILITY_TOGGLES, provider -> new AbilityTogglesComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }
}
