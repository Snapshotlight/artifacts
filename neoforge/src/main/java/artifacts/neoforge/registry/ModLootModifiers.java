package artifacts.neoforge.registry;

import artifacts.Artifacts;
import artifacts.neoforge.loot.RollLootTableModifier;
import artifacts.neoforge.loot.SmeltOresWithPickaxeHeaterModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModLootModifiers {

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Artifacts.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<RollLootTableModifier>> ROLL_LOOT_TABLE = LOOT_MODIFIERS.register("roll_loot_table", RollLootTableModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<SmeltOresWithPickaxeHeaterModifier>> SMELT_ORE = LOOT_MODIFIERS.register("smelt_ores_with_pickaxe_heater", SmeltOresWithPickaxeHeaterModifier.CODEC);
}
