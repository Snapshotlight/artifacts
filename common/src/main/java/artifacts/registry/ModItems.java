package artifacts.registry;

import artifacts.Artifacts;
import artifacts.ability.*;
import artifacts.ability.mobeffect.InvisibilityAbility;
import artifacts.ability.mobeffect.LimitedWaterBreathingAbility;
import artifacts.ability.mobeffect.MountSpeedAbility;
import artifacts.ability.mobeffect.NightVisionAbility;
import artifacts.ability.retaliation.SetAttackersOnFireAbility;
import artifacts.ability.retaliation.StrikeAttackersWithLightningAbility;
import artifacts.ability.retaliation.ThornsAbility;
import artifacts.item.EverlastingFoodItem;
import artifacts.item.UmbrellaItem;
import artifacts.item.WearableArtifactItem;
import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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

    public static RegistrySupplier<Item> MIMIC_SPAWN_EGG = register("mimic_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.MIMIC.supplier(), 0x805113, 0x212121, new Item.Properties().arch$tab(CREATIVE_TAB.supplier())));
    public static RegistrySupplier<Item> UMBRELLA = register("umbrella", UmbrellaItem::new);
    public static RegistrySupplier<Item> EVERLASTING_BEEF = register("everlasting_beef", () -> new EverlastingFoodItem(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build(), ModGameRules.EVERLASTING_BEEF_COOLDOWN, ModGameRules.EVERLASTING_BEEF_ENABLED));
    public static RegistrySupplier<Item> ETERNAL_STEAK = register("eternal_steak", () -> new EverlastingFoodItem(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8F).build(), ModGameRules.ETERNAL_STEAK_COOLDOWN, ModGameRules.ETERNAL_STEAK_ENABLED));

    // head
    public static RegistrySupplier<WearableArtifactItem> PLASTIC_DRINKING_HAT = register("plastic_drinking_hat", () -> new WearableArtifactItem(
            SoundEvents.BOTTLE_FILL,
            AttributeModifierAbility.create(ModAttributes.DRINKING_SPEED, ModGameRules.PLASTIC_DRINKING_HAT_DRINKING_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, Artifacts.id("plastic_drinking_hat/drinking_speed_bonus").toString()),
            AttributeModifierAbility.create(ModAttributes.EATING_SPEED, ModGameRules.PLASTIC_DRINKING_HAT_EATING_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, Artifacts.id("plastic_drinking_hat/eating_speed_bonus").toString())
    ));
    public static RegistrySupplier<WearableArtifactItem> NOVELTY_DRINKING_HAT = register("novelty_drinking_hat", () -> new WearableArtifactItem(
            SoundEvents.BOTTLE_FILL,
            new CustomTooltipAbility("artifacts.tooltip.item.novelty_drinking_hat"),
            AttributeModifierAbility.create(ModAttributes.DRINKING_SPEED, ModGameRules.NOVELTY_DRINKING_HAT_DRINKING_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, Artifacts.id("novelty_drinking_hat/drinking_speed_bonus").toString()),
            AttributeModifierAbility.create(ModAttributes.EATING_SPEED, ModGameRules.NOVELTY_DRINKING_HAT_EATING_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, Artifacts.id("novelty_drinking_hat/eating_speed_bonus").toString())
    ));
    public static RegistrySupplier<WearableArtifactItem> SNORKEL = register("snorkel", () -> new WearableArtifactItem(
            LimitedWaterBreathingAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> NIGHT_VISION_GOGGLES = register("night_vision_goggles", () -> new WearableArtifactItem(
            NightVisionAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> VILLAGER_HAT = register("villager_hat", () -> new WearableArtifactItem(
            AttributeModifierAbility.create(ModAttributes.VILLAGER_REPUTATION, ModGameRules.VILLAGER_HAT_REPUTATION_BONUS, Artifacts.id("villager_hat/villager_reputation_bonus").toString())
    ));
    public static RegistrySupplier<WearableArtifactItem> SUPERSTITIOUS_HAT = register("superstitious_hat", () -> new WearableArtifactItem(
            IncreaseEnchantmentLevelAbility.looting()
    ));
    public static RegistrySupplier<WearableArtifactItem> COWBOY_HAT = register("cowboy_hat", () -> new WearableArtifactItem(
            SoundEvents.ARMOR_EQUIP_LEATHER::value,
            MountSpeedAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> ANGLERS_HAT = register("anglers_hat", () -> new WearableArtifactItem(
            SoundEvents.ARMOR_EQUIP_LEATHER::value,
            IncreaseEnchantmentLevelAbility.luckOfTheSea(),
            IncreaseEnchantmentLevelAbility.lure()
    ));

    // necklace
    public static RegistrySupplier<WearableArtifactItem> LUCKY_SCARF = register("lucky_scarf", () -> new WearableArtifactItem(
            IncreaseEnchantmentLevelAbility.fortune()
    ));
    public static RegistrySupplier<WearableArtifactItem> SCARF_OF_INVISIBILITY = register("scarf_of_invisibility", () -> new WearableArtifactItem(
            InvisibilityAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> CROSS_NECKLACE = register("cross_necklace", () -> new WearableArtifactItem(
            SoundEvents.ARMOR_EQUIP_DIAMOND::value,
            MakePiglinsNeutralAbility.INSTANCE,
            BonusInvincibilityTicksAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> PANIC_NECKLACE = register("panic_necklace", () -> new WearableArtifactItem(
            SoundEvents.ARMOR_EQUIP_DIAMOND::value,
            ApplySpeedAfterDamageAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> SHOCK_PENDANT = register("shock_pendant", () -> new WearableArtifactItem(
            StrikeAttackersWithLightningAbility.createDefaultInstance(),
            ArtifactAbility.createDefaultInstance(SimpleAbility.lightningImmunity().getFirst())
    ));
    public static RegistrySupplier<WearableArtifactItem> FLAME_PENDANT = register("flame_pendant", () -> new WearableArtifactItem(
            SetAttackersOnFireAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> THORN_PENDANT = register("thorn_pendant", () -> new WearableArtifactItem(
            ThornsAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> CHARM_OF_SINKING = register("charm_of_sinking", () -> new WearableArtifactItem(
            ArtifactAbility.createDefaultInstance(SimpleAbility.sinking().getFirst())
    ));

    // belt
    public static RegistrySupplier<WearableArtifactItem> CLOUD_IN_A_BOTTLE = register("cloud_in_a_bottle", () -> new WearableArtifactItem(
            SoundEvents.BOTTLE_FILL_DRAGONBREATH,
            DoubleJumpAbility.createDefaultInstance(),
            AttributeModifierAbility.create(Attributes.SAFE_FALL_DISTANCE, ModGameRules.CLOUD_IN_A_BOTTLE_SAFE_FALL_DISTANCE_BONUS, Artifacts.id("cloud_in_a_bottle/safe_fall_distance_bonus").toString())
    ));
    public static RegistrySupplier<WearableArtifactItem> OBSIDIAN_SKULL = register("obsidian_skull", () ->  new WearableArtifactItem(
            SoundEvents.ARMOR_EQUIP_IRON::value,
            ApplyFireResistanceAfterFireDamageAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> ANTIDOTE_VESSEL = register("antidote_vessel", () -> new WearableArtifactItem(
            SoundEvents.BOTTLE_FILL,
            MakePiglinsNeutralAbility.INSTANCE,
            RemoveBadEffectsAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> UNIVERSAL_ATTRACTOR = register("universal_attractor", () -> new WearableArtifactItem(
            MakePiglinsNeutralAbility.INSTANCE,
            AttractItemsAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> CRYSTAL_HEART = register("crystal_heart", () -> new WearableArtifactItem(
            SoundEvents.ARMOR_EQUIP_DIAMOND::value,
            AttributeModifierAbility.create(Attributes.MAX_HEALTH, ModGameRules.CRYSTAL_HEART_HEALTH_BONUS, Artifacts.id("crystal_heart/health_bonus").toString())
    ));
    public static RegistrySupplier<WearableArtifactItem> HELIUM_FLAMINGO = register("helium_flamingo", () -> new WearableArtifactItem(
            new Item.Properties(),
            ModSoundEvents.POP, 0.7F,
            SwimInAirAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> CHORUS_TOTEM = register("chorus_totem", () -> new WearableArtifactItem(
            TeleportOnDeathAbility.createDefaultInstance()
    ));

    // hands
    public static RegistrySupplier<WearableArtifactItem> DIGGING_CLAWS = register("digging_claws", () -> new WearableArtifactItem(
            SoundEvents.ARMOR_EQUIP_NETHERITE::value,
            AttributeModifierAbility.create(Attributes.BLOCK_BREAK_SPEED, ModGameRules.DIGGING_CLAWS_BLOCK_BREAK_SPEED_BONUS, Artifacts.id("digging_claws/block_break_speed_bonus").toString()),
            UpgradeToolTierAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> FERAL_CLAWS = register("feral_claws", () -> new WearableArtifactItem(
            SoundEvents.ARMOR_EQUIP_NETHERITE::value,
            AttributeModifierAbility.create(Attributes.ATTACK_SPEED, ModGameRules.FERAL_CLAWS_ATTACK_SPEED_BONUS, Artifacts.id("feral_claws/attack_speed_bonus").toString())
    ));
    public static RegistrySupplier<WearableArtifactItem> POWER_GLOVE = register("power_glove", () -> new WearableArtifactItem(
            AttributeModifierAbility.create(Attributes.ATTACK_DAMAGE, ModGameRules.POWER_GLOVE_ATTACK_DAMAGE_BONUS, Artifacts.id("power_glove/attack_damage_bonus").toString())
    ));
    public static RegistrySupplier<WearableArtifactItem> FIRE_GAUNTLET = register("fire_gauntlet", () -> new WearableArtifactItem(
            SoundEvents.ARMOR_EQUIP_IRON::value,
            AttributeModifierAbility.create(ModAttributes.ATTACK_BURNING_DURATION, ModGameRules.FIRE_GAUNTLET_FIRE_DURATION, Artifacts.id("fire_gauntlet/attack_burning_duration_bonus").toString())
    ));
    public static RegistrySupplier<WearableArtifactItem> POCKET_PISTON = register("pocket_piston", () -> new WearableArtifactItem(
            SoundEvents.PISTON_EXTEND,
            AttributeModifierAbility.create(Attributes.ATTACK_KNOCKBACK, ModGameRules.POCKET_PISTON_ATTACK_KNOCKBACK_BONUS, Artifacts.id("pocket_piston/attack_knockback_bonus").toString())
    ));
    public static RegistrySupplier<WearableArtifactItem> VAMPIRIC_GLOVE = register("vampiric_glove", () -> new WearableArtifactItem(
            AttributeModifierAbility.create(ModAttributes.ATTACK_DAMAGE_ABSORPTION, ModGameRules.VAMPIRIC_GLOVE_ABSORPTION_RATIO, Artifacts.id("vampiric_glove/attack_damage_absorption_bonus").toString()),
            AttributeModifierAbility.create(ModAttributes.MAX_ATTACK_DAMAGE_ABSORBED, ModGameRules.VAMPIRIC_GLOVE_MAX_HEALING_PER_HIT, Artifacts.id("vampiric_glove/max_attack_damage_absorbed_bonus").toString())
    ));
    public static RegistrySupplier<WearableArtifactItem> GOLDEN_HOOK = register("golden_hook", () -> new WearableArtifactItem(
            AttributeModifierAbility.create(ModAttributes.ENTITY_EXPERIENCE, ModGameRules.GOLDEN_HOOK_ENTITY_EXPERIENCE_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, new ResourceLocation("golden_hook/entity_experience_bonus").toString()),
            MakePiglinsNeutralAbility.INSTANCE
    ));
    public static RegistrySupplier<WearableArtifactItem> ONION_RING = register("onion_ring", () -> new WearableArtifactItem(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(2).build()),
            ApplyHasteAfterEatingAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> PICKAXE_HEATER = register("pickaxe_heater", () -> new WearableArtifactItem(
            SoundEvents.ARMOR_EQUIP_IRON::value,
            ArtifactAbility.createDefaultInstance(SimpleAbility.smeltOres().getFirst())
    ));

    // feet
    public static RegistrySupplier<WearableArtifactItem> AQUA_DASHERS = register("aqua_dashers", () -> new WearableArtifactItem(
            ArtifactAbility.createDefaultInstance(SimpleAbility.sprintOnWater().getFirst())
    ));
    public static RegistrySupplier<WearableArtifactItem> BUNNY_HOPPERS = register("bunny_hoppers", () -> new WearableArtifactItem(
            AttributeModifierAbility.create(Attributes.JUMP_STRENGTH, ModGameRules.BUNNY_HOPPERS_JUMP_STRENGTH_BONUS, Artifacts.id("bunny_hoppers/jump_strength_bonus").toString()),
            AttributeModifierAbility.create(Attributes.SAFE_FALL_DISTANCE, ModGameRules.BUNNY_HOPPERS_SAFE_FALL_DISTANCE_BONUS, Artifacts.id("bunny_hoppers/safe_fall_distance_bonus").toString()),
            ArtifactAbility.createDefaultInstance(SimpleAbility.cancelFallDamage().getFirst()),
            new HurtSoundAbility(SoundEvents.RABBIT_HURT)
    ));
    public static RegistrySupplier<WearableArtifactItem> KITTY_SLIPPERS = register("kitty_slippers", () -> new WearableArtifactItem(
            SoundEvents.CAT_AMBIENT,
            ArtifactAbility.createDefaultInstance(SimpleAbility.scareCreepers().getFirst()),
            new HurtSoundAbility(SoundEvents.CAT_HURT)
    ));
    public static RegistrySupplier<WearableArtifactItem> RUNNING_SHOES = register("running_shoes", () -> new WearableArtifactItem(
            SprintingSpeedAbility.INSTANCE,
            SprintingStepHeightAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> SNOWSHOES = register("snowshoes", () -> new WearableArtifactItem(
            ArtifactAbility.createDefaultInstance(SimpleAbility.walkOnPowderSnow().getFirst()),
            ReduceIceSlipperinessAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> STEADFAST_SPIKES = register("steadfast_spikes", () -> new WearableArtifactItem(
            AttributeModifierAbility.create(Attributes.KNOCKBACK_RESISTANCE, ModGameRules.STEADFAST_SPIKES_KNOCKBACK_RESISTANCE, Artifacts.id("steadfast_spikes/knockback_resistance_bonus").toString())
    ));
    public static RegistrySupplier<WearableArtifactItem> FLIPPERS = register("flippers", () -> new WearableArtifactItem(
            SwimSpeedAbility.createDefaultInstance()
    ));
    public static RegistrySupplier<WearableArtifactItem> ROOTED_BOOTS = register("rooted_boots", () -> new WearableArtifactItem(
            SoundEvents.ARMOR_EQUIP_LEATHER::value,
            ReplenishHungerOnGrassAbility.createDefaultInstance(),
            GrowPlantsAfterEatingAbility.createDefaultInstance()
    ));

    // curio
    public static RegistrySupplier<WearableArtifactItem> WHOOPEE_CUSHION = register("whoopee_cushion", () -> new WearableArtifactItem(
            new Item.Properties(),
            ModSoundEvents.FART, 1F,
            FartAbility.createDefaultInstance()
    ));

    private static <T extends Item> RegistrySupplier<T> register(String name, Supplier<T> supplier) {
        return RegistrySupplier.of(ITEMS.register(name, supplier));
    }
}
