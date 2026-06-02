package none.alchemy.cld;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ControlledLeafDecayForge {
    public ControlledLeafDecayForge() {
        ControlledLeafDecay.init();
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        ControlledLeafDecayCommands.register(event.getDispatcher());
    }
}
