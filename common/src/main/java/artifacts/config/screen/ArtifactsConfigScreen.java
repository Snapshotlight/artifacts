package artifacts.config.screen;

import artifacts.Artifacts;
import artifacts.config.AbstractConfigManager;
import artifacts.config.ItemConfigs;
import artifacts.config.ItemConfigsManager;
import artifacts.config.value.Value;
import artifacts.registry.ModItems;
import dev.architectury.registry.registries.RegistrySupplier;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.AbstractFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.util.*;

public class ArtifactsConfigScreen {

    private final ConfigBuilder builder;

    private final Map<String, SubCategoryBuilder> subCategories = new HashMap<>();

    public ArtifactsConfigScreen(Screen parent) {
        builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("%s.config.title".formatted(Artifacts.MOD_ID)))
                .setSavingRunnable(() -> {
                    for (AbstractConfigManager config : Artifacts.CONFIG.configs) {
                        config.onConfigChanged();
                    }
                    ItemConfigsManager.INSTANCE.onConfigChanged();
                });
    }

    public Screen build() {
        Artifacts.CONFIG.configs.forEach(AbstractConfigManager::loadFromConfig);
        ItemConfigsManager.INSTANCE.loadFromConfig();

        for (AbstractConfigManager config : Artifacts.CONFIG.configs) {
            addConfigs(config);
        }

        ConfigCategory itemConfigs = builder.getOrCreateCategory(Component.translatable("%s.config.items.title".formatted(Artifacts.MOD_ID)));
        List<RegistrySupplier<Item>> items = new ArrayList<>();
        for (RegistrySupplier<Item> item : ModItems.ITEMS) {
            items.add(item);
        }
        items.sort(Comparator.comparing(item -> item.getId().getPath()));
        for (RegistrySupplier<Item> item : items) {
            addItemConfigs(item.get(), builder, itemConfigs);
        }

        subCategories.forEach((key, subCategory) -> {
            ConfigCategory category = builder.getOrCreateCategory(getTitle(key.split("\\.")[0]));
            category.addEntry(subCategory.build());
        });

        return builder.build();
    }

    private void addConfigs(AbstractConfigManager config) {
        ConfigCategory configBuilder = builder.getOrCreateCategory(getTitle(config.getName()));
        config.getValues().keySet().stream().sorted().forEach(key -> {
            String[] names = key.split("\\.");
            Value.ConfigValue<?> value = config.getValues().get(key);
            var field = createField(config, builder.entryBuilder(), config.getName(), key, value, config.getTooltips(key).size());
            if (names.length == 1) {
                configBuilder.addEntry(field);
            } else {
                SubCategoryBuilder subCategory = getSubCategory(config.getName() + '.' +  names[0]);
                subCategory.add(field);
            }
        });
    }

    private SubCategoryBuilder getSubCategory(String key) {
        if (subCategories.containsKey(key)) {
            return subCategories.get(key);
        }
        subCategories.put(key, builder.entryBuilder().startSubCategory(getTitle(key)));
        return subCategories.get(key);
    }

    private static void addItemConfigs(Item item, ConfigBuilder builder, ConfigCategory category) {
        SubCategoryBuilder subCategory = builder.entryBuilder().startSubCategory(item.getDescription());
        String id = BuiltInRegistries.ITEM.getKey(item).getPath();
        boolean hasField = false;
        for (String key : ItemConfigsManager.INSTANCE.getValues().keySet()) {
            if (key.startsWith(id)) {
                hasField = true;
                Value.ConfigValue<?> value = ItemConfigsManager.INSTANCE.getValues().get(key);
                subCategory.add(createField(ItemConfigsManager.INSTANCE, builder.entryBuilder(), "items", key, value, ItemConfigs.getTooltips(key).size()));
            }
        }
        if (hasField) {
            category.addEntry(subCategory.build());
        }
    }

    private static AbstractConfigListEntry<?> createField(AbstractConfigManager config, ConfigEntryBuilder builder, String categoryName, String key, Value.ConfigValue<?> value, int tooltipCount) {
        String[] names = key.split("\\.");
        key = categoryName + '.' + key;
        String name = names[names.length - 1];
        name = name.equals("cooldown") || name.equals("enabled") ? name : key;
        Component title = getTitle(name);
        Component[] tooltips = getTooltips(key, tooltipCount);
        FieldBuilder<?, ?, ?> configEntry = value.type().createConfigEntry(config, builder, title, cast(value));
        if (configEntry instanceof AbstractFieldBuilder<?,?,?> fieldBuilder) {
            fieldBuilder.setTooltip(tooltips);
        } else if (configEntry instanceof DropdownMenuBuilder<?> dropdownBuilder) {
            dropdownBuilder.setTooltip(tooltips);
        }
        return configEntry.build();
    }

    private static Component getTitle(String categoryKey) {
        return Component.translatable("%s.config.%s.title".formatted(Artifacts.MOD_ID, categoryKey));
    }

    private static Component[] getTooltips(String name, int count) {
        Component[] tooltips = new Component[count];
        if (count > 1) {
            for (int i = 0; i < tooltips.length; i++) {
                tooltips[i] = Component.translatable("%s.config.%s.description.%s".formatted(Artifacts.MOD_ID, name, i));
            }
        } else {
            tooltips[0] = Component.translatable("%s.config.%s.description".formatted(Artifacts.MOD_ID, name));
        }
        return tooltips;
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object object) {
        return (T) object;
    }
}
