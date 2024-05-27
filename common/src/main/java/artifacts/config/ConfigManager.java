package artifacts.config;

import artifacts.Artifacts;
import artifacts.config.value.Value;
import artifacts.config.value.type.NumberValueType;
import artifacts.config.value.type.StringRepresentableValueType;
import artifacts.config.value.type.ValueType;
import artifacts.platform.PlatformServices;
import com.electronwill.nightconfig.core.ConfigSpec;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileWatcher;
import com.electronwill.nightconfig.core.io.ParsingException;
import com.electronwill.nightconfig.core.io.WritingMode;
import dev.architectury.utils.GameInstance;
import net.minecraft.util.StringRepresentable;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// see Neoforge ConfigFileTypeHandler
public class ConfigManager {

    private static ConfigManager ITEMS;

    private final CommentedFileConfig config;
    private final ConfigSpec spec = createConfigSpec();
    private final Path configPath;

    private ConfigManager(String fileName) {
        this.configPath = Path.of(Artifacts.MOD_ID, "%s.toml".formatted(fileName));
        Path path = PlatformServices.platformHelper.getConfigDir().resolve(configPath);
        config = CommentedFileConfig.builder(PlatformServices.platformHelper.getConfigDir().resolve(configPath))
                .sync()
                .preserveInsertionOrder()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        try {
            config.load();
        } catch (ParsingException exception) {
            Artifacts.LOGGER.warn("Failed to load config file {}, attempting to recreate", configPath);
            try {
                backUpConfig(config.getNioPath(), 5);
                Files.delete(config.getNioPath());
                config.load();
            } catch (Throwable t) {
                exception.addSuppressed(t);
                throw new RuntimeException("Failed to load config file " + configPath, exception);
            }
        }

        if (!spec.isCorrect(config)) {
            correctConfigAndSave();
        }

        Artifacts.LOGGER.debug("Loaded config file {}", configPath);
        try {
            FileWatcher.defaultInstance().addWatch(path, new ConfigWatcher());
            Artifacts.LOGGER.debug("Watching config file {} for changes", configPath);
        } catch (IOException exception) {
            throw new RuntimeException("Couldn't watch config file", exception);
        }
    }

    public static void setup() {
        ITEMS = new ConfigManager("items");
    }

    private void correctConfigAndSave() {
        addMissingKeys();
        spec.correct(config);
        config.save();
    }

    private void addMissingKeys() {
        List<String> keys = new ArrayList<>();
        for (ValueType<?, ?> type : ItemConfigs.getValueTypes()) {
            keys.addAll(ItemConfigs.getValues(type).keySet());
        }
        Collections.sort(keys);
        for (String key : keys) {
            for (ValueType<?, ?> type : ItemConfigs.getValueTypes()) {
                if (ItemConfigs.getValues(type).containsKey(key)) {
                    if (!config.contains(key)) {
                        config.add(key, ItemConfigs.getValues(type).get(key).getDefaultValue());
                        StringBuilder builder = new StringBuilder();
                        for (String tooltip : ItemConfigs.TOOLTIPS.get(key)) {
                            builder.append(tooltip).append('\n');
                        }
                        builder.append(type.getAllowedValuesComment());
                        config.setComment(key, builder.toString());
                    }
                }
            }
        }
    }

    private ConfigSpec createConfigSpec() {
        ConfigSpec spec = new ConfigSpec();
        for (ValueType<?, ?> type : ItemConfigs.getValueTypes()) {
            for (String key : ItemConfigs.getValues(type).keySet()) {
                addToSpec(spec, key, type);
            }
        }
        return spec;
    }

    private <T> void addToSpec(ConfigSpec spec, String key, ValueType<T, ?> type) {
        if (type instanceof NumberValueType<?> numberValueType) {
            defineNumber(spec, key, numberValueType);
        } else if (type instanceof StringRepresentableValueType<?> stringRepresentableValueType) {
            defineStringRepresentable(spec, key, stringRepresentableValueType);
        } else {
            defineValue(spec, key, type);
        }
    }

    private <T extends Number & Comparable<T>> void defineNumber(ConfigSpec spec, String key, NumberValueType<T> type) {
        Value.ConfigValue<T> value = ItemConfigs.getValues(type).get(key);
        spec.defineInRange(key, value.getDefaultValue(), type.getMin(), type.getMax());
    }

    private <T extends StringRepresentable> void defineStringRepresentable(ConfigSpec spec, String key, StringRepresentableValueType<T> type) {
        Value.ConfigValue<T> value = ItemConfigs.getValues(type).get(key);
        List<String> allowedValues = new ArrayList<>();
        allowedValues.addAll(type.getValues().stream().map(StringRepresentable::getSerializedName).toList());
        allowedValues.addAll(type.getValues().stream().map(StringRepresentable::getSerializedName).map(String::toUpperCase).toList());
        spec.defineInList(key, value.getDefaultValue().getSerializedName(), allowedValues);
    }

    private <T> void defineValue(ConfigSpec spec, String key, ValueType<T, ?> type) {
        Value.ConfigValue<T> value = ItemConfigs.getValues(type).get(key);
        spec.define(key, value.getDefaultValue());
    }

    public static <T> T getConfigValue(String key) {
        return ITEMS.config.get(key);
    }

    public static void backUpConfig(final Path commentedFileConfig, final int maxBackups) {
        Path bakFileLocation = commentedFileConfig.getParent();
        String bakFileName = FilenameUtils.removeExtension(commentedFileConfig.getFileName().toString());
        String bakFileExtension = FilenameUtils.getExtension(commentedFileConfig.getFileName().toString()) + ".bak";
        Path bakFile = bakFileLocation.resolve(bakFileName + "-1" + "." + bakFileExtension);
        try {
            for (int i = maxBackups; i > 0; i--) {
                Path oldBak = bakFileLocation.resolve(bakFileName + "-" + i + "." + bakFileExtension);
                if (Files.exists(oldBak)) {
                    if (i >= maxBackups)
                        Files.delete(oldBak);
                    else
                        Files.move(oldBak, bakFileLocation.resolve(bakFileName + "-" + (i + 1) + "." + bakFileExtension));
                }
            }
            Files.copy(commentedFileConfig, bakFile);
        } catch (IOException exception) {
            Artifacts.LOGGER.warn("Failed to back up config file {}", commentedFileConfig, exception);
        }
    }

    // See neoforge ConfigFileTypeHandler
    private class ConfigWatcher implements Runnable {

        @Override
        public void run() {
            try {
                config.load();
                if (!spec.isCorrect(config)) {
                    Artifacts.LOGGER.warn("Configuration file {} is not correct. Correcting", configPath);
                    correctConfigAndSave();
                }
            } catch (ParsingException exception) {
                throw new RuntimeException("Failed to load config file " + configPath, exception);
            }
            Artifacts.LOGGER.info("Config file {} changed", configPath);
            if (GameInstance.getServer() != null) {
                ItemConfigs.loadFromConfig(GameInstance.getServer());
            }
        }
    }
}
