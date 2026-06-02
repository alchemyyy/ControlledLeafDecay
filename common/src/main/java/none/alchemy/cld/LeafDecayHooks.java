package none.alchemy.cld;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class LeafDecayHooks {
    private LeafDecayHooks() {
    }

    public static void serverTick(MinecraftServer server) {
        Config.ensureLoaded(server);
        LeafDecayScheduler.INSTANCE.tick();
    }

    public static void blockChanged(Level level, BlockPos pos, BlockState newState) {
        if (!newState.isAir() || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        for (Direction direction : Direction.values()) {
            BlockPos leafPos = pos.relative(direction);
            if (!serverLevel.hasChunkAt(leafPos) || !serverLevel.getBlockState(leafPos).is(BlockTags.LEAVES)) {
                continue;
            }

            LeafDecayScheduler.INSTANCE.schedule(serverLevel, leafPos, Config.randomDecayDelay(serverLevel.getRandom()));
        }
    }
}
