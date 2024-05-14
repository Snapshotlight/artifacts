package artifacts.registry;

import artifacts.Artifacts;
import artifacts.ability.*;
import artifacts.ability.mobeffect.GenericMobEffectAbility;
import artifacts.ability.mobeffect.LimitedWaterBreathingAbility;
import artifacts.ability.mobeffect.NightVisionAbility;
import artifacts.ability.retaliation.SetAttackersOnFireAbility;
import artifacts.ability.retaliation.StrikeAttackersWithLightningAbility;
import artifacts.ability.retaliation.ThornsAbility;
import artifacts.ability.value.IntegerValue;
import artifacts.item.EverlastingFoodItem;
import artifacts.item.UmbrellaItem;
import artifacts.item.WearableArtifactItem;
import artifacts.platform.PlatformServices;
import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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
    public static final RegistrySupplier<Item> EVERLASTING_BEEF = register("everlasting_beef", () -> new EverlastingFoodItem(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build(), ModGameRules.EVERLASTING_BEEF_COOLDOWN, ModGameRules.EVERLASTING_BEEF_ENABLED));
    public static final RegistrySupplier<Item> ETERNAL_STEAK = register("eternal_steak", () -> new EverlastingFoodItem(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8F).build(), ModGameRules.ETERNAL_STEAK_COOLDOWN, ModGameRules.ETERNAL_STEAK_ENABLED));

    // head
    public static final RegistrySupplier<WearableArtifactItem> PLASTIC_DRINKING_HAT = wearableItem("plastic_drinking_hat", builder -> builder
            .equipSound(SoundEvents.BOTTLE_FILL)
            .addAttributeModifier(ModAttributes.DRINKING_SPEED, ModGameRules.PLASTIC_DRINKING_HAT_DRINKING_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(ModAttributes.EATING_SPEED, ModGameRules.PLASTIC_DRINKING_HAT_EATING_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );
    public static final RegistrySupplier<WearableArtifactItem> NOVELTY_DRINKING_HAT = wearableItem("novelty_drinking_hat", builder -> builder
            .equipSound(SoundEvents.BOTTLE_FILL)
            .addAbility(new CustomTooltipAbility("artifacts.tooltip.item.novelty_drinking_hat"))
            .addAttributeModifier(ModAttributes.DRINKING_SPEED, ModGameRules.NOVELTY_DRINKING_HAT_DRINKING_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(ModAttributes.EATING_SPEED, ModGameRules.NOVELTY_DRINKING_HAT_EATING_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );
    public static final RegistrySupplier<WearableArtifactItem> SNORKEL = wearableItem("snorkel", builder -> builder
            .addAbility(LimitedWaterBreathingAbility.CODEC)
    );
    public static final RegistrySupplier<WearableArtifactItem> NIGHT_VISION_GOGGLES = wearableItem("night_vision_goggles", builder -> builder
            .addAbility(NightVisionAbility.CODEC)
    );
    public static final RegistrySupplier<WearableArtifactItem> VILLAGER_HAT = wearableItem("villager_hat", builder -> builder
            .addAttributeModifier(ModAttributes.VILLAGER_REPUTATION, ModGameRules.VILLAGER_HAT_REPUTATION_BONUS)
    );
    public static final RegistrySupplier<WearableArtifactItem> SUPERSTITIOUS_HAT = wearableItem("superstitious_hat", builder -> builder
            .addAbility(IncreaseEnchantmentLevelAbility.looting())
    );
    public static final RegistrySupplier<WearableArtifactItem> COWBOY_HAT = wearableItem("cowboy_hat", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_LEATHER)
            .addAttributeModifier(ModAttributes.MOUNT_SPEED, ModGameRules.COWBOY_HAT_MOUNT_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );
    public static final RegistrySupplier<WearableArtifactItem> ANGLERS_HAT = wearableItem("anglers_hat", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_LEATHER)
            .addAbility(IncreaseEnchantmentLevelAbility.luckOfTheSea())
            .addAbility(IncreaseEnchantmentLevelAbility.lure())
    );

    // necklace
    public static final RegistrySupplier<WearableArtifactItem> LUCKY_SCARF = wearableItem("lucky_scarf", builder -> builder
            .addAbility(IncreaseEnchantmentLevelAbility.fortune())
    );
    public static final RegistrySupplier<WearableArtifactItem> SCARF_OF_INVISIBILITY = wearableItem("scarf_of_invisibility", builder -> builder
            .addAbility(new GenericMobEffectAbility(MobEffects.INVISIBILITY, IntegerValue.ONE, ModGameRules.SCARF_OF_INVISIBILITY_ENABLED))
    );
    public static final RegistrySupplier<WearableArtifactItem> CROSS_NECKLACE = wearableItem("cross_necklace", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_DIAMOND)
            .addAbility(new ApplyCooldownAfterDamageAbility(ModGameRules.CROSS_NECKLACE_COOLDOWN, Optional.empty()))
            .addAttributeModifier(ModAttributes.INVINCIBILITY_TICKS, ModGameRules.CROSS_NECKLACE_BONUS_INVINCIBILITY_TICKS, AttributeModifier.Operation.ADD_VALUE, false)
    );
    public static final RegistrySupplier<WearableArtifactItem> PANIC_NECKLACE = wearableItem("panic_necklace", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_DIAMOND)
            .addAbility(ApplySpeedAfterDamageAbility.CODEC)
    );
    public static final RegistrySupplier<WearableArtifactItem> SHOCK_PENDANT = wearableItem("shock_pendant", builder -> builder
            .addAbility(StrikeAttackersWithLightningAbility.CODEC)
            .addAbility(SimpleAbility.lightningImmunity())
    );
    public static final RegistrySupplier<WearableArtifactItem> FLAME_PENDANT = wearableItem("flame_pendant", builder -> builder
            .addAbility(SetAttackersOnFireAbility.CODEC)
    );
    public static final RegistrySupplier<WearableArtifactItem> THORN_PENDANT = wearableItem("thorn_pendant", builder -> builder
            .addAbility(ThornsAbility.CODEC)
    );
    public static final RegistrySupplier<WearableArtifactItem> CHARM_OF_SINKING = wearableItem("charm_of_sinking", builder -> builder
            .addAbility(SimpleAbility.sinking())
    );

    // belt
    public static final RegistrySupplier<WearableArtifactItem> CLOUD_IN_A_BOTTLE = wearableItem("cloud_in_a_bottle", builder -> builder
            .equipSound(SoundEvents.BOTTLE_FILL_DRAGONBREATH)
            .addAbility(DoubleJumpAbility.CODEC)
            .addAttributeModifier(Attributes.SAFE_FALL_DISTANCE, ModGameRules.CLOUD_IN_A_BOTTLE_SAFE_FALL_DISTANCE_BONUS)
    );
    public static final RegistrySupplier<WearableArtifactItem> OBSIDIAN_SKULL = wearableItem("obsidian_skull", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_IRON)
            .addAbility(ApplyFireResistanceAfterFireDamageAbility.CODEC)
    );
    public static final RegistrySupplier<WearableArtifactItem> ANTIDOTE_VESSEL = wearableItem("antidote_vessel", builder -> builder
            .equipSound(SoundEvents.BOTTLE_FILL)
            .addAbility(MakePiglinsNeutralAbility.INSTANCE)
            .addAbility(RemoveBadEffectsAbility.CODEC)
    );
    public static final RegistrySupplier<WearableArtifactItem> UNIVERSAL_ATTRACTOR = wearableItem("universal_attractor", builder -> builder
            .addAbility(MakePiglinsNeutralAbility.INSTANCE)
            .addAbility(AttractItemsAbility.CODEC)
    );
    public static final RegistrySupplier<WearableArtifactItem> CRYSTAL_HEART = wearableItem("crystal_heart", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_DIAMOND)
            .addAttributeModifier(Attributes.MAX_HEALTH, ModGameRules.CRYSTAL_HEART_HEALTH_BONUS)
    );
    public static final RegistrySupplier<WearableArtifactItem> HELIUM_FLAMINGO = wearableItem("helium_flamingo", builder -> builder
            .equipSound(ModSoundEvents.POP)
            .equipSoundPitch(0.7F)
            .addAbility(SwimInAirAbility.CODEC)
    );
    public static final RegistrySupplier<WearableArtifactItem> CHORUS_TOTEM = wearableItem("chorus_totem", builder -> builder
            .addAbility(TeleportOnDeathAbility.CODEC)
    );

    // hands
    public static final RegistrySupplier<WearableArtifactItem> DIGGING_CLAWS = wearableItem("digging_claws", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_NETHERITE)
            .addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, ModGameRules.DIGGING_CLAWS_BLOCK_BREAK_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAbility(UpgradeToolTierAbility.CODEC)
    );
    public static final RegistrySupplier<WearableArtifactItem> FERAL_CLAWS = wearableItem("feral_claws", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_NETHERITE)
            .addAttributeModifier(Attributes.ATTACK_SPEED, ModGameRules.FERAL_CLAWS_ATTACK_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );
    public static final RegistrySupplier<WearableArtifactItem> POWER_GLOVE = wearableItem("power_glove", builder -> builder
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, ModGameRules.POWER_GLOVE_ATTACK_DAMAGE_BONUS)
    );
    public static final RegistrySupplier<WearableArtifactItem> FIRE_GAUNTLET = wearableItem("fire_gauntlet", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_IRON)
            .addAttributeModifier(ModAttributes.ATTACK_BURNING_DURATION, ModGameRules.FIRE_GAUNTLET_FIRE_DURATION)
    );
    public static final RegistrySupplier<WearableArtifactItem> POCKET_PISTON = wearableItem("pocket_piston", builder -> builder
            .equipSound(SoundEvents.PISTON_EXTEND)
            .addAttributeModifier(Attributes.ATTACK_KNOCKBACK, ModGameRules.POCKET_PISTON_ATTACK_KNOCKBACK_BONUS)
    );
    public static final RegistrySupplier<WearableArtifactItem> VAMPIRIC_GLOVE = wearableItem("vampiric_glove", builder -> builder
            .addAttributeModifier(ModAttributes.ATTACK_DAMAGE_ABSORPTION, ModGameRules.VAMPIRIC_GLOVE_ABSORPTION_RATIO)
            .addAttributeModifier(ModAttributes.MAX_ATTACK_DAMAGE_ABSORBED, ModGameRules.VAMPIRIC_GLOVE_MAX_HEALING_PER_HIT)
    );
    public static final RegistrySupplier<WearableArtifactItem> GOLDEN_HOOK = wearableItem("golden_hook", builder -> builder
            .addAttributeModifier(ModAttributes.ENTITY_EXPERIENCE, ModGameRules.GOLDEN_HOOK_ENTITY_EXPERIENCE_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAbility(MakePiglinsNeutralAbility.INSTANCE)
    );
    public static final RegistrySupplier<WearableArtifactItem> ONION_RING = wearableItem("onion_ring", builder -> builder
            .properties(properties -> properties.food(new FoodProperties.Builder().nutrition(2).build()))
            .addAbility(ApplyHasteAfterEatingAbility.CODEC)
    );
    public static final RegistrySupplier<WearableArtifactItem> PICKAXE_HEATER = wearableItem("pickaxe_heater", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_IRON)
            .addAbility(SimpleAbility.smeltOres())
    );

    // feet
    public static final RegistrySupplier<WearableArtifactItem> AQUA_DASHERS = wearableItem("aqua_dashers", builder -> builder
            .addAbility(SimpleAbility.sprintOnWater())
    );
    public static final RegistrySupplier<WearableArtifactItem> BUNNY_HOPPERS = wearableItem("bunny_hoppers", builder -> builder
            .addAttributeModifier(Attributes.JUMP_STRENGTH, ModGameRules.BUNNY_HOPPERS_JUMP_STRENGTH_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(Attributes.SAFE_FALL_DISTANCE, ModGameRules.BUNNY_HOPPERS_SAFE_FALL_DISTANCE_BONUS)
            .addAbility(SimpleAbility.cancelFallDamage())
            .addAbility(new HurtSoundAbility(SoundEvents.RABBIT_HURT))
    );
    public static final RegistrySupplier<WearableArtifactItem> KITTY_SLIPPERS = wearableItem("kitty_slippers", builder -> builder
            .equipSound(SoundEvents.CAT_AMBIENT)
            .addAbility(SimpleAbility.scareCreepers())
            .addAbility(new HurtSoundAbility(SoundEvents.CAT_HURT))
    );
    public static final RegistrySupplier<WearableArtifactItem> RUNNING_SHOES = wearableItem("running_shoes", builder -> builder
            .addAttributeModifier(ModAttributes.SPRINTING_SPEED, ModGameRules.RUNNING_SHOES_SPRINTING_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(ModAttributes.SPRINTING_STEP_HEIGHT, ModGameRules.RUNNING_SHOES_SPRINTING_STEP_HEIGHT_BONUS)
    );
    public static final RegistrySupplier<WearableArtifactItem> SNOWSHOES = wearableItem("snowshoes", builder -> builder
            .addAbility(SimpleAbility.walkOnPowderSnow().getFirst())
            .addAttributeModifier(ModAttributes.SLIP_RESISTANCE, ModGameRules.SNOWSHOES_SLIPPERINESS_REDUCTION)
    );
    public static final RegistrySupplier<WearableArtifactItem> STEADFAST_SPIKES = wearableItem("steadfast_spikes", builder -> builder
            .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, ModGameRules.STEADFAST_SPIKES_KNOCKBACK_RESISTANCE)
    );
    public static final RegistrySupplier<WearableArtifactItem> FLIPPERS = wearableItem("flippers", builder -> builder
            .addAttributeModifier(ModAttributes.SWIM_SPEED, ModGameRules.FLIPPERS_SWIM_SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );
    public static final RegistrySupplier<WearableArtifactItem> ROOTED_BOOTS = wearableItem("rooted_boots", builder -> builder
            .equipSound(SoundEvents.ARMOR_EQUIP_LEATHER)
            .addAbility(ReplenishHungerOnGrassAbility.CODEC)
            .addAbility(GrowPlantsAfterEatingAbility.CODEC)
    );

    // curio
    public static final RegistrySupplier<WearableArtifactItem> WHOOPEE_CUSHION = wearableItem("whoopee_cushion", builder -> builder
            .equipSound(ModSoundEvents.FART)
            .addAttributeModifier(ModAttributes.FLATULENCE, ModGameRules.WHOOPEE_CUSHION_FART_CHANCE)
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
