package artifacts.registry;

import artifacts.Artifacts;
import artifacts.ability.UpgradeToolTierAbility;
import artifacts.ability.value.BooleanValue;
import artifacts.ability.value.DoubleValue;
import artifacts.ability.value.IntegerValue;
import artifacts.mixin.gamerule.BooleanValueInvoker;
import artifacts.mixin.gamerule.IntegerValueInvoker;
import artifacts.network.BooleanGameRuleChangedPacket;
import artifacts.network.IntegerGameRuleChangedPacket;
import com.google.common.base.CaseFormat;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dev.architectury.networking.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;

import java.util.function.Supplier;

public class ModGameRules {

    public static final BiMap<String, BooleanGameRule> BOOLEAN_VALUES = HashBiMap.create();
    public static final BiMap<String, IntegerGameRule> INTEGER_VALUES = HashBiMap.create();
    public static final BiMap<String, DoubleGameRule> DOUBLE_VALUES = HashBiMap.create();

    public static final BooleanGameRule
            ANTIDOTE_VESSEL_ENABLED = booleanGameRule(ModItems.ANTIDOTE_VESSEL, "enabled"),
            AQUA_DASHERS_ENABLED = booleanGameRule(ModItems.AQUA_DASHERS, "enabled"),
            CHARM_OF_SINKING_ENABLED = booleanGameRule(ModItems.CHARM_OF_SINKING, "enabled"),
            CLOUD_IN_A_BOTTLE_ENABLED = booleanGameRule(ModItems.CLOUD_IN_A_BOTTLE, "enabled"),
            ETERNAL_STEAK_ENABLED = booleanGameRule(ModItems.ETERNAL_STEAK, "enabled"),
            EVERLASTING_BEEF_ENABLED = booleanGameRule(ModItems.EVERLASTING_BEEF, "enabled"),
            KITTY_SLIPPERS_ENABLED = booleanGameRule(ModItems.KITTY_SLIPPERS, "enabled"),
            PICKAXE_HEATER_ENABLED = booleanGameRule(ModItems.PICKAXE_HEATER, "enabled"),
            ROOTED_BOOTS_ENABLED = booleanGameRule(ModItems.ROOTED_BOOTS, "enabled"),
            SCARF_OF_INVISIBILITY_ENABLED = booleanGameRule(ModItems.SCARF_OF_INVISIBILITY, "enabled"),
            UNIVERSAL_ATTRACTOR_ENABLED = booleanGameRule(ModItems.UNIVERSAL_ATTRACTOR, "enabled"),

            BUNNY_HOPPERS_DO_CANCEL_FALL_DAMAGE = booleanGameRule(ModItems.BUNNY_HOPPERS, "doCancelFallDamage"),
            CHORUS_TOTEM_DO_CONSUME_ON_USE = booleanGameRule(ModItems.CHORUS_TOTEM, "doConsumeOnUse"),
            FLAME_PENDANT_DO_GRANT_FIRE_RESISTANCE = booleanGameRule(ModItems.FLAME_PENDANT, "doGrantFireResistance"),
            ROOTED_BOOTS_DO_GROW_PLANTS_AFTER_EATING = booleanGameRule(ModItems.ROOTED_BOOTS, "doGrowPlantsAfterEating"),
            RUNNING_SHOES_DO_INCREASE_STEP_HEIGHT = booleanGameRule(ModItems.RUNNING_SHOES, "doIncreaseStepHeight"),
            SHOCK_PENDANT_DO_CANCEL_LIGHTNING_DAMAGE = booleanGameRule(ModItems.SHOCK_PENDANT, "doCancelLightningDamage"),
            SNORKEL_IS_INFINITE = booleanGameRule(ModItems.SNORKEL, "isInfinite", false),
            SNOWSHOES_ALLOW_WALKING_ON_POWDER_SNOW = booleanGameRule(ModItems.SNOWSHOES, "allowWalkingOnPowderSnow"),
            UMBRELLA_IS_SHIELD = booleanGameRule(ModItems.UMBRELLA, "isShield"),
            UMBRELLA_IS_GLIDER = booleanGameRule(ModItems.UMBRELLA, "isGlider");

