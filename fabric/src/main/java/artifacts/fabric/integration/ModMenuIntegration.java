package artifacts.fabric.integration;

import artifacts.config.screen.ArtifactsConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ArtifactsConfigScreen::createScreen;
    }
}
