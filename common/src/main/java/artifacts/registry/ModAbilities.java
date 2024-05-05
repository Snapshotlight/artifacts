package artifacts.registry;

import artifacts.Artifacts;
import artifacts.ability.*;
import artifacts.ability.mobeffect.LimitedWaterBreathingAbility;
import artifacts.ability.mobeffect.MobEffectAbility;
import artifacts.ability.mobeffect.MountSpeedAbility;
import artifacts.ability.mobeffect.NightVisionAbility;
import artifacts.ability.retaliation.SetAttackersOnFireAbility;
import artifacts.ability.retaliation.StrikeAttackersWithLightningAbility;
import artifacts.ability.retaliation.ThornsAbility;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

import static artifacts.ability.ArtifactAbility.Type;

public class ModAbilities {

    private static final ResourceLocation REGISTRY_ID = Artifacts.id("ability_types");
    private static final ResourceKey<Registry<Type<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(REGISTRY_ID);
    public static final Registrar<Type<?>> REGISTRY = RegistrarManager.get(Artifacts.MOD_ID).<Type<?>>builder(REGISTRY_ID)
            .syncToClients()
            .build();

    public static final Supplier<Type<AbsorbDamageAbility>> ABSORB_DAMAGE = register("absorb_damage");
    public static final RegistrySupplier<Type<ApplyFireResistanceAfterFireDamageAbility>> APPLY_FIRE_RESISTANCE_AFTER_FIRE_DAMAGE = register("apply_fire_resistance_after_fire_damage");
    public static final RegistrySupplier<Type<ApplyHasteAfterEatingAbility>> APPLY_HASTE_AFTER_EATING = register("apply_haste_after_eating");
    public static final RegistrySupplier<Type<ApplySpeedAfterDamageAbility>> APPLY_SPEED_AFTER_DAMAGE = register("apply_speed_after_damage");
    public static final RegistrySupplier<Type<SimpleAbility>> ATTRACT_ITEMS = register("attract_items");
    public static final RegistrySupplier<Type<AttributeModifierAbility>> ATTRIBUTE_MODIFIER = register("attribute_modifier");
    public static final RegistrySupplier<Type<BonusInvincibilityTicksAbility>> BONUS_INVINCIBILITY_TICKS = register("bonus_invincibility_ticks");
    public static final RegistrySupplier<Type<SimpleAbility>> CANCEL_FALL_DAMAGE = register("cancel_fall_damage");
    public static final RegistrySupplier<Type<CustomTooltipAbility>> CUSTOM_TOOLTIP = register("custom_tooltip");
    public static final RegistrySupplier<Type<DigSpeedAbility>> DIG_SPEED = register("dig_speed");
    public static final RegistrySupplier<Type<ExperienceBonusAbility>> EXPERIENCE_BONUS = register("experience_bonus");
    public static final RegistrySupplier<Type<DoubleJumpAbility>> DOUBLE_JUMP = register("double_jump");
    public static final RegistrySupplier<Type<FartAbility>> FART = register("fart");
    public static final RegistrySupplier<Type<FireAspectAbility>> FIRE_ASPECT = register("fire_aspect");
    public static final RegistrySupplier<Type<SimpleAbility>> GROW_PLANTS_AFTER_EATING = register("grow_plants_after_eating");
    public static final RegistrySupplier<Type<IncreaseEnchantmentLevelAbility>> INCREASE_ENCHANTMENT_LEVEL = register("increase_enchantment_level");
    public static final RegistrySupplier<Type<MobEffectAbility>> INVISIBILITY = register("invisibility");
    public static final RegistrySupplier<Type<MobEffectAbility>> JUMP_BOOST = register("jump_boost");
    public static final RegistrySupplier<Type<KnockbackAbility>> KNOCKBACK = register("knockback");
    public static final RegistrySupplier<Type<SimpleAbility>> LIGHTNING_IMMUNITY = register("lightning_immunity");
    public static final RegistrySupplier<Type<LimitedWaterBreathingAbility>> LIMITED_WATER_BREATHING = register("limited_water_breathing");
    public static final RegistrySupplier<Type<MakePiglinsNeutralAbility>> MAKE_PIGLINS_NEUTRAL = register("make_piglins_neutral");
    public static final RegistrySupplier<Type<HurtSoundAbility>> MODIFY_HURT_SOUND = register("modify_hurt_sound");
    public static final RegistrySupplier<Type<MountSpeedAbility>> MOUNT_SPEED = register("mount_speed");
    public static final RegistrySupplier<Type<NightVisionAbility>> NIGHT_VISION = register("night_vision");
    public static final RegistrySupplier<Type<ReduceEatingDurationAbility>> REDUCE_EATING_DURATION = register("reduce_eating_duration");
    public static final RegistrySupplier<Type<ReduceIceSlipperinessAbility>> REDUCE_ICE_SLIPPERINESS = register("reduce_ice_slipperiness");
    public static final RegistrySupplier<Type<RemoveBadEffectsAbility>> REMOVE_BAD_EFFECTS = register("remove_bad_effects");
    public static final RegistrySupplier<Type<ReplenishHungerOnGrassAbility>> REPLENISH_HUNGER_ON_GRASS = register("replenish_hunger_on_grass");
    public static final RegistrySupplier<Type<SimpleAbility>> SCARE_CREEPERS = register("scare_creepers");
    public static final RegistrySupplier<Type<SetAttackersOnFireAbility>> SET_ATTACKERS_ON_FIRE = register("set_attackers_on_fire");
    public static final RegistrySupplier<Type<SimpleAbility>> SINKING = register("sinking");
    public static final RegistrySupplier<Type<SimpleAbility>> SMELT_ORES = register("smelt_ores");
    public static final RegistrySupplier<Type<SimpleAbility>> SPRINT_ON_WATER = register("sprint_on_water");
    public static final RegistrySupplier<Type<SprintingSpeedAbility>> SPRINTING_SPEED = register("sprinting_speed");
    public static final RegistrySupplier<Type<SprintingStepHeightAbility>> SPRINTING_STEP_HEIGHT = register("sprinting_step_height");
    public static final RegistrySupplier<Type<StrikeAttackersWithLightningAbility>> STRIKE_ATTACKERS_WITH_LIGHTNING = register("strike_attackers_with_lightning");
    public static final RegistrySupplier<Type<SwimInAirAbility>> SWIM_IN_AIR = register("swim_in_air");
    public static final RegistrySupplier<Type<SwimSpeedAbility>> SWIM_SPEED = register("swim_speed");
    public static final RegistrySupplier<Type<TeleportOnDeathAbility>> TELEPORT_ON_DEATH = register("teleport_on_death");
    public static final RegistrySupplier<Type<ThornsAbility>> THORNS = register("thorns");
    public static final RegistrySupplier<Type<UpgradeToolTierAbility>> UPGRADE_TOOL_TIER = register("upgrade_tool_tier");
    public static final RegistrySupplier<Type<VillagerReputation>> VILLAGER_REPUTATION = register("villager_reputation");
    public static final RegistrySupplier<Type<SimpleAbility>> WALK_ON_POWDER_SNOW = register("walk_on_powdered_snow");

    public static void register() {

    }

    public static <T extends ArtifactAbility> RegistrySupplier<Type<T>> register(String name) {
        return RegistrySupplier.of(REGISTRY.register(Artifacts.id(name), Type::new));
    }
}
