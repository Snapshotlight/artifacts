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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SameParameterValue")
public class ItemConfigs {

    static final Map<String, List<String>> TOOLTIPS = new HashMap<>();

    private static final Map<ValueType<?, ?>, ValueMap<?>> VALUES = new HashMap<>();
    private static final List<ValueType<?, ?>> VALUE_TYPES = new ArrayList<>();

    public static final Map<ResourceLocation, List<String>> ITEM_TO_KEYS = new HashMap<>();

    public static final Value.ConfigValue<Boolean>
            ANTIDOTE_VESSEL_ENABLED = booleanValue(ModItems.ANTIDOTE_VESSEL, "enabled",
                    "Whether the Antidote Vessel reduces the duration of negative effects"),
            AQUA_DASHERS_ENABLED = booleanValue(ModItems.AQUA_DASHERS, "enabled",
                    "Whether the Aqua-Dashers allow the wearer to sprint on water"),
            CHARM_OF_SINKING_ENABLED = booleanValue(ModItems.CHARM_OF_SINKING, "enabled",
                    "Whether the Charm of Sinking removes the wearer's collision with water"),
            CLOUD_IN_A_BOTTLE_ENABLED = booleanValue(ModItems.CLOUD_IN_A_BOTTLE, "enabled",
                    "Whether the Cloud in a Bottle allows the wearer to double jump"),
            ETERNAL_STEAK_ENABLED = booleanValue(ModItems.ETERNAL_STEAK, "enabled",
                    "Whether the Eternal Steak can be eaten"),
            EVERLASTING_BEEF_ENABLED = booleanValue(ModItems.EVERLASTING_BEEF, "enabled",
                    "Whether the Everlasting Beef can be eaten"),
            KITTY_SLIPPERS_ENABLED = booleanValue(ModItems.KITTY_SLIPPERS, "enabled",
                    "Whether the Kitty Slippers scare nearby creepers"),
            PICKAXE_HEATER_ENABLED = booleanValue(ModItems.PICKAXE_HEATER, "enabled",
                    "Whether the Pickaxe Heater smelts mined ores"),
            ROOTED_BOOTS_ENABLED = booleanValue(ModItems.ROOTED_BOOTS, "enabled",
                    "Whether the Rooted Boots replenish hunger when standing on grass"),
            SCARF_OF_INVISIBILITY_ENABLED = booleanValue(ModItems.SCARF_OF_INVISIBILITY, "enabled",
                    "Whether the Scarf of Invisibility makes players invisible"),
            UNIVERSAL_ATTRACTOR_ENABLED = booleanValue(ModItems.UNIVERSAL_ATTRACTOR, "enabled",
                    "Whether the Universal Attractor attracts nearby items"),

            CHORUS_TOTEM_DO_CONSUME_ON_USE = booleanValue(ModItems.CHORUS_TOTEM, "consumeOnUse",
                    "Whether the Chorus Totem is consumed after activating"),
            FLAME_PENDANT_DO_GRANT_FIRE_RESISTANCE = booleanValue(ModItems.FLAME_PENDANT, "grantFireResistance",
                    "Whether the Flame Pendant grants Fire Resistance after igniting an entity"),
            ROOTED_BOOTS_DO_GROW_PLANTS_AFTER_EATING = booleanValue(ModItems.ROOTED_BOOTS, "growPlantsAfterEating",
                    "Whether the Rooted Boots apply a bone meal effect after eating food"),
            SHOCK_PENDANT_DO_CANCEL_LIGHTNING_DAMAGE = booleanValue(ModItems.SHOCK_PENDANT, "cancelLightningDamage",
                    "Whether the Shock Pendant cancels damage from lightning"),
            SNORKEL_IS_INFINITE = booleanValue(ModItems.SNORKEL, "isInfinite", false,
                    "Whether the Snorkel's water breathing effect depletes when underwater"),
            SNOWSHOES_ALLOW_WALKING_ON_POWDER_SNOW = booleanValue(ModItems.SNOWSHOES, "allowWalkingOnPowderSnow",
                    "Whether the Snowshoes allow the wearer to walk on powdered snow"),
            UMBRELLA_IS_SHIELD = booleanValue(ModItems.UMBRELLA, "isShield",
                    "Whether the Umbrella can be used as a shield"),
            UMBRELLA_IS_GLIDER = booleanValue(ModItems.UMBRELLA, "isGlider",
                    "Whether the Umbrella slows the player's falling speed when held");

