package artifacts.config;

import artifacts.ability.UpgradeToolTierAbility;
import artifacts.config.value.Value;
import artifacts.config.value.ValueTypes;
import artifacts.config.value.type.ValueType;
import artifacts.network.UpdateItemConfigPacket;
import artifacts.registry.ModItems;
import com.google.common.base.CaseFormat;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SameParameterValue")
public class ItemConfigs {

    private static final Map<ValueType<?, ?>, ValueMap<?>> VALUES = new HashMap<>();
    private static final List<ValueType<?, ?>> VALUE_TYPES = new ArrayList<>();

    public static final Value.ConfigValue<Boolean>
            ANTIDOTE_VESSEL_ENABLED = booleanValue(ModItems.ANTIDOTE_VESSEL, "enabled"),
            AQUA_DASHERS_ENABLED = booleanValue(ModItems.AQUA_DASHERS, "enabled"),
            CHARM_OF_SINKING_ENABLED = booleanValue(ModItems.CHARM_OF_SINKING, "enabled"),
            CLOUD_IN_A_BOTTLE_ENABLED = booleanValue(ModItems.CLOUD_IN_A_BOTTLE, "enabled"),
            ETERNAL_STEAK_ENABLED = booleanValue(ModItems.ETERNAL_STEAK, "enabled"),
            EVERLASTING_BEEF_ENABLED = booleanValue(ModItems.EVERLASTING_BEEF, "enabled"),
            KITTY_SLIPPERS_ENABLED = booleanValue(ModItems.KITTY_SLIPPERS, "enabled"),
            PICKAXE_HEATER_ENABLED = booleanValue(ModItems.PICKAXE_HEATER, "enabled"),
            ROOTED_BOOTS_ENABLED = booleanValue(ModItems.ROOTED_BOOTS, "enabled"),
            SCARF_OF_INVISIBILITY_ENABLED = booleanValue(ModItems.SCARF_OF_INVISIBILITY, "enabled"),
            UNIVERSAL_ATTRACTOR_ENABLED = booleanValue(ModItems.UNIVERSAL_ATTRACTOR, "enabled"),

            CHORUS_TOTEM_DO_CONSUME_ON_USE = booleanValue(ModItems.CHORUS_TOTEM, "consumeOnUse"),
            FLAME_PENDANT_DO_GRANT_FIRE_RESISTANCE = booleanValue(ModItems.FLAME_PENDANT, "grantFireResistance"),
            ROOTED_BOOTS_DO_GROW_PLANTS_AFTER_EATING = booleanValue(ModItems.ROOTED_BOOTS, "growPlantsAfterEating"),
            SHOCK_PENDANT_DO_CANCEL_LIGHTNING_DAMAGE = booleanValue(ModItems.SHOCK_PENDANT, "cancelLightningDamage"),
            SNORKEL_IS_INFINITE = booleanValue(ModItems.SNORKEL, "isInfinite", false),
            SNOWSHOES_ALLOW_WALKING_ON_POWDER_SNOW = booleanValue(ModItems.SNOWSHOES, "allowWalkingOnPowderSnow"),
            UMBRELLA_IS_SHIELD = booleanValue(ModItems.UMBRELLA, "isShield"),
            UMBRELLA_IS_GLIDER = booleanValue(ModItems.UMBRELLA, "isGlider");

    public static final Value.ConfigValue<Double>
            CLOUD_IN_A_BOTTLE_SPRINT_JUMP_VERTICAL_VELOCITY = nonNegativeDouble(ModItems.CLOUD_IN_A_BOTTLE, "sprintJumpVerticalVelocity", 0.25),
            CLOUD_IN_A_BOTTLE_SPRINT_JUMP_HORIZONTAL_VELOCITY = nonNegativeDouble(ModItems.CLOUD_IN_A_BOTTLE, "sprintJumpHorizontalVelocity", 0.25);

