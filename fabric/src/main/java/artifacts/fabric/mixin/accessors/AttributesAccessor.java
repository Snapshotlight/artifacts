package artifacts.fabric.mixin.accessors;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Attributes.class)
public interface AttributesAccessor {

    @Invoker
    static Holder<Attribute> invokeRegister(String string, Attribute attribute) {
        throw new UnsupportedOperationException();
    }
}
