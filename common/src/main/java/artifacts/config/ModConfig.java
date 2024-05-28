package artifacts.config;

import java.util.List;

public class ModConfig {

    public ClientConfig client = new ClientConfig();
    public GeneralConfig general = new GeneralConfig();
    public ItemConfigs items = new ItemConfigs();

    public final List<AbstractConfigManager> configs = List.of(general, client, items);

    public void setup() {
        configs.forEach(AbstractConfigManager::setup);
    }
}
