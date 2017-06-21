package twilightforest.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import twilightforest.entity.EntityTFRovingCube;

public class EntityAICubeMoveToRedstoneSymbols extends EntityAIBase {

	private final EntityTFRovingCube myCube;
	private final double speed;
	private BlockPos targetPos;

	public EntityAICubeMoveToRedstoneSymbols(EntityTFRovingCube entityTFRovingCube, double d) {
		this.myCube = entityTFRovingCube;
		this.speed = d;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (this.myCube.getRNG().nextInt(20) != 0) {
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
	public boolean shouldContinueExecuting() {
		return !this.myCube.getNavigator().noPath();
	}

	@Override
	public void startExecuting() {
		this.myCube.getNavigator().tryMoveToXYZ(targetPos.getX(), targetPos.getY(), targetPos.getZ(), this.speed);
	}

	/**
	 * Search the area for a redstone circle (8 redstone dust around a blank square)
	 */
	private BlockPos searchForRedstoneSymbol(EntityTFRovingCube myCube2, int xzRange, int yRange) {
		BlockPos curPos = new BlockPos(myCube2);

		for (int x = -xzRange; x < xzRange; x++) {
			for (int z = -xzRange; z < xzRange; z++) {
				for (int y = -yRange; y < yRange; y++) {
					if (this.isRedstoneSymbol(curPos.add(x, y, z))) {

						//System.out.println("Cube found symbol at " + x + ", " + y + ", " + z);

						this.myCube.hasFoundSymbol = true;
						this.myCube.symbolX = curPos.getX() + x;
						this.myCube.symbolY = curPos.getY() + y;
						this.myCube.symbolZ = curPos.getZ() + z;

						return curPos.add(x, y, z);
					}
				}
			}
		}

		return null;
	}

	private boolean isRedstoneSymbol(BlockPos pos) {
		if (!this.myCube.world.isBlockLoaded(pos) || !this.myCube.world.isAirBlock(pos)) {
			return false;
		} else {
			// we found an air block, is it surrounded by redstone?
			for (EnumFacing e : EnumFacing.VALUES) {
				if (this.myCube.world.getBlockState(pos.offset(e)).getBlock() != Blocks.REDSTONE_WIRE) {
					return false;
				}
			}

			return true;
		}
	}

}