    public static final IntegerGameRule
            CROSS_NECKLACE_BONUS_INVINCIBILITY_TICKS = integerGameRule(ModItems.CROSS_NECKLACE, "bonusInvincibilityTicks", 20, 60 * 20),
            CHORUS_TOTEM_HEALTH_RESTORED = integerGameRule(ModItems.CHORUS_TOTEM, "healthRestored", 10),
            DIGGING_CLAWS_TOOL_TIER = integerGameRule(ModItems.DIGGING_CLAWS, "toolTier", 2, 5),
            THORN_PENDANT_MAX_DAMAGE = integerGameRule(ModItems.THORN_PENDANT, "maxDamage", 6),
            THORN_PENDANT_MIN_DAMAGE = integerGameRule(ModItems.THORN_PENDANT, "minDamage", 2),
            VAMPIRIC_GLOVE_MAX_HEALING_PER_HIT = integerGameRule(ModItems.VAMPIRIC_GLOVE, "maxHealingPerHit", 6),
            VILLAGER_HAT_REPUTATION_BONUS = integerGameRule(ModItems.VILLAGER_HAT, "reputationBonus", 100),

            ANGLERS_HAT_LUCK_OF_THE_SEA_LEVEL_BONUS = enchantmentBonus(ModItems.ANGLERS_HAT, "luckOfTheSeaLevelBonus"),
            ANGLERS_HAT_LURE_LEVEL_BONUS = enchantmentBonus(ModItems.ANGLERS_HAT, "lureLevelBonus"),
            LUCKY_SCARF_FORTUNE_BONUS = enchantmentBonus(ModItems.LUCKY_SCARF, "fortuneBonus"),
            SUPERSTITIOUS_HAT_LOOTING_LEVEL_BONUS = enchantmentBonus(ModItems.SUPERSTITIOUS_HAT, "lootingLevelBonus"),

            ANTIDOTE_VESSEL_MAX_EFFECT_DURATION = durationSeconds(ModItems.ANTIDOTE_VESSEL, "maxEffectDuration", 5),
            CHORUS_TOTEM_COOLDOWN = durationSeconds(ModItems.CHORUS_TOTEM, "cooldown", 0),
            CROSS_NECKLACE_COOLDOWN = durationSeconds(ModItems.CROSS_NECKLACE, "cooldown", 0),
            ETERNAL_STEAK_COOLDOWN = durationSeconds(ModItems.ETERNAL_STEAK, "cooldown", 15),
            EVERLASTING_BEEF_COOLDOWN = durationSeconds(ModItems.EVERLASTING_BEEF, "cooldown", 15),
            FIRE_GAUNTLET_FIRE_DURATION = durationSeconds(ModItems.FIRE_GAUNTLET, "fireDuration", 8),
            FLAME_PENDANT_COOLDOWN = durationSeconds(ModItems.FLAME_PENDANT, "cooldown", 0),
            FLAME_PENDANT_FIRE_DURATION = durationSeconds(ModItems.FLAME_PENDANT, "fireDuration", 10),
            HELIUM_FLAMINGO_FLIGHT_DURATION = durationSeconds(ModItems.HELIUM_FLAMINGO, "flightDuration", 8),
            HELIUM_FLAMINGO_RECHARGE_DURATION = durationSeconds(ModItems.HELIUM_FLAMINGO, "rechargeDuration", 15),
            OBSIDIAN_SKULL_FIRE_RESISTANCE_COOLDOWN = durationSeconds(ModItems.OBSIDIAN_SKULL, "fireResistanceCooldown", 60),
            OBSIDIAN_SKULL_FIRE_RESISTANCE_DURATION = durationSeconds(ModItems.OBSIDIAN_SKULL, "fireResistanceDuration", 30),
            ONION_RING_HASTE_DURATION_PER_FOOD_POINT = durationSeconds(ModItems.ONION_RING, "hasteDurationPerFoodPoint", 6),
            PANIC_NECKLACE_COOLDOWN = durationSeconds(ModItems.PANIC_NECKLACE, "cooldown", 0),
            PANIC_NECKLACE_SPEED_DURATION = durationSeconds(ModItems.PANIC_NECKLACE, "speedDuration", 8),
            ROOTED_BOOTS_HUNGER_REPLENISHING_DURATION = durationSeconds(ModItems.ROOTED_BOOTS, "hungerReplenishingDuration", 15),
            SHOCK_PENDANT_COOLDOWN = durationSeconds(ModItems.SHOCK_PENDANT, "cooldown", 0),
            SNORKEL_WATER_BREATHING_DURATION = durationSeconds(ModItems.SNORKEL, "waterBreathingDuration", 15),
            THORN_PENDANT_COOLDOWN = durationSeconds(ModItems.THORN_PENDANT, "cooldown", 0),

