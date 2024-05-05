package artifacts.neoforge.data;

import artifacts.Artifacts;
import artifacts.loot.ArtifactRarityAdjustedChance;
import artifacts.loot.ConfigValueChance;
import artifacts.neoforge.loot.RollLootTableModifier;
import artifacts.neoforge.loot.SmeltOresWithPickaxeHeaterModifier;
import artifacts.registry.ModItems;
import artifacts.registry.ModLootTables;
import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LootModifiers implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected final List<Builder> lootBuilders = new ArrayList<>();
    private final PackOutput packOutput;
    private final Map<String, JsonElement> toSerialize = new HashMap<>();

    public LootModifiers(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    private void addLoot() {
        for (ResourceKey<LootTable> lootTable : List.of(EntityType.COW.getDefaultLootTable(), EntityType.MOOSHROOM.getDefaultLootTable())) {
            lootBuilders.add(
                    new Builder(lootTable)
                            .lootPoolCondition(ConfigValueChance.everlastingBeefChance())
                            .lootModifierCondition(LootTableIdCondition.builder(lootTable.location()))
                            .parameterSet(LootContextParamSets.ENTITY)
                            .lootPoolCondition(LootItemKilledByPlayerCondition.killedByPlayer())
                            .everlastingBeef()
            );
        }

        for (ResourceKey<LootTable> lootTable : Arrays.asList(
                BuiltInLootTables.VILLAGE_DESERT_HOUSE,
                BuiltInLootTables.VILLAGE_PLAINS_HOUSE,
                BuiltInLootTables.VILLAGE_SAVANNA_HOUSE
        )) {
            builder(lootTable, 0.05F)
                    .item(ModItems.VILLAGER_HAT.get());
        }
        for (ResourceKey<LootTable> lootTable : Arrays.asList(
                BuiltInLootTables.VILLAGE_SNOWY_HOUSE,
                BuiltInLootTables.VILLAGE_TAIGA_HOUSE
        )) {
            builder(lootTable, 0.08F)
                    .item(ModItems.VILLAGER_HAT.get())
                    .item(ModItems.SNOWSHOES.get());
        }

        builder(BuiltInLootTables.SPAWN_BONUS_CHEST, 1)
                .item(ModItems.WHOOPEE_CUSHION.get());
        builder(BuiltInLootTables.VILLAGE_ARMORER, 0.1F)
                .item(ModItems.STEADFAST_SPIKES.get())
                .item(ModItems.SUPERSTITIOUS_HAT.get())
                .item(ModItems.RUNNING_SHOES.get())
                .item(ModItems.VAMPIRIC_GLOVE.get());
        builder(BuiltInLootTables.VILLAGE_BUTCHER, 0.05F)
                .item(ModItems.EVERLASTING_BEEF.get());
        builder(BuiltInLootTables.VILLAGE_TANNERY, 0.2F)
                .item(ModItems.UMBRELLA.get(), 3)
                .item(ModItems.WHOOPEE_CUSHION.get(), 2)
                .item(ModItems.KITTY_SLIPPERS.get())
                .item(ModItems.BUNNY_HOPPERS.get())
                .item(ModItems.SCARF_OF_INVISIBILITY.get())
                .item(ModItems.ANGLERS_HAT.get());
        builder(BuiltInLootTables.VILLAGE_TEMPLE, 0.2F)
                .item(ModItems.CROSS_NECKLACE.get())
                .item(ModItems.ANTIDOTE_VESSEL.get())
                .item(ModItems.CHARM_OF_SINKING.get());
        builder(BuiltInLootTables.VILLAGE_TOOLSMITH, 0.15F)
                .item(ModItems.DIGGING_CLAWS.get())
                .item(ModItems.POCKET_PISTON.get());
        builder(BuiltInLootTables.VILLAGE_WEAPONSMITH, 0.1F)
                .item(ModItems.FERAL_CLAWS.get());
        builder(BuiltInLootTables.ABANDONED_MINESHAFT, 0.3F)
                .item(ModItems.ONION_RING.get(), 2)
                .item(ModItems.NIGHT_VISION_GOGGLES.get())
                .item(ModItems.PANIC_NECKLACE.get())
                .item(ModItems.OBSIDIAN_SKULL.get())
                .item(ModItems.SUPERSTITIOUS_HAT.get())
                .item(ModItems.DIGGING_CLAWS.get())
                .item(ModItems.CLOUD_IN_A_BOTTLE.get())
                .item(ModItems.VAMPIRIC_GLOVE.get())
                .item(ModItems.AQUA_DASHERS.get())
                .item(ModItems.PICKAXE_HEATER.get())
                .drinkingHat(1);
        builder(BuiltInLootTables.BASTION_HOGLIN_STABLE, 0.25F)
                .artifact(5)
                .item(ModItems.BUNNY_HOPPERS.get(), 3)
                .item(ModItems.FLAME_PENDANT.get(), 3)
                .item(ModItems.COWBOY_HAT.get(), 3)
                .item(ModItems.PICKAXE_HEATER.get(), 3)
                .item(ModItems.EVERLASTING_BEEF.get());
        builder(BuiltInLootTables.BASTION_TREASURE, 0.65F)
                .artifact(6)
                .item(ModItems.GOLDEN_HOOK.get(), 3)
                .item(ModItems.CROSS_NECKLACE.get(), 3)
                .item(ModItems.FIRE_GAUNTLET.get(), 2)
                .item(ModItems.STEADFAST_SPIKES.get())
                .item(ModItems.PANIC_NECKLACE.get())
                .item(ModItems.CRYSTAL_HEART.get())
                .item(ModItems.ANTIDOTE_VESSEL.get());
        builder(BuiltInLootTables.BURIED_TREASURE, 0.25F)
                .item(ModItems.SNORKEL.get(), 5)
                .item(ModItems.FLIPPERS.get(), 5)
                .item(ModItems.UMBRELLA.get(), 5)
                .item(ModItems.GOLDEN_HOOK.get(), 5)
                .item(ModItems.FERAL_CLAWS.get(), 3)
                .item(ModItems.DIGGING_CLAWS.get(), 3)
                .item(ModItems.KITTY_SLIPPERS.get(), 3)
                .item(ModItems.BUNNY_HOPPERS.get(), 3)
                .item(ModItems.LUCKY_SCARF.get(), 3)
                .item(ModItems.AQUA_DASHERS.get(), 3)
                .item(ModItems.ANGLERS_HAT.get(), 3)
                .drinkingHat(3);
        builder(BuiltInLootTables.DESERT_PYRAMID, 0.2F)
                .item(ModItems.FLAME_PENDANT.get(), 2)
                .item(ModItems.THORN_PENDANT.get(), 2)
                .item(ModItems.WHOOPEE_CUSHION.get(), 2)
                .item(ModItems.CHARM_OF_SINKING.get(), 2)
                .item(ModItems.SHOCK_PENDANT.get())
                .item(ModItems.UMBRELLA.get())
                .item(ModItems.SCARF_OF_INVISIBILITY.get())
                .item(ModItems.UNIVERSAL_ATTRACTOR.get())
                .item(ModItems.VAMPIRIC_GLOVE.get())
                .item(ModItems.ONION_RING.get());
        builder(BuiltInLootTables.END_CITY_TREASURE, 0.3F)
                .item(ModItems.CHORUS_TOTEM.get(), 6)
                .item(ModItems.HELIUM_FLAMINGO.get(), 4)
                .item(ModItems.CRYSTAL_HEART.get())
                .item(ModItems.CLOUD_IN_A_BOTTLE.get());
        builder(BuiltInLootTables.JUNGLE_TEMPLE, 0.35F)
                .item(ModItems.KITTY_SLIPPERS.get(), 2)
                .item(ModItems.ROOTED_BOOTS.get(), 2)
                .item(ModItems.BUNNY_HOPPERS.get())
                .item(ModItems.ANGLERS_HAT.get());
        builder(BuiltInLootTables.NETHER_BRIDGE, 0.15F)
                .item(ModItems.CROSS_NECKLACE.get())
                .item(ModItems.NIGHT_VISION_GOGGLES.get())
                .item(ModItems.POCKET_PISTON.get())
                .item(ModItems.RUNNING_SHOES.get())
                .item(ModItems.COWBOY_HAT.get())
                .drinkingHat(1);
        builder(BuiltInLootTables.PILLAGER_OUTPOST, 0.25F)
                .item(ModItems.PANIC_NECKLACE.get())
                .item(ModItems.POCKET_PISTON.get())
                .item(ModItems.STEADFAST_SPIKES.get())
                .item(ModItems.POWER_GLOVE.get())
                .item(ModItems.CROSS_NECKLACE.get())
                .item(ModItems.SCARF_OF_INVISIBILITY.get())
                .item(ModItems.CRYSTAL_HEART.get())
                .item(ModItems.CLOUD_IN_A_BOTTLE.get())
                .item(ModItems.SUPERSTITIOUS_HAT.get())
                .item(ModItems.ROOTED_BOOTS.get());
        builder(BuiltInLootTables.RUINED_PORTAL, 0.15F)
                .item(ModItems.NIGHT_VISION_GOGGLES.get())
                .item(ModItems.THORN_PENDANT.get())
                .item(ModItems.FIRE_GAUNTLET.get())
                .item(ModItems.POWER_GLOVE.get())
                .item(ModItems.UNIVERSAL_ATTRACTOR.get())
                .item(ModItems.OBSIDIAN_SKULL.get())
                .item(ModItems.LUCKY_SCARF.get())
                .item(ModItems.COWBOY_HAT.get());
        builder(BuiltInLootTables.SHIPWRECK_TREASURE, 0.15F)
                .item(ModItems.GOLDEN_HOOK.get(), 3)
                .item(ModItems.SNORKEL.get())
                .item(ModItems.FLIPPERS.get())
                .item(ModItems.SCARF_OF_INVISIBILITY.get())
                .item(ModItems.STEADFAST_SPIKES.get())
                .item(ModItems.UNIVERSAL_ATTRACTOR.get())
                .item(ModItems.FERAL_CLAWS.get())
                .item(ModItems.NIGHT_VISION_GOGGLES.get())
                .item(ModItems.OBSIDIAN_SKULL.get())
                .item(ModItems.RUNNING_SHOES.get())
                .item(ModItems.CHARM_OF_SINKING.get());
        builder(BuiltInLootTables.STRONGHOLD_CORRIDOR, 0.3F)
                .item(ModItems.POWER_GLOVE.get())
                .item(ModItems.ANTIDOTE_VESSEL.get())
                .item(ModItems.SUPERSTITIOUS_HAT.get())
                .item(ModItems.LUCKY_SCARF.get())
                .item(ModItems.AQUA_DASHERS.get())
                .item(ModItems.HELIUM_FLAMINGO.get())
                .item(ModItems.ROOTED_BOOTS.get())
                .item(ModItems.PICKAXE_HEATER.get());
        builder(BuiltInLootTables.UNDERWATER_RUIN_BIG, 0.45F)
                .item(ModItems.SNORKEL.get(), 3)
                .item(ModItems.FLIPPERS.get(), 3)
                .item(ModItems.SUPERSTITIOUS_HAT.get())
                .item(ModItems.LUCKY_SCARF.get())
                .item(ModItems.FIRE_GAUNTLET.get())
                .item(ModItems.CROSS_NECKLACE.get())
                .item(ModItems.POWER_GLOVE.get())
                .item(ModItems.CLOUD_IN_A_BOTTLE.get())
                .item(ModItems.ANGLERS_HAT.get());
        builder(BuiltInLootTables.WOODLAND_MANSION, 0.4F)
                .item(ModItems.CHORUS_TOTEM.get(), 2)
                .artifact(1);
        builder(BuiltInLootTables.IGLOO_CHEST, 0.3F)
                .item(ModItems.SNOWSHOES.get(), 2)
                .item(ModItems.VILLAGER_HAT.get())
                .item(ModItems.LUCKY_SCARF.get());
        builder(BuiltInLootTables.ANCIENT_CITY_ICE_BOX, 0.2F)
                .item(ModItems.SNOWSHOES.get());
        builder(BuiltInLootTables.ANCIENT_CITY, 0.15F)
                .item(ModItems.ROOTED_BOOTS.get())
                .item(ModItems.PICKAXE_HEATER.get())
                .item(ModItems.ONION_RING.get())
                .item(ModItems.AQUA_DASHERS.get())
                .item(ModItems.CHARM_OF_SINKING.get())
                .item(ModItems.SHOCK_PENDANT.get())
                .item(ModItems.HELIUM_FLAMINGO.get());

        archaeologyBuilder(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY)
                .item(ModItems.COWBOY_HAT.get())
                .item(ModItems.OBSIDIAN_SKULL.get())
                .item(ModItems.SNORKEL.get())
                .item(ModItems.POWER_GLOVE.get())
                .item(ModItems.KITTY_SLIPPERS.get())
                .item(ModItems.NIGHT_VISION_GOGGLES.get())
                .item(ModItems.SHOCK_PENDANT.get());
        archaeologyBuilder(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY)
                .item(ModItems.CHARM_OF_SINKING.get())
                .item(ModItems.UNIVERSAL_ATTRACTOR.get())
                .item(ModItems.SUPERSTITIOUS_HAT.get())
                .item(ModItems.UMBRELLA.get())
                .drinkingHat(1);
        archaeologyBuilder(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY)
                .item(ModItems.LUCKY_SCARF.get())
                .item(ModItems.FIRE_GAUNTLET.get())
                .item(ModItems.ANGLERS_HAT.get())
                .item(ModItems.DIGGING_CLAWS.get())
                .item(ModItems.ANTIDOTE_VESSEL.get());
        archaeologyBuilder(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY)
                .item(ModItems.AQUA_DASHERS.get())
                .item(ModItems.ONION_RING.get())
                .item(ModItems.RUNNING_SHOES.get())
                .item(ModItems.BUNNY_HOPPERS.get())
                .item(ModItems.VAMPIRIC_GLOVE.get());
        archaeologyBuilder(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE)
                .item(ModItems.ROOTED_BOOTS.get())
                .item(ModItems.PICKAXE_HEATER.get())
                .item(ModItems.AQUA_DASHERS.get())
                .item(ModItems.SNOWSHOES.get())
                .item(ModItems.STEADFAST_SPIKES.get())
                .item(ModItems.VILLAGER_HAT.get())
                .item(ModItems.CLOUD_IN_A_BOTTLE.get())
                .item(ModItems.FERAL_CLAWS.get())
                .item(ModItems.POCKET_PISTON.get())
                .item(ModItems.WHOOPEE_CUSHION.get())
                .item(ModItems.FLAME_PENDANT.get())
                .item(ModItems.THORN_PENDANT.get());
    }

    protected Builder builder(ResourceKey<LootTable> lootTable, float baseChance) {
        if (!ModLootTables.INJECTED_LOOT_TABLES.contains(lootTable)) {
            throw new IllegalArgumentException("Missing injected loot table: %s".formatted(lootTable));
        }
        Builder builder = new Builder(lootTable);
        builder.lootPoolCondition(ArtifactRarityAdjustedChance.adjustedChance(baseChance));
        builder.lootModifierCondition(LootTableIdCondition.builder(lootTable.location()));
        lootBuilders.add(builder);
        return builder;
    }

    protected Builder archaeologyBuilder(ResourceKey<LootTable> lootTable) {
        if (!ModLootTables.ARCHAEOLOGY_LOOT_TABLES.contains(lootTable)) {
            throw new IllegalArgumentException("Missing archaeology loot table: %s".formatted(lootTable));
        }
        Builder builder = new Builder(lootTable).replace();
        builder.lootModifierCondition(LootTableIdCondition.builder(lootTable.location()));
        builder.lootModifierCondition(ConfigValueChance.archaeologyChance());
        lootBuilders.add(builder);
        return builder;
    }

    protected void start() {
        add("smelt_ores_with_pickaxe_heater", new SmeltOresWithPickaxeHeaterModifier(new LootItemCondition[0]));
        addLoot();

        for (Builder lootBuilder : lootBuilders) {
            add("inject/" + lootBuilder.getName(), lootBuilder.build());
        }
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        start();

        Path neoForgePath = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve("neoforge").resolve("loot_modifiers").resolve("global_loot_modifiers.json");
        Path modifierFolderPath = this.packOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(Artifacts.MOD_ID).resolve("loot_modifiers");
        List<ResourceLocation> entries = new ArrayList<>();

        ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();

        toSerialize.forEach((name, json) -> {
            entries.add(new ResourceLocation(Artifacts.MOD_ID, name));
            Path modifierPath = modifierFolderPath.resolve(name + ".json");
            futuresBuilder.add(DataProvider.saveStable(cache, json, modifierPath));
        });

        JsonObject forgeJson = new JsonObject();
        forgeJson.addProperty("replace", false);
        forgeJson.add("entries", GSON.toJsonTree(entries.stream().map(ResourceLocation::toString).collect(Collectors.toList())));

        JsonArray conditions = new JsonArray();
        JsonObject condition = new JsonObject();
        JsonObject modsLoaded = new JsonObject();
        conditions.add(condition);
        condition.addProperty("condition", "fabric:not");
        condition.add("value", modsLoaded);
        modsLoaded.addProperty("condition", "fabric:all_mods_loaded");
        modsLoaded.add("values", new JsonArray());
        forgeJson.add("fabric:load_conditions", conditions);

        futuresBuilder.add(DataProvider.saveStable(cache, forgeJson, neoForgePath));

        return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
    }

    public <T extends IGlobalLootModifier> void add(String modifier, T instance) {
        JsonElement json = IGlobalLootModifier.DIRECT_CODEC.encodeStart(JsonOps.INSTANCE, instance).getOrThrow();
        this.toSerialize.put(modifier, json);
    }

    @Override
    public String getName() {
        return "Global Loot Modifiers : " + Artifacts.MOD_ID;
    }

    @SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
    protected static class Builder {

        private final ResourceKey<LootTable> lootTable;
        private final LootPool.Builder lootPool = LootPool.lootPool();
        private final List<LootItemCondition> conditions;
        private boolean replace = false;

        private LootContextParamSet paramSet = LootContextParamSets.CHEST;

        private Builder(ResourceKey<LootTable> lootTable) {
            this.lootTable = lootTable;
            this.conditions = new ArrayList<>();
        }

        private RollLootTableModifier build() {
            return new RollLootTableModifier(conditions.toArray(new LootItemCondition[]{}), Artifacts.key(Registries.LOOT_TABLE, "inject/" + lootTable.location().getPath()), replace);
        }

        protected LootTable.Builder createLootTable() {
            return new LootTable.Builder().withPool(lootPool);
        }

        public LootContextParamSet getParameterSet() {
            return paramSet;
        }

        protected String getName() {
            return lootTable.location().getPath();
        }

        private Builder parameterSet(LootContextParamSet paramSet) {
            this.paramSet = paramSet;
            return this;
        }

        public Builder replace() {
            this.replace = true;
            return this;
        }

        private Builder lootPoolCondition(LootItemCondition.Builder condition) {
            lootPool.when(condition);
            return this;
        }

        private Builder lootModifierCondition(LootItemCondition.Builder condition) {
            conditions.add(condition.build());
            return this;
        }

        private Builder item(Item item, int weight) {
            lootPool.add(LootTables.item(item, weight));
            return this;
        }

        private Builder item(Item item) {
            return item(item, 1);
        }

        private Builder artifact(int weight) {
            lootPool.add(LootTables.artifact(weight));
            return this;
        }

        private Builder drinkingHat(int weight) {
            lootPool.add(LootTables.drinkingHat(weight));
            return this;
        }

        private Builder everlastingBeef() {
            lootPool.add(LootTables.item(ModItems.EVERLASTING_BEEF.get(), 1)
                    .apply(
                            SmeltItemFunction.smelted().when(
                                    LootItemEntityPropertyCondition.hasProperties(
                                            LootContext.EntityTarget.THIS,
                                            EntityPredicate.Builder.entity().flags(
                                                    EntityFlagsPredicate.Builder.flags()
                                                            .setOnFire(true)
                                            )
                                    )
                            )
                    )
            );
            return this;
        }
    }
}
