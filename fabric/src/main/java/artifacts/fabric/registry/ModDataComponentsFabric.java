package artifacts.fabric.registry;

import artifacts.registry.ModDataComponents;
import artifacts.registry.RegistrySupplier;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;

public class ModDataComponentsFabric {

    public static final RegistrySupplier<DataComponentType<Boolean>> COSMETICS_ENABLED = RegistrySupplier.of(ModDataComponents.DATA_COMPONENT_TYPES.register("cosmetic_toggle", () ->
            DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build()
    ));

    public static void register() {
        // no-op
    }
}
