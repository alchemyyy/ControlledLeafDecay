package none.alchemy.cld;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;

public final class LeafDecayScheduler {
    public static final LeafDecayScheduler INSTANCE = new LeafDecayScheduler();

    private final List<BlockUpdate> plannedUpdates = new ArrayList<>();
    private final List<BlockUpdate> scheduledUpdates = new ArrayList<>();

    private LeafDecayScheduler() {
    }

    public void schedule(ServerLevel level, BlockPos pos, int delay) {
        plannedUpdates.add(new BlockUpdate(level, pos.immutable(), delay));
    }

    public void tick() {
        if (!plannedUpdates.isEmpty()) {
            scheduledUpdates.addAll(plannedUpdates);
            plannedUpdates.clear();
        }

        Iterator<BlockUpdate> iterator = scheduledUpdates.iterator();
        while (iterator.hasNext()) {
            BlockUpdate scheduledUpdate = iterator.next();
            scheduledUpdate.ticksRemaining--;

            if (scheduledUpdate.ticksRemaining > 0) {
                continue;
            }

            iterator.remove();

            ServerLevel level = scheduledUpdate.level.get();
            if (level == null || !level.hasChunkAt(scheduledUpdate.pos)) {
                continue;
            }

            BlockState state = level.getBlockState(scheduledUpdate.pos);
            if (state.is(BlockTags.LEAVES)) {
                state.randomTick(level, scheduledUpdate.pos, level.getRandom());
            }
        }
    }

    private static final class BlockUpdate {
        private final WeakReference<ServerLevel> level;
        private final BlockPos pos;
        private int ticksRemaining;

        private BlockUpdate(ServerLevel level, BlockPos pos, int ticksRemaining) {
            this.level = new WeakReference<>(level);
            this.pos = pos;
            this.ticksRemaining = ticksRemaining;
        }
    }
}
