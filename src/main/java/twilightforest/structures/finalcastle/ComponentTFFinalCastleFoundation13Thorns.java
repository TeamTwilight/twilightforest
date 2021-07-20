package twilightforest.structures.finalcastle;

import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.RotationUtil;

import java.util.Random;

/**
 * Foundation that makes thorns go all through the tower
 *
 * @author benma_000
 */
public class ComponentTFFinalCastleFoundation13Thorns extends ComponentTFFinalCastleFoundation13 {

	public ComponentTFFinalCastleFoundation13Thorns(TemplateManager manager, CompoundNBT nbt) {
		super(TFFinalCastlePieces.TFFCFTh21, nbt);
	}

	public ComponentTFFinalCastleFoundation13Thorns(TFFeature feature, Random rand, int i, StructureTFComponentOld sideTower) {
		super(TFFinalCastlePieces.TFFCFTh21, feature, rand, i, sideTower);

		this.boundingBox = new MutableBoundingBox(sideTower.getBoundingBox().minX - 5, sideTower.getBoundingBox().maxY - 1, sideTower.getBoundingBox().minZ - 5, sideTower.getBoundingBox().maxX + 5, sideTower.getBoundingBox().maxY, sideTower.getBoundingBox().maxZ + 5);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// thorns
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

		for (Rotation i : RotationUtil.ROTATIONS) {
			this.makeThornVine(world, decoRNG, i, sbb);
		}

		return true;
	}

	private void makeThornVine(ISeedReader world, Random decoRNG, Rotation rotation, MutableBoundingBox sbb) {

		int x = 3 + decoRNG.nextInt(13);
		int z = 3 + decoRNG.nextInt(13);

		int y = this.boundingBox.getYSize() + 5;

		int twist = decoRNG.nextInt(4);
		int twistMod = 3 + decoRNG.nextInt(3);

		final BlockState thorns = TFBlocks.brown_thorns.get().getDefaultState();

		while (this.getBlockStateFromPosRotated(world, x, y, z, sbb, rotation).getBlock() != TFBlocks.deadrock.get() && this.getYWithOffset(y) > 60) {
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

	private void makeThornBranch(ISeedReader world, int x, int y, int z, Rotation rotation, MutableBoundingBox sbb) {
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
				BlockState thorns = TFBlocks.green_thorns.get().getDefaultState()
						.with(
								RotatedPillarBlock.AXIS,
								add == Rotation.NONE || add == Rotation.CLOCKWISE_180 ? Direction.Axis.X : Direction.Axis.Z
						);
				if (i > 0) {
					this.setBlockStateRotated(world, thorns, x + (dx * i), y, z + (dz * i), rotation, sbb);
				}
				// go up that far
				this.setBlockStateRotated(world, thorns.with(RotatedPillarBlock.AXIS, Direction.Axis.Y), destX, y + i, destZ, rotation, sbb);
				// go back half that far
				if (i > (dist / 2)) {
					this.setBlockStateRotated(world, thorns, x + (dx * i), y + dist - 1, z + (dz * i), rotation, sbb);
				}
			}
		}
	}
}
