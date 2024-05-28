package artifacts.config;

import java.util.List;

public class ModConfig {

    public ClientConfigManager client = new ClientConfigManager();
    public GeneralConfigManager general = new GeneralConfigManager();

    public final List<AbstractConfigManager> configs = List.of(general, client, ItemConfigsManager.INSTANCE);

    public void setup() {
        general.setup();
        client.setup();
        ItemConfigsManager.INSTANCE = new ItemConfigsManager();
        ItemConfigsManager.INSTANCE.buildSpec();
        ItemConfigsManager.INSTANCE.setup();
    }
}
