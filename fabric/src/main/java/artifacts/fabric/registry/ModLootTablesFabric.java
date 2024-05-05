package artifacts.fabric.registry;

import artifacts.Artifacts;
import artifacts.loot.ConfigValueChance;
import artifacts.loot.ReplaceWithLootTableFunction;
import artifacts.registry.ModLootTables;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

public class ModLootTablesFabric {

    public static void onLootTableLoad(ResourceKey<LootTable> id, LootTable.Builder supplier, LootTableSource source) {
        if (source.isBuiltin()) {
            if (ModLootTables.INJECTED_LOOT_TABLES.contains(id)) {
                supplier.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(getInjectName(id))));
            }
            if (ModLootTables.ARCHAEOLOGY_LOOT_TABLES.contains(id)) {
                supplier.modifyPools(pool -> pool.apply(ReplaceWithLootTableFunction
                        .replaceWithLootTable(getInjectName(id))
                        .when(ConfigValueChance.archaeologyChance()))
                );
            }
        }
    }

    private static ResourceKey<LootTable> getInjectName(ResourceKey<LootTable> name) {
        return Artifacts.key(Registries.LOOT_TABLE, "inject/" + name.location().getPath());
    }
}
