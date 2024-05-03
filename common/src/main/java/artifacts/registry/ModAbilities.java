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

import static artifacts.ability.ArtifactAbility.Type;

public class ModAbilities {

    public static final Type<AbsorbDamageAbility> ABSORB_DAMAGE = register("absorb_damage");
    public static final Type<ApplyFireResistanceAfterFireDamageAbility> APPLY_FIRE_RESISTANCE_AFTER_FIRE_DAMAGE = register("apply_fire_resistance_after_fire_damage");
    public static final Type<ApplyHasteAfterEatingAbility> APPLY_HASTE_AFTER_EATING = register("apply_haste_after_eating");
    public static final Type<ApplySpeedAfterDamageAbility> APPLY_SPEED_AFTER_DAMAGE = register("apply_speed_after_damage");
    public static final Type<SimpleAbility> ATTRACT_ITEMS = register("attract_items");
    public static final Type<AttributeModifierAbility> ATTRIBUTE_MODIFIER = register("attribute_modifier");
    public static final Type<BonusInvincibilityTicksAbility> BONUS_INVINCIBILITY_TICKS = register("bonus_invincibility_ticks");
    public static final Type<SimpleAbility> CANCEL_FALL_DAMAGE = register("cancel_fall_damage");
    public static final Type<CustomTooltipAbility> CUSTOM_TOOLTIP = register("custom_tooltip");
    public static final Type<DigSpeedAbility> DIG_SPEED = register("dig_speed");
    public static final Type<ExperienceBonusAbility> EXPERIENCE_BONUS = register("experience_bonus");
    public static final Type<DoubleJumpAbility> DOUBLE_JUMP = register("double_jump");
    public static final Type<FartAbility> FART = register("fart");
    public static final Type<FireAspectAbility> FIRE_ASPECT = register("fire_aspect");
    public static final Type<SimpleAbility> GROW_PLANTS_AFTER_EATING = register("grow_plants_after_eating");
    public static final Type<IncreaseEnchantmentLevelAbility> INCREASE_ENCHANTMENT_LEVEL = register("increase_enchantment_level");
    public static final Type<MobEffectAbility> INVISIBILITY = register("invisibility");
    public static final Type<MobEffectAbility> JUMP_BOOST = register("jump_boost");
    public static final Type<KnockbackAbility> KNOCKBACK = register("knockback");
    public static final Type<SimpleAbility> LIGHTNING_IMMUNITY = register("lightning_immunity");
    public static final Type<LimitedWaterBreathingAbility> LIMITED_WATER_BREATHING = register("limited_water_breathing");
    public static final Type<MakePiglinsNeutralAbility> MAKE_PIGLINS_NEUTRAL = register("make_piglins_neutral");
    public static final Type<HurtSoundAbility> MODIFY_HURT_SOUND = register("modify_hurt_sound");
    public static final Type<MountSpeedAbility> MOUNT_SPEED = register("mount_speed");
    public static final Type<NightVisionAbility> NIGHT_VISION = register("night_vision");
    public static final Type<ReduceEatingDurationAbility> REDUCE_EATING_DURATION = register("reduce_eating_duration");
    public static final Type<ReduceIceSlipperinessAbility> REDUCE_ICE_SLIPPERINESS = register("reduce_ice_slipperiness");
    public static final Type<RemoveBadEffectsAbility> REMOVE_BAD_EFFECTS = register("remove_bad_effects");
    public static final Type<ReplenishHungerOnGrassAbility> REPLENISH_HUNGER_ON_GRASS = register("replenish_hunger_on_grass");
    public static final Type<SimpleAbility> SCARE_CREEPERS = register("scare_creepers");
    public static final Type<SetAttackersOnFireAbility> SET_ATTACKERS_ON_FIRE = register("set_attackers_on_fire");
    public static final Type<SimpleAbility> SINKING = register("sinking");
    public static final Type<SimpleAbility> SMELT_ORES = register("smelt_ores");
    public static final Type<SimpleAbility> SPRINT_ON_WATER = register("sprint_on_water");
    public static final Type<SprintingSpeedAbility> SPRINTING_SPEED = register("sprinting_speed");
    public static final Type<SprintingStepHeightAbility> SPRINTING_STEP_HEIGHT = register("sprinting_step_height");
    public static final Type<StrikeAttackersWithLightningAbility> STRIKE_ATTACKERS_WITH_LIGHTNING = register("strike_attackers_with_lightning");
    public static final Type<SwimInAirAbility> SWIM_IN_AIR = register("swim_in_air");
    public static final Type<SwimSpeedAbility> SWIM_SPEED = register("swim_speed");
    public static final Type<TeleportOnDeathAbility> TELEPORT_ON_DEATH = register("teleport_on_death");
    public static final Type<ThornsAbility> THORNS = register("thorns");
    public static final Type<UpgradeToolTierAbility> UPGRADE_TOOL_TIER = register("upgrade_tool_tier");
    public static final Type<VillagerReputation> VILLAGER_REPUTATION = register("villager_reputation");
    public static final Type<SimpleAbility> WALK_ON_POWDER_SNOW = register("walk_on_powdered_snow");

    public static <T extends ArtifactAbility> Type<T> register(String name) {
        return new Type<>(Artifacts.id(name));
    }
}
