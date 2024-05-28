package artifacts.config;

import java.util.List;

public class ModConfig {

    public ClientConfigManager client = new ClientConfigManager();
    public GeneralConfigManager general = new GeneralConfigManager();
    public ItemConfigsManager items = new ItemConfigsManager();

    public final List<AbstractConfigManager> configs = List.of(general, client, items);

    public void setup() {
        general.setup();
        client.setup();
        items.setup();
    }
}
