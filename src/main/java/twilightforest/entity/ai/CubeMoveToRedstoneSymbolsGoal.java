package twilightforest.entity.ai;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import twilightforest.entity.RovingCube;

import java.util.EnumSet;

public class CubeMoveToRedstoneSymbolsGoal extends Goal {

	private final RovingCube myCube;
	private final double speed;
	private BlockPos targetPos;

	public CubeMoveToRedstoneSymbolsGoal(RovingCube entityTFRovingCube, double d) {
		this.myCube = entityTFRovingCube;
		this.speed = d;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.myCube.getRandom().nextInt(20) != 0) {
			return false;
		} else {
			BlockPos pos = this.searchForRedstoneSymbol(this.myCube, 16, 5);

			if (pos == null) {
				return false;
			} else {
				this.targetPos = pos;
				return true;
			}
		}
	}

	@Override
	public boolean canContinueToUse() {
		return !this.myCube.getNavigation().isDone();
	}

	@Override
	public void start() {
		this.myCube.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), this.speed);
	}

	/**
	 * Search the area for a redstone circle (8 redstone dust around a blank square)
	 */
	private BlockPos searchForRedstoneSymbol(RovingCube myCube2, int xzRange, int yRange) {
		BlockPos curPos = new BlockPos(myCube2.blockPosition());

		for (int x = -xzRange; x < xzRange; x++) {
			for (int z = -xzRange; z < xzRange; z++) {
				for (int y = -yRange; y < yRange; y++) {
					if (this.isRedstoneSymbol(curPos.offset(x, y, z))) {
						this.myCube.hasFoundSymbol = true;
						this.myCube.symbolX = curPos.getX() + x;
						this.myCube.symbolY = curPos.getY() + y;
						this.myCube.symbolZ = curPos.getZ() + z;

						return curPos.offset(x, y, z);
					}
				}
			}
		}

		return null;
	}

	private boolean isRedstoneSymbol(BlockPos pos) {
		if (!this.myCube.level.hasChunkAt(pos) || !this.myCube.level.isEmptyBlock(pos)) {
			return false;
		} else {
			// we found an air block, is it surrounded by redstone?
			for (Direction e : Direction.values()) {
				if (this.myCube.level.getBlockState(pos.relative(e)).getBlock() != Blocks.REDSTONE_WIRE) {
					return false;
				}
			}

			return true;
		}
	}

}
