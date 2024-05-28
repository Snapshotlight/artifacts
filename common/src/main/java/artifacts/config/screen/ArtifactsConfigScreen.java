package artifacts.config.screen;

import artifacts.Artifacts;
import artifacts.config.AbstractConfigManager;
import artifacts.config.value.Value;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.impl.builders.AbstractFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

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
                });
    }

    public Screen build() {
        Artifacts.CONFIG.configs.forEach(AbstractConfigManager::loadFromConfig);

        for (AbstractConfigManager config : Artifacts.CONFIG.configs) {
            addConfigs(config);
        }

        subCategories.keySet().stream().sorted().forEach(key -> {
            ConfigCategory category = builder.getOrCreateCategory(getTitle(key.split("\\.")[0]));
            category.addEntry(subCategories.get(key).build());
        });

        return builder.build();
    }

    private void addConfigs(AbstractConfigManager config) {
        ConfigCategory configBuilder = builder.getOrCreateCategory(getTitle(config.getName()));
        config.getValues().keySet().stream().sorted().forEach(key -> {
            String[] names = key.split("\\.");
            Value.ConfigValue<?> value = config.getValues().get(key);
            var field = createField(config, config.getName(), key, value, config.getTooltips(key).size());
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

    private AbstractConfigListEntry<?> createField(AbstractConfigManager config, String categoryName, String key, Value.ConfigValue<?> value, int tooltipCount) {
        String[] names = key.split("\\.");
        key = categoryName + '.' + key;
        String name = names[names.length - 1];
        name = name.equals("cooldown") || name.equals("enabled") ? name : key;
        Component[] tooltips = getTooltips(key, tooltipCount);
        FieldBuilder<?, ?, ?> configEntry = createConfigEntry(config, value, getTitle(name));
        if (configEntry instanceof AbstractFieldBuilder<?,?,?> fieldBuilder) {
            fieldBuilder.setTooltip(tooltips);
        } else if (configEntry instanceof DropdownMenuBuilder<?> dropdownBuilder) {
            dropdownBuilder.setTooltip(tooltips);
        }
        return configEntry.build();
    }

    private <T> FieldBuilder<?, ?, ?> createConfigEntry(AbstractConfigManager config, Value.ConfigValue<T> value, Component title) {
        return ConfigEntries.createConfigEntry(config, value, builder.entryBuilder(), title);
    }

    private static Component getTitle(String categoryKey) {
        String name = categoryKey.substring(categoryKey.lastIndexOf('.') + 1);
        if (ResourceLocation.isValidPath(name) && BuiltInRegistries.ITEM.containsKey(Artifacts.id(name))) {
            return BuiltInRegistries.ITEM.get(Artifacts.id(name)).getDescription();
        }
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
}