            BUNNY_HOPPERS_JUMP_BOOST_LEVEL = mobEffectLevel(ModItems.BUNNY_HOPPERS, "jumpBoostLevel", 2),
            COWBOY_HAT_SPEED_LEVEL = mobEffectLevel(ModItems.COWBOY_HAT, "speedLevel", 2),
            ONION_RING_HASTE_LEVEL = mobEffectLevel(ModItems.ONION_RING, "hasteLevel", 2),
            PANIC_NECKLACE_SPEED_LEVEL = mobEffectLevel(ModItems.PANIC_NECKLACE, "speedLevel", 1);

    public static final DoubleGameRule
            CHORUS_TOTEM_TELEPORTATION_CHANCE = percentage(ModItems.CHORUS_TOTEM, "teleportationChance", 100),
            CLOUD_IN_A_BOTTLE_SPRINT_JUMP_VERTICAL_VELOCITY = doubleGameRule(ModItems.CLOUD_IN_A_BOTTLE, "sprintJumpVerticalVelocity", 50, 100 * 100, 100),
            CLOUD_IN_A_BOTTLE_SPRINT_JUMP_HORIZONTAL_VELOCITY = doubleGameRule(ModItems.CLOUD_IN_A_BOTTLE, "sprintJumpHorizontalVelocity", 50, 100 * 100, 100),
            CRYSTAL_HEART_HEALTH_BONUS = doubleGameRule(ModItems.CRYSTAL_HEART, "healthBonus", 10, 100, 1),
            DIGGING_CLAWS_DIG_SPEED_BONUS = doubleGameRule(ModItems.DIGGING_CLAWS, "digSpeedBonus", 32, 10),
            FERAL_CLAWS_ATTACK_SPEED_BONUS = percentage(ModItems.FERAL_CLAWS, "attackSpeedBonus", 40),
            FLAME_PENDANT_STRIKE_CHANCE = percentage(ModItems.FLAME_PENDANT, "strikeChance", 40),
            FLIPPERS_SWIM_SPEED_BONUS = doubleGameRule(ModItems.FLIPPERS, "swimSpeedBonus", 100, 100 * 100, 100),
            GOLDEN_HOOK_EXPERIENCE_BONUS = doubleGameRule(ModItems.GOLDEN_HOOK, "experienceBonus", 75, 10 * 100, 100),
            NIGHT_VISION_GOGGLES_STRENGTH = percentage(ModItems.NIGHT_VISION_GOGGLES, "strength", 25),
            NOVELTY_DRINKING_HAT_DRINKING_DURATION_MULTIPLIER = percentage(ModItems.NOVELTY_DRINKING_HAT, "drinkingDurationMultiplier", 30),
            NOVELTY_DRINKING_HAT_EATING_DURATION_MULTIPLIER = percentage(ModItems.NOVELTY_DRINKING_HAT, "eatingDurationMultiplier", 60),
            PLASTIC_DRINKING_HAT_DRINKING_DURATION_MULTIPLIER = percentage(ModItems.PLASTIC_DRINKING_HAT, "drinkingDurationMultiplier", 30),
            PLASTIC_DRINKING_HAT_EATING_DURATION_MULTIPLIER = percentage(ModItems.PLASTIC_DRINKING_HAT, "eatingDurationMultiplier", 60),
            POCKET_PISTON_KNOCKBACK_STRENGTH = doubleGameRule(ModItems.POCKET_PISTON, "knockbackStrength", 15, 10),
            POWER_GLOVE_ATTACK_DAMAGE_BONUS = doubleGameRule(ModItems.POWER_GLOVE, "attackDamageBonus", 4, 1),
            RUNNING_SHOES_SPEED_BONUS = doubleGameRule(ModItems.RUNNING_SHOES, "speedBonus", 40, 100 * 100, 100),
            SHOCK_PENDANT_STRIKE_CHANCE = percentage(ModItems.SHOCK_PENDANT, "strikeChance", 25),
            STEADFAST_SPIKES_KNOCKBACK_RESISTANCE = doubleGameRule(ModItems.STEADFAST_SPIKES, "knockbackResistance", 10, 10, 10),
            SNOWSHOES_SLIPPERINESS_REDUCTION = percentage(ModItems.SNOWSHOES, "slipperinessReduction", 100),
            THORN_PENDANT_STRIKE_CHANCE = percentage(ModItems.THORN_PENDANT, "strikeChance", 50),
            VAMPIRIC_GLOVE_ABSORPTION_CHANCE = percentage(ModItems.VAMPIRIC_GLOVE, "absorptionChance", 100),
            VAMPIRIC_GLOVE_ABSORPTION_RATIO = doubleGameRule(ModItems.VAMPIRIC_GLOVE, "absorptionRatio", 20, 100),
            WHOOPEE_CUSHION_FART_CHANCE = percentage(ModItems.WHOOPEE_CUSHION, "fartChance", 12);

