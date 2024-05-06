package artifacts.registry;

import artifacts.Artifacts;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.function.Supplier;

public class ModAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Artifacts.MOD_ID, Registries.ATTRIBUTE);

    public static final RegistrySupplier<Attribute> VILLAGER_REPUTATION = register("artifacts.player.villager_reputation", () -> new RangedAttribute("artifacts.player.villager_reputation", 0D, 0D, 1024D).setSyncable(true));

    public static RegistrySupplier<Attribute> register(String name, Supplier<Attribute> supplier) {
        return RegistrySupplier.of(ATTRIBUTES.register(Artifacts.id(name), supplier));
    }
}
