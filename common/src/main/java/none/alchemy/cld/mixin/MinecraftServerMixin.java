package none.alchemy.cld.mixin;

import java.util.function.BooleanSupplier;

import none.alchemy.cld.LeafDecayHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "tickServer", at = @At("TAIL"))
    private void controlledleafdecay$tickServer(BooleanSupplier hasTimeLeft, CallbackInfo callback) {
        LeafDecayHooks.serverTick((MinecraftServer) (Object) this);
    }
}
