package artifacts.ability;

import artifacts.ability.value.BooleanValue;
import artifacts.registry.ModAbilities;
import artifacts.registry.ModGameRules;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public record SimpleAbility(Supplier<Type<SimpleAbility>> type, BooleanValue enabled) implements ArtifactAbility {

    private static Pair<MapCodec<SimpleAbility>, StreamCodec<ByteBuf, SimpleAbility>> codecs(Supplier<Type<SimpleAbility>> type, ModGameRules.BooleanGameRule enabled) {
        return Pair.of(codec(type, enabled), streamCodec(type, enabled));
    }

    private static MapCodec<SimpleAbility> codec(Supplier<Type<SimpleAbility>> type, ModGameRules.BooleanGameRule enabled) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                BooleanValue.field(enabled, "enabled").forGetter(SimpleAbility::enabled)
        ).apply(instance, value -> new SimpleAbility(type, value)));
    }

    private static StreamCodec<ByteBuf, SimpleAbility> streamCodec(Supplier<Type<SimpleAbility>> type, ModGameRules.BooleanGameRule enabled) {
        return StreamCodec.composite(
                BooleanValue.defaultStreamCodec(enabled),
                SimpleAbility::enabled,
                value -> new SimpleAbility(type, value)
        );
    }

    @Override
    public Type<?> getType() {
        return type.get();
    }

    @Override
    public boolean isNonCosmetic() {
        return enabled.get();
    }

    public static Pair<MapCodec<SimpleAbility>, StreamCodec<ByteBuf, SimpleAbility>> cancelFallDamage() {
        return codecs(ModAbilities.CANCEL_FALL_DAMAGE, ModGameRules.BUNNY_HOPPERS_DO_CANCEL_FALL_DAMAGE);
    }

    public static Pair<MapCodec<SimpleAbility>, StreamCodec<ByteBuf, SimpleAbility>> sprintOnWater() {
        return codecs(ModAbilities.SPRINT_ON_WATER, ModGameRules.AQUA_DASHERS_ENABLED);
    }

    public static Pair<MapCodec<SimpleAbility>, StreamCodec<ByteBuf, SimpleAbility>> sinking() {
        return codecs(ModAbilities.SINKING, ModGameRules.CHARM_OF_SINKING_ENABLED);
    }

    public static Pair<MapCodec<SimpleAbility>, StreamCodec<ByteBuf, SimpleAbility>> smeltOres() {
        return codecs(ModAbilities.SMELT_ORES, ModGameRules.PICKAXE_HEATER_ENABLED);
    }

    public static Pair<MapCodec<SimpleAbility>, StreamCodec<ByteBuf, SimpleAbility>> scareCreepers() {
        return codecs(ModAbilities.SCARE_CREEPERS, ModGameRules.KITTY_SLIPPERS_ENABLED);
    }

    public static Pair<MapCodec<SimpleAbility>, StreamCodec<ByteBuf, SimpleAbility>> walkOnPowderSnow() {
        return codecs(ModAbilities.WALK_ON_POWDER_SNOW, ModGameRules.SNOWSHOES_ALLOW_WALKING_ON_POWDER_SNOW);
    }

    public static Pair<MapCodec<SimpleAbility>, StreamCodec<ByteBuf, SimpleAbility>> lightningImmunity() {
        return codecs(ModAbilities.LIGHTNING_IMMUNITY, ModGameRules.SHOCK_PENDANT_DO_CANCEL_LIGHTNING_DAMAGE);
    }
}