    private static String createName(RegistrySupplier<? extends Item> item, String name) {
        return String.format("%s.%s.%s",
                Artifacts.MOD_ID,
                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, item.getId().getPath()),
                name
        );
    }

    private static BooleanGameRule booleanGameRule(RegistrySupplier<? extends Item> item, String key) {
        return booleanGameRule(item, key, true);
    }

    private static BooleanGameRule booleanGameRule(RegistrySupplier<? extends Item> item, String key, boolean defaultValue) {
        String name = createName(item, key);
        BooleanGameRule result = new BooleanGameRule();
        result.update(defaultValue);
        GameRules.Type<GameRules.BooleanValue> type = BooleanValueInvoker.invokeCreate(defaultValue, (server, value) -> {
            result.update(value.get());
            NetworkManager.sendToPlayers(server.getPlayerList().getPlayers(), new BooleanGameRuleChangedPacket(name, value.get()));
        });
        result.key = GameRules.register(name, GameRules.Category.PLAYER, type);
        BOOLEAN_VALUES.put(name, result);
        return result;
    }

    private static IntegerGameRule integerGameRule(RegistrySupplier<? extends Item> item, String key, int defaultValue) {
        return integerGameRule(item, key, defaultValue, Integer.MAX_VALUE);
    }

    private static IntegerGameRule integerGameRule(RegistrySupplier<? extends Item> item, String key, int defaultValue, int maxValue) {
        return integerGameRule(item, key, defaultValue, maxValue, 1);
    }

    private static IntegerGameRule integerGameRule(RegistrySupplier<? extends Item> item, String key, int defaultValue, int maxValue, int multiplier) {
        String name = createName(item, key);
        IntegerGameRule result = new IntegerGameRule(defaultValue, maxValue, multiplier);
        result.update(defaultValue);
        GameRules.Type<GameRules.IntegerValue> type = IntegerValueInvoker.invokeCreate(defaultValue, (server, value) -> {
            result.update(value.get());
            NetworkManager.sendToPlayers(server.getPlayerList().getPlayers(), new IntegerGameRuleChangedPacket(name, value.get()));
        });
        result.key = GameRules.register(name, GameRules.Category.PLAYER, type);

        INTEGER_VALUES.put(name, result);
        return result;
    }

    private static IntegerGameRule durationSeconds(RegistrySupplier<? extends Item> item, String key, int defaultValue) {
        return integerGameRule(item, key, defaultValue, 20 * 60 * 60, 20);
    }

    private static IntegerGameRule mobEffectLevel(RegistrySupplier<? extends Item> item, String key, int defaultValue) {
        return integerGameRule(item, key, defaultValue, 128);
    }

    private static IntegerGameRule enchantmentBonus(RegistrySupplier<? extends Item> item, String key) {
        return integerGameRule(item, key, 1, 100);
    }

    private static DoubleGameRule doubleGameRule(RegistrySupplier<? extends Item> item, String key, int defaultValue, int maxValue, double factor) {
        DoubleGameRule gameRule = new DoubleGameRule(integerGameRule(item, key, defaultValue, maxValue), factor);
        DOUBLE_VALUES.put(createName(item, key), gameRule);
        return gameRule;
    }

    private static DoubleGameRule doubleGameRule(RegistrySupplier<? extends Item> item, String key, int defaultValue, int factor) {
        return doubleGameRule(item, key, defaultValue, Integer.MAX_VALUE, factor);
    }

    private static DoubleGameRule percentage(RegistrySupplier<? extends Item> item, String key, int defaultValue) {
        return doubleGameRule(item, key, defaultValue, 100, 100);
    }

    public static void updateValue(String name, boolean value) {
        BOOLEAN_VALUES.get(name).update(value);
    }

    public static void updateValue(String name, int value) {
        INTEGER_VALUES.get(name).update(value);
    }

    public static void onPlayerJoinLevel(ServerPlayer player) {
        BOOLEAN_VALUES.forEach((key, value) -> NetworkManager.sendToPlayer(player, new BooleanGameRuleChangedPacket(key, value.value)));
        INTEGER_VALUES.forEach((key, value) -> NetworkManager.sendToPlayer(player, new IntegerGameRuleChangedPacket(key, value.value)));
    }

    public static void onServerStarted(MinecraftServer server) {
        BOOLEAN_VALUES.values().forEach(value -> value.update(server));
        INTEGER_VALUES.values().forEach(value -> value.update(server));
    }

    public static class BooleanGameRule implements Supplier<Boolean>, BooleanValue, StringRepresentable {

        private Boolean value = true;
        private GameRules.Key<GameRules.BooleanValue> key;

        @Override
        public Boolean get() {
            return value;
        }

        public GameRules.Key<GameRules.BooleanValue> getKey() {
            return key;
        }

        private void update(MinecraftServer server) {
            update(server.getGameRules().getBoolean(key));
        }

        private void update(boolean value) {
            this.value = value;
        }

        @Override
        public String getSerializedName() {
            return key.getId();
        }
    }

    public static class IntegerGameRule implements Supplier<Integer>, IntegerValue, StringRepresentable {

        private final int max;
        private final int multiplier;
        private int value;
        private GameRules.Key<GameRules.IntegerValue> key;

        private IntegerGameRule(int defaultValue, int max, int multiplier) {
            this.value = defaultValue;
            this.max = max;
            this.multiplier = multiplier;
        }

        @Override
        public Integer get() {
            return Math.min(max, Math.max(0, value)) * multiplier;
        }

        public int max() {
            return max;
        }

        public int multiplier() {
            return multiplier;
        }

        public GameRules.Key<GameRules.IntegerValue> getKey() {
            return key;
        }

        private void update(MinecraftServer server) {
            update(server.getGameRules().getInt(key));
        }

        private void update(int value) {
            this.value = value;
        }

        @Override
        public String getSerializedName() {
            return key.getId();
        }
    }

    public record DoubleGameRule(IntegerGameRule integerGameRule, double factor) implements Supplier<Double>, DoubleValue, StringRepresentable {

        @Override
        public Double get() {
            return integerGameRule.get() / factor;
        }

        @Override
        public String getSerializedName() {
            return integerGameRule().getSerializedName();
        }
    }
}
