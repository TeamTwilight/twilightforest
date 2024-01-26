package twilightforest.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.EventHooks;
import twilightforest.entity.boss.Naga;
import twilightforest.util.EntityUtil;

public class NagaSmashGoal extends Goal {

	private final Naga naga;

	public NagaSmashGoal(Naga naga) {
		this.naga = naga;
	}

	@Override
	public boolean canUse() {
		return this.naga.horizontalCollision && EventHooks.getMobGriefingEvent(this.naga.level(), this.naga);
	}

	@Override
	public void start() {
		// NAGA SMASH!
		if (this.naga.level().isClientSide()) return;

		AABB bb = this.naga.getBoundingBox();

		int minx = Mth.floor(bb.minX - 0.75D);
		int miny = Mth.floor(bb.minY + 1.01D);
		int minz = Mth.floor(bb.minZ - 0.75D);
		int maxx = Mth.floor(bb.maxX + 0.75D);
		int maxy = Mth.floor(bb.maxY + 0.0D);
		int maxz = Mth.floor(bb.maxZ + 0.75D);

		BlockPos min = new BlockPos(minx, miny, minz);
		BlockPos max = new BlockPos(maxx, maxy, maxz);

		if (this.naga.level().hasChunksAt(min, max)) {
			for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
				BlockState state = this.naga.level().getBlockState(pos);
				if (state.is(BlockTags.LEAVES) || (this.naga.shouldDestroyAllBlocks() && EntityUtil.canDestroyBlock(this.naga.level(), pos, this.naga))) {
					this.naga.level().destroyBlock(pos, !state.is(BlockTags.LEAVES));
				}
			}
		}
	}
}