    public static final Value.ConfigValue<Double>
            CLOUD_IN_A_BOTTLE_SPRINT_JUMP_VERTICAL_VELOCITY = nonNegativeDouble(ModItems.CLOUD_IN_A_BOTTLE, "sprintJumpVerticalVelocity", 0.25,
                    "The amount of extra vertical velocity that is applied to players " +
                            "that double jump while sprinting using the Cloud in a Bottle"),
            CLOUD_IN_A_BOTTLE_SPRINT_JUMP_HORIZONTAL_VELOCITY = nonNegativeDouble(ModItems.CLOUD_IN_A_BOTTLE, "sprintJumpHorizontalVelocity", 0.25,
                    "The amount of extra horizontal velocity that is applied to players " +
                            "that double jump while sprinting using the Cloud in a Bottle");

    public static final Value.ConfigValue<Double>
            BUNNY_HOPPERS_FALL_DAMAGE_MULTIPLIER = attributeModifier(ModItems.BUNNY_HOPPERS, "fallDamageMultiplier", 0,
                    "How much the Bunny Hoppers reduce or increase fall damage",
                    "Values between -1 and 0 reduce fall damage",
                    "Values above 0 increase fall damage"),
            BUNNY_HOPPERS_JUMP_STRENGTH_BONUS = attributeModifier(ModItems.BUNNY_HOPPERS, "jumpStrengthBonus", 0.40,
                    "The amount of extra jump strength the Bunny Hoppers apply to players"),
            BUNNY_HOPPERS_SAFE_FALL_DISTANCE_BONUS = attributeModifier(ModItems.BUNNY_HOPPERS, "safeFallDistanceBonus", 10,
                    "The amount of extra safe fall distance in blocks that is granted by the Bunny Hoppers"),
            CHARM_OF_SHRINKING_SCALE_MODIFIER = attributeModifier(ModItems.CHARM_OF_SHRINKING, "scaleModifier", -0.50,
                    "How much the Charm of Shrinking decreases or increases the player's Scale",
                    "Values between -1 and 0 reduce the player's scale",
                    "Values above 0 increase the player's scale"),
            CLOUD_IN_A_BOTTLE_SAFE_FALL_DISTANCE_BONUS = attributeModifier(ModItems.CLOUD_IN_A_BOTTLE, "safeFallDistanceBonus", 3,
                    "The amount of extra safe fall distance in blocks that is granted by the Cloud in a Bottle"),
            COWBOY_HAT_MOUNT_SPEED_BONUS = attributeModifier(ModItems.COWBOY_HAT, "mountSpeedBonus", 0.40,
                    "How much the Cowboy Hat increases the speed of ridden mounts"),
            CROSS_NECKLACE_BONUS_INVINCIBILITY_TICKS = attributeModifier(ModItems.CROSS_NECKLACE, "bonusInvincibilityTicks", 20,
                    "The amount of extra ticks the player stays invincible for " +
                            "after taking damage while wearing the Cross Necklace"),
            CRYSTAL_HEART_HEALTH_BONUS = attributeModifier(ModItems.CRYSTAL_HEART, "healthBonus", 10,
                    "The amount of extra health points that are granted by the crystal heart"),
            DIGGING_CLAWS_BLOCK_BREAK_SPEED_BONUS = attributeModifier(ModItems.DIGGING_CLAWS, "blockBreakSpeedBonus", 0.30,
                    "How much the digging claws increase the wearer's mining speed"),
            FERAL_CLAWS_ATTACK_SPEED_BONUS = attributeModifier(ModItems.FERAL_CLAWS, "attackSpeedBonus", 0.40,
                    "How much the feral claws increase the wearer's attack speed"),
            FIRE_GAUNTLET_FIRE_DURATION = attributeModifier(ModItems.FIRE_GAUNTLET, "fireDuration", 8,
                    "How long an entity is set on fire for after being attacked by an entity wearing the Fire Gauntlet"),
            FLIPPERS_SWIM_SPEED_BONUS = attributeModifier(ModItems.FLIPPERS, "swimSpeedBonus", 0.70,
                    "How much the Flippers increase the wearer's swim speed"),
            GOLDEN_HOOK_ENTITY_EXPERIENCE_BONUS = attributeModifier(ModItems.GOLDEN_HOOK, "entityExperienceBonus", 0.50,
                    "The amount of extra experience dropped by entities " +
                            "that are killed by players wearing the Golden Hook"),
            NOVELTY_DRINKING_HAT_DRINKING_SPEED_BONUS = attributeModifier(ModItems.NOVELTY_DRINKING_HAT, "drinkingSpeedBonus", 1.50,
                    "How much the Novelty Drinking Hat increases the wearer's drinking speed"),
            NOVELTY_DRINKING_HAT_EATING_SPEED_BONUS = attributeModifier(ModItems.NOVELTY_DRINKING_HAT, "eatingSpeedBonus", 0.50,
                    "How much the Novelty Drinking Hat increases the wearer's eating speed"),
            PLASTIC_DRINKING_HAT_DRINKING_SPEED_BONUS = attributeModifier(ModItems.PLASTIC_DRINKING_HAT, "drinkingSpeedBonus", 1.50,
                    "How much the Plastic Drinking Hat increases the wearer's drinking speed"),
            PLASTIC_DRINKING_HAT_EATING_SPEED_BONUS = attributeModifier(ModItems.PLASTIC_DRINKING_HAT, "eatingSpeedBonus", 0.50,
                    "How much the Plastic Drinking Hat increases the wearer's eating speed"),
            POCKET_PISTON_ATTACK_KNOCKBACK_BONUS = attributeModifier(ModItems.POCKET_PISTON, "attackKnockbackBonus", 0.75,
                    "The amount of extra knockback that is granted by the Pocket Piston"),
            POWER_GLOVE_ATTACK_DAMAGE_BONUS = attributeModifier(ModItems.POWER_GLOVE, "attackDamageBonus", 4,
                    "The amount of extra damage that is dealt by melee attacks from players wearing the Power Glove"),
            RUNNING_SHOES_SPRINTING_SPEED_BONUS = attributeModifier(ModItems.RUNNING_SHOES, "sprintingSpeedBonus", 0.40,
                    "How much the Running Shoes increase the wearer's sprinting speed"),
            RUNNING_SHOES_SPRINTING_STEP_HEIGHT_BONUS = attributeModifier(ModItems.RUNNING_SHOES, "sprintingStepHeightBonus", 0.5,
                    "How much the Running Shoes increase the wearer's step height while sprinting"),
            SNOWSHOES_MOVEMENT_SPEED_ON_SNOW_BONUS = attributeModifier(ModItems.SNOWSHOES, "movementSpeedOnSnowBonus", 0.50,
                    "How much the Snowshoes increase the wearer's movement speed on snow blocks"),
            STEADFAST_SPIKES_KNOCKBACK_RESISTANCE = attributeModifier(ModItems.STEADFAST_SPIKES, "knockbackResistance", 1.00,
                    "How much knockback resistance is granted by the Steadfast Spikes"),
            STEADFAST_SPIKES_SLIPPERINESS_REDUCTION = attributeModifier(ModItems.STEADFAST_SPIKES, "slipperinessReduction", 1.00,
                    "How much the Steadfast Spikes reduce the slipperiness of ice"),
            VAMPIRIC_GLOVE_MAX_HEALING_PER_HIT = attributeModifier(ModItems.VAMPIRIC_GLOVE, "maxHealingPerHit", 6,
                    "The maximum amount of healing that can be absorbed in a single hit " +
                            "when attacking an entity while wearing the Vampiric Glove"),
            VILLAGER_HAT_REPUTATION_BONUS = attributeModifier(ModItems.VILLAGER_HAT, "reputationBonus", 0.75,
                    "The amount of extra reputation that is granted by the villager hat when trading with villagers");

