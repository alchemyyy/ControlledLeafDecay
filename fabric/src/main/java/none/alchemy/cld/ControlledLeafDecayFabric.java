package none.alchemy.cld;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ControlledLeafDecayFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ControlledLeafDecay.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ControlledLeafDecayCommands.register(dispatcher));
    }
}
