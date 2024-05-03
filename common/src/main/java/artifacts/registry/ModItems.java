package artifacts.registry;

import artifacts.Artifacts;
import artifacts.ability.*;
import artifacts.ability.mobeffect.InvisibilityAbility;
import artifacts.ability.mobeffect.JumpBoostAbility;
import artifacts.ability.mobeffect.LimitedWaterBreathingAbility;
import artifacts.ability.mobeffect.NightVisionAbility;
import artifacts.ability.retaliation.SetAttackersOnFireAbility;
import artifacts.ability.retaliation.StrikeAttackersWithLightningAbility;
import artifacts.ability.retaliation.ThornsAbility;
import artifacts.item.EverlastingFoodItem;
import artifacts.item.UmbrellaItem;
import artifacts.item.wearable.WearableArtifactItem;
import artifacts.item.wearable.WhoopeeCushionItem;
import artifacts.item.wearable.belt.*;
import artifacts.item.wearable.feet.*;
import artifacts.item.wearable.hands.*;
import artifacts.item.wearable.head.*;
import artifacts.item.wearable.necklace.*;
import artifacts.platform.PlatformServices;
import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
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

    public static RegistrySupplier<Item> MIMIC_SPAWN_EGG = register("mimic_spawn_egg", () -> new ArchitecturySpawnEggItem(ModEntityTypes.MIMIC.getRegistrySupplier(), 0x805113, 0x212121, new Item.Properties().arch$tab(CREATIVE_TAB.getRegistrySupplier())));
    public static RegistrySupplier<Item> UMBRELLA = register("umbrella", UmbrellaItem::new);
    public static RegistrySupplier<Item> EVERLASTING_BEEF = register("everlasting_beef", () -> new EverlastingFoodItem(new FoodProperties.Builder().nutrition(3).saturationMod(0.3F).build(), ModGameRules.EVERLASTING_BEEF_COOLDOWN, ModGameRules.EVERLASTING_BEEF_ENABLED));
    public static RegistrySupplier<Item> ETERNAL_STEAK = register("eternal_steak", () -> new EverlastingFoodItem(new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).build(), ModGameRules.ETERNAL_STEAK_COOLDOWN, ModGameRules.ETERNAL_STEAK_ENABLED));

    // head
    public static RegistrySupplier<DrinkingHatItem> PLASTIC_DRINKING_HAT = register("plastic_drinking_hat", () -> new DrinkingHatItem(ModGameRules.PLASTIC_DRINKING_HAT_DRINKING_DURATION_MULTIPLIER, ModGameRules.PLASTIC_DRINKING_HAT_EATING_DURATION_MULTIPLIER, false));
    public static RegistrySupplier<DrinkingHatItem> NOVELTY_DRINKING_HAT = register("novelty_drinking_hat", () -> new DrinkingHatItem(ModGameRules.NOVELTY_DRINKING_HAT_DRINKING_DURATION_MULTIPLIER, ModGameRules.NOVELTY_DRINKING_HAT_EATING_DURATION_MULTIPLIER, true));
    public static RegistrySupplier<WearableArtifactItem> SNORKEL = register("snorkel", () -> new WearableArtifactItem(new LimitedWaterBreathingAbility()));
    public static RegistrySupplier<WearableArtifactItem> NIGHT_VISION_GOGGLES = register("night_vision_goggles", () -> new WearableArtifactItem(new NightVisionAbility()));
    public static RegistrySupplier<WearableArtifactItem> VILLAGER_HAT = register("villager_hat", () -> new WearableArtifactItem(new VillagerReputation()));
    public static RegistrySupplier<WearableArtifactItem> SUPERSTITIOUS_HAT = register("superstitious_hat", () -> new WearableArtifactItem(IncreaseEnchantmentLevelAbility.looting()));
    public static RegistrySupplier<WearableArtifactItem> COWBOY_HAT = register("cowboy_hat", CowboyHatItem::new);
    public static RegistrySupplier<WearableArtifactItem> ANGLERS_HAT = register("anglers_hat", AnglersHatItem::new);

    // necklace
    public static RegistrySupplier<WearableArtifactItem> LUCKY_SCARF = register("lucky_scarf", () -> new WearableArtifactItem(IncreaseEnchantmentLevelAbility.fortune()));
    public static RegistrySupplier<WearableArtifactItem> SCARF_OF_INVISIBILITY = register("scarf_of_invisibility", () -> new WearableArtifactItem(new InvisibilityAbility()));
    public static RegistrySupplier<WearableArtifactItem> CROSS_NECKLACE = register("cross_necklace", CrossNecklaceItem::new);
    public static RegistrySupplier<WearableArtifactItem> PANIC_NECKLACE = register("panic_necklace", PanicNecklaceItem::new);
    public static RegistrySupplier<WearableArtifactItem> SHOCK_PENDANT = register("shock_pendant", () -> new WearableArtifactItem(new StrikeAttackersWithLightningAbility(), SimpleAbility.lightningImmunity()));
    public static RegistrySupplier<WearableArtifactItem> FLAME_PENDANT = register("flame_pendant", () -> new WearableArtifactItem(new SetAttackersOnFireAbility()));
    public static RegistrySupplier<WearableArtifactItem> THORN_PENDANT = register("thorn_pendant", () -> new WearableArtifactItem(new ThornsAbility()));
    public static RegistrySupplier<WearableArtifactItem> CHARM_OF_SINKING = register("charm_of_sinking", () -> new WearableArtifactItem(SimpleAbility.sinking()));

    // belt
    public static RegistrySupplier<WearableArtifactItem> CLOUD_IN_A_BOTTLE = register("cloud_in_a_bottle", CloudInABottleItem::new);
    public static RegistrySupplier<WearableArtifactItem> OBSIDIAN_SKULL = register("obsidian_skull", ObsidianSkullItem::new);
    public static RegistrySupplier<WearableArtifactItem> ANTIDOTE_VESSEL = register("antidote_vessel", AntidoteVesselItem::new);
    public static RegistrySupplier<WearableArtifactItem> UNIVERSAL_ATTRACTOR = register("universal_attractor", () -> new WearableArtifactItem(new MakePiglinsNeutralAbility(), new AttractItemsAbility()));
    public static RegistrySupplier<WearableArtifactItem> CRYSTAL_HEART = register("crystal_heart", CrystalHeartItem::new);
    public static RegistrySupplier<WearableArtifactItem> HELIUM_FLAMINGO = register("helium_flamingo", HeliumFlamingoItem::new);
    public static RegistrySupplier<WearableArtifactItem> CHORUS_TOTEM = register("chorus_totem", () -> new WearableArtifactItem(new TeleportOnDeathAbility()));

    // hands
    public static RegistrySupplier<WearableArtifactItem> DIGGING_CLAWS = register("digging_claws", DiggingClawsItem::new);
    public static RegistrySupplier<WearableArtifactItem> FERAL_CLAWS = register("feral_claws", FeralClawsItem::new);
    public static RegistrySupplier<WearableArtifactItem> POWER_GLOVE = register("power_glove", () -> new WearableArtifactItem(
            CustomTooltipAbility.attributeTooltip("attack_damage"),
            new AttributeModifierAbility(Attributes.ATTACK_DAMAGE, () -> (double) ModGameRules.POWER_GLOVE_ATTACK_DAMAGE_BONUS.get(), Artifacts.id("power_glove/attack_damage_bonus"))
    ));
    public static RegistrySupplier<WearableArtifactItem> FIRE_GAUNTLET = register("fire_gauntlet", FireGauntletItem::new);
    public static RegistrySupplier<WearableArtifactItem> POCKET_PISTON = register("pocket_piston", PocketPistonItem::new);
    public static RegistrySupplier<WearableArtifactItem> VAMPIRIC_GLOVE = register("vampiric_glove", () -> new WearableArtifactItem(new AbsorbDamageAbility()));
    public static RegistrySupplier<WearableArtifactItem> GOLDEN_HOOK = register("golden_hook", () -> new WearableArtifactItem(new ExperienceBonusAbility(), new MakePiglinsNeutralAbility()));
    public static RegistrySupplier<WearableArtifactItem> ONION_RING = register("onion_ring", () -> new WearableArtifactItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).build()), new ApplyHasteAfterEatingAbility()));
    public static RegistrySupplier<WearableArtifactItem> PICKAXE_HEATER = register("pickaxe_heater", PickaxeHeaterItem::new);

    // feet
    public static RegistrySupplier<WearableArtifactItem> AQUA_DASHERS = register("aqua_dashers", () -> new WearableArtifactItem(SimpleAbility.sprintOnWater()));
    public static RegistrySupplier<WearableArtifactItem> BUNNY_HOPPERS = register("bunny_hoppers", () -> new WearableArtifactItem(new JumpBoostAbility(), SimpleAbility.cancelFallDamage(), new HurtSoundAbility(SoundEvents.RABBIT_HURT)));
    public static RegistrySupplier<WearableArtifactItem> KITTY_SLIPPERS = register("kitty_slippers", KittySlippersItem::new);
    public static RegistrySupplier<WearableArtifactItem> RUNNING_SHOES = register("running_shoes", () -> new WearableArtifactItem(new SprintingSpeedAbility(), new SprintingStepHeightAbility()));
    public static RegistrySupplier<WearableArtifactItem> SNOWSHOES = register("snowshoes", () -> new WearableArtifactItem(SimpleAbility.walkOnPowderSnow(), new ReduceIceSlipperinessAbility()));
    public static RegistrySupplier<WearableArtifactItem> STEADFAST_SPIKES = register("steadfast_spikes", () -> new WearableArtifactItem(
            CustomTooltipAbility.attributeTooltip("knockback_resistance"),
            new AttributeModifierAbility(Attributes.KNOCKBACK_RESISTANCE, ModGameRules.STEADFAST_SPIKES_KNOCKBACK_RESISTANCE, Artifacts.id("steadfast_spikes/knockback_resistance"))
    ));
    public static RegistrySupplier<WearableArtifactItem> FLIPPERS = register("flippers", () -> new WearableArtifactItem(
            CustomTooltipAbility.attributeTooltip("swim_speed"),
            PlatformServices.platformHelper.getFlippersSwimAbility()
    ));
    public static RegistrySupplier<WearableArtifactItem> ROOTED_BOOTS = register("rooted_boots", RootedBootsItem::new);

    // curio
    public static RegistrySupplier<WearableArtifactItem> WHOOPEE_CUSHION = register("whoopee_cushion", WhoopeeCushionItem::new);

    private static <T extends Item> RegistrySupplier<T> register(String name, Supplier<T> supplier) {
        return RegistrySupplier.of(ITEMS.register(name, supplier));
    }
}
