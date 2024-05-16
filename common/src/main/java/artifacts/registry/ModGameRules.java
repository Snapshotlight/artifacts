package artifacts.registry;

import artifacts.Artifacts;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModGameRules {

    public static final BiMap<String, BooleanGameRule> BOOLEAN_VALUES = HashBiMap.create();
    public static final BiMap<String, IntegerGameRule> INTEGER_VALUES = HashBiMap.create();

    public static final List<BooleanGameRule> BOOLEAN_GAME_RULES = new ArrayList<>();
    public static final List<IntegerGameRule> INTEGER_GAME_RULES = new ArrayList<>();

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

            CHORUS_TOTEM_DO_CONSUME_ON_USE = booleanGameRule(ModItems.CHORUS_TOTEM, "doConsumeOnUse"),
            FLAME_PENDANT_DO_GRANT_FIRE_RESISTANCE = booleanGameRule(ModItems.FLAME_PENDANT, "doGrantFireResistance"),
            ROOTED_BOOTS_DO_GROW_PLANTS_AFTER_EATING = booleanGameRule(ModItems.ROOTED_BOOTS, "doGrowPlantsAfterEating"),
            SHOCK_PENDANT_DO_CANCEL_LIGHTNING_DAMAGE = booleanGameRule(ModItems.SHOCK_PENDANT, "doCancelLightningDamage"),
            SNORKEL_IS_INFINITE = booleanGameRule(ModItems.SNORKEL, "isInfinite", false),
            SNOWSHOES_ALLOW_WALKING_ON_POWDER_SNOW = booleanGameRule(ModItems.SNOWSHOES, "allowWalkingOnPowderSnow"),
            UMBRELLA_IS_SHIELD = booleanGameRule(ModItems.UMBRELLA, "isShield"),
            UMBRELLA_IS_GLIDER = booleanGameRule(ModItems.UMBRELLA, "isGlider");

    public static final IntegerGameRule
            ANGLERS_HAT_LUCK_OF_THE_SEA_LEVEL_BONUS = integerGameRule(ModItems.ANGLERS_HAT, "luckOfTheSeaLevelBonus", 1),
            ANGLERS_HAT_LURE_LEVEL_BONUS = integerGameRule(ModItems.ANGLERS_HAT, "lureLevelBonus", 1),
            ANTIDOTE_VESSEL_MAX_EFFECT_DURATION = integerGameRule(ModItems.ANTIDOTE_VESSEL, "maxEffectDuration", 5),
            BUNNY_HOPPERS_FALL_DAMAGE_MULTIPLIER = integerGameRule(ModItems.BUNNY_HOPPERS, "fallDamageMultiplier", 0),
            BUNNY_HOPPERS_JUMP_STRENGTH_BONUS = integerGameRule(ModItems.BUNNY_HOPPERS, "jumpStrengthBonus", 40),
            BUNNY_HOPPERS_SAFE_FALL_DISTANCE_BONUS = integerGameRule(ModItems.BUNNY_HOPPERS, "safeFallDistanceBonus", 10),
            CHARM_OF_SHRINKING_SCALE_MODIFIER = integerGameRule(ModItems.CHARM_OF_SHRINKING, "scaleModifier", -50),
            CHORUS_TOTEM_HEALTH_RESTORED = integerGameRule(ModItems.CHORUS_TOTEM, "healthRestored", 10),
            CHORUS_TOTEM_COOLDOWN = integerGameRule(ModItems.CHORUS_TOTEM, "cooldown", 0),
            CHORUS_TOTEM_TELEPORTATION_CHANCE = integerGameRule(ModItems.CHORUS_TOTEM, "teleportationChance", 100),
            CLOUD_IN_A_BOTTLE_SPRINT_JUMP_VERTICAL_VELOCITY = integerGameRule(ModItems.CLOUD_IN_A_BOTTLE, "sprintJumpVerticalVelocity", 25),
            CLOUD_IN_A_BOTTLE_SPRINT_JUMP_HORIZONTAL_VELOCITY = integerGameRule(ModItems.CLOUD_IN_A_BOTTLE, "sprintJumpHorizontalVelocity", 25),
            CLOUD_IN_A_BOTTLE_SAFE_FALL_DISTANCE_BONUS = integerGameRule(ModItems.CLOUD_IN_A_BOTTLE, "safeFallDistanceBonus", 3),
            COWBOY_HAT_MOUNT_SPEED_BONUS = integerGameRule(ModItems.COWBOY_HAT, "mountSpeedBonus", 40),
            CROSS_NECKLACE_BONUS_INVINCIBILITY_TICKS = integerGameRule(ModItems.CROSS_NECKLACE, "bonusInvincibilityTicks", 20),
            CROSS_NECKLACE_COOLDOWN = integerGameRule(ModItems.CROSS_NECKLACE, "cooldown", 0),
            CRYSTAL_HEART_HEALTH_BONUS = integerGameRule(ModItems.CRYSTAL_HEART, "healthBonus", 10),
            DIGGING_CLAWS_BLOCK_BREAK_SPEED_BONUS = integerGameRule(ModItems.DIGGING_CLAWS, "blockBreakSpeedBonus", 30),
            DIGGING_CLAWS_TOOL_TIER = integerGameRule(ModItems.DIGGING_CLAWS, "toolTier", 2),
            ETERNAL_STEAK_COOLDOWN = integerGameRule(ModItems.ETERNAL_STEAK, "cooldown", 15),
            EVERLASTING_BEEF_COOLDOWN = integerGameRule(ModItems.EVERLASTING_BEEF, "cooldown", 15),
            FERAL_CLAWS_ATTACK_SPEED_BONUS = integerGameRule(ModItems.FERAL_CLAWS, "attackSpeedBonus", 40),
            FIRE_GAUNTLET_FIRE_DURATION = integerGameRule(ModItems.FIRE_GAUNTLET, "fireDuration", 8),
            FLAME_PENDANT_COOLDOWN = integerGameRule(ModItems.FLAME_PENDANT, "cooldown", 0),
            FLAME_PENDANT_FIRE_DURATION = integerGameRule(ModItems.FLAME_PENDANT, "fireDuration", 10),
            FLAME_PENDANT_STRIKE_CHANCE = integerGameRule(ModItems.FLAME_PENDANT, "strikeChance", 40),
            FLIPPERS_SWIM_SPEED_BONUS = integerGameRule(ModItems.FLIPPERS, "swimSpeedBonus", 70),
            GOLDEN_HOOK_ENTITY_EXPERIENCE_BONUS = integerGameRule(ModItems.GOLDEN_HOOK, "entityExperienceBonus", 50),
            HELIUM_FLAMINGO_FLIGHT_DURATION = integerGameRule(ModItems.HELIUM_FLAMINGO, "flightDuration", 8),
            HELIUM_FLAMINGO_RECHARGE_DURATION = integerGameRule(ModItems.HELIUM_FLAMINGO, "rechargeDuration", 15),
            LUCKY_SCARF_FORTUNE_BONUS = integerGameRule(ModItems.LUCKY_SCARF, "fortuneBonus", 1),
            NIGHT_VISION_GOGGLES_STRENGTH = integerGameRule(ModItems.NIGHT_VISION_GOGGLES, "strength", 15),
            NOVELTY_DRINKING_HAT_DRINKING_SPEED_BONUS = integerGameRule(ModItems.NOVELTY_DRINKING_HAT, "drinkingSpeedBonus", 150),
            NOVELTY_DRINKING_HAT_EATING_SPEED_BONUS = integerGameRule(ModItems.NOVELTY_DRINKING_HAT, "eatingSpeedBonus", 50),
            OBSIDIAN_SKULL_FIRE_RESISTANCE_COOLDOWN = integerGameRule(ModItems.OBSIDIAN_SKULL, "fireResistanceCooldown", 60),
            OBSIDIAN_SKULL_FIRE_RESISTANCE_DURATION = integerGameRule(ModItems.OBSIDIAN_SKULL, "fireResistanceDuration", 30),
            ONION_RING_HASTE_DURATION_PER_FOOD_POINT = integerGameRule(ModItems.ONION_RING, "hasteDurationPerFoodPoint", 6),
            ONION_RING_HASTE_LEVEL = integerGameRule(ModItems.ONION_RING, "level", 2),
            PANIC_NECKLACE_COOLDOWN = integerGameRule(ModItems.PANIC_NECKLACE, "cooldown", 0),
            PANIC_NECKLACE_SPEED_DURATION = integerGameRule(ModItems.PANIC_NECKLACE, "speedDuration", 8),
            PANIC_NECKLACE_SPEED_LEVEL = integerGameRule(ModItems.PANIC_NECKLACE, "speedLevel", 1),
            PLASTIC_DRINKING_HAT_DRINKING_SPEED_BONUS = integerGameRule(ModItems.PLASTIC_DRINKING_HAT, "drinkingSpeedBonus", 150),
            PLASTIC_DRINKING_HAT_EATING_SPEED_BONUS = integerGameRule(ModItems.PLASTIC_DRINKING_HAT, "eatingSpeedBonus", 50),
            POCKET_PISTON_ATTACK_KNOCKBACK_BONUS = integerGameRule(ModItems.POCKET_PISTON, "attackKnockbackBonus", 75),
            POWER_GLOVE_ATTACK_DAMAGE_BONUS = integerGameRule(ModItems.POWER_GLOVE, "attackDamageBonus", 4),
            ROOTED_BOOTS_HUNGER_REPLENISHING_DURATION = integerGameRule(ModItems.ROOTED_BOOTS, "hungerReplenishingDuration", 10),
            RUNNING_SHOES_SPRINTING_SPEED_BONUS = integerGameRule(ModItems.RUNNING_SHOES, "sprintingSpeedBonus", 40),
            RUNNING_SHOES_SPRINTING_STEP_HEIGHT_BONUS = integerGameRule(ModItems.RUNNING_SHOES, "sprintingStepHeightBonus", 50),
            SHOCK_PENDANT_COOLDOWN = integerGameRule(ModItems.SHOCK_PENDANT, "cooldown", 0),
            SHOCK_PENDANT_STRIKE_CHANCE = integerGameRule(ModItems.SHOCK_PENDANT, "strikeChance", 25),
            SNORKEL_WATER_BREATHING_DURATION = integerGameRule(ModItems.SNORKEL, "waterBreathingDuration", 15),
            SNOWSHOES_MOVEMENT_SPEED_ON_SNOW_BONUS = integerGameRule(ModItems.SNOWSHOES, "movementSpeedOnSnowBonus", 50),
            STEADFAST_SPIKES_KNOCKBACK_RESISTANCE = integerGameRule(ModItems.STEADFAST_SPIKES, "knockbackResistance", 10),
            STEADFAST_SPIKES_SLIPPERINESS_REDUCTION = integerGameRule(ModItems.STEADFAST_SPIKES, "slipperinessReduction", 100),
            SUPERSTITIOUS_HAT_LOOTING_LEVEL_BONUS = integerGameRule(ModItems.SUPERSTITIOUS_HAT, "lootingLevelBonus", 1),
            THORN_PENDANT_COOLDOWN = integerGameRule(ModItems.THORN_PENDANT, "cooldown", 0),
            THORN_PENDANT_MAX_DAMAGE = integerGameRule(ModItems.THORN_PENDANT, "maxDamage", 6),
            THORN_PENDANT_MIN_DAMAGE = integerGameRule(ModItems.THORN_PENDANT, "minDamage", 2),
            THORN_PENDANT_STRIKE_CHANCE = integerGameRule(ModItems.THORN_PENDANT, "strikeChance", 50),
            VAMPIRIC_GLOVE_ABSORPTION_RATIO = integerGameRule(ModItems.VAMPIRIC_GLOVE, "absorptionRatio", 20),
            VAMPIRIC_GLOVE_MAX_HEALING_PER_HIT = integerGameRule(ModItems.VAMPIRIC_GLOVE, "maxHealingPerHit", 6),
            VILLAGER_HAT_REPUTATION_BONUS = integerGameRule(ModItems.VILLAGER_HAT, "reputationBonus", 75),
            WHOOPEE_CUSHION_FART_CHANCE = integerGameRule(ModItems.WHOOPEE_CUSHION, "fartChance", 12);

    private static String createName(RegistrySupplier<? extends Item> item, String name) {
        return String.format("%s.%s.%s",
                Artifacts.MOD_ID,
                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, item.unwrapKey().orElseThrow().location().getPath()),
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
        String name = createName(item, key);
        IntegerGameRule result = new IntegerGameRule(defaultValue);
        result.update(defaultValue);
        GameRules.Type<GameRules.IntegerValue> type = IntegerValueInvoker.invokeCreate(defaultValue, (server, value) -> {
            result.update(value.get());
            NetworkManager.sendToPlayers(server.getPlayerList().getPlayers(), new IntegerGameRuleChangedPacket(name, value.get()));
        });
        result.key = GameRules.register(name, GameRules.Category.PLAYER, type);

        INTEGER_VALUES.put(name, result);
        return result;
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

    public static class IntegerGameRule implements Supplier<Integer>, StringRepresentable {

        private int value;
        private GameRules.Key<GameRules.IntegerValue> key;

        private IntegerGameRule(int defaultValue) {
            this.value = defaultValue;
        }

        @Override
        public Integer get() {
            return value;
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

        public IntegerValue asDuration() {
            return asIntegerValue(20 * 60 * 60, 20);
        }

        public IntegerValue asMobEffectLevel() {
            return asIntegerValue(128, 1);
        }

        public IntegerValue asIntegerValue() {
            return asIntegerValue(Integer.MAX_VALUE, 1);
        }

        public IntegerValue asIntegerValue(int max, int multiplier) {
            return new IntegerValue.GameRuleValue(this, max, multiplier);
        }

        public DoubleValue asPercentage() {
            return asDoubleValue(100, 100);
        }

        public DoubleValue asDoubleValue(int max, double factor) {
            return asDoubleValue(0, max, factor);
        }

        public DoubleValue asDoubleValue(int min, int max, double factor) {
            return new DoubleValue.GameRuleValue(this, min, max, factor);
        }
    }
}