    public static final Value.ConfigValue<Double>
            BUNNY_HOPPERS_FALL_DAMAGE_MULTIPLIER = attributeModifier(ModItems.BUNNY_HOPPERS, "fallDamageMultiplier", 0),
            BUNNY_HOPPERS_JUMP_STRENGTH_BONUS = attributeModifier(ModItems.BUNNY_HOPPERS, "jumpStrengthBonus", 0.40),
            BUNNY_HOPPERS_SAFE_FALL_DISTANCE_BONUS = attributeModifier(ModItems.BUNNY_HOPPERS, "safeFallDistanceBonus", 10),
            CHARM_OF_SHRINKING_SCALE_MODIFIER = attributeModifier(ModItems.CHARM_OF_SHRINKING, "scaleModifier", -0.50),
            CLOUD_IN_A_BOTTLE_SAFE_FALL_DISTANCE_BONUS = attributeModifier(ModItems.CLOUD_IN_A_BOTTLE, "safeFallDistanceBonus", 3),
            COWBOY_HAT_MOUNT_SPEED_BONUS = attributeModifier(ModItems.COWBOY_HAT, "mountSpeedBonus", 0.40),
            CROSS_NECKLACE_BONUS_INVINCIBILITY_TICKS = attributeModifier(ModItems.CROSS_NECKLACE, "bonusInvincibilityTicks", 20),
            CRYSTAL_HEART_HEALTH_BONUS = attributeModifier(ModItems.CRYSTAL_HEART, "healthBonus", 10),
            DIGGING_CLAWS_BLOCK_BREAK_SPEED_BONUS = attributeModifier(ModItems.DIGGING_CLAWS, "blockBreakSpeedBonus", 0.30),
            FERAL_CLAWS_ATTACK_SPEED_BONUS = attributeModifier(ModItems.FERAL_CLAWS, "attackSpeedBonus", 0.40),
            FIRE_GAUNTLET_FIRE_DURATION = attributeModifier(ModItems.FIRE_GAUNTLET, "fireDuration", 8),
            FLIPPERS_SWIM_SPEED_BONUS = attributeModifier(ModItems.FLIPPERS, "swimSpeedBonus", 0.70),
            GOLDEN_HOOK_ENTITY_EXPERIENCE_BONUS = attributeModifier(ModItems.GOLDEN_HOOK, "entityExperienceBonus", 0.50),
            NOVELTY_DRINKING_HAT_DRINKING_SPEED_BONUS = attributeModifier(ModItems.NOVELTY_DRINKING_HAT, "drinkingSpeedBonus", 1.50),
            NOVELTY_DRINKING_HAT_EATING_SPEED_BONUS = attributeModifier(ModItems.NOVELTY_DRINKING_HAT, "eatingSpeedBonus", 0.50),
            PLASTIC_DRINKING_HAT_DRINKING_SPEED_BONUS = attributeModifier(ModItems.PLASTIC_DRINKING_HAT, "drinkingSpeedBonus", 1.50),
            PLASTIC_DRINKING_HAT_EATING_SPEED_BONUS = attributeModifier(ModItems.PLASTIC_DRINKING_HAT, "eatingSpeedBonus", 0.50),
            POCKET_PISTON_ATTACK_KNOCKBACK_BONUS = attributeModifier(ModItems.POCKET_PISTON, "attackKnockbackBonus", 0.75),
            POWER_GLOVE_ATTACK_DAMAGE_BONUS = attributeModifier(ModItems.POWER_GLOVE, "attackDamageBonus", 4),
            RUNNING_SHOES_SPRINTING_SPEED_BONUS = attributeModifier(ModItems.RUNNING_SHOES, "sprintingSpeedBonus", 0.40),
            RUNNING_SHOES_SPRINTING_STEP_HEIGHT_BONUS = attributeModifier(ModItems.RUNNING_SHOES, "sprintingStepHeightBonus", 0.5),
            SNOWSHOES_MOVEMENT_SPEED_ON_SNOW_BONUS = attributeModifier(ModItems.SNOWSHOES, "movementSpeedOnSnowBonus", 0.50),
            STEADFAST_SPIKES_KNOCKBACK_RESISTANCE = attributeModifier(ModItems.STEADFAST_SPIKES, "knockbackResistance", 1.00),
            STEADFAST_SPIKES_SLIPPERINESS_REDUCTION = attributeModifier(ModItems.STEADFAST_SPIKES, "slipperinessReduction", 1.00),
            VAMPIRIC_GLOVE_MAX_HEALING_PER_HIT = attributeModifier(ModItems.VAMPIRIC_GLOVE, "maxHealingPerHit", 6),
            VILLAGER_HAT_REPUTATION_BONUS = attributeModifier(ModItems.VILLAGER_HAT, "reputationBonus", 0.75);