    public static final Value.ConfigValue<Double>
            WHOOPEE_CUSHION_FART_CHANCE = fraction(ModItems.WHOOPEE_CUSHION, "fartChance", 0.12,
                    "The probability that a fart sound plays when sneaking " +
                            "or double jumping while wearing the Whoopee Cushion"),
            CHORUS_TOTEM_TELEPORTATION_CHANCE = fraction(ModItems.CHORUS_TOTEM, "teleportationChance", 1.00,
                    "The probability that the Chorus Totem activates when a player dies"),
            FLAME_PENDANT_STRIKE_CHANCE = fraction(ModItems.FLAME_PENDANT, "strikeChance", 0.40,
                    "The probability that the flame pendant lights an attacker on fire"),
            NIGHT_VISION_GOGGLES_STRENGTH = fraction(ModItems.NIGHT_VISION_GOGGLES, "strength", 0.15,
                    "The strength of the night vision effect applied by the Night Vision Goggles"),
            THORN_PENDANT_STRIKE_CHANCE = fraction(ModItems.THORN_PENDANT, "strikeChance", 0.50,
                    "The probability that the thorn pendant damages an attacking entity"),
            SHOCK_PENDANT_STRIKE_CHANCE = fraction(ModItems.SHOCK_PENDANT, "strikeChance", 0.25,
                    "The probability that the Shock Pendant strikes an attacking entity with lightning"),
            VAMPIRIC_GLOVE_ABSORPTION_RATIO = fraction(ModItems.VAMPIRIC_GLOVE, "absorptionRatio", 0.20,
                    "The proportion of melee damage dealt that is absorbed by the Vampiric Gloves");

