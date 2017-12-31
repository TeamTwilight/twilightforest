package twilightforest.structures.finalcastle;

import net.minecraft.block.Block;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.RotationUtil;
import twilightforest.util.StructureBoundingBoxUtils;

import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleFoundation13 extends StructureTFComponentOld {
	protected int groundLevel = -1;

	public ComponentTFFinalCastleFoundation13() {
	}

	public ComponentTFFinalCastleFoundation13(TFFeature feature, Random rand, int i, StructureTFComponentOld sideTower) {
		super(feature, i);

		this.setCoordBaseMode(sideTower.getCoordBaseMode());
		this.boundingBox = new StructureBoundingBox(sideTower.getBoundingBox().minX - 2, sideTower.getBoundingBox().minY - 1, sideTower.getBoundingBox().minZ - 2, sideTower.getBoundingBox().maxX + 2, sideTower.getBoundingBox().minY, sideTower.getBoundingBox().maxZ + 2);

	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// offset bounding box to average ground level
		if (this.groundLevel < 0) {
			this.groundLevel = this.getDeadrockLevel(world, sbb);

			if (this.groundLevel < 0) {
				return true;
			}
		}

		// how tall are we
		int height = this.boundingBox.maxY - this.groundLevel;
		int mid = height / 2;

		// assume square
		int size = this.boundingBox.maxX - this.boundingBox.minX;

		for (Rotation rotation : RotationUtil.ROTATIONS) {
			// do corner
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 1, -1, 1, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 2, -1, 1, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 2, -mid, 0, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 1, -1, 2, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 0, -mid, 2, rotation, sbb);

			for (int x = 6; x < (size - 3); x += 4) {
				this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, x, -1, 1, rotation, sbb);
				this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, x, -mid, 0, rotation, sbb);
			}

		}

		return true;
	}

	/**
	 * Find what y level the local deadrock is.  Just check the center of the chunk we're given
	 */
	protected int getDeadrockLevel(World world, StructureBoundingBox sbb) {
		int groundLevel = 256;

		for (int y = 150; y > 0; y--) // is 150 a good place to start? :)
		{
			final Vec3i center = StructureBoundingBoxUtils.getCenter(sbb);

			int cx = center.getX();
			int cz = center.getZ();

			Block block = world.getBlockState(new BlockPos(cx, y, cz)).getBlock();
			if (block == TFBlocks.deadrock) {
				groundLevel = y;
				break;
			}
		}

		return groundLevel;
	}
}
