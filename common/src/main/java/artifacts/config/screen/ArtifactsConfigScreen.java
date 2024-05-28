package artifacts.config.screen;

import artifacts.Artifacts;
import artifacts.config.ItemConfigs;
import artifacts.config.value.Value;
import artifacts.config.value.type.ValueType;
import artifacts.item.WearableArtifactItem;
import artifacts.registry.ModItems;
import dev.architectury.registry.registries.RegistrySupplier;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.impl.builders.AbstractFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArtifactsConfigScreen {

    public static Screen createScreen(Screen parent) {
        ItemConfigs.loadFromConfig();
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent)
                .setSavingRunnable(ItemConfigs::save)
                .setTitle(Component.translatable("%s.config.title".formatted(Artifacts.MOD_ID)));

        ConfigCategory itemConfigs = builder.getOrCreateCategory(Component.translatable("%s.config.items.title".formatted(Artifacts.MOD_ID)));
        List<RegistrySupplier<Item>> items = new ArrayList<>();
        for (RegistrySupplier<Item> item : ModItems.ITEMS) {
            if (item.get() instanceof WearableArtifactItem) {
                items.add(item);
            }
        }
        items.sort(Comparator.comparing(item -> item.getId().getPath()));
        for (RegistrySupplier<Item> item : items) {
            if (item.get() instanceof WearableArtifactItem) {
                addItemConfigs(item.get(), builder, itemConfigs);
            }
        }

        return builder.build();
    }

    private static void addItemConfigs(Item item, ConfigBuilder builder, ConfigCategory category) {
        SubCategoryBuilder subCategory = builder.entryBuilder().startSubCategory(item.getDescription());
        for (String key : ItemConfigs.ITEM_TO_KEYS.get(BuiltInRegistries.ITEM.getKey(item))) {
            for (ValueType<?, ?> type : ItemConfigs.getValueTypes()) {
                if (ItemConfigs.getValues(type).containsKey(key)) {
                    Value.ConfigValue<?> value = ItemConfigs.getValues(type).get(key);
                    int tooltipCount = ItemConfigs.getTooltips(key).size();
                    Component[] tooltips = new Component[tooltipCount];
                    if (tooltipCount > 1) {
                        for (int i = 0; i < tooltips.length; i++) {
                            tooltips[i] = Component.translatable("%s.config.items.%s.description.%s".formatted(Artifacts.MOD_ID, key, i));
                        }
                    } else {
                        tooltips[0] = Component.translatable("%s.config.items.%s.description".formatted(Artifacts.MOD_ID, key));
                    }
                    String name = key.split("\\.")[1];
                    name = name.equals("cooldown") || name.equals("enabled") ? name : key;
                    Component title = Component.translatable("%s.config.items.%s.title".formatted(Artifacts.MOD_ID, name));
                    FieldBuilder<?, ?, ?> configEntry = value.type().createConfigEntry(builder.entryBuilder(), title, cast(value));
                    if (configEntry instanceof AbstractFieldBuilder<?,?,?> fieldBuilder) {
                        fieldBuilder.setTooltip(tooltips);
                    } else if (configEntry instanceof DropdownMenuBuilder<?> dropdownBuilder) {
                        dropdownBuilder.setTooltip(tooltips);
                    }
                    configEntry.build();
                    subCategory.add(configEntry.build());
                }
            }
        }
        category.addEntry(subCategory.build());
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object object) {
        return (T) object;
    }
}