    public static final Value.ConfigValue<Integer>
            CHORUS_TOTEM_HEALTH_RESTORED = nonNegativeInt(ModItems.CHORUS_TOTEM, "healthRestored", 10,
                    "The amount of health points that are restored after the Chorus Totem activates"),
            THORN_PENDANT_MAX_DAMAGE = nonNegativeInt(ModItems.THORN_PENDANT, "maxDamage", 6,
                    "The minimum amount of damage that is dealt when the Thorn Pendant activates"),
            THORN_PENDANT_MIN_DAMAGE = nonNegativeInt(ModItems.THORN_PENDANT, "minDamage", 2,
                    "The maximum amount of damage that is dealt when the Thorn Pendant activates");

    public static final Value.ConfigValue<Integer>
            ANTIDOTE_VESSEL_MAX_EFFECT_DURATION = duration(ModItems.ANTIDOTE_VESSEL, "maxEffectDuration", 5,
                    "The maximum duration in seconds negative mob effects can last when wearing the antidote vessel"),
            CHORUS_TOTEM_COOLDOWN = duration(ModItems.CHORUS_TOTEM, "cooldown", 0,
                    "The duration in seconds the Chorus Totem goes on cooldown for after activating"),
            CROSS_NECKLACE_COOLDOWN = duration(ModItems.CROSS_NECKLACE, "cooldown", 0,
                    "The duration in seconds the Cross Necklace goes on cooldown for after activating"),
            ETERNAL_STEAK_COOLDOWN = duration(ModItems.ETERNAL_STEAK, "cooldown", 15,
                    "The duration in seconds the Eternal Steak goes on cooldown for after being eaten"),
            EVERLASTING_BEEF_COOLDOWN = duration(ModItems.EVERLASTING_BEEF, "cooldown", 15,
                    "The duration in seconds the Everlasting Beef goes on cooldown for after being eaten"),
            FLAME_PENDANT_COOLDOWN = duration(ModItems.FLAME_PENDANT, "cooldown", 0,
                    "The duration in seconds the Flame Pendant goes on cooldown for after setting an entity on fire"),
            FLAME_PENDANT_FIRE_DURATION = duration(ModItems.FLAME_PENDANT, "fireDuration", 10,
                    "How long an attacking entity is set on fire for when the Flame Pendant activates"),
            HELIUM_FLAMINGO_FLIGHT_DURATION = duration(ModItems.HELIUM_FLAMINGO, "flightDuration", 8,
                    "The amount of time in seconds a player can fly with the Helium Flamingo before needing to recharge"),
            HELIUM_FLAMINGO_RECHARGE_DURATION = duration(ModItems.HELIUM_FLAMINGO, "rechargeDuration", 15,
                    "The amount of time in seconds it takes for the Helium Flamingo to recharge"),
            OBSIDIAN_SKULL_FIRE_RESISTANCE_COOLDOWN = duration(ModItems.OBSIDIAN_SKULL, "fireResistanceCooldown", 60,
                    "The amount of time in seconds the Obsidian Skull goes on cooldown for after taking fire damage"),
            OBSIDIAN_SKULL_FIRE_RESISTANCE_DURATION = duration(ModItems.OBSIDIAN_SKULL, "fireResistanceDuration", 30,
                    "The duration of the fire resistance effect that is applied when taking fire damage while wearing the Obsidian Skull"),
            ONION_RING_HASTE_DURATION_PER_FOOD_POINT = duration(ModItems.ONION_RING, "hasteDurationPerFoodPoint", 6,
                    "The duration of haste that is applied per food point eaten while wearing the Onion Ring"),
            PANIC_NECKLACE_COOLDOWN = duration(ModItems.PANIC_NECKLACE, "cooldown", 0,
                    "The duration in seconds the Panic Necklace goes on cooldown for after taking damage"),
            PANIC_NECKLACE_SPEED_DURATION = duration(ModItems.PANIC_NECKLACE, "speedDuration", 8,
                    "The duration in seconds of the speed effect that is applied when taking damage while wearing the Panic Necklace"),
            ROOTED_BOOTS_HUNGER_REPLENISHING_DURATION = duration(ModItems.ROOTED_BOOTS, "hungerReplenishingDuration", 10,
                    "The amount of time in seconds it takes to replenish a single point of hunger while wearing the rooted boots"),
            SHOCK_PENDANT_COOLDOWN = duration(ModItems.SHOCK_PENDANT, "cooldown", 0,
                    "The amount of time in seconds the Shock Pendant goes on cooldown for " +
                            "after striking an attacker with lightning"),
            SNORKEL_WATER_BREATHING_DURATION = duration(ModItems.SNORKEL, "waterBreathingDuration", 15,
                    "The duration of the water breathing effect that is applied by the Snorkel"),
            THORN_PENDANT_COOLDOWN = duration(ModItems.THORN_PENDANT, "cooldown", 0,
                    "The duration in seconds the thorn pendant goes on cooldown for after activating");

