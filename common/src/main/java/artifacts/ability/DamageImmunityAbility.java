package artifacts.ability;

import artifacts.config.value.Value;
import artifacts.config.value.ValueTypes;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModTags;
import artifacts.util.ModCodecs;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.List;
import java.util.Set;

public record DamageImmunityAbility(Value<Boolean> enabled, TagKey<DamageType> tag) implements ArtifactAbility {

    private static final Set<TagKey<DamageType>> CUSTOM_TOOLTIP_TAGS = Set.of(
            DamageTypeTags.IS_LIGHTNING,
            ModTags.IS_HOT_FLOOR
    );

    public static final MapCodec<DamageImmunityAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ValueTypes.BOOLEAN.enabledField().forGetter(DamageImmunityAbility::enabled),
            TagKey.codec(Registries.DAMAGE_TYPE).fieldOf("tag").forGetter(DamageImmunityAbility::tag)
    ).apply(instance, DamageImmunityAbility::new));

    public static final StreamCodec<ByteBuf, DamageImmunityAbility> STREAM_CODEC = StreamCodec.composite(
            ValueTypes.BOOLEAN.streamCodec(),
            DamageImmunityAbility::enabled,
            ModCodecs.tagKeyStreamCodec(Registries.DAMAGE_TYPE),
            DamageImmunityAbility::tag,
            DamageImmunityAbility::new
    );

    @Override
    public Type<?> getType() {
        return ModAbilities.DAMAGE_IMMUNITY.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return enabled().get();
    }

    @Override
    public void addAbilityTooltip(List<MutableComponent> tooltip) {
        if (CUSTOM_TOOLTIP_TAGS.contains(tag())) {
            tooltip.add(tooltipLine(tag().location().getPath()));
        }
    }
}
