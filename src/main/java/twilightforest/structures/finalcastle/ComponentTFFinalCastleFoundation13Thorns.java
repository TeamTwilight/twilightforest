package twilightforest.structures.finalcastle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFThorns;
import twilightforest.block.TFBlocks;
import twilightforest.enums.ThornVariant;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.RotationUtil;

import java.util.Random;

/**
 * Foundation that makes thorns go all through the tower
 *
 * @author benma_000
 */
public class ComponentTFFinalCastleFoundation13Thorns extends ComponentTFFinalCastleFoundation13 {
	public ComponentTFFinalCastleFoundation13Thorns() {
	}

	public ComponentTFFinalCastleFoundation13Thorns(TFFeature feature, Random rand, int i, StructureTFComponentOld sideTower) {
		super(feature, rand, i, sideTower);

		this.boundingBox = new StructureBoundingBox(sideTower.getBoundingBox().minX - 5, sideTower.getBoundingBox().maxY - 1, sideTower.getBoundingBox().minZ - 5, sideTower.getBoundingBox().maxX + 5, sideTower.getBoundingBox().maxY, sideTower.getBoundingBox().maxZ + 5);

	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// thorns
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

		for (Rotation i : RotationUtil.ROTATIONS) {
			this.makeThornVine(world, decoRNG, i, sbb);
		}

		return true;
	}

	private void makeThornVine(World world, Random decoRNG, Rotation rotation, StructureBoundingBox sbb) {

		int x = 3 + decoRNG.nextInt(13);
		int z = 3 + decoRNG.nextInt(13);

		int y = this.boundingBox.getYSize() + 5;

		int twist = decoRNG.nextInt(4);
		int twistMod = 3 + decoRNG.nextInt(3);

		final IBlockState thorns = TFBlocks.thorns.getDefaultState();

		while (this.getBlockStateFromPosRotated(world, x, y, z, sbb, rotation) != TFBlocks.deadrock && this.getYWithOffset(y) > 60) {
			this.setBlockStateRotated(world, thorns, x, y, z, rotation, sbb);
			// twist vines around the center block
			switch (twist) {
				case 0:
					this.setBlockStateRotated(world, thorns, x + 1, y, z, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x, y, z + 1, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x + 1, y, z + 1, rotation, sbb);
					break;
				case 1:
					this.setBlockStateRotated(world, thorns, x + 1, y, z, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x, y, z - 1, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x + 1, y, z - 1, rotation, sbb);
					break;
				case 2:
					this.setBlockStateRotated(world, thorns, x - 1, y, z, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x, y, z - 1, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x - 1, y, z - 1, rotation, sbb);
					break;
				case 3:
					this.setBlockStateRotated(world, thorns, x - 1, y, z, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x, y, z + 1, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x - 1, y, z + 1, rotation, sbb);
					break;
			}

			if (Math.abs(y % twistMod) == 1) {
				// make branch
				this.makeThornBranch(world, x, y, z, rotation, sbb);
			}

			// twist randomly
			if (y % twistMod == 0) {
				twist++;
				twist = twist % 4;
			}

			y--;
		}
	}

	private void makeThornBranch(World world, int x, int y, int z, Rotation rotation, StructureBoundingBox sbb) {
		Random rand = new Random(world.getSeed() + (x * 321534781) ^ (y * 756839) + z);

		// pick a direction
		Rotation dir = RotationUtil.getRandomRotation(rand);

		// initialize direction variables
		int dx = 0;
		int dz = 0;

		switch (dir) {
			case NONE:
				dx = +1;
				break;
			case CLOCKWISE_90:
				dz = +1;
				break;
			case CLOCKWISE_180:
				dx = -1;
				break;
			case COUNTERCLOCKWISE_90:
				dz = -1;
				break;
		}

		// how far do we branch?
		int dist = 2 + rand.nextInt(3);

		// check to make sure there's room
		int destX = x + (dist * dx);
		int destZ = z + (dist * dz);

		if (destX > 0 && destX < this.boundingBox.getXSize() && destZ > 0 && destZ < this.boundingBox.getZSize()) {
			for (int i = 0; i < dist; i++) {
				// go out that far
				final Rotation add = dir.add(rotation).add(this.rotation);
				IBlockState thorns = TFBlocks.thorns.getDefaultState()
						.withProperty(BlockTFThorns.VARIANT, ThornVariant.GREEN)
						.withProperty(
								BlockTFThorns.AXIS,
								add == Rotation.NONE || add == Rotation.CLOCKWISE_180 ? EnumFacing.Axis.X : EnumFacing.Axis.Z
						);
				if (i > 0) {
					this.setBlockStateRotated(world, thorns, x + (dx * i), y, z + (dz * i), rotation, sbb);
				}
				// go up that far
				this.setBlockStateRotated(world, thorns.withProperty(BlockTFThorns.AXIS, EnumFacing.Axis.Y), destX, y + i, destZ, rotation, sbb);
				// go back half that far
				if (i > (dist / 2)) {
					this.setBlockStateRotated(world, thorns, x + (dx * i), y + dist - 1, z + (dz * i), rotation, sbb);
				}


			}


		}

	}


}