    public static final Value.ConfigValue<Integer>
            ANGLERS_HAT_LUCK_OF_THE_SEA_LEVEL_BONUS = enchantmentLevel(ModItems.ANGLERS_HAT, "luckOfTheSeaLevelBonus", 1,
                    "The amount of extra levels of luck of the sea that are granted by the Angler's Hat"),
            ANGLERS_HAT_LURE_LEVEL_BONUS = enchantmentLevel(ModItems.ANGLERS_HAT, "lureLevelBonus", 1,
                    "The amount of extra levels of lure that are granted by the Angler's Hat"),
            LUCKY_SCARF_FORTUNE_BONUS = nonNegativeInt(ModItems.LUCKY_SCARF, "fortuneBonus", 1,
                    "The amount of extra levels of fortune that are granted by the Lucky Scarf"),
            SUPERSTITIOUS_HAT_LOOTING_LEVEL_BONUS = nonNegativeInt(ModItems.SUPERSTITIOUS_HAT, "lootingLevelBonus", 1,
                    "The amount of extra levels of Looting that are granted by the Superstitious Hat");

    public static final Value.ConfigValue<Integer>
            ONION_RING_HASTE_LEVEL = mobEffectLevel(ModItems.ONION_RING, "hasteLevel", 2,
                    "The level of the haste effect that is applied by the Onion Ring"),
            PANIC_NECKLACE_SPEED_LEVEL = mobEffectLevel(ModItems.PANIC_NECKLACE, "speedLevel", 1,
                    "The level of the speed effect that is applied by the Panic Necklace");

    public static final Value.ConfigValue<UpgradeToolTierAbility.Tier>
            DIGGING_CLAWS_TOOL_TIER = register(ModItems.DIGGING_CLAWS, "toolTier", ValueTypes.TOOL_TIER, UpgradeToolTierAbility.Tier.STONE,
                    "The tool tier that the digging claws increase the wearer's mining level to");

