package artifacts.registry;

import artifacts.Artifacts;
import artifacts.loot.ReplaceWithLootTableFunction;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class ModLootFunctions {

    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTIONS = DeferredRegister.create(Artifacts.MOD_ID, Registries.LOOT_FUNCTION_TYPE);

    public static final Holder<LootItemFunctionType<ReplaceWithLootTableFunction>> REPLACE_WITH_LOOT_TABLE = LOOT_FUNCTIONS.register("replace_with_loot_table", () -> new LootItemFunctionType<>(ReplaceWithLootTableFunction.CODEC));

}
