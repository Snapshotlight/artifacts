package artifacts.ability;

import artifacts.Artifacts;
import artifacts.registry.ModAbilities;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record CustomTooltipAbility(String name) implements ArtifactAbility {

    public static final MapCodec<CustomTooltipAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(CustomTooltipAbility::name)
    ).apply(instance, CustomTooltipAbility::new));

    public static final StreamCodec<ByteBuf, CustomTooltipAbility> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            CustomTooltipAbility::name,
            CustomTooltipAbility::new
    );

    public static CustomTooltipAbility attributeTooltip(String name) {
        return new CustomTooltipAbility("%s.tooltip.ability.attribute.%s".formatted(Artifacts.MOD_ID, name));
    }

    @Override
    public Type<?> getType() {
        return ModAbilities.CUSTOM_TOOLTIP.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return false;
    }

    @Override
    public void addTooltipIfNonCosmetic(List<MutableComponent> tooltip, @Nullable Player player) {
        tooltip.add(Component.translatable(name));
    }
}
