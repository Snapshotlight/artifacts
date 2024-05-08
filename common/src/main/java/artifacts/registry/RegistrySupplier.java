package artifacts.registry;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

// TODO remove this if/when arch RegistrySupplier gets equals/hashcode for holders
public record RegistrySupplier<T>(dev.architectury.registry.registries.RegistrySupplier<T> supplier) implements Supplier<T>, Holder<T> {

    public static <T> RegistrySupplier<T> of(dev.architectury.registry.registries.RegistrySupplier<T> supplier) {
        return new RegistrySupplier<>(supplier);
    }

    @Override
    public T get() {
        return supplier.get();
    }

    @Override
    public T value() {
        return supplier().value();
    }

    @Override
    public boolean isBound() {
        return supplier().isBound();
    }

    @Override
    public boolean is(ResourceLocation resourceLocation) {
        return supplier().is(resourceLocation);
    }

    @Override
    public boolean is(ResourceKey<T> resourceKey) {
        return supplier().is(resourceKey);
    }

    @Override
    public boolean is(Predicate<ResourceKey<T>> predicate) {
        return supplier().is(predicate);
    }

    @Override
    public boolean is(TagKey<T> tagKey) {
        return supplier().is(tagKey);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean is(Holder<T> holder) {
        return supplier().is(holder);
    }

    @Override
    public Stream<TagKey<T>> tags() {
        return supplier().tags();
    }

    @Override
    public Either<ResourceKey<T>, T> unwrap() {
        return supplier().unwrap();
    }

    @Override
    public Optional<ResourceKey<T>> unwrapKey() {
        return supplier().unwrapKey();
    }

    @Override
    public Kind kind() {
        return Kind.REFERENCE;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> holderOwner) {
        return supplier().canSerializeIn(holderOwner);
    }

    @Override
    public int hashCode() {
        return supplier().getKey().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Holder<?> h && h.kind() == Kind.REFERENCE && h.unwrapKey().orElseThrow() == supplier().getKey();
    }
}
