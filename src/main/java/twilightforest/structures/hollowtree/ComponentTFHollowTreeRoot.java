package twilightforest.structures.hollowtree;

import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.TFGenerator;

import java.util.Random;

import static net.minecraft.block.BlockLog.LOG_AXIS;

public class ComponentTFHollowTreeRoot extends ComponentTFHollowTreeMedBranch {

	private int groundLevel = -1;


	public ComponentTFHollowTreeRoot() {
		super();
	}

	public ComponentTFHollowTreeRoot(TFFeature feature, int i, int sx, int sy, int sz, double length, double angle, double tilt, boolean leafy) {
		super(feature, i, sx, sy, sz, length, angle, tilt, leafy);
		this.boundingBox = new StructureBoundingBox(Math.min(src.getX(), dest.getX()), Math.min(src.getY(), dest.getY()), Math.min(src.getZ(), dest.getZ()), Math.max(src.getX(), dest.getX()), Math.max(src.getY(), dest.getY()), Math.max(src.getZ(), dest.getZ()));
	}


	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb, boolean drawLeaves) {
		if (!drawLeaves)
		{
			// offset bounding box to average ground level
			if (this.groundLevel < 0)
			{
				this.groundLevel = this.getSampledDirtLevel(world, sbb);

				if (this.groundLevel < 0)
				{
					return true;
				}

				src = new BlockPos(src.getX(), groundLevel + 5, src.getZ());
			}

			BlockPos rSrc = src.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);
			BlockPos rDest = dest.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);

			drawRootLine(world, sbb, rSrc.getX(), rSrc.getY(), rSrc.getZ(), rDest.getX(), rDest.getY(), rDest.getZ(), TFBlocks.root.getDefaultState());
			drawRootLine(world, sbb, rSrc.getX(), rSrc.getY() - 1, rSrc.getZ(), rDest.getX(), rDest.getY() - 1, rDest.getZ(), TFBlocks.root.getDefaultState());
		}

		return true;
	}


	/**
	 * Draws a line
	 */
	protected void drawRootLine(World world, StructureBoundingBox sbb, int x1, int y1, int z1, int x2, int y2, int z2, IBlockState blockValue) {
		BlockPos lineCoords[] = TFGenerator.getBresehnamArrays(x1, y1, z1, x2, y2, z2);

		for (BlockPos coords : lineCoords) {
			IBlockState block = this.getBlockStateFromPos(world, coords.getX(), coords.getY(), coords.getZ(), sbb);

			// three choices here
			if (!block.isNormalCube() || block.getBlock() != Blocks.AIR && block.getMaterial() == Material.GRASS) {

				// air, other non-solid, or grass, make wood block
				IBlockState log = TFBlocks.twilight_log.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
				this.setBlockState(world, log, coords.getX(), coords.getY(), coords.getZ(), sbb);
			} else if (block.getBlock() != Blocks.AIR && block.getMaterial() == Material.WOOD) {
				// wood, do nothing
			} else {
				// solid, make root block
				this.setBlockState(world, blockValue, coords.getX(), coords.getY(), coords.getZ(), sbb);
			}
		}
	}
}