    public static final Value.ConfigValue<Double>
            WHOOPEE_CUSHION_FART_CHANCE = fraction(ModItems.WHOOPEE_CUSHION, "fartChance", 0.12),
            CHORUS_TOTEM_TELEPORTATION_CHANCE = fraction(ModItems.CHORUS_TOTEM, "teleportationChance", 1.00),
            FLAME_PENDANT_STRIKE_CHANCE = fraction(ModItems.FLAME_PENDANT, "strikeChance", 0.40),
            NIGHT_VISION_GOGGLES_STRENGTH = fraction(ModItems.NIGHT_VISION_GOGGLES, "strength", 0.15),
            THORN_PENDANT_STRIKE_CHANCE = fraction(ModItems.THORN_PENDANT, "strikeChance", 0.50),
            SHOCK_PENDANT_STRIKE_CHANCE = fraction(ModItems.SHOCK_PENDANT, "strikeChance", 0.25),
            VAMPIRIC_GLOVE_ABSORPTION_RATIO = fraction(ModItems.VAMPIRIC_GLOVE, "absorptionRatio", 0.20);

    public static final Value.ConfigValue<Integer>
            CHORUS_TOTEM_HEALTH_RESTORED = nonNegativeInt(ModItems.CHORUS_TOTEM, "healthRestored", 10),
            THORN_PENDANT_MAX_DAMAGE = nonNegativeInt(ModItems.THORN_PENDANT, "maxDamage", 6),
            THORN_PENDANT_MIN_DAMAGE = nonNegativeInt(ModItems.THORN_PENDANT, "minDamage", 2);

    public static final Value.ConfigValue<Integer>
            ANTIDOTE_VESSEL_MAX_EFFECT_DURATION = duration(ModItems.ANTIDOTE_VESSEL, "maxEffectDuration", 5),
            CHORUS_TOTEM_COOLDOWN = duration(ModItems.CHORUS_TOTEM, "cooldown", 0),
            CROSS_NECKLACE_COOLDOWN = duration(ModItems.CROSS_NECKLACE, "cooldown", 0),
            ETERNAL_STEAK_COOLDOWN = duration(ModItems.ETERNAL_STEAK, "cooldown", 15),
            EVERLASTING_BEEF_COOLDOWN = duration(ModItems.EVERLASTING_BEEF, "cooldown", 15),
            FLAME_PENDANT_COOLDOWN = duration(ModItems.FLAME_PENDANT, "cooldown", 0),
            FLAME_PENDANT_FIRE_DURATION = duration(ModItems.FLAME_PENDANT, "fireDuration", 10),
            HELIUM_FLAMINGO_FLIGHT_DURATION = duration(ModItems.HELIUM_FLAMINGO, "flightDuration", 8),
            HELIUM_FLAMINGO_RECHARGE_DURATION = duration(ModItems.HELIUM_FLAMINGO, "rechargeDuration", 15),
            OBSIDIAN_SKULL_FIRE_RESISTANCE_COOLDOWN = duration(ModItems.OBSIDIAN_SKULL, "fireResistanceCooldown", 60),
            OBSIDIAN_SKULL_FIRE_RESISTANCE_DURATION = duration(ModItems.OBSIDIAN_SKULL, "fireResistanceDuration", 30),
            ONION_RING_HASTE_DURATION_PER_FOOD_POINT = duration(ModItems.ONION_RING, "hasteDurationPerFoodPoint", 6),
            PANIC_NECKLACE_COOLDOWN = duration(ModItems.PANIC_NECKLACE, "cooldown", 0),
            PANIC_NECKLACE_SPEED_DURATION = duration(ModItems.PANIC_NECKLACE, "speedDuration", 8),
            ROOTED_BOOTS_HUNGER_REPLENISHING_DURATION = duration(ModItems.ROOTED_BOOTS, "hungerReplenishingDuration", 10),
            SHOCK_PENDANT_COOLDOWN = duration(ModItems.SHOCK_PENDANT, "cooldown", 0),
            SNORKEL_WATER_BREATHING_DURATION = duration(ModItems.SNORKEL, "waterBreathingDuration", 15),
            THORN_PENDANT_COOLDOWN = duration(ModItems.THORN_PENDANT, "cooldown", 0);

    public static final Value.ConfigValue<Integer>
            ANGLERS_HAT_LUCK_OF_THE_SEA_LEVEL_BONUS = enchantmentLevel(ModItems.ANGLERS_HAT, "luckOfTheSeaLevelBonus", 1),
            ANGLERS_HAT_LURE_LEVEL_BONUS = enchantmentLevel(ModItems.ANGLERS_HAT, "lureLevelBonus", 1),
            LUCKY_SCARF_FORTUNE_BONUS = nonNegativeInt(ModItems.LUCKY_SCARF, "fortuneBonus", 1),
            SUPERSTITIOUS_HAT_LOOTING_LEVEL_BONUS = nonNegativeInt(ModItems.SUPERSTITIOUS_HAT, "lootingLevelBonus", 1);

