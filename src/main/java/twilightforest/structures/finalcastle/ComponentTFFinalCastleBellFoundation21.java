package twilightforest.structures.finalcastle;

import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.structures.StructureTFComponent;
import twilightforest.util.RotationUtil;

import java.util.Random;

/**
 * A larger foundation that comes all the way from the top of a tower
 *
 * @author benma_000
 */
public class ComponentTFFinalCastleBellFoundation21 extends ComponentTFFinalCastleFoundation13 {
	public ComponentTFFinalCastleBellFoundation21() {
	}

	public ComponentTFFinalCastleBellFoundation21(Random rand, int i, StructureTFComponent sideTower) {
		super(rand, i, sideTower);

		this.boundingBox = new StructureBoundingBox(sideTower.getBoundingBox().minX - 2, sideTower.getBoundingBox().maxY - 1, sideTower.getBoundingBox().minZ - 2, sideTower.getBoundingBox().maxX + 2, sideTower.getBoundingBox().maxY, sideTower.getBoundingBox().maxZ + 2);

	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// find ground level
		if (this.groundLevel < 0) {
			this.groundLevel = this.getDeadrockLevel(world, sbb);
		}

		int mid = 16;
		int low = 32;

		// assume square
		int size = this.boundingBox.maxX - this.boundingBox.minX;

		for (Rotation rotation : RotationUtil.ROTATIONS) {
			// do corner
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 2, -1, 1, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 2, -mid, 0, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 1, -1, 2, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 0, -mid, 2, rotation, sbb);

			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 1, -low, 1, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 0, -low, 1, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 1, -low, 0, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 0, -low, 0, rotation, sbb);

			for (int x = 6; x < (size - 3); x += 4) {
				this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, x, -1, 1, rotation, sbb);
				this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, x, -mid, 0, rotation, sbb);
			}

		}

		return true;
	}


}
