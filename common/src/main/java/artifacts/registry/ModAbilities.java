package artifacts.registry;

import artifacts.Artifacts;
import artifacts.ability.*;
import artifacts.ability.mobeffect.GenericMobEffectAbility;
import artifacts.ability.mobeffect.LimitedWaterBreathingAbility;
import artifacts.ability.mobeffect.NightVisionAbility;
import artifacts.ability.retaliation.SetAttackersOnFireAbility;
import artifacts.ability.retaliation.StrikeAttackersWithLightningAbility;
import artifacts.ability.retaliation.ThornsAbility;
import com.mojang.serialization.MapCodec;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

import static artifacts.ability.ArtifactAbility.Type;

public class ModAbilities {

    private static final ResourceLocation REGISTRY_ID = Artifacts.id("ability_types");
    public static final Registrar<Type<?>> REGISTRY = RegistrarManager.get(Artifacts.MOD_ID).<Type<?>>builder(REGISTRY_ID)
            .syncToClients()
            .build();

    public static final RegistrySupplier<Type<ApplyCooldownAfterDamageAbility>> APPLY_COOLDOWN_AFTER_DAMAGE = register("apply_cooldown_after_damage", ApplyCooldownAfterDamageAbility.CODEC, ApplyCooldownAfterDamageAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<ApplyFireResistanceAfterFireDamageAbility>> APPLY_FIRE_RESISTANCE_AFTER_FIRE_DAMAGE = register("apply_fire_resistance_after_fire_damage", ApplyFireResistanceAfterFireDamageAbility.CODEC, ApplyFireResistanceAfterFireDamageAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<ApplyHasteAfterEatingAbility>> APPLY_HASTE_AFTER_EATING = register("apply_haste_after_eating", ApplyHasteAfterEatingAbility.CODEC, ApplyHasteAfterEatingAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<ApplySpeedAfterDamageAbility>> APPLY_SPEED_AFTER_DAMAGE = register("apply_speed_after_damage", ApplySpeedAfterDamageAbility.CODEC, ApplySpeedAfterDamageAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<AttractItemsAbility>> ATTRACT_ITEMS = register("attract_items", AttractItemsAbility.CODEC, AttractItemsAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<AttributeModifierAbility>> ATTRIBUTE_MODIFIER = register("attribute_modifier", AttributeModifierAbility.CODEC, AttributeModifierAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<SimpleAbility>> CANCEL_FALL_DAMAGE = register("cancel_fall_damage", SimpleAbility::createType);
    public static final RegistrySupplier<Type<CustomTooltipAbility>> CUSTOM_TOOLTIP = register("custom_tooltip", CustomTooltipAbility.CODEC, CustomTooltipAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<DoubleJumpAbility>> DOUBLE_JUMP = register("double_jump", DoubleJumpAbility.CODEC, DoubleJumpAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<GrowPlantsAfterEatingAbility>> GROW_PLANTS_AFTER_EATING = register("grow_plants_after_eating", GrowPlantsAfterEatingAbility.CODEC, GrowPlantsAfterEatingAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<IncreaseEnchantmentLevelAbility>> INCREASE_ENCHANTMENT_LEVEL = register("increase_enchantment_level", IncreaseEnchantmentLevelAbility.CODEC, IncreaseEnchantmentLevelAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<SimpleAbility>> LIGHTNING_IMMUNITY = register("lightning_immunity", SimpleAbility::createType);
    public static final RegistrySupplier<Type<LimitedWaterBreathingAbility>> LIMITED_WATER_BREATHING = register("limited_water_breathing", LimitedWaterBreathingAbility.CODEC, LimitedWaterBreathingAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<MakePiglinsNeutralAbility>> MAKE_PIGLINS_NEUTRAL = register("make_piglins_neutral", MakePiglinsNeutralAbility.CODEC, MakePiglinsNeutralAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<GenericMobEffectAbility>> MOB_EFFECT = register("mob_effect", GenericMobEffectAbility.CODEC, GenericMobEffectAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<HurtSoundAbility>> MODIFY_HURT_SOUND = register("modify_hurt_sound", HurtSoundAbility.CODEC, HurtSoundAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<NightVisionAbility>> NIGHT_VISION = register("night_vision", NightVisionAbility.CODEC, NightVisionAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<RemoveBadEffectsAbility>> REMOVE_BAD_EFFECTS = register("remove_bad_effects", RemoveBadEffectsAbility.CODEC, RemoveBadEffectsAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<ReplenishHungerOnGrassAbility>> REPLENISH_HUNGER_ON_GRASS = register("replenish_hunger_on_grass", ReplenishHungerOnGrassAbility.CODEC, ReplenishHungerOnGrassAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<SimpleAbility>> SCARE_CREEPERS = register("scare_creepers", SimpleAbility::createType);
    public static final RegistrySupplier<Type<SetAttackersOnFireAbility>> SET_ATTACKERS_ON_FIRE = register("set_attackers_on_fire", SetAttackersOnFireAbility.CODEC, SetAttackersOnFireAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<SimpleAbility>> SINKING = register("sinking", SimpleAbility::createType);
    public static final RegistrySupplier<Type<SimpleAbility>> SMELT_ORES = register("smelt_ores", SimpleAbility::createType);
    public static final RegistrySupplier<Type<SimpleAbility>> SPRINT_ON_WATER = register("sprint_on_water", SimpleAbility::createType);
    public static final RegistrySupplier<Type<StrikeAttackersWithLightningAbility>> STRIKE_ATTACKERS_WITH_LIGHTNING = register("strike_attackers_with_lightning", StrikeAttackersWithLightningAbility.CODEC, StrikeAttackersWithLightningAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<SwimInAirAbility>> SWIM_IN_AIR = register("swim_in_air", SwimInAirAbility.CODEC, SwimInAirAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<TeleportOnDeathAbility>> TELEPORT_ON_DEATH = register("teleport_on_death", TeleportOnDeathAbility.CODEC, TeleportOnDeathAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<ThornsAbility>> THORNS = register("thorns", ThornsAbility.CODEC, ThornsAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<UpgradeToolTierAbility>> UPGRADE_TOOL_TIER = register("upgrade_tool_tier", UpgradeToolTierAbility.CODEC, UpgradeToolTierAbility.STREAM_CODEC);
    public static final RegistrySupplier<Type<SimpleAbility>> WALK_ON_POWDER_SNOW = register("walk_on_powdered_snow", SimpleAbility::createType);

    public static void register() {
        // no-op
    }

    public static <T extends ArtifactAbility> RegistrySupplier<Type<T>> register(String name, MapCodec<T> codec, StreamCodec<ByteBuf, T> streamCodec) {
        return register(name, () -> new Type<>(codec, streamCodec));
    }

    public static <T extends ArtifactAbility> RegistrySupplier<Type<T>> register(String name, Supplier<Type<T>> type) {
        return RegistrySupplier.of(REGISTRY.register(Artifacts.id(name), type));
    }
}
