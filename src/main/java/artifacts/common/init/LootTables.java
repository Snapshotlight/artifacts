package artifacts.common.init;

import artifacts.Artifacts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;

public class LootTables {

    public static final ResourceLocation MIMIC = new ResourceLocation(Artifacts.MODID, "mimic");

    @Mod.EventBusSubscriber(modid = Artifacts.MODID)
    public static class LootTableEvents {

        public static final List<String> LOOT_TABLE_LOCATIONS = Arrays.asList(
                "chests/abandoned_mineshaft",
                "chests/desert_pyramid",
                "chests/underwater_ruin_big",
                "chests/simple_dungeon"
        );

        @SubscribeEvent
        public static void onLootTableLoad(LootTableLoadEvent event) {
            String prefix = "minecraft:";
            String name = event.getName().toString();

            if (name.startsWith(prefix)) {
                String location = name.substring(name.indexOf(prefix) + prefix.length());
                if (LOOT_TABLE_LOCATIONS.contains(location)) {
                    event.getTable().addPool(getInjectPool(location));
                }
            }
        }

        public static LootPool getInjectPool(String entryName) {
            return LootPool.builder()
                    .addEntry(getInjectEntry(entryName))
                    .name("artifacts_inject")
                    .build();
        }

        private static LootEntry.Builder<?> getInjectEntry(String name) {
            ResourceLocation table = new ResourceLocation(Artifacts.MODID, "inject/" + name);
            return TableLootEntry.builder(table).weight(1);
        }
    }
}