    private static Value.ConfigValue<Boolean> booleanValue(Holder<? extends Item> holder, String name, String... tooltips) {
        return booleanValue(holder, name, true, tooltips);
    }

    private static Value.ConfigValue<Boolean> booleanValue(Holder<? extends Item> holder, String name, boolean defaultValue, String... tooltips) {
        return register(holder, name, ValueTypes.BOOLEAN, defaultValue, tooltips);
    }

    private static Value.ConfigValue<Double> nonNegativeDouble(Holder<? extends Item> holder, String name, double defaultValue, String... tooltips) {
        return register(holder, name, ValueTypes.NON_NEGATIVE_DOUBLE, defaultValue, tooltips);
    }

    private static Value.ConfigValue<Double> attributeModifier(Holder<? extends Item> holder, String name, double defaultValue, String... tooltips) {
        return register(holder, name, ValueTypes.ATTRIBUTE_MODIFIER_AMOUNT, defaultValue, tooltips);
    }

    private static Value.ConfigValue<Double> fraction(Holder<? extends Item> holder, String name, double defaultValue, String... tooltips) {
        return register(holder, name, ValueTypes.FRACTION, defaultValue, tooltips);
    }

    private static Value.ConfigValue<Integer> nonNegativeInt(Holder<? extends Item> holder, String name, int defaultValue, String... tooltips) {
        return register(holder, name, ValueTypes.NON_NEGATIVE_INT, defaultValue, tooltips);
    }

    private static Value.ConfigValue<Integer> duration(Holder<? extends Item> holder, String name, int defaultValue, String... tooltips) {
        return register(holder, name, ValueTypes.DURATION, defaultValue, tooltips);
    }

    private static Value.ConfigValue<Integer> enchantmentLevel(Holder<? extends Item> holder, String name, int defaultValue, String... tooltips) {
        return register(holder, name, ValueTypes.ENCHANTMENT_LEVEL, defaultValue, tooltips);
    }

    private static Value.ConfigValue<Integer> mobEffectLevel(Holder<? extends Item> holder, String name, int defaultValue, String... tooltips) {
        return register(holder, name, ValueTypes.MOB_EFFECT_LEVEL, defaultValue, tooltips);
    }

    private static <T> Value.ConfigValue<T> register(Holder<? extends Item> holder, String name, ValueType<T, ?> type, T defaultValue, String... tooltips) {
        String id = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, holder.unwrapKey().orElseThrow().location().getPath()) + '.' + name;
        if (!type.isCorrect(defaultValue)) {
            throw new IllegalArgumentException(type.makeError(defaultValue));
        }
        Value.ConfigValue<T> value = new Value.ConfigValue<>(type, id, defaultValue);
        getValues(type).put(id, value);
        TOOLTIPS.put(id, List.of(tooltips));
        if (holder.unwrapKey().isEmpty()) {
            throw new IllegalStateException();
        }
        ResourceLocation item = holder.unwrapKey().get().location();
        if (!ITEM_TO_KEYS.containsKey(item)) {
            ITEM_TO_KEYS.put(item, new ArrayList<>());
        }
        ITEM_TO_KEYS.get(item).add(id);
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

    public static List<String> getTooltips(String key) {
        return TOOLTIPS.get(key);
    }

    public static void save() {
        for (ValueMap<?> map : VALUES.values()) {
            map.saveValues();
        }
    }

    public static void loadFromConfig() {
        for (ValueMap<?> map : VALUES.values()) {
            map.loadValues();
        }
    }

    public static void loadFromConfigAndSend(MinecraftServer server) {
        for (ValueMap<?> map : VALUES.values()) {
            map.loadValuesAndSend(server);
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

        public void saveValues() {
            map.forEach((key, config) -> {
                if (!ItemConfigsManager.INSTANCE.get(config.type(), key).equals(config.get())) {
                    ItemConfigsManager.INSTANCE.set(config.type(), key, config.get());
                }
            });
        }

        public void loadValues() {
            map.forEach((key, config) -> config.set(ItemConfigsManager.INSTANCE.get(config.type(), key)));
        }

        public void loadValuesAndSend(MinecraftServer server) {
            map.forEach((key, config) -> {
                T value = ItemConfigsManager.INSTANCE.get(config.type(), key);
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
