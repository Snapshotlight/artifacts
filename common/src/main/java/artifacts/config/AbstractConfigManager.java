package artifacts.config;

import artifacts.Artifacts;
import artifacts.config.value.Value;
import artifacts.config.value.type.ValueType;
import artifacts.platform.PlatformServices;
import com.electronwill.nightconfig.core.ConfigSpec;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileWatcher;
import com.electronwill.nightconfig.core.io.ParsingException;
import com.electronwill.nightconfig.core.io.WritingMode;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

// see Neoforge ConfigFileTypeHandler
public abstract class AbstractConfigManager {

    protected CommentedFileConfig config;
    protected ConfigSpec spec = new ConfigSpec();
    private final Path configPath;
    private final String name;

    protected AbstractConfigManager(String fileName) {
        this.name = fileName;
        this.configPath = Path.of(Artifacts.MOD_ID, "%s.toml".formatted(fileName));
    }

    protected void setup() {
        config = CommentedFileConfig.builder(PlatformServices.platformHelper.getConfigDir().resolve(configPath))
                .sync()
                .preserveInsertionOrder()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        Path path = PlatformServices.platformHelper.getConfigDir().resolve(configPath);

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

    private void correctConfigAndSave() {
        addMissingKeys();
        spec.correct(config);
        config.save();
    }

    public String getName() {
        return name;
    }

    public <T, C> T get(ValueType<T, C> type, String key) {
        return type.read(config.get(key));
    }

    public <T, C> void set(ValueType<T, C> type, String key, T value) {
        config.set(key, type.write(value));
    }

    protected <T> void reset(String key, Value.ConfigValue<T> value) {
        config.add(key, value.type().write(value.getDefaultValue()));
    }

    protected <T> void loadFromConfig(String key, Value.ConfigValue<T> value) {
        value.set(get(value.type(), key));
    }

    public void loadFromConfig() {
        getValues().forEach(this::loadFromConfig);
    }

    protected void addMissingKeys() {
        Map<String, Value.ConfigValue<?>> values = getValues();

        List<String> keys = new ArrayList<>(values.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            if (!config.contains(key)) {
                Value.ConfigValue<?> value = values.get(key);
                reset(key, value);
                StringBuilder builder = new StringBuilder();
                for (String tooltip : getTooltips(key)) {
                    builder.append(tooltip).append('\n');
                }
                builder.append(value.type().getAllowedValuesComment());
                config.setComment(key, builder.toString());
            }
        }
    }

    public abstract List<String> getTooltips(String key);

    public abstract Map<String, Value.ConfigValue<?>> getValues();

    public void onConfigChanged() {
        getValues().forEach(this::loadFromConfig);
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
            onConfigChanged();
        }
    }
}
