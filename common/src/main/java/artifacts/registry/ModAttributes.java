package artifacts.registry;

import artifacts.Artifacts;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.ArrayList;
import java.util.List;

public class ModAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Artifacts.MOD_ID, Registries.ATTRIBUTE);

    public static final List<RegistrySupplier<Attribute>> PLAYER_ATTRIBUTES = new ArrayList<>();
    public static final List<RegistrySupplier<Attribute>> GENERIC_ATTRIBUTES = new ArrayList<>();

    public static final RegistrySupplier<Attribute> ENTITY_EXPERIENCE = addPlayerAttribute("entity_experience", 1, 0, 64);
    public static final RegistrySupplier<Attribute> VILLAGER_REPUTATION = addPlayerAttribute("villager_reputation", 0, 0, 1024);

    public static final RegistrySupplier<Attribute> DRINKING_SPEED = addGenericAttribute("drinking_speed", 1, 1, Double.MAX_VALUE);
    public static final RegistrySupplier<Attribute> EATING_SPEED = addGenericAttribute("eating_speed", 1, 1, Double.MAX_VALUE);

    public static RegistrySupplier<Attribute> addPlayerAttribute(String name, double d, double min, double max) {
        String id = "artifacts.player." + name;
        RegistrySupplier<Attribute> attribute = register(id, d, min, max);
        PLAYER_ATTRIBUTES.add(attribute);
        return attribute;
    }

    public static RegistrySupplier<Attribute> addGenericAttribute(String name, double d, double min, double max) {
        String id = "artifacts.generic." + name;
        RegistrySupplier<Attribute> attribute = register(id, d, min, max);
        GENERIC_ATTRIBUTES.add(attribute);
        return attribute;
    }

    public static RegistrySupplier<Attribute> register(String name, double d, double min, double max) {
        return RegistrySupplier.of(ATTRIBUTES.register(Artifacts.id(name), () -> new RangedAttribute(name, d, min, max).setSyncable(true)));
    }
}
