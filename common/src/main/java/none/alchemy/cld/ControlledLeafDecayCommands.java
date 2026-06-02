package none.alchemy.cld;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public final class ControlledLeafDecayCommands {
    private ControlledLeafDecayCommands() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("cld")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("reload")
                        .executes(context -> reloadConfig(context.getSource()))));
    }

    private static int reloadConfig(CommandSourceStack source) {
        try {
            Config.reload(source.getServer());
            source.sendSuccess(
                    () -> Component.literal("ControlledLeafDecay config reloaded from " + Config.loadedPath()),
                    true);
            Constants.LOG.info("Reloaded {} config from {}", Constants.MOD_ID, Config.loadedPath());
            return 1;
        } catch (Exception exception) {
            Constants.LOG.error("Failed to reload {} config", Constants.MOD_ID, exception);
            source.sendFailure(Component.literal("Failed to reload ControlledLeafDecay config: " + exception.getMessage()));
            return 0;
        }
    }
}
