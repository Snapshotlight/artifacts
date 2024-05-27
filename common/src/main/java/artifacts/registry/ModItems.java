package artifacts.registry;

import artifacts.Artifacts;
import artifacts.ability.*;
import artifacts.ability.mobeffect.*;
import artifacts.ability.retaliation.SetAttackersOnFireAbility;
import artifacts.ability.retaliation.StrikeAttackersWithLightningAbility;
import artifacts.ability.retaliation.ThornsAbility;
import artifacts.config.ItemConfigs;
import artifacts.config.value.Value;
import artifacts.item.EverlastingFoodItem;
import artifacts.item.UmbrellaItem;
import artifacts.item.WearableArtifactItem;
import artifacts.platform.PlatformServices;
import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Artifacts.MOD_ID, Registries.ITEM);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Artifacts.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> CREATIVE_TAB = RegistrySupplier.of(CREATIVE_MODE_TABS.register("main", () ->
            CreativeTabRegistry.create(
                    Component.translatable("%s.creative_tab".formatted(Artifacts.MOD_ID)),
                    () -> new ItemStack(ModItems.BUNNY_HOPPERS.get())
            )
    ));

    public static final RegistrySupplier<Item> MIMIC_SPAWN_EGG = register("mimic_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.MIMIC.supplier(), 0x805113, 0x212121, new Item.Properties().arch$tab(CREATIVE_TAB.supplier())));
    public static final RegistrySupplier<Item> UMBRELLA = register("umbrella", UmbrellaItem::new);
    public static final RegistrySupplier<Item> EVERLASTING_BEEF = register("everlasting_beef", () -> new EverlastingFoodItem(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build(), ItemConfigs.EVERLASTING_BEEF_COOLDOWN, ItemConfigs.EVERLASTING_BEEF_ENABLED));
    public static final RegistrySupplier<Item> ETERNAL_STEAK = register("eternal_steak", () -> new EverlastingFoodItem(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8F).build(), ItemConfigs.ETERNAL_STEAK_COOLDOWN, ItemConfigs.ETERNAL_STEAK_ENABLED));

    // head
    public static final RegistrySupplier<WearableArtifactItem> PLASTIC_DRINKING_HAT = wearableItem("plastic_drinking_hat", builder -> builder
            .equipSound(SoundEvents.BOTTLE_FILL)
            .addAttributeModifier(ModAttributes.DRINKING_SPEED, ItemConfigs.PLASTIC_DRINKING_HAT_DRINKING_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(ModAttributes.EATING_SPEED, ItemConfigs.PLASTIC_DRINKING_HAT_EATING_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );
    public static final RegistrySupplier<WearableArtifactItem> NOVELTY_DRINKING_HAT = wearableItem("novelty_drinking_hat", builder -> builder
            .equipSound(SoundEvents.BOTTLE_FILL)
            .addAbility(new CustomTooltipAbility(Component.translatable("artifacts.tooltip.item.novelty_drinking_hat")))
            .addAttributeModifier(ModAttributes.DRINKING_SPEED, ItemConfigs.NOVELTY_DRINKING_HAT_DRINKING_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(ModAttributes.EATING_SPEED, ItemConfigs.NOVELTY_DRINKING_HAT_EATING_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );
    public static final RegistrySupplier<WearableArtifactItem> SNORKEL = wearableItem("snorkel", builder -> builder
            .addAbility(new LimitedWaterBreathingAbility(ItemConfigs.SNORKEL_WATER_BREATHING_DURATION, ItemConfigs.SNORKEL_IS_INFINITE))
    );
    public static final RegistrySupplier<WearableArtifactItem> NIGHT_VISION_GOGGLES = wearableItem("night_vision_goggles", builder -> builder
            .addAbility(new NightVisionAbility(ItemConfigs.NIGHT_VISION_GOGGLES_STRENGTH))
    );
    public static final RegistrySupplier<WearableArtifactItem> VILLAGER_HAT = wearableItem("villager_hat", builder -> builder
            .addAttributeModifier(ModAttributes.VILLAGER_REPUTATION, ItemConfigs.VILLAGER_HAT_REPUTATION_BONUS, AttributeModifier.Operation.ADD_VALUE)
    );
    public static final RegistrySupplier<WearableArtifactItem> SUPERSTITIOUS_HAT = wearableItem("superstitious_hat", builder -> builder
            .increasesEnchantment(Enchantments.LOOTING, ItemConfigs.SUPERSTITIOUS_HAT_LOOTING_LEVEL_BONUS)
    );
    public static final RegistrySupplier<WearableArtifactItem> COWBOY_HAT = wearableItem("cowboy_hat", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_LEATHER)
            .addAttributeModifier(ModAttributes.MOUNT_SPEED, ItemConfigs.COWBOY_HAT_MOUNT_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );
    public static final RegistrySupplier<WearableArtifactItem> ANGLERS_HAT = wearableItem("anglers_hat", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_LEATHER)
            .increasesEnchantment(Enchantments.LUCK_OF_THE_SEA, ItemConfigs.ANGLERS_HAT_LUCK_OF_THE_SEA_LEVEL_BONUS)
            .increasesEnchantment(Enchantments.LURE, ItemConfigs.ANGLERS_HAT_LURE_LEVEL_BONUS)
    );

    // necklace
    public static final RegistrySupplier<WearableArtifactItem> LUCKY_SCARF = wearableItem("lucky_scarf", builder -> builder
            .increasesEnchantment(Enchantments.FORTUNE, ItemConfigs.LUCKY_SCARF_FORTUNE_BONUS)
    );
    public static final RegistrySupplier<WearableArtifactItem> SCARF_OF_INVISIBILITY = wearableItem("scarf_of_invisibility", builder -> builder
            .addAbility(new GenericMobEffectAbility(MobEffects.INVISIBILITY, Value.Constant.ONE, ItemConfigs.SCARF_OF_INVISIBILITY_ENABLED))
    );
    public static final RegistrySupplier<WearableArtifactItem> CROSS_NECKLACE = wearableItem("cross_necklace", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_DIAMOND)
            .addAbility(new ApplyCooldownAfterDamageAbility(ItemConfigs.CROSS_NECKLACE_COOLDOWN, Optional.empty()))
            .addAttributeModifier(ModAttributes.INVINCIBILITY_TICKS, ItemConfigs.CROSS_NECKLACE_BONUS_INVINCIBILITY_TICKS, AttributeModifier.Operation.ADD_VALUE, false)
    );
    public static final RegistrySupplier<WearableArtifactItem> PANIC_NECKLACE = wearableItem("panic_necklace", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_DIAMOND)
            .addAbility(new ApplyMobEffectAfterDamageAbility(
                    MobEffects.MOVEMENT_SPEED,
                    ItemConfigs.PANIC_NECKLACE_SPEED_LEVEL,
                    ItemConfigs.PANIC_NECKLACE_SPEED_DURATION,
                    Optional.empty()
            ))
            .addAbility(new ApplyCooldownAfterDamageAbility(
                    ItemConfigs.PANIC_NECKLACE_COOLDOWN,
                    Optional.empty()
            ))
    );
    public static final RegistrySupplier<WearableArtifactItem> SHOCK_PENDANT = wearableItem("shock_pendant", builder -> builder
            .addAbility(new StrikeAttackersWithLightningAbility(
                    ItemConfigs.SHOCK_PENDANT_STRIKE_CHANCE,
                    ItemConfigs.SHOCK_PENDANT_COOLDOWN
            ))
            .addAbility(new SimpleAbility(ModAbilities.LIGHTNING_IMMUNITY, ItemConfigs.SHOCK_PENDANT_DO_CANCEL_LIGHTNING_DAMAGE))
    );
    public static final RegistrySupplier<WearableArtifactItem> FLAME_PENDANT = wearableItem("flame_pendant", builder -> builder
            .addAbility(new SetAttackersOnFireAbility(
                    ItemConfigs.FLAME_PENDANT_STRIKE_CHANCE,
                    ItemConfigs.FLAME_PENDANT_COOLDOWN,
                    ItemConfigs.FLAME_PENDANT_FIRE_DURATION,
                    ItemConfigs.FLAME_PENDANT_DO_GRANT_FIRE_RESISTANCE
            ))
    );
    public static final RegistrySupplier<WearableArtifactItem> THORN_PENDANT = wearableItem("thorn_pendant", builder -> builder
            .addAbility(new ThornsAbility(
                    ItemConfigs.THORN_PENDANT_STRIKE_CHANCE,
                    ItemConfigs.THORN_PENDANT_COOLDOWN,
                    ItemConfigs.THORN_PENDANT_MIN_DAMAGE,
                    ItemConfigs.THORN_PENDANT_MAX_DAMAGE
            ))
    );
    public static final RegistrySupplier<WearableArtifactItem> CHARM_OF_SINKING = wearableItem("charm_of_sinking", builder -> builder
            .addAbility(new SimpleAbility(ModAbilities.SINKING, ItemConfigs.CHARM_OF_SINKING_ENABLED))
    );
    public static final RegistrySupplier<WearableArtifactItem> CHARM_OF_SHRINKING = wearableItem("charm_of_shrinking", builder -> builder
            .addAttributeModifier(Attributes.SCALE, ItemConfigs.CHARM_OF_SHRINKING_SCALE_MODIFIER, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

    // belt
    public static final RegistrySupplier<WearableArtifactItem> CLOUD_IN_A_BOTTLE = wearableItem("cloud_in_a_bottle", builder -> builder
            .equipSound(SoundEvents.BOTTLE_FILL_DRAGONBREATH)
            .addAbility(new DoubleJumpAbility(
                    ItemConfigs.CLOUD_IN_A_BOTTLE_ENABLED,
                    ItemConfigs.CLOUD_IN_A_BOTTLE_SPRINT_JUMP_HORIZONTAL_VELOCITY,
                    ItemConfigs.CLOUD_IN_A_BOTTLE_SPRINT_JUMP_VERTICAL_VELOCITY
            ))
            .addAttributeModifier(Attributes.SAFE_FALL_DISTANCE, ItemConfigs.CLOUD_IN_A_BOTTLE_SAFE_FALL_DISTANCE_BONUS, AttributeModifier.Operation.ADD_VALUE)
    );
    public static final RegistrySupplier<WearableArtifactItem> OBSIDIAN_SKULL = wearableItem("obsidian_skull", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_IRON)
            .addAbility(new ApplyMobEffectAfterDamageAbility(
                    MobEffects.FIRE_RESISTANCE,
                    Value.Constant.ONE,
                    ItemConfigs.OBSIDIAN_SKULL_FIRE_RESISTANCE_DURATION,
                    Optional.of(DamageTypeTags.IS_FIRE)
            ))
            .addAbility(new ApplyCooldownAfterDamageAbility(
                    ItemConfigs.OBSIDIAN_SKULL_FIRE_RESISTANCE_COOLDOWN,
                    Optional.of(DamageTypeTags.IS_FIRE)
            ))
    );
    public static final RegistrySupplier<WearableArtifactItem> ANTIDOTE_VESSEL = wearableItem("antidote_vessel", builder -> builder
            .equipSound(SoundEvents.BOTTLE_FILL)
            .addAbility(MakePiglinsNeutralAbility.INSTANCE)
            .addAbility(new RemoveBadEffectsAbility(
                    ItemConfigs.ANTIDOTE_VESSEL_ENABLED,
                    ItemConfigs.ANTIDOTE_VESSEL_MAX_EFFECT_DURATION
            ))
    );
    public static final RegistrySupplier<WearableArtifactItem> UNIVERSAL_ATTRACTOR = wearableItem("universal_attractor", builder -> builder
            .addAbility(MakePiglinsNeutralAbility.INSTANCE)
            .addAbility(new AttractItemsAbility(ItemConfigs.UNIVERSAL_ATTRACTOR_ENABLED))
    );
    public static final RegistrySupplier<WearableArtifactItem> CRYSTAL_HEART = wearableItem("crystal_heart", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_DIAMOND)
            .addAttributeModifier(Attributes.MAX_HEALTH, ItemConfigs.CRYSTAL_HEART_HEALTH_BONUS, AttributeModifier.Operation.ADD_VALUE)
    );
    public static final RegistrySupplier<WearableArtifactItem> HELIUM_FLAMINGO = wearableItem("helium_flamingo", builder -> builder
            .equipSound(ModSoundEvents.POP)
            .equipSoundPitch(0.7F)
            .addAbility(new SwimInAirAbility(
                    ItemConfigs.HELIUM_FLAMINGO_FLIGHT_DURATION,
                    ItemConfigs.HELIUM_FLAMINGO_RECHARGE_DURATION
            ))
    );
    public static final RegistrySupplier<WearableArtifactItem> CHORUS_TOTEM = wearableItem("chorus_totem", builder -> builder
            .addAbility(new TeleportOnDeathAbility(
                    ItemConfigs.CHORUS_TOTEM_TELEPORTATION_CHANCE,
                    ItemConfigs.CHORUS_TOTEM_HEALTH_RESTORED,
                    ItemConfigs.CHORUS_TOTEM_COOLDOWN,
                    ItemConfigs.CHORUS_TOTEM_DO_CONSUME_ON_USE
            ))
    );

    // hands
    public static final RegistrySupplier<WearableArtifactItem> DIGGING_CLAWS = wearableItem("digging_claws", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_NETHERITE)
            .addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, ItemConfigs.DIGGING_CLAWS_BLOCK_BREAK_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAbility(new UpgradeToolTierAbility(ItemConfigs.DIGGING_CLAWS_TOOL_TIER))
    );
    public static final RegistrySupplier<WearableArtifactItem> FERAL_CLAWS = wearableItem("feral_claws", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_NETHERITE)
            .addAttributeModifier(Attributes.ATTACK_SPEED, ItemConfigs.FERAL_CLAWS_ATTACK_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );
    public static final RegistrySupplier<WearableArtifactItem> POWER_GLOVE = wearableItem("power_glove", builder -> builder
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, ItemConfigs.POWER_GLOVE_ATTACK_DAMAGE_BONUS, AttributeModifier.Operation.ADD_VALUE)
    );
    public static final RegistrySupplier<WearableArtifactItem> FIRE_GAUNTLET = wearableItem("fire_gauntlet", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_IRON)
            .addAttributeModifier(ModAttributes.ATTACK_BURNING_DURATION, ItemConfigs.FIRE_GAUNTLET_FIRE_DURATION, AttributeModifier.Operation.ADD_VALUE)
    );
    public static final RegistrySupplier<WearableArtifactItem> POCKET_PISTON = wearableItem("pocket_piston", builder -> builder
            .equipSound(SoundEvents.PISTON_EXTEND)
            .addAttributeModifier(Attributes.ATTACK_KNOCKBACK, ItemConfigs.POCKET_PISTON_ATTACK_KNOCKBACK_BONUS, AttributeModifier.Operation.ADD_VALUE)
    );
    public static final RegistrySupplier<WearableArtifactItem> VAMPIRIC_GLOVE = wearableItem("vampiric_glove", builder -> builder
            .addAttributeModifier(ModAttributes.ATTACK_DAMAGE_ABSORPTION, ItemConfigs.VAMPIRIC_GLOVE_ABSORPTION_RATIO, AttributeModifier.Operation.ADD_VALUE)
            .addAttributeModifier(ModAttributes.MAX_ATTACK_DAMAGE_ABSORBED, ItemConfigs.VAMPIRIC_GLOVE_MAX_HEALING_PER_HIT, AttributeModifier.Operation.ADD_VALUE)
    );
    public static final RegistrySupplier<WearableArtifactItem> GOLDEN_HOOK = wearableItem("golden_hook", builder -> builder
            .addAttributeModifier(ModAttributes.ENTITY_EXPERIENCE, ItemConfigs.GOLDEN_HOOK_ENTITY_EXPERIENCE_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAbility(MakePiglinsNeutralAbility.INSTANCE)
    );
    public static final RegistrySupplier<WearableArtifactItem> ONION_RING = wearableItem("onion_ring", builder -> builder
            .properties(properties -> properties.food(new FoodProperties.Builder().nutrition(2).build()))
            .addAbility(new ApplyMobEffectAfterEatingAbility(
                    MobEffects.DIG_SPEED,
                    ItemConfigs.ONION_RING_HASTE_DURATION_PER_FOOD_POINT,
                    ItemConfigs.ONION_RING_HASTE_LEVEL
            ))
    );
    public static final RegistrySupplier<WearableArtifactItem> PICKAXE_HEATER = wearableItem("pickaxe_heater", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_IRON)
            .addAbility(new SimpleAbility(ModAbilities.SMELT_ORES, ItemConfigs.PICKAXE_HEATER_ENABLED))
    );

    // feet
    public static final RegistrySupplier<WearableArtifactItem> AQUA_DASHERS = wearableItem("aqua_dashers", builder -> builder
            .addAbility(new SimpleAbility(ModAbilities.SPRINT_ON_FLUIDS, ItemConfigs.AQUA_DASHERS_ENABLED))
    );
    public static final RegistrySupplier<WearableArtifactItem> BUNNY_HOPPERS = wearableItem("bunny_hoppers", builder -> builder
            .addAttributeModifier(Attributes.JUMP_STRENGTH, ItemConfigs.BUNNY_HOPPERS_JUMP_STRENGTH_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(Attributes.FALL_DAMAGE_MULTIPLIER, ItemConfigs.BUNNY_HOPPERS_FALL_DAMAGE_MULTIPLIER, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(Attributes.SAFE_FALL_DISTANCE, ItemConfigs.BUNNY_HOPPERS_SAFE_FALL_DISTANCE_BONUS, AttributeModifier.Operation.ADD_VALUE)
            .addAbility(new HurtSoundAbility(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.RABBIT_HURT)))
    );
    public static final RegistrySupplier<WearableArtifactItem> KITTY_SLIPPERS = wearableItem("kitty_slippers", builder -> builder
            .equipSound(SoundEvents.CAT_AMBIENT)
            .addAbility(new SimpleAbility(ModAbilities.SCARE_CREEPERS, ItemConfigs.KITTY_SLIPPERS_ENABLED))
            .addAbility(new HurtSoundAbility(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.CAT_HURT)))
    );
    public static final RegistrySupplier<WearableArtifactItem> RUNNING_SHOES = wearableItem("running_shoes", builder -> builder
            .addAttributeModifier(ModAttributes.SPRINTING_SPEED, ItemConfigs.RUNNING_SHOES_SPRINTING_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(ModAttributes.SPRINTING_STEP_HEIGHT, ItemConfigs.RUNNING_SHOES_SPRINTING_STEP_HEIGHT_BONUS, AttributeModifier.Operation.ADD_VALUE)
    );
    public static final RegistrySupplier<WearableArtifactItem> SNOWSHOES = wearableItem("snowshoes", builder -> builder
            .addAbility(new SimpleAbility(ModAbilities.WALK_ON_POWDER_SNOW, ItemConfigs.SNOWSHOES_ALLOW_WALKING_ON_POWDER_SNOW))
            .addAttributeModifier(ModAttributes.MOVEMENT_SPEED_ON_SNOW, ItemConfigs.SNOWSHOES_MOVEMENT_SPEED_ON_SNOW_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );
    public static final RegistrySupplier<WearableArtifactItem> STEADFAST_SPIKES = wearableItem("steadfast_spikes", builder -> builder
            .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, ItemConfigs.STEADFAST_SPIKES_KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADD_VALUE)
            .addAttributeModifier(ModAttributes.SLIP_RESISTANCE, ItemConfigs.STEADFAST_SPIKES_SLIPPERINESS_REDUCTION, AttributeModifier.Operation.ADD_VALUE)
    );
    public static final RegistrySupplier<WearableArtifactItem> FLIPPERS = wearableItem("flippers", builder -> builder
            .addAttributeModifier(ModAttributes.SWIM_SPEED, ItemConfigs.FLIPPERS_SWIM_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );
    public static final RegistrySupplier<WearableArtifactItem> ROOTED_BOOTS = wearableItem("rooted_boots", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_LEATHER)
            .addAbility(new ReplenishHungerOnGrassAbility(
                    ItemConfigs.ROOTED_BOOTS_ENABLED,
                    ItemConfigs.ROOTED_BOOTS_HUNGER_REPLENISHING_DURATION
            ))
            .addAbility(new GrowPlantsAfterEatingAbility(ItemConfigs.ROOTED_BOOTS_DO_GROW_PLANTS_AFTER_EATING))
    );

    // curio
    public static final RegistrySupplier<WearableArtifactItem> WHOOPEE_CUSHION = wearableItem("whoopee_cushion", builder -> builder
            .equipSound(ModSoundEvents.FART)
            .addAttributeModifier(ModAttributes.FLATULENCE, ItemConfigs.WHOOPEE_CUSHION_FART_CHANCE, AttributeModifier.Operation.ADD_VALUE)
    );

    private static RegistrySupplier<WearableArtifactItem> wearableItem(String name, Consumer<WearableArtifactItem.Builder> consumer) {
        return register(name, () -> {
            var builder = new WearableArtifactItem.Builder(name);
            consumer.accept(builder);
            PlatformServices.platformHelper.processWearableArtifactBuilder(builder);
            return builder.build();
        });
    }

    private static <T extends Item> RegistrySupplier<T> register(String name, Supplier<T> supplier) {
        return RegistrySupplier.of(ITEMS.register(name, supplier));
    }
}