    public static final Value.ConfigValue<Integer>
            ONION_RING_HASTE_LEVEL = mobEffectLevel(ModItems.ONION_RING, "hasteLevel", 2),
            PANIC_NECKLACE_SPEED_LEVEL = mobEffectLevel(ModItems.PANIC_NECKLACE, "speedLevel", 1);

    public static final Value.ConfigValue<UpgradeToolTierAbility.Tier>
            DIGGING_CLAWS_TOOL_TIER = register(ModItems.DIGGING_CLAWS, "toolTier", ValueTypes.TOOL_TIER, UpgradeToolTierAbility.Tier.STONE);

    private static Value.ConfigValue<Boolean> booleanValue(Holder<? extends Item> holder, String name) {
        return booleanValue(holder, name, true);
    }

    private static Value.ConfigValue<Boolean> booleanValue(Holder<? extends Item> holder, String name, boolean defaultValue) {
        return register(holder, name, ValueTypes.BOOLEAN, defaultValue);
    }

    private static Value.ConfigValue<Double> nonNegativeDouble(Holder<? extends Item> holder, String name, double defaultValue) {
        return register(holder, name, ValueTypes.NON_NEGATIVE_DOUBLE, defaultValue);
    }

    private static Value.ConfigValue<Double> attributeModifier(Holder<? extends Item> holder, String name, double defaultValue) {
        return register(holder, name, ValueTypes.ATTRIBUTE_MODIFIER_AMOUNT, defaultValue);
    }

    private static Value.ConfigValue<Double> fraction(Holder<? extends Item> holder, String name, double defaultValue) {
        return register(holder, name, ValueTypes.FRACTION, defaultValue);
    }

    private static Value.ConfigValue<Integer> nonNegativeInt(Holder<? extends Item> holder, String name, int defaultValue) {
        return register(holder, name, ValueTypes.NON_NEGATIVE_INT, defaultValue);
    }

    private static Value.ConfigValue<Integer> duration(Holder<? extends Item> holder, String name, int defaultValue) {
        return register(holder, name, ValueTypes.DURATION, defaultValue);
    }

    private static Value.ConfigValue<Integer> enchantmentLevel(Holder<? extends Item> holder, String name, int defaultValue) {
        return register(holder, name, ValueTypes.ENCHANTMENT_LEVEL, defaultValue);
    }

    private static Value.ConfigValue<Integer> mobEffectLevel(Holder<? extends Item> holder, String name, int defaultValue) {
        return register(holder, name, ValueTypes.MOB_EFFECT_LEVEL, defaultValue);
    }

    private static <T> Value.ConfigValue<T> register(Holder<? extends Item> holder, String name, ValueType<T, ?> type, T defaultValue) {
        String id = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, holder.unwrapKey().orElseThrow().location().getPath()) + '.' + name;
        if (!type.isCorrect(defaultValue)) {
            throw new IllegalArgumentException(type.makeError(defaultValue));
        }
        Value.ConfigValue<T> value = new Value.ConfigValue<>(type, id, defaultValue);
        getValues(type).put(id, value);
        return value;
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<String, Value.ConfigValue<T>> getValues(ValueType<T, ?> type) {
        if (!VALUE_TYPES.contains(type)) {
            VALUES.put(type, new ValueMap<>());
            VALUE_TYPES.add(type);
        }
        ValueMap<T> valueMap = (ValueMap<T>) VALUES.get(type);
        return valueMap.getMap();
    }

    public static List<ValueType<?, ?>> getValueTypes() {
        return VALUE_TYPES;
    }

    public static void loadFromConfig(MinecraftServer server) {
        for (ValueMap<?> map : VALUES.values()) {
            map.loadValues(server);
        }
    }

    public static void sendToPlayer(ServerPlayer player) {
        for (ValueMap<?> map : VALUES.values()) {
            map.sendToPlayer(player);
        }
    }

    private static class ValueMap<T> {
        private final Map<String, Value.ConfigValue<T>> map = new HashMap<>();

        public Map<String, Value.ConfigValue<T>> getMap() {
            return map;
        }

        public void loadValues(MinecraftServer server) {
            map.forEach((key, config) -> {
                T value = config.type().read(ConfigManager.getConfigValue(key));
                if (!config.get().equals(value)) {
                    config.set(value);
                    NetworkManager.sendToPlayers(server.getPlayerList().getPlayers(), new UpdateItemConfigPacket(config));
                }
            });
        }

        public void sendToPlayer(ServerPlayer player) {
            map.forEach((key, config) -> NetworkManager.sendToPlayer(player, new UpdateItemConfigPacket(config)));
        }
    }
}
