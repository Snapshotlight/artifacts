package artifacts.ability;

import artifacts.config.value.Value;
import artifacts.config.value.ValueTypes;
import artifacts.registry.ModAbilities;
import artifacts.util.ModCodecs;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public record CollideWithFluidsAbility(Supplier<Type<CollideWithFluidsAbility>> type, Value<Boolean> enabled, Value<Boolean> dealLavaDamage, Optional<TagKey<Fluid>> tag) implements ArtifactAbility {

    public static Type<CollideWithFluidsAbility> createType() {
        AtomicReference<Type<CollideWithFluidsAbility>> type = new AtomicReference<>();
        type.set(new Type<>(codec(type::get), streamCodec(type::get)));
        return type.get();
    }

    private static MapCodec<CollideWithFluidsAbility> codec(Supplier<Type<CollideWithFluidsAbility>> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ValueTypes.BOOLEAN.enabledField().forGetter(CollideWithFluidsAbility::enabled),
                ValueTypes.BOOLEAN.codec().optionalFieldOf("deal_lava_damage", Value.Constant.TRUE).forGetter(CollideWithFluidsAbility::dealLavaDamage),
                TagKey.codec(Registries.FLUID).optionalFieldOf("tag").forGetter(CollideWithFluidsAbility::tag)
        ).apply(instance, (enabled, dealLavaDamage, tag) -> new CollideWithFluidsAbility(type, enabled, dealLavaDamage, tag)));
    }

    private static StreamCodec<ByteBuf, CollideWithFluidsAbility> streamCodec(Supplier<Type<CollideWithFluidsAbility>> type) {
        return StreamCodec.composite(
                ValueTypes.BOOLEAN.streamCodec(),
                CollideWithFluidsAbility::enabled,
                ValueTypes.BOOLEAN.streamCodec(),
                CollideWithFluidsAbility::dealLavaDamage,
                ByteBufCodecs.optional(ModCodecs.tagKeyStreamCodec(Registries.FLUID)),
                CollideWithFluidsAbility::tag,
                (enabled, dealLavaDamage, tag) -> new CollideWithFluidsAbility(type, enabled, dealLavaDamage, tag)
        );
    }

    @Override
    public Type<?> getType() {
        return type.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return enabled().get();
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        if (getType() == ModAbilities.SNEAK_ON_FLUIDS.get() && tag().isPresent() && FluidTags.LAVA.equals(tag().get())) {
            tooltip.add(tooltipLine("lava"));
        } else if (getType() == ModAbilities.SPRINT_ON_FLUIDS.get() && tag.isEmpty()) {
            ArtifactAbility.super.addAbilityTooltip(tooltip);
        }
    }
}
