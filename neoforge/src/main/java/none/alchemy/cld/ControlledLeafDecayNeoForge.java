package none.alchemy.cld;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(Constants.MOD_ID)
public class ControlledLeafDecayNeoForge {
    public ControlledLeafDecayNeoForge(IEventBus modEventBus) {
        ControlledLeafDecay.init();
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        ControlledLeafDecayCommands.register(event.getDispatcher());
    }
}